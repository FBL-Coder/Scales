package com.etsoft.scales.ui.activity

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.BlueBoothState
import com.etsoft.scales.R
import com.etsoft.scales.Server.BlueUtils
import com.etsoft.scales.adapter.ListViewAdapter.BluetoothListAdapter
import com.etsoft.scales.app.MyApp.Companion.mBluetoothAdapter
import com.etsoft.scales.app.MyApp.Companion.mBluetoothSocket
import com.etsoft.scales.bean.BlueBoothDevicesBean
import com.etsoft.scales.receiver.BlueSSReceiver
import com.etsoft.scales.receiver.BluetoothReceiver
import com.etsoft.scales.utils.ClsUtils
import com.etsoft.scales.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_add_dev.*
import java.lang.ref.WeakReference
import java.util.*

/**
 * Author：FBL  Time： 2018/7/26.
 * 添加蓝牙设备
 */

class AddDevActivity : BaseActivity() {

    companion object {
        const val LIST_PAIRING = 100
        const val LIST_CONNECT = 200
    }

    private var mHandler: MyHandler? = null

    /**
     * 搜索设备集合
     */
    private var mList_SS = ArrayList<BlueBoothDevicesBean>()
    /**
     * 搜索设备显示适配器
     */
    private var mBlue_SS_Adapter: BluetoothListAdapter? = null
    /**
     * 已配对设备显示适配器
     */
    private var mBlue_OK_Adapter: BluetoothListAdapter? = null
    /**
     * 已配对设备集合
     */
    private val mBlue_Ok_List = ArrayList<BlueBoothDevicesBean>()
    /**
     * 请求配对 BluetoothReceiver
     */
    private var pairingReceiver: BluetoothReceiver? = null
    /**
     * 搜索 BluetoothReceiver
     */
    private var seekReceiver: BlueSSReceiver? = null


    override fun setView(): Int? {
        return R.layout.activity_add_dev
    }

