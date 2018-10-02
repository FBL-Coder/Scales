package com.etsoft.scales.ui.activity

import android.os.Bundle
import android.view.View
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.R
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.bean.InputRecordListBean
import kotlinx.android.synthetic.main.activity_input_info.*

/**
 * Author：FBL  Time： 2018/8/7.
 * 入库详情
 */
class InputInfoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_info)

        initView()

        initData()
    }

    private fun initData() {
        var content = intent.getSerializableExtra("content") as InputRecordListBean.DataBean
        DoctorInfo_content.text = MyApp.gson.toJson(content)
        LogUtils.i(MyApp.gson.toJson(content))
    }

    private fun initView() {
        DoctorInfo_TitleBar.run {
            title.text = "入库详情"
            back.setOnClickListener { finish() }
            moor.visibility = View.GONE
        }
    }
}
