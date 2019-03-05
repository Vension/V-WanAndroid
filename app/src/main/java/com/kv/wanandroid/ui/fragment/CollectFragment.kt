package com.kv.wanandroid.ui.fragment

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kv.wanandroid.R
import com.kv.wanandroid.ui.adapter.CollectAdapter
import com.kv.wanandroid.ui.fragment.agent.AgentWebFragment
import com.vension.frame.core.mvp.AbsCompatMVPRefreshFragment
import com.vension.frame.ext.showToast
import com.kv.wanandroid.event.RefreshHomeEvent
import com.kv.wanandroid.mvp.contract.CollectContract
import com.kv.wanandroid.mvp.model.bean.CollectionArticle
import com.kv.wanandroid.mvp.model.bean.CollectionResponseBody
import com.kv.wanandroid.mvp.presenter.CollectPresenter
import com.wuhenzhizao.titlebar.widget.CommonTitleBar
import org.greenrobot.eventbus.EventBus

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/23 11:04.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class CollectFragment : AbsCompatMVPRefreshFragment<CollectionArticle, CollectContract.View, CollectPresenter>(),
    CollectContract.View {

    override fun initToolBar(mCommonTitleBar: CommonTitleBar) {
        super.initToolBar(mCommonTitleBar)
        mCommonTitleBar.centerTextView.text = "我的收藏"
    }

    override fun createPresenter(): CollectPresenter {
        return CollectPresenter()
    }

    override fun createRecyAdapter(): CollectAdapter {
        return CollectAdapter(context)
    }

    override fun addItemClickListener(mAdapter: BaseQuickAdapter<CollectionArticle, BaseViewHolder>) {
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            val item  = mAdapter?.getItem(position) as CollectionArticle
            item?.let {
                val mBundle = getBundleExtras()
                mBundle.putString("web_url",it.link)
                mBundle.putString("web_title",it.title)
                mBundle.putInt("web_id",it.id)
                startProxyActivity(AgentWebFragment::class.java,mBundle)
            }
        }
    }

    override fun addItemChildClickListener(mAdapter: BaseQuickAdapter<CollectionArticle, BaseViewHolder>) {
        super.addItemChildClickListener(mAdapter)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val item  = mAdapter?.getItem(position) as CollectionArticle
            item?.let {
                when(view.id){
                    R.id.iv_like -> {
                        mAdapter.remove(position)
                        mPresenter?.removeCollectArticle(item.id, item.originId)
                    }
                    else -> {

                    }
                }
            }
        }
    }

    override fun onTargetRequestApi(isRefresh: Boolean, page: Int, limit: Int) {
        mPresenter?.getCollectList(page)
    }

    override fun setCollectList(page: Int,articles: CollectionResponseBody<CollectionArticle>) {
        articles.datas.let {
            if(page > 0) setMoreData(it) else setRefreshData(it)
        }
    }

    override fun showRemoveCollectSuccess(success: Boolean) {
        if (success) {
            showToast(getString(R.string.cancel_collect_success))
            EventBus.getDefault().post(RefreshHomeEvent(true))
        }
    }
}