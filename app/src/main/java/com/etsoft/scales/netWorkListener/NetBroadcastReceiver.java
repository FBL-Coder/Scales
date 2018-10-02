package com.etsoft.scales.netWorkListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.etsoft.scales.app.MyApp;

/**
 * Created by cheng on 2016/11/28.
 */
public class NetBroadcastReceiver extends BroadcastReceiver {
    private static NetEvevtChangListener netEvevt;
    public static void setEvevt(NetEvevtChangListener evevt) {
        netEvevt = evevt;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = AppNetworkMgr.getNetworkState(MyApp.Companion.getMApplication().getApplicationContext());
            // 接口回调传过去状态的类型
            try {
                netEvevt.onNetChange(netWorkState);
            }catch (Exception e){
            }
        }
    }


    // 自定义接口
    public interface NetEvevtChangListener {
        void onNetChange(int netMobile);
    }
}
