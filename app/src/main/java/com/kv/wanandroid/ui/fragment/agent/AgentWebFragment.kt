package com.kv.wanandroid.ui.fragment.agent

import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ImageButton
import com.just.agentweb.AgentWeb
import com.just.agentweb.NestedScrollAgentWebView
import com.kv.wanandroid.Constant
import com.kv.wanandroid.R
import com.kv.wanandroid.getAgentWeb
import com.kv.wanandroid.ui.adapter.MyAdapter
import com.vension.frame.core.BasePopupWindow
import com.vension.frame.core.mvp.AbsCompatMVPFragment
import com.vension.frame.ext.showToast
import com.vension.frame.http.BaseBean
import com.kv.wanandroid.event.RefreshHomeEvent
import com.kv.wanandroid.mvp.contract.ContentContract
import com.kv.wanandroid.mvp.presenter.ContentPresenter
import com.wuhenzhizao.titlebar.widget.CommonTitleBar
import kotlinx.android.synthetic.main.fragment_agent_web.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.imageResource


/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/20 13:58
 * 描  述：代理WebFragment
 * ========================================================
 */

class AgentWebFragment : AbsCompatMVPFragment<ContentContract.View<BaseBean>, ContentContract.Presenter<BaseBean>>(),
    ContentContract.View<BaseBean> {

    override fun setRefreshData(listData: MutableList<BaseBean>) {
    }

    override fun setRefreshData(listData: MutableList<BaseBean>, header: List<View>?, footer: List<View>?) {
    }

    override fun setMoreData(listData: MutableList<BaseBean>) {
    }

    override fun loadFail() {
    }

    private var agentWeb: AgentWeb? = null
    private lateinit var shareTitle: String
    private lateinit var shareUrl: String
    private var shareId: Int = 0

    override fun createPresenter(): ContentContract.Presenter<BaseBean> {
        return ContentPresenter()
    }

    override fun initToolBar(mCommonTitleBar: CommonTitleBar) {
        mCommonTitleBar.apply {
            centerTextView.text = "正在加载中..."
            rightImageButton.imageResource = R.drawable.icon_more
            setListener { v, action1, extra ->
                when(action1){
                    CommonTitleBar.ACTION_LEFT_BUTTON,
                    CommonTitleBar.ACTION_LEFT_TEXT -> {
                        activity?.onBackPressed()
                    }
                    CommonTitleBar.ACTION_RIGHT_BUTTON ,
                    CommonTitleBar.ACTION_RIGHT_TEXT -> {
                        showPopListView(mCommonTitleBar.rightImageButton)
                    }
                }
            }
        }
    }
    override fun attachLayoutRes(): Int {
        return R.layout.fragment_agent_web
    }

    override fun initViewAndData(view: View, savedInstanceState: Bundle?) {
        super.initViewAndData(view, savedInstanceState)
        getBundleExtras()?.let {
            shareId = it.getInt("web_id", -1)
            shareTitle = it.getString("web_title", "")
            shareUrl = it.getString("web_url", "")
            mCommonTitleBar.centerTextView.text = shareTitle
        }
    }

    override fun lazyLoadData() {
        val webView = NestedScrollAgentWebView(context)
        val layoutParams = CoordinatorLayout.LayoutParams(-1, -1)
        layoutParams.behavior = AppBarLayout.ScrollingViewBehavior()

        agentWeb = shareUrl.getAgentWeb(activity!!,
            cl_main,
            layoutParams,
            webView,
            webChromeClient,
            webViewClient)

        agentWeb?.webCreator?.webView?.let {
            it.settings.domStorageEnabled = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }
    }

    override fun showCollectSuccess(success: Boolean) {
        if (success){
            showToast(getString(R.string.collect_success))
            EventBus.getDefault().post(RefreshHomeEvent(true))
        }
    }


    override fun showCancelCollectSuccess(success: Boolean) {
        if (success){
            showToast(getString(R.string.cancel_collect_success))
            EventBus.getDefault().post(RefreshHomeEvent(true))
        }
    }


    override fun onResume() {
        agentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onPause() {
        agentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        agentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }


    override fun onBackPressed(): Boolean {
        agentWeb?.let {
            if (!it.back()) {
                super.onBackPressed()
            }
        }
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (agentWeb?.handleKeyEvent(keyCode, event)!!) {
            true
        } else {
            activity?.finish()
            super.onKeyDown(keyCode, event)
        }
    }


    /**
     * webViewClient
     */
    private val webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            super.onReceivedSslError(view, handler, error)
            handler?.proceed()
        }
    }

    /**
     * webChromeClient
     */
    private val webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
//            if (100 == newProgress) {
//                // 关闭进度条
//                web_ProgressBar.visibility = View.GONE
//            } else {
//                // 加载中显示进度条
//                if (View.GONE == web_ProgressBar.visibility) {
//                    web_ProgressBar.visibility = View.VISIBLE
//                }
//                web_ProgressBar.progress = newProgress
//            }
        }

        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
            if(shareTitle.isNullOrEmpty()){
                title.let {
                    mCommonTitleBar.centerTextView.text = it
                }
            }
        }
    }


    private fun showPopListView(rightImageButton: ImageButton) {
        val contentView = LayoutInflater.from(context).inflate(R.layout.layout_pop_list, null)
        //创建并显示popWindow
        val mPopupwindow = BasePopupWindow.PopupWindowBuilder(context!!)
            .setView(contentView)
            .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
            .setBgDarkAlpha(0.7f) // 控制亮度
            .setAnimationStyle(R.style.DialogTheme_Animation) // 添加自定义显示和消失动画
            .size(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
            .create()
        //处理popWindow 显示内容
        handleListView(mPopupwindow,contentView)
        mPopupwindow.showAsDropDown(rightImageButton,10,  0)
    }

    private fun handleListView(pop:BasePopupWindow,contentView: View) {
        val recyclerView = contentView.findViewById<View>(R.id.pop_recyclerView) as RecyclerView
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = manager
        val adapter = MyAdapter()
        adapter.setData(mockData())
        adapter.addItemClickListener(object : MyAdapter.OnItemClickListener{
            override fun onItemClick(obj: Any?, position: Int) {
                when(position){
                    0 -> {
                        Intent().run {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT,
                                getString(
                                    R.string.share_article_url,
                                    getString(R.string.app_name),
                                    shareTitle,
                                    shareUrl
                                ))
                            type = Constant.CONTENT_SHARE_TYPE
                            startActivity(Intent.createChooser(this, getString(R.string.action_share)))
                        }
                    }
                    1 -> {
                        if (isLogin) {
                            mPresenter?.addCollectArticle(shareId)
                        } else {
                            startProxyActivity(LoginFragment::class.java)
                            showToast(resources.getString(R.string.login_tint))
                        }
                    }
                    2 -> {
                        Intent().run {
                            action = "android.intent.action.VIEW"
                            data = Uri.parse(shareUrl)
                            startActivity(this)
                        }
                    }
                    else -> {

                    }
                }
                pop.dissmiss()
            }
        })
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun mockData(): List<String> {
        val data = ArrayList<String>()
        data.add("分享")
        data.add("收藏")
        data.add("用浏览器打开")
        return data
    }


}