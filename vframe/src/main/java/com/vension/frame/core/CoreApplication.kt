package com.vension.frame.core

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.support.multidex.MultiDex
import android.util.Log
import com.gw.swipeback.tools.WxSwipeBackActivityManager
import com.vension.frame.VFrame
import com.vension.frame.cache.PageCache
import kotlin.properties.Delegates

/**
 * ===================================================================
 * @author: Created by Vension on 2019/2/14 16:11.
 * @email:  250685***4@qq.com
 * @update: update by *** on 2019/2/14 16:11
 * @desc:   character determines attitude, attitude determines destiny
 * ===================================================================
 */
open class CoreApplication : Application() {

    companion object {
        private val TAG = "CoreApplication"

        var context: Context by Delegates.notNull()
            private set

        lateinit var instance: Application
    }


    /**
     * 这个最先执行
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        //MultiDex分包方法 必须最先初始化
        MultiDex.install(this)
    }

    /**
     * 程序启动的时候执行
     */
    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
        //初始化Frame入口
        VFrame.init(this,true)
        //初始化侧滑返回
        WxSwipeBackActivityManager.getInstance().init(this)
    }


    /**
     *
     * 初始化阿里路由框架
     */
//    private fun initARouter() {
//        if (BuildConfig.DEBUG) {
//            //打印日志
//            ARouter.openLog()
//            //开启调试模式 在InstantRun模式下开启调试模式，线上版本需要关闭，不然会有安全风险
//            ARouter.openDebug()
//        }
//        //尽可能早初始化 在Application中初始化
//        ARouter.init(baseApplication)
//    }

    /**
     * 低内存的时候执行
     */
    override fun onLowMemory() {
        Log.d("CoreApplication", "onLowMemory")
        super.onLowMemory()
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    /**
     * HOME键退出应用程序
     * 程序在内存清理的时候执行
     */
    override fun onTrimMemory(level: Int) {
        Log.d("CoreApplication", "onTrimMemory")
        super.onTrimMemory(level)
    }

    /**
     * 程序终止的时候执行
     */
    override fun onTerminate() {
        Log.d("CoreApplication", "onTerminate")
        super.onTerminate()
        PageCache.pageActivityCache.quit()
    }


    override fun onConfigurationChanged(newConfig: Configuration?) {
        Log.d("CoreApplication", "onConfigurationChanged")
        super.onConfigurationChanged(newConfig)
    }

}