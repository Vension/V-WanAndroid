package com.kv.wanandroid.test

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kv.wanandroid.R

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/23 11:13
 * 描  述：
 * ========================================================
 */

class TestRefreshAdapter(private val context: Context?)
    : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_lv_pop) {
    override fun convert(helper: BaseViewHolder?, item: String?) {

        helper ?: return
        item ?: return

        helper.setText(R.id.text_content, item)
    }
}