package com.etsoft.scales.ui.fragment.home

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.etsoft.scales.R
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.ui.activity.BaseActivity
import com.etsoft.scales.ui.activity.MainActivity
import com.etsoft.scales.ui.activity.RecordNotificationActivity
import com.etsoft.scales.utils.ToastUtil
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
        initView()
        initData()
        initEvent()
    }

    private fun initData() {
        Mine_Input_Statistics.text = "${MyApp.UserInfo!!.data.data.replenishStockTotalPayMoney}kg"
        Mine_Out_Statistics.text = "${MyApp.UserInfo!!.data.data.outboundTotalWeight}kg"
    }

    private fun initEvent() {

        Mine_Head!!.setOnClickListener {
            //头像点击
            ToastUtil.showText("点击头像")
        }
        Mine_UserName!!.text = "我是昵称"

        Mine_Notification!!.setOnClickListener {
            //历史通知
            startActivity(Intent(mActivity, RecordNotificationActivity::class.java))
        }
        Mine_DevManager!!.setOnClickListener {
            //设备管理
            ToastUtil.showText("设备管理")
        }
        Mine_Setting!!.setOnClickListener {
            //设置
            ToastUtil.showText("设置")
        }
        Mine_Logout!!.setOnClickListener {
            //退出
            System.exit(0)
        }
    }

    private fun initView() {
        Mine_TitleBar!!.back.visibility = View.GONE
        Mine_TitleBar!!.title.text = "我的"
        Mine_TitleBar!!.moor.visibility = View.GONE
    }
}
