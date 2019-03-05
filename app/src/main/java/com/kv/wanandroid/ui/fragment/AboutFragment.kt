package com.kv.wanandroid.ui.fragment

import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import com.kv.wanandroid.R
import com.vension.frame.core.AbsCompatFragment
import com.wuhenzhizao.titlebar.widget.CommonTitleBar
import kotlinx.android.synthetic.main.fragment_about.*

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/23 11:41.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class AboutFragment : AbsCompatFragment() {

    companion object {
        fun getInstance(bundle: Bundle): AboutFragment {
            val fragment = AboutFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun isToolbarCover(): Boolean {
        return true
    }

    override fun initToolBar(mCommonTitleBar: CommonTitleBar) {
        super.initToolBar(mCommonTitleBar)
        mCommonTitleBar.run {
            centerTextView.text = "关于我们"
            alpha = 0.0f
        }
    }

    override fun attachLayoutRes(): Int {
        return R.layout.fragment_about
    }

    override fun initViewAndData(view: View, savedInstanceState: Bundle?) {
//        val arcLayout = findViewById<ArcView>(R.id.shapeofview_ArcView)
//        if (arcLayout != null) {
//            ValueAnimator.ofFloat(0f, -200f, 200f).apply {
//                addUpdateListener { animation -> arcLayout.arcHeight = ((animation.animatedValue as Float).toInt()) }
//                duration = 5000
//                repeatCount = ValueAnimator.INFINITE
//                repeatMode = ValueAnimator.REVERSE
//            }.start()
//        }

        about_content.run {
            text = Html.fromHtml(getString(R.string.about_content))
            movementMethod = LinkMovementMethod.getInstance()
        }

        val versionStr = getString(R.string.app_name) + activity?.packageManager?.getPackageInfo(activity?.packageName, 0)?.versionName
        about_version.text = versionStr

        myNestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val height = shapeofview_ArcView.height
            var alpha = 1.0f
            if (mCommonTitleBar?.top!! + scrollY < height) {
                alpha = scrollY / (height - mCommonTitleBar?.height!!).toFloat()
            }
            mCommonTitleBar?.alpha = alpha
        })
    }

    override fun lazyLoadData() {
    }
}