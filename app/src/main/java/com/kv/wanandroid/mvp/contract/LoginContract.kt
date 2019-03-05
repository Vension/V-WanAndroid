package com.kv.wanandroid.mvp.contract

import com.vension.frame.core.mvp.IModel
import com.vension.frame.core.mvp.IPresenter
import com.vension.frame.core.mvp.IView
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.kv.wanandroid.mvp.model.bean.LoginData
import io.reactivex.Observable

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/21 15:29.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
interface LoginContract {

    interface View : IView {
        fun loginSuccess(data: LoginData)
        fun loginFail()
    }


    interface Presenter : IPresenter<View> {
        fun loginWanAndroid(username: String, password: String)
    }

    interface Model : IModel {

        fun loginWanAndroid(username: String, password: String): Observable<HttpResult<LoginData>>

    }

}