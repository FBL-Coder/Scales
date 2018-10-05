package com.etsoft.scales.app

import android.app.Activity
import android.app.Application
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.bean.LoginBean
import com.etsoft.scales.bean.RecycleListBean
import com.etsoft.scales.utils.Density
import com.etsoft.scales.utils.httpGetDataUtils.LoggerInterceptor
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import java.util.ArrayList
import com.etsoft.scales.utils.gson.NullStringEmptyTypeAdapterFactory
import com.smartdevice.aidltestdemo.ClientApplication


/**
 * Author：FBL  Time： 2018/7/20.
 */
open class MyApp : ClientApplication() {

    var activities: ArrayList<Activity>? = null

    companion object {
        var mApplication: MyApp? = null
        /**
         * 用户登录信息
         */
        var UserInfo: LoginBean? = null
        var gson = GsonBuilder().registerTypeAdapterFactory(NullStringEmptyTypeAdapterFactory<Any>()).create()
        /**
         * 回收物列表信息
         */
        var mRecycleListBean: RecycleListBean? = null

        var mclient = OkHttpClient().newBuilder().addInterceptor(LoggerInterceptor()).build()
    }

    override fun onCreate() {
        super.onCreate()
        mApplication = this
        OkHttpUtils.initClient(mclient)
        LogUtils.getLogConfig().configAllowLog(true)//logcat库 初始化
                .configTagPrefix("Scales-->  ")
                .configShowBorders(false)
        Fresco.initialize(this)//图片加载库 初始化
        Density.setDensity(this, 370f)//简单屏幕适配方案 初始化
    }

    fun getActivities(): List<Activity> {
        if (activities == null) {
            activities = ArrayList()
        }
        return activities as ArrayList<Activity>
    }

    fun setActivities(activity: Activity) {
        if (activities == null)
            activities = ArrayList()
        activities!!.add(activity)
    }
}
