package com.kv.wanandroid.mvp.model

import com.kv.wanandroid.mvp.contract.TodoAddContract
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.vension.frame.core.mvp.AbsModel
import com.vension.frame.http.RetrofitHelper
import com.kv.wanandroid.api.ApiService
import io.reactivex.Observable

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/26 17:55
 * 描  述：
 * ========================================================
 */

class TodoAddModel : AbsModel(), TodoAddContract.Model {

    override fun addTodo(map: MutableMap<String, Any>): Observable<HttpResult<Any>> {
        return RetrofitHelper.getService(ApiService::class.java).addTodo(map)
    }

    override fun updateTodo(id: Int, map: MutableMap<String, Any>): Observable<HttpResult<Any>> {
        return RetrofitHelper.getService(ApiService::class.java).updateTodo(id, map)
    }

}