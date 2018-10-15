package com.etsoft.scales.ui.activity

import android.view.View
import com.etsoft.scales.R
import kotlinx.android.synthetic.main.activity_forget.*


/**
 * Author：FBL  Time： 2018/7/20.
 * 忘记密码
 */
class ForgetPassActivity : BaseActivity() {

    override fun setView(): Int {
        return R.layout.activity_forget
    }
    override fun onCreate() {

        initView()

        initEvent()
    }

    private fun initEvent() {
        ForGet_Ok.setOnClickListener {

        }
    }

    private fun initView() {
        ForGet_Titlebar.run {
            title.text = getString(R.string.forgetPass)
            back.setOnClickListener { finish() }
            moor.visibility = View.INVISIBLE
        }


    }
}
