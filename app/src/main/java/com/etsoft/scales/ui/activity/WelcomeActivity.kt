package com.etsoft.scales.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager

/**
 * Author：FBL  Time： 2018/7/24.
 * 欢迎页面
 */
class WelcomeActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)// 隐藏标题
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)// 设置全屏

        Thread.sleep(1000)
        startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
        finish()
    }
}



