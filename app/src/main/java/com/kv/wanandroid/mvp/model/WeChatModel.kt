package com.kv.wanandroid.mvp.model

import com.kv.wanandroid.mvp.contract.WeChatContract
import com.kv.wanandroid.mvp.model.bean.HttpResult
import com.kv.wanandroid.mvp.model.bean.WXChapterBean
import com.vension.frame.core.mvp.AbsModel
import com.vension.frame.http.RetrofitHelper
import com.vension.frame.rx.SchedulerUtils
import com.kv.wanandroid.api.ApiService
import io.reactivex.Observable

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/23 10:37
 * 描  述：
 * ========================================================
 */

class WeChatModel : AbsModel(), WeChatContract.Model {

    override fun getWXChapters(): Observable<HttpResult<MutableList<WXChapterBean>>> {
        return RetrofitHelper.getService(ApiService::class.java).getWXChapters()
                .compose(SchedulerUtils.ioToMain())
    }

}