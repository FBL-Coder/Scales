package com.etsoft.scales.Server

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.IntentFilter
import android.os.Handler
import android.view.View
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.app.MyApp.Companion.mBluetoothAdapter
import com.etsoft.scales.app.MyApp.Companion.mBluetoothSocket
import com.etsoft.scales.receiver.BlueBoothReceiver
import com.etsoft.scales.ui.activity.AddDevActivity
import com.etsoft.scales.utils.BlueBoothState
import com.etsoft.scales.utils.BlueBoothState.Companion.BLUE_CONNECT_CLOSE_ERROR
import com.etsoft.scales.utils.SystemUtils
import com.etsoft.scales.view.MyDialog
import java.util.*

/**
 * Author：FBL  Time： 2018/10/7.
 * 蓝牙相关处理工具
 */
class BlueUtils {

    companion object {
        //是否读数据
        var isReadData = false
        //是否注册
        var isRegistered = false
        //是否重复尝试连接
        var isRepetitionConnect = true

        /**
         * 连接蓝牙
         */
        fun connectBlue(mHandler: Handler, mDevice: BluetoothDevice) {
            Thread(Runnable {
                while (isRepetitionConnect) {
                    LogUtils.e("蓝牙正在连接………………")
                    var btSocket: BluetoothSocket? = null
                    try {
                        btSocket = mDevice!!.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
                        btSocket!!.connect()

                        if (btSocket.isConnected) {
                            mBluetoothSocket = btSocket
                            mHandler?.sendEmptyMessage(BlueBoothState.BULECONNECT_SUCCESS)
                            isRepetitionConnect = false
                            return@Runnable
                        } else
                            mHandler?.sendEmptyMessage(BlueBoothState.BULECONNECT_FAILED)
                    } catch (e: Exception) {
                        mHandler?.sendEmptyMessage(BlueBoothState.BULECONNECT_FAILED)
                        LogUtils.e("蓝牙连接失败$e")
                    }
                    Thread.sleep(10000)
                }
            }).start()
        }

        /**
         * 断开连接
         */
        fun disConnect(handler: Handler) {
            if (mBluetoothSocket != null && mBluetoothSocket!!.isConnected) {
                try {
                    mBluetoothSocket!!.close()
                    LogUtils.w("蓝牙断开")
                } catch (e: Exception) {
                    LogUtils.e("蓝牙断开失败:$e")
                    handler.sendEmptyMessage(BLUE_CONNECT_CLOSE_ERROR)
                }
            }
        }

        /**
         * 蓝牙数据解析
         */
        fun readBlueData(mHandler: Handler, btSocket: BluetoothSocket) {
            Thread(Runnable {
                while (btSocket != null && btSocket.isConnected) {
                    if (!isReadData) {
                        return@Runnable
                    }
                    //定义一个存储空间buff
                    var buff = ByteArray(50)//原始十进制数组

                    try {
                        var inStream = btSocket!!.inputStream
                        inStream!!.read(buff) //读取数据存储在buff数组中
                        LogUtils.i(buff)
                        if (buff.contains(61)) {
                            LogUtils.i(buff)
                            var msg = mHandler?.obtainMessage()
                            msg.what = BlueBoothState.BLUE_READDATA_SUCCESS
                            msg.obj = buff
                            mHandler?.sendMessage(msg)
                        }
                    } catch (e: Exception) {
                        LogUtils.w("蓝牙数据异常---$e")
                        mHandler?.sendEmptyMessage(BlueBoothState.BLUE_OBTAINDATA_ERROR)
                    } catch (ee: Error) {
                        LogUtils.w("蓝牙数据错误---$ee")
                    }
                }
            }).start()
        }


        fun disposeData(mHandler: Handler, buff: ByteArray) {
            Thread(Runnable {
                var Secondbyte = ByteArray(8)//处理过的二进制数组
                var Second0xff = Array(8, init = { "" })//处理过的十六进制数组
                var SecondASCII = Array(8, init = { "" })//处理过的ASCII数组
                try {
                    val key: Byte = 61
                    for (i in buff.indices) {
                        if (buff[i] == key && buff.size - i >= 8) {
                            System.arraycopy(buff, i, Secondbyte, 0, 8)
                        }
                    }
                    if (Secondbyte.contains(0)) return@Runnable
                    for (i in Secondbyte.indices) {
                        Second0xff[i] = Integer.toHexString(Secondbyte[i].toInt())
                    }

                    for (i in Second0xff.indices) {
                        SecondASCII[i] = SystemUtils.convertHexToString(Second0xff[i])
                    }

                    var strWeight = ""
                    for (i in SecondASCII.indices) {
                        strWeight += SecondASCII[i]
                    }

                    var Str = strWeight.reversed().removeSuffix("=")
                    LogUtils.w("蓝牙秤重量为==$Str")
                    var msg = mHandler?.obtainMessage()
                    msg.what = BlueBoothState.BLUE_DISPOSEDATA_SUCCESS
                    msg.obj = Str.toDouble()
                    mHandler?.sendMessage(msg)
                } catch (e: Exception) {
                    LogUtils.w("蓝牙数据异常---$e")
                    mHandler?.sendEmptyMessage(BlueBoothState.BLUE_OBTAINDATA_ERROR)
                } catch (ee: Error) {
                    LogUtils.w("蓝牙数据错误---$ee")
                }
            }).start()
        }


        /**
         * 检测蓝牙是否打开
         */
        fun isBlueOpen(activity: AddDevActivity): Boolean {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (mBluetoothAdapter == null || !mBluetoothAdapter!!.isEnabled) {
                MyDialog(activity).setMessage("需要打开蓝牙，是否打开？")
                        .setCancelable(false)
                        .setNegativeButton("取消") { dialog, which ->
                            dialog.dismiss()
                            activity.finish()
                        }.setPositiveButton("打开") { dialog, which ->
                            dialog.dismiss()
                            //若没打开则打开蓝牙
                            MyApp.mBluetoothAdapter!!.enable()
                        }.create().run {
                            window.attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or
                                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            this
                        }.show()
                return false
            } else {
                return true
            }
        }

        /**
         * 注册蓝牙相关广播
         */
        fun registerSeekReceiver(context: Context, mHandler: Handler): BlueBoothReceiver {
            //注册Bluetooth广播

            var filter = IntentFilter()
            filter.addAction(BluetoothDevice.ACTION_FOUND)
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)
            filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            filter.priority = 1000

            var seekBlueReceiver = BlueBoothReceiver(mHandler)
            LogUtils.i("注册蓝牙广播接受器")
            context.registerReceiver(seekBlueReceiver, filter)
            isRegistered = true
            return seekBlueReceiver
        }

        /**
         * 解除注册
         */

        fun unRegisterReceiver(context: Context, receiver: BlueBoothReceiver) {
            if (receiver == null)
                return
            LogUtils.i("注销蓝牙广播接受器")
            context.unregisterReceiver(receiver)
            isRegistered = false
        }
    }
}
