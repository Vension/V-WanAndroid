package com.kv.wanandroid.mvp.model

import com.kv.wanandroid.mvp.contract.KnowledgeContract
import com.kv.wanandroid.mvp.model.bean.ArticleResponseBody
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.vension.frame.http.RetrofitHelper
import com.vension.frame.rx.SchedulerUtils
import com.kv.wanandroid.api.ApiService
import io.reactivex.Observable

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/21 11:04.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class KnowledgeModel : CommonModel() , KnowledgeContract.Model {

    override fun requestKnowledgeList(page: Int, cid: Int): Observable<HttpResult<ArticleResponseBody>> {
        return RetrofitHelper.getService(ApiService::class.java).getKnowledgeList(page, cid)
            .compose(SchedulerUtils.ioToMain())
    }

}