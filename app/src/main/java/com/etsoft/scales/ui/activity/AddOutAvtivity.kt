package com.etsoft.scales.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.etsoft.scales.R
import com.etsoft.scales.Server.BlueUtils
import com.etsoft.scales.Server.BlueUtils.Companion.isReadData
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.app.MyApp.Companion.mBluetoothDataIsEnable
import com.etsoft.scales.bean.Out_Main_Bean
import com.etsoft.scales.utils.BlueBoothState
import com.etsoft.scales.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_add_out.*
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import com.etsoft.scales.utils.MoneyValueFilter
import android.text.InputFilter
import java.math.RoundingMode


/**
 * Author：FBL  Time： 2018/9/30.
 * 添加出库数据
 */
class AddOutAvtivity : BaseActivity() {


    private var mHandler: MyHandler? = null
    private var position = 0
    private var mType = 0

    override fun setView(): Int {
        return R.layout.activity_add_out
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
        Add_Out_Weight.filters = arrayOf<InputFilter>(MoneyValueFilter().setDigits(1))

        when (mType) {
            1 -> {
                Add_Out_Type_Name.text = "重量"
                Add_Out_Type.text = MyApp.mRecycleListBean_Type_1!!.data[position].name
                Add_Out_Uuit.text = MyApp.mRecycleListBean_Type_1!!.data[position].unit
                Add_Out_Weight.isEnabled = !MyApp.mBluetoothDataIsEnable
                Add_Out_Weight.isFocusable = !MyApp.mBluetoothDataIsEnable
            }
            2 -> {
                Add_Out_Type_Name.text = "数量"
                Add_Out_Type.text = MyApp.mRecycleListBean_Type_2!!.data[position].name
                Add_Out_Uuit.text = MyApp.mRecycleListBean_Type_2!!.data[position].unit
            }
            3 -> {
                Add_Out_Type_Name.text = "重量"
                Add_Out_Type.text = MyApp.mRecycleListBean_Type_3!!.data[position].name
                Add_Out_Uuit.text = MyApp.mRecycleListBean_Type_3!!.data[position].unit
                Add_Out_Weight.isEnabled = !MyApp.mBluetoothDataIsEnable
                Add_Out_Weight.isFocusable = !MyApp.mBluetoothDataIsEnable
            }
        }




        Add_Input_Cancle.setOnClickListener { finish() }

        Add_Input_Ok.setOnClickListener {
            val Weight = Add_Out_Weight.text.toString()
            val ToPlace = Add_Out_ToPlace.text.toString()
            if (Weight == "0" || Weight == "" || ToPlace == "") {
                ToastUtil.showText("请填写必需信息")
                return@setOnClickListener
            }
            setResult(100, intent
                    .run {
                        putExtra("data", Out_Main_Bean().run {
                            when (mType) {
                                1 -> recyclingPriceId = MyApp.mRecycleListBean_Type_1!!.data[position].id.toString()
                                2 -> recyclingPriceId = MyApp.mRecycleListBean_Type_2!!.data[position].id.toString()
                                3 -> recyclingPriceId = MyApp.mRecycleListBean_Type_3!!.data[position].id.toString()
                            }
                            weight = Weight
                            toPlace = ToPlace
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
    private class MyHandler(activity: AddOutAvtivity) : Handler() {
        private var activityWeakReference: WeakReference<AddOutAvtivity> = WeakReference<AddOutAvtivity>(activity)
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
                            activity.Add_Out_Weight.setText(DecimalFormat("0.0").run {
                                roundingMode = RoundingMode.DOWN
                                this
                            }.format(mWeight))
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
