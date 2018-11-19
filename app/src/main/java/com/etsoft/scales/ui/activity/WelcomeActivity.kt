package com.etsoft.scales.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.etsoft.scales.SaveKey
import com.etsoft.scales.helper.LoginHelper
import com.etsoft.scales.utils.AppSharePreferenceMgr

/**
 * Author：FBL  Time： 2018/7/24.
 * 欢迎页面
 */
class WelcomeActivity : BaseActivity() {

    override fun setView(): Int? {
        return 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)// 隐藏标题

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)// 设置全屏
        startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
        finish()
    }
}



