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
import com.etsoft.scales.app.MyApp.Companion.mRecycleListBean
import com.etsoft.scales.bean.OutListBean
import com.etsoft.scales.bean.Out_Main_Bean
import com.etsoft.scales.bean.RecycleListBean
import com.etsoft.scales.receiver.BlueBoothReceiver
import com.etsoft.scales.ui.activity.AddDevActivity
import com.etsoft.scales.ui.activity.AddOutAvtivity
import com.etsoft.scales.ui.activity.MainActivity
import com.etsoft.scales.ui.activity.OutInfoActivity
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.httpGetDataUtils.MyHttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.etsoft.scales.utils.httpGetDataUtils.ResultDesc
import com.etsoft.scales.view.MyDialog
import com.etsoft.scales.view.TitleBar
import kotlinx.android.synthetic.main.fragment_out_main.*

/**
 * Author：FBL  Time： 2018/7/20.
 * 出库
 */
class OutMainFragment : Fragment() {
    private var mOutList: OutListBean? = null
    private var Maxpage = 1
    private var page = 1
    private var mType = -1
    private var mMain_Out_ListViewAdapter: Main_Out_ListViewAdapter? = null
    private var Main_Out_TitleBar: TitleBar? = null
    private var mSelectBean = ArrayList<RecycleListBean.DataBean>()
    private var mSelectName: ArrayList<String>? = null


    companion object {
        private var mActivity: MainActivity? = null
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
        mActivity!!.mLoadDialog!!.show()
        initView(view)
        BlueBoothReceiver.mOnBlueConnecetChangerlistener_out = object : BlueBoothReceiver.OnBlueConnecetChangerlistener {
            override fun OnBlueChanger(isConnect: Boolean) {
                if (isConnect)
                    Main_Out_TitleBar?.back?.setImageResource(R.drawable.ic_settings_bluetooth_blue_a200_24dp)
                else
                    Main_Out_TitleBar?.back?.setImageResource(R.drawable.ic_settings_bluetooth_black_24dp)
            }
        }
        initdata(page)
    }

    /**
     * 获取回收物信息
     */
    private fun getRecycleData() {
        OkHttpUtils.getAsyn(Ports.RECYCLELIST, object : MyHttpCallback(mActivity) {
            override fun onSuccess(resultDesc: ResultDesc?) {
                mActivity!!.mLoadDialog!!.hide()
                if (resultDesc!!.getcode() != 0) {
                    ToastUtil.showText(resultDesc.result)
                } else {
                    try {
                        mRecycleListBean = MyApp.gson.fromJson(resultDesc!!.result, RecycleListBean::class.java)
                        showTypeDialog()
                    } catch (e: Exception) {
                        LogUtils.e("获取数据异常 ：data= ${resultDesc!!.result}")
                        ToastUtil.showText("服务器异常")
                    }
                }
            }

            override fun onFailure(code: Int, message: String?) {
                super.onFailure(code, message)
                ToastUtil.showText("服务器异常")
            }
        }, "回收列表")
    }


    /**
     * 回收物选择框
     */
    private fun showSelectDialog() {
        if (mRecycleListBean == null) {
            mActivity!!.mLoadDialog!!.show()
            getRecycleData()
            return
        }
        var position = 0
        if (mType == -1) {
            showTypeDialog()
        } else {
            mSelectName = ArrayList()
            for (i in mRecycleListBean!!.data) {
                if (i.type == mType) {
                    mSelectBean.add(i)
                    mSelectName!!.add(i.name + "     ￥" + i.price)
                }
            }

            MyDialog(mActivity!!).setTitle("选择回收物")
                    .setSingleChoiceItems(ArrayAdapter(mActivity, android.R.layout.simple_list_item_single_choice, mSelectName), 0, DialogInterface.OnClickListener { dialog, which ->
                        position = which
                    }).setPositiveButton("确定") { dialog, which ->
                        dialog.dismiss()
                        if (mSelectName?.size == 0) {
                            ToastUtil.showText("该类型没有对应回收物，请重新选择")
                            return@setPositiveButton
                        }
                        //跳转到具体添加回收物页面
                        startActivityForResult(Intent(mActivity, AddOutAvtivity::class.java).run {
                            putExtra("data", mSelectBean[position])
                            putExtra("type", mType)
                            this
                        }, Activity.RESULT_FIRST_USER)
                    }.setNegativeButton("取消") { dialog, which ->
                        dialog.dismiss()
                    }.create().run {
                        window.attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        this
                    }.show()
        }
    }

