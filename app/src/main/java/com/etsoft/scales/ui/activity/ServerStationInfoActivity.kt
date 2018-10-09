package com.etsoft.scales.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.etsoft.scales.Ports
import com.etsoft.scales.R
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.httpGetDataUtils.MyHttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.etsoft.scales.utils.httpGetDataUtils.ResultDesc
import kotlinx.android.synthetic.main.activity_serverstation_info.*
import okhttp3.Call

/**
 * Author：FBL  Time： 2018/7/25.
 * 服务站点详情
 */
class ServerStationInfoActivity : BaseActivity() {

    var id = "1"


    override fun setView(): Int {
        return R.layout.activity_serverstation_info
    }

    override fun onCreate() {
        initView()
        initData()
    }

    /**
     * 站点详情
     */
    private fun initData() {
        mLoadDialog!!.show()
        id = intent.getStringExtra("id")!!

        OkHttpUtils.getAsyn(Ports.SERVERLIST, HashMap<String, String>().run { put("id", id);this }, object : MyHttpCallback(this) {

            override fun onSuccess(resultDesc: ResultDesc?) {
                mLoadDialog!!.hide()
                ServerTation_Info_content.text = resultDesc!!.result
            }

            @SuppressLint("SetTextI18n")
            override fun onFailure(code: Int, message: String?) {
                mLoadDialog!!.hide()
                ServerTation_Info_content.text = "$code++$message"
                ToastUtil.showText(message)
            }
        }, "站点详情")
    }

    private fun initView() {
        ServerTation_Info_TitleBar.run {
            title.text = "站点详情"
            back.setOnClickListener { finish() }
            moor.setImageResource(R.mipmap.ic_autorenew_white_24dp)
            moor.setOnClickListener {
                initData()
            }
        }
    }
}
