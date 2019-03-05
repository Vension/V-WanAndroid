package com.kv.wanandroid.test

import android.os.Handler
import com.vension.frame.core.mvp.AbsPresenter

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/29 10:29.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class TestRefreshPresenter : AbsPresenter<TestRefreshModel, TestRefreshContract.View>(), TestRefreshContract.Presenter {

    override fun createModel(): TestRefreshModel? {
        return TestRefreshModel()
    }

    override fun getTestDatas() {
        mView?.showLoading()
        Handler().postDelayed({
            mView?.setTestData(mutableListOf())
        }, 2000)
    }

    private fun getListData(): MutableList<String> {
        return mutableListOf("I am test data-> 1","I am test data-> 2","I am test data-> 3")
    }


}