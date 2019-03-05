package com.vension.frame.http.intercepter

import com.vension.frame.VFrame
import com.vension.frame.utils.NetWorkUtil
import okhttp3.Interceptor
import okhttp3.Response

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/20 13:21
 * 描  述：OfflineCacheInterceptor: 没有网时缓存
 * ========================================================
 */

class OfflineCacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (!NetWorkUtil.isNetworkAvailable(VFrame.getContext())) {
            // 无网络时，设置超时为4周  只对get有用,post没有缓冲
            val maxStale = 60 * 60 * 24 * 28
            response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .removeHeader("nyn")
                    .build()
        }

        return response
    }


}