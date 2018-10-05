package com.etsoft.scales.ui.fragment.home

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andview.refreshview.XRefreshView
import com.etsoft.scales.Ports
import com.etsoft.scales.R
import com.etsoft.scales.SortType
import com.etsoft.scales.adapter.ListViewAdapter.Main_Out_ListViewAdapter
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.bean.OutListBean
import com.etsoft.scales.ui.activity.AddOutAvtivity
import com.etsoft.scales.ui.activity.BaseActivity
import com.etsoft.scales.ui.activity.MainActivity
import com.etsoft.scales.ui.activity.OutInfoActivity
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.httpGetDataUtils.MyHttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.etsoft.scales.utils.httpGetDataUtils.ResultDesc
import kotlinx.android.synthetic.main.fragment_out_main.*
import okhttp3.Call

/**
 * Author：FBL  Time： 2018/7/20.
 * 出库
 */
class OutMainFragment : Fragment() {
    private var mOutList: OutListBean? = null
    private var Maxpage = 1
    private var page = 1

    companion object {
        private var mActivity: BaseActivity? = null
        fun initFragment(activity: MainActivity): OutMainFragment {
            var mOutMainFragment = OutMainFragment()
            mActivity = activity
            return mOutMainFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_out_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        mActivity!!.mLoadDialog!!.show()
        initdata(page)
    }

    /**
     * 加载出库数据
     */
    private fun initdata(page: Int = 1, type: String = SortType.CREATETIME, linit: Int = 2) {

        var pram = HashMap<String, String>()
        pram[type] = "ASC"//DESC
        pram["limit"] = "$linit"
        pram["page"] = "$page"
        OkHttpUtils.getAsyn(Ports.OUTBACKLIST, pram, object : MyHttpCallback(mActivity) {

            override fun onSuccess(resultDesc: ResultDesc?) {
                mActivity!!.mLoadDialog!!.hide()
                Out_XRefreshView.stopRefresh()
                Out_XRefreshView.stopLoadMore()
                var list = MyApp.gson.fromJson(resultDesc!!.result, OutListBean::class.java)
                if (list!!.code == 0) {
                    if (mOutList == null) mOutList = list else mOutList!!.data.addAll(list.data)
                    var pages = list!!.count / linit
                    this@OutMainFragment.Maxpage = Math.ceil(pages.toDouble()).toInt()
                } else {
                    ToastUtil.showText(list.msg)
                }
                initListView()
            }

            override fun onFailure(call: Call, code: Int, message: String?) {
                mActivity!!.mLoadDialog!!.hide()
                Out_XRefreshView.stopRefresh()
                Out_XRefreshView.stopLoadMore()
                ToastUtil.showText(message)
            }
        }, "出库数据")
    }

    /**
     * 初始化ListView数据
     */
    private fun initListView() {

        Out_XRefreshView.run {
            pullLoadEnable = true
            pullRefreshEnable = true
            setXRefreshViewListener(object : XRefreshView.SimpleXRefreshListener() {
                override fun onRefresh(isPullDown: Boolean) {
                    super.onRefresh(isPullDown)
                    Out_XRefreshView.startRefresh()
                    page = 1
                    mOutList = null
                    initdata()
                }

                override fun onLoadMore(isSilence: Boolean) {
                    super.onLoadMore(isSilence)
                    if (page <= Maxpage) {
                        page++
                        Out_XRefreshView.mPullLoading = true
                        initdata(page)
                    } else {
                        Out_XRefreshView.setLoadComplete(true)
                        ToastUtil.showText("没有数据了")
                    }
                }
            })
        }

        Out_Record_ListView.adapter = Main_Out_ListViewAdapter(mActivity!!,mOutList!!)

        Out_Record_ListView.setOnItemClickListener { parent, view, position, id ->
            startActivity(Intent(mActivity, OutInfoActivity::class.java).run {
                putExtra("content", mOutList!!.data[position]);this
            })
        }
    }


    /**
     * 初始化TitleBar
     */
    private fun initView() {
        Out_Record_TitleBar!!.run {
            back.visibility = View.GONE
            title.text = "出库记录"
            moor.setImageResource(R.mipmap.ic_add_white_24dp)
            moor.setOnClickListener {
                startActivity(Intent(mActivity!!, AddOutAvtivity::class.java))
            }
        }
    }
}
