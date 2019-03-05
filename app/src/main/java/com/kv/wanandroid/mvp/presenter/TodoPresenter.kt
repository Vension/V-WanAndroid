package com.kv.wanandroid.mvp.presenter

import com.kv.wanandroid.mvp.contract.TodoContract
import com.kv.wanandroid.mvp.model.TodoModel
import com.vension.frame.core.mvp.AbsPresenter
import com.vension.frame.ext.rxHandleWithModel


/**
 * Created by chenxz on 2018/8/7.
 */
class TodoPresenter : AbsPresenter<TodoContract.Model, TodoContract.View>(), TodoContract.Presenter {

    override fun createModel(): TodoContract.Model? = TodoModel()

    override fun getAllTodoList(type: Int) {
        mModel?.getTodoList(type)?.rxHandleWithModel(mModel, mView) {

        }
    }

    override fun getNoTodoList(page: Int, type: Int) {
        mModel?.getNoTodoList(page, type)?.rxHandleWithModel(mModel, mView, page == 1) {
            mView?.showNoTodoList(it.data)
        }
    }

    override fun getDoneList(page: Int, type: Int) {
        mModel?.getDoneList(page, type)?.rxHandleWithModel(mModel, mView, page == 1) {
            mView?.showNoTodoList(it.data)
        }
    }

    override fun deleteTodoById(id: Int) {
        mModel?.deleteTodoById(id)?.rxHandleWithModel(mModel, mView,false) {
            mView?.showDeleteSuccess(true)
        }
    }

    override fun updateTodoById(id: Int, status: Int) {
        mModel?.updateTodoById(id, status)?.rxHandleWithModel(mModel, mView,false) {
            mView?.showUpdateSuccess(true)
        }
    }

}