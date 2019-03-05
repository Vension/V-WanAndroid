package com.vension.frame.http.intercepter

import com.vension.frame.http.HttpConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/10/31 15:16
 * 描  述：SaveCookieInterceptor: 保存 Cookie
 * ========================================================
 */

class SaveCookieInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val requestUrl = request.url().toString()
        val domain = request.url().host()
        // set-cookie maybe has multi, login to save cookie
        if ((requestUrl.contains(HttpConfig.SAVE_USER_LOGIN_KEY)
                        || requestUrl.contains(HttpConfig.SAVE_USER_REGISTER_KEY))
                && !response.headers(HttpConfig.SET_COOKIE_KEY).isEmpty()) {
            val cookies = response.headers(HttpConfig.SET_COOKIE_KEY)
            val cookie = HttpConfig.encodeCookie(cookies)
            HttpConfig.saveCookie(requestUrl, domain, cookie)
        }
        return response
    }
}