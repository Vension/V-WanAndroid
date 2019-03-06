package com.kv.wanandroid.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatDelegate
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.kv.wanandroid.Constant
import com.kv.wanandroid.R
import com.kv.wanandroid.event.LoginEvent
import com.kv.wanandroid.event.RefreshHomeEvent
import com.kv.wanandroid.mvp.contract.MainContract
import com.kv.wanandroid.mvp.presenter.MainPresenter
import com.kv.wanandroid.test.TestFragment
import com.kv.wanandroid.ui.fragment.*
import com.kv.wanandroid.ui.fragment.agent.LoginFragment
import com.kv.wanandroid.util.DialogUtil
import com.kv.wanandroid.util.SettingUtil
import com.vension.frame.core.mvp.AbsCompatMVPActivity
import com.vension.frame.ext.showToast
import com.vension.frame.utils.PreferenceUtil
import com.vension.frame.views.ShapeImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import q.rorbin.badgeview.Badge
import q.rorbin.badgeview.QBadgeView


class MainActivity : AbsCompatMVPActivity<MainContract.View, MainContract.Presenter>(), MainContract.View {

    private var mHomeFragment: MenuTabHomeFragment? = null
    private var mProjectFragment: MenuTabProjectFragment? = null
    private var mWeChatFragment: MenuTabWeChatFragment? = null
    private var mKnowledgeTreeFragment: MenuTabKnowledgeTreeFragment? = null
    private var mNavigationFragment: MenuTabNavigationFragment? = null

    //默认为0
    private var mIndex = 0

    /**
     * local username
     */
    private val username: String by PreferenceUtil(Constant.USERNAME_KEY, "")
    /**
     * username TextView
     */
    private lateinit var nav_username: TextView
    private lateinit var siv_avatar: ShapeImageView

    /**
     * 退出登录 Dialog
     */
    private val mDialog by lazy {
        DialogUtil.getWaitDialog(this@MainActivity, resources.getString(R.string.logout_ing))
    }


