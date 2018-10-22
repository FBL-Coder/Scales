package com.etsoft.scales.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.PersistableBundle
import android.view.View
import com.etsoft.scales.R
import com.etsoft.scales.SaveKey
import com.etsoft.scales.Server.BlueUtils
import com.etsoft.scales.Server.BlueUtils.Companion.isReadData
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.app.MyApp.Companion.mBluetoothDataIsEnable
import com.etsoft.scales.bean.Input_Main_List_Bean
import com.etsoft.scales.utils.AppSharePreferenceMgr
import com.etsoft.scales.utils.BlueBoothState
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.view.MyDialog
import kotlinx.android.synthetic.main.activity_add_input.*
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import com.etsoft.scales.utils.MoneyValueFilter
import android.text.InputFilter



/**
 * Author：FBL  Time： 2018/9/30.
 * 添加入库数据
 */
class AddInputAvtivity : BaseActivity() {

    private var mHandler: MyHandler? = null
    private var position = 0
    private var mType = -1

    override fun setView(): Int {
        return R.layout.activity_add_input
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHandler = MyHandler(this)
        initData()
    }

    private fun initData() {

        position = intent.getIntExtra("position", 0)
        mType = intent.getIntExtra("type", -1)
        //启动数据监听
        if (mType == 1 || mType == 3)
            if (mBluetoothDataIsEnable) {
                isReadData = true
                BlueUtils.readBlueData(mHandler!!, MyApp.mBluetoothSocket!!)
            }
        Add_Input_KG.filters = arrayOf<InputFilter>(MoneyValueFilter().setDigits(1))


        when (mType) {
            1 -> {
                Add_Input_Type_Name.text = "重量"
                Add_Input_Type.text = MyApp.mRecycleListBean_Type_1?.data!![position]?.name
                Add_Input_DanWei.text = MyApp.mRecycleListBean_Type_1?.data!![position]?.unit
                Add_Input_DanJia.text = "${MyApp.mRecycleListBean_Type_1?.data!![position]?.price}"
                Add_Input_KG.isEnabled = !MyApp.mBluetoothDataIsEnable
                Add_Input_KG.isFocusable = !MyApp.mBluetoothDataIsEnable
            }
            2 -> {
                Add_Input_Type_Name.text = "数量"
                Add_Input_Type.text = MyApp.mRecycleListBean_Type_2?.data!![position]?.name
                Add_Input_DanWei.text = MyApp.mRecycleListBean_Type_2?.data!![position]?.unit
                Add_Input_DanJia.text = "${MyApp.mRecycleListBean_Type_2?.data!![position]?.price}"
            }
            3 -> {
                Add_Input_Type_Name.text = "重量"
                Add_Input_Type.text = MyApp.mRecycleListBean_Type_3?.data!![position]?.name
                Add_Input_DanWei.text = MyApp.mRecycleListBean_Type_3?.data!![position]?.unit
                Add_Input_DanJia.text = "${MyApp.mRecycleListBean_Type_3?.data!![position]?.price}"
                Add_Input_KG.isEnabled = !MyApp.mBluetoothDataIsEnable
                Add_Input_KG.isFocusable = !MyApp.mBluetoothDataIsEnable
            }
        }
        var serverName = AppSharePreferenceMgr.get(SaveKey.SERVERSTATION_NAME, "未选择") as String
        Input_ServerStation.text = serverName
        if (serverName == "未选择") {
            Input_ServerStation.setTextColor(Color.RED)
        } else Input_ServerStation.setTextColor(Color.BLACK)

        Input_ServerStation.setOnClickListener {
            val text = if (AppSharePreferenceMgr.get(SaveKey.SERVERSTATION_ID, -1) == -1) "是否前往选择服务站?" else "是否重新选择服务站?"
            MyDialog(this).setMessage(text)
                    .setNegativeButton("取消") { dialog, which ->
                        dialog.dismiss()
                    }.setPositiveButton("选择") { dialog, which ->
                        dialog.dismiss()
                        startActivity(Intent(this, ServerStationActivity::class.java))
                        finish()
                    }.create().run {
                        window.attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        this
                    }.show()
        }

        Add_Input_Cancle.setOnClickListener { finish() }

        Add_Input_Ok.setOnClickListener {
            val weight_tv = Add_Input_KG.text.toString()
            if (weight_tv == "0.0" || weight_tv == "0" || weight_tv == "") {
                ToastUtil.showText("货物重量不能为空")
                return@setOnClickListener
            }
            setResult(101, intent
                    .run {
                        putExtra("data", Input_Main_List_Bean().run {

                            when (mType) {
                                1 -> {
                                    type = MyApp.mRecycleListBean_Type_1?.data!![position]?.name
                                    price = MyApp.mRecycleListBean_Type_1?.data!![position]?.price.toString()
                                    unit = MyApp.mRecycleListBean_Type_1?.data!![position]?.unit
                                    typeid = MyApp.mRecycleListBean_Type_1?.data!![position]?.id.toString()
                                    total = DecimalFormat("0.0").format(MyApp.mRecycleListBean_Type_1!!.data[position].price * weight_tv.toDouble())
                                }
                                2 -> {
                                    type = MyApp.mRecycleListBean_Type_2?.data!![position]?.name
                                    price = MyApp.mRecycleListBean_Type_2?.data!![position]?.price.toString()
                                    unit = MyApp.mRecycleListBean_Type_2?.data!![position]?.unit
                                    typeid = MyApp.mRecycleListBean_Type_2?.data!![position]?.id.toString()
                                    total = DecimalFormat("0.0").format(MyApp.mRecycleListBean_Type_2!!.data[position].price * weight_tv.toDouble())
                                }
                                3 -> {
                                    type = MyApp.mRecycleListBean_Type_3?.data!![position]?.name
                                    price = MyApp.mRecycleListBean_Type_3?.data!![position]?.price.toString()
                                    unit = MyApp.mRecycleListBean_Type_3?.data!![position]?.unit
                                    typeid = MyApp.mRecycleListBean_Type_3?.data!![position]?.id.toString()
                                    total = DecimalFormat("0.0").format(MyApp.mRecycleListBean_Type_3!!.data[position].price * weight_tv.toDouble())
                                }
                            }
                            weight = weight_tv
                            this
                        })
                        this
                    })
            finish()
        }
    }

    /**
     * Handler 静态内部类，防止内存泄漏
     */
    @SuppressLint("SetTextI18n")
    private class MyHandler(activity: AddInputAvtivity) : Handler() {
        private var activityWeakReference: WeakReference<AddInputAvtivity> = WeakReference<AddInputAvtivity>(activity)
        override fun handleMessage(msg: Message) {
            val activity = activityWeakReference.get()
            if (activity != null) {
                when (msg.what) {
                    BlueBoothState.BLUE_READDATA_SUCCESS -> {
                        if (msg.obj != null)
                            BlueUtils.disposeData(this, msg.obj as ByteArray)
                    }
                    BlueBoothState.BLUE_OBTAINDATA_ERROR -> {
                        ToastUtil.showText("蓝牙数据出错,请稍后在试")
                    }
                    BlueBoothState.BLUE_DISPOSEDATA_SUCCESS -> {
                        if (msg.obj != null) {
                            val mWeight = msg.obj as Double
                            activity.Add_Input_KG.setText(DecimalFormat("0.0").format(mWeight))
                        }
                    }
                }
            }
        }
    }

    override fun onStop() {
        isReadData = false
        super.onStop()
    }
}
