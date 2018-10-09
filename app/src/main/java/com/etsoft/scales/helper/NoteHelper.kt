package com.etsoft.scales.helper

import com.etsoft.scales.ui.activity.BaseActivity
import com.etsoft.scales.Ports
import com.etsoft.scales.utils.httpGetDataUtils.HttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.MyHttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.etsoft.scales.utils.httpGetDataUtils.ResultDesc
import okhttp3.Call

/**
 * Author：FBL  Time： 2018/8/8.
 * 短信验证
 */
class NoteHelper {
    companion object {

        fun getNote(activity: BaseActivity, phone: String) {
            var params = HashMap<String, String>()
            params[""] = phone
            OkHttpUtils.postAsyn(Ports.LOGIN, params, object : MyHttpCallback(activity) {
                override fun onSuccess(resultDesc: ResultDesc?) {
                }

                override fun onFailure(code: Int, message: String?) {
                }
            }, "短信验证")

        }

    }

}
