package com.kv.wanandroid.mvp.presenter

import com.kv.wanandroid.mvp.contract.NavigationContract
import com.kv.wanandroid.mvp.model.NavigationModel
import com.vension.frame.core.mvp.AbsPresenter
import com.vension.frame.ext.rxHandleWithModel


/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/22 16:46
 * 描  述：
 * ========================================================
 */

class NavigationPresenter : AbsPresenter<NavigationContract.Model, NavigationContract.View>(), NavigationContract.Presenter {

    override fun createModel(): NavigationContract.Model? {
        return NavigationModel()
    }

    override fun requestNavigationList() {
        mModel?.requestNavigationList()?.rxHandleWithModel(mModel, mView) {
            mView?.setNavigationData(it.data)
        }
    }

}