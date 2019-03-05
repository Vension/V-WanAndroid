package com.kv.wanandroid.mvp.contract

import com.vension.frame.core.mvp.IModel
import com.vension.frame.core.mvp.IPresenter
import com.vension.frame.core.mvp.IView
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.kv.wanandroid.mvp.model.bean.ProjectTreeBean
import io.reactivex.Observable

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/22 15:11.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
interface ProjectContract {

    interface View : IView {
        fun setProjectTree(list: List<ProjectTreeBean>)
    }

    interface Presenter : IPresenter<View> {
        fun requestProjectTree()
    }

    interface Model : IModel {
        fun requestProjectTree(): Observable<HttpResult<List<ProjectTreeBean>>>
    }

}