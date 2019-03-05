package com.kv.wanandroid.mvp.model

import com.kv.wanandroid.mvp.contract.CollectContract
import com.kv.wanandroid.mvp.model.bean.CollectionArticle
import com.kv.wanandroid.mvp.model.bean.CollectionResponseBody
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.vension.frame.core.mvp.AbsModel
import com.vension.frame.http.RetrofitHelper
import com.vension.frame.rx.SchedulerUtils
import com.kv.wanandroid.api.ApiService
import io.reactivex.Observable

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/23 11:07
 * 描  述：
 * ========================================================
 */

class CollectModel : AbsModel(), CollectContract.Model {

    override fun getCollectList(page: Int): Observable<HttpResult<CollectionResponseBody<CollectionArticle>>> {
        return RetrofitHelper.getService(ApiService::class.java).getCollectList(page)
                .compose(SchedulerUtils.ioToMain())
    }

    override fun removeCollectArticle(id: Int, originId: Int): Observable<HttpResult<Any>> {
        return RetrofitHelper.getService(ApiService::class.java).removeCollectArticle(id, originId)
                .compose(SchedulerUtils.ioToMain())
    }

}