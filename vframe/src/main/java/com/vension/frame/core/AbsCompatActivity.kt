package com.vension.frame.core

import Constant
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.FrameLayout
import butterknife.ButterKnife
import butterknife.Unbinder
import com.gw.swipeback.SwipeBackLayout
import com.tbruyelle.rxpermissions2.RxPermissions
import com.vension.frame.R
import com.vension.frame.cache.PageCache
import com.vension.frame.dialog.LoadingDialog
import com.vension.frame.event.NetworkChangeEvent
import com.vension.frame.receiver.NetworkChangeReceiver
import com.vension.frame.utils.KeyBoardUtil
import com.vension.frame.utils.PreferenceUtil
import com.vension.frame.views.MultiStatusLayout
import com.wuhenzhizao.titlebar.statusbar.StatusBarUtils
import com.wuhenzhizao.titlebar.utils.KeyboardConflictCompat
import com.wuhenzhizao.titlebar.widget.CommonTitleBar
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/10/29 11:59
 * 描  述：Activity-基类
 * ========================================================
 */

 abstract class AbsCompatActivity : AppCompatActivity(), IActivity {

    val TAG = this.javaClass.simpleName //获取上下文并设置log标记

    /**
     * 在使用自定义toolbar时候的根布局 = toolBarView+childView
     */
    private var rootView: View? = null
    /**
     * 多状态的Layout
     */
    protected var actMultiStatusLayout: MultiStatusLayout? = null
    /**
     * 6.0动态获取权限
     */
    protected lateinit var mRxPermissions: RxPermissions
    protected var disposable: Disposable? = null
    /**
     * butterknife Unbinder
     */
    private lateinit var mUnBinder: Unbinder
    /**
     * 是否登录缓存
     */
    protected var isLogin: Boolean by PreferenceUtil(Constant.KEY_LOGIN, false)
    /**
     * 缓存上一次的网络状态
     */
    private var hasNetwork: Boolean by PreferenceUtil(Constant.KEY_HAS_NETWORK, true)
    /**
     * 网络状态变化的广播
     */
    private var mNetworkChangeReceiver: NetworkChangeReceiver? = null
    /**
     * 提示View
     */
    protected lateinit var mTipView: View
    protected lateinit var mWindowManager: WindowManager
    protected lateinit var mLayoutParams: WindowManager.LayoutParams

    /**
     * LoadingDialog
     */
    protected val mLoadingDialog: LoadingDialog by lazy { LoadingDialog.Builder(this).setCancelable(false).setCancelOutside(false).create() }


    /**
     * 重连点击监听
     */
    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        requestApi()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        KeyboardConflictCompat.assistWindow(window)//解决软键盘bug
        StatusBarUtils.transparentStatusBar(window)//代码设置透明状态栏
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onCreate(savedInstanceState)
        try {
            this.initContentView(attachLayoutRes())//加载布局
            mUnBinder = ButterKnife.bind(this)
            mRxPermissions = RxPermissions(this)

            //设置ToolBar
            if (showToolBar()){
                val mCommonTitleBar = rootView?.findViewById<View>(R.id.commonTitleBar) as CommonTitleBar//子布局容器
                initToolBar(mCommonTitleBar)
            }

            //是否支持侧滑返回
            if (isSupportSwipeBack()){
                val mSwipeBackLayout = SwipeBackLayout(this)
                mSwipeBackLayout.isSwipeFromEdge = true
                mSwipeBackLayout.attachToActivity(this)
            }

            //开启事件总线
            if (useEventBus()) {
                EventBus.getDefault().register(this)
            }

            initTipView()
            //初始化view和数据
            initViewAndData(savedInstanceState)
            //请求网络数据
            requestApi()
            //设置多状态Layout的重连监听
            actMultiStatusLayout?.let {
                it.setOnRetryClickListener(mRetryClickListener)
            }
        }catch (ex : Exception){
            ex.printStackTrace()
        }finally {
            // 动态注册网络变化广播
            if (mNetworkChangeReceiver == null){
                mNetworkChangeReceiver = NetworkChangeReceiver()
            }
            registerReceiver(mNetworkChangeReceiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
            PageCache.pageActivityCache.add(this)//activity进栈
        }
    }


    private fun initContentView(@LayoutRes layoutResID: Int) {
        if (layoutResID == 0){
            throw RuntimeException("layoutResID == -1 have u create your layout?")
        }
        if (showToolBar() && getToolBarResId() > 0){
            //如果需要显示自定义toolbar,并且资源id存在的情况下，实例化rootView
            rootView = LayoutInflater.from(this).inflate(if (isToolbarCover()) R.layout.activity_base_toolbar_cover
            else R.layout.activity_base,null,false)//根布局

            //toolbar容器
            val vsToolbar = rootView?.findViewById<View>(R.id.vs_toolbar) as ViewStub
            //内容布局容器
            val flContainer = rootView?.findViewById<View>(R.id.fl_container) as FrameLayout
            //toolbar资源id
            vsToolbar.layoutResource = getToolBarResId()
            vsToolbar.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            vsToolbar.inflate()//显示toolbar
            LayoutInflater.from(this).inflate(layoutResID, flContainer, true)//子布局
            setContentView(rootView)
        }else{
            //不显示通用toolbar
            super.setContentView(layoutResID)
        }
    }


    override fun onDestroy() {
        if(disposable != null && disposable!!.isDisposed){
            disposable!!.dispose()
            disposable = null
        }
        super.onDestroy()
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        mNetworkChangeReceiver?.let {
            unregisterReceiver(it)
            mNetworkChangeReceiver = null
        }
        rootView = null
        mUnBinder.unbind()
        mLoadingDialog.dismiss()
        PageCache.pageActivityCache.remove(this)//activity出栈
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Fragment 逐个出栈
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun finish() {
        super.finish()
        if (mTipView != null && mTipView.parent != null) {
            mWindowManager.removeView(mTipView)
        }
    }

//    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
//        if (KeyEvent.KEYCODE_BACK == keyCode) {
//            popBackStack()
//            return true
//        }
//        return super.onKeyDown(keyCode, event)
//    }



    /** ============================= 抽象方法 ===============================*/
    /**
     *  加载布局
     */
    @LayoutRes
    abstract fun attachLayoutRes(): Int
    /**
     *  请求数据前的一些初始化设置
     */
    abstract fun initViewAndData(savedInstanceState: Bundle?)
    /** ============================= 公共方法 ===============================*/
    /**
     * 请求加载网络数据
     */
    open fun requestApi(){}
    /**
     * 无网状态—>有网状态 的自动重连操作，子类可重写该方法
     */
    open fun doReConnected() {
        requestApi()
    }

    /** ============================= 私有方法 ===============================*/

    /**
     * 初始化 TipView
     */
    private fun initTipView() {
        mTipView = layoutInflater.inflate(R.layout.layout_network_tip, null)
        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mLayoutParams = WindowManager.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT)
        mLayoutParams.gravity = Gravity.TOP
        mLayoutParams.x = 0
        mLayoutParams.y = 0
        mLayoutParams.windowAnimations = R.style.anim_float_view // add animations
    }

    /**
     * 获取自定义toolbar 资源id 默认为-1，
     * showToolBar()方法必须返回true才有效
     */
    private fun getToolBarResId(): Int {
        return R.layout.layout_default_toolbar
    }

    /**
     * 初始化标题栏
     *  CommonTitleBar.ACTION_LEFT_TEXT;        // 左边TextView被点击
     *  CommonTitleBar.ACTION_LEFT_BUTTON;      // 左边ImageBtn被点击
     *  CommonTitleBar.ACTION_RIGHT_TEXT;       // 右边TextView被点击
     *  CommonTitleBar.ACTION_RIGHT_BUTTON;     // 右边ImageBtn被点击
     *  CommonTitleBar.ACTION_SEARCH;           // 搜索框被点击,搜索框不可输入的状态下会被触发
     *  CommonTitleBar.ACTION_SEARCH_SUBMIT;    // 搜索框输入状态下,键盘提交触发，此时，extra为输入内容
     *  CommonTitleBar.ACTION_SEARCH_VOICE;     // 语音按钮被点击
     *  CommonTitleBar.ACTION_SEARCH_DELETE;    // 搜索删除按钮被点击
     *  CommonTitleBar.ACTION_CENTER_TEXT;      // 中间文字点击
     */
    open fun initToolBar(mCommonTitleBar: CommonTitleBar) {
        mCommonTitleBar.setListener {_: View?, action: Int, extra: String? ->
            if (action == CommonTitleBar.ACTION_LEFT_BUTTON || action == CommonTitleBar.ACTION_LEFT_TEXT) {
                onBackPressed()
            }
        }
    }

    /**
     * 使用默认的ToolBar
     * @param toolbar 工具栏
     * @param homeAsUpEnabled 是否显示左边菜单
     * @param title 标题
     */
    protected fun initToolbar(toolbar: Toolbar, homeAsUpEnabled: Boolean, title: String) {
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUpEnabled)
    }


    /**
     * Network ChangeEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkChangeEvent(event: NetworkChangeEvent) {
        hasNetwork = event.isConnected
        checkNetwork(event.isConnected)
    }

    private fun checkNetwork(isConnected: Boolean) {
        if (enableNetworkTip()) {
            if (isConnected) {
                doReConnected()
                if (mTipView != null && mTipView.parent != null) {
                    mWindowManager.removeView(mTipView)
                }
            } else {
                if (mTipView.parent == null) {
                    mWindowManager.addView(mTipView, mLayoutParams)
                }
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_UP) {
            val v = currentFocus
            // 如果不是落在EditText区域，则需要关闭输入法
            if (KeyBoardUtil.isHideKeyboard(v, ev)) {
                KeyBoardUtil.hideKeyBoard(this, v)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /** ====================== implements start ======================= */


    override fun getPageFragmentManager(): FragmentManager {
        return supportFragmentManager
    }

    override fun showToolBar(): Boolean {
        return false
    }

    override fun getBundleExtras(): Bundle {
        return intent.extras ?: Bundle()
    }

    override fun postDelay(delayMillis: Long, block: () -> Unit) {
        window.decorView.postDelayed(block, delayMillis)
    }

