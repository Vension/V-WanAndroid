package com.kv.wanandroid.mvp.presenter

import com.kv.wanandroid.mvp.contract.SearchListContract
import com.kv.wanandroid.mvp.model.SearchListModel
import com.kv.wanandroid.mvp.model.bean.Article
import com.vension.frame.ext.rxHandleWithModel


class SearchListPresenter : CommonPresenter<Article, SearchListContract.Model, SearchListContract.View<Article>>(), SearchListContract.Presenter<Article> {


    override fun createModel(): SearchListContract.Model? {
        return SearchListModel()
    }

    override fun queryBySearchKey(page: Int, key: String) {
        mModel?.queryBySearchKey(page, key)?.rxHandleWithModel(mModel, mView, page == 0) {
             mView?.showArticles(page,it.data)
        }
    }

}