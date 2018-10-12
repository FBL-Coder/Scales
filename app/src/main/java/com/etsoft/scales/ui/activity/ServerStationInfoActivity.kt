package com.etsoft.scales.ui.activity

import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.Ports
import com.etsoft.scales.R
import com.etsoft.scales.SaveKey.Companion.SERVERSTATION_ID
import com.etsoft.scales.SaveKey.Companion.SERVERSTATION_NAME
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.bean.ServerStationInfoBean
import com.etsoft.scales.utils.AppSharePreferenceMgr
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.httpGetDataUtils.MyHttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.etsoft.scales.utils.httpGetDataUtils.ResultDesc
import com.etsoft.scales.view.MyDialog
import kotlinx.android.synthetic.main.activity_serverstation_info.*

/**
 * Author：FBL  Time： 2018/7/25.
 * 服务站点详情
 */
class ServerStationInfoActivity : BaseActivity() {

    private var id = "0"
    private var mInfoBean: ServerStationInfoBean? = null


    override fun setView(): Int {
        return R.layout.activity_serverstation_info
    }

    override fun onCreate() {
        initView()
        initData()
    }

    /**
     * 站点详情
     */
    private fun initData() {
        mLoadDialog!!.show()
        id = intent.getStringExtra("id")!!

        OkHttpUtils.getAsyn(Ports.SERVERLISTINFO, HashMap<String, String>().run { put("id", id);this }, object : MyHttpCallback(this) {
            override fun onSuccess(resultDesc: ResultDesc?) {
                mLoadDialog!!.hide()
                try {
                    if (resultDesc!!.getcode() != 0) {
                        ToastUtil.showText(resultDesc.result)
                    } else {

                        mInfoBean = MyApp.gson.fromJson(resultDesc!!.result, ServerStationInfoBean::class.java)
                        Name.text = mInfoBean!!.data.name
                        Admin.text = mInfoBean!!.data.functionary
                        Phone.text = mInfoBean!!.data.functionary_phone
                        companie.text = mInfoBean!!.data.companie
                        Time.text = mInfoBean!!.data.update_time
                        address.text = mInfoBean!!.data.address
                    }
                } catch (e: Exception) {
                    LogUtils.e("获取数据异常 ：data= ${resultDesc!!.result}")
                    ToastUtil.showText("服务器异常")
                }
            }

            override fun onFailure(code: Int, message: String?) {
                super.onFailure(code, message)
                mLoadDialog!!.hide()
                ToastUtil.showText(message)
            }
        }, "站点详情")
    }

    private fun initView() {
        ServerTation_Info_TitleBar.run {
            title.text = "站点详情"
            back.setOnClickListener { finish() }
            moor.setImageResource(R.drawable.ic_note_add_black_24dp)
            moor.setOnClickListener {
                if (mInfoBean == null) {
                    ToastUtil.showText("站点信息加载失败,请稍后再试")
                    return@setOnClickListener
                }
                if (AppSharePreferenceMgr.get(SERVERSTATION_ID, -1) != -1) {
                    MyDialog(this@ServerStationInfoActivity)
                            .setMessage("您已选择服务站!\n是否重新选择此服务站?")
                            .setNegativeButton("取消") { dialog, which ->
                                dialog.dismiss()
                            }.setPositiveButton("是的") { dialog, which ->
                                AppSharePreferenceMgr.put(SERVERSTATION_ID, mInfoBean!!.data.id)
                                AppSharePreferenceMgr.put(SERVERSTATION_NAME, mInfoBean!!.data.name)
                                ToastUtil.showText("绑定成功")
                                dialog.dismiss()
                            }.show()
                } else {
                    MyDialog(this@ServerStationInfoActivity)
                            .setMessage("是否选择此服务站为工作服务站?")
                            .setNegativeButton("取消") { dialog, which ->
                                dialog.dismiss()
                            }.setPositiveButton("是的") { dialog, which ->
                                AppSharePreferenceMgr.put(SERVERSTATION_ID, mInfoBean!!.data.id)
                                AppSharePreferenceMgr.put(SERVERSTATION_NAME, mInfoBean!!.data.name)
                                ToastUtil.showText("绑定成功")
                                dialog.dismiss()
                            }.show()
                }
            }
        }
    }
}
