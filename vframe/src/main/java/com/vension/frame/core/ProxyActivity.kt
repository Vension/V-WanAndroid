package com.vension.frame.core

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import com.vension.frame.R
import com.vension.frame.ext.showToast


/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/11/1 16:37
 * 描  述：代理Activity
 * ========================================================
 */

open class ProxyActivity : AbsCompatActivity() {

    companion object {
        const val PROXY_FRAGMENT_CLASS_KEY = "Proxy Fragment Class key"
        const val PROXY_FRAGMENT_NAV_KEY = "Proxy Fragment Nav key"
        const val PROXY_FRAGMENT_POP_KEY = "Proxy Fragment Pop key"
    }

    private var mFragment: Fragment? = null

    override fun attachLayoutRes(): Int = R.layout.activity_proxy

    override fun enableNetworkTip(): Boolean {
        return false
    }

    private fun onCreatePageFragmentClass(): Class<out AbsCompatFragment>? {
        val mBundle = intent.extras
        return try {
            Class.forName(mBundle?.getString(PROXY_FRAGMENT_CLASS_KEY)) as Class<out AbsCompatFragment>
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (mFragment != null) {
            supportFragmentManager.putFragment(outState!!, "ProxyFragment", mFragment!!)
        }
    }

    override fun initViewAndData(savedInstanceState: Bundle?) {
        try {
            if (savedInstanceState != null) {
                mFragment = supportFragmentManager.getFragment(savedInstanceState, "ProxyFragment")
            }
            // setup main fragment
            if (mFragment == null) {

                val fClass = onCreatePageFragmentClass()

                if (null == fClass) {
                    showToast("Error: Proxy class is null")
                    return
                }

                val mBundle = Bundle(intent.extras)
                //状态栏是否可见
                if (!mBundle.containsKey(PROXY_FRAGMENT_NAV_KEY)) {
                    mBundle.putBoolean(PROXY_FRAGMENT_NAV_KEY, true)
                }
                //返回按钮是否显示
                if (!mBundle.containsKey(PROXY_FRAGMENT_POP_KEY)) {
                    mBundle.putBoolean(PROXY_FRAGMENT_POP_KEY, true)
                }

                /*
                * Google推荐采用静态工厂的方式创建Fragment，因为屏幕切换时Activity会重建
                * Fragment在重新创建的时候只会调用无参的构造方法，
                * 并且如果之前通过fragment.setArguments(bundle)这种方式设置过参数的话，Fragment重建时会得到这些参数
                */
                mFragment = Fragment.instantiate(this, fClass.name, mBundle)
                mFragment?.arguments = mBundle
            }
            //设置Fragment
            mFragment?.let { setMainFragment(it) }
        } catch (e: Exception) {
            finish()
        }
    }


    private fun setMainFragment(fragment: Fragment) {
        setMainFragment(fragment, 0, 0)
    }


    private fun setMainFragment(fragment: Fragment, enterAnim: Int, exitAnim: Int) {
        if (!this.isFinishing) {
            val mFragmentTransaction = getPageFragmentManager().beginTransaction()
            if (enterAnim != 0 && exitAnim != 0) {
                mFragmentTransaction.setCustomAnimations(enterAnim, exitAnim)
            }
            mFragmentTransaction.replace(R.id.proxy_content, fragment)
            mFragmentTransaction.commitAllowingStateLoss()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (mFragment != null) mFragment = null
    }

    override fun onBackPressed() {
        if (IFragment::class.java.isInstance(mFragment)) {
            (mFragment as IFragment).onBackPressed()
        }
        super.onBackPressed()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (IFragment::class.java.isInstance(mFragment)) {
            if ((mFragment as IFragment).onKeyDown(keyCode, event)) {
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

}