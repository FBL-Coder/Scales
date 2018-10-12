package com.etsoft.scales.ui.activity

import android.view.View
import com.etsoft.scales.R
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
        Type.text = content.recyclingPrice.name
        Uuid.text = content.recyclingPrice.uuid
        toPlace.text = content.to_place
        weight.text = "${content.weight} ${content.recyclingPrice.unit}"
        status.text = if (content.recyclingPrice.status.toInt() == 1) "上架" else "下架"
        price.text = content.recyclingPrice.price.toString()
        inventory.text = content.recyclingPrice.inventory.toString()
        update.text = content.recyclingPrice.update_time
        workname.text = content.staff_name
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
