package com.etsoft.scales.ui.activity

import android.view.View
import com.etsoft.scales.R
import com.etsoft.scales.bean.InputRecordListBean
import com.etsoft.scales.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_input_info.*

/**
 * Author：FBL  Time： 2018/8/7.
 * 入库详情
 */
class InputInfoActivity : BaseActivity() {

    override fun setView(): Int {
        return R.layout.activity_input_info
    }

    override fun onCreate() {

        initView()

        initData()
    }

    private fun initData() {
        var content = intent.getSerializableExtra("content") as InputRecordListBean.DataBean

        if (content != null) {
            Type.text = content?.recyclingPrice?.name
            Uuid.text = content?.recyclingPrice?.uuid
            weight.text = content?.weight.toString() + content?.recyclingPrice?.unit
            price.text = content?.recyclingPrice?.price.toString()
            total.text = content?.pay_money.toString()
            pay_type.text = if (content?.pay_type == 1) "现金" else "其他"
            username.text = content?.user_name
            userPhone.text = content?.user_phone
            workname.text = content?.staff_id.toString() + "NULL name"
            ServerStation.text = content?.servicePoint?.name
            serverPhone.text = content?.servicePoint?.functionary_phone
            date.text = content?.update_time
        } else ToastUtil.showText("发生错误")
    }

    private fun initView() {
        DoctorInfo_TitleBar.run {
            title.text = "入库详情"
            back.setOnClickListener { finish() }
            moor.visibility = View.INVISIBLE
        }
    }
}
