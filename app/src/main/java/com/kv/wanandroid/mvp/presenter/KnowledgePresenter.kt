package com.kv.wanandroid.mvp.presenter

import com.kv.wanandroid.mvp.contract.KnowledgeContract
import com.kv.wanandroid.mvp.model.KnowledgeModel
import com.kv.wanandroid.mvp.model.bean.Article
import com.vension.frame.ext.rxHandleWithModel

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/21 11:06.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class KnowledgePresenter : CommonPresenter<Article, KnowledgeContract.Model, KnowledgeContract.View<Article>>(),
    KnowledgeContract.Presenter<Article>{


    override fun createModel(): KnowledgeContract.Model? {
        return KnowledgeModel()
    }

    override fun requestKnowledgeList(page: Int, cid: Int) {
        mModel?.requestKnowledgeList(page, cid)?.rxHandleWithModel(mModel, mView,false) {
            mView?.setKnowledgeList(page,it.data)
        }
    }

}