    /**
     * 加载出库数据
     */
    private fun initdata(page: Int = 1, type: String = SortType.CREATETIME, linit: Float = 10.0F) {

        var pram = HashMap<String, String>()
        pram[type] = "DESC"//DESC
        pram["limit"] = "$linit"
        pram["page"] = "$page"
        OkHttpUtils.getAsyn(Ports.OUTBACKLIST, pram, object : MyHttpCallback(mActivity) {

            override fun onSuccess(resultDesc: ResultDesc?) {
                mActivity!!.mLoadDialog!!.hide()
                try {
                    if (resultDesc!!.getcode() != 0) {
                        ToastUtil.showText(resultDesc.result)
                    } else {
                        Out_XRefreshView.stopRefresh()
                        Out_XRefreshView.stopLoadMore()
                        var list = MyApp.gson.fromJson(resultDesc!!.result, OutListBean::class.java)
                        if (mOutList == null) mOutList = list else mOutList?.data?.addAll(list.data)
                        val pages = list!!.count / linit
                        this@OutMainFragment.Maxpage = Math.ceil(pages.toDouble()).toInt()
                        initListView()
                    }
                } catch (e: Exception) {
                    LogUtils.e("获取数据异常 ：data= ${resultDesc!!.result}")
                    ToastUtil.showText("服务器异常")
                }
            }

            override fun onFailure(code: Int, message: String?) {
                super.onFailure(code, message)
                mActivity!!.mLoadDialog!!.hide()
                Out_XRefreshView.stopRefresh()
                Out_XRefreshView.stopLoadMore()
                ToastUtil.showText("服务器异常")
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
    private fun initView(view: View) {

        Main_Out_TitleBar = view.findViewById(R.id.Main_Out_TitleBar) as TitleBar
        Main_Out_TitleBar?.run {
            if (MyApp.mBluetoothDataIsEnable)
                back.setImageResource(R.drawable.ic_settings_bluetooth_blue_a200_24dp)
            else
                back.setImageResource(R.drawable.ic_settings_bluetooth_black_24dp)
            back.setOnClickListener {
                startActivity(Intent(mActivity, AddDevActivity::class.java))
            }
            title.text = "出库记录"
            moor.setImageResource(R.drawable.ic_add_circle_outline_black_24dp)
            moor.setOnClickListener {
                mType = -1
                mSelectBean.clear()
                mSelectName = null
                showTypeDialog()
            }
            this
        }
    }

    /**
     * 显示首选回收物类型选择框
     */
    private fun showTypeDialog() {
        if (mType == -1) {
            MyDialog(mActivity!!).setTitle("选择回收物类型")
                    .setSingleChoiceItems(ArrayAdapter(mActivity, android.R.layout.simple_list_item_single_choice, arrayOf("再生资源", "电子废弃物")), 0, DialogInterface.OnClickListener { dialog, which ->
                        mType = which + 1
                    }).setPositiveButton("确定") { dialog, which ->
                        dialog.dismiss()
                        if (mType == -1)
                            mType = 1
                        showSelectDialog()
                    }.setNegativeButton("取消") { dialog, which ->
                        dialog.dismiss()
                    }.create().run {
                        window.attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        this
                    }.show()
        } else
            showSelectDialog()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 100 && data != null) {
            var bean = data.getSerializableExtra("data") as Out_Main_Bean
            mActivity!!.mLoadDialog!!.show()
            OkHttpUtils.postAsyn(Ports.ADDOUTBACK, MyApp.gson.toJson(bean), object : MyHttpCallback(mActivity) {

                override fun onSuccess(resultDesc: ResultDesc?) {
                    mActivity!!.mLoadDialog!!.hide()
                    try {
                        if (resultDesc!!.getcode() != 0) {
                            ToastUtil.showText(resultDesc.result)
                        } else {
                            ToastUtil.showText("新增成功")
                        }
                    } catch (e: Exception) {
                        LogUtils.e("获取数据异常 ：data= ${resultDesc!!.result}")
                        ToastUtil.showText("服务器异常")
                    }
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    mActivity!!.mLoadDialog!!.hide()
                    ToastUtil.showText("服务器异常")
                }
            }, "新增出库")

        }
    }
}
