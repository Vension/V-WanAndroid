package com.kv.wanandroid.ui.fragment.agent

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import cn.bingoogolapple.bgabanner.BGALocalImageSize
import com.kv.wanandroid.Constant
import com.kv.wanandroid.R
import com.kv.wanandroid.redirectToMain
import com.vension.frame.core.AbsCompatFragment
import com.vension.frame.utils.AppUtil
import com.vension.frame.utils.PreferenceUtil
import kotlinx.android.synthetic.main.fragment_guide.*

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/19 15:55
 * 描  述：引导页面
 * ========================================================
 */

class GuideFragment : AbsCompatFragment(){

    private var versionCode: Int by PreferenceUtil(Constant.VERSION_CODE, -1)

    override fun showToolBar(): Boolean {
        return false
    }


    override fun attachLayoutRes(): Int {
        return R.layout.fragment_guide
    }

    override fun initViewAndData(view: View, savedInstanceState: Bundle?) {
        setBanners()// 设置数据源
        btn_guide_enter.setOnClickListener {
            versionCode = AppUtil.getAppVersionCode(context!!)
            redirectToMain(activity!!)
            activity?.finish()
        }
    }

    override fun lazyLoadData() {
    }


    /**
     * 设置banner 引导数据
     */
    private fun setBanners() {
        // Bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
        val localImageSize = BGALocalImageSize(720, 1280, 320f, 640f)
        // 设置数据源
        banner_guide.setData(localImageSize,ImageView.ScaleType.CENTER_CROP,
            R.drawable.img_welcome_1,R.drawable.img_welcome_2,R.drawable.img_welcome_3,R.drawable.img_welcome_4)
    }

}