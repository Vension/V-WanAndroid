package com.kv.wanandroid.mvp.model

import com.kv.wanandroid.mvp.contract.CommonContract
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.vension.frame.core.mvp.AbsModel
import com.vension.frame.http.RetrofitHelper
import com.vension.frame.rx.SchedulerUtils
import com.kv.wanandroid.api.ApiService
import io.reactivex.Observable

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/6 14:14
 * 描  述：
 * ========================================================
 */

open class CommonModel : AbsModel(), CommonContract.Model  {

    override fun addCollectArticle(id: Int): Observable<HttpResult<Any>> {
        return RetrofitHelper.getService(ApiService::class.java).addCollectArticle(id)
                .compose(SchedulerUtils.ioToMain())
    }

    override fun cancelCollectArticle(id: Int): Observable<HttpResult<Any>> {
        return RetrofitHelper.getService(ApiService::class.java).cancelCollectArticle(id)
                .compose(SchedulerUtils.ioToMain())
    }

}