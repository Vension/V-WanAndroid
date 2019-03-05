package com.vension.frame.http.exception

import com.google.gson.JsonParseException
import com.vension.frame.ext.Loge
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/10/31 15:20
 * 描  述：统一处理网络请求出现错误的情况，包括接口返回数据不是目标数据
 * ========================================================
 */

//
object ExceptionHandle {
    private const val TAG = "ExceptionHandle"
    var errorCode = ErrorStatus.UNKNOWN_ERROR
    var errorMsg = "请求失败，请稍后重试"

    fun handleException(e: Throwable): String {
        e.printStackTrace()
        if (e is SocketTimeoutException //网络超时
            || e is ConnectException    //连接异常
            || e is retrofit2.HttpException
        ) { //均视为网络错误
            Loge(TAG,"网络连接异常: " + e.message)
            errorMsg = "网络连接异常"
            errorCode = ErrorStatus.NETWORK_ERROR
        } else if (e is JsonParseException
            || e is JSONException
            || e is ParseException) {   //均视为解析错误
            Loge(TAG,"数据解析异常: " + e.message)
            errorMsg = "数据解析异常"
            errorCode = ErrorStatus.SERVER_ERROR
        } else if (e is ApiException) {//服务器返回的错误信息
            errorMsg = e.message.toString()
            errorCode = ErrorStatus.SERVER_ERROR
        } else if (e is UnknownHostException) {
            Loge(TAG,"网络连接异常: " + e.message)
            errorMsg = "网络连接异常"
            errorCode = ErrorStatus.NETWORK_ERROR
        } else if (e is IllegalArgumentException) {
            errorMsg = "参数错误"
            errorCode = ErrorStatus.SERVER_ERROR
        } else {//未知错误
            try {
                Loge(TAG,"错误: " + e.message)
            } catch (e1: Exception) {
                Loge(TAG,"未知错误Debug调试")
            }

            errorMsg = "未知错误，可能抛锚了吧~"
            errorCode = ErrorStatus.UNKNOWN_ERROR
        }
        return errorMsg
    }

}
