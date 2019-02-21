package com.etsoft.scales.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.Ports
import com.etsoft.scales.R
import com.etsoft.scales.SaveKey
import com.etsoft.scales.adapter.ListViewAdapter.InputFailedRecordListViewAdapter
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.app.MyApp.Companion.isUpLoading_UI
import com.etsoft.scales.bean.AppInputBean
import com.etsoft.scales.bean.UpInputFailedBean
import com.etsoft.scales.netWorkListener.AppNetworkMgr
import com.etsoft.scales.utils.File_Cache
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.httpGetDataUtils.MyHttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.etsoft.scales.utils.httpGetDataUtils.ResultDesc
import kotlinx.android.synthetic.main.activity_inputfailed_record.*
import java.lang.ref.WeakReference

/**
 * Author：FBL  Time： 2018/7/26.
 * 入库失败记录
 */

class UploadFailedActivity : BaseActivity() {

    private var mInputRecordListViewAdapter: InputFailedRecordListViewAdapter? = null
    private var myHandler: MyHandler? = null
    private var mUpInputFailedBean: UpInputFailedBean? = null
    private var mIsUpOkList: Array<Boolean>? = null
    private val UpLoadOK = 100
    private val UpLoadFailed = -11
    private var isUpLoading = false
    private var delPosition = -1

    override fun setView(): Int {
        return R.layout.activity_inputfailed_record
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isUpLoading_UI = true
        initView()
        myHandler = MyHandler(this)
        if (MyApp.isUpLoading_Thread) {
            ToastUtil.showText("后台正在自动上传，请稍后操作")
        } else
            initdata()
    }

    /**
     * 读取本地入库失败数据
     */
    private fun initdata() {
        mLoadDialog!!.show()
        Thread(Runnable {

            var data = File_Cache.readFile(SaveKey.FILE_DATA_NAME)
            if (data != null && data != "") {
                LogUtils.i("未上传成功json数据 = $data")
                mUpInputFailedBean = MyApp.gson.fromJson(data, UpInputFailedBean::class.java)
                myHandler!!.sendEmptyMessage(1)
            }
        }).start()
    }

    /**
     * 初始化ListView数据
     */
    private fun initListView() {
        var mUpInputFailedBean_Use = UpInputFailedBean()
        mUpInputFailedBean_Use!!.data = ArrayList<AppInputBean>()
        for (i in mUpInputFailedBean!!.data.indices) {
            if (!mUpInputFailedBean!!.data[i].isUpLoadOK) {
                mUpInputFailedBean_Use!!.data.add(mUpInputFailedBean!!.data[i])
            }
        }

        if (mInputRecordListViewAdapter == null) {
            mInputRecordListViewAdapter = InputFailedRecordListViewAdapter(mUpInputFailedBean_Use!!)
            InputFailed_ListView.adapter = mInputRecordListViewAdapter
        } else mInputRecordListViewAdapter!!.notifyDataSetChanged(mUpInputFailedBean_Use!!)

        if (mUpInputFailedBean_Use.data.size == 0) {
            ToastUtil.showText("已无未上传数据")
            finish()
        }
        InputFailed_ListView.setOnItemClickListener { parent, view, position, id ->
            startActivityForResult(Intent(this, FailedInfoActivity::class.java).run {
                putExtra("data", mUpInputFailedBean!!.data[position])
                this
            }, Activity.RESULT_FIRST_USER)
            delPosition = position
        }
    }

    private fun initView() {
        InputFailed_Titlebar.title.text = "未上传记录"
        InputFailed_Titlebar.moor.setImageResource(R.drawable.ic_cloud_upload_white_24dp)
        InputFailed_Titlebar.moor.setOnClickListener {
            if (MyApp.isUpLoading_Thread) {
                ToastUtil.showText("后台正在自动上传，请稍后操作")
                return@setOnClickListener
            }
            val NETWORK = AppNetworkMgr.getNetworkState(MyApp.mApplication!!.applicationContext)
            if (NETWORK == 0) {
                ToastUtil.showText("当前网络不可用，请稍后再试")
                return@setOnClickListener
            }

            if (isUpLoading) {
                ToastUtil.showText("正在上传，请稍后")
                return@setOnClickListener
            }

            UploadData()
        }
        InputFailed_Titlebar.back.setOnClickListener {
            finish()
        }
    }

