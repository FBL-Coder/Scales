package com.etsoft.scales.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.Ports
import com.etsoft.scales.R
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.bean.NotificationInfoBean
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.httpGetDataUtils.MyHttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.etsoft.scales.utils.httpGetDataUtils.ResultDesc
import kotlinx.android.synthetic.main.activity_notification_info.*
import okhttp3.Call

/**
 * Author：FBL  Time： 2018/7/25.
 * 通知详情
 */
class NotificationInfoActivity : BaseActivity() {

    var id = "1"


    override fun setView(): Int {
        return R.layout.activity_notification_info
    }

    override fun onCreate() {
        initView()
        mLoadDialog!!.show()
        initData()
    }

    /**
     * 通知列表
     */
    private fun initData() {

        id = intent.getStringExtra("id")!!

        OkHttpUtils.getAsyn(Ports.NOTIFICATIONLISTINFO, HashMap<String, String>().run { put("id", id);this }, object : MyHttpCallback(this) {

            @SuppressLint("SetTextI18n")
            override fun onSuccess(resultDesc: ResultDesc?) {
                mLoadDialog!!.hide()
                try {
                    if (resultDesc!!.getcode() != 0) {
                        ToastUtil.showText(resultDesc.result)
                    } else {
                        val info = MyApp.gson.fromJson<NotificationInfoBean>(resultDesc!!.result, NotificationInfoBean::class.java)
                        Title.text = info?.data?.title
                        val type = if (info?.data?.type == 1) "物价调整" else "其他"
                        date.text = type + "  丨  " + info?.data?.admin_alias + "  丨  " + info?.data?.update_time
                        content.text = "    " + info?.data?.content
                    }
                } catch (e: Exception) {
                    LogUtils.e("获取数据异常 ：data= ${resultDesc!!.result}")
                    ToastUtil.showText("服务器异常")
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onFailure(code: Int, message: String?) {
                super.onFailure(code, message)
                mLoadDialog!!.hide()
                ToastUtil.showText("服务器异常")
            }
        }, "通知详情")
    }

    private fun initView() {
        Notification_Info_TitleBar.run {
            title.text = "通知详情"
            back.setOnClickListener { finish() }
            moor.visibility = View.GONE
            this
        }
    }
}
