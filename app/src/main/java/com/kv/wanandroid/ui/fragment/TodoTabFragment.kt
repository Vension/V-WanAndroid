package com.kv.wanandroid.ui.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.View
import com.kv.wanandroid.Constant
import com.kv.wanandroid.R
import com.vension.frame.adapter.BaseFragmentAdapter
import com.vension.frame.core.AbsCompatFragment
import com.kv.wanandroid.event.TodoEvent
import com.wuhenzhizao.titlebar.widget.CommonTitleBar
import kotlinx.android.synthetic.main.fragment_todo_tab.*
import org.greenrobot.eventbus.EventBus

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/26 12:29.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class TodoTabFragment : AbsCompatFragment() {

    private val viewPagerAdapter: BaseFragmentAdapter by lazy {
        BaseFragmentAdapter(getPageFragmentManager(),getFragments(),getTitles())
    }

    override fun initToolBar(mCommonTitleBar: CommonTitleBar) {
        super.initToolBar(mCommonTitleBar)
        mCommonTitleBar.centerTextView.text = getString(R.string.nav_todo)
    }

    override fun attachLayoutRes(): Int {
        return R.layout.fragment_todo_tab
    }

    override fun initViewAndData(view: View, savedInstanceState: Bundle?) {
        viewPager_todo.run {
            adapter = viewPagerAdapter
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            offscreenPageLimit = getFragments().size
        }
        tabLayout.run {
            setupWithViewPager(viewPager_todo)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager_todo))
            addOnTabSelectedListener(onTabSelectedListener)
        }

        fab_menu.setClosedOnTouchOutside(true)
        fab_add.setOnClickListener(clickListener)
        fab_todo.setOnClickListener(clickListener)
        fab_done.setOnClickListener(clickListener)
    }

    override fun lazyLoadData() {
    }

    private fun getTitles(): MutableList<String> {
        val list = mutableListOf<String>()
        list.add("只用这一个")
        list.add("工作")
        list.add("学习")
        list.add("生活")
        return list
    }

    private fun getFragments(): MutableList<Fragment> {
        val list2 = mutableListOf<Fragment>()
        list2.add(TodoFragment.getInstance(0))
        list2.add(TodoFragment.getInstance(1))
        list2.add(TodoFragment.getInstance(2))
        list2.add(TodoFragment.getInstance(3))
        return list2
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
                viewPager_todo.setCurrentItem(it.position, false)
            }
        }
    }

    private val clickListener = View.OnClickListener {
        val curIndex = viewPager_todo.currentItem
        fab_menu.close(false)
        when (it.id) {
            R.id.fab_add -> {
                EventBus.getDefault().post(TodoEvent(Constant.TODO_ADD, curIndex))
            }
            R.id.fab_todo -> {
                EventBus.getDefault().post(TodoEvent(Constant.TODO_NO, curIndex))
            }
            R.id.fab_done -> {
                EventBus.getDefault().post(TodoEvent(Constant.TODO_DONE, curIndex))
            }
        }
    }



}