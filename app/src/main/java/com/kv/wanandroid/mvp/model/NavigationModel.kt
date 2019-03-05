package com.kv.wanandroid.mvp.model

import com.kv.wanandroid.mvp.contract.NavigationContract
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.kv.wanandroid.mvp.model.bean.NavigationBean
import com.vension.frame.core.mvp.AbsModel
import com.vension.frame.http.RetrofitHelper
import com.vension.frame.rx.SchedulerUtils
import com.kv.wanandroid.api.ApiService
import io.reactivex.Observable

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/22 16:45
 * 描  述：
 * ========================================================
 */

class NavigationModel : AbsModel(), NavigationContract.Model {

    override fun requestNavigationList(): Observable<HttpResult<List<NavigationBean>>> {
        return RetrofitHelper.getService(ApiService::class.java).getNavigationList()
                .compose(SchedulerUtils.ioToMain())
    }

}