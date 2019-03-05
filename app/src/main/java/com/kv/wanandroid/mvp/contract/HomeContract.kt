package com.kv.wanandroid.mvp.contract

import com.kv.wanandroid.mvp.model.bean.Article
import com.kv.wanandroid.mvp.model.bean.ArticleResponseBody
import com.kv.wanandroid.mvp.model.bean.Banner
import com.kv.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/6 14:06.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
interface HomeContract {

    interface View<data> : CommonContract.View<data> {

        fun setBanners(banners:List<Banner>)

        fun setArticles(articles: ArticleResponseBody)
    }

    interface Presenter<data> : CommonContract.Presenter<data, View<data>> {

        fun requestHomeData()

        fun requestBanner()

        fun requestArticles(num: Int)

    }

    interface Model : CommonContract.Model {

        fun requestBanner(): Observable<HttpResult<List<Banner>>>

        fun requestTopArticles(): Observable<HttpResult<MutableList<Article>>>

        fun requestArticles(num: Int): Observable<HttpResult<ArticleResponseBody>>
    }

}