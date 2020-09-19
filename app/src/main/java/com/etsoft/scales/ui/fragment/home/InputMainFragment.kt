package com.etsoft.scales.ui.fragment.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.Ports
import com.etsoft.scales.R
import com.etsoft.scales.SaveKey
import com.etsoft.scales.SaveKey.Companion.FILE_DATA_NAME
import com.etsoft.scales.adapter.ListViewAdapter.Main_Input_ListViewAdapter
import com.etsoft.scales.adapter.ListViewAdapter.SelectDialogListViewAdapter
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.app.MyApp.Companion.mRecycleListBean
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
import io.github.xudaojie.qrcodelib.CaptureActivity
import kotlinx.android.synthetic.main.fragment_input_main.*
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.sign


/**
 * Author：FBL  Time： 2018/7/20.
 * 入库
 */
class InputMainFragment : Fragment() {

    companion object {
        var FileHandData = 111
        var FileNoData = 100
        var ADDITEM_CODE = 101
        var mActivity: MainActivity? = null
        fun initFragment(activity: MainActivity): InputMainFragment {
            var mCareFragment = InputMainFragment()
            mActivity = activity
            return mCareFragment
        }
    }

    private var listid = 0

    private var Type_Name = listOf("再生资源", "电子废弃物", "低价回收物", "有毒有害物",
            "废纸张", "废塑料", "废旧金属", "电子产品", "废玻璃", "废旧棉织物", "废木头", "其它")


    //选择Dialog 后的列表
    private var mName_Select = ArrayList<RecycleListBean.DataBean>()

    //选择Dialog名称
    private var mName_Search = ArrayList<RecycleListBean.DataBean>()

    //添加完成后的记录列表
    private var mInputLiat = ArrayList<Input_Main_List_Bean>()
    private var mMain_Input_ListViewAdapter: Main_Input_ListViewAdapter? = null
    private var mHandler: MyHandler? = null
    private var mLoadDialog: ProgressBarDialog? = null
    private var mActivity_: MainActivity? = null
    private var ServerStation_Id: Int = -1
    private var time = ""
    private var dealid = ""
    private var code = ""

    private var mPosition_Select = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_input_main, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLoadDialog = mActivity!!.mLoadDialog!!
        mActivity_ = mActivity
        mHandler = MyHandler(this)
        File_Cache.setHandler(mHandler)
        //获取回收物列表
        mLoadDialog?.show()
        getRecycleData()

        initView()
        initListView()

        //蓝牙连接状态监听
        BlueBoothReceiver.mOnBlueConnecetChangerlistener_input = object : OnBlueConnecetChangerlistener {
            override fun OnBlueChanger(isConnect: Boolean) {
                if (isConnect)
                    Input_Main_Back.setImageResource(R.drawable.ic_settings_bluetooth_blue_a200_24dp)
                else
                    Input_Main_Back.setImageResource(R.drawable.ic_settings_bluetooth_black_24dp)
            }
        }

        //后台线程，查询未上传记录，并且自动上传
//        UnUploadRecordTimer.runTimeronUpload(mHandler!!)


