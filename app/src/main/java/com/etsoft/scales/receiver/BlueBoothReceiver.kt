package com.etsoft.scales.receiver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.utils.BlueBoothState
import com.etsoft.scales.app.MyApp.Companion.mBluetoothDevice
import com.etsoft.scales.app.MyApp.Companion.mBluetoothSocket
import com.etsoft.scales.utils.ClsUtils

/**
 * Author：FBL  Time： 2018/10/7.
 * 蓝牙 BroadcastReceiver
 */
class BlueBoothReceiver(mHandler: Handler) : BroadcastReceiver() {

    private var mHandler = mHandler

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent!!.action
        LogUtils.i(action)
        var mDevice: BluetoothDevice? = null
        var name = ""
        if (action == BluetoothDevice.ACTION_FOUND || action == BluetoothDevice.ACTION_PAIRING_REQUEST
                || action == BluetoothDevice.ACTION_ACL_CONNECTED || action == BluetoothDevice.ACTION_ACL_DISCONNECTED) {
            mDevice = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            if (mDevice != null && mDevice!!.name == null)
                name = "NULL"
            else
                name = mDevice.name
        }
        when (action) {
            BluetoothDevice.ACTION_FOUND -> {//蓝牙正在搜索
                LogUtils.w("发现设备 : 设备名称:$name \n 设备MAC:${mDevice!!.address}")
                //显示已配对设备
                if (mDevice.bondState == BluetoothDevice.BOND_BONDED) {//已配对设备
                    mHandler.sendMessage(mHandler.obtainMessage().run {
                        what = BlueBoothState.BLUE_SEEK_PAIRING
                        obj = mDevice
                        this
                    })
                } else if (mDevice.bondState != BluetoothDevice.BOND_BONDED) {//未配对设备
                    mHandler.sendMessage(mHandler.obtainMessage().run {
                        what = BlueBoothState.BLUE_SEEK_UNPAIRING
                        obj = mDevice
                        this
                    })
                }
            }

            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {//搜索结束
                mHandler.sendEmptyMessage(BlueBoothState.BLUE_SEEK_STOP)
            }

            BluetoothAdapter.ACTION_STATE_CHANGED -> {//蓝牙状态
                val blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)
                when (blueState) {
                    BluetoothAdapter.STATE_TURNING_ON -> {//正在打开
                    }
                    BluetoothAdapter.STATE_ON -> {//打开
                      mHandler.sendEmptyMessage(BlueBoothState.BULECONNECT_OPEN)
                    }
                    BluetoothAdapter.STATE_TURNING_OFF -> {//正在关闭
                    }
                    BluetoothAdapter.STATE_OFF -> {//关闭
                        mHandler.sendEmptyMessage(BlueBoothState.BULECONNECT_CLOSE)
                    }
                }
            }
            BluetoothDevice.ACTION_PAIRING_REQUEST -> {//配对请求
                if (name != "BTD-YH") {
                    try {
                        //1.确认配对
                        ClsUtils.setPairingConfirmation(mDevice!!.javaClass, mDevice, true)
                        //2.终止有序广播
                        LogUtils.d("order...isOrderedBroadcast:$isOrderedBroadcast,isInitialStickyBroadcast:$isInitialStickyBroadcast")
                        abortBroadcast()//如果没有将广播终止，则会出现一个一闪而过的配对框。
                        //3.调用setPin方法进行配对...
                        val ret = ClsUtils.setPin(mDevice.javaClass, mDevice, "1234")
                        LogUtils.d("配对结果---$ret")
                        if (ret)
                            mHandler.sendEmptyMessage(BlueBoothState.BLUE_PAIRING_SUCCESS)
                        else
                            mHandler.sendEmptyMessage(BlueBoothState.BLUE_PAIRING_FAILED)
                    } catch (e: Exception) {
                        LogUtils.e("配对失败---$e")
                        mHandler.sendEmptyMessage(BlueBoothState.BLUE_PAIRING_FAILED)
                    }
                } else {
                    mHandler.sendEmptyMessage(BlueBoothState.BLUE_PAIRING_DEV_EROOR)
                }
            }

            BluetoothDevice.ACTION_ACL_CONNECTED -> {//连接成功
//                mHandler.sendEmptyMessage(BlueBoothState.BULECONNECT_SUCCESS)
                LogUtils.i("蓝牙回调   连接成功")
            }

            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {//连接断开
                LogUtils.i("蓝牙回调   断开连接")
                if (mBluetoothSocket != null) {
                    mBluetoothSocket!!.close()
                    mBluetoothSocket = null
                    mBluetoothDevice = null
                }
                mHandler.sendEmptyMessage(BlueBoothState.BLUE_CONNECT_CLOSE)
            }
        }
    }
}
