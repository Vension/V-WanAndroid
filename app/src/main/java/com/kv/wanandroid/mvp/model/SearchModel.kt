package com.kv.wanandroid.mvp.model

import com.kv.wanandroid.mvp.contract.SearchContract
import com.kv.wanandroid.mvp.model.bean.HotSearchBean
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.vension.frame.core.mvp.AbsModel
import com.vension.frame.http.RetrofitHelper
import com.vension.frame.rx.SchedulerUtils
import com.kv.wanandroid.api.ApiService
import io.reactivex.Observable

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/23 14:40
 * 描  述：
 * ========================================================
 */

class SearchModel : AbsModel(), SearchContract.Model {

    override fun getHotSearchData(): Observable<HttpResult<MutableList<HotSearchBean>>> {
        return RetrofitHelper.getService(ApiService::class.java).getHotSearchData()
                .compose(SchedulerUtils.ioToMain())
    }

}