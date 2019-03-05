package com.kv.wanandroid.mvp.presenter

import com.kv.wanandroid.mvp.contract.HomeContract
import com.kv.wanandroid.mvp.model.HomeModel
import com.kv.wanandroid.mvp.model.bean.Article
import com.kv.wanandroid.mvp.model.bean.ArticleResponseBody
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.kv.wanandroid.util.SettingUtil
import com.vension.frame.ext.rxHandleWithModel
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/6 14:38.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class HomePresenter : CommonPresenter<Article, HomeContract.Model, HomeContract.View<Article>>(), HomeContract.Presenter<Article>{

    override fun createModel(): HomeContract.Model? {
        return HomeModel()
    }

    override fun requestHomeData() {
        requestBanner()
        val observable = if (SettingUtil.getIsShowTopArticle()) {
            mModel?.requestArticles(0)
        } else {
            Observable.zip(mModel?.requestTopArticles(), mModel?.requestArticles(0),
                BiFunction<HttpResult<MutableList<Article>>, HttpResult<ArticleResponseBody>,
                        HttpResult<ArticleResponseBody>> { t1, t2 ->
                    t1.data.forEach {
                        // 置顶数据中没有标识，手动添加一个标识
                        it.top = "1"
                    }
                    t2.data.datas.addAll(0, t1.data)
                    t2
                })
        }
        observable?.rxHandleWithModel(mModel, mView,true) {
            mView?.setArticles(it.data)
        }
    }

    override fun requestBanner() {
        mModel?.requestBanner()?.rxHandleWithModel(mModel, mView, false) {
            mView?.setBanners(it.data)
        }
    }

    override fun requestArticles(num: Int) {
        mModel?.requestArticles(num)?.rxHandleWithModel(mModel, mView, false) {
            mView?.setArticles(it.data)
        }
    }

//    override fun requestBanner() {
//        val disposable = homeModel.requestBanners()
//            .retryWhen(RetryWithDelay())
//            .subscribe({ results ->
//                mView?.apply {
//                    if (results.errorCode != 0) {
//                        showMessage(results.errorMsg)
//                    } else {
//                        setBanners(results.data)
//                    }
//                    hideLoading()
//                }
//            }, { t ->
//                mView?.apply {
//                    hideLoading()
//                    showMessage(ExceptionHandle.handleException(t))
//                }
//            })
//        addSubscription(disposable)
//    }

//    override fun requestArticles(num: Int) {
//        if (num == 0)
//            mView?.showLoading()
//        val disposable = homeModel.requestArticles(num)
//            .retryWhen(RetryWithDelay())
//            .subscribe({ results ->
//                mView?.apply {
//                    if (results.errorCode != 0) {
//                        showMessage(results.errorMsg)
//                    } else {
//                        setArticles(results.data)
//                    }
//                    hideLoading()
//                }
//            }, { t ->
//                mView?.apply {
//                    hideLoading()
//                    showMessage(ExceptionHandle.handleException(t))
//                }
//            })
//        addSubscription(disposable)
//    }

}