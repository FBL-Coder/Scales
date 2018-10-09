package com.etsoft.scales.ui.activity

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.widget.Toast
import com.apkfuns.logutils.LogUtils
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.etsoft.scales.Ports
import com.etsoft.scales.R
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.app.MyApp.Companion.mLocationInfo
import com.etsoft.scales.netWorkListener.NetBroadcastReceiver
import com.etsoft.scales.ui.fragment.home.InputMainFragment
import com.etsoft.scales.ui.fragment.home.MineMainFragment
import com.etsoft.scales.ui.fragment.home.OutMainFragment
import com.etsoft.scales.utils.PermissionsUtli
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.httpGetDataUtils.MyHttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import kotlinx.android.synthetic.main.activity_main_scales.*
import java.util.*
import kotlin.collections.HashMap


/**
 * Author：FBL  Time： 2018/7/20.
 * 首页
 */
class MainActivity : BaseActivity() {

    private var mBroadcastReceiver: NetBroadcastReceiver? = null
    private var mIntentFilter: IntentFilter? = null
    private var fragments: ArrayList<Fragment>? = null
    private var fm = supportFragmentManager
    private var mInputMainFragment: InputMainFragment? = null

    override fun onResume() {
        super.onResume()
        //注册网络状态广播接受器
        mBroadcastReceiver = NetBroadcastReceiver()
        mIntentFilter = IntentFilter()
        mIntentFilter!!.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(mBroadcastReceiver, mIntentFilter)
    }

    override fun setView(): Int {
        return R.layout.activity_main_scales
    }

    override fun onCreate() {
        initView()
        fragments = initFragment()
        ViewEvent()
        PermissionsUtli.verifyStoragePermissions(this)
        UpLocation()
    }


    private fun initFragment(): ArrayList<Fragment> {
        mInputMainFragment = InputMainFragment.initFragment(this@MainActivity)
        return ArrayList<Fragment>().run {
            add(mInputMainFragment!!)
            add(OutMainFragment.initFragment(this@MainActivity))
            add(MineMainFragment.initFragment(this@MainActivity))
            this
        }
    }

    private fun ViewEvent() {
        val bt = fm.beginTransaction()
        bt.replace(R.id.Main_GroupView, fragments!![0])
        bt.commit()

        Main_BottomNavigationBar!!.setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {
                val Transaction = fm.beginTransaction()
                if (fragments != null) {
                    if (position < fragments!!.size) {

                        for (i in fragments!!.indices) {
                            Transaction.hide(fragments!![i])
                        }
                        if (fragments!![position].isAdded) {
                            Transaction.show(fragments!![position])
                        } else {
                            Transaction.add(R.id.Main_GroupView, fragments!![position])
                            Transaction.show(fragments!![position])
                        }

                        Transaction.commit()
                    }
                }
            }

            override fun onTabUnselected(position: Int) {}
            override fun onTabReselected(position: Int) {}
        })
    }

    private fun initView() {
        Main_BottomNavigationBar
                .addItem(BottomNavigationItem(R.drawable.ic_input_black_24dp, "入库"))
                .addItem(BottomNavigationItem(R.drawable.ic_zoom_out_map_black_24dp, "出库"))
                .addItem(BottomNavigationItem(R.drawable.ic_account_circle_black_24dp, "我的"))
                .setActiveColor(R.color.black)
                .initialise()

    }

    private var mTimeExit = 0L
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        try {
            if (keyCode != KeyEvent.KEYCODE_BACK)
                return false
            if (System.currentTimeMillis() - mTimeExit < 1500) {
                for (i in 0 until MyApp.mApplication!!.activities!!.size) {
                    MyApp.mApplication?.activities!![i]?.finish()
                }
                System.exit(0)
            } else {
                ToastUtil.showText("再按一次退出", Toast.LENGTH_SHORT)
                mTimeExit = System.currentTimeMillis()
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }

    override fun onDestroy() {
        //注销网络状态广播接收器
        unregisterReceiver(mBroadcastReceiver!!)
        super.onDestroy()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mInputMainFragment!!.onActivityResult(requestCode, resultCode, data)
    }


    /**
     * 上传经纬度获取附近站点
     */
    private fun UpLocation() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (mLocationInfo != null) {
                    var map = HashMap<String, String>().run {
                        put("lng", "${mLocationInfo!!.longitude}")
                        put("lat", "${mLocationInfo!!.latitude}")
                        this
                    }
                    OkHttpUtils.getAsyn(Ports.LOCATION_SERVER, map, object : MyHttpCallback(this@MainActivity) {}, "上传定位")
                    cancel()
                } else
                    LogUtils.e("经纬度数据为空")
            }
        }, 0, 5000)

    }
}
