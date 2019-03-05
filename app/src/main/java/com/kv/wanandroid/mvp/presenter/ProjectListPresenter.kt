package com.kv.wanandroid.mvp.presenter

import com.kv.wanandroid.mvp.contract.ProjectListContract
import com.kv.wanandroid.mvp.model.ProjectListModel
import com.kv.wanandroid.mvp.model.bean.Article
import com.vension.frame.ext.rxHandleWithModel

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/22 15:43.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class ProjectListPresenter : CommonPresenter<Article, ProjectListContract.Model, ProjectListContract.View<Article>>(),
    ProjectListContract.Presenter<Article>{


    override fun createModel(): ProjectListContract.Model? {
        return ProjectListModel()
    }

    private val projectListModel: ProjectListModel by lazy {
        ProjectListModel()
    }

    override fun requestProjectList(page: Int, cid: Int) {
        mModel?.requestProjectList(page, cid)?.rxHandleWithModel(mModel, mView, false) {
            mView?.setProjectList(page, it.data)
        }
    }

}