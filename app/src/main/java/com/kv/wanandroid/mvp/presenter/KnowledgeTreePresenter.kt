package com.kv.wanandroid.mvp.presenter

import com.kv.wanandroid.mvp.contract.KnowledgeTreeContract
import com.kv.wanandroid.mvp.model.KnowledgeTreeModel
import com.kv.wanandroid.mvp.model.bean.KnowledgeTreeBody
import com.vension.frame.core.mvp.AbsPresenter
import com.vension.frame.ext.rxHandleWithModel

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/7 15:53.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class KnowledgeTreePresenter : AbsPresenter<KnowledgeTreeContract.Model, KnowledgeTreeContract.View>(),
    KnowledgeTreeContract.Presenter{

    override fun createModel(): KnowledgeTreeContract.Model? {
        return KnowledgeTreeModel()
    }

    override fun requestKnowledgeTree() {
        mModel?.requestKnowledgeTree()?.rxHandleWithModel(mModel, mView,false) {
            mView?.setRefreshData(it.data as MutableList<KnowledgeTreeBody>)
        }
    }

}