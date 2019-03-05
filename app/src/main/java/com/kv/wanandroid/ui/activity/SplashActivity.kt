package com.kv.wanandroid.ui.activity

import android.Manifest
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.Toast
import com.kv.wanandroid.R
import com.vension.frame.core.AbsCompatActivity
import com.vension.frame.core.CoreApplication
import com.vension.frame.utils.AppUtil
import com.vension.frame.utils.DensityUtil
import com.vension.frame.utils.PreferenceUtil
import com.vension.frame.utils.TimeUtil
import com.vension.frame.views.CircleCountDownView
import com.kv.wanandroid.Constant
import com.kv.wanandroid.redirectToMain
import com.kv.wanandroid.ui.fragment.agent.GuideFragment
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * ========================================================
 * @author: Created by Vension on 2018/10/31 17:14.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class SplashActivity : AbsCompatActivity() {

    private var versionCode: Int by PreferenceUtil(Constant.VERSION_CODE, -1)

    private var textTypeface : Typeface = Typeface.createFromAsset(CoreApplication.context.assets, "fonts/Lobster-1.4.otf")
    private var descTypeFace : Typeface = Typeface.createFromAsset(CoreApplication.context.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
    private lateinit var alphaAnimation : AlphaAnimation

    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    override fun enableNetworkTip(): Boolean {
        return false
    }

    override fun useEventBus(): Boolean {
        return false
    }

    override fun attachLayoutRes(): Int = R.layout.activity_splash

    override fun initViewAndData(savedInstanceState: Bundle?) {
        tv_app_name.typeface = textTypeface
        tv_version.typeface = descTypeFace
        val welcomeHint = getString(R.string.welcome_hint_noVersion,"2014", TimeUtil.getTime(System.currentTimeMillis(),"yyyy"), "VFrame.com")
        tv_version.text = welcomeHint


        val animation01 = TranslateAnimation(0f, 0f, 0f, DensityUtil.dp2px(this, 50f).toFloat())
        val animation02 = TranslateAnimation(0f, 0f, 0f,-DensityUtil.dp2px(this, 50f).toFloat())
        //渐变展示启动屏
        alphaAnimation = AlphaAnimation(0.3f, 1.0f)
        alphaAnimation?.run {
            duration = 3000
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    showAdvertisement()//显示广告页
                }

                override fun onAnimationStart(p0: Animation?) {
                }
            })
        }
        iv_splash.startAnimation(alphaAnimation)
        animation01.run {
            duration = 500
            fillAfter = true
            repeatCount = 0
            interpolator = DecelerateInterpolator()
        }
        animation02.run {
            duration = 500
            fillAfter = true
            repeatCount = 0
            interpolator = DecelerateInterpolator()
        }
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            img_slogan_01.visibility = View.VISIBLE
            img_slogan_01.startAnimation(animation01)
            handler.postDelayed({
                img_slogan_02.visibility = View.VISIBLE
                img_slogan_02.startAnimation(animation02)
            }, 400)
        }, 300)
    }


    private fun showAdvertisement() {
        iv_splash.visibility = View.GONE
        view_GlowingLoader.visibility = View.VISIBLE
//        iv_splash.setImageResource(R.drawable.img_advertisment)
        startCountDown()
    }

    /**开始倒计时*/
    private val countDownTime = 3 * 1000
    private fun startCountDown() {
        circleCountDownView.visibility = View.VISIBLE
        circleCountDownView.setCountdownTime(countDownTime.toLong())
        circleCountDownView.startCountDownTime(object : CircleCountDownView.OnCountdownFinishListener{
            override fun countdownFinished() {
                checkPermission()
            }
        })
    }

    /**
     * 6.0以下版本(系统自动申请) 不会弹框
     * 有些厂商修改了6.0系统申请机制，他们修改成系统自动申请权限了
     */
    private fun checkPermission(){
        disposable = mRxPermissions?.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
            .subscribe { granted ->
                if (granted) {
                    // All requested permissions are granted

                } else {
                    // At least one permission is denied
                    Toast.makeText(this@SplashActivity,"用户关闭了权限",Toast.LENGTH_LONG).show()
                }
                redirectToMain()
            }
    }


    /**进入主界面*/
    private fun redirectToMain() {
        //是否第一次进入App
        if (versionCode < AppUtil.getAppVersionCode(this)) {
            //进入向导界面
            startProxyActivity(GuideFragment::class.java)
        } else {
            //进入主界面
            redirectToMain(this)
        }
        finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        alphaAnimation.cancel()
        circleCountDownView.cancel()
    }

    //设置点击返回键不响应
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            true
        } else super.onKeyDown(keyCode, event)
    }



}

