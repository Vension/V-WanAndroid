package com.kv.wanandroid.mvp.contract

import com.kv.wanandroid.mvp.model.bean.ArticleResponseBody
import com.kv.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/23 16:21.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
interface SearchListContract {

    interface View<data> : CommonContract.View<data> {
        fun showArticles(page: Int,articles: ArticleResponseBody)
    }

    interface Presenter<data> : CommonContract.Presenter<data, View<data>> {
        fun queryBySearchKey(page: Int, key: String)
    }

    interface Model : CommonContract.Model {

        fun queryBySearchKey(page: Int, key: String): Observable<HttpResult<ArticleResponseBody>>

    }

}