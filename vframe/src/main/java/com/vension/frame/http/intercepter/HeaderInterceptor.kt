package com.vension.frame.http.intercepter

import Constant
import com.vension.frame.utils.PreferenceUtil
import com.vension.frame.http.HttpConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/10/31 15:11
 * 描  述：请求头拦截器
 * ========================================================
 */

class HeaderInterceptor : Interceptor {

    /**
     * token
     */
    private var token: String by PreferenceUtil(Constant.TOKEN_KEY, "")

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val builder = request.newBuilder()

        builder.addHeader("Content-type", "application/json; charset=utf-8")
                // .header("token", token)
                // .method(request.method(), request.body())
//            .header ("Connection", "keep-alive")
//            .header ("Accept", "*/*")
//            .header ("Cache-Control", String.format("public, max-age=%d", 60))
//                    .header ("Authorization", _Authorization)
//                    .header ("X-Oc-TimeStamp", _TimeStamp)
//            .header ("X-Oc-Device-Model", android.os.Build.MODEL)
//            .header ("X-Oc-Os-Model", "Android " + android.os.Build.VERSION.RELEASE)
//            .header ("X-Oc-App-Bundle", AppUtil.getAppVersionCode(VFrame.getContext()).toString())
//            .header ("X-Oc-App-Version", AppUtil.getAppVersionName(VFrame.getContext()))
//            .header ("X-Oc-Merchant-Language", "2")
//                    .header ("X-Oc-Sign", MD5Helper.MD5(_SignedString).toString().toLowerCase())
//            .method(request.method(), request.body())
        val domain = request.url().host()
        val url = request.url().toString()
        if (domain.isNotEmpty() && (url.contains(HttpConfig.COLLECTIONS_WEBSITE)
                        || url.contains(HttpConfig.UNCOLLECTIONS_WEBSITE)
                        || url.contains(HttpConfig.ARTICLE_WEBSITE)
                        || url.contains(HttpConfig.TODO_WEBSITE))) {
            val spDomain: String by PreferenceUtil(domain, "")
            val cookie: String = if (spDomain.isNotEmpty()) spDomain else ""
            if (cookie.isNotEmpty()) {
                // 将 Cookie 添加到请求头
                builder.addHeader(HttpConfig.COOKIE_NAME, cookie)
            }
        }

        return chain.proceed(builder.build())
    }

}