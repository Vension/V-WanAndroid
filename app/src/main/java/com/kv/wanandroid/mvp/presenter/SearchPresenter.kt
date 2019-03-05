package com.kv.wanandroid.mvp.presenter

import com.kv.wanandroid.mvp.contract.SearchContract
import com.kv.wanandroid.mvp.model.SearchModel
import com.kv.wanandroid.mvp.model.bean.SearchHistoryBean
import com.vension.frame.core.mvp.AbsPresenter
import com.vension.frame.ext.rxHandleWithModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.litepal.LitePal

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/23 14:41
 * 描  述：
 * ========================================================
 */

class SearchPresenter : AbsPresenter<SearchContract.Model, SearchContract.View>(), SearchContract.Presenter {

    override fun createModel(): SearchContract.Model? {
        return SearchModel()
    }

    override fun deleteById(id: Long) {
        doAsync {
            LitePal.delete(SearchHistoryBean::class.java, id)
        }

    }

    override fun clearAllHistory() {
        doAsync {
            LitePal.deleteAll(SearchHistoryBean::class.java)
            uiThread {

            }
        }
    }

    override fun saveSearchKey(key: String) {
        doAsync {
            val historyBean = SearchHistoryBean(key.trim())
            val beans = LitePal.where("key = '${key.trim()}'").find(SearchHistoryBean::class.java)
            if (beans.size == 0) {
                historyBean.save()
            } else {
                deleteById(beans[0].id)
                historyBean.save()
            }
        }
    }

    override fun queryHistory() {
        doAsync {
            val historyBeans = LitePal.findAll(SearchHistoryBean::class.java)
            historyBeans.reverse()
            uiThread {
                mView?.showHistoryData(historyBeans)
            }
        }
    }

    override fun getHotSearchData() {
        mModel?.getHotSearchData()?.rxHandleWithModel(mModel, mView) {
            mView?.showHotSearchData(it.data)
        }
    }

}