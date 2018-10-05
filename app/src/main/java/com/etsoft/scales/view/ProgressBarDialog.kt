package com.etsoft.scales.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.support.annotation.NonNull
import android.view.KeyEvent
import android.widget.TextView
import com.etsoft.scales.R
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.netWorkListener.AppNetworkMgr
import com.etsoft.scales.utils.ToastUtil

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
        val NETWORK = AppNetworkMgr.getNetworkState(MyApp.mApplication!!.applicationContext)
        if (NETWORK == 0) {
            ToastUtil.showText("请检查网络连接")
        } else {
            dialog.show()
            val textView = dialog.findViewById<TextView>(R.id.Dialog_TV)
            textView.text = text
        }
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
