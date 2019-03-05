package com.kv.wanandroid.ui.activity

import android.os.Bundle
import com.kv.wanandroid.R
import com.vension.frame.core.AbsCompatActivity
import com.kv.wanandroid.ui.fragment.SettingFragment
import com.wuhenzhizao.titlebar.widget.CommonTitleBar

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/27 16:15.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class SetingActivity: AbsCompatActivity() {

    override fun showToolBar(): Boolean {
        return true
    }

    override fun initToolBar(mCommonTitleBar: CommonTitleBar) {
        super.initToolBar(mCommonTitleBar)
        mCommonTitleBar.run {
            centerTextView.text= "设置"
        }
    }

    override fun attachLayoutRes(): Int {
        return R.layout.fragment_seting
    }

    override fun initViewAndData(savedInstanceState: Bundle?) {
        addFragment(R.id.fl_seting, SettingFragment.getInstance())
    }

}