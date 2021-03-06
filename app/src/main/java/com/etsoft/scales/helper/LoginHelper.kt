package com.etsoft.scales.helper

import android.content.Intent
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.Ports
import com.etsoft.scales.SaveKey
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.bean.LoginBean
import com.etsoft.scales.ui.activity.BaseActivity
import com.etsoft.scales.ui.activity.MainActivity
import com.etsoft.scales.utils.AppSharePreferenceMgr
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.httpGetDataUtils.MyHttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.etsoft.scales.utils.httpGetDataUtils.ResultDesc
import com.google.gson.Gson

/**
 * Author：FBL  Time： 2018/8/8.
 * 登陆功能
 */
class LoginHelper {
    companion object {
        fun login(activity: BaseActivity, userid: String, pass: String) {
            var params = HashMap<String, String>()
            params["phone"] = userid
            params["password"] = pass
            var jsonstr = Gson().toJson(params)

            OkHttpUtils.postAsyn(Ports.LOGIN, jsonstr,
                    object : MyHttpCallback(activity) {
                        override fun onSuccess(resultDesc: ResultDesc?) {
                            try {
                                activity.mLoadDialog!!.hide()
                                if (resultDesc!!.getcode() != 0) {
                                    ToastUtil.showText(resultDesc.result)
                                } else {
                                    MyApp.UserInfo = Gson().fromJson(resultDesc!!.result, LoginBean::class.java)
                                    AppSharePreferenceMgr.put(SaveKey.USER_NAME, userid)
                                    AppSharePreferenceMgr.put(SaveKey.USER_PASS, pass)
                                    AppSharePreferenceMgr.put(SaveKey.ACCESS_TOKEN, MyApp.UserInfo!!.data.access_token)
                                    AppSharePreferenceMgr.put(SaveKey.REFRESH_TOKEN, MyApp.UserInfo!!.data.refresh_token)
                                    activity.startActivity(Intent(activity, MainActivity::class.java))
                                    activity.finish()
                                }
                            } catch (e: Exception) {
                                LogUtils.e("获取数据异常 ：data= ${resultDesc!!.result}")
                                ToastUtil.showText("服务器异常")
                            }
                        }

                        override fun onFailure(code: Int, message: String?) {
                            super.onFailure(code, message)
                            activity.mLoadDialog!!.hide()
                            ToastUtil.showText("服务器异常")
                        }
                    }, "登陆")

        }
    }
}
