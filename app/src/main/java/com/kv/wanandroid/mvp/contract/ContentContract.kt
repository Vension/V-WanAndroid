package com.kv.wanandroid.mvp.contract

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/27 12:12
 * 描  述：
 * ========================================================
 */

interface ContentContract {

    interface View<data> : CommonContract.View<data> {
    }

    interface Presenter<data> : CommonContract.Presenter<data, View<data>> {
    }


    interface Model : CommonContract.Model {

    }

}