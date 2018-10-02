package com.etsoft.scales.ui.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import com.etsoft.scales.Ports
import com.etsoft.scales.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_out)
        initData()
    }

    private fun initData() {
        Add_Out_Cancle.setOnClickListener { finish() }

        Add_Out_Ok.setOnClickListener {

            var toPlace = Add_Out_ToPlace.text.toString()
            var id = Add_Out_ID.text.toString()
            var weight = Add_Out_Weight.text.toString()
            if (toPlace.isEmpty() || id.isEmpty() || weight.isEmpty()) {
                Add_Out_Ok.snack("请输入信息", Snackbar.LENGTH_SHORT) {
                    setAction("关闭") {
                        dismiss()
                    }
                }
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
