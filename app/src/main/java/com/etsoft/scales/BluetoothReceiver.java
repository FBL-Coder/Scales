package com.etsoft.scales;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.apkfuns.logutils.LogUtils;
import com.etsoft.scales.utils.ClsUtils;


public class BluetoothReceiver extends BroadcastReceiver {

    String pin = "1234";  //此处为你要连接的蓝牙设备的初始密钥，一般为1234或0000
    private Handler mHandler;

    public BluetoothReceiver(Handler handler) {
        mHandler = handler;
    }

    //广播接收器，当远程蓝牙设备被发现时，回调函数onReceiver()会被执行
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction(); //得到action
        LogUtils.e("action1=" + action);
        BluetoothDevice btDevice = null;  //创建一个蓝牙device对象
        // 从Intent中获取设备对象
        btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        if (BluetoothDevice.ACTION_FOUND.equals(action)) {  //发现设备
            LogUtils.e("发现设备:" + "[" + btDevice.getName() + "]" + ":" + btDevice.getAddress());

            if (btDevice.getName().contains("HC-05"))//HC-05设备如果有多个，第一个搜到的那个会被尝试。
            {
                if (btDevice.getBondState() == BluetoothDevice.BOND_NONE) {

                    LogUtils.e("ywq" + "attemp to bond:" + "[" + btDevice.getName() + "]");
                    try {
                        //通过工具类ClsUtils,调用createBond方法
                        ClsUtils.createBond(btDevice.getClass(), btDevice);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                       LogUtils.e(e);
                    }
                }
            } else {
                LogUtils.d("error" + "Is faild");
            }
        } else if (action.equals("android.bluetooth.device.action.PAIRING_REQUEST")) //再次得到的action，会等于PAIRING_REQUEST
        {
            LogUtils.d("action2=" + action);
            if (btDevice.getAddress().equals("00:15:83:45:68:5A")) {
                LogUtils.d("here" + "OKOKOK");
                try {

                    //1.确认配对
                    ClsUtils.setPairingConfirmation(btDevice.getClass(), btDevice, true);
                    //2.终止有序广播
                    LogUtils.d("order..." + "isOrderedBroadcast:" + isOrderedBroadcast() + ",isInitialStickyBroadcast:" + isInitialStickyBroadcast());
                    abortBroadcast();//如果没有将广播终止，则会出现一个一闪而过的配对框。
                    //3.调用setPin方法进行配对...
                    boolean ret = ClsUtils.setPin(btDevice.getClass(), btDevice, pin);
                    LogUtils.d(ret);
                    mHandler.sendEmptyMessage(200);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    LogUtils.e(e);
                }
            } else {
                LogUtils.e("提示信息", "这个设备不是目标蓝牙设备");
                mHandler.sendEmptyMessage(-2);
            }

        }
    }
}
