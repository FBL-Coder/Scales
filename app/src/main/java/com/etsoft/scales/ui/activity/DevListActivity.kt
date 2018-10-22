package com.etsoft.scales.ui.activity

import android.content.Intent
import android.os.Bundle
import com.etsoft.scales.R
import com.etsoft.scales.adapter.GridViewAdapter.GridView_CardView_Side_Adapter
import com.etsoft.scales.bean.CareFragment_Bean
import kotlinx.android.synthetic.main.activity_dev_list.*

/**
 * Author：FBL  Time： 2018/7/26.
 * 设备列表
 */

class DevListActivity : BaseActivity() {

    override fun setView(): Int {
        return R.layout.activity_dev_list
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initGridView()
    }

    private fun initGridView() {

        var list = ArrayList<CareFragment_Bean>().run {
            add(CareFragment_Bean(R.mipmap.ic_favorite_white_48dp, "#8ee5ee", "电子秤1"))
            add(CareFragment_Bean(R.mipmap.ic_favorite_white_48dp, "#8ee5ee", "电子秤2"))
            add(CareFragment_Bean(R.mipmap.ic_favorite_white_48dp, "#8ee5ee", "电子秤3"))
            add(CareFragment_Bean(R.mipmap.ic_favorite_white_48dp, "#8ee5ee", "电子秤3"))
            this
        }
        var mGridView_CardView_Adapter = GridView_CardView_Side_Adapter(list!!)
        DevList_GridView!!.adapter = mGridView_CardView_Adapter
    }

    private fun initView() {
        DevList_TitleBar.run {
            title.text = "设备列表"
            back.setOnClickListener { finish() }
            moor.setImageResource(R.drawable.ic_add_circle_outline_black_24dp)
            moor.setOnClickListener {
                startActivity(Intent(this@DevListActivity, AddDevActivity::class.java))

            }
        }
    }
}
