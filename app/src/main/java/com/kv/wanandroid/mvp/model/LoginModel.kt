package com.kv.wanandroid.mvp.model

import com.kv.wanandroid.mvp.contract.LoginContract
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.kv.wanandroid.mvp.model.bean.LoginData
import com.vension.frame.core.mvp.AbsModel
import com.vension.frame.http.RetrofitHelper
import com.vension.frame.rx.SchedulerUtils
import com.kv.wanandroid.api.ApiService
import io.reactivex.Observable

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/21 15:32.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class LoginModel : AbsModel() , LoginContract.Model{

    override fun loginWanAndroid(username: String, password: String): Observable<HttpResult<LoginData>> {
        return RetrofitHelper.getService(ApiService::class.java).loginWanAndroid(username, password)
            .compose(SchedulerUtils.ioToMain())
    }

}