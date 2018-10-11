package com.etsoft.scales.ui.activity

import android.view.View
import com.etsoft.scales.R
import com.etsoft.scales.app.MyApp
import kotlinx.android.synthetic.main.activity_self.*

/**
 * Author：FBL  Time： 2018/10/11.
 * 个人信息
 */
class SelfActivity : BaseActivity() {


    override fun setView(): Int? {
        return R.layout.activity_self
    }

    override fun onCreate() {
        initView()
    }

    private fun initView() {
        Mine_UserSelf_TitleBar.run {
            back.setOnClickListener { finish() }
            title.text = "个人信息"
            moor.visibility = View.GONE
            this
        }

        if (MyApp.UserInfo != null) {
            Name.text = MyApp.UserInfo?.data?.name
            Phone.text = MyApp.UserInfo?.data?.phone
            company.text = MyApp.UserInfo?.data?.company_name
            position.text = MyApp.UserInfo?.data?.position
            time.text = MyApp.UserInfo?.data?.create_time
        }

    }
}