        //定时获取回收物列表
        Thread {
            while (true) {
                Thread.sleep(300000)
                getRecycleData()
            }
        }.start()
    }

    override fun onStart() {
        Input_Main_Warn.visibility = View.GONE
        //获取本地未上传记录数据
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
                    } else {
                        mHandler!!.sendEmptyMessage(FileNoData)
                    }
                }
            }
        }).start()
    }

    /**
     * 获取回收物信息
     */
    private fun getRecycleData() {
        var map = HashMap<String, String>()
        map["limit"] = "1000"
        OkHttpUtils.getAsyn(Ports.RECYCLELIST, map, object : MyHttpCallback(mActivity) {
            override fun onSuccess(resultDesc: ResultDesc?) {
                mActivity!!.mLoadDialog!!.hide()
                if (resultDesc!!.getcode() != 0) {
                    ToastUtil.showText(resultDesc.result)
                } else {
                    try {
                        mRecycleListBean = MyApp.gson.fromJson(resultDesc!!.result, RecycleListBean::class.java)
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

            MyDialog(mActivity_!!)
                    .setMessage("是否先扫描二维码？(持有绿卡用户)")
                    .setNeutralButton("取消打印") { dialog, which ->
                        dialog.dismiss()
                        return@setNeutralButton
                    }
                    .setPositiveButton("扫描后打印") { dialog, which ->
                        dialog.dismiss()
                        val i = Intent(mActivity_, CaptureActivity::class.java)
                        startActivityForResult(i, Activity.RESULT_FIRST_USER)
                    }
                    .setNegativeButton("跳过扫描") { dialog, which ->
                        dialog.dismiss()
                        printEvent()
                    }.show()
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
                            initListView()
                        }.show()
        }





        Input_Main_Add.setOnClickListener {
            //添加本地记录
            if (mRecycleListBean == null) {
                mActivity!!.mLoadDialog!!.show()
                getRecycleData()
            } else {
                if (mInputLiat.size == 0) {
                    mName_Search.clear()
                }
                showTypeDialog()
            }
        }
    }

    /**
     * 打印逻辑
     */
    private fun printEvent() {

        time = SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(System.currentTimeMillis()))
        var time_id = time.replace("-".toRegex(), "").substring(0, 8)
        var time_8Char = System.currentTimeMillis().toString()
        dealid = time_id + time_8Char.substring(time_8Char.length - 8)

        ServerStation_Id = AppSharePreferenceMgr.get(SaveKey.SERVERSTATION_ID, -1) as Int
        if (ServerStation_Id == -1) {
            ToastUtil.showText("请先选择服务站点")
            return
        }
        mActivity_!!.mLoadDialog!!.show("正在打印", false)
        PrintData("两网融合", dealid)
    }

    /**
     * 上传入库数据到服务器
     */
    private fun UpToServer(time: String, dealid: String) {

        mActivity!!.mLoadDialog!!.show(arrayOf("正在上传", "上传超时"), false)

        var UpBean = AppInputBean()
        UpBean.phone = "17611110000"
        UpBean.servicePointId = ServerStation_Id?.toString()
        UpBean.staffId = MyApp.UserInfo?.data?.id?.toString()
        UpBean.type = mPosition_Select.toString()
        UpBean.greenId = code

        //将本地记录转成要上传的JSON数据
        var lingsBeanList = ArrayList<AppInputBean.RecyclingsBean>()
        for (i in mInputLiat.indices) {
            var lingsBean = AppInputBean.RecyclingsBean()
            lingsBean.recyclingPriceId = mInputLiat[i]?.typeid
            lingsBean.weight = mInputLiat[i]?.weightValid
            lingsBean.number = mInputLiat[i]?.number
            lingsBean.typename = mInputLiat[i]?.type
            lingsBeanList.add(lingsBean)
        }

        UpBean.recyclings = lingsBeanList
        UpBean.time = time
        UpBean.dealId = dealid

        val NETWORK = AppNetworkMgr.getNetworkState(MyApp.mApplication!!.applicationContext)
        if (NETWORK == 0) {
            mActivity!!.mLoadDialog!!.hide()
            ToastUtil.showText("网络不可用，可在未长传界面进行上传")
            UpBean.failureInfo = "网络不可用，无法上传"
            writeData(UpBean)
        } else {
            var str = MyApp.gson.toJson(UpBean)
            LogUtils.i(str)
            OkHttpUtils.postAsyn(Ports.ADDOUTBACKLIST, str, object : MyHttpCallback(mActivity) {
                override fun onSuccess(resultDesc: ResultDesc?) {
                    mActivity!!.mLoadDialog!!.hide()
                    if (resultDesc!!.getcode() != 0) {
                        UpBean.failureInfo = "返回数据Code != 0，返回结果：$resultDesc"
                        ToastUtil.showText("上传失败，可在未长传界面进行上传")
                        LogUtils.i(resultDesc.result)
                        writeData(UpBean)
                    } else {
                        ToastUtil.showText("上传成功")
                    }
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    var mes = if (message?.length!! > 200) message?.substring(0, 200) else message
                    mActivity!!.mLoadDialog!!.hide()
                    ToastUtil.showText("上传失败，可在未长传界面进行上传")
                    UpBean.failureInfo = "onFailure方法，服务器或连接异常，返回结果：$mes"
                    writeData(UpBean)
                }
            }, "新增入库")
        }
        mInputLiat.clear()
        initListView()
    }

    /**  1  37  13
     * 1  39  44
     * 打印票据
     */
    private fun PrintData(compiler: String, traceElement: String) {
        Thread(Runnable {
            try {
                if (mIzkcService == null) {
                    mIzkcService.printerInit()
                }
                if (mIzkcService.checkPrinterAvailable()) {
                    mIzkcService.printTextAlgin(compiler + "\n\n", 0, 1, 1)
                    var Str = "单  号： $traceElement\n"
                    mIzkcService.printGBKText(Str)
                    var data = "时  间： " + SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(System.currentTimeMillis())) + "\n"

                    mIzkcService.printGBKText(data)

                    mIzkcService.printGBKText("服务站： " + AppSharePreferenceMgr.get(SaveKey.SERVERSTATION_NAME, "") + "\n")
                    mIzkcService.printGBKText("********************************")
                    mIzkcService.setTypeface(1)
                    mIzkcService.setFontSize(1)
                    mIzkcService.printGBKText("    类型     重量     数量     单价     总价\n")
                    mIzkcService.printGBKText("------------------------------------------------\n")


                    for (i in mInputLiat.indices) {
                        var name = ""
                        var wight = ""
                        var count = ""
                        var price = ""
                        var total = ""
                        var name_type = getSize(mInputLiat[i].type)
                        if (name_type > 12) {
                            mInputLiat[i].type = mInputLiat[i].type.subSequence(0, 6).toString()
                            name_type = 12
                        }
                        when (name_type) {
                            2 -> name = " ${mInputLiat[i].type}         "
                            3 -> name = " ${mInputLiat[i].type}        "
                            4 -> name = " ${mInputLiat[i].type}       "
                            5 -> name = " ${mInputLiat[i].type}      "
                            6 -> name = " ${mInputLiat[i].type}     "
                            7 -> name = " ${mInputLiat[i].type}    "
                            8 -> name = " ${mInputLiat[i].type}   "
                            9 -> name = " ${mInputLiat[i].type}  "
                            10 -> name = " ${mInputLiat[i].type} "
                            11 -> name = " ${mInputLiat[i].type}"
                            12 -> name = "${mInputLiat[i].type}"
                        }
                        when (mInputLiat[i].weightValid.length) {
                            2 -> wight = " ${mInputLiat[i].weightValid}      "
                            3 -> wight = " ${mInputLiat[i].weightValid}     "
                            4 -> wight = " ${mInputLiat[i].weightValid}    "
                            5 -> wight = " ${mInputLiat[i].weightValid}   "
                            6 -> wight = " ${mInputLiat[i].weightValid}  "
                            7 -> wight = " ${mInputLiat[i].weightValid} "
                            8 -> wight = " ${mInputLiat[i].weightValid}"
                        }
                        when (mInputLiat[i].number.length) {
                            1 -> count = " ${mInputLiat[i].number}       "
                            2 -> count = " ${mInputLiat[i].number}      "
                            3 -> count = " ${mInputLiat[i].number}     "
                            4 -> count = " ${mInputLiat[i].number}    "
                            5 -> count = " ${mInputLiat[i].number}   "
                            6 -> count = " ${mInputLiat[i].number}  "
                            7 -> count = " ${mInputLiat[i].number} "
                            8 -> count = " ${mInputLiat[i].number}"
                        }
                        when (mInputLiat[i].price.length) {
                            1 -> price = " ${mInputLiat[i].price}       "
                            2 -> price = " ${mInputLiat[i].price}      "
                            3 -> price = " ${mInputLiat[i].price}     "
                            4 -> price = " ${mInputLiat[i].price}    "
                            5 -> price = " ${mInputLiat[i].price}   "
                            6 -> price = " ${mInputLiat[i].price}  "
                            7 -> price = " ${mInputLiat[i].price} "
                            8 -> price = " ${mInputLiat[i].price}"
                        }
                        when (mInputLiat[i].total.length) {
                            1 -> total = " ${mInputLiat[i].total}       "
                            2 -> total = " ${mInputLiat[i].total}      "
                            3 -> total = " ${mInputLiat[i].total}     "
                            4 -> total = " ${mInputLiat[i].total}    "
                            5 -> total = " ${mInputLiat[i].total}   "
                            6 -> total = " ${mInputLiat[i].total}  "
                            7 -> total = " ${mInputLiat[i].total} "
                            8 -> total = " ${mInputLiat[i].total}"
                        }
                        mIzkcService.printGBKText("${name + wight + count + price + total}\n")
                    }

                    mIzkcService.setTypeface(0)
                    mIzkcService.setFontSize(0)

                    var ZongZhong = ""
                    var Num = 0

                    for (i in mInputLiat.indices) {
                        Num += mInputLiat[i].number.toInt()
                    }
                    mIzkcService.printGBKText("********************************")
                    mIzkcService.printGBKText("累计数量：$Num\n")

                    var WeightAll = 0.000
                    for (i in mInputLiat.indices) {
                        WeightAll += mInputLiat[i].weightValid.toDouble()
                    }
                    ZongZhong = "累计重量：" + DecimalFormat("0.00").format(WeightAll) + "kg"

                    mIzkcService.printGBKText(ZongZhong + "\n")

                    var ZongJia = ""

                    var MeonyAll = 0.000
                    for (i in mInputLiat.indices) {
                        MeonyAll += mInputLiat[i].total.toDouble()
                        LogUtils.i("每个类型总价=" + mInputLiat[i].total.toDouble())
                    }
                    ZongJia = "累计总额：￥" + totalMoney(MeonyAll)

                    mIzkcService.printGBKText(ZongJia + "\n")

                    mIzkcService.printGBKText("操 作 员：" + MyApp.UserInfo!!.data.name + "\n")
                    mIzkcService.printGBKText("\n\n\n")

                    mHandler!!.sendEmptyMessage(1)
                } else {
                    mHandler!!.sendEmptyMessage(-3)
                }
            } catch (e: Exception) {
                LogUtils.e("打印票据异常：$e")
                mHandler!!.sendEmptyMessage(-1)
            }
        }).start()
    }


    /**
     * 获取类型长度
     */
    fun getSize(Str: String): Int {
        val p = Pattern.compile("[\u4e00-\u9fa5]")
        var Size = 0
        var chars = Str.toCharArray()
        for (i in chars.indices) {
            val m = p.matcher(chars[i].toString())
            if (m.find()) {
                Size += 2
            } else {
                Size += 1
            }
        }
        return Size
    }


    /**
     * 四舍五入
     */
    fun totalMoney(money: Double): String {
        LogUtils.i("总金额为 = $money")
        val df = DecimalFormat("0.0")
        LogUtils.i("取小数后两位总金额为 = ${df.format(money)}")
        var zongjia = df.format(money)
//        //小数的整数部分
//        var intPart = zongjia.substring(0, zongjia.indexOf("."))
//        //小数的小数部分
//        var floatPart = zongjia.substring(zongjia.indexOf(".") + 1)
//        floatPart = if (floatPart.toInt() < 5)
//            "0"
//        else
//            "5"
//        zongjia = "$intPart.$floatPart"
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
     * 显示首选回收物类型选择框
     */
    private fun showTypeDialog() {
        MyDialog(mActivity!!).setTitle("选择回收物类型")
                .setSingleChoiceItems(ArrayAdapter(mActivity, android.R.layout.simple_list_item_single_choice, Type_Name), 0) { dialog, which ->
                    mPosition_Select = which + 1
                }.setPositiveButton("确定") { dialog, _ ->
                    dialog.dismiss()
                    showSelectDialog(mPosition_Select)
                }.setNegativeButton("取消") { dialog, _ ->
                    dialog.dismiss()
                    mPosition_Select = 1
                }.create().run {
                    window.attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    this
                }.show()
    }


    /**
     * 回收物选择框
     */
    private fun showSelectDialog(position: Int) {

        var mPosition = 0
        mName_Select.clear()
        mName_Search.clear()

        for (data in mRecycleListBean!!.data) {
            if (data.type == position)
                mName_Select!!.add(data)
        }
        if (mName_Select!!.size == 0) {
            ToastUtil.showText("该类型没有对应回收物，请重新选择")
            return
        }
        mName_Search.addAll(mName_Select!!)

        var dialog = MyDialog(mActivity!!).setTitle("选择回收物")
                .setView(R.layout.dialog_inpiut_key)
                .setPositiveButton("确定") { dialog, which ->
                    dialog.dismiss()
                    //跳转到具体添加回收物页面
                    startActivityForResult(Intent(mActivity, AddInputAvtivity::class.java).run {
                        //                            putExtra("position", mPosition)
                        putExtra("type", position)
                        putExtra("data", mName_Search[mPosition])
                        this
                    }, Activity.RESULT_FIRST_USER)
                }.setNegativeButton("取消") { dialog, which ->
                    dialog.dismiss()
                }.create().run {
                    window.attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    this
                }
        dialog.show()

        (mActivity!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(mActivity?.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        var sSelectAdapter = SelectDialogListViewAdapter(mName_Search)
        var Search = dialog.findViewById<SearchView>(R.id.Search)
        var AllList = dialog.findViewById<TextView>(R.id.AllList)
        var ListView = dialog.findViewById<ListView>(R.id.ListView)
        var ListViewnull = dialog.findViewById<TextView>(R.id.ListView_null)
        ListView?.setOnItemClickListener { parent, view, position, id ->
            mPosition = position
            sSelectAdapter.notifyDataSetChanged(position, mName_Search)
        }
        ListView?.emptyView = ListViewnull
        ListView?.adapter = sSelectAdapter
        Search?.setIconifiedByDefault(false)
        Search?.isSubmitButtonEnabled = true
        Search?.clearFocus()
        AllList?.setOnClickListener {
            Search?.setQuery("", false)
            mName_Search.clear()
            mName_Search.addAll(mName_Select!!)
            sSelectAdapter = SelectDialogListViewAdapter(mName_Search)
            ListView?.adapter = sSelectAdapter
        }
        HideKeyboard(Search!!)
        Search?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(queryText: String): Boolean {
                LogUtils.d("搜索提交数据 = $queryText")
                if (queryText.isEmpty()) {
                    ToastUtil.showText("请输入关键字")
                    return false
                }
                mName_Search.clear()
                for (i in mName_Select!!.indices) {
                    if (mName_Select!![i].name.contains(queryText))
                        mName_Search.add(mName_Select!![i])
                }
                sSelectAdapter = SelectDialogListViewAdapter(mName_Search)
                ListView?.adapter = sSelectAdapter
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null)
            when (resultCode) {
                ADDITEM_CODE -> {
                    //回首页页面返回数据
                    var bean = data.getSerializableExtra("ResultData") as Input_Main_List_Bean
                    bean.id = listid.toString()
                    mInputLiat.add(bean)
                    initListView()
                    listid++
                }
                Activity.RESULT_OK -> {
                    //扫描二维码/条形码返回
                    code = data.getStringExtra("result")
                    LogUtils.i("扫描返回结果是 = $code")
                    printEvent()
                }
            }
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
                        activity.mActivity_!!.mLoadDialog!!.hide()
                        ToastUtil.showText("打印完成")
//                        activity.UpToServer(activity.time, activity.dealid)
                    }
                    -1 -> {
                        activity.mActivity_!!.mLoadDialog!!.hide()
                        ToastUtil.showText("打印机发生错误,未能正常打印")
                    }
                    -2 -> {
                        activity.mActivity_!!.mLoadDialog!!.hide()
                        ToastUtil.showText("未找到打印机")
                    }
                    -3 -> {
                        activity.mActivity_!!.mLoadDialog!!.hide()
                        ToastUtil.showText("打印机正忙，稍后在试")
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
                    FileNoData -> {
                        activity.Input_Main_Warn.visibility = View.GONE
                    }
                }
            }
        }
    }

    fun HideKeyboard(v: View) {
        var imm = v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0)
    }
}
