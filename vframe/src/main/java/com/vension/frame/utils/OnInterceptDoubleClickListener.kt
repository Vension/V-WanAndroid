package com.vension.frame.utils

import android.view.View

/**
 * ===================================================================
 * @author: Created by Vension on 2019/2/14 16:08.
 * @email:  250685***4@qq.com
 * @update: update by *** on 2019/2/14 16:08
 * @desc:   防止连续点击
 * ===================================================================
 */
abstract class OnInterceptDoubleClickListener : View.OnClickListener {

    private var mThrottleFirstTime: Long = 500
    private var mLastClickTime: Long = 0

    constructor()
    constructor(throttleFirstTime: Long) {
        mThrottleFirstTime = throttleFirstTime
    }

    override fun onClick(v: View?) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - mLastClickTime > mThrottleFirstTime) {
            mLastClickTime = currentTime
            onInterceptDoubleClick(v)
        }
    }

    /**拦截双击事件*/
    abstract fun onInterceptDoubleClick(v: View?)

}