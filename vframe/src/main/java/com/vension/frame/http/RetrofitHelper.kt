package com.vension.frame.http

import com.orhanobut.logger.Logger
import com.vension.frame.VFrame
import com.vension.frame.http.intercepter.CacheInterceptor
import com.vension.frame.http.intercepter.HeaderInterceptor
import com.vension.frame.http.intercepter.SaveCookieInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit


/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/10/29 16:54
 * 描  述：网络助手
 * ========================================================
 */

object RetrofitHelper {

    val mOkHttpClient by lazy {
        OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val mRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(mOkHttpClient)
            .build()
    }

    private lateinit var BASE_URL: String
    /**
     * 缓存不同配置的retrofit集合，如url ,converter等
     */
    private var retrofitMap = ConcurrentHashMap<String,Retrofit>()

    private fun getRetrofit(baseUrl: String): Retrofit {
        if (!retrofitMap.containsKey(baseUrl)){
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)  // baseUrl
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            retrofitMap[baseUrl] = retrofit
        }
        return retrofitMap[baseUrl]!!
    }


    /**
     * 获取 OkHttpClient
     */
    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
        //添加一个log拦截器,打印所有的log
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                message -> Logger.i( "请求参数:$message")
        })
        //可以设置请求过滤的水平,body,basic,headers
        if (VFrame.debug) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        //设置 请求的缓存的大小跟位置
        val cacheFile = File(VFrame.getContext().cacheDir, "cache")
        val cache = Cache(cacheFile, HttpConfig.MAX_CACHE_SIZE)

        builder.run {
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(HeaderInterceptor())
            addInterceptor(SaveCookieInterceptor())
            addInterceptor(CacheInterceptor())
            cache(cache)  //添加缓存
            connectTimeout(HttpConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(HttpConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(HttpConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true) // 错误重连
//             cookieJar(CookieManager())
        }
        return builder.build()
    }

    fun setBaseUrl(baseUrl: String) {
        BASE_URL = baseUrl
    }

    /**
     * 全局保存不同配置的Retrofit,如不同的baseUrl等
     *
     * @param tag      标记key
     * @param retrofit 对应的retrofit对象
     */
    fun put(tag: String, retrofit: Retrofit) {
        retrofitMap[tag] = retrofit
    }

    operator fun get(tag: String): Retrofit {
        return retrofitMap[tag]!!
    }

    fun remove(tag: String) {
        retrofitMap.remove(tag)
    }

    fun <T> getService(baseUrl : String,service: Class<T>): T = getRetrofit(baseUrl).create(service)
    fun <T> getService(service: Class<T>): T = getRetrofit(BASE_URL).create(service)

}

