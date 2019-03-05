package com.kv.wanandroid.mvp.presenter

import com.kv.wanandroid.mvp.contract.TodoAddContract
import com.kv.wanandroid.mvp.model.TodoAddModel
import com.vension.frame.core.mvp.AbsPresenter
import com.vension.frame.ext.rxHandleWithModel


/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/26 17:56
 * 描  述：
 * ========================================================
 */

class TodoAddPresenter : AbsPresenter<TodoAddContract.Model, TodoAddContract.View>(), TodoAddContract.Presenter {

    override fun createModel(): TodoAddContract.Model? = TodoAddModel()

    override fun addTodo() {
        val type = mView?.getType() ?: 0
        val title = mView?.getTitle().toString()
        val content = mView?.getContent().toString()
        val date = mView?.getCurrentDate().toString()
        val priority = mView?.getPriority().toString()

        val map = mutableMapOf<String, Any>()
        map["type"] = type
        map["title"] = title
        map["content"] = content
        map["date"] = date
        map["priority"] = priority

        mModel?.addTodo(map)?.rxHandleWithModel(mModel, mView,false,true) {
            mView?.showAddTodo(true)
        }
    }

    override fun updateTodo(id: Int) {
        val type = mView?.getType() ?: 0
        val title = mView?.getTitle().toString()
        val content = mView?.getContent().toString()
        val date = mView?.getCurrentDate().toString()
        val status = mView?.getStatus() ?: 0
        val priority = mView?.getPriority().toString()

        val map = mutableMapOf<String, Any>()
        map["type"] = type
        map["title"] = title
        map["content"] = content
        map["date"] = date
        map["status"] = status
        map["priority"] = priority

        mModel?.updateTodo(id, map)?.rxHandleWithModel(mModel, mView,false,true) {
            mView?.showUpdateTodo(true)
        }
    }


}