package com.kv.wanandroid.mvp.presenter

import com.kv.wanandroid.mvp.contract.MainContract
import com.kv.wanandroid.mvp.model.MainModel
import com.vension.frame.core.mvp.AbsPresenter
import com.vension.frame.ext.rxHandleWithModel


/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/27 11:45
 * 描  述：
 * ========================================================
 */

class MainPresenter : AbsPresenter<MainContract.Model, MainContract.View>(), MainContract.Presenter {

    override fun createModel(): MainContract.Model? = MainModel()

    override fun logout() {
        mModel?.logout()?.rxHandleWithModel(mModel, mView) {
            mView?.showLogoutSuccess(success = true)
        }
    }
}