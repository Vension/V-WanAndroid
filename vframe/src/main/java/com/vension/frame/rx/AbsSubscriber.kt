package com.vension.frame.rx

import com.vension.frame.core.CoreApplication
import com.vension.frame.utils.NetWorkUtil
import com.vension.frame.core.mvp.IView
import com.vension.frame.http.BaseBean
import com.vension.frame.http.exception.ErrorStatus
import com.vension.frame.http.exception.ExceptionHandle
import io.reactivex.subscribers.ResourceSubscriber

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/6 14:24
 * 描  述：Rx2.x 订阅者
 * ========================================================
 */

abstract class AbsSubscriber<T : BaseBean>(view: IView? = null) : ResourceSubscriber<T>() {

    private var mView = view

    abstract fun onSuccess(t: T)

    override fun onComplete() {
        mView?.hideLoading()
        mView?.dismissProgressDialog()
    }

    override fun onStart() {
        super.onStart()
        mView?.showLoading()
        if (!NetWorkUtil.isNetworkConnected(CoreApplication.instance)) {
            mView?.showMessage("网络连接不可用,请检查网络设置!")
            onComplete()
        }
    }

    override fun onNext(t: T) {
        when {
            t.errorCode == ErrorStatus.SUCCESS -> onSuccess(t)
            t.errorCode == ErrorStatus.TOKEN_INVAILD -> {
                // Token 过期，重新登录
            }
            else -> mView?.showMessage(t.errorMsg)
        }
    }


    override fun onError(t: Throwable) {
        mView?.hideLoading()
        mView?.dismissProgressDialog()
        ExceptionHandle.handleException(t)
    }
}
