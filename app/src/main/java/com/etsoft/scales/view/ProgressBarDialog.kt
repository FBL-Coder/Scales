package com.etsoft.scales.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.support.annotation.NonNull
import android.view.KeyEvent
import android.widget.TextView
import com.etsoft.scales.R

/**
 * Author：FBL  Time： 2018/7/20.
 * 加载Dialog
 */

class ProgressBarDialog @SuppressLint("NewApi")
constructor(private val mContext: Context) {
    private val dialog: Dialog = Dialog(mContext)

    init {
        dialog.setContentView(R.layout.progress_bar_dialog)
        dialog.create()
    }

    fun show(text: String = "加载数据...") {
        dialog.show()
        val textView = dialog.findViewById<TextView>(R.id.Dialog_TV)
        textView.text = text
    }

    fun show(text: String = "加载数据...", isClickHide: Boolean = true) {
        dialog.setCancelable(isClickHide)
        dialog.show()
        val textView = dialog.findViewById<TextView>(R.id.Dialog_TV)
        textView.text = text
    }

    fun hide() {
        dialog.hide()
    }

    fun isShow(): Boolean {
        return dialog.isShowing
    }


    fun destroyDialog() {
        dialog.dismiss()
    }
}
