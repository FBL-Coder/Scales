package com.etsoft.scales.ui.activity

import android.content.Intent
import android.os.Bundle
import com.andview.refreshview.XRefreshView
import com.etsoft.scales.Ports
import com.etsoft.scales.R
import com.etsoft.scales.adapter.ListViewAdapter.RecordNotificationListViewAdapter
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.bean.RecordNotificationBean
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.httpGetDataUtils.MyHttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.etsoft.scales.utils.httpGetDataUtils.ResultDesc
import kotlinx.android.synthetic.main.activity_record_notification.*
import okhttp3.Call
import java.util.*


/**
 * Author：FBL  Time： 2018/7/25.
 * 历史通知
 */
class RecordNotificationActivity : BaseActivity() {
    private var mListBean: RecordNotificationBean? = null
    private var Maxpage = 1
    private var page = 1

    override fun setView(): Int {
        return R.layout.activity_record_notification
    }

    override fun onCreate() {
        initView()
        mLoadDialog!!.show()
        initdata()
    }

    /**
     * 加载通知列表数据
     */
    private fun initdata(page: Int = 1, linit: Int = 2) {
        var pram = HashMap<String, String>()
        pram["limit"] = "$linit"
        pram["page"] = "$page"
        OkHttpUtils.getAsyn(Ports.NOTIFICATIONLIST, pram, object : MyHttpCallback(this) {

            override fun onSuccess(resultDesc: ResultDesc?) {
                mLoadDialog!!.hide()
                Notification_Record_XRefreshView.stopRefresh()
                Notification_Record_XRefreshView.stopLoadMore()
                var list = MyApp.gson.fromJson(resultDesc!!.result, RecordNotificationBean::class.java)
                if (list!!.code == 0) {
                    if (mListBean == null) mListBean = list else mListBean!!.data.addAll(list.data)
                    var pages = mListBean!!.count / linit
                    Maxpage = Math.ceil(pages.toDouble()).toInt()
                } else {
                    ToastUtil.showText(list.msg)
                }
                initListView()
            }

            override fun onFailure(call: Call, code: Int, message: String?) {
                mLoadDialog!!.hide()
                Notification_Record_XRefreshView.stopRefresh()
                Notification_Record_XRefreshView.stopLoadMore()
                ToastUtil.showText(message)
            }
        },"列表数据")
    }

    /**
     * 初始化ListView数据
     */
    private fun initListView() {

        Notification_Record_XRefreshView.run {
            pullLoadEnable = true
            pullRefreshEnable = true
            setXRefreshViewListener(object : XRefreshView.SimpleXRefreshListener() {
                override fun onRefresh(isPullDown: Boolean) {
                    super.onRefresh(isPullDown)
                    Notification_Record_XRefreshView.startRefresh()
                    mListBean = null
                    page = 1
                    Notification_Record_XRefreshView.setLoadComplete(false)
                    initdata()
                }

                override fun onLoadMore(isSilence: Boolean) {
                    super.onLoadMore(isSilence)
                    if (page < Maxpage) {
                        page++
                        Notification_Record_XRefreshView.mPullLoading = true
                        initdata(page)
                    } else {
                        Notification_Record_XRefreshView.setLoadComplete(true)
                        ToastUtil.showText("没有数据了")
                    }
                }
            })
        }

        Notification_Record_List.adapter = RecordNotificationListViewAdapter(mListBean!!)

        Notification_Record_List.setOnItemClickListener { parent, view, position, id ->
            startActivity(Intent(this@RecordNotificationActivity, NotificationInfoActivity::class.java).run {
                putExtra("id", "${mListBean!!.data[position].id}")
                this
            })
        }
    }

    private fun initView() {
        Notification_Record_TitleBar.run {
            back.setOnClickListener { finish() }
            title.text = "历史通知"
            moor.setImageResource(R.mipmap.ic_add_white_24dp).run {
                setOnClickListener {
                    ToastUtil.showText("点击添加")
                }
            }
        }
    }
}
