package com.etsoft.scales.ui.activity

import android.os.Bundle
import android.view.View
import com.etsoft.scales.R
import com.etsoft.scales.adapter.ListViewAdapter.Main_Input_ListViewAdapter
import com.etsoft.scales.bean.Input_Main_List_Bean
import com.etsoft.scales.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_add_dev.*

/**
 * Author：FBL  Time： 2018/7/26.
 * 添加设备
 */

class AddDevActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dev)

        initView()
        initListTJ()
        initListSS()
    }

    private fun initListTJ() {
        var data = ArrayList<Input_Main_List_Bean>()
        for (i in 1..5)
            data.add(Input_Main_List_Bean().run { id = "已添加数据";this })
        Main_AddDev_TJ.adapter = Main_Input_ListViewAdapter(data)
    }

    private fun initListSS() {
        var data = ArrayList<Input_Main_List_Bean>()
        for (i in 1..5)
            data.add(Input_Main_List_Bean().run { id = "搜索设备";this })
        Main_AddDev_SS.adapter = Main_Input_ListViewAdapter(data)
    }

    private fun initView() {
        Main_AddDev_TitleBar.run {
            title.text = "添加设备"
            moor.visibility = View.GONE
            back.setOnClickListener {
                finish()
            }
        }

        Main_AddDev_Booth_SS.setOnClickListener {
            ToastUtil.showText("搜索设备")
        }
    }
}
