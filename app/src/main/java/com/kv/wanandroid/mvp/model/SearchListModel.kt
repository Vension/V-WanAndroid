package com.kv.wanandroid.mvp.model

import com.kv.wanandroid.mvp.contract.SearchListContract
import com.kv.wanandroid.mvp.model.bean.ArticleResponseBody
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.vension.frame.http.RetrofitHelper
import com.vension.frame.rx.SchedulerUtils
import com.kv.wanandroid.api.ApiService
import io.reactivex.Observable

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/23 16:22
 * 描  述：
 * ========================================================
 */

class SearchListModel : CommonModel(), SearchListContract.Model {

    override fun queryBySearchKey(page: Int, key: String): Observable<HttpResult<ArticleResponseBody>> {
        return RetrofitHelper.getService(ApiService::class.java).queryBySearchKey(page, key)
                .compose(SchedulerUtils.ioToMain())
    }

}