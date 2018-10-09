package com.etsoft.scales.ui.activity

import android.os.Bundle
import com.etsoft.scales.Ports
import com.etsoft.scales.R
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.httpGetDataUtils.MyHttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.etsoft.scales.utils.httpGetDataUtils.ResultDesc
import kotlinx.android.synthetic.main.activity_add_out.*
import okhttp3.Call

/**
 * Author：FBL  Time： 2018/9/30.
 * 添加出库数据
 */
class AddOutAvtivity : BaseActivity() {

    override fun setView(): Int {
        return R.layout.activity_add_out
    }

    override fun onCreate() {
        initData()
    }

    private fun initData() {
        Add_Out_Cancle.setOnClickListener { finish() }

        Add_Out_Ok.setOnClickListener {

            var toPlace = Add_Out_ToPlace.text.toString()
            var id = Add_Out_ID.text.toString()
            var weight = Add_Out_Weight.text.toString()
            if (toPlace.isEmpty() || id.isEmpty() || weight.isEmpty()) {
                ToastUtil.showText("请输入信息")
                return@setOnClickListener
            }
            var map = HashMap<String, String>().run {
                put("toPlace", toPlace)
                put("id", id)
                put("weight", weight)
                this
            }
            OkHttpUtils.postAsyn(Ports.ADDOUTBACK, map, object : MyHttpCallback(this) {

                override fun onSuccess(resultDesc: ResultDesc?) {

                }

                override fun onFailure(call: Call?, code: Int, message: String?) {

                }
            },"添加出库")
            finish()
        }
    }
}
