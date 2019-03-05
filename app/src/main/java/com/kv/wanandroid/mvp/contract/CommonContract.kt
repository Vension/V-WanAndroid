package com.kv.wanandroid.mvp.contract

import com.vension.frame.core.mvp.IModel
import com.vension.frame.core.mvp.IPresenter
import com.vension.frame.core.mvp.IViewRefresh
import com.kv.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable


/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/6 14:06
 * 描  述：
 * ========================================================
 */

interface CommonContract {

    interface View<data> : IViewRefresh<data> {

        fun showCollectSuccess(success: Boolean)

        fun showCancelCollectSuccess(success: Boolean)
    }

    interface Presenter<data,in V : View<data>> : IPresenter<V> {

        fun addCollectArticle(id: Int)

        fun cancelCollectArticle(id: Int)

    }

    interface Model : IModel {

        fun addCollectArticle(id: Int): Observable<HttpResult<Any>>

        fun cancelCollectArticle(id: Int): Observable<HttpResult<Any>>

    }

}