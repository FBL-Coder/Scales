package com.etsoft.scales.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.widget.Toast
import com.apkfuns.logutils.LogUtils
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.etsoft.scales.Ports
import com.etsoft.scales.R
import com.etsoft.scales.SaveKey
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.app.MyApp.Companion.mLocationInfo
import com.etsoft.scales.bean.ServerStationInfoBean
import com.etsoft.scales.ui.fragment.home.InputMainFragment
import com.etsoft.scales.ui.fragment.home.MineMainFragment
import com.etsoft.scales.ui.fragment.home.OutMainFragment
import com.etsoft.scales.utils.AppSharePreferenceMgr
import com.etsoft.scales.utils.PermissionsUtli
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.httpGetDataUtils.MyHttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.etsoft.scales.utils.httpGetDataUtils.ResultDesc
import com.etsoft.scales.view.MyDialog
import kotlinx.android.synthetic.main.activity_main_scales.*
import java.util.*
import kotlin.collections.HashMap


/**
 * Author：FBL  Time： 2018/7/20.
 * 首页
 */
class MainActivity : BaseActivity() {

    private var fragments: ArrayList<Fragment>? = null
    private var fm = supportFragmentManager
    private var mInputMainFragment: InputMainFragment? = null
    private var mOutMainFragment: OutMainFragment? = null


    override fun setView(): Int {
        return R.layout.activity_main_scales
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        fragments = initFragment()
        ViewEvent()
        PermissionsUtli.verifyStoragePermissions(this)
//        UpLocation()
    }



    private fun initFragment(): ArrayList<Fragment> {
        mInputMainFragment = InputMainFragment.initFragment(this@MainActivity)
        mOutMainFragment = OutMainFragment.initFragment(this@MainActivity)
        return ArrayList<Fragment>().run {
            add(mInputMainFragment!!)
            add(mOutMainFragment!!)
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
                .addItem(BottomNavigationItem(R.drawable.ic_system_update_alt_white_24dp, "入库"))
                .addItem(BottomNavigationItem(R.drawable.ic_present_to_all_white_24dp, "出库"))
                .addItem(BottomNavigationItem(R.drawable.ic_account_circle_black_24dp, "我的"))
                .setActiveColor(R.color.app_color)
                .initialise()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return false
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mInputMainFragment!!.onActivityResult(requestCode, resultCode, data)
        mOutMainFragment!!.onActivityResult(requestCode, resultCode, data)
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
                    OkHttpUtils.getAsyn(Ports.LOCATION_SERVER, map, object : MyHttpCallback(this@MainActivity) {
                        override fun onSuccess(resultDesc: ResultDesc?) {
                            if (resultDesc!!.getcode() != 0) {
                                LogUtils.e(resultDesc.result)
                            } else {
                                try {
                                    var StationInfo = MyApp.gson.fromJson<ServerStationInfoBean>(resultDesc!!.result, resultDesc::class.java)

                                    if (StationInfo.data.id != AppSharePreferenceMgr.get(SaveKey.SERVERSTATION_ID, -1)) {
                                        MyDialog(this@MainActivity)
                                                .setMessage("您上次绑定服务站点与这次定位服务站点不一致\n是否切换到当前定位服务站点?")
                                                .setNegativeButton("取消") { dialog, which ->
                                                    dialog.dismiss()
                                                }
                                                .setPositiveButton("切换") { dialog, which ->
                                                    AppSharePreferenceMgr.put(SaveKey.SERVERSTATION_ID, StationInfo!!.data.id)
                                                    AppSharePreferenceMgr.put(SaveKey.SERVERSTATION_NAME, StationInfo!!.data.name)
                                                }
                                    }
                                } catch (e: Exception) {
                                    LogUtils.e("根据经纬度获取站点错误=$e")
                                }
                            }
                        }

                        override fun onFailure(code: Int, message: String?) {
                            super.onFailure(code, message)
                            LogUtils.e("根据经纬度获取站点错误,code = $code , msg = $message")
                        }
                    }, "上传定位")
                    cancel()
                } else
                    LogUtils.e("经纬度数据为空")
            }
        }, 0, 5000)

    }
}
