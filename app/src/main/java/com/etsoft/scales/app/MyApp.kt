package com.etsoft.scales.app

import android.annotation.SuppressLint
import android.app.Activity
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.bean.LoginBean
import com.etsoft.scales.bean.RecycleListBean
import com.etsoft.scales.utils.Density
import com.etsoft.scales.utils.gson.NullStringEmptyTypeAdapterFactory
import com.etsoft.scales.utils.httpGetDataUtils.LoggerInterceptor
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.smartdevice.aidltestdemo.ClientApplication
import okhttp3.OkHttpClient
import java.util.*


/**
 * Author：FBL  Time： 2018/7/20.
 */
open class MyApp : ClientApplication() {

    var activities: ArrayList<Activity>? = null
    var gson = Gson()


    companion object {
        var mApplication: MyApp? = null
        /**
         * 用户登录信息
         */
        var UserInfo: LoginBean? = null
        /**
         * gson NULL适配器
         */
        var gson = GsonBuilder().registerTypeAdapterFactory(NullStringEmptyTypeAdapterFactory<Any>()).create()
        /**
         * 回收物列表信息
         */
        var mRecycleListBean: RecycleListBean? = null

        /**
         * OkhttpClirnt
         */
        var mclient = OkHttpClient().newBuilder().addInterceptor(LoggerInterceptor()).build()


        /**
         * 定位对象
         */
        @SuppressLint("StaticFieldLeak")
        private var mLocationClient: AMapLocationClient? = null

        /**
         *  位置详情
         */
        var mLocationInfo: AMapLocation? = null

        /**
         * 开始定位
         */
        fun getLocation() {
            Thread(Runnable {
                mLocationClient!!.startLocation()
            }).start()
        }

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

        //初始化定位
        mLocationClient = AMapLocationClient(applicationContext)
        //设置定位回调监听
        mLocationClient!!.setLocationListener {
            if (it != null) {
                if (it.errorCode == 0) {
                    mLocationInfo = it
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    LogUtils.e("AmapError", "location Error, ErrCode:"
                            + it.errorCode + ", errInfo:"
                            + it.errorInfo);
                }
            }
        }

        var option = AMapLocationClientOption()
                .setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
                .setOnceLocation(true)
                .setOnceLocationLatest(true)
                .setNeedAddress(true)
        mLocationClient!!.setLocationOption(option)

        getLocation()
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
