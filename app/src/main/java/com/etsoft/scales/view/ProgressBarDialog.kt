package com.etsoft.scales.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Looper
import android.view.View
import android.widget.TextView
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.R
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.netWorkListener.AppNetworkMgr
import com.etsoft.scales.utils.ToastUtil
import java.util.*

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

    fun show(text: Array<String> = arrayOf("正在加载", "加载失败"), isClickHide: Boolean = true) {
        dialog.setCancelable(isClickHide)
        dialog.window.attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        dialog.show()
        isShow = true
        val textView = dialog.findViewById<TextView>(R.id.Dialog_TV)
        textView.text = text[0]
        dialogTimer(text[1])
    }

    private fun dialogTimer(text: String, pageMax: Int = 20) {
        var page1 = 0
        Timer().schedule(object : TimerTask() {
            override fun run() {
                LogUtils.i("Dialog显示时长--page:$page1")
                if (page1 == pageMax) {
                    if (dialog.isShowing) {
                        dialog.dismiss()
                        cancel()
                    }
                    Looper.prepare()
                    ToastUtil.showText(text)
                    Looper.loop()
                } else {
                    if (!isShow) {
                        cancel()
                        LogUtils.i("dialog正常diss--isShow:$isShow")
                    }
                }
                page1++
            }
        }, 0, 1000)
    }

    fun setMessage(text: String = "加载数据...") {
        val textView = dialog.findViewById<TextView>(R.id.Dialog_TV)
        textView.text = text
        dialog.show()
    }


    fun show(text: String = "加载数据...") {
        dialog.window.attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        dialog.show()
        isShow = true
        val textView = dialog.findViewById<TextView>(R.id.Dialog_TV)
        textView.text = text
        dialogTimer("加载失败")
    }

    fun show(text: String = "加载数据...", isClickHide: Boolean = true, pageMax: Int = 20) {
        dialog.window.attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        dialog.setCancelable(isClickHide)
        dialog.show()
        isShow = true
        val textView = dialog.findViewById<TextView>(R.id.Dialog_TV)
        textView.text = text
        dialogTimer("加载失败", pageMax)
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
