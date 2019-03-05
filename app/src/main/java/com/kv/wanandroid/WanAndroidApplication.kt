package com.kv.wanandroid

import android.app.Application
import android.content.Context
import com.vension.frame.core.CoreApplication
import com.vension.frame.http.RetrofitHelper
import org.litepal.LitePal
import kotlin.properties.Delegates

/**
 * ========================================================
 * @author: Created by Vension on 2018/10/31 17:13.
 * @email:  2506856664@qq.com
 * @desc:
 * ========================================================
 */
class WanAndroidApplication : CoreApplication() {

    companion object {
        private val TAG = "CoreApplication"

        var context: Context by Delegates.notNull()
            private set

        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
        RetrofitHelper.setBaseUrl(Constant.BASE_URL)
        LitePal.initialize(this)//初始化 LitePal
    }
}