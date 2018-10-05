package com.etsoft.scales.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.R
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.helper.LoginHelper
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.Validator
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Author：FBL  Time： 2018/7/24.
 * 登陆页面
 */
class LoginActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initEvent()
    }

    private fun initEvent() {
        Login_Enter.setOnClickListener {
            var id = Login_ID.text.toString()
            var pass = Login_Pass.text.toString()

            if (!Validator.isMobile(id) && pass.isEmpty()) {
                ToastUtil.showText("账号密码不合适")
            }
            mLoadDialog!!.show("正在登陆……")
            LoginHelper.login(this, id, pass)

        }

        Login_Forget.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgetPassActivity::class.java))
        }

        Login_Register.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

}
