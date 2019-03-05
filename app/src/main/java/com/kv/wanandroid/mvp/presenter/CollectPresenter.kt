package com.kv.wanandroid.mvp.presenter

import com.kv.wanandroid.mvp.contract.CollectContract
import com.kv.wanandroid.mvp.model.CollectModel
import com.vension.frame.core.mvp.AbsPresenter
import com.vension.frame.ext.rxHandleWithModel


/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/23 11:08
 * 描  述：
 * ========================================================
 */

class CollectPresenter : AbsPresenter<CollectContract.Model, CollectContract.View>(), CollectContract.Presenter {

    override fun createModel(): CollectContract.Model? {
        return CollectModel()
    }

    override fun getCollectList(page: Int) {
        mModel?.getCollectList(page)?.rxHandleWithModel(mModel, mView,false) {
            mView?.setCollectList(page,it.data)
        }
    }

//    override fun getCollectList(page: Int) {
//        mView?.showLoading()
//        val disposable = collectModel.getCollectList(page)
//                .retryWhen(RetryWithDelay())
//                .subscribe({ results ->
//                    mView?.run {
//                        if (results.errorCode != 0) {
//                            showMessage(results.errorMsg)
//                        } else {
//                            setCollectList(page,results.data)
//                        }
//                        hideLoading()
//                    }
//                }, { t ->
//                    mView?.apply {
//                        hideLoading()
//                        showMessage(ExceptionHandle.handleException(t))
//                    }
//                })
//        addSubscription(disposable)
//    }

    override fun removeCollectArticle(id: Int, originId: Int) {
        mModel?.removeCollectArticle(id, originId)?.rxHandleWithModel(mModel,mView,false) {
            mView?.showRemoveCollectSuccess(true)
        }
//        val disposable = collectModel.removeCollectArticle(id, originId)
//                .retryWhen(RetryWithDelay())
//                .subscribe({ results ->
//                    mView?.run {
//                        if (results.errorCode != 0) {
//                            showMessage(results.errorMsg)
//                        } else {
//                            showRemoveCollectSuccess(true)
//                        }
//                        hideLoading()
//                    }
//                }, { t ->
//                    mView?.apply {
//                        hideLoading()
//                        showMessage(ExceptionHandle.handleException(t))
//                    }
//                })
//        addSubscription(disposable)
    }

}