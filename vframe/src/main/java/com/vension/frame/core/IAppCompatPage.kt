package com.vension.frame.core

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View

/**
 * ===================================================================
 * @author: Created by Vension on 2019/2/14 16:02.
 * @email:  250685***4@qq.com
 * @update: update by *** on 2019/2/14 16:02
 * @desc:   character determines attitude, attitude determines destiny
 * ===================================================================
 */
interface IAppCompatPage {

    fun getPageFragmentManager(): FragmentManager

//    fun postBackStack(fragment: Fragment)
//    fun popBackStack()

    /**
     * 是否显示通用toolBar
     *
     * @return true 默认显示
     */
    fun showToolBar(): Boolean = true

    /**
     * toolbar是否覆盖在内容区上方
     *
     * @return false 不覆盖  true 覆盖
     */
    fun isToolbarCover(): Boolean = false

    /**
     * 请求数据前是否检查网络连接
     *
     * @return false 默认不检查
     */
    fun isCheckNet(): Boolean = true

    /**
     * 是否需要显示 TipView
     */
    fun enableNetworkTip(): Boolean = true

    /**
     *  获取bundle 中的数据
     */
    fun getBundleExtras() : Bundle = Bundle()

    /**
     * 是否使用 EventBus
     */
    fun useEventBus(): Boolean = true

    /**
     * 延时
     */
    fun postDelay(delayMillis: Long, block: () -> Unit)

    fun startProxyActivity(_Class: Class<out Fragment>)
    fun startProxyActivity(_Class: Class<out Fragment>, view: View?)
    fun startProxyActivity(_Class: Class<out Fragment>, bundle: Bundle)
    fun startProxyActivity(_Class: Class<out Fragment>, bundle: Bundle, view: View?)
    fun startProxyActivityForResult(_Class: Class<out Fragment>, requestCode: Int)
    fun startProxyActivityForResult(_Class: Class<out Fragment>, view: View?, requestCode: Int)
    fun startProxyActivityForResult(_Class: Class<out Fragment>, bundle: Bundle, requestCode: Int)
    fun startProxyActivityForResult(_Class: Class<out Fragment>, bundle: Bundle, view: View?, requestCode: Int)

}