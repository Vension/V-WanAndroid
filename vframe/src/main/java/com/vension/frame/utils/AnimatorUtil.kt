package com.vension.frame.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/1 18:06
 * 描  述：动画相关工具类
 * ========================================================
 */

object AnimatorUtil {

    private val FAST_OUT_SLOW_IN_INTERPOLATOR: LinearOutSlowInInterpolator by lazy {
        LinearOutSlowInInterpolator()
    }

    private val LINER_INTERPOLATOR: AccelerateInterpolator by lazy {
        AccelerateInterpolator()
    }

    /**
     * 显示View
     * @param view View
     * @param listener ViewPropertyAnimatorListener
     */
    fun scaleShow(view: View, listener: ViewPropertyAnimatorListener) {
        view.visibility = View.VISIBLE
        ViewCompat.animate(view)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1.0f)
                .setDuration(800)
                .setListener(listener)
                .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                .start()
    }

    /**
     * 隐藏View
     * @param view View
     * @param listener ViewPropertyAnimatorListener
     */
    fun scaleHide(view: View, listener: ViewPropertyAnimatorListener) {
        ViewCompat.animate(view)
                .scaleX(0.0f)
                .scaleY(0.0f)
                .alpha(0.0f)
                .setDuration(800)
                .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                .setListener(listener)
                .start()
    }

    /**
     * 显示view
     *
     * @param view View
     * @param listener ViewPropertyAnimatorListener
     */
    fun translateShow(view: View, listener: ViewPropertyAnimatorListener) {
        view.visibility = View.VISIBLE
        ViewCompat.animate(view)
                .translationY(0f)
                .setDuration(400)
                .setListener(listener)
                .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                .start()
    }

    /**
     * 隐藏view
     *
     * @param view View
     * @param listener ViewPropertyAnimatorListener
     */
    fun translateHide(view: View, listener: ViewPropertyAnimatorListener) {
        view.visibility = View.VISIBLE
        ViewCompat.animate(view)
                .translationY(350f)
                .setDuration(400)
                .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                .setListener(listener)
                .start()
    }

    interface OnRevealAnimationListener {
        fun onRevealHide()

        fun onRevealShow()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun animateRevealShow(
        context: Context, view: View,
        startRadius: Int, @ColorRes color: Int,
        listener: OnRevealAnimationListener) {
        val cx = (view.left + view.right) / 2
        val cy = (view.top + view.bottom) / 2

        val finalRadius = Math.hypot(view.width.toDouble(), view.height.toDouble()).toFloat()

        // 设置圆形显示动画
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, startRadius.toFloat(), finalRadius)
        anim.duration = 300
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.visibility = View.VISIBLE
                listener.onRevealShow()
            }

            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                view.setBackgroundColor(ContextCompat.getColor(context, color))
            }
        })

        anim.start()
    }

    // 圆圈凝聚效果
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun animateRevealHide(
        context: Context, view: View,
        finalRadius: Int, @ColorRes color: Int,
        listener: OnRevealAnimationListener
    ) {
        val cx = (view.left + view.right) / 2
        val cy = (view.top + view.bottom) / 2
        val initialRadius = view.width
        // 与入场动画的区别就是圆圈起始和终止的半径相反
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius.toFloat(), finalRadius.toFloat())
        anim.duration = 300
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                view.setBackgroundColor(ContextCompat.getColor(context, color))
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                listener.onRevealHide()
                view.visibility = View.INVISIBLE
            }
        })
        anim.start()
    }

}