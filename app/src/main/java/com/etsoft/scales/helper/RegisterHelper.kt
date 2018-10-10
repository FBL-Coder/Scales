package com.etsoft.scales.helper

import com.etsoft.scales.ui.activity.BaseActivity
import com.etsoft.scales.Ports
import com.etsoft.scales.SaveKey
import com.etsoft.scales.utils.AppSharePreferenceMgr
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.httpGetDataUtils.MyHttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.etsoft.scales.utils.httpGetDataUtils.ResultDesc
import okhttp3.Call

/**
 * Author：FBL  Time： 2018/8/8.
 * 注册功能
 */
class RegisterHelper {
    companion object {

        /**
         * 注册方法
         */
        fun register(activity: BaseActivity, userid: String, pass: String, note: String = "") {
            var params = HashMap<String, String>()
            params[""] = userid
            params[""] = pass
            params[""] = note
            OkHttpUtils.postAsyn(Ports.REGISTER, params, object : MyHttpCallback(activity) {
                override fun onSuccess(resultDesc: ResultDesc?) {
                    if (resultDesc!!.getcode() != 0) {
                        ToastUtil.showText(resultDesc.result)
                    } else {
                        AppSharePreferenceMgr.put(SaveKey.USER_NAME, userid)
                        AppSharePreferenceMgr.put(SaveKey.USER_PASS, pass)
                    }
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                }
            }, "注册")

        }

    }

}
