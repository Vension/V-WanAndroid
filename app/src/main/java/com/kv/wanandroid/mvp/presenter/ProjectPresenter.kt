package com.kv.wanandroid.mvp.presenter

import com.kv.wanandroid.mvp.contract.ProjectContract
import com.kv.wanandroid.mvp.model.ProjectModel
import com.vension.frame.core.mvp.AbsPresenter
import com.vension.frame.ext.rxHandleWithModel

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/22 15:14.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class ProjectPresenter : AbsPresenter<ProjectContract.Model, ProjectContract.View>(), ProjectContract.Presenter {

    override fun createModel(): ProjectContract.Model? = ProjectModel()

    override fun requestProjectTree() {
        mModel?.requestProjectTree()?.rxHandleWithModel(mModel, mView) {
            mView?.setProjectTree(it.data)
        }
    }

}