package com.etsoft.scales.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.andview.refreshview.XRefreshView
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.Ports
import com.etsoft.scales.R
import com.etsoft.scales.adapter.ListViewAdapter.InputRecordListViewAdapter
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.bean.InputRecordListBean
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.httpGetDataUtils.MyHttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.etsoft.scales.utils.httpGetDataUtils.ResultDesc
import kotlinx.android.synthetic.main.activity_input_record.*
import okhttp3.Call
import java.util.*

/**
 * Author：FBL  Time： 2018/7/26.
 * 入库记录
 */

class InputRecordActivity : BaseActivity() {

    private var mListBean: InputRecordListBean? = null
    private var Maxpage = 1
    private var page = 1
    private var mInputRecordListViewAdapter: InputRecordListViewAdapter? = null

    override fun setView(): Int {
        return R.layout.activity_input_record
    }

    override fun onCreate() {
        initView()
        mLoadDialog!!.show()
        initdata()
    }

    /**
     * 加载入库数据
     */
    private fun initdata(page: Int = 1, linit: Int = 2) {
        var pram = HashMap<String, String>()
        pram["limit"] = "$linit"
        pram["page"] = "$page"
        OkHttpUtils.getAsyn(Ports.INPUTBACKLIST, pram, object : MyHttpCallback(this) {

            override fun onSuccess(resultDesc: ResultDesc?) {
                mLoadDialog!!.hide()
                if (resultDesc!!.getcode() != 0) {
                    ToastUtil.showText(resultDesc.result)
                } else {
                    InputRecord_XRefreshView.stopRefresh()
                    InputRecord_XRefreshView.stopLoadMore()
                    var list = MyApp.gson.fromJson(resultDesc!!.result, InputRecordListBean::class.java)
                    if (list!!.code == 0) {
                        if (mListBean == null) mListBean = list else mListBean?.data?.addAll(list.data)
                        var pages = mListBean!!.count / linit
                        Maxpage = Math.ceil(pages.toDouble()).toInt()
                    } else {
                        LogUtils.e("获取数据失败:" + "code=" + list.code + "  msg=" + list.msg)
                    }
                    initListView()
                }
            }

            override fun onFailure(code: Int, message: String?) {
                super.onFailure(code, message)
                mLoadDialog!!.hide()
                InputRecord_XRefreshView.stopRefresh()
                InputRecord_XRefreshView.stopLoadMore()
                LogUtils.e("获取数据失败:code=$code  msg=$message")
                ToastUtil.showText(message)
            }
        }, "入库列表")
    }

    /**
     * 初始化ListView数据
     */
    private fun initListView() {

        InputRecord_XRefreshView.run {
            pullLoadEnable = true
            pullRefreshEnable = true
            setXRefreshViewListener(object : XRefreshView.SimpleXRefreshListener() {
                override fun onRefresh(isPullDown: Boolean) {
                    super.onRefresh(isPullDown)
                    InputRecord_XRefreshView.startRefresh()
                    mListBean = null
                    page = 1
                    InputRecord_XRefreshView.setLoadComplete(false)
                    initdata()
                }

                override fun onLoadMore(isSilence: Boolean) {
                    super.onLoadMore(isSilence)
                    if (page < Maxpage) {
                        page++
                        InputRecord_XRefreshView.mPullLoading = true
                        initdata(page)
                    } else {
                        InputRecord_XRefreshView.setLoadComplete(true)
                        ToastUtil.showText("没有数据了")
                    }
                }
            })
        }


        if (mInputRecordListViewAdapter == null) {
            mInputRecordListViewAdapter = InputRecordListViewAdapter(mListBean!!)
            InputRecord_ListView.adapter = mInputRecordListViewAdapter
            InputRecord_ListView.setOnItemClickListener { parent, view, position, id ->
                startActivity(Intent(this, InputInfoActivity::class.java).run {
                    putExtra("content", mListBean!!.data[position])
                    this
                })
            }
        } else mInputRecordListViewAdapter!!.notifyDataSetChanged(mListBean!!)
    }

    private fun initView() {
        InputRecord_Titlebar.run {
            title.text = "入库记录"
            moor.setImageResource(R.drawable.ic_add_circle_outline_black_24dp)
            moor.visibility = View.INVISIBLE
            back.setOnClickListener {
                finish()
            }
        }
    }
}
