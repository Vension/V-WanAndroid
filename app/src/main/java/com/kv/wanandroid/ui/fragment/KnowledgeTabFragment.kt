package com.kv.wanandroid.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.View
import com.kv.wanandroid.Constant
import com.kv.wanandroid.R
import com.vension.frame.adapter.BaseFragmentStatePagerAdapter
import com.vension.frame.core.AbsCompatFragment
import com.kv.wanandroid.mvp.model.bean.Knowledge
import com.kv.wanandroid.mvp.model.bean.KnowledgeTreeBody
import com.wuhenzhizao.titlebar.widget.CommonTitleBar
import kotlinx.android.synthetic.main.fragment_knowledge_tab.*
import org.jetbrains.anko.imageResource

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/7 17:17.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class KnowledgeTabFragment : AbsCompatFragment() {

    private var knowledges = mutableListOf<Knowledge>()
    private var fragments = mutableListOf<Fragment>()
    private var titles = mutableListOf<String>()

    private val viewPagerAdapter: BaseFragmentStatePagerAdapter by lazy {
        BaseFragmentStatePagerAdapter(getPageFragmentManager(),fragments,titles)
    }

    /**
     * onTabSelectedListener
     */
    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            // 默认切换的时候，会有一个过渡动画，设为false后，取消动画，直接显示
            tab?.let {
                viewPager.setCurrentItem(it.position, false)
            }
        }
    }


    override fun initToolBar(mCommonTitleBar: CommonTitleBar) {
        mCommonTitleBar.rightImageButton.imageResource = R.drawable.icon_share
        getBundleExtras().getSerializable(Constant.CONTENT_DATA_KEY)?.let {
            val data = it as KnowledgeTreeBody
            data.children.let { children ->
                knowledges.addAll(children)
                children.forEach{
                    fragments.add(KnowledgeFragment.getInstance(it.id))
                    titles.add(it.name)
                }
            }
        }

        mCommonTitleBar.centerTextView.text = getBundleExtras().getString(Constant.CONTENT_TITLE_KEY)
        mCommonTitleBar.setListener { v, action1, extra ->
            when(action1){
                CommonTitleBar.ACTION_LEFT_BUTTON,
                CommonTitleBar.ACTION_LEFT_TEXT -> {
                    activity?.onBackPressed()
                }
                CommonTitleBar.ACTION_RIGHT_BUTTON ,
                CommonTitleBar.ACTION_RIGHT_TEXT -> {
                    Intent().run {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT,
                            getString(
                                R.string.share_article_url,
                                getString(R.string.app_name),
                                knowledges[tabLayout.selectedTabPosition].name,
                                knowledges[tabLayout.selectedTabPosition].id.toString()
                            ))
                        type = Constant.CONTENT_SHARE_TYPE
                        startActivity(Intent.createChooser(this, getString(R.string.action_share)))
                    }
                }
            }
        }
    }

    override fun attachLayoutRes(): Int {
        return R.layout.fragment_knowledge_tab
    }

    override fun initViewAndData(view: View, savedInstanceState: Bundle?) {
        viewPager.run {
            adapter = viewPagerAdapter
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            offscreenPageLimit = knowledges.size
        }
        tabLayout.run {
            setupWithViewPager(viewPager)
            // TabLayoutHelper.setUpIndicatorWidth(tabLayout)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
            addOnTabSelectedListener(onTabSelectedListener)
        }
    }

    override fun lazyLoadData() {
    }

}