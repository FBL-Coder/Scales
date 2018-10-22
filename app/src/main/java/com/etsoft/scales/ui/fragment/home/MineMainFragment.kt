package com.etsoft.scales.ui.fragment.home

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.R
import com.etsoft.scales.SaveKey
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.bean.UpInputFailedBean
import com.etsoft.scales.ui.activity.*
import com.etsoft.scales.utils.AppSharePreferenceMgr
import com.etsoft.scales.utils.File_Cache
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.view.MyDialog
import com.etsoft.scales.view.ProgressBarDialog
import kotlinx.android.synthetic.main.fragment_mine_main.*
import java.lang.ref.WeakReference


/**
 * Author：FBL  Time： 2018/7/20.
 * 我的Fragment
 */
class MineMainFragment : Fragment() {

    companion object {
        private var mActivity: BaseActivity? = null
        fun initFragment(activity: MainActivity): MineMainFragment {
            var mMineFragment = MineMainFragment()
            mActivity = activity
            return mMineFragment
        }
    }

    var mLoadDialog: ProgressBarDialog? = null
    private var mHandler: MyHandler? = null
    var mUpInputFailedBean: UpInputFailedBean? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mine_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mHandler = MyHandler(this)
        mLoadDialog = mActivity!!.mLoadDialog!!
        initData()
        initEvent()
    }

    override fun onStart() {
        super.onStart()
        if (AppSharePreferenceMgr.get(SaveKey.SERVERSTATION_NAME, "") == "") {
            ServerStationName.setTextColor(Color.RED)
            ServerStationName.text = "未选择"
        } else {
            ServerStationName.setTextColor(Color.BLACK)
            ServerStationName.text = AppSharePreferenceMgr.get(SaveKey.SERVERSTATION_NAME, "") as String
        }
    }

    private fun initData() {
        Mine_Input_Statistics.text = "${MyApp.UserInfo?.data?.data?.replenishStockTotalPayMoney}kg"
        Mine_Out_Statistics.text = "${MyApp.UserInfo?.data?.data?.outboundTotalWeight}kg"
        Mine_UserName.text = MyApp.UserInfo?.data?.name
    }

    private fun initEvent() {

        Mine_UserSelf!!.setOnClickListener {
            //个人信息
            startActivity(Intent(mActivity!!, SelfActivity::class.java))
        }

        Mine_Notification!!.setOnClickListener {
            //历史通知
            startActivity(Intent(mActivity, RecordNotificationActivity::class.java))
        }

        Mine_Server.setOnClickListener {
            //服务站点
            startActivity(Intent(mActivity, ServerStationActivity::class.java))
        }
        RegisterUser!!.setOnClickListener {
            //注册用户
            startActivity(Intent(mActivity, RegisterUserActivity::class.java))
        }
        User_Query!!.setOnClickListener {
            //客户查询
            startActivity(Intent(mActivity, UserQueryActivity::class.java))
        }
        Mine_Logout!!.setOnClickListener {
            mActivity!!.mLoadDialog!!.show()
            getUpLoadFailed()

        }
    }

    private fun getUpLoadFailed() {
        Thread(Runnable {
            var data = File_Cache.readFile(SaveKey.FILE_DATA_NAME)
            LogUtils.i("未上传成功json数据 = $data")
            mUpInputFailedBean = MyApp.gson.fromJson(data, UpInputFailedBean::class.java)
            mHandler!!.sendEmptyMessage(1)
        }).start()
    }
}

/**
 * Handler 静态内部类，防止内存泄漏
 */
private class MyHandler(activity: MineMainFragment) : Handler() {
    private val activityWeakReference: WeakReference<MineMainFragment> = WeakReference<MineMainFragment>(activity)

    override fun handleMessage(msg: Message) {
        val activity = activityWeakReference.get()
        if (activity != null) {
            when (msg.what) {
                1 -> {
                    activity.mLoadDialog!!.hide()
                    if (activity.mUpInputFailedBean!!.data.size > 0) {
                        ToastUtil.showText("用户还有未上传记录，请先上传")
                        return
                    }
                    MyDialog(activity.activity!!).setMessage("是否退出登录")
                            .setNegativeButton("取消") { dialog, which ->
                                dialog.dismiss()
                            }.setPositiveButton("退出") { dialog, which ->
                                activity.activity!!.startActivity(Intent(activity.activity, LoginActivity::class.java))
                                AppSharePreferenceMgr.clear(MyApp.mApplication)
                                //退出
                                activity.activity!!.finish()
                                dialog.dismiss()
                            }.create().run {
                                window.attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or
                                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                this
                            }.show()
                }
            }
        }
    }
}
