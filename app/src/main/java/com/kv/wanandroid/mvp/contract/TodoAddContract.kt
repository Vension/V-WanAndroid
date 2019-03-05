package com.kv.wanandroid.mvp.contract

import com.vension.frame.core.mvp.IModel
import com.vension.frame.core.mvp.IPresenter
import com.vension.frame.core.mvp.IView
import com.kv.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/26 12:15.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
interface TodoAddContract {

    interface View : IView {

        fun showAddTodo(success: Boolean)

        fun showUpdateTodo(success: Boolean)

        fun getType(): Int
        fun getCurrentDate(): String
        fun getTitle(): String
        fun getContent(): String
        fun getStatus(): Int
        fun getItemId(): Int
        fun getPriority(): String
    }

    interface Presenter : IPresenter<View> {

        fun addTodo()

        fun updateTodo(id: Int)

    }

    interface Model : IModel {

        fun addTodo(map: MutableMap<String, Any>): Observable<HttpResult<Any>>

        fun updateTodo(id: Int, map: MutableMap<String, Any>): Observable<HttpResult<Any>>

    }


}