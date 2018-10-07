package com.etsoft.scales.receiver

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler

import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.BlueBoothState
import com.etsoft.scales.utils.ClsUtils


class BluetoothReceiver(private val mHandler: Handler) : BroadcastReceiver() {

    internal var pin = "1234"  //此处为你要连接的蓝牙设备的初始密钥，一般为1234或0000

    //广播接收器，当远程蓝牙设备被发现时，回调函数onReceiver()会被执行
    override fun onReceive(context: Context, intent: Intent) {

        val action = intent.action //得到action
        LogUtils.e("action1=" + action!!)
        var btDevice: BluetoothDevice? = null  //创建一个蓝牙device对象
        // 从Intent中获取设备对象
        btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        var name = ""
        if (btDevice!!.name == null)
            name = "NULL"
        else
            name = btDevice.name
        if (BluetoothDevice.ACTION_FOUND == action) {  //发现设备

            LogUtils.e("发现设备:" + "[" + name + "]" + ":" + btDevice.address)

        } else if (action == "android.bluetooth.device.action.PAIRING_REQUEST") {
            //再次得到的action，会等于PAIRING_REQUEST
            if (name != "BTD-YH") {
                try {
                    //1.确认配对
                    ClsUtils.setPairingConfirmation(btDevice!!.javaClass, btDevice, true)
                    //2.终止有序广播
                    LogUtils.d("order...isOrderedBroadcast:$isOrderedBroadcast,isInitialStickyBroadcast:$isInitialStickyBroadcast")
                    abortBroadcast()//如果没有将广播终止，则会出现一个一闪而过的配对框。
                    //3.调用setPin方法进行配对...
                    val ret = ClsUtils.setPin(btDevice.javaClass, btDevice, pin)
                    LogUtils.d("配对结果---$ret")
                    if (ret)
                        mHandler.sendEmptyMessage(BlueBoothState.BLUE_PAIRING_SUCCESS)
                    else
                        mHandler.sendEmptyMessage(BlueBoothState.BLUE_PAIRING_FAILED)
                } catch (e: Exception) {
                    // TODO Auto-generated catch block
                    LogUtils.e("配对失败---$e")
                    mHandler.sendEmptyMessage(BlueBoothState.BLUE_PAIRING_FAILED)
                }
            }else{
                mHandler.sendEmptyMessage(BlueBoothState.BLUE_PAIRING_DEV_EROOR)
            }
        }
    }
}
