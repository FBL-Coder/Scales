package com.etsoft.scales.ui.activity

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.BluetoothReceiver
import com.etsoft.scales.R
import com.etsoft.scales.adapter.ListViewAdapter.BlueBooth_SSListViewAdapter
import com.etsoft.scales.adapter.ListViewAdapter.PairedBluetoothDialogAdapter
import com.etsoft.scales.app.MyApp.Companion.mBluetoothAdapter
import com.etsoft.scales.bean.BlueBoothSSBean
import com.etsoft.scales.bean.Blue_OK_Bean
import com.etsoft.scales.utils.ClsUtils
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.view.MyDialog
import kotlinx.android.synthetic.main.activity_add_dev.*
import java.lang.ref.WeakReference
import java.util.*


/**
 * Author：FBL  Time： 2018/7/26.
 * 添加蓝牙设备
 */

class AddDevActivity : BaseActivity() {

    /**
     * 搜索设备集合
     */
    private var mList_SS = ArrayList<BlueBoothSSBean>()
    /**
     * 搜索设备显示适配器
     */
    private var mBlue_SS_Adapter: BlueBooth_SSListViewAdapter? = null
    /**
     * 已配对设备显示适配器
     */
    private var mBlue_OK_Adapter: PairedBluetoothDialogAdapter? = null
    /**
     * 已配对设备集合
     */
    private val mBlue_Ok_List = ArrayList<Blue_OK_Bean>()
    /**
     * 请求配对 BluetoothReceiver
     */
    private var receiver: BluetoothReceiver? = null

    /**
     * 是否配对
     */
    private var isPairing = false


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dev)
        initView()
        blue_OK_Click()
        ssListClick()
    }


    //已配对蓝牙列表点击事件
    private fun blue_OK_Click() {
        Main_AddDev_TJ.setOnItemClickListener { parent, view, position, id ->


        }

    }

    /**
     * 初始化逻辑
     */
    private fun initView() {
        Main_AddDev_TitleBar.run {
            title.text = "添加设备"
            moor.visibility = View.INVISIBLE
            back.setOnClickListener {
                finish()
            }
        }

        Main_AddDev_Booth_SS.setOnClickListener {
            //搜索蓝牙设备
            startDiscovery()
        }

        if (!mBluetoothAdapter.isEnabled) {
            MyDialog(this).setMessage("需要打开蓝牙，是否打开？")
                    .setCancelable(false)
                    .setNegativeButton("取消") { dialog, which ->
                        dialog.dismiss()
                        finish()
                    }.setPositiveButton("打开") { dialog, which ->
                        dialog.dismiss()
                        //若没打开则打开蓝牙
                        mBluetoothAdapter.enable()
                    }.show()
        }
        //注册设备被发现时的广播
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(mReceiver, filter)
        //注册一个搜索结束时的广播
        val filter2 = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(mReceiver, filter2)

        val filter3 = IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST")
        filter3.addAction("android.bluetooth.device.action.FOUND")
        filter3.priority = 1000
        receiver = BluetoothReceiver(MyHandler(this))
        registerReceiver(receiver, filter3)
    }

    /**
     * 开始搜索设备
     */
    private fun startDiscovery() {
        mList_SS.clear()
        mBlue_Ok_List.clear()
        Main_AddDev_Booth_SS.isClickable = false
        Main_AddDev_Booth_SS.setImageResource(R.mipmap.ic_bluetooth_connected_white_24dp)
        mBluetoothAdapter.startDiscovery()
        LogUtils.i("开始搜索")
    }


    /**
     * 搜索列表点击
     * 配对设备
     */
    private fun ssListClick() {
        Main_AddDev_SS.setOnItemClickListener { parent, view, position, id ->
            isPairing = true
            mLoadDialog!!.show()
            ClsUtils.createBond(mList_SS[position].mDevice.javaClass, mList_SS[position].mDevice)
        }
    }

    /**
     * 搜索和被搜索BroadcastReceiver
     */
    private val mReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            val action = intent.action

            LogUtils.i(action)
            if (action == BluetoothDevice.ACTION_FOUND) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                //显示已配对设备
                if (device.bondState == BluetoothDevice.BOND_BONDED) {
                    var blue_OK_Bean = Blue_OK_Bean()
                    blue_OK_Bean.mDevice = device
                    blue_OK_Bean.name = device.name
                    blue_OK_Bean.mac = device.address
                    mBlue_Ok_List.add(blue_OK_Bean)
                    if (mBlue_OK_Adapter == null) {
                        mBlue_OK_Adapter = PairedBluetoothDialogAdapter(this@AddDevActivity, mBlue_Ok_List)
                        Main_AddDev_TJ.adapter = mBlue_OK_Adapter
                    } else {
                        mBlue_OK_Adapter!!.notifyDataSetChanged(mBlue_Ok_List)
                    }
                } else if (device.bondState != BluetoothDevice.BOND_BONDED) {//显示未配对设备
                    var Bean = BlueBoothSSBean()
                    Bean.mDevice = device
                    Bean.MAC = device.address
                    Bean.Name = device.name
                    mList_SS.add(Bean)
                    if (mBlue_SS_Adapter == null) {
                        mBlue_SS_Adapter = BlueBooth_SSListViewAdapter(mList_SS)
                        Main_AddDev_SS.adapter = mBlue_SS_Adapter
                    } else {
                        mBlue_SS_Adapter!!.notifyDataSetChanged(mList_SS)
                    }
                }

            } else if (action == BluetoothAdapter.ACTION_DISCOVERY_FINISHED) {//搜索结束
                Main_AddDev_Booth_SS.isClickable = true
                Main_AddDev_Booth_SS.setImageResource(R.mipmap.ic_bluetooth_searching_light_blue_500_24dp)
            }
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        //解除注册
        unregisterReceiver(mReceiver)
        unregisterReceiver(receiver)
        LogUtils.i("解除注册")
    }

    /**
     * Handler 静态内部类，防止内存泄漏
     */
    private class MyHandler(activity: AddDevActivity) : Handler() {
        private val activityWeakReference: WeakReference<AddDevActivity> = WeakReference<AddDevActivity>(activity)

        override fun handleMessage(msg: Message) {
            val activity = activityWeakReference.get()
            if (activity != null) {
                if (activity.isPairing) {
                    activity.isPairing = false
                    when (msg.what) {
                        200 -> {
                            activity.mLoadDialog!!.hide()
                            activity.startDiscovery()
                        }
                        -1 -> {
                            activity.mLoadDialog!!.hide()
                            ToastUtil.showText("配对失败")
                        }
                        -2 -> {
                            activity.mLoadDialog!!.hide()
                            ToastUtil.showText("不是目标设备")
                        }
                    }
                }

            }
        }
    }

}
