package com.kv.wanandroid.mvp.presenter

import com.kv.wanandroid.mvp.contract.LoginContract
import com.kv.wanandroid.mvp.model.LoginModel
import com.vension.frame.core.mvp.AbsPresenter
import com.vension.frame.ext.rxHandleWithModel

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/21 15:33.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class LoginPresenter : AbsPresenter<LoginContract.Model, LoginContract.View>(), LoginContract.Presenter {

    override fun createModel(): LoginContract.Model? = LoginModel()

    override fun loginWanAndroid(username: String, password: String) {
        mModel?.loginWanAndroid(username, password)?.rxHandleWithModel(mModel, mView,false,true) {
            mView?.loginSuccess(it.data)
        }
    }

}