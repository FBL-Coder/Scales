package com.etsoft.scales.ui.activity

import android.os.Bundle
import android.view.View
import com.etsoft.scales.R
import com.etsoft.scales.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_editpass.*

/**
 * Author：FBL  Time： 2018/7/25.
 * 修改密码
 */
class EditPassActivity : BaseActivity() {

    override fun setView(): Int {
        return R.layout.activity_editpass
    }

    override fun onCreate() {
        initView()
    }

    private fun initView() {
        EditPass_Titlebar.run {
            title.text = "修改密码"
            back.setOnClickListener { finish() }
            moor.visibility = View.INVISIBLE
        }

        EditPass_OK.setOnClickListener {
            var newpass = EditPass_NewPass.text.toString()
            var newpass_ = EditPass_NewPass_.text.toString()
            if (newpass.isEmpty() || newpass_.isEmpty() || newpass !== newpass_) {
                ToastUtil.showText("请输入新密码")
                return@setOnClickListener
            }
            ToastUtil.showText("点击提交")

        }
    }
}
