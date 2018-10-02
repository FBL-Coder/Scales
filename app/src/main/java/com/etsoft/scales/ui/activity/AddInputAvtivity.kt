package com.etsoft.scales.ui.activity

import android.app.Activity
import android.os.Bundle
import com.etsoft.scales.R
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.bean.Input_Main_List_Bean
import com.etsoft.scales.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_add_input.*

/**
 * Author：FBL  Time： 2018/9/30.
 * 添加入库数据
 */
class AddInputAvtivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_input)
        initData()
    }

    private fun initData() {

        var position = intent.getIntExtra("position", 0)

        Add_Input_Type.text = MyApp.mRecycleListBean!!.data[position].name
        Add_Input_KG.text = "56.56"
        Add_Input_DanWei.text = MyApp.mRecycleListBean!!.data[position].unit
        Add_Input_DanJia.text = "￥${MyApp.mRecycleListBean!!.data[position].price}"
        Add_Input_ZongJia.text = "￥${MyApp.mRecycleListBean!!.data[position].price * 56.56}"



        Add_Input_Cancle.setOnClickListener { finish() }

        Add_Input_Ok.setOnClickListener {
            ToastUtil.showText("正在提交")
            setResult(Activity.RESULT_OK, intent
                    .run {
                        putExtra("data", Input_Main_List_Bean().run {
                            type = MyApp.mRecycleListBean!!.data[position].name
                            weight = "56.56"
                            price = MyApp.mRecycleListBean!!.data[position].price.toString()
                            unit = MyApp.mRecycleListBean!!.data[position].unit
                            total = (MyApp.mRecycleListBean!!.data[position].price * 56.56).toString()
                            this
                        })
                        this
                    })
            finish()
        }
    }
}
