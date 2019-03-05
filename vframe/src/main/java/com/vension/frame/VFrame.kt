package com.vension.frame

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.LogcatLogStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.vension.frame.cache.FileCache
import com.vension.frame.utils.CrashUtil

/**
 * ===================================================================
 * @author: Created by Vension on 2019/2/14 15:31.
 * @email:  250685***4@qq.com
 * @update: update by *** on 2019/2/14 15:31
 * @desc:   VFrame 入口
 * ===================================================================
 */
@SuppressLint("StaticFieldLeak")
object VFrame {

    lateinit var mContext: Context
        private set
    lateinit var mApplication: Context
        private set
    var debug = false
        private set


    fun init(application: Application) {
        init(application,false)
    }
    fun init(application: Application,isDebug: Boolean) {
        debug = isDebug
        mApplication = application
        mContext = application.applicationContext
//        SPUtils.init(application.applicationContext,"VFrame.sp")
        FileCache.init(application.applicationContext)
        initLogger()//初始化Logger日志打印
        initCrash()//崩溃日志收集
    }



    /**
     * 初始化Logger日志打印
     */
    private fun initLogger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)                  // (可选）是否显示线程信息
            .methodCount(2)                         // (可选）要显示的方法行数
            .methodOffset(5)                        // (可选）设置调用堆栈的函数偏移值，0的话则从打印该Log函数开始输出堆栈信息，默认是0
            .logStrategy(LogcatLogStrategy())           // (可选）要改要打印的日志策略。默认LogCat
            .tag("V-Frame_Logger--->")             // (可选）每个日志的全局标记。默认PRETTY_LOGGER
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                //DEBUG模式下不打印LOG
                return debug
            }
        })
    }

    fun initCrash() {
        CrashUtil.init()
    }

    fun getContext(): Context {
        return mContext
    }

    fun getApplication(): Context {
        return mApplication
    }


    /**
     * 获取资源对象
     * @return  Resources
     */
    fun getResources(): Resources {
        return getContext().resources
    }

    fun getString(@StringRes id: Int): String {
        return getResources().getString(id)
    }

    fun getTheme(): Resources.Theme {
        return getContext().theme
    }

    fun getAssets(): AssetManager {
        return getContext().assets
    }

    fun getDrawable(@DrawableRes id: Int): Drawable? {
        return ContextCompat.getDrawable(getContext(), id)
    }

    fun getColor(@ColorRes id: Int): Int {
        return ContextCompat.getColor(getContext(), id)
    }

    fun getSystemService(name: String): Any {
        return getContext().getSystemService(name)
    }

    fun getConfigguration(): Configuration {
        return getResources().configuration
    }

    fun getDisplayMetrics(): DisplayMetrics {
        return getResources().displayMetrics
    }


//    private void installAPK(){
//
//            if (Build.VERSION.SDK_INT >= 26) {
//                    boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
//                    if (hasInstallPermission) {
//                        //安装应用
//                    } else {
//                        //跳转至“安装未知应用”权限界面，引导用户开启权限
//                        Uri selfPackageUri = Uri.parse("package:" + this.getPackageName());
//                        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, selfPackageUri);
//                        startActivityForResult(intent, REQUEST_CODE_UNKNOWN_APP);
//                    }
//                }else {
//                    //安装应用
//                }
//
//    }
//
////接收“安装未知应用”权限的开启结果
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//            super.onActivityResult(requestCode, resultCode, data);
//            if (requestCode == REQUEST_CODE_UNKNOWN_APP) {
//                    installAPK();
//                }
//    }


}