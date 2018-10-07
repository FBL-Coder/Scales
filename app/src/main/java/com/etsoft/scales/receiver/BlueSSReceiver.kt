package com.etsoft.scales.receiver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.BlueBoothState

/**
 * Author：FBL  Time： 2018/10/7.
 * 蓝牙搜索和被搜索BroadcastReceiver
 */
class BlueSSReceiver(mHandler: Handler) : BroadcastReceiver() {

    private var mHandler = mHandler

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent!!.action

        LogUtils.i(action)
        if (action == BluetoothDevice.ACTION_FOUND) {
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

            //显示已配对设备
            if (device.bondState == BluetoothDevice.BOND_BONDED) {//已配对设备
                mHandler.sendMessage(mHandler.obtainMessage().run {
                    what = BlueBoothState.BLUE_SEEK_PAIRING
                    obj = device
                    this
                })
            } else if (device.bondState != BluetoothDevice.BOND_BONDED) {//未配对设备
                mHandler.sendMessage(mHandler.obtainMessage().run {
                    what = BlueBoothState.BLUE_SEEK_UNPAIRING
                    obj = device
                    this
                })
            }

        } else if (action == BluetoothAdapter.ACTION_DISCOVERY_FINISHED) {//搜索结束
            mHandler.sendEmptyMessage(BlueBoothState.BLUE_SEEK_STOP)
        }
    }
}
