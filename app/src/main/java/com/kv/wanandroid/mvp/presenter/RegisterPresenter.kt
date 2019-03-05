package com.kv.wanandroid.mvp.presenter

import com.kv.wanandroid.mvp.contract.RegisterContract
import com.kv.wanandroid.mvp.model.RegisterModel
import com.vension.frame.core.mvp.AbsPresenter
import com.vension.frame.ext.rxHandleWithModel


/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/21 15:55
 * 描  述：
 * ========================================================
 */

class RegisterPresenter : AbsPresenter<RegisterContract.Model, RegisterContract.View>(), RegisterContract.Presenter {

    override fun createModel(): RegisterContract.Model? {
        return RegisterModel()
    }
    override fun registerWanAndroid(username: String, password: String, repassword: String) {
        mModel?.registerWanAndroid(username, password, repassword)?.rxHandleWithModel(mModel, mView, false, true) {
            mView?.apply {
                if (it.errorCode != 0) {
                    registerFail()
                } else {
                    registerSuccess(it.data)
                }
            }
        }
    }

}