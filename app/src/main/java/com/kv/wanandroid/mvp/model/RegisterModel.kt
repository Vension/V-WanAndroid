package com.kv.wanandroid.mvp.model

import com.kv.wanandroid.mvp.contract.RegisterContract
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.kv.wanandroid.mvp.model.bean.LoginData
import com.vension.frame.core.mvp.AbsModel
import com.vension.frame.http.RetrofitHelper
import com.vension.frame.rx.SchedulerUtils
import com.kv.wanandroid.api.ApiService
import io.reactivex.Observable

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/21 15:55
 * 描  述：
 * ========================================================
 */

class RegisterModel : AbsModel(), RegisterContract.Model {

    override fun registerWanAndroid(username: String, password: String, repassword: String): Observable<HttpResult<LoginData>> {
        return RetrofitHelper.getService(ApiService::class.java).registerWanAndroid(username, password, repassword)
                .compose(SchedulerUtils.ioToMain())
    }

}