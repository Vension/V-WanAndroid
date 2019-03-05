package com.vension.frame.ext

import com.vension.frame.R
import com.vension.frame.core.CoreApplication
import com.vension.frame.core.mvp.IModel
import com.vension.frame.core.mvp.IView
import com.vension.frame.http.BaseBean
import com.vension.frame.http.exception.ErrorStatus
import com.vension.frame.http.exception.ExceptionHandle
import com.vension.frame.http.function.RetryWithDelay
import com.vension.frame.rx.SchedulerUtils
import com.vension.frame.utils.NetWorkUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/26 9:25
 * 描  述：rx处理
 * ========================================================
 */


fun <T : BaseBean> Observable<T>.rxHandle(
    view: IView?,
    isShowLoading: Boolean = true,
    isShowProgressDialog: Boolean = false,
    onSuccess: (T) -> Unit
): Disposable {
    if (isShowLoading) {
        view?.showLoading()
    }
    if (isShowProgressDialog) {
        view?.showProgressDialog(false)
    }
    return this.compose(SchedulerUtils.ioToMain())
        .retryWhen(RetryWithDelay())
        .subscribe({
            when {
                it.errorCode == ErrorStatus.SUCCESS -> onSuccess.invoke(it)
                it.errorCode == ErrorStatus.TOKEN_INVAILD -> {
                    // Token 过期，重新登录
                }
                else -> view?.showMessage(it.errorMsg)
            }
            view?.hideLoading()
            view?.dismissProgressDialog()
        }, {
            view?.hideLoading()
            view?.dismissProgressDialog()
            view?.showMessage(ExceptionHandle.handleException(it))
        })
}

fun <T : BaseBean> Observable<T>.rxHandleWithModel(
    model: IModel?,
    view: IView?,
    isShowLoading: Boolean = true,
    isShowProgressDialog: Boolean = false,
    onSuccess: (T) -> Unit
) {
    return this.compose(SchedulerUtils.ioToMain())
        .retryWhen(RetryWithDelay())
        .subscribe(object : Observer<T> {

            override fun onComplete() {
                view?.hideLoading()
                view?.dismissProgressDialog()
            }

            override fun onSubscribe(d: Disposable) {
                if (isShowLoading) {
                    view?.showLoading()
                }
                if (isShowProgressDialog) {
                    view?.showProgressDialog(false)
                }
                model?.addDisposable(d)
                if (!NetWorkUtil.isNetworkConnected(CoreApplication.instance)) {
                    view?.showMessage(CoreApplication.instance.resources.getString(R.string.network_unavailable_tip))
                    onComplete()
                }
            }

            override fun onNext(t: T) {
                when {
                    t.errorCode == ErrorStatus.SUCCESS -> onSuccess.invoke(t)
                    t.errorCode == ErrorStatus.TOKEN_INVAILD -> {
                        // Token 过期，重新登录
                    }
                    else -> view?.showMessage(t.errorMsg)
                }
            }

            override fun onError(t: Throwable) {
                view?.hideLoading()
                view?.dismissProgressDialog()
                view?.showMessage(ExceptionHandle.handleException(t))
            }
        })
}


