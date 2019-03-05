package com.kv.wanandroid.test

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.vension.frame.core.mvp.AbsCompatMVPRefreshFragment
import com.wuhenzhizao.titlebar.widget.CommonTitleBar

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/29 10:19.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class TestRefreshFragment : AbsCompatMVPRefreshFragment<String, TestRefreshContract.View, TestRefreshPresenter>(),
    TestRefreshContract.View {

    override fun enableLoadMore(): Boolean {
        return false
    }

    override fun initToolBar(mCommonTitleBar: CommonTitleBar) {
        super.initToolBar(mCommonTitleBar)
        mCommonTitleBar.centerTextView.text = "测试列表"
    }

    override fun createPresenter(): TestRefreshPresenter {
        return TestRefreshPresenter()
    }

    override fun createRecyAdapter(): BaseQuickAdapter<String, BaseViewHolder> {
        return TestRefreshAdapter(context)
    }

    override fun addItemClickListener(mAdapter: BaseQuickAdapter<String, BaseViewHolder>) {
    }

    override fun onTargetRequestApi(isRefresh: Boolean, page: Int, limit: Int) {
        mPresenter?.getTestDatas()
    }


    override fun setTestData(testDatas: MutableList<String>) {
//        val hear = View.inflate(context, R.layout.layout_toolbar_search,null)
//        setRefreshData(testDatas, listOf(hear),null)
        setRefreshData(testDatas)
    }

}