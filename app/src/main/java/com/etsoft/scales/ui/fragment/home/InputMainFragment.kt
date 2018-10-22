package com.etsoft.scales.ui.fragment.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.Ports
import com.etsoft.scales.R
import com.etsoft.scales.SaveKey
import com.etsoft.scales.SaveKey.Companion.FILE_DATA_NAME
import com.etsoft.scales.adapter.ListViewAdapter.Main_Input_ListViewAdapter
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.app.MyApp.Companion.mRecycleListBean
import com.etsoft.scales.app.MyApp.Companion.mRecycleListBean_Type_1
import com.etsoft.scales.app.MyApp.Companion.mRecycleListBean_Type_2
import com.etsoft.scales.app.MyApp.Companion.mRecycleListBean_Type_3
import com.etsoft.scales.bean.AppInputBean
import com.etsoft.scales.bean.Input_Main_List_Bean
import com.etsoft.scales.bean.RecycleListBean
import com.etsoft.scales.bean.UpInputFailedBean
import com.etsoft.scales.netWorkListener.AppNetworkMgr
import com.etsoft.scales.receiver.BlueBoothReceiver
import com.etsoft.scales.receiver.BlueBoothReceiver.OnBlueConnecetChangerlistener
import com.etsoft.scales.ui.activity.*
import com.etsoft.scales.utils.AppSharePreferenceMgr
import com.etsoft.scales.utils.File_Cache
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.httpGetDataUtils.MyHttpCallback
import com.etsoft.scales.utils.httpGetDataUtils.OkHttpUtils
import com.etsoft.scales.utils.httpGetDataUtils.ResultDesc
import com.etsoft.scales.view.MyDialog
import com.etsoft.scales.view.ProgressBarDialog
import com.smartdevice.aidltestdemo.BaseActivity.mIzkcService
import kotlinx.android.synthetic.main.fragment_input_main.*
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * Author：FBL  Time： 2018/7/20.
 * 入库
 */
class InputMainFragment : Fragment() {

    companion object {
        var FileHandData = 111
        var mActivity: MainActivity? = null
        fun initFragment(activity: MainActivity): InputMainFragment {
            var mCareFragment = InputMainFragment()
            mActivity = activity
            return mCareFragment
        }
    }

    private var listid = 0

    private var mInputLiat = ArrayList<Input_Main_List_Bean>()
    private var mMain_Input_ListViewAdapter: Main_Input_ListViewAdapter? = null
    private var mHandler: MyHandler? = null
    private var mLoadDialog: ProgressBarDialog? = null
    private var mActivity_: MainActivity? = null

