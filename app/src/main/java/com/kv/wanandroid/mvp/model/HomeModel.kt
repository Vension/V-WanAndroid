package com.kv.wanandroid.mvp.model

import com.kv.wanandroid.mvp.contract.HomeContract
import com.kv.wanandroid.mvp.model.bean.Article
import com.kv.wanandroid.mvp.model.bean.ArticleResponseBody
import com.kv.wanandroid.mvp.model.bean.Banner
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.vension.frame.http.RetrofitHelper
import com.vension.frame.rx.SchedulerUtils
import com.kv.wanandroid.api.ApiService
import io.reactivex.Observable

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/6 14:43.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class HomeModel : CommonModel(), HomeContract.Model {

    override fun requestBanner(): Observable<HttpResult<List<Banner>>> {
         return RetrofitHelper.getService(ApiService::class.java).getBanners()
             .compose(SchedulerUtils.ioToMain())
     }

    override fun requestTopArticles(): Observable<HttpResult<MutableList<Article>>> {
        return RetrofitHelper.getService(ApiService::class.java).getTopArticles()
            .compose(SchedulerUtils.ioToMain())
    }

    override fun requestArticles(num: Int): Observable<HttpResult<ArticleResponseBody>> {
        return RetrofitHelper.getService(ApiService::class.java).getArticles(num)
            .compose(SchedulerUtils.ioToMain())
    }


}