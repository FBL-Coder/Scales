package com.etsoft.scales.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.View
import android.view.animation.AnimationUtils
import com.andview.refreshview.XRefreshView
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.Ports
import com.etsoft.scales.R
import com.etsoft.scales.adapter.ListViewAdapter.ServerStationListViewAdapter
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.bean.RecordNotificationBean
import com.etsoft.scales.bean.ServerStationBean
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.httpGetDataUtils.MyHttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.etsoft.scales.utils.httpGetDataUtils.ResultDesc
import kotlinx.android.synthetic.main.activity_server_station.*
import java.util.*


/**
 * Author：FBL  Time： 2018/7/25.
 * 服务站点列表
 */
class ServerStationActivity : BaseActivity() {
    private var mListBean: ServerStationBean? = null
    private var Maxpage = 1
    private var page = 1
    private var mType = 0
    private var mServerStationListViewAdapter: ServerStationListViewAdapter? = null


    override fun setView(): Int {
        return R.layout.activity_server_station
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    /**
     * 加载服务站点列表数据
     */
    private fun initdata(key: String = "", linit: Float = 10.0F) {
        var pram = HashMap<String, String>()
        pram["limit"] = "$linit"
        pram["page"] = "$page"
        pram["type"] = "$mType"
        pram["name"] = "$key"
        OkHttpUtils.getAsyn(Ports.SERVERLIST, pram, object : MyHttpCallback(this) {

            override fun onSuccess(resultDesc: ResultDesc?) {
                mLoadDialog!!.hide()
                ServerStation_XRefreshView.stopRefresh()
                ServerStation_XRefreshView.stopLoadMore()
                try {
                    var list = MyApp.gson.fromJson(resultDesc!!.result, ServerStationBean::class.java)
                    if (list!!.code == 0) {
                        if (mListBean == null) mListBean = list else mListBean!!.data.addAll(list.data)
                        var pages = mListBean!!.count / linit
                        Maxpage = Math.ceil(pages.toDouble()).toInt()
                    } else {
                        LogUtils.e("获取数据失败:code=${list.code}  msg=${list.msg}")
                    }
                    if (ServerStation_DataView.visibility == View.GONE) {
                        ServerStation_SelectView.startAnimation(AnimationUtils.loadAnimation(this@ServerStationActivity, R.anim.abc_popup_exit))
                        ServerStation_SelectView.visibility = View.GONE
                        ServerStation_DataView.visibility = View.VISIBLE
                    }
                    initListView()
                } catch (e: Exception) {
                    LogUtils.e("获取数据异常 ：data= ${resultDesc!!.result}")
                    ToastUtil.showText("服务器异常")
                }
            }

            override fun onFailure(code: Int, message: String?) {
                super.onFailure(code, message)
                mLoadDialog!!.hide()
                ServerStation_XRefreshView.stopRefresh()
                ServerStation_XRefreshView.stopLoadMore()
                ToastUtil.showText("服务器异常")
            }
        }, "列表数据")
    }

    /**
     * 初始化ListView数据
     */
    private fun initListView() {

        ServerStation_XRefreshView.run {
            pullLoadEnable = true
            pullRefreshEnable = true
            setXRefreshViewListener(object : XRefreshView.SimpleXRefreshListener() {
                override fun onRefresh(isPullDown: Boolean) {
                    super.onRefresh(isPullDown)
                    ServerStation_XRefreshView.startRefresh()
                    ServerStation_XRefreshView.setLoadComplete(false)
                    mListBean = null
                    page = 1
                    initdata()
                }

                override fun onLoadMore(isSilence: Boolean) {
                    super.onLoadMore(isSilence)
                    if (page < Maxpage) {
                        page++
                        ServerStation_XRefreshView.mPullLoading = true
                        initdata()
                    } else {
                        ServerStation_XRefreshView.setLoadComplete(true)
                        ToastUtil.showText("没有数据了")
                    }
                }
            })
        }

        if (mServerStationListViewAdapter == null) {
            mServerStationListViewAdapter = ServerStationListViewAdapter(mListBean!!)
            ServerStation_List.adapter = mServerStationListViewAdapter
            ServerStation_List.setOnItemClickListener { parent, view, position, id ->
                startActivity(Intent(this, ServerStationInfoActivity::class.java).run {
                    putExtra("id", "${mListBean!!.data[position].id}")
                    this
                })
            }
        } else mServerStationListViewAdapter!!.notifyDataSetChanged(mListBean!!)

    }

    private fun initView() {
        ServerStation_TitleBar.run {
            back.setOnClickListener { finish() }
            title.text = "服务站点列表"
            moor.setImageResource(R.drawable.ic_search_black_24dp)
            moor.setOnClickListener {
                if (Search_View.visibility == View.VISIBLE) {
                    Search_View.visibility = View.GONE
                } else {
                    Search_View.visibility = View.VISIBLE
                    Search.setIconifiedByDefault(false)
                    Search.isSubmitButtonEnabled = true
                    Search.onActionViewExpanded()
                    SearchView_Cancle.setOnClickListener {
                        Search.setQuery("", false)
                        Search_View.visibility = View.GONE
                    }
                }
            }
        }

        ServerStation_Type.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.ServerStation_type1 -> mType = 1
                R.id.ServerStation_type2 -> mType = 2
                R.id.ServerStation_type3 -> mType = 3
                R.id.ServerStation_type4 -> mType = 4
                R.id.ServerStation_type5 -> mType = 5
            }
        }

        ServerStation_OK.setOnClickListener {
            mLoadDialog?.show()
            initdata()
        }

        Search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(queryText: String): Boolean {
                LogUtils.d("搜索提交数据 = $queryText")
                if (queryText.isEmpty()) {
                    ToastUtil.showText("请输入关键字")
                    return false
                }
                mListBean = null
                initdata(queryText)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }
}
