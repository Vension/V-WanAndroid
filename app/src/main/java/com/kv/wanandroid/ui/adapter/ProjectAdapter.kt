package com.kv.wanandroid.ui.adapter

import android.content.Context
import android.text.Html
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kv.wanandroid.R
import com.vension.frame.glide.ImageLoader
import com.kv.wanandroid.mvp.model.bean.Article

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/22 15:53
 * 描  述：
 * ========================================================
 */

class ProjectAdapter(private val context: Context) : BaseQuickAdapter<Article, BaseViewHolder>(R.layout.item_recy_project) {

    override fun convert(helper: BaseViewHolder?, item: Article?) {

        helper ?: return
        item ?: return
        helper.setText(R.id.item_project_list_title_tv, Html.fromHtml(item.title))
                .setText(R.id.item_project_list_content_tv, Html.fromHtml(item.desc))
                .setText(R.id.item_project_list_time_tv, "时间：${item.niceDate}")
                .setText(R.id.item_project_list_author_tv, "作者：${item.author}")
                .setImageResource(R.id.item_project_list_like_iv, if (item.collect) R.drawable.ic_like else R.drawable.ic_like_not)
                .addOnClickListener(R.id.item_project_list_like_iv)
        context.let {
            ImageLoader.loadImage(it,helper.getView(R.id.item_project_list_iv),item.envelopePic)
        }

    }

}