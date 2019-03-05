package com.vension.frame.core.mvp

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/2 14:41
 * 描  述：MVP-View-接口
 *        为了满足部分人的诉求以及向下兼容, {@link IView} 中的部分方法使用 JAVA 1.8 的默认方法实现,
 *        这样实现类可以按实际需求选择是否实现某些方法,不实现则使用默认方法中的逻辑
 *
 * ========================================================
 */

interface IView {


    /**
     * 显示进度条
     */
    fun showProgressDialog(cancelable: Boolean)

    /**
     * 隐藏进度条
     */
    fun dismissProgressDialog()

    /**
     * 显示加载
     */
    fun showLoading()

    /**
     * 隐藏加载
     */
    fun hideLoading()

    /**
     * 显示信息
     *
     * @param message 消息内容
     */
    fun showMessage(message: String = "请求失败")


}