package com.etsoft.scales.ui.activity

import android.os.Bundle
import android.view.View
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.R
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.bean.OutListBean
import kotlinx.android.synthetic.main.activity_out_info.*

/**
 * Author：FBL  Time： 2018/7/26.
 * 出库详情
 */

class OutInfoActivity : BaseActivity() {

    override fun setView(): Int {
        return R.layout.activity_out_info
    }
    override fun onCreate() {
        initView()
        initData()
    }

    private fun initData() {

        var content = intent.getSerializableExtra("content") as OutListBean.DataBean
        LogUtils.i(MyApp.gson.toJson(content))
    }

    private fun initView() {
        OutInfo_TitleBar.run {
            title.text = "出库详情"
            moor.visibility = View.INVISIBLE
            back.setOnClickListener {
                finish()
            }
        }
    }
}
