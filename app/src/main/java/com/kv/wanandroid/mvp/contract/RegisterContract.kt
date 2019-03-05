package com.kv.wanandroid.mvp.contract

import com.vension.frame.core.mvp.IModel
import com.vension.frame.core.mvp.IPresenter
import com.vension.frame.core.mvp.IView
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.kv.wanandroid.mvp.model.bean.LoginData
import io.reactivex.Observable


/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/21 15:50
 * 描  述：
 * ========================================================
 */

interface RegisterContract {

    interface View : IView {

        fun registerSuccess(data: LoginData)

        fun registerFail()
    }

    interface Presenter : IPresenter<View> {

        fun registerWanAndroid(username: String, password: String, repassword: String)

    }

    interface Model : IModel {
        fun registerWanAndroid(username: String, password: String, repassword: String): Observable<HttpResult<LoginData>>
    }

}