    /**
     * 首选回收物类型
     */
    private var mType = -1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_input_main, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLoadDialog = mActivity!!.mLoadDialog!!
        mActivity_ = mActivity
        mHandler = MyHandler(this)
        File_Cache.setHandler(mHandler)
        getRecycleData(0)
        initView()
        initListView()
        BlueBoothReceiver.mOnBlueConnecetChangerlistener_input = object : OnBlueConnecetChangerlistener {
            override fun OnBlueChanger(isConnect: Boolean) {
                if (isConnect)
                    Input_Main_Back.setImageResource(R.drawable.ic_settings_bluetooth_blue_a200_24dp)
                else
                    Input_Main_Back.setImageResource(R.drawable.ic_settings_bluetooth_black_24dp)
            }
        }
    }

    override fun onStart() {
        Input_Main_Warn.visibility = View.GONE
        getFile()
        super.onStart()
    }

    /**
     * 读取本地未上传数据
     */
    private fun getFile() {
        Thread(Runnable {
            var data = File_Cache.readFile(SaveKey.FILE_DATA_NAME)
            if (data != "") {
                var failedBean = MyApp.gson.fromJson(data, UpInputFailedBean::class.java)
                if (failedBean.data != null) {
                    if (failedBean.data.size > 0) {
                        mHandler!!.sendEmptyMessage(FileHandData)
                    }
                }
            }
        }).start()
    }

    /**
     * 获取回收物信息
     */
    private fun getRecycleData(type: Int) {

        var map = HashMap<String, String>()
        map["limit"] = "1000"
        OkHttpUtils.getAsyn(Ports.RECYCLELIST, map, object : MyHttpCallback(mActivity) {
            override fun onSuccess(resultDesc: ResultDesc?) {
                mActivity!!.mLoadDialog!!.hide()
                if (resultDesc!!.getcode() != 0) {
                    ToastUtil.showText(resultDesc.result)
                } else {
                    try {
                        mRecycleListBean_Type_1 = RecycleListBean()
                        mRecycleListBean_Type_1!!.data = ArrayList<RecycleListBean.DataBean>()
                        mRecycleListBean_Type_2 = RecycleListBean()
                        mRecycleListBean_Type_2!!.data = ArrayList<RecycleListBean.DataBean>()
                        mRecycleListBean_Type_3 = RecycleListBean()
                        mRecycleListBean_Type_3!!.data = ArrayList<RecycleListBean.DataBean>()
                        mRecycleListBean = MyApp.gson.fromJson(resultDesc!!.result, RecycleListBean::class.java)
                        for (i in mRecycleListBean!!.data!!.indices) {
                            when (mRecycleListBean!!.data[i].type) {
                                1 -> mRecycleListBean_Type_1!!.data.add(mRecycleListBean!!.data[i])
                                2 -> mRecycleListBean_Type_2!!.data.add(mRecycleListBean!!.data[i])
                                3 -> mRecycleListBean_Type_3!!.data.add(mRecycleListBean!!.data[i])
                            }
                        }
                        if (type == 1)
                            showTypeDialog()
                    } catch (e: Exception) {
                        LogUtils.e("获取数据异常 ：$e  ,data= ${resultDesc!!.result}")
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
     * 刷新/初始化当前本地记录列表
     */
    private fun initListView() {
        if (mMain_Input_ListViewAdapter == null) {
            mMain_Input_ListViewAdapter = Main_Input_ListViewAdapter(mInputLiat)
            Input_Main_ListView.adapter = mMain_Input_ListViewAdapter
            Input_Main_ListView.setOnItemLongClickListener { _, _, position, _ ->
                MyDialog(mActivity!!)
                        .setMessage("是否要删除此条信息？")
                        .setNegativeButton("取消") { dialog, which ->
                            dialog.dismiss()
                        }
                        .setPositiveButton("删除") { dialog, which ->
                            mInputLiat.removeAt(position)
                            initListView()
                            dialog.dismiss()
                        }.create().show()
                true
            }
        } else {
            mMain_Input_ListViewAdapter!!.notifyDataSetChanged(mInputLiat!!)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun initView() {
        Input_Main_Title.text = "入库"
        Input_Main_Print.setImageResource(R.drawable.ic_print_black_24dp)
        Input_Main_Print.setOnClickListener {
            if (mInputLiat == null || mInputLiat.size == 0) {
                ToastUtil.showText("没有数据,请添加记录")
                return@setOnClickListener
            }
            UpToServer("17611110000")

//                val edit = MyEditText(mActivity)
//                edit.hint = "请输入手机号"
//                edit.inputType = InputType.TYPE_CLASS_PHONE
//                edit.maxLines = 1
//                MyDialog(mActivity!!)
//                        .setMessage("打印票据需要输入用户手机号!")
//                        .setView(edit, 40, 0, 40, 0)
//                        .setNegativeButton("取消") { dialog, which ->
//                            dialog.dismiss()
//                        }
//                        .setPositiveButton("打印") { dialog, which ->
//                            val phone = edit.text.toString()
//                            if (phone == "") {
//                                ToastUtil.showText("请输入用户手机号")
//                                return@setPositiveButton
//                            }
//
//                            dialog.dismiss()
//                        }.create().show()
        }
        Input_Main_Back.setImageResource(R.drawable.ic_settings_bluetooth_black_24dp)
        Input_Main_Back.setOnClickListener {
            startActivity(Intent(mActivity, AddDevActivity::class.java))
        }

        Input_Main_Record!!.setOnClickListener {
            //跳转入库记录
            startActivity(Intent(mActivity, InputRecordActivity::class.java))
        }
        Input_Main_Clear.setOnClickListener {
            //清楚当前本地记录
            if (mInputLiat.size == 0) {
                ToastUtil.showText("请先添加记录")
            } else
                MyDialog(mActivity!!).setMessage("是否清理当前记录?")
                        .setNegativeButton("取消") { dialog, which ->
                            dialog.dismiss()
                        }.setPositiveButton("清理") { dialog, which ->
                            dialog.dismiss()
                            mInputLiat.clear()
                            mType = -1
                            initListView()
                        }.show()
        }

        Input_Main_Add.setOnClickListener {
            //添加本地记录
            if (mRecycleListBean == null) {
                mActivity!!.mLoadDialog!!.show()
                getRecycleData(1)
            } else {
                if (mInputLiat.size == 0)
                    mType = -1
                showTypeDialog()
            }
        }
    }

    /**
     * 上传入库数据到服务器
     */
    private fun UpToServer(userphone: String) {
        val ServerStation_Id = AppSharePreferenceMgr.get(SaveKey.SERVERSTATION_ID, -1)
        if (ServerStation_Id == -1) {
            ToastUtil.showText("请先选择服务站点")
            return
        }
        mActivity!!.mLoadDialog!!.show(arrayOf("正在打印", "打印超时"))

        var UpBean = AppInputBean()
        UpBean.phone = userphone
        UpBean.servicePointId = ServerStation_Id?.toString()
        UpBean.staffId = MyApp.UserInfo?.data?.id?.toString()
        UpBean.type = if (mType == 2) "1" else mType.toString()

        //将本地记录转成要上传的JSON数据
        var lingsBeanList = ArrayList<AppInputBean.RecyclingsBean>()
        for (i in mInputLiat.indices) {
            var lingsBean = AppInputBean.RecyclingsBean()
            lingsBean.recyclingPriceId = mInputLiat[i]?.typeid
            lingsBean.weight = mInputLiat[i]?.weight
            lingsBean.typename = mInputLiat[i]?.type
            lingsBeanList.add(lingsBean)
        }
        UpBean.recyclings = lingsBeanList
        var time = SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(System.currentTimeMillis()))
        UpBean.time = time
        var time_id = time.replace("-".toRegex(), "").substring(0, 8)
        var time_8Char = System.currentTimeMillis().toString()
        var deal_id = time_id + time_8Char.substring(time_8Char.length - 8)
        UpBean.dealId = deal_id

        PrintData("两网融合", deal_id)

        val NETWORK = AppNetworkMgr.getNetworkState(MyApp.mApplication!!.applicationContext)
        if (NETWORK == 0) {
            ToastUtil.showText("网络不可用，可在未长传界面进行上传")
            writeData(UpBean)
            return
        }

        OkHttpUtils.postAsyn(Ports.ADDOUTBACKLIST, MyApp.gson.toJson(UpBean), object : MyHttpCallback(mActivity) {
            override fun onSuccess(resultDesc: ResultDesc?) {
                mActivity!!.mLoadDialog!!.hide()
                if (resultDesc!!.getcode() != 0) {
                    ToastUtil.showText(resultDesc.result)
                } else {
                    mActivity!!.mLoadDialog!!.show("正在打印")

                }
            }

            override fun onFailure(code: Int, message: String?) {
                super.onFailure(code, message)
                ToastUtil.showText("上传失败，可在未长传界面进行上传")
                writeData(UpBean)
            }
        }, "新增入库")
    }

    /**
     * 打印票据
     */
    private fun PrintData(compiler: String, traceElement: String) {
        Thread(Runnable {
            try {
                if (mIzkcService == null) {
                    mHandler!!.sendEmptyMessage(-2)
                    return@Runnable
                }
                mIzkcService.printerInit()
                mIzkcService.printTextAlgin(compiler + "\n\n", 0, 1, 1)
                var Str = "单  号： $traceElement\n"
                mIzkcService.printGBKText(Str)
                var data = "时  间： " + SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(System.currentTimeMillis())) + "\n"
                mIzkcService.printGBKText(data)

                mIzkcService.printGBKText("服务站： " + AppSharePreferenceMgr.get(SaveKey.SERVERSTATION_NAME, "") + "\n")
                mIzkcService.printGBKText("********************************")
                var type = ""
                if (mType == 2) type = "数量" else type = "重量"

                var array = arrayOf(" 类型 ", "  $type", "   单价  ", " 总价")
                var array1 = intArrayOf(0, 1, 2, 2)
                var array2 = intArrayOf(0, 0, 0, 0)
                mIzkcService.printColumnsText(array, array1, array2)

                mIzkcService.printGBKText("--------------------------------")

                for (i in mInputLiat.indices) {
                    if (mInputLiat[i].type.length > 4) {
                        mInputLiat[i].type = mInputLiat[i].type.subSequence(0, 4).toString()
                    }

                    var array = arrayOf(mInputLiat[i].type, mInputLiat[i].weight, mInputLiat[i].unit, "￥" + mInputLiat[i].price, "￥" + mInputLiat[i].total)

                    var no2 = 0
                    var no3 = 0
                    var no4 = 0
                    var no5 = 0
                    var no6 = 0
                    when (mInputLiat[i].type.length) {//类型
                        1 -> no2 = 3
                        2 -> no2 = 2
                        3 -> no2 = 0
                    }
                    if (mType == 2) {
                        when (mInputLiat[i].weight.length) {//重量
                            1 -> no3 = 3
                            2 -> no3 = 2
                        }
                    } else {
                        when (mInputLiat[i].weight.length) {//重量
                            1 -> no3 = 5
                            2 -> no3 = 4
                            3 -> no3 = 3
                            4 -> no3 = 2
                            5 -> no3 = 1
                        }
                    }
                    when (mInputLiat[i].price.length) {//单价
                        1 -> no5 = 4
                        2 -> no5 = 3
                        3 -> no5 = 2
                        4 -> no5 = 1
                    }

                    when (mInputLiat[i].total.length) {//总价
                        3 -> no6 = 4
                        4 -> no6 = 3
                        5 -> no6 = 2
                        6 -> no6 = 1
                        7 -> no6 = 0
                    }

                    //                          0       6       0       4       3
                    LogUtils.i("打印间距 = $no2 -- $no3 -- $no4 -- $no5 --$no6")
                    var array3 = intArrayOf(no2, no3, no4, no5, no6)
                    var array4 = intArrayOf(0, 0, 0, 0, 0)
                    mIzkcService.printColumnsText(array, array3, array4)
                    mIzkcService.printGBKText("\n")
                }
                mIzkcService.printGBKText("********************************")
                var ZongZhong = ""
                if (mType == 2) {
                    var NumAll = 0
                    for (i in mInputLiat.indices) {
                        NumAll += mInputLiat[i].weight.toInt()
                    }
                    ZongZhong = "累计数量：" + NumAll + "台"
                } else {
                    var WeightAll = 0.000
                    for (i in mInputLiat.indices) {
                        WeightAll += mInputLiat[i].weight.toDouble()
                    }
                    ZongZhong = "累计重量：" + DecimalFormat("0.00").format(WeightAll) + "kg"
                }
                mIzkcService.printGBKText(ZongZhong + "\n")

                var MeonyAll = 0.000
                for (i in mInputLiat.indices) {
                    MeonyAll += mInputLiat[i].total.toDouble()
                    LogUtils.i("每个类型总价=" + mInputLiat[i].total.toDouble())
                }

                var ZongJia = "累计总额：￥" + totalMoney(MeonyAll)

                mIzkcService.printGBKText(ZongJia + "\n")

                mIzkcService.printGBKText("操 作 员：" + MyApp.UserInfo!!.data.name + "\n")
                mIzkcService.printGBKText("\n\n\n")
                mHandler!!.sendEmptyMessage(1)
            } catch (e: Exception) {
                LogUtils.e("打印票据异常：$e")
                mHandler!!.sendEmptyMessage(-1)
            }
        }).start()
    }

    /**
     * 小5取0，大5取5
     */
    fun totalMoney(money: Double): String {
        LogUtils.i("总金额为 = $money")
        val bigDec = BigDecimal(money)
        val total = bigDec.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).toDouble()
        val df = DecimalFormat("0.0")
        LogUtils.i("四舍五入后总金额为 = ${df.format(total)}")
        var zongjia = df.format(total)
        //小数的整数部分
        var intPart = zongjia.substring(0, zongjia.indexOf("."))
        //小数的小数部分
        var floatPart = zongjia.substring(zongjia.indexOf(".") + 1)
        floatPart = if (floatPart.toInt() < 5)
            "0"
        else
            "5"
        zongjia = "$intPart.$floatPart"
        return zongjia
    }

    /**
     * 写入文件
     */
    fun writeData(UpBean: AppInputBean) {
        Thread(Runnable {
            val data = File_Cache.readFile(FILE_DATA_NAME)
            var failedBean: UpInputFailedBean? = null
            if (data == "") {
                failedBean = UpInputFailedBean()
                failedBean.data = ArrayList<AppInputBean>()
            } else {
                failedBean = MyApp.gson.fromJson<UpInputFailedBean>(data, UpInputFailedBean::class.java)
            }
            failedBean!!.data.add(UpBean)
            File_Cache.writeFileToSD(MyApp.gson.toJson(failedBean), FILE_DATA_NAME)
        }).start()
    }


    /**
     * 回收物选择框
     */
    private fun showSelectDialog() {

        if (mRecycleListBean == null) {
            mActivity!!.mLoadDialog!!.show()
            getRecycleData(1)
            return
        }

        var names = ArrayList<String>()
        var position = 0
        if (mType == -1) {
            showTypeDialog()
        } else {
            when (mType) {
                1 -> {
                    for (i in mRecycleListBean_Type_1!!.data.indices) {
                        names.add(mRecycleListBean_Type_1!!.data[i].name + "       ￥" + mRecycleListBean_Type_1!!.data[i].price)
                    }
                }
                2 -> {
                    for (i in mRecycleListBean_Type_2!!.data.indices) {
                        names.add(mRecycleListBean_Type_2!!.data[i].name + "       ￥" + mRecycleListBean_Type_1!!.data[i].price)
                    }

                }
                3 -> {
                    for (i in mRecycleListBean_Type_3!!.data.indices) {
                        names.add(mRecycleListBean_Type_3!!.data[i].name + "       ￥" + mRecycleListBean_Type_1!!.data[i].price)
                    }
                }
            }


            MyDialog(mActivity!!).setTitle("选择回收物")
                    .setSingleChoiceItems(ArrayAdapter(mActivity, android.R.layout.simple_list_item_single_choice, names), 0, DialogInterface.OnClickListener { dialog, which ->
                        position = which
                    }).setPositiveButton("确定") { dialog, which ->
                        dialog.dismiss()
                        if (names.size == 0) {
                            ToastUtil.showText("该类型没有对应回收物，请重新选择")
                            return@setPositiveButton
                        }
                        //跳转到具体添加回收物页面
                        startActivityForResult(Intent(mActivity, AddInputAvtivity::class.java).run {
                            putExtra("position", position)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 101 && data != null) {
            //回首页页面返回数据
            var bean = data.getSerializableExtra("data") as Input_Main_List_Bean
            bean.id = listid.toString()
            mInputLiat.add(bean)
            initListView()
            listid++
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

    /**
     * Handler 静态内部类，防止内存泄漏
     */
    private class MyHandler(activity: InputMainFragment) : Handler() {
        private val activityWeakReference: WeakReference<InputMainFragment> = WeakReference<InputMainFragment>(activity)
        override fun handleMessage(msg: Message) {
            val activity = activityWeakReference.get()
            if (activity != null) {
                activity.mLoadDialog!!.hide()
                when (msg.what) {
                    1 -> {
                        activity.mInputLiat.clear()
                        activity.initListView()
                        ToastUtil.showText("打印完成")
                    }
                    -1 -> {
                        activity.mInputLiat.clear()
                        activity.initListView()
                        ToastUtil.showText("上传成功,但打印机发生错误,未能正常打印")
                    }
                    -2 -> {
                        activity.mInputLiat.clear()
                        activity.initListView()
                        ToastUtil.showText("上传成功,但未找到打印机")
                    }
                    File_Cache.WriteOk -> {
                        activity.Input_Main_Warn.visibility = View.VISIBLE
                        activity.Input_Main_Warn.setOnClickListener {
                            activity.mActivity_!!.startActivity(Intent(mActivity, UploadFailedActivity::class.java))
                        }
                    }
                    FileHandData -> {
                        activity.Input_Main_Warn.visibility = View.VISIBLE
                        activity.Input_Main_Warn.setOnClickListener {
                            activity.mActivity_!!.startActivity(Intent(mActivity, UploadFailedActivity::class.java))
                        }
                    }
                }
            }
        }
    }
}
