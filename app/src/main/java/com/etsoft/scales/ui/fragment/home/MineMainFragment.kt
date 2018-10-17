package com.etsoft.scales.ui.fragment.home

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.etsoft.scales.R
import com.etsoft.scales.SaveKey
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.ui.activity.*
import com.etsoft.scales.utils.AppSharePreferenceMgr
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.view.MyDialog
import kotlinx.android.synthetic.main.fragment_mine_main.*


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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mine_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            MyDialog(mActivity!!).setMessage("是否退出登录？")
                    .setNegativeButton("取消") { dialog, which ->
                        dialog.dismiss()
                    }.setPositiveButton("退出") { dialog, which ->
                        startActivity(Intent(mActivity, LoginActivity::class.java))
                        AppSharePreferenceMgr.clear(MyApp.mApplication)
                        //退出
                        mActivity!!.finish()
                        dialog.dismiss()
                    }.create().run {
                        window.attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        this
                    }.show()
        }
    }
}
