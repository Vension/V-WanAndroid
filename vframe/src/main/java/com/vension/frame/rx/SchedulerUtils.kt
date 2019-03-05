package com.vension.frame.rx

import com.vension.frame.rx.scheduler.IoMainScheduler

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/6 14:19
 * 描  述：
 * ========================================================
 */

object SchedulerUtils {

    fun <T> ioToMain(): IoMainScheduler<T> {
        return IoMainScheduler()
    }

}