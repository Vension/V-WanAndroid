package com.vension.frame.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AppCompatDialog
import android.view.LayoutInflater
import com.vension.frame.R


/**
 * ========================================================
 * @author: Created by Vension on 2018/10/30 13:58.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class LoadingDialog : AppCompatDialog {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, theme: Int) : super(context, theme)
    constructor(context: Context?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(
        context,
        cancelable,
        cancelListener
    )

    class Builder(private val context: Context) {
        private var isCancelable = false
        private var isCancelOutside = false

        fun setCancelable(isCancelable: Boolean): Builder {
            this.isCancelable = isCancelable
            return this
        }

        fun setCancelOutside(isCancelOutside: Boolean): Builder {
            this.isCancelOutside = isCancelOutside
            return this
        }

        @SuppressLint("InflateParams")
        fun create(): LoadingDialog {
            val inflater = LayoutInflater.from(this.context)
            val view = inflater.inflate(R.layout.dialog_loading, null)
            val loadingDailog = LoadingDialog(this.context, R.style.LoadingDialogStyle)
            loadingDailog.setContentView(view)
            loadingDailog.setCancelable(this.isCancelable)
            loadingDailog.setCanceledOnTouchOutside(this.isCancelOutside)
            return loadingDailog
        }
    }
}