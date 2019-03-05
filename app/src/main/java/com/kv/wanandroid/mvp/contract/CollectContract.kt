package com.kv.wanandroid.mvp.contract

import com.vension.frame.core.mvp.IModel
import com.vension.frame.core.mvp.IPresenter
import com.vension.frame.core.mvp.IViewRefresh
import com.kv.wanandroid.mvp.model.bean.CollectionArticle
import com.kv.wanandroid.mvp.model.bean.CollectionResponseBody
import com.kv.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/23 11:06.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
interface CollectContract {

    interface View : IViewRefresh<CollectionArticle> {

        fun setCollectList(page:Int,articles: CollectionResponseBody<CollectionArticle>)

        fun showRemoveCollectSuccess(success: Boolean)

    }

    interface Presenter : IPresenter<View> {

        fun getCollectList(page: Int)

        fun removeCollectArticle(id: Int, originId: Int)

    }


    interface Model : IModel {

        fun getCollectList(page: Int): Observable<HttpResult<CollectionResponseBody<CollectionArticle>>>

        fun removeCollectArticle(id: Int, originId: Int): Observable<HttpResult<Any>>

    }

}