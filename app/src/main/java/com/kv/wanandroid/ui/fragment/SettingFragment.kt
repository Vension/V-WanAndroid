package com.kv.wanandroid.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import com.kv.wanandroid.R
import com.vension.frame.ext.showSnackMsg
import com.vension.frame.rx.SchedulerUtils
import com.vension.frame.utils.CacheDataUtil
import com.kv.wanandroid.event.RefreshHomeEvent
import io.reactivex.Observable
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit


/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/27 14:41
 * 描  述：
 * ========================================================
 */

class SettingFragment : PreferenceFragmentCompat() {

    companion object {
        fun getInstance(): SettingFragment {
            return SettingFragment()
        }
    }

    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.pref_setting)
        setHasOptionsMenu(true)
        initView()
    }

    private fun initView() {
        setDefaultText()

        findPreference("switch_show_top").setOnPreferenceChangeListener { preference, newValue ->
            // 通知首页刷新数据
            // 延迟发送通知：为了保证刷新数据时 SettingUtil.getIsShowTopArticle() 得到最新的值
            Observable.timer(100, TimeUnit.MILLISECONDS)
                .compose(SchedulerUtils.ioToMain())
                .subscribe({
                    EventBus.getDefault().post(RefreshHomeEvent(true))
                }, {})
            true
        }


        findPreference("clearCache").onPreferenceClickListener = Preference.OnPreferenceClickListener {
            CacheDataUtil.clearAllCache(activity!!)
            activity!!.showSnackMsg(getString(R.string.clear_cache_successfully))
            setDefaultText()
            false
        }

        try {
            val version = "当前版本 " + activity?.packageManager?.getPackageInfo(activity?.packageName, 0)?.versionName
            findPreference("version").summary = version
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

//        findPreference("version").setOnPreferenceClickListener {
//            Beta.checkUpgrade()
//            false
//        }


        findPreference("author").onPreferenceClickListener = Preference.OnPreferenceClickListener {
           startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.about_developers))))
            false
        }

        findPreference("copyRight").onPreferenceClickListener = Preference.OnPreferenceClickListener {
            AlertDialog.Builder(activity)
                .setTitle(R.string.copyright)
                .setMessage(R.string.copyright_content)
                .setCancelable(true)
                .show()
            false
        }

    }


    private fun setDefaultText() {
        try {
            findPreference("clearCache").summary = CacheDataUtil.getTotalCacheSize(activity!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}