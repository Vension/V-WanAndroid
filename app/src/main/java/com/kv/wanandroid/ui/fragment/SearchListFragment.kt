package com.kv.wanandroid.ui.fragment

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kv.wanandroid.Constant
import com.kv.wanandroid.R
import com.kv.wanandroid.WanAndroidApplication
import com.kv.wanandroid.ui.adapter.SearchListAdapter
import com.kv.wanandroid.ui.fragment.agent.AgentWebFragment
import com.kv.wanandroid.ui.fragment.agent.LoginFragment
import com.vension.frame.core.mvp.AbsCompatMVPRefreshFragment
import com.vension.frame.ext.showToast
import com.vension.frame.utils.NetWorkUtil
import com.kv.wanandroid.mvp.contract.SearchListContract
import com.kv.wanandroid.mvp.model.bean.Article
import com.kv.wanandroid.mvp.model.bean.ArticleResponseBody
import com.kv.wanandroid.mvp.presenter.SearchListPresenter
import com.wuhenzhizao.titlebar.widget.CommonTitleBar

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/23 16:26
 * 描  述：搜索结果列表
 * ========================================================
 */

class SearchListFragment : AbsCompatMVPRefreshFragment<Article, SearchListContract.View<Article>, SearchListPresenter>(),
    SearchListContract.View<Article> {

    private lateinit var keyStr:String


    override fun initToolBar(mCommonTitleBar: CommonTitleBar) {
        super.initToolBar(mCommonTitleBar)
        keyStr = getBundleExtras().getString(Constant.SEARCH_KEY)
        mCommonTitleBar.centerTextView.text = keyStr
    }

    override fun createPresenter(): SearchListPresenter {
        return SearchListPresenter()
    }

    override fun createRecyAdapter(): SearchListAdapter {
        return SearchListAdapter(context)
    }

    override fun addItemClickListener(mAdapter: BaseQuickAdapter<Article, BaseViewHolder>) {
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            val item  = mAdapter?.getItem(position) as Article
            item?.let {
                val mBundle = getBundleExtras()
                mBundle.putString("web_url",it.link)
                mBundle.putString("web_title",it.title)
                mBundle.putInt("web_id",it.id)
                startProxyActivity(AgentWebFragment::class.java,mBundle)
            }
        }
    }

    override fun addItemChildClickListener(mAdapter: BaseQuickAdapter<Article, BaseViewHolder>) {
        super.addItemChildClickListener(mAdapter)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val item  = mAdapter?.getItem(position) as Article
            item?.let {
                when(view.id){
                    R.id.iv_like -> {
                        if (isLogin) {
                            if (!NetWorkUtil.isNetworkAvailable(WanAndroidApplication.context)) {
                                showToast(resources.getString(R.string.page_not_network))
                                return@setOnItemChildClickListener
                            }
                            val collect = item.collect
                            item.collect = !collect
                            mAdapter.setData(position, item)
                            if (collect) {
                                mPresenter?.cancelCollectArticle(item.id)
                            } else {
                                mPresenter?.addCollectArticle(item.id)
                            }
                        } else {
                            startProxyActivity(LoginFragment::class.java)
                            showToast(resources.getString(R.string.login_tint))
                        }
                    }
                    else -> {

                    }
                }
            }
        }
    }

    override fun onTargetRequestApi(isRefresh: Boolean, page: Int, limit: Int) {
        mPresenter?.queryBySearchKey(0, keyStr)
    }

    override fun showArticles(page: Int,articles: ArticleResponseBody) {
        if(page > 0) setMoreData(articles.datas) else setRefreshData(articles.datas)
    }

    override fun showCollectSuccess(success: Boolean) {
        if (success) {
            showToast(getString(R.string.collect_success))
        }
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if (success) {
            showToast(getString(R.string.cancel_collect_success))
        }
    }

}