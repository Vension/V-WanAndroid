package com.vension.frame.core.mvp

import android.os.Bundle
import android.view.View
import com.vension.frame.core.AbsCompatFragment
import com.vension.frame.ext.showToast

/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/10/29 12:21
 * 描  述：带 MVP 的 Fragment - 基类
 *
 * Kotlin 泛型中的 in 和 out
 * 如果你的类是将泛型作为内部方法的返回，那么可以用 out:因为其主要是产生（produce）指定泛型对象
 * interface Production<out T> {
 *    fun produce(): T
 *  }
 * 如果你的类是将泛型对象作为函数的参数，那么可以用 in,因为其主要是消费指定泛型对象
 * interface Consumer<in T> {
 *    fun consume(item: T)
 *   }
 *
 * Invariant(不变)
 * 如果既将泛型作为函数参数，又将泛型作为函数的输出，那就既不用 in 或 out。
 * interface ProductionConsumer<T> {
 *    fun produce(): T
 *     fun consume(item: T)
 *  }
 * ========================================================
 */

abstract class AbsCompatMVPFragment<in V: IView,P : IPresenter<V>> : AbsCompatFragment(), IView {

    /*
    *   kotlin 懒加载，在第一次使用Presenter时初始化，这种设计是针对一个View只针对一个Presenter。
    *   多个Presenter的情况此处不应该使用泛型
    */
    protected val mPresenter: P by lazy {
        createPresenter()
    }

    /**
     *  创建Presenter
     *
     *  @return P
     */
    abstract fun createPresenter(): P

    override fun initViewAndData(view: View, savedInstanceState: Bundle?) {
        //Presenter attach View
        mPresenter?.let {
            it.attachView(this as V)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        //view和presenter解绑
        mPresenter?.let {
            it.detachView()
        }
    }

    override fun showLoading() {
        fraMultiStatusLayout?.let {
            it.showLoading()
        }
    }

    override fun hideLoading() {
        fraMultiStatusLayout?.let {
            it.showContent()
        }
    }


    override fun showProgressDialog(cancelable: Boolean) {
        if (!mLoadingDialog.isShowing){
            mLoadingDialog.show()
        }
    }

    override fun dismissProgressDialog() {
        if (mLoadingDialog.isShowing){
            mLoadingDialog.dismiss()
        }
    }

    override fun showMessage(message: String) {
        fraMultiStatusLayout?.let {
            it.showError()
        }
        showToast(message)
    }

}