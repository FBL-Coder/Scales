package com.etsoft.scales.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.etsoft.scales.ui.activity.WelcomeActivity


/**
 * Author：FBL  Time： 2018/10/15.
 * 自启动服务
 */
class ContentReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val it = Intent(context, WelcomeActivity::class.java)
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(it)
    }
}
