package com.vension.frame.core

/**
 * ===================================================================
 * @author: Created by Vension on 2019/2/14 16:06.
 * @email:  250685***4@qq.com
 * @update: update by *** on 2019/2/14 16:06
 * @desc:   AbsCompatActivity - 接口
 * ===================================================================
 */
interface IActivity : IAppCompatPage {

    /**
     * 是否支持侧滑返回
     *
     * @return true 默认开启
     */
    fun isSupportSwipeBack(): Boolean = true

}