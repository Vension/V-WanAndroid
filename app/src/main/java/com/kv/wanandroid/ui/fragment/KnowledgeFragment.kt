package com.kv.wanandroid.ui.fragment

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kv.wanandroid.Constant
import com.kv.wanandroid.R
import com.kv.wanandroid.WanAndroidApplication
import com.kv.wanandroid.ui.adapter.KnowledgeAdapter
import com.kv.wanandroid.ui.fragment.agent.AgentWebFragment
import com.kv.wanandroid.ui.fragment.agent.LoginFragment
import com.vension.frame.core.mvp.AbsCompatMVPRefreshFragment
import com.vension.frame.ext.showToast
import com.vension.frame.utils.NetWorkUtil
import com.kv.wanandroid.mvp.presenter.KnowledgePresenter
import com.kv.wanandroid.mvp.contract.KnowledgeContract
import com.kv.wanandroid.mvp.model.bean.Article
import com.kv.wanandroid.mvp.model.bean.ArticleResponseBody

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/20 16:59.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class KnowledgeFragment : AbsCompatMVPRefreshFragment<Article, KnowledgeContract.View<Article>, KnowledgePresenter>(),
    KnowledgeContract.View<Article> {

    companion object {
        fun getInstance(cid: Int): KnowledgeFragment {
            val fragment = KnowledgeFragment()
            val args = Bundle()
            args.putInt(Constant.CONTENT_CID_KEY, cid)
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * cid
     */
    private var cid: Int = 0


    override fun showToolBar(): Boolean {
        return false
    }

    override fun createRecyAdapter(): KnowledgeAdapter {
        return KnowledgeAdapter(activity)
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

    override fun initViewAndData(view: View, savedInstanceState: Bundle?) {
        super.initViewAndData(view, savedInstanceState)
        cid = arguments?.getInt(Constant.CONTENT_CID_KEY) ?: 0
    }

    override fun onTargetRequestApi(isRefresh: Boolean, page: Int, limit: Int) {
        mPresenter?.requestKnowledgeList(page, cid)
    }

    override fun createPresenter(): KnowledgePresenter {
        return  KnowledgePresenter()
    }

    override fun setKnowledgeList(page: Int,articles: ArticleResponseBody) {
        articles.datas.let {
            if(page > 0) setMoreData(it) else setRefreshData(it)
        }
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