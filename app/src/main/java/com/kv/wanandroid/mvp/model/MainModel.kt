package com.kv.wanandroid.mvp.model

import com.kv.wanandroid.mvp.contract.MainContract
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.vension.frame.core.mvp.AbsModel
import com.vension.frame.http.RetrofitHelper
import com.kv.wanandroid.api.ApiService
import io.reactivex.Observable

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/27 11:44
 * 描  述：
 * ========================================================
 */

class MainModel : AbsModel(), MainContract.Model {

    override fun logout(): Observable<HttpResult<Any>> {
        return RetrofitHelper.getService(ApiService::class.java).logout()
    }

}