//    override fun postBackStack(fragment: Fragment) {
//        if (fragment.isAdded) {
//            return
//        }
//        supportFragmentManager
//            .beginTransaction()
//            .add(android.R.id.content, fragment)
//            .addToBackStack(fragment.javaClass.name)
//            .commitAllowingStateLoss()
//    }
//
//    override fun popBackStack() {
//        if (supportFragmentManager.backStackEntryCount > 1) {
//            supportFragmentManager.popBackStack()
//            return
//        }
//        ActivityCompat.finishAfterTransition(this)
//    }


    override fun startProxyActivity(_Class: Class<out Fragment>) {
        startProxyActivity(_Class, Bundle(), null)
    }

    override fun startProxyActivity(_Class: Class<out Fragment>, view: View?) {
        startProxyActivity(_Class,  Bundle(), view)
    }

    override fun startProxyActivity(_Class: Class<out Fragment>, bundle: Bundle) {
        startProxyActivity(_Class, bundle, null)
    }

    override fun startProxyActivity(_Class: Class<out Fragment>, bundle: Bundle, view: View?) {
        startProxyActivityForResult(_Class, bundle, view,-1)
    }

    override fun startProxyActivityForResult(_Class: Class<out Fragment>, requestCode: Int) {
        startProxyActivityForResult(_Class,  Bundle(), null,requestCode)
    }

    override fun startProxyActivityForResult(_Class: Class<out Fragment>, view: View?, requestCode: Int) {
        startProxyActivityForResult(_Class,  Bundle(), view,requestCode)
    }

    override fun startProxyActivityForResult(_Class: Class<out Fragment>, bundle: Bundle, requestCode: Int) {
        startProxyActivityForResult(_Class, bundle, null,requestCode)
    }

    override fun startProxyActivityForResult(_Class: Class<out Fragment>, bundle: Bundle, view: View?, requestCode: Int) {
        var options: Bundle? = null
        if (null != view) {
            options = ActivityOptionsCompat.makeScaleUpAnimation(view, view.width / 2, view.height / 2, 0, 0).toBundle()
        }
        val mIntent = Intent(this, ProxyActivity::class.java)
        bundle?.let {
            it.putString(ProxyActivity.PROXY_FRAGMENT_CLASS_KEY, _Class.name) // com.project.app.activity.*
            mIntent.putExtras(bundle)
        }
        startActivityForResult(mIntent, requestCode, options)
    }


    // ====================================添加fragment=======================================
    /**
     * 添加 Fragment
     * @param containerViewId
     * @param fragment
     */
    protected fun addFragment(containerViewId: Int, fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(containerViewId, fragment)
        fragmentTransaction.commit()
    }


    /**
     * 添加 Fragment
     * @param containerViewId
     * @param fragment
     */
    protected fun addFragment(containerViewId: Int, fragment: Fragment, tag: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        // 设置tag，不然下面 findFragmentByTag(tag)找不到
        fragmentTransaction.add(containerViewId, fragment, tag)
        fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.commit()
    }

    /**
     * 替换 Fragment
     * @param containerViewId
     * @param fragment
     */
    protected fun replaceFragment(containerViewId: Int, fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(containerViewId, fragment)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    /**
     * 替换 Fragment
     * @param containerViewId
     * @param fragment
     */
    protected fun replaceFragment(containerViewId: Int, fragment: Fragment, tag: String) {
        if (supportFragmentManager.findFragmentByTag(tag) == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            // 设置tag
            fragmentTransaction.replace(containerViewId, fragment, tag)
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            // 这里要设置tag，上面也要设置tag
            fragmentTransaction.addToBackStack(tag)
            fragmentTransaction.commit()
        } else {
            // 存在则弹出在它上面的所有fragment，并显示对应fragment
            supportFragmentManager.popBackStack(tag, 0)
        }
    }

}