    private val mNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.menu_tab_1 -> {
                switchFragment(0)
            }
            R.id.menu_tab_2 -> {
                switchFragment(1)
            }
            R.id.menu_tab_3 -> {
                false
            }
            R.id.menu_tab_4 -> {
                switchFragment(3)
            }
            R.id.menu_tab_5 -> {
                switchFragment(4)
            }
            else -> {
                false
            }
        }
        true
    }

    /**
     * NavigationView 监听
     */
    private val onDrawerNavigationItemSelectedListener =
        NavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_collect -> {
                    if (isLogin) {
                        startProxyActivity(CollectFragment::class.java)
                    } else {
                        showToast(resources.getString(R.string.login_tint))
                        startProxyActivity(LoginFragment::class.java)
                    }
                }
                R.id.nav_todo -> {
                    if (isLogin) {
                        startProxyActivity(TodoTabFragment::class.java)
                    } else {
                        showToast(resources.getString(R.string.login_tint))
                        startProxyActivity(LoginFragment::class.java)
                    }
                }
                R.id.nav_night_mode -> {
                    if (SettingUtil.getIsNightMode()) {
                        SettingUtil.setIsNightMode(false)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    } else {
                        SettingUtil.setIsNightMode(true)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    window.setWindowAnimations(R.style.WindowAnimationFadeInOut)
                    recreate()
                }
                R.id.nav_setting -> {
                    Intent(this@MainActivity, SetingActivity::class.java).run {
                        startActivity(this)
                    }
                }
                R.id.nav_about_us -> {
                    startProxyActivity(AboutFragment::class.java)
                }
                R.id.nav_logout -> {
                    logout()
                }
                R.id.nav_test -> {
                    startProxyActivity(TestFragment::class.java)
                }
            }
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }


    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    override fun attachLayoutRes(): Int {
        return R.layout.activity_main
    }

    override fun createPresenter(): MainContract.Presenter {
        return MainPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt("currTabIndex",0)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //记录fragment的位置,防止崩溃 activity被系统回收时，fragment错乱
        outState?.putInt("currTabIndex", mIndex)
    }

    override fun initViewAndData(savedInstanceState: Bundle?) {
        super.initViewAndData(savedInstanceState)
        toolbar_home.run {
            title = getString(R.string.app_name)
            setSupportActionBar(this)
        }

        navigationView_bottom.run {
            // 以前使用 BottomNavigationViewHelper.disableShiftMode(this) 方法来设置底部图标和字体都显示并去掉点击动画
            // 升级到 28.0.0 之后，官方重构了 BottomNavigationView ，目前可以使用 labelVisibilityMode = 1 来替代
            // BottomNavigationViewHelper.disableShiftMode(this)
            labelVisibilityMode = 1
            onNavigationItemSelectedListener = mNavigationItemSelectedListener
            menu.getItem(mIndex).isChecked = true
        }

        drawer_layout.run {
            var toggle = ActionBarDrawerToggle(
                this@MainActivity,
                this,
                toolbar_home
                , R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
            addDrawerListener(toggle)
            toggle.syncState()
        }

        navigationView_left.run {
            setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener)
            siv_avatar = getHeaderView(0).findViewById(R.id.siv_avatar)
            nav_username = getHeaderView(0).findViewById(R.id.tv_username)
            menu.findItem(R.id.nav_logout).isVisible = isLogin
            getHeaderView(0).setOnClickListener {
                if (!isLogin) {
                    startProxyActivity(LoginFragment::class.java)
                } else {
                    showToast("已登录查看头像")
                }

            }
        }

        siv_avatar?.run {
            setImageResource(if (isLogin) R.drawable.icon_logo else R.drawable.icon_default_avatar)
        }
        nav_username?.run {
            text = if (!isLogin) {
                getString(R.string.login)
            } else {
                username
            }
        }

        btn_menu_center.setOnClickListener {
            switchFragment(2)
            navigationView_bottom.menu.getItem(mIndex).isChecked = true
        }

        addBadgeAt(1, 100)//添加数字
        QBadgeView(this)
            .bindTarget(navigationView_bottom.getBottomNavigationItemView(3))
            .setBadgeText("")
            .setGravityOffset(20f, 6f, true)

        switchFragment(mIndex)
    }

    private fun addBadgeAt(position: Int, number: Int): Badge {
        // add badge
        return QBadgeView(this)
            .setBadgeNumber(number)
            .setGravityOffset(10f, 2f, true)
            .bindTarget(navigationView_bottom.getBottomNavigationItemView(position))
            .setOnDragStateChangedListener { dragState, badge, targetView ->
                if (Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState)
                    Toast.makeText(this@MainActivity, "移除消息", Toast.LENGTH_SHORT).show()
            }
    }


    /**
     * 切换Fragment
     * @param position 下标
     */
    private fun switchFragment(position: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction!!)
        mIndex = position
        when (position) {
            0 ->{
                toolbar_home.title = getString(R.string.menu_tab_1)
                mHomeFragment?.let {
                    transaction.show(it)
                } ?: MenuTabHomeFragment.getInstance().let {
                    mHomeFragment = it
                    transaction.add(R.id.container_main, it, "home")
                }
            }
            1 -> {
                toolbar_home.title = getString(R.string.menu_tab_2)
                mProjectFragment?.let {
                    transaction.show(it)
                } ?: MenuTabProjectFragment.getInstance().let {
                    mProjectFragment = it
                    transaction.add(R.id.container_main, it, "project")
                }
            }
            2 -> {
                toolbar_home.title = getString(R.string.menu_tab_3)
                mWeChatFragment?.let {
                    transaction.show(it)
                } ?: MenuTabWeChatFragment.getInstance().let {
                    mWeChatFragment = it
                    transaction.add(R.id.container_main, it, "weChat")
                }
            }
            3 -> {
                toolbar_home.title = getString(R.string.menu_tab_4)
                mKnowledgeTreeFragment?.let {
                    transaction.show(it)
                } ?: MenuTabKnowledgeTreeFragment.getInstance().let {
                    mKnowledgeTreeFragment = it
                    transaction.add(R.id.container_main, it, "KnowledgeTree")
                }
            }
            4 -> {
                toolbar_home.title = getString(R.string.menu_tab_5)
                mNavigationFragment?.let {
                    transaction.show(it)
                } ?: MenuTabNavigationFragment.getInstance().let {
                    mNavigationFragment = it
                    transaction.add(R.id.container_main, it, "Navigation")
                }
            }
            else -> {
                transaction.add(R.id.container_main, Fragment(), "test")
            }
        }
        //
        /*
        *  如果你需要同步的操作, 并且你不需要加到back stack里, 使用commitNow().
        *  如果你操作很多transactions, 并且不需要同步, 或者你需要把transactions加在back stack里, 那就使用commit().
        *  如果你希望在某一个指定的点, 确保所有的transactions都被执行, 那么使用executePendingTransactions().
        *
        */
        transaction.commit()
