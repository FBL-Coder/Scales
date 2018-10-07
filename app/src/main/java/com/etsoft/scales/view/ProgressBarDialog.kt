package com.etsoft.scales.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Looper
import android.support.annotation.NonNull
import android.view.KeyEvent
import android.widget.TextView
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.R
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.netWorkListener.AppNetworkMgr
import com.etsoft.scales.utils.ToastUtil
import java.util.*
import kotlin.math.log

/**
 * Author：FBL  Time： 2018/7/20.
 * 加载Dialog
 */

class ProgressBarDialog @SuppressLint("NewApi")
constructor(private val mContext: Context) {

    private var isShow = false
    private val dialog: Dialog = Dialog(mContext)

    init {
        dialog.setContentView(R.layout.progress_bar_dialog)
        dialog.create()
    }

    fun show(text: Array<String> = arrayOf("正在加载", "加载失败")) {
        val NETWORK = AppNetworkMgr.getNetworkState(MyApp.mApplication!!.applicationContext)
        var page = 0
        dialog.show()
        isShow = true
        val textView = dialog.findViewById<TextView>(R.id.Dialog_TV)
        textView.text = text[0]

         Timer().schedule(object : TimerTask() {
            override fun run() {
                LogUtils.i("Dialog显示时长--page:$page")
                if (page == 8) {
                    if (dialog.isShowing) {
                        dialog.dismiss()
                        cancel()
                    }
                    Looper.prepare()
                    ToastUtil.showText(text[1])
                    Looper.loop()
                } else {
                    if (!isShow) {
                        cancel()
                        LogUtils.i("dialog正常diss--isShow:$isShow")
                    }
                }
                page++
            }
        }, 0, 1000)
    }

    fun show(text: String = "加载数据...", isClickHide: Boolean = true) {
        dialog.setCancelable(isClickHide)
        dialog.show()
        val textView = dialog.findViewById<TextView>(R.id.Dialog_TV)
        textView.text = text
    }

    fun hide() {
        dialog.hide()
        isShow = false
    }

    fun isShow(): Boolean {
        return dialog.isShowing
    }


    fun destroyDialog() {
        dialog.dismiss()
    }
}
