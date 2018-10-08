package com.etsoft.scales.utils

/**
 * Author：FBL  Time： 2018/10/7.
 */
class BlueBoothState {
    companion object {

        //蓝牙打开
        const val BULECONNECT_OPEN = 1000

        //蓝牙关闭
        const val BULECONNECT_CLOSE = 2000

        //蓝牙连接成功
        const val BULECONNECT_SUCCESS = 200

        //蓝牙连接失败
        const val BULECONNECT_FAILED = -100

        //蓝牙断开连接
        const val BLUE_CONNECT_CLOSE = 210

        //蓝牙断开连接异常
        const val BLUE_CONNECT_CLOSE_ERROR = 212

        //蓝牙读取数据
        const val BLUE_READDATA_SUCCESS = 300

        //蓝牙处理数据
        const val BLUE_DISPOSEDATA_SUCCESS = 3000

        //蓝牙收到收据
        const val BLUE_OBTAINDATA_ERROR = 300

        //蓝牙搜索到已配对设备
        const val BLUE_SEEK_PAIRING = 10

        //蓝牙搜索到未配对设备
        const val BLUE_SEEK_UNPAIRING = 11

        //蓝牙搜索结束
        const val BLUE_SEEK_STOP = 12

        //蓝牙配对成功
        const val BLUE_PAIRING_SUCCESS = 22

        //蓝牙配对失败
        const val BLUE_PAIRING_FAILED = 21

        //蓝牙配对不是目标设备
        const val BLUE_PAIRING_DEV_EROOR = 23

    }
}
