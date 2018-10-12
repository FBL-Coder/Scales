package com.etsoft.scales.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Message
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

/**
 * Author：FBL  Time： 2018/9/30.
 * 添加入库数据
 */
class AddInputAvtivity : BaseActivity() {

    private var mHandler: MyHandler? = null
    private var position = 0

    override fun setView(): Int {
        return R.layout.activity_add_input
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
        Input_ServerStation.text = AppSharePreferenceMgr.get(SaveKey.SERVERSTATION_NAME, "未选择") as String
        Add_Input_Type.text = MyApp.mRecycleListBean?.data!![position]?.name
        Add_Input_DanWei.text = MyApp.mRecycleListBean?.data!![position]?.unit
        Add_Input_DanJia.text = "${MyApp.mRecycleListBean?.data!![position]?.price}"

        Input_ServerStation.setOnClickListener {
            val text = if (AppSharePreferenceMgr.get(SaveKey.SERVERSTATION_ID, -1) == -1) "是否前往选择服务站?" else "是否重新选择服务站?"
            MyDialog(this).setMessage(text)
                    .setNegativeButton("取消") { dialog, which ->
                        dialog.dismiss()
                    }.setPositiveButton("选择") { dialog, which ->
                        dialog.dismiss()
                        startActivity(Intent(this, ServerStationActivity::class.java))
                        finish()
                    }.show()
        }

        Add_Input_Cancle.setOnClickListener { finish() }

        Add_Input_Ok.setOnClickListener {
            val weight_tv = Add_Input_KG.text.toString()
            if (weight_tv == "0.00" || weight_tv == "0"||weight_tv == "") {
                ToastUtil.showText("货物重量不能为空")
                return@setOnClickListener
            }
            setResult(101, intent
                    .run {
                        putExtra("data", Input_Main_List_Bean().run {
                            type = MyApp.mRecycleListBean?.data!![position]?.name
                            weight = weight_tv
                            price = MyApp.mRecycleListBean?.data!![position]?.price.toString()
                            unit = MyApp.mRecycleListBean?.data!![position]?.unit
                            total = "${DecimalFormat("0.00").format(MyApp.mRecycleListBean!!.data[position].price * weight_tv.toDouble())}"
                            typeid = MyApp.mRecycleListBean?.data!![position]?.id.toString()
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
                            val mWeight = msg.obj as String
                            activity.Add_Input_KG.setText(mWeight)
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
