package com.kv.wanandroid.mvp.model

import com.kv.wanandroid.mvp.contract.ProjectListContract
import com.kv.wanandroid.mvp.model.bean.ArticleResponseBody
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.vension.frame.http.RetrofitHelper
import com.vension.frame.rx.SchedulerUtils
import com.kv.wanandroid.api.ApiService
import io.reactivex.Observable

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/22 15:42.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class ProjectListModel : CommonModel(), ProjectListContract.Model {

    override fun requestProjectList(page: Int, cid: Int): Observable<HttpResult<ArticleResponseBody>> {
        return RetrofitHelper.getService(ApiService::class.java).getProjectList(page, cid)
            .compose(SchedulerUtils.ioToMain())
    }

}