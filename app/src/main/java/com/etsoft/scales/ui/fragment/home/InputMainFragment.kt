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
import com.etsoft.scales.Ports
import com.etsoft.scales.R
import com.etsoft.scales.adapter.GridViewAdapter.GridView_CardView_Side_Adapter
import com.etsoft.scales.adapter.ListViewAdapter.Main_Input_ListViewAdapter
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.app.MyApp.Companion.mRecycleListBean
import com.etsoft.scales.bean.CareFragment_Bean
import com.etsoft.scales.bean.Input_Main_List_Bean
import com.etsoft.scales.bean.RecycleListBean
import com.etsoft.scales.ui.activity.*
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.httpGetDataUtils.MyHttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.etsoft.scales.utils.httpGetDataUtils.ResultDesc
import com.etsoft.scales.view.MyDialog
import com.smartdevice.aidltestdemo.BaseActivity.mIzkcService
import kotlinx.android.synthetic.main.fragment_input_main.*


/**
 * Author：FBL  Time： 2018/7/20.
 * 入库
 */
class InputMainFragment : Fragment() {

    companion object {
        private var mActivity: BaseActivity? = null
        fun initFragment(activity: MainActivity): InputMainFragment {
            var mCareFragment = InputMainFragment()
            mActivity = activity
            return mCareFragment
        }
    }

    private var listid = 0
    private var mInputLiat = ArrayList<Input_Main_List_Bean>()
    private var mGridView_CardView_Adapter: GridView_CardView_Side_Adapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_input_main, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getRecycleData(0)
        initListView()
        initGridView()
    }

    /**
     * 获取回收物信息
     */
    private fun getRecycleData(type: Int) {
        OkHttpUtils.getAsyn(Ports.RECYCLELIST, object : MyHttpCallback(mActivity) {
            override fun onSuccess(resultDesc: ResultDesc?) {
                mActivity!!.mLoadDialog!!.hide()
                mRecycleListBean = MyApp.gson.fromJson(resultDesc!!.result, RecycleListBean::class.java)
                if (type == 1)
                    showSelectDialog()
            }
        }, "回收列表")
    }

    private fun initListView() {
        var adapter = Main_Input_ListViewAdapter(mInputLiat)
        Input_Main_ListView.adapter = adapter
        Input_Main_ListView.setSelection(Input_Main_ListView.bottom)
        Input_Main_ListView.setOnItemClickListener { parent, view, position, id ->
            if (position <= mInputLiat.size && position > 0) {
                ToastUtil.showText("数据点击---$position")
            } else if (position == 0) {
            } else {
                if (mRecycleListBean == null) {
                    mActivity!!.mLoadDialog!!.show()
                    getRecycleData(1)
                    return@setOnItemClickListener
                } else
                    showSelectDialog()
            }
        }

        Input_Main_ListView.setOnItemLongClickListener { _, _, position, _ ->
            if (position == 0 || position > mInputLiat.size) false
            else {
                MyDialog(mActivity!!)
                        .setMessage("是否要删除此条信息？")
                        .setNegativeButton("取消") { dialog, which ->
                            dialog.dismiss()
                        }
                        .setPositiveButton("删除") { dialog, which ->
                            mInputLiat.removeAt(position - 1)
                            initListView()
                            dialog.dismiss()
                        }.create().show()
                true
            }
        }

    }

    private fun initGridView() {
        var list = ArrayList<CareFragment_Bean>().run {
            add(CareFragment_Bean(R.mipmap.ic_favorite_white_48dp, "#8ee5ee", "电子秤1"))
            this
        }
        mGridView_CardView_Adapter = GridView_CardView_Side_Adapter(list!!)
        Main_GridView!!.adapter = mGridView_CardView_Adapter
        Main_GridView!!.setOnItemClickListener { _, _, position, _ ->
            ToastUtil.showText("设备点击")
        }
    }


    private fun initView() {
        Input_Main_TitleBar!!.back.visibility = View.GONE
        Input_Main_TitleBar!!.title.text = "入库"
        Input_Main_TitleBar!!.moor.setImageResource(R.mipmap.ic_cloud_upload_white_24dp)
        Input_Main_TitleBar!!.moor.setOnClickListener {
            MyDialog(mActivity!!)
                    .setMessage("是否上传至服务器？")
                    .setNegativeButton("取消") { dialog, which ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("上传") { dialog, which ->
                        UpToServer()
                        dialog.dismiss()
                    }.create().show()
        }

        Input_Main_Record!!.setOnClickListener {
            startActivity(Intent(mActivity, InputRecordActivity::class.java))
        }

        Main_Add_Dev!!.setOnClickListener {
            startActivity(Intent(mActivity, DevListActivity::class.java))
        }
    }

    /**
     * 上传入库数据到服务器
     */
    private fun UpToServer() {
        var Data = ArrayList<Input_Main_List_Bean>()
        Data.add(Input_Main_List_Bean().run {
            id = "编号"
            type = "类型"
            weight = "重量"
            unit = "单位"
            price = "单位"
            total = "总价"
            this
        })
        Data.addAll(mInputLiat)

        mIzkcService.printerInit()
        com.smartdevice.aidltestdemo.BaseActivity.mIzkcService.printGBKText("------------------")
        for (i in Data.indices) {
            com.smartdevice.aidltestdemo.BaseActivity.mIzkcService.sendRAWData("printer", byteArrayOf(0x0a, 0x0a, 0x1b, 0x33))
            var array = arrayOf(Data[i].id, Data[i].type, Data[i].weight, Data[i].price, Data[i].unit, Data[i].total)
            var array1 = intArrayOf(0, 2, 1, 1, 1, 2)
            var array2 = intArrayOf(1, 1, 1, 1, 1, 1)
            com.smartdevice.aidltestdemo.BaseActivity.mIzkcService.printColumnsText(array, array1, array2)
        }
        com.smartdevice.aidltestdemo.BaseActivity.mIzkcService.sendRAWData("printer", byteArrayOf(0x0a, 0x0a, 0x1b, 0x69))



//        var map = HashMap<String, String>().run {
//            put("userId", "")
//            put("servicePointId", "")
//            put("staffId", "")
//            put("weight", "")
//            put("recyclingPriceId", "")
//            this
//        }
//
//        OkHttpUtils.postAync(Ports.ADDINPUTBACK, MyApp.gson.toJson(map), object : MyHttpCallback(mActivity) {
//            override fun onSuccess(resultDesc: ResultDesc?) {
//
//            }
//
//            override fun onFailure(call: Call?, code: Int, message: String?) {
//
//            }
//        }, "入库")

    }

    /**
     * 回收物选择框
     */
    private fun showSelectDialog() {
        var names = ArrayList<String>()
        var position = 0
        if (mRecycleListBean == null) {

        }
        for (i in mRecycleListBean!!.data.indices) {
            names.add(mRecycleListBean!!.data[i].name)
        }
        MyDialog(mActivity!!)
                .setSingleChoiceItems(ArrayAdapter(mActivity, android.R.layout.simple_list_item_single_choice, names), 0, DialogInterface.OnClickListener { dialog, which ->
                    position = which
                }).setNegativeButton("确定") { dialog, which ->
                    dialog.dismiss()
                    startActivityForResult(Intent(mActivity, AddInputAvtivity::class.java).run {
                        putExtra("position", position)
                        this
                    }, Activity.RESULT_FIRST_USER)
                }.create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            var bean = data.getSerializableExtra("data") as Input_Main_List_Bean
            bean.id = listid.toString()
            mInputLiat.add(bean)
            initListView()
            listid++
        }
    }

}
