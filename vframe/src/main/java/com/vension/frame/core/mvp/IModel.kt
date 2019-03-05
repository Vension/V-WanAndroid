package com.vension.frame.core.mvp

import io.reactivex.disposables.Disposable

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/10/31 13:38
 * 描  述：MVP-Model-接口
 * ========================================================
 */

interface IModel {

    fun addDisposable(disposable: Disposable?)

    fun onDetach()

}