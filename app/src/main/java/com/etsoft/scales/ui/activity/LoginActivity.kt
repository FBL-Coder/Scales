package com.etsoft.scales.ui.activity

import android.content.Intent
import android.os.Bundle
import com.etsoft.scales.R
import com.etsoft.scales.helper.LoginHelper
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.Validator
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Author：FBL  Time： 2018/7/24.
 * 登陆页面
 */
class LoginActivity : BaseActivity() {

    override fun setView(): Int {
        return R.layout.activity_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initEvent()
    }

    private fun initEvent() {

        Versions.text = packageManager.getPackageInfo(packageName, 0).versionName

        Login_Enter.setOnClickListener {
            var id = Login_ID.text.toString()
            var pass = Login_Pass.text.toString()

            if (!Validator.isMobile(id) && pass.isEmpty()) {
                ToastUtil.showText("账号密码不合适")
            }
            mLoadDialog!!.show(arrayOf("正在登陆", "登陆超时"), false)
            LoginHelper.login(this, id, pass)

        }

        Login_Forget.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgetPassActivity::class.java))
        }

    }

}