//        transaction.commitAllowingStateLoss()
    }


    /**
     * 隐藏所有的Fragment
     * @param transaction transaction
     */
    private fun hideFragments(transaction: FragmentTransaction) {
        mHomeFragment?.let { transaction.hide(it) }
        mProjectFragment?.let { transaction.hide(it) }
        mWeChatFragment?.let { transaction.hide(it) }
        mKnowledgeTreeFragment?.let { transaction.hide(it) }
        mNavigationFragment?.let { transaction.hide(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        //icon 为 Animatable 时点击时开始执行自带动画效果
        val drawable = item?.icon
        if (drawable is Animatable) {
            (drawable as Animatable).start()
        }
        when (item?.itemId) {
            R.id.action_search -> {
                startProxyActivity(SearchFragmet::class.java)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHomeFragment = null
        mNavigationFragment = null
        mKnowledgeTreeFragment = null
        mProjectFragment = null
        mWeChatFragment = null
    }

    private var exitTime: Long = 0
    override fun onBackPressed() {
        if (System.currentTimeMillis().minus(exitTime) <= 2000) {
            finish()
        } else {
            exitTime = System.currentTimeMillis()
            showToast("再按一次退出程序")
        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(event: LoginEvent) {
        if (event.isLogin) {
            nav_username?.text = username
            siv_avatar?.setImageResource(R.drawable.icon_logo)
            navigationView_left.menu.findItem(R.id.nav_logout).isVisible = true
            mHomeFragment?.lazyLoadData()
        } else {
            nav_username?.text = resources.getString(R.string.login)
            siv_avatar?.setImageResource(R.drawable.icon_default_avatar)
            navigationView_left.menu.findItem(R.id.nav_logout).isVisible = false
            mHomeFragment?.lazyLoadData()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshHomeEvent(event: RefreshHomeEvent) {
        if (event.isRefresh) {
            when(mIndex){
                0 ->{
                    mHomeFragment?.lazyLoadData()
                }
                2 ->{
                    mProjectFragment?.lazyLoadData()
                }
                2 ->{
                    mWeChatFragment?.lazyLoadData()
                }
                3 ->{
                    mKnowledgeTreeFragment?.lazyLoadData()
                }
                4 ->{
                    mNavigationFragment?.lazyLoadData()
                }
            }

        }
    }

    /**
     * Logout
     */
    private fun logout() {
        DialogUtil.getConfirmDialog(this, resources.getString(R.string.confirm_logout),
            DialogInterface.OnClickListener { _, _ ->
                mDialog.show()
                mPresenter?.logout()
            }).show()
    }

    override fun showLogoutSuccess(success: Boolean) {
        if (success) {
            doAsync {
                // CookieManager().clearAllCookies()
                PreferenceUtil.clear()
                uiThread {
                    mDialog.dismiss()
                    showToast(resources.getString(R.string.logout_success))
                    isLogin = false
                    EventBus.getDefault().post(LoginEvent(false))
                    startProxyActivity(LoginFragment::class.java)
                }
            }
        }
    }

    override fun recreate() {
        try {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if (mHomeFragment != null) {
                fragmentTransaction.remove(mHomeFragment!!)
            }
            if (mKnowledgeTreeFragment != null) {
                fragmentTransaction.remove(mKnowledgeTreeFragment!!)
            }
            if (mNavigationFragment != null) {
                fragmentTransaction.remove(mNavigationFragment!!)
            }
            if (mProjectFragment != null) {
                fragmentTransaction.remove(mProjectFragment!!)
            }
            if (mWeChatFragment != null) {
                fragmentTransaction.remove(mWeChatFragment!!)
            }
            fragmentTransaction.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.recreate()
    }

}
