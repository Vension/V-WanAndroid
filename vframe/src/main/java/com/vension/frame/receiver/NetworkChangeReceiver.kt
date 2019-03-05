package com.vension.frame.receiver

import Constant
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.vension.frame.utils.NetWorkUtil
import com.vension.frame.utils.PreferenceUtil
import com.vension.frame.event.NetworkChangeEvent
import org.greenrobot.eventbus.EventBus

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/10/31 12:23
 * 描  述：网络状态监听
 * ========================================================
 */

class NetworkChangeReceiver : BroadcastReceiver() {

    private val netTAG = "NetworkConnectChanged"
    
    /**
     * 缓存上一次的网络状态
     */
    private var hasNetwork: Boolean by PreferenceUtil(Constant.KEY_HAS_NETWORK, true)

    override fun onReceive(context: Context, intent: Intent) {
        //判断当前的网络连接状态是否可用
        val isConnected = NetWorkUtil.isNetworkConnected(context)
        Log.d(netTAG, "onReceive: 当前网络 $isConnected")
        if (isConnected) {
            if (isConnected != hasNetwork) {
                EventBus.getDefault().post(NetworkChangeEvent(isConnected))
            }
        } else {
            EventBus.getDefault().post(NetworkChangeEvent(isConnected))
        }
    }

}