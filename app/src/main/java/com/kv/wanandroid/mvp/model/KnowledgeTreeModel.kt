package com.kv.wanandroid.mvp.model

import com.kv.wanandroid.mvp.contract.KnowledgeTreeContract
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.kv.wanandroid.mvp.model.bean.KnowledgeTreeBody
import com.vension.frame.core.mvp.AbsModel
import com.vension.frame.http.RetrofitHelper
import com.vension.frame.rx.SchedulerUtils
import com.kv.wanandroid.api.ApiService
import io.reactivex.Observable

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/7 15:55.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class KnowledgeTreeModel : AbsModel(), KnowledgeTreeContract.Model {

   override fun requestKnowledgeTree(): Observable<HttpResult<List<KnowledgeTreeBody>>> {
        return RetrofitHelper.getService(ApiService::class.java).getKnowledgeTree()
            .compose(SchedulerUtils.ioToMain())
    }
}