package com.etsoft.scales.ui.activity

import com.etsoft.scales.Ports
import com.etsoft.scales.R
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.httpGetDataUtils.MyHttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.etsoft.scales.utils.httpGetDataUtils.ResultDesc
import kotlinx.android.synthetic.main.actiivity_register_user.*

/**
 * Author：FBL  Time： 2018/10/10.
 * 注册客户
 */
class RegisterUserActivity : BaseActivity() {

    private var sex = 1

    override fun setView(): Int? {

        return R.layout.actiivity_register_user
    }

    override fun onCreate() {
        initView()
    }

    private fun initView() {
        RegisterUser_TitleBar.run {
            back.setOnClickListener { finish() }
            title.text = "客户注册"
            moor.setImageResource(R.drawable.ic_save_black_24dp)
            moor.setOnClickListener {
                mLoadDialog!!.show(arrayOf("正在注册", "注册超时"), false)
                upData()
            }
        }

        RegisterUser_Sax.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.man -> {
                    sex = 1
                }
                R.id.women -> {
                    sex = 0
                }
            }
        }
    }

    private fun upData() {
        val name = RegisterUser_Name.text.toString()
        val cardid = RegisterUser_CardID.text.toString()
        val phone = RegisterUser_Phone.text.toString()
        val district = RegisterUser_District.text.toString()
        val towns = RegisterUser_Towns.text.toString()
        val street = RegisterUser_Street.text.toString()
        val community = RegisterUser_Community.text.toString()
        val plot = RegisterUser_Plot.text.toString()
        val buildingno = RegisterUser_BuildingNo.text.toString()
        val doorno = RegisterUser_DoorNo.text.toString()

        if (name == "" || phone == "" || district == ""
                || towns == "" || street == "" || community == ""
                || plot == "" || buildingno == "" || doorno == "") {
            ToastUtil.showText("信息不全,请把左边有 ★ 的全部填写")
            mLoadDialog!!.hide()
            return
        }

        val map = HashMap<String, String>().run {
            put("name", name)
            put("greenId", cardid)
            put("phone", phone)
            put("county", district)
            put("town", towns)
            put("street", street)
            put("community", plot)
            put("neighborhood", community)
            put("buildingNo", buildingno)
            put("doorNo", doorno)
            put("sex", sex.toString())
            this
        }

        OkHttpUtils.postAsyn(Ports.REISTERUSER, MyApp.gson.toJson(map), object : MyHttpCallback(this) {

            override fun onSuccess(resultDesc: ResultDesc?) {
                mLoadDialog!!.hide()
                ToastUtil.showText("注册成功")
                finish()
            }

            override fun onFailure(code: Int, message: String?) {
                super.onFailure(code, message)
                mLoadDialog!!.hide()
                ToastUtil.showText(message)
            }

        }, "客户注册")
    }
}
