package com.vension.frame.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.DisplayMetrics


/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/5/29 9:27
 * 描  述：尺寸工具类
 * ========================================================
 */

object DensityUtil {


    /**
     * 获取设备尺寸密度
     */
    fun getDensity(context: Context) = context.resources.displayMetrics.density

    /**
     * 获取设备收缩密度
     */
    fun getScaleDensity(context: Context) = context.resources.displayMetrics.scaledDensity

    fun getDisplayMetrics(context: Context): DisplayMetrics {
        val activity: Activity
        if (context !is Activity && context is ContextWrapper) {
            activity = context.baseContext as Activity
        } else {
            activity = context as Activity
        }

        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        return metrics
    }

    fun getScreenPixelSize(context: Context): IntArray {
        val metrics = getDisplayMetrics(context)
        return intArrayOf(metrics.widthPixels, metrics.heightPixels)
    }

    fun dp2px(context: Context?, dp: Float): Float {
        return if (context == null) -1.0f else dp * getDensity(context)
    }

    fun px2dp(context: Context?, px: Float): Float {
        return if (context == null) -1.0f else px / getDensity(context)
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
//    fun dp2px(context: Context, dpVal: Float): Int {
//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.resources.displayMetrics).toInt()
//    }

    /**
     * sp转px
     * @param context
     * @param spVal
     * @return
     */
//    fun sp2px(context: Context, spVal: Float): Int {
//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.resources.displayMetrics).toInt()
//    }

//    /**
//     * px转dp
//     * @param context
//     * @param pxVal
//     * @return
//     */
//    fun px2dp(context: Context, pxVal: Float): Float {
//        val scale = context.resources.displayMetrics.density
//        return pxVal / scale
//    }
//
//    /**
//     * px转sp
//     * @param context
//     * @param pxVal
//     * @return
//     */
//    fun px2sp(context: Context, pxVal: Float): Float {
//        return pxVal / context.resources.displayMetrics.scaledDensity
//    }

    /**
     * dp转px
     */
    fun dp2px(context: Context, dpValue: Float) = (dpValue * getDensity(context) + 0.5f).toInt()

    /**
     * px转dp
     */
    fun px2dp(context: Context, pxValue: Float) = ((pxValue / getDensity(context)) + 0.5f).toInt()

    /**
     * sp转px
     */
    fun sp2px(context: Context, spValue: Float) = ((spValue * getScaleDensity(context)) + 0.5f).toInt()

    /**
     * px转sp
     */
    fun px2sp(context: Context, pxValue: Float) = (pxValue / getScaleDensity(context) + 0.5f).toInt()


    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = 0
        try {
            var c = Class.forName("com.android.internal.R\$dimen")
            var obj = c!!.newInstance()
            var field = c.getField("status_bar_height")
            val x1 = Integer.parseInt(field!!.get(obj).toString())
            statusBarHeight = context.getResources().getDimensionPixelSize(x1)
        } catch (var7: Exception) {
            var7.printStackTrace()
        }
        return statusBarHeight
    }

    fun getAppInScreenheight(context: Context): Int {
        return getScreenHeight(context) - getStatusBarHeight(context)
    }


}