    @SuppressLint("NewApi")
    override fun onCreate() {
        mHandler = MyHandler(this)
        initView()
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

        //检测蓝牙是否打开
        BlueUtils.isBlueOpen(this)

        Main_AddDev_Booth_SS.setOnClickListener {
            //搜索蓝牙设备
            startDiscovery()
        }

        //注册设备被发现时的广播
        val filter = IntentFilter("android.bluetooth.device.action.FOUND")
        filter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED")
        filter.priority = 1000
        seekReceiver = BlueSSReceiver(mHandler!!)
        registerReceiver(seekReceiver, filter)

        //注册设备配对时的广播
        val filter1 = IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST")
        filter1.addAction("android.bluetooth.device.action.FOUND")
        filter1.priority = 1000
        pairingReceiver = BluetoothReceiver(mHandler!!)
        registerReceiver(pairingReceiver, filter1)

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
    private fun PairingBlue() {
        if (mBlue_SS_Adapter != null)
            mBlue_SS_Adapter!!.setOnConnectClick { view, position ->
                mLoadDialog!!.show(arrayOf("正在配对", "配对超时"))
                ClsUtils.createBond(mList_SS[position].mDevice.javaClass, mList_SS[position].mDevice)
            }
    }

    /**
     * 已配对蓝牙列表点击事件
     * 连接设备
     */
    private fun ConnectBlue() {
        if (mBlue_OK_Adapter != null)

            mBlue_OK_Adapter!!.setOnConnectClick { view, position ->

                if (mBlue_Ok_List[position].isConnect) {
                    mLoadDialog!!.show(arrayOf("正在断开", "断开超时"))
                    BlueUtils.disConnect(mHandler!!, position)
                } else {
                    //创建连接
                    mLoadDialog!!.show(arrayOf("正在连接", "连接超时"))
                    BlueUtils.connectBlue(mHandler!!, mBlue_Ok_List, position)
                    LogUtils.e("连接蓝牙设备MAC: ${mBlue_Ok_List[position].mDevice.address} \n" +
                            "UUID:${mBlue_Ok_List[position].mDevice.uuids}")
                }
            }
    }


    /**
     * Handler 静态内部类，防止内存泄漏
     */
    private class MyHandler(activity: AddDevActivity) : Handler() {
        private val activityWeakReference: WeakReference<AddDevActivity> = WeakReference<AddDevActivity>(activity)

        override fun handleMessage(msg: Message) {
            val activity = activityWeakReference.get()
            if (activity != null) {

                when (msg.what) {
                    BlueBoothState.BLUE_PAIRING_SUCCESS -> {
                        activity.mLoadDialog!!.hide()
                        activity.startDiscovery()
                    }
                    BlueBoothState.BLUE_PAIRING_FAILED -> {
                        activity.mLoadDialog!!.hide()
                        ToastUtil.showText("配对失败")
                    }
                    BlueBoothState.BLUE_PAIRING_DEV_EROOR -> {
                        activity.mLoadDialog!!.hide()
                        ToastUtil.showText("不是目标设备")
                    }
                    BlueBoothState.BLUE_OBTAINDATA_SUCCESS -> {
//                        ToastUtil.showText("收到蓝牙数据" + msg.obj)
                    }

                    BlueBoothState.BLUE_OBTAINDATA_ERROR -> {
                        ToastUtil.showText("蓝牙数据解析异常")
                    }
                    BlueBoothState.BULECONNECT_SUCCESS -> {
                        // 连接成功
                        activity.mLoadDialog!!.hide()
                        activity.mBlue_OK_Adapter!!.notifyDataSetChanged(activity.mBlue_Ok_List)
                        //启动数据监听
                        mBluetoothSocket = msg.obj as BluetoothSocket
                        BlueUtils.disposeBlueData(this, mBluetoothSocket!!)
                    }

                    BlueBoothState.BLUE_CONNECT_CLOSE -> {
                        //断开连接
                        activity.mLoadDialog!!.hide()
                        activity.mBlue_Ok_List[msg.arg1 as Int].isConnect = false
                        activity.mBlue_OK_Adapter!!.notifyDataSetChanged(activity.mBlue_Ok_List)
                    }
                    BlueBoothState.BLUE_CONNECT_CLOSE_ERROR -> {
                        //断开连接异常
                        activity.mLoadDialog!!.hide()
                        ToastUtil.showText("断开连接异常")
                    }
                    BlueBoothState.BULECONNECT_FAILED -> {
                        //连接失败
                        activity.mLoadDialog!!.hide()
                        ToastUtil.showText("连接失败")
                    }
                    BlueBoothState.BLUE_SEEK_STOP -> {
                        activity.Main_AddDev_Booth_SS.isClickable = true
                        activity.Main_AddDev_Booth_SS.setImageResource(R.mipmap.ic_bluetooth_searching_light_blue_500_24dp)
                    }

                    BlueBoothState.BLUE_SEEK_PAIRING -> {
                        var device = msg.obj as BluetoothDevice
                        var blue_OK_Bean = BlueBoothDevicesBean()
                        blue_OK_Bean.mDevice = device
                        blue_OK_Bean.Name = device.name
                        blue_OK_Bean.MAC = device.address
                        activity.mBlue_Ok_List.add(blue_OK_Bean)
                        if (activity.mBlue_OK_Adapter == null) {
                            activity.mBlue_OK_Adapter = BluetoothListAdapter(activity, activity.mBlue_Ok_List, AddDevActivity.LIST_CONNECT)
                            activity.Main_AddDev_Connect.adapter = activity.mBlue_OK_Adapter
                        } else {
                            activity.mBlue_OK_Adapter!!.notifyDataSetChanged(activity.mBlue_Ok_List)
                        }
                        activity.ConnectBlue()
                    }

                    BlueBoothState.BLUE_SEEK_UNPAIRING -> {
                        var device = msg.obj as BluetoothDevice
                        var Bean = BlueBoothDevicesBean()
                        Bean.mDevice = device
                        Bean.MAC = device.address
                        Bean.Name = device.name
                        activity.mList_SS.add(Bean)
                        if (activity.mBlue_SS_Adapter == null) {
                            activity.mBlue_SS_Adapter = BluetoothListAdapter(activity, activity.mList_SS, AddDevActivity.LIST_PAIRING)
                            activity.Main_AddDev_SS.adapter = activity.mBlue_SS_Adapter
                        } else {
                            activity.mBlue_SS_Adapter!!.notifyDataSetChanged(activity.mList_SS)
                        }
                        activity.PairingBlue()
                    }
                }
            }
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        //解除注册
        unregisterReceiver(seekReceiver)
        unregisterReceiver(pairingReceiver)
        LogUtils.i("解除注册")
    }

}
