package com.etsoft.scales.ui.activity

import android.app.Activity
import android.os.Handler
import android.os.Message
import com.etsoft.scales.utils.BlueBoothState
import com.etsoft.scales.R
import com.etsoft.scales.Server.BlueUtils
import com.etsoft.scales.Server.BlueUtils.Companion.isReadData
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.app.MyApp.Companion.mBluetoothDataIsEnable
import com.etsoft.scales.bean.Input_Main_List_Bean
import com.etsoft.scales.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_add_input.*
import java.lang.ref.WeakReference

/**
 * Author：FBL  Time： 2018/9/30.
 * 添加入库数据
 */
class AddInputAvtivity : BaseActivity() {


    private var mHandler: MyHandler? = null

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

    /**
     * Handler 静态内部类，防止内存泄漏
     */
    private class MyHandler(activity: BaseActivity) : Handler() {
        private var activityWeakReference: WeakReference<BaseActivity> = WeakReference<BaseActivity>(activity)
        override fun handleMessage(msg: Message) {
            val activity = activityWeakReference.get()
            if (activity != null) {
                when (msg.what) {
                    BlueBoothState.BLUE_READDATA_SUCCESS -> {
                        BlueUtils.disposeData(this, msg.obj as ByteArray)
                    }
                    BlueBoothState.BLUE_OBTAINDATA_ERROR -> {
                        ToastUtil.showText("蓝牙数据出错,请稍后在试")
                    }
                    BlueBoothState.BLUE_DISPOSEDATA_SUCCESS -> {
                        activity.Add_Input_KG.text = msg.obj as String
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
