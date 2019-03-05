package com.kv.wanandroid.mvp.presenter

import com.kv.wanandroid.mvp.contract.WeChatContract
import com.kv.wanandroid.mvp.model.WeChatModel
import com.vension.frame.core.mvp.AbsPresenter
import com.vension.frame.ext.rxHandleWithModel


/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/23 10:37
 * 描  述：
 * ========================================================
 */

class WeChatPresenter : AbsPresenter<WeChatContract.Model, WeChatContract.View>(), WeChatContract.Presenter {

    override fun createModel(): WeChatContract.Model? = WeChatModel()

    override fun getWXChapters() {
        mModel?.getWXChapters()?.rxHandleWithModel(mModel, mView) {
            mView?.showWXChapters(it.data)
        }
    }

}