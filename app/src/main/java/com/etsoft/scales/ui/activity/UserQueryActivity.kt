package com.etsoft.scales.ui.activity

import android.view.View
import com.etsoft.scales.Ports
import com.etsoft.scales.R
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.bean.UserQueryBean
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.httpGetDataUtils.MyHttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.etsoft.scales.utils.httpGetDataUtils.ResultDesc
import kotlinx.android.synthetic.main.activity_user_query.*

/**
 * Author：FBL  Time： 2018/10/10.
 * 客户查询
 */
class UserQueryActivity : BaseActivity() {


    override fun setView(): Int? {
        return R.layout.activity_user_query
    }

    override fun onCreate() {
        initView()
    }

    private fun initData() {
        Query_Result.visibility = View.GONE
        val phone = User_Query_Title.text.toString()
        if (phone == "") {
            ToastUtil.showText("请输入手机号")
            return
        }
        mLoadDialog!!.show(arrayOf("正在查询", "查询超时"), false)
        OkHttpUtils.getAsyn(Ports.USERQUERY, HashMap<String, String>().run { put("phone", phone);this }, object : MyHttpCallback(this) {
            override fun onSuccess(resultDesc: ResultDesc?) {

                mLoadDialog!!.hide()
                if (resultDesc!!.getcode() != 0) {
                    ToastUtil.showText(resultDesc.result)
                } else {
                    val mUserQueryBean = MyApp.gson.fromJson<UserQueryBean>(resultDesc!!.result, UserQueryBean::class.java)
                    Query_Result.visibility = View.VISIBLE
                    Name.text = mUserQueryBean!!.data?.name
                    Time.text = mUserQueryBean!!.data?.create_time
                }

            }

            override fun onFailure(code: Int, message: String?) {
                super.onFailure(code, message)

                mLoadDialog!!.hide()
                ToastUtil.showText(message)
            }
        }, "用户查询")

    }

    private fun initView() {
        User_Query_TitleBar.run {
            back.setOnClickListener { finish() }
            title.text = "用户查询"
            moor.visibility = View.GONE
        }
        User_Query_Query.setOnClickListener { initData() }
    }
}
