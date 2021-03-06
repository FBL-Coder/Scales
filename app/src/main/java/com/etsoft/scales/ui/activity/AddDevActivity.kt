package com.etsoft.scales.ui.activity

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.PersistableBundle
import android.view.View
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.R
import com.etsoft.scales.Server.BlueUtils
import com.etsoft.scales.Server.BlueUtils.Companion.isRepetitionConnect
import com.etsoft.scales.adapter.ListViewAdapter.BluetoothListAdapter
import com.etsoft.scales.app.MyApp.Companion.mBlueBoothReceiver
import com.etsoft.scales.app.MyApp.Companion.mBluetoothAdapter
import com.etsoft.scales.app.MyApp.Companion.mBluetoothDataIsEnable
import com.etsoft.scales.app.MyApp.Companion.mBluetoothDevice
import com.etsoft.scales.app.MyApp.Companion.mBluetoothDeviceED
import com.etsoft.scales.app.MyApp.Companion.mBluetoothSocket
import com.etsoft.scales.bean.BlueBoothDevicesBean
import com.etsoft.scales.utils.BlueBoothState
import com.etsoft.scales.utils.ClsUtils
import com.etsoft.scales.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_add_dev.*
import kotlinx.android.synthetic.main.titlebar_view.*
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
    private var mBlue_Ok_List = ArrayList<BlueBoothDevicesBean>()


    override fun setView(): Int? {
        return R.layout.activity_add_dev
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHandler = MyHandler(this)
        initView()
    }

    /**
     * 初始化逻辑
     */
    private fun initView() {
        Main_AddDev_TitleBar.run {
            title.text = "添加设备"
            moor.setImageResource(R.drawable.ic_bluetooth_connected_black_24dp)
            moor.setOnClickListener {
                if (mBluetoothAdapter == null || !mBluetoothAdapter!!.isEnabled) {
                    ToastUtil.showText("请打开蓝牙")
                    return@setOnClickListener
                }
                //搜索蓝牙设备
                startDiscovery()
            }
            back.setOnClickListener {
                finish()
            }
        }

        if (BlueUtils.isRegistered) {
            BlueUtils.unRegisterReceiver(applicationContext, mBlueBoothReceiver!!)
        }
        mBlueBoothReceiver = BlueUtils.registerSeekReceiver(applicationContext, mHandler!!)
        //检测蓝牙是否打开
        if (BlueUtils.isBlueOpen(this))
        //检测蓝牙是否连接
            if (mBluetoothSocket != null && mBluetoothSocket!!.isConnected)
                showConnectView()
            else
                startDiscovery()

    }

    /**
     * 显示已连接的设备
     */
    private fun showConnectView() {
        try {
            ConnectDevice.visibility = View.VISIBLE
            DevName.text = mBluetoothDevice!!.name
            DevMAC.text = mBluetoothDevice!!.address
            ConnectClose.setOnClickListener {
                LogUtils.i("主动断开蓝牙连接")
                mBluetoothDeviceED = null
                mLoadDialog!!.show(arrayOf("正在断开", "断开超时"), false)
                BlueUtils.disConnect(mHandler!!)
            }
        }catch (e:Exception){
            ToastUtil.showText("蓝牙连接异常")
        }
    }


    /**
     * 开始搜索设备
     * 17317390767
        123456
     */
    private fun startDiscovery() {
        moor_wiating.visibility = View.VISIBLE
        Main_AddDev_TitleBar.moor.visibility = View.GONE
        mList_SS.clear()
        mBlue_Ok_List.clear()
        mBlue_OK_Adapter?.notifyDataSetChanged(mBlue_Ok_List)
        mBlue_SS_Adapter?.notifyDataSetChanged(mList_SS)
        mBluetoothAdapter?.startDiscovery()
        LogUtils.i("开始搜索")
    }


    /**
     * 搜索列表点击
     * 配对设备
     */
    private fun PairingBlue() {
        if (mBluetoothDataIsEnable) {
            ToastUtil.showText("蓝牙为连接状态,不可配对其他设备")
            return
        }
        if (mBlue_SS_Adapter != null)
            mBlue_SS_Adapter!!.setOnConnectClick { view, position ->
                if (mBluetoothAdapter == null || !mBluetoothAdapter!!.isEnabled) {
                    ToastUtil.showText("请打开蓝牙")
                    return@setOnConnectClick
                }
                if (mBluetoothDataIsEnable) {
                    ToastUtil.showText("蓝牙已经连接,请先断开连接")
                    return@setOnConnectClick
                }
                mLoadDialog!!.show(arrayOf("正在配对", "配对超时"), false)
                ClsUtils.createBond(mList_SS[position].mDevice.javaClass, mList_SS[position].mDevice)
            }
    }

    /**
     * 已配对蓝牙列表点击事件
     * 连接设备
     */
    @SuppressLint(value = "NewApi")
    private fun ConnectBlue() {
        if (mBlue_OK_Adapter != null)

            mBlue_OK_Adapter!!.setOnConnectClick { view, position ->

                if (mBluetoothAdapter == null || !mBluetoothAdapter!!.isEnabled) {
                    ToastUtil.showText("请打开蓝牙")
                    return@setOnConnectClick
                }
                if (mBluetoothDataIsEnable) {
                    ToastUtil.showText("蓝牙为连接状态,请先断开连接")
                    return@setOnConnectClick
                }
                //创建连接
                isRepetitionConnect = true
                mLoadDialog!!.show(arrayOf("正在连接", "连接超时"), false)
                mBluetoothAdapter!!.cancelDiscovery()
                BlueUtils.connectBlue(mHandler!!, mBlue_Ok_List[position].mDevice)
                LogUtils.e("连接蓝牙设备MAC: ${mBlue_Ok_List[position].mDevice.address} \n" +
                        "UUID:${mBlue_Ok_List[position].mDevice.uuids}")
            }
    }

    override fun onStart() {
        LogUtils.i("取消搜索")
        mBluetoothAdapter?.cancelDiscovery()
        super.onStart()
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
                    BlueBoothState.BULECONNECT_OPEN -> {//蓝牙打开成功
                        activity.startDiscovery()
                    }
                    BlueBoothState.BLUE_PAIRING_SUCCESS -> {//配对成功
                        activity.mLoadDialog!!.hide()
                        activity.startDiscovery()
                    }
                    BlueBoothState.BLUE_PAIRING_FAILED -> {//配对失败
                        activity.mLoadDialog!!.hide()
                        ToastUtil.showText("配对失败")
                    }
                    BlueBoothState.BLUE_PAIRING_DEV_EROOR -> {//不是目标设备
                        activity.mLoadDialog!!.hide()
                        ToastUtil.showText("不是目标设备")
                    }
                    BlueBoothState.BLUE_OBTAINDATA_ERROR -> {//数据解析异常
                        ToastUtil.showText("蓝牙数据解析异常")
                    }
                    BlueBoothState.BULECONNECT_SUCCESS -> { // 连接成功
                        ToastUtil.showText("蓝牙已连接")
                        activity.mLoadDialog!!.hide()
                        activity.showConnectView()
                        try {
                            if (activity.mBlue_Ok_List.size == 1)
                                activity.mBlue_Ok_List.removeAt(0)
                            else {
                                for (i in activity.mBlue_Ok_List.indices) {
                                    if (activity.mBlue_Ok_List[i].MAC == mBluetoothDevice!!.address)
                                        activity.mBlue_Ok_List!!.removeAt(i)
                                }
                            }
                            activity.mBlue_OK_Adapter!!.notifyDataSetChanged(activity.mBlue_Ok_List)
                        } catch (e: Exception) {
                            mBluetoothAdapter!!.startDiscovery()
                        }
                    }
                    BlueBoothState.BLUE_CONNECT_CLOSE -> {//断开连接
                        ToastUtil.showText("蓝牙已断开")
                        activity.mLoadDialog!!.hide()
                        activity.ConnectDevice.visibility = View.GONE
                    }
                    BlueBoothState.BLUE_CONNECT_CLOSE_ERROR -> {//断开异常
                        //断开连接异常
                        activity.mLoadDialog!!.hide()
                        ToastUtil.showText("断开连接异常")
                    }
                    BlueBoothState.BULECONNECT_FAILED -> {//连接失败
                        //连接失败
                        activity.mLoadDialog!!.hide()
                        ToastUtil.showText("蓝牙连接失败")
                    }
                    BlueBoothState.BLUE_SEEK_STOP -> {//停止搜索
                        activity.moor_wiating.visibility = View.GONE
                        activity.Main_AddDev_TitleBar.moor.visibility = View.VISIBLE
                    }

                    BlueBoothState.BLUE_SEEK_PAIRING -> {//搜索到的已配对设备
                        //已配对设备
                        if (msg.obj != null) {
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
                    }

                    BlueBoothState.BLUE_SEEK_UNPAIRING -> {//搜索到的未配对设备
                        //未配对设备
                        if (msg.obj != null) {
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
    }
}
