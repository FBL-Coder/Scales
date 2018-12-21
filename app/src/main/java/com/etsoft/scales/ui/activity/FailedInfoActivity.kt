package com.etsoft.scales.ui.activity

import android.app.Activity
import android.os.Bundle
import android.widget.SimpleAdapter
import com.etsoft.scales.R
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.bean.AppInputBean
import com.etsoft.scales.view.MyDialog
import kotlinx.android.synthetic.main.activity_failed_info.*
import java.util.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.set

/**
 * Author：FBL  Time： 2018/11/19.
 * 未上传记录详情
 */
class FailedInfoActivity : BaseActivity() {


    private var mBean: AppInputBean? = null
    private var isDel: Boolean = false

    override fun setView(): Int? {
        return R.layout.activity_failed_info
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBean = intent.getSerializableExtra("data") as AppInputBean
        initView()
    }

    private fun initView() {
        Failed_Info_TitleBar.run {
            title.text = "记录详情"
            back.setOnClickListener {
                finish()
            }
            moor.setImageResource(R.drawable.ic_delete_forever_white_24dp)
            moor.setOnClickListener {
                MyDialog(this@FailedInfoActivity)
                        .setTitle("提示（慎重）")
                        .setMessage("如果此条数据已失效，或者无法上传。可以删除，您是否要删除？")
                        .setPositiveButton("删除") { dialog, which ->
                            dialog.dismiss()
                            isDel = true
                            finish()
                        }
                        .setNegativeButton("取消") { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()
            }
            moor.setOnLongClickListener {
                MyDialog(this@FailedInfoActivity)
                        .setTitle("上传参数")
                        .setMessage(MyApp.gson.toJson(mBean))
                        .setNegativeButton("关闭") { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()
                true
            }
        }
        Failed_Info_Cause.text = mBean?.failureInfo
        dealId.text = mBean?.dealId
        time.text = mBean?.time
        servicePointId.text = mBean?.servicePointId
        UpNum.text = "${mBean?.upLoadCount}"

        var list = ArrayList<HashMap<String, String>>()
        for (i in mBean!!.recyclings.indices) {
            var map = HashMap<String, String>()
            map["type"] = "类型：" + mBean!!.recyclings[i].typename
            map["weight"] = "重量：" + mBean!!.recyclings[i].weight + " kg"
            list.add(map)
        }

        Views.adapter = SimpleAdapter(this, list, R.layout.adapter_listview_faileditem, arrayOf("type", "weight"),
                intArrayOf(R.id.failed_test1, R.id.failed_test2))


    }

    override fun finish() {
        setResult(Activity.RESULT_OK, intent.run {
            putExtra("isDel", isDel)
            this
        })
        super.finish()
    }
}
