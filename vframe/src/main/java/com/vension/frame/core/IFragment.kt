package com.vension.frame.core

import android.support.annotation.IdRes
import android.view.KeyEvent
import android.view.View

/**
 * ===================================================================
 * @author: Created by Vension on 2019/2/14 16:09.
 * @email:  250685***4@qq.com
 * @update: update by *** on 2019/2/14 16:09
 * @desc:   AbsCompatFragment - 接口
 * ===================================================================
 */
interface IFragment : IAppCompatPage {

    fun <V : View> findViewById(@IdRes id: Int): V

    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean

    fun onBackPressed(): Boolean

}