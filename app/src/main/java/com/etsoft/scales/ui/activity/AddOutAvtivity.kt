package com.etsoft.scales.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
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

/**
 * Author：FBL  Time： 2018/9/30.
 * 添加出库数据
 */
class AddOutAvtivity : BaseActivity() {


    private var mHandler: MyHandler? = null
    private var position = 0

    override fun setView(): Int {
        return R.layout.activity_add_out
    }

    override fun onCreate() {
        mHandler = MyHandler(this)
        //启动数据监听
        if (mBluetoothDataIsEnable) {
            isReadData = true
            BlueUtils.readBlueData(mHandler!!, MyApp.mBluetoothSocket!!)
        }
        initData()
    }

    private fun initData() {

        position = intent.getIntExtra("position", 0)

        Add_Out_Type.text = MyApp.mRecycleListBean!!.data[position].name
        Add_Out_Weight
        Add_Out_Uuit.text = MyApp.mRecycleListBean!!.data[position].unit
        Add_Out_ToPlace

        Add_Input_Cancle.setOnClickListener { finish() }

        Add_Input_Ok.setOnClickListener {
            val Weight = Add_Out_Weight.text.toString()
            val ToPlace = Add_Out_ToPlace.text.toString()
            if (Weight == "0.00") {
                ToastUtil.showText("该物品重量为:0.00kg,不可添加")
                return@setOnClickListener
            }
            setResult(100, intent
                    .run {
                        putExtra("data", Out_Main_Bean().run {
                            recyclingPriceId = MyApp.mRecycleListBean!!.data[position].id.toString()
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
                            val mWeight = msg.obj as String
                            activity.Add_Out_Weight.setText(mWeight)
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