    private fun UploadData() {
        if (mUpInputFailedBean == null || mUpInputFailedBean!!.data?.size!! == 0) {
            return
        }
        isUpLoading = true
        mLoadDialog!!.show("正在上传:" + "0/" + mUpInputFailedBean!!.data.size, false, 15)
        val num = mUpInputFailedBean!!.data.size
        mIsUpOkList = Array(num) { _ -> false }
        Thread(Runnable {
            for (i in 0..num - 1) {
                Thread.sleep(1000)
                var upData = mUpInputFailedBean!!.data[i]
                OkHttpUtils.postAsyn(Ports.ADDOUTBACKLIST, MyApp.gson.toJson(upData), object : MyHttpCallback(this) {
                    override fun onSuccess(resultDesc: ResultDesc?) {
                        var msg = myHandler!!.obtainMessage()
                        msg.arg1 = i + 1
                        msg.arg2 = num
                        if (resultDesc!!.getcode() != 0) {
                            msg.obj = resultDesc.result
                            msg.what = UpLoadFailed
                            myHandler!!.sendMessage(msg)
                            mIsUpOkList!![i] = false
                            upData.failureInfo = "返回数据Code != 0，返回数据 = ${resultDesc.result} "
                            upData.upLoadCount++
                        } else {
                            mIsUpOkList!![i] = true
                            msg.what = UpLoadOK
                            myHandler!!.sendMessage(msg)
                        }
                    }

                    override fun onFailure(code: Int, message: String?) {
                        super.onFailure(code, message)
                        LogUtils.d(message)
                        var mes = if (message?.length!! > 200) message?.substring(0, 200) else message
                        mIsUpOkList!![i] = false
                        upData.failureInfo = "onFailure方法，服务器异常、Timeout $mes"
                        upData.upLoadCount++
                        var msg = myHandler!!.obtainMessage()
                        msg.obj = message
                        msg.what = UpLoadFailed
                        msg.arg1 = i + 1
                        msg.arg2 = num
                        myHandler!!.sendMessage(msg)
                    }
                }, "新增入库")
            }
        }).start()
    }

    fun listViewData(num: Int) {
        mLoadDialog!!.show()
        var UpNo = 0
        var UpOK = 0
        for (i in mIsUpOkList!!.indices) {
            if (mIsUpOkList!![i])
                UpOK++
            else
                UpNo++
        }
        ToastUtil.showText("上传完成, 成功：$UpOK，失败：$UpNo", 3000)
        for (i in 0..num - 1) {
            if (mIsUpOkList!![i]) {
                mUpInputFailedBean!!.data[i].isUpLoadOK = true
            }
        }
        writeData()
    }

    private fun writeData() {
        Thread(Runnable {
            var mUpInputFailedBean_Uped = UpInputFailedBean()
            mUpInputFailedBean_Uped.data = ArrayList<AppInputBean>()
            for (i in mUpInputFailedBean!!.data.indices) {
                if (!mUpInputFailedBean!!.data[i].isUpLoadOK)
                    mUpInputFailedBean_Uped.data.add(mUpInputFailedBean!!.data[i])
            }
            File_Cache.writeFileToSD(MyApp.gson.toJson(mUpInputFailedBean_Uped), SaveKey.FILE_DATA_NAME)
            myHandler!!.sendEmptyMessage(2)
        }).start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            var isDel = data.getBooleanExtra("isDel", false)
            if (isDel && delPosition != -1) {
                mUpInputFailedBean!!.data.removeAt(delPosition)
                delPosition = -1
                mLoadDialog!!.show()
                writeData()
            }
        }
    }


    /**
     * Handler 静态内部类，防止内存泄漏
     */
    private class MyHandler(activity: UploadFailedActivity) : Handler() {
        private val activityWeakReference: WeakReference<UploadFailedActivity> = WeakReference<UploadFailedActivity>(activity)
        private var str_msg = ""

        override fun handleMessage(msg: Message) {
            val activity = activityWeakReference.get()
            if (activity != null) {
                when (msg.what) {
                    1 -> {
                        activity.mLoadDialog!!.hide()
                        activity.initListView()
                        activity.isUpLoading = false
                    }
                    2 -> {
                        activity.initdata()
                    }
                    activity.UpLoadOK -> {
                        activity.mLoadDialog!!.setMessage("正在上传:" + msg.arg1 + "/" + msg.arg2)
                        if (msg.arg1 == msg.arg2) {
                            activity.listViewData(msg.arg2)
                            activity.mLoadDialog!!.hide()
                        }
                    }
                    activity.UpLoadFailed -> {
                        activity.mLoadDialog!!.setMessage("正在上传:" + msg.arg1 + "/" + msg.arg2)
                        if (msg.obj != null && msg.obj as String != "") str_msg += msg.obj as String + "\n"
                        if (msg.arg1 == msg.arg2) {
                            var msg_str = msg.obj as String
                            activity.listViewData(msg.arg2)
                            activity.mLoadDialog!!.hide()
                            if (str_msg != "") {
                                ToastUtil.showText(str_msg, Toast.LENGTH_LONG)
                            }

                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isUpLoading_UI = false
    }
}
