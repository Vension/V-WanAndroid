package com.kv.wanandroid.mvp.model

import com.kv.wanandroid.mvp.contract.ProjectContract
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.kv.wanandroid.mvp.model.bean.ProjectTreeBean
import com.vension.frame.core.mvp.AbsModel
import com.vension.frame.http.RetrofitHelper
import com.vension.frame.rx.SchedulerUtils
import com.kv.wanandroid.api.ApiService
import io.reactivex.Observable

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/22 15:13.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class ProjectModel : AbsModel(), ProjectContract.Model {

    override fun requestProjectTree(): Observable<HttpResult<List<ProjectTreeBean>>> {
        return RetrofitHelper.getService(ApiService::class.java).getProjectTree()
            .compose(SchedulerUtils.ioToMain())
    }

}