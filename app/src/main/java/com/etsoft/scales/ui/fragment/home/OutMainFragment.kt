package com.etsoft.scales.ui.fragment.home

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.andview.refreshview.XRefreshView
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.Ports
import com.etsoft.scales.R
import com.etsoft.scales.SortType
import com.etsoft.scales.adapter.ListViewAdapter.Main_Out_ListViewAdapter
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.bean.Input_Main_List_Bean
import com.etsoft.scales.bean.OutListBean
import com.etsoft.scales.bean.Out_Main_Bean
import com.etsoft.scales.bean.RecycleListBean
import com.etsoft.scales.ui.activity.*
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.httpGetDataUtils.MyHttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.etsoft.scales.utils.httpGetDataUtils.ResultDesc
import com.etsoft.scales.view.MyDialog
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
    private var mMain_Out_ListViewAdapter: Main_Out_ListViewAdapter? = null

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
     * 获取回收物信息
     */
    private fun getRecycleData(type: Int) {
        OkHttpUtils.getAsyn(Ports.RECYCLELIST, object : MyHttpCallback(mActivity) {
            override fun onSuccess(resultDesc: ResultDesc?) {
                mActivity!!.mLoadDialog!!.hide()
                MyApp.mRecycleListBean = MyApp.gson.fromJson(resultDesc!!.result, RecycleListBean::class.java)
                if (MyApp.mRecycleListBean!!.code == 0 && MyApp.mRecycleListBean!!.data != null)
                    if (type == 1)
                        showSelectDialog()
            }

            override fun onFailure(code: Int, message: String?) {
                ToastUtil.showText(message)
            }
        }, "回收列表")
    }


    /**
     * 回收物选择框
     */
    private fun showSelectDialog() {
        var names = ArrayList<String>()
        var position = 0
        if (MyApp.mRecycleListBean == null) {
            ToastUtil.showText("数据获取失败,请稍后再试!")
            mActivity!!.mLoadDialog!!.show()
            getRecycleData(1)
        }
        if (MyApp.mRecycleListBean!!.code == 0 && MyApp.mRecycleListBean!!.data == null) {
            ToastUtil.showText("暂无了回收物")
            return
        }
        for (i in MyApp.mRecycleListBean!!.data.indices) {
            names.add(MyApp.mRecycleListBean!!.data[i].name)
        }
        MyDialog(mActivity!!)
                .setSingleChoiceItems(ArrayAdapter(mActivity, android.R.layout.simple_list_item_single_choice, names), 0, DialogInterface.OnClickListener { dialog, which ->
                    position = which
                }).setPositiveButton("确定") { dialog, which ->
                    dialog.dismiss()
                    startActivityForResult(Intent(mActivity, AddOutAvtivity::class.java).run {
                        putExtra("position", position)
                        this
                    }, Activity.RESULT_FIRST_USER)
                }.setNegativeButton("取消") { dialog, which ->
                    dialog.dismiss()
                }.create().show()
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
                if (mOutList == null) mOutList = list else mOutList!!.data.addAll(list.data)
                val pages = list!!.count / linit
                this@OutMainFragment.Maxpage = Math.ceil(pages.toDouble()).toInt()
                initListView()
            }

            override fun onFailure(code: Int, message: String?) {
                mActivity!!.mLoadDialog!!.hide()
                Out_XRefreshView.stopRefresh()
                Out_XRefreshView.stopLoadMore()
                LogUtils.e("获取数据失败:code=$code  msg=$message")
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
                    Out_XRefreshView.setLoadComplete(false)
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

        if (mMain_Out_ListViewAdapter == null) {
            mMain_Out_ListViewAdapter = Main_Out_ListViewAdapter(mActivity!!, mOutList!!)
            Out_Record_ListView.adapter = mMain_Out_ListViewAdapter
            Out_Record_ListView.setOnItemClickListener { parent, view, position, id ->
                startActivity(Intent(mActivity, OutInfoActivity::class.java).run {
                    putExtra("content", mOutList!!.data[position]);this
                })
            }
        } else mMain_Out_ListViewAdapter!!.notifyDataSetChanged(mOutList!!)
    }

    /**
     * 初始化TitleBar
     */
    private fun initView() {
        Main_Out_TitleBar.back.setImageResource(R.drawable.ic_settings_bluetooth_black_24dp)
        Main_Out_TitleBar.back.setOnClickListener {
            startActivity(Intent(mActivity, AddDevActivity::class.java))
        }
        Main_Out_TitleBar.title.text = "出库记录"
        Main_Out_TitleBar.moor.setImageResource(R.drawable.ic_add_circle_outline_black_24dp)
        Main_Out_TitleBar.moor.setOnClickListener {
            showSelectDialog()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 100 && data != null) {
            var bean = data.getSerializableExtra("data") as Out_Main_Bean
            mActivity!!.mLoadDialog!!.show()
            OkHttpUtils.postAsyn(Ports.ADDOUTBACK, MyApp.gson.toJson(bean), object : MyHttpCallback(mActivity) {

                override fun onSuccess(resultDesc: ResultDesc?) {
                    mActivity!!.mLoadDialog!!.hide()
                    ToastUtil.showText("新增成功")
                }

                override fun onFailure(code: Int, message: String?) {
                    mActivity!!.mLoadDialog!!.hide()
                    LogUtils.e("新增出库失败  code = $code, msg = $message")
                    ToastUtil.showText(message)
                }
            }, "新增出库")

        }
    }

}
