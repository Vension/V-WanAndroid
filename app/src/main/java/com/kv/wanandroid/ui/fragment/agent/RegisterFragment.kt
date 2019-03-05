package com.kv.wanandroid.ui.fragment.agent

import android.os.Bundle
import android.view.View
import com.kv.wanandroid.Constant
import com.kv.wanandroid.R
import com.vension.frame.core.mvp.AbsCompatMVPFragment
import com.vension.frame.ext.showToast
import com.vension.frame.utils.PreferenceUtil
import com.kv.wanandroid.event.LoginEvent
import com.kv.wanandroid.mvp.contract.RegisterContract
import com.kv.wanandroid.mvp.model.bean.LoginData
import com.kv.wanandroid.mvp.presenter.RegisterPresenter
import com.wuhenzhizao.titlebar.widget.CommonTitleBar
import kotlinx.android.synthetic.main.fragment_register.*
import org.greenrobot.eventbus.EventBus

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/21 16:03
 * 描  述：注册账号
 * ========================================================
 */

class RegisterFragment : AbsCompatMVPFragment<RegisterContract.View, RegisterPresenter>(), RegisterContract.View {

    //本地保存的username
    private var user: String by PreferenceUtil(Constant.USERNAME_KEY, "")
    //本地保存的password
    private var pwd: String by PreferenceUtil(Constant.PASSWORD_KEY, "")


    override fun initToolBar(mCommonTitleBar: CommonTitleBar) {
        super.initToolBar(mCommonTitleBar)
        mCommonTitleBar.centerTextView.text = "注册"
    }

    override fun enableNetworkTip(): Boolean = false


    override fun attachLayoutRes(): Int {
        return R.layout.fragment_register
    }

    override fun initViewAndData(view: View, savedInstanceState: Bundle?) {
        super.initViewAndData(view, savedInstanceState)
        btn_register.setOnClickListener(onClickListener)
        tv_sign_in.setOnClickListener(onClickListener)
    }

    override fun lazyLoadData() {
    }

    override fun createPresenter(): RegisterPresenter {
        return RegisterPresenter()
    }

    override fun registerSuccess(data: LoginData) {
        showToast(getString(R.string.register_success))
        isLogin = true
        user = data.username
        pwd = data.password

        EventBus.getDefault().post(LoginEvent(true))
        activity!!.finish()
    }

    override fun registerFail() {
        isLogin = false
    }

    /**
     * OnClickListener
     */
    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btn_register -> {
                register()
            }
            R.id.tv_sign_in -> {
                startProxyActivity(LoginFragment::class.java)
                activity!!.finish()
                activity!!.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }


    /**
     * Register
     */
    private fun register() {
        if (validate()) {
            mPresenter?.registerWanAndroid(et_username.text.toString(),
                et_password.text.toString(),
                et_password2.text.toString())
        }
    }

    /**
     * check data
     */
    private fun validate(): Boolean {
        var valid = true
        val username: String = et_username.text.toString()
        val password: String = et_password.text.toString()
        val password2: String = et_password2.text.toString()
        if (username.isNullOrEmpty()) {
            et_username.error = getString(R.string.username_not_empty)
            valid = false
        }
        if (password.isNullOrEmpty()) {
            et_password.error = getString(R.string.password_not_empty)
            valid = false
        }
        if (password2.isNullOrEmpty()) {
            et_password2.error = getString(R.string.confirm_password_not_empty)
            valid = false
        }
        if (password != password2) {
            et_password2.error = getString(R.string.password_cannot_match)
            valid = false
        }
        return valid
    }


}