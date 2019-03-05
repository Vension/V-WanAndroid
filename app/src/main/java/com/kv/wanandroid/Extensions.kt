package com.kv.wanandroid

import android.app.Activity
import android.content.Intent
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.kv.wanandroid.ui.activity.MainActivity

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/4/2 16:15
 * 描  述：
 * ========================================================
 */

/**
 * 进入主界面
 */
fun redirectToMain(context: Activity) {
    val intent = Intent()
    intent.setClass(context, MainActivity::class.java)
    context.startActivity(intent)
    context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
}

/**
 * getAgentWeb
 */
fun String.getAgentWeb(
    activity: Activity,
    webContent: ViewGroup,
    layoutParams: ViewGroup.LayoutParams,
    webView: WebView,
    webChromeClient: WebChromeClient?,
    webViewClient: WebViewClient
) = AgentWeb.with(activity)//传入Activity or Fragment
    .setAgentWebParent(webContent, 0, layoutParams)//传入AgentWeb 的父控件
    .useDefaultIndicator()// 使用默认进度条
    .setWebView(webView)
    .setWebChromeClient(webChromeClient)
    .setWebViewClient(webViewClient)
    .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
    .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
    .createAgentWeb()//
    .ready()
    .go(this)