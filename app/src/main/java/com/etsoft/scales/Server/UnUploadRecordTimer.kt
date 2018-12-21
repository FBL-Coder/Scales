package com.etsoft.scales.Server

import android.os.Handler
import android.os.Looper
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.Ports
import com.etsoft.scales.SaveKey
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.app.MyApp.Companion.isUpLoading_Thread
import com.etsoft.scales.bean.AppInputBean
import com.etsoft.scales.bean.UpInputFailedBean
import com.etsoft.scales.netWorkListener.AppNetworkMgr
import com.etsoft.scales.ui.fragment.home.InputMainFragment.Companion.FileNoData
import com.etsoft.scales.utils.File_Cache
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.httpGetDataUtils.HttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.etsoft.scales.utils.httpGetDataUtils.ResultDesc

/**
 * Author：FBL  Time： 2018/10/23.
 * 检查本地未上传记录，并且自动上传
 */
class UnUploadRecordTimer {

    companion object {

        var mIsUpOkList: Array<Boolean>? = null


        //同步线程上传，待优化
        fun runTimeronUpload(handler: Handler) {
            Thread(Runnable {
                while (true) {
                    try {
                        LogUtils.i("后台线程查询是否存在未上传记录")
                        if (!MyApp.isUpLoading_UI) {
                            val NETWORK = AppNetworkMgr.getNetworkState(MyApp.mApplication!!.applicationContext)
                            if (NETWORK != 0) {
                                var datajson = File_Cache.readFile(SaveKey.FILE_DATA_NAME)
                                if (datajson != "") {
                                    var mFailedBean = MyApp.gson.fromJson(datajson, UpInputFailedBean::class.java)
                                    if (mFailedBean!!.data.size > 0) {
                                        LogUtils.i("后台线程查询存在未上传记录，条数为= " + mFailedBean!!.data.size)
                                        isUpLoading_Thread = true
                                        mIsUpOkList = Array(mFailedBean.data.size) { _ -> false }
                                        for (i in mFailedBean.data.indices) {
                                            Thread.sleep(1000)
                                            var upData = mFailedBean!!.data[i]
                                            LogUtils.i("后台线程上传数据 upLoadCount = " + upData.upLoadCount)
                                            OkHttpUtils.postSync(Ports.ADDOUTBACKLIST, MyApp.gson.toJson(upData), object : HttpCallback() {
                                                override fun onSuccess(resultDesc: ResultDesc?) {
                                                    super.onSuccess(resultDesc)
                                                    if (resultDesc!!.getcode() == 0) {
                                                        mIsUpOkList!![i] = true
                                                    } else {
                                                        upData.upLoadCount++
                                                        mIsUpOkList!![i] = false
                                                    }
                                                }

                                                override fun onFailure(code: Int, message: String?) {
                                                    super.onFailure(code, message)
                                                    mIsUpOkList!![i] = false
                                                    upData.upLoadCount++
                                                }
                                            }, "检索未上传，并且自动上传")
                                        }

                                        var UpNo = 0
                                        var UpOK = 0
                                        for (i in mIsUpOkList!!.indices) {
                                            if (mIsUpOkList!![i])
                                                UpOK++
                                            else
                                                UpNo++
                                        }
                                        LogUtils.i("后台线程未上传记录已上传完成, 成功：$UpOK，失败：$UpNo")
                                        for (i in mFailedBean.data.indices) {
                                            if (mIsUpOkList!![i]) {
                                                mFailedBean!!.data[i].isUpLoadOK = true
                                            }
                                        }
                                        var mUpInputFailedBean_Uped = UpInputFailedBean()
                                        mUpInputFailedBean_Uped.data = ArrayList<AppInputBean>()
                                        for (i in mFailedBean!!.data.indices) {
                                            if (!mFailedBean!!.data[i].isUpLoadOK)
                                                mUpInputFailedBean_Uped.data.add(mFailedBean!!.data[i])
                                        }
                                        File_Cache.writeFileToSD(MyApp.gson.toJson(mUpInputFailedBean_Uped), SaveKey.FILE_DATA_NAME)
                                        isUpLoading_Thread = false
                                        if (mUpInputFailedBean_Uped.data.size == 0) {
                                            handler.sendEmptyMessage(FileNoData)
                                            Looper.prepare()
                                            ToastUtil.showText("本地记录已全部上传")
                                            Looper.loop()
                                        }
                                    } else {
                                        LogUtils.i("后台线程查询没有未上传记录，进入进入睡眠")
                                        Thread.sleep(1800000)
                                    }
                                } else {
                                    LogUtils.i("后台线程返回“”，进入进入睡眠")
                                    Thread.sleep(600000)
                                }
                            } else {
                                LogUtils.i("后台线程查询网路不可用，进入进入睡眠")
                                Thread.sleep(300000)
                            }
                        } else {
                            LogUtils.i("后台线程查询UI界面正在上传，进入进入睡眠")
                            Thread.sleep(600000)
                        }
                    } catch (e: Exception) {
                        LogUtils.e("自动上传列表发生错误= $e")
                        isUpLoading_Thread = false
                        Thread.sleep(1800000)
                    }
                }
            }).start()
        }
    }
}
