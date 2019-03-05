package com.kv.wanandroid.ui.fragment

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kv.wanandroid.Constant
import com.kv.wanandroid.R
import com.kv.wanandroid.ui.adapter.SearchHistoryAdapter
import com.vension.frame.core.mvp.AbsCompatMVPFragment
import com.vension.frame.ext.getRandomColor
import com.vension.frame.ext.showToast
import com.kv.wanandroid.mvp.contract.SearchContract
import com.kv.wanandroid.mvp.model.bean.HotSearchBean
import com.kv.wanandroid.mvp.model.bean.SearchHistoryBean
import com.kv.wanandroid.mvp.presenter.SearchPresenter
import com.wuhenzhizao.titlebar.widget.CommonTitleBar
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/23 14:33.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class SearchFragmet : AbsCompatMVPFragment<SearchContract.View, SearchPresenter>(), SearchContract.View{

    /**
     * 热搜数据
     */
    private var mHotSearchDatas = mutableListOf<HotSearchBean>()

    /**
     * datas
     */
    private val datas = mutableListOf<SearchHistoryBean>()

    /**
     * SearchHistoryAdapter
     */
    private val searchHistoryAdapter by lazy {
        SearchHistoryAdapter(context, datas)
    }

    /**
     * LinearLayoutManager
     */
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(context)
    }

    private val mClearDialog by lazy {
        AlertDialog.Builder(context!!)
            .setTitle("确定清空搜索历史吗？")
            .setCancelable(true)
            .setPositiveButton("确定"){ dialog, which ->
                datas.clear()
                searchHistoryAdapter.replaceData(datas)
                mPresenter?.clearAllHistory()
                dialog.dismiss()
            }
            .setNegativeButton("取消") { dialog, which ->
                dialog.dismiss()
            }
            .create()
    }

    override fun getToolBarResId(layout: Int): Int {
        return R.layout.layout_toolbar_search
    }

    override fun initToolBar(mCommonTitleBar: CommonTitleBar) {
        mCommonTitleBar.setListener { v, action1, extra ->
            when(action1){
                CommonTitleBar.ACTION_LEFT_BUTTON,
                CommonTitleBar.ACTION_LEFT_TEXT -> {
                    activity?.onBackPressed()
                }
                CommonTitleBar.ACTION_RIGHT_BUTTON ,
                CommonTitleBar.ACTION_RIGHT_TEXT -> {
                    mCommonTitleBar.centerSearchEditText?.let {
                        goToSearchList(it.text.toString())
                    }
                }
                CommonTitleBar.ACTION_SEARCH_SUBMIT ->{
                    goToSearchList(extra.toString())
                }
            }
        }
    }


    override fun createPresenter(): SearchPresenter {
        return SearchPresenter()
    }

    override fun attachLayoutRes(): Int {
        return R.layout.fragment_search
    }

    override fun initViewAndData(view: View, savedInstanceState: Bundle?) {
        super.initViewAndData(view, savedInstanceState)
        hot_search_flow_layout.run {
            setOnTagClickListener { view, position, parent ->
                if (mHotSearchDatas.size > 0) {
                    val hotSearchBean = mHotSearchDatas[position]
                    goToSearchList(hotSearchBean.name)
                    true
                }
                false
            }
        }

        rv_history_search.run {
            layoutManager = linearLayoutManager
            adapter = searchHistoryAdapter
            itemAnimator = DefaultItemAnimator()
        }

        searchHistoryAdapter.run {
            bindToRecyclerView(rv_history_search)
            onItemClickListener = this@SearchFragmet.onItemClickListener
            onItemChildClickListener = this@SearchFragmet.onItemChildClickListener
        }

        search_history_clear_all_tv.setOnClickListener {
            if (datas.size > 0){
                mClearDialog.show()
            }
        }

    }


    override fun lazyLoadData() {
        mPresenter?.getHotSearchData()
    }


    override fun onResume() {
        super.onResume()
        mPresenter?.queryHistory()
    }

    /**
     * 进行搜索操作
     */
    private fun goToSearchList(key: String) {
        if (key.isNullOrEmpty()){
            showToast("请输入搜索内容~")
        }else{
            mPresenter?.saveSearchKey(key)
            getBundleExtras().putString(Constant.SEARCH_KEY,key)
            startProxyActivity(SearchListFragment::class.java,getBundleExtras())
        }
    }

    override fun showHistoryData(historyBeans: MutableList<SearchHistoryBean>) {
        searchHistoryAdapter.replaceData(historyBeans)
        layout_search_history.visibility = if (historyBeans.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    override fun showHotSearchData(hotSearchDatas: MutableList<HotSearchBean>) {
        this.mHotSearchDatas.addAll(hotSearchDatas)
        hot_search_flow_layout.adapter = object : TagAdapter<HotSearchBean>(hotSearchDatas) {
            override fun getView(parent: FlowLayout?, position: Int, hotSearchBean: HotSearchBean?): View {
                val tv: TextView = LayoutInflater.from(parent?.context).inflate(R.layout.flow_layout_tv,
                    hot_search_flow_layout, false) as TextView
                tv.text = hotSearchBean?.name
                tv.setTextColor(getRandomColor())
                return tv
            }
        }
    }

    /**
     * ItemClickListener
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (searchHistoryAdapter.data.size != 0) {
            val item = searchHistoryAdapter.data[position]
            goToSearchList(item.key)
        }
    }


    /**
     * ItemChildClickListener
     */
    private val onItemChildClickListener =
        BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
            if (searchHistoryAdapter.data.size != 0) {
                val item = searchHistoryAdapter.data[position]
                when (view.id) {
                    R.id.iv_clear -> {
                        mPresenter?.deleteById(item.id)
                        searchHistoryAdapter.remove(position)
                    }
                }
            }
        }


}