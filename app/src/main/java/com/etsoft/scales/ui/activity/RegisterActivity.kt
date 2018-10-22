package com.etsoft.scales.ui.activity

import android.os.Bundle
import android.view.View
import com.etsoft.scales.R
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.Validator
import kotlinx.android.synthetic.main.activity_register.*

/**
 * Author：FBL  Time： 2018/7/20.
 * 注册页面
 */
class RegisterActivity : BaseActivity() {

    override fun setView(): Int {
        return R.layout.activity_register
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initEvent()
    }


    private fun initEvent() {
        Register_ok.setOnClickListener {

            var phone = Register_Phone.text.toString()
            var pass = Register_Password.text.toString()
            var note = Register_Note.text.toString()

            if (!Validator.isMobile(phone) && pass.isEmpty() && note.isEmpty()) ToastUtil.showText("账号密码不合适")

//            RegisterHelper.register(this, phone, pass, note)
        }

        Register_Note_Get.setOnClickListener {
            //获取短信验证码
            var phone = Register_Phone.text.toString()
            if (!Validator.isMobile(phone)) ToastUtil.showText(getString(R.string.phoneerror))
//            NoteHelper.getNote(this,phone)
        }
    }

    private fun initView() {
        Register_Titlebar.run {
            title.text = getString(R.string.register)
            back.setOnClickListener { finish() }
            moor.visibility = View.INVISIBLE
        }
    }

}
