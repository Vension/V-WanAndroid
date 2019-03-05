package com.vension.frame.http.exception

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/27 10:15
 * 描  述：服务请求异常
 * ========================================================
 */

class ApiException(code: Int, message: String, cause: Throwable?) : RuntimeException(message, cause)