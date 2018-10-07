package com.etsoft.scales.Server

import android.bluetooth.BluetoothSocket
import android.os.Handler
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.BlueBoothState
import com.etsoft.scales.BlueBoothState.Companion.BLUE_CONNECT_CLOSE_ERROR
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.app.MyApp.Companion.mBluetoothSocket
import com.etsoft.scales.bean.BlueBoothDevicesBean
import com.etsoft.scales.ui.activity.BaseActivity
import com.etsoft.scales.utils.SystemUtils
import com.etsoft.scales.view.MyDialog
import java.io.IOException
import java.util.*

/**
 * Author：FBL  Time： 2018/10/7.
 */
class BlueUtils {

    companion object {
        /**
         * 连接蓝牙
         */
        fun connectBlue(mHandler: Handler, mBlueDevList: ArrayList<BlueBoothDevicesBean>, position: Int) {
            var btSocket: BluetoothSocket? = null
            try {

                btSocket = mBlueDevList[position].mDevice!!.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
                btSocket!!.connect()

                if (btSocket.isConnected) {
                    var mes = mHandler.obtainMessage()
                    mes.what = BlueBoothState.BULECONNECT_SUCCESS
                    mes.obj = btSocket
                    mHandler.sendMessage(mes)
                    mBlueDevList[position].isConnect = true
                } else
                    mHandler.sendEmptyMessage(BlueBoothState.BULECONNECT_FAILED)
            } catch (e: Exception) {
                mHandler.sendEmptyMessage(BlueBoothState.BULECONNECT_FAILED)
                LogUtils.e("蓝牙连接失败$e")
                try {
                    btSocket!!.close()
                } catch (e2: IOException) {
                }
            }
        }

        /**
         * 断开连接
         */
        fun disConnect(handler: Handler, position: Int) {
            if (mBluetoothSocket != null && mBluetoothSocket!!.isConnected) {
                try {
                    mBluetoothSocket!!.close()
                    handler.sendMessage(handler.obtainMessage().run {
                        what = BlueBoothState.BLUE_CONNECT_CLOSE
                        arg1 = position
                        this
                    })
                    LogUtils.w("蓝牙断开成功")
                } catch (e: Exception) {
                    LogUtils.e("蓝牙断开失败:$e")
                    handler.sendEmptyMessage(BLUE_CONNECT_CLOSE_ERROR)
                }
            }
        }

        /**
         * 蓝牙数据解析
         */
        fun disposeBlueData(mHandler: Handler, btSocket: BluetoothSocket) {
            Thread(Runnable {
                while (btSocket != null && btSocket.isConnected) {
                    //定义一个存储空间buff
                    val buff = ByteArray(8)
                    try {
                        var inStream = btSocket!!.inputStream
                        inStream!!.read(buff) //读取数据存储在buff数组中
                        LogUtils.w(buff)
                        var ReceiveData = SystemUtils.bytesToHexString(buff)

                        val msg = mHandler.obtainMessage()
                        msg.what = BlueBoothState.BLUE_OBTAINDATA_SUCCESS
                        msg.obj = ReceiveData
                        mHandler!!.sendMessage(msg)
                    } catch (e: Exception) {
                        LogUtils.w("蓝牙数据异常---$e")
                        mHandler!!.sendEmptyMessage(BlueBoothState.BLUE_OBTAINDATA_ERROR)
                    }
                }
            }).start()
        }


        /**
         * 检测蓝牙是否打开
         */
        fun isBlueOpen(activity: BaseActivity) {
            if (!MyApp.mBluetoothAdapter.isEnabled) {
                MyDialog(activity).setMessage("需要打开蓝牙，是否打开？")
                        .setCancelable(false)
                        .setNegativeButton("取消") { dialog, which ->
                            dialog.dismiss()
                            activity.finish()
                        }.setPositiveButton("打开") { dialog, which ->
                            dialog.dismiss()
                            //若没打开则打开蓝牙
                            MyApp.mBluetoothAdapter.enable()
                        }.show()
            }
        }
    }
}
