package com.kv.wanandroid.mvp.presenter

import com.kv.wanandroid.mvp.contract.ContentContract
import com.kv.wanandroid.mvp.model.ContentModel
import com.vension.frame.http.BaseBean


/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/27 12:17
 * 描  述：
 * ========================================================
 */

class ContentPresenter : CommonPresenter<BaseBean, ContentContract.Model, ContentContract.View<BaseBean>>(), ContentContract.Presenter<BaseBean> {


    override fun createModel(): ContentContract.Model? = ContentModel()

}