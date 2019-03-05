package com.vension.frame.http

import java.io.Serializable

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/26 9:23
 * 描  述：
 * ========================================================
 */

open class BaseBean : Serializable {

    var errorCode: Int = 0
    var errorMsg: String = ""
}