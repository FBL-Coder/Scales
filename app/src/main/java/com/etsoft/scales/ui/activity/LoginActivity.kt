package com.etsoft.scales.ui.activity

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Bundle
import com.etsoft.scales.R
import com.etsoft.scales.SaveKey
import com.etsoft.scales.helper.LoginHelper
import com.etsoft.scales.utils.AppSharePreferenceMgr
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
        val UserID = AppSharePreferenceMgr.get(SaveKey.USER_NAME, "") as String
        val UserPASS = AppSharePreferenceMgr.get(SaveKey.USER_PASS, "") as String
        if (UserID != "" && UserPASS != "") {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
//            mLoadDialog!!.show("正在登陆",false)
//            LoginHelper.login(this, UserID, UserPASS)
        }
        initEvent()
    }

    private fun initEvent() {

        Versions.text = packageManager.getPackageInfo(packageName, 0).versionName

        Login_Enter.setOnClickListener {
            var id = Login_ID.text.toString()
            var pass = Login_Pass.text.toString()
            if (isApkInDebug()) {
                id = "17317390767"
                pass = "123456"
            }

            if (!Validator.isMobile(id) && pass.isEmpty()) {
                ToastUtil.showText("账号密码不合适，请核对后登陆")
                return@setOnClickListener
            }
            mLoadDialog!!.show(arrayOf("正在登陆", "登陆超时"), false)
            LoginHelper.login(this, id, pass)

        }

        Login_Forget.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgetPassActivity::class.java))
        }

    }

    /**
     * 判断是否处于debug模式
     */
    fun isApkInDebug(): Boolean {
        try {
            val info = applicationInfo
            return info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: Exception) {
            return false
        }

    }

}
