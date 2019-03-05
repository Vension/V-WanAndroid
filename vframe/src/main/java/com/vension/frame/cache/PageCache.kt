package com.vension.frame.cache

import android.app.Activity
import android.support.v4.app.Fragment
import java.util.*

/**
 * ===================================================================
 * @author: Created by Vension on 2019/2/14 16:15.
 * @email:  250685***4@qq.com
 * @update: update by *** on 2019/2/14 16:15
 * @desc:   页面堆栈缓存
 * ===================================================================
 */
open class PageCache <T : Any> internal constructor() {

    private val fObserver = Stack<T>()

    val observerList: List<T>
        @Synchronized get() = fObserver

    val lastObserver: T
        @Synchronized get() = fObserver.lastElement()

    private object PageHelper {
        internal val PAGE_FRAGMENT_CACHE = FragmentACache()
        internal val PAGE_ACTIVITY_CACHE = ActivityACache()
    }

    @Synchronized
    fun add(t: T): Boolean {
        return !fObserver.contains(t) && fObserver.add(t)
    }

    @Synchronized
    fun remove(t: T): Boolean {
        return fObserver.contains(t) && fObserver.remove(t)
    }

    @Synchronized
    fun remove(position: Int): Boolean {
        return fObserver.removeAt(position) != null
    }

    @Synchronized
    fun getObserver(_Class: Class<out T>): T? {
        for (t in fObserver) {
            if (t.javaClass.name == _Class.name) {
                return t
            }
        }
        return null
    }

    @Synchronized
    fun clear() {
        if (!fObserver.isEmpty()) {
            fObserver.clear()
        }
    }

    @Synchronized
    fun size(): Int {
        return fObserver.size
    }


    class FragmentACache : PageCache<Fragment>()

    class ActivityACache : PageCache<Activity>() {

        /**程序退出 */
        @Synchronized
        fun quit() {
            for (mActivity in observerList) {
                if (!mActivity.isFinishing) {
                    mActivity.finish()
                }
            }
            super.clear()
            // 杀死进程
            android.os.Process.killProcess(android.os.Process.myPid())
            // 退出程序
            System.exit(0)
            // 通知系统回收
            System.gc()
        }
    }

    companion object {

        val pageFragmentCache: FragmentACache
            get() = PageHelper.PAGE_FRAGMENT_CACHE

        val pageActivityCache: ActivityACache
            get() = PageHelper.PAGE_ACTIVITY_CACHE
    }

}
