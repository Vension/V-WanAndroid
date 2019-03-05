package com.kv.wanandroid.mvp.contract

import com.vension.frame.core.mvp.IModel
import com.vension.frame.core.mvp.IPresenter
import com.vension.frame.core.mvp.IViewRefresh
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.kv.wanandroid.mvp.model.bean.KnowledgeTreeBody
import io.reactivex.Observable

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/7 15:39.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
interface KnowledgeTreeContract {

    interface View : IViewRefresh<KnowledgeTreeBody> {

    }

    interface Presenter : IPresenter<View> {

        fun requestKnowledgeTree()

    }

    interface Model : IModel {

        fun requestKnowledgeTree(): Observable<HttpResult<List<KnowledgeTreeBody>>>

    }

}