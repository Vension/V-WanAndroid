package com.kv.wanandroid.mvp.presenter

import com.kv.wanandroid.mvp.contract.CommonContract
import com.vension.frame.core.mvp.AbsPresenter
import com.vension.frame.ext.rxHandleWithModel


/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/6 14:39
 * 描  述：
 * ========================================================
 */

open class CommonPresenter<data,M : CommonContract.Model,V : CommonContract.View<data>>
    : AbsPresenter<M, V>(), CommonContract.Presenter<data,V> {


    override fun addCollectArticle(id: Int) {
        mModel?.addCollectArticle(id)?.rxHandleWithModel(mModel, mView,false,true) {
            mView?.showCollectSuccess(true)
        }
    }

    override fun cancelCollectArticle(id: Int) {
        mModel?.cancelCollectArticle(id)?.rxHandleWithModel(mModel, mView,false,true) {
            mView?.showCancelCollectSuccess(true)
        }
    }

}