package com.kv.wanandroid.mvp.contract

import com.vension.frame.core.mvp.IModel
import com.vension.frame.core.mvp.IPresenter
import com.vension.frame.core.mvp.IView
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.kv.wanandroid.mvp.model.bean.WXChapterBean
import io.reactivex.Observable

/**
 * ========================================================
 * @author: Created by Vension on 2018/11/23 10:36.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
interface WeChatContract {

    interface View : IView {
        fun showWXChapters(chapters: MutableList<WXChapterBean>)
    }

    interface Presenter : IPresenter<View> {
        fun getWXChapters()
    }

    interface Model : IModel {
        fun getWXChapters(): Observable<HttpResult<MutableList<WXChapterBean>>>
    }

}