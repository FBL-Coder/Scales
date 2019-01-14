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
import com.etsoft.scales.Server.UnUploadRecordTimer
import com.etsoft.scales.adapter.ListViewAdapter.Gift_ListViewAdapter
import com.etsoft.scales.adapter.ListViewAdapter.Main_Input_ListViewAdapter
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.app.MyApp.Companion.mRecycleListBean
import com.etsoft.scales.app.MyApp.Companion.mRecycleListBean_Type_1
import com.etsoft.scales.app.MyApp.Companion.mRecycleListBean_Type_2
import com.etsoft.scales.app.MyApp.Companion.mRecycleListBean_Type_3
import com.etsoft.scales.bean.*
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
import kotlinx.android.synthetic.main.activity_out_info.*
import kotlinx.android.synthetic.main.fragment_input_main.*
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


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

    private var mInputLiat = ArrayList<Input_Main_List_Bean>()
    private var mMain_Input_ListViewAdapter: Main_Input_ListViewAdapter? = null
    private var mMain_Gifts_ListViewAdapter: Gift_ListViewAdapter? = null
    private var mHandler: MyHandler? = null
    private var mLoadDialog: ProgressBarDialog? = null
    private var mActivity_: MainActivity? = null
    private var ServerStation_Id: Int = -1
    private var time = ""
    private var dealid = ""
    private var code = ""

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
        //获取回收物列表
        getRecycleData(0)

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

    private fun initGiftListView(bean: GiftListBean) {
        if (mMain_Gifts_ListViewAdapter == null) {
            mMain_Gifts_ListViewAdapter = Gift_ListViewAdapter(bean)
            Main_Input_Gifts.adapter = mMain_Gifts_ListViewAdapter
        } else {
            mMain_Gifts_ListViewAdapter!!.notifyDataSetChanged(bean)
        }
        Main_GiftView.visibility = View.VISIBLE
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
                            mType = -1
                            Input_Main_Gift.visibility = View.GONE
                            Main_GiftView.visibility = View.GONE
                            initListView()
                        }.show()
        }

        Input_Main_Gift.setOnClickListener {
            mLoadDialog!!.show()
            OkHttpUtils.getAsyn(Ports.GIFTLIST, HashMap<String, String>().run {
                put("limit", "100")
                this
            }, object : MyHttpCallback(mActivity) {

                override fun onSuccess(resultDesc: ResultDesc?) {
                    super.onSuccess(resultDesc)
                    mLoadDialog!!.hide()
                    initGiftListView(MyApp.gson.fromJson(resultDesc!!.result, GiftListBean::class.java))
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    mLoadDialog!!.hide()
                }

            }, "礼品列表")
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
        PrintData("两网融合", dealid, mType)
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
        UpBean.type = if (mType == 2) "1" else mType.toString()
        UpBean.greenId = code

        //将本地记录转成要上传的JSON数据
        var lingsBeanList = ArrayList<AppInputBean.RecyclingsBean>()
        for (i in mInputLiat.indices) {
            var lingsBean = AppInputBean.RecyclingsBean()
            lingsBean.recyclingPriceId = mInputLiat[i]?.typeid
            lingsBean.weight = mInputLiat[i]?.weight
            lingsBean.typename = mInputLiat[i]?.type
            lingsBeanList.add(lingsBean)
        }

        if (mType == 3 && mMain_Gifts_ListViewAdapter != null && mMain_Gifts_ListViewAdapter!!.getGiftNum() != null) {
            var listgifts = ArrayList<AppInputBean.GiftsBean>()
            var mGiftBean = mMain_Gifts_ListViewAdapter!!.getGiftNum()
            for (i in mGiftBean.data.indices) {
                if (mGiftBean.data[i].condition > 0) {
                    var giftBean = AppInputBean.GiftsBean()
                    giftBean.giftId = mGiftBean.data[i].id.toString()
                    giftBean.number = mGiftBean.data[i].condition.toString()
                    listgifts.add(giftBean)
                }
            }
            UpBean.gifts = listgifts
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
            OkHttpUtils.postAsyn(Ports.ADDOUTBACKLIST, MyApp.gson.toJson(UpBean), object : MyHttpCallback(mActivity) {
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
        Input_Main_Gift.visibility = View.GONE
        Main_GiftView.visibility = View.GONE
        initListView()
    }

    /**  1  37  13
     * 1  39  44
     * 打印票据
     */
    private fun PrintData(compiler: String, traceElement: String, UpType: Int) {
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


                    var type = ""
                    mIzkcService.setTypeface(1)
                    mIzkcService.setFontSize(1)
                    if (mType == 2) {

                        var array = arrayOf("  类 型 ", "  数 量", "  单 价  ", " 总 价  ")
                        var array1 = intArrayOf(2, 6, 6, 2)
                        var array2 = intArrayOf(1, 1, 1, 1)
                        mIzkcService.printColumnsText(array, array1, array2)

                        mIzkcService.printGBKText("------------------------------------------------")

                        for (i in mInputLiat.indices) {
                            var name_type = getSize(mInputLiat[i].type)
                            if (name_type > 8) {
                                mInputLiat[i].type = mInputLiat[i].type.subSequence(0, 8).toString()
                            }
                            var array = arrayOf(mInputLiat[i].type, mInputLiat[i].weight, mInputLiat[i].unit, "￥" + mInputLiat[i].price, "￥" + mInputLiat[i].total)
                            var no2 = 0
                            var no3 = 0
                            var no4 = 0
                            var no5 = 0
                            var no6 = 0
                            when (name_type) {//类型
                                1F -> no2 = 14
                                1.5F -> no2 = 13
                                2F -> no2 = 12
                                2.5F -> no2 = 11
                                3F -> no2 = 10
                                3.5F -> no2 = 9
                                4F -> no2 = 8
                                4.5F -> no2 = 7
                                5F -> no2 = 6
                                5.5F -> no2 = 5
                                6F -> no2 = 4
                                6.5F -> no2 = 3
                                7F -> no2 = 2
                                7.5F -> no2 = 1
                                8F -> no2 = 0
                            }
                            when (mInputLiat[i].weight.length) {//数量
                                1 -> no3 = 5
                                2 -> no3 = 4
                                3 -> no3 = 3
                                4 -> no3 = 2
                                5 -> no3 = 1
                            }

                            when (mInputLiat[i].price.length) {//单价
                                1 -> no5 = 7
                                2 -> no5 = 6
                                3 -> no5 = 5
                                4 -> no5 = 4
                            }

                            when (mInputLiat[i].total.length) {//总价
                                3 -> no6 = 7
                                4 -> no6 = 6
                                5 -> no6 = 5
                                6 -> no6 = 4
                                7 -> no6 = 3
                            }

                            //                          0       6       0       4       3
                            LogUtils.i("打印间距 = $no2 -- $no3 -- $no4 -- $no5 --$no6")
                            var array3 = intArrayOf(no2, no3, no4, no5, no6)
                            var array4 = intArrayOf(1, 1, 1, 1, 1)
                            mIzkcService.printColumnsText(array, array3, array4)
                            mIzkcService.printGBKText("\n")
                        }

                    } else {

                        var array = arrayOf(" 类型 ", "  有效重量 ", " 除杂重量", "单价 ", "  总价  ")
                        var array1 = intArrayOf(1, 2, 2, 2, 2)
                        var array2 = intArrayOf(0, 0, 0, 0, 0)
                        mIzkcService.printColumnsText(array, array1, array2)

                        mIzkcService.printGBKText("------------------------------------------------")

                        for (i in mInputLiat.indices) {

                            var name_type = getSize(mInputLiat[i].type)
                            if (name_type > 5) {
                                mInputLiat[i].type = mInputLiat[i].type.subSequence(0, 5).toString()
                            }
                            var Chuzanum = mInputLiat[i].weight_all.toDouble() - mInputLiat[i].weight.toDouble()
                            var Chuzi = DecimalFormat("0.0").run {
                                roundingMode = RoundingMode.DOWN
                                this
                            }.format(Chuzanum)

                            var array = arrayOf(mInputLiat[i].type, mInputLiat[i].weight + " " + mInputLiat[i].unit, Chuzi + " " + mInputLiat[i].unit, "￥" + mInputLiat[i].price, "￥" + mInputLiat[i].total)
                            var no2 = 0
                            var no3 = 0
                            var no4 = 0
                            var no6 = 0
                            var no7 = 0
                            when (name_type) {//类型
                                1F -> no2 = 8
                                1.5F -> no2 = 7
                                2F -> no2 = 6
                                2.5F -> no2 = 5
                                3F -> no2 = 4
                                3.5F -> no2 = 3
                                4F -> no2 = 2
                                4.5F -> no2 = 1
                                5F -> no2 = 0
                            }
                            when (mInputLiat[i].weight.length) {//有效重量
                                1 -> no3 = 5
                                2 -> no3 = 4
                                3 -> no3 = 3
                                4 -> no3 = 2
                                5 -> no3 = 1
                            }
                            when (mInputLiat[i].weight.length) {//除杂重量
                                1 -> no4 = 6
                                2 -> no4 = 5
                                3 -> no4 = 4
                                4 -> no4 = 3
                            }
                            when (mInputLiat[i].price.length) {//单价
                                1 -> no6 = 4
                                2 -> no6 = 3
                                3 -> no6 = 2
                                4 -> no6 = 1
                            }

                            when (mInputLiat[i].total.length) {//总价
                                3 -> no7 = 5
                                4 -> no7 = 4
                                5 -> no7 = 3
                                6 -> no7 = 2
                                7 -> no7 = 1
                            }

                            //                          0       6       0       4       3
                            LogUtils.i("打印间距 = $no2 -- $no3 -- $no4 --  --$no6")
                            var array3 = intArrayOf(no2, no3, no4, no6, no7)
                            var array4 = intArrayOf(1, 1, 1, 1, 1)
                            mIzkcService.printColumnsText(array, array3, array4)
                            mIzkcService.printGBKText("\n")
                        }
                    }

                    mIzkcService.setTypeface(0)
                    mIzkcService.setFontSize(0)
                    if (UpType == 3 && mMain_Gifts_ListViewAdapter != null && mMain_Gifts_ListViewAdapter!!.getGiftNum().data != null) {
                        mIzkcService.printGBKText("\n")
                        mIzkcService.printGBKText("回馈礼品：\n")
                        mIzkcService.printGBKText("--------------------------------")

                        for (i in mMain_Gifts_ListViewAdapter!!.getGiftNum().data.indices) {
                            if (mMain_Gifts_ListViewAdapter!!.getGiftNum().data[i].condition > 0) {
                                var array = arrayOf("${mMain_Gifts_ListViewAdapter!!.getGiftNum().data[i].name}", "${mMain_Gifts_ListViewAdapter!!.getGiftNum().data[i].condition}", "个")
                                var array1 = intArrayOf(2, 2, 2)
                                var array2 = intArrayOf(1, 1, 1)
                                mIzkcService.printColumnsText(array, array1, array2)
                            }
                        }
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

                    var ZongJia = ""
                    if (UpType == 3) {
                        ZongJia = "累计总额：￥-/-"
                    } else {
                        var MeonyAll = 0.000
                        for (i in mInputLiat.indices) {
                            MeonyAll += mInputLiat[i].total.toDouble()
                            LogUtils.i("每个类型总价=" + mInputLiat[i].total.toDouble())
                        }
                        ZongJia = "累计总额：￥" + totalMoney(MeonyAll)
                    }
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
    fun getSize(Str: String): Float {
        val p = Pattern.compile("[\u4e00-\u9fa5]")
        var Size = 0F
        var chars = Str.toCharArray()
        for (i in chars.indices) {
            val m = p.matcher(chars[i].toString())
            if (m.find()) {
                Size++
            } else {
                Size += 0.5F
            }
        }
        return Size
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
                    for (a in mRecycleListBean_Type_2!!.data.indices) {
                        names.add(mRecycleListBean_Type_2!!.data[a].name + "       ￥" + mRecycleListBean_Type_2!!.data[a].price)
                    }

                }
                3 -> {
                    for (i in mRecycleListBean_Type_3!!.data.indices) {
                        names.add(mRecycleListBean_Type_3!!.data[i].name + "       ￥" + mRecycleListBean_Type_3!!.data[i].price)
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
        if (data != null)
            when (resultCode) {
                ADDITEM_CODE -> {
                    //回首页页面返回数据
                    var bean = data.getSerializableExtra("data") as Input_Main_List_Bean
                    bean.id = listid.toString()
                    mInputLiat.add(bean)
                    initListView()
                    listid++
                    if (bean.mType_type == 3) {
                        Input_Main_Gift.visibility = View.VISIBLE
                    } else {
                        Input_Main_Gift.visibility = View.GONE
                    }
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
     * 显示首选回收物类型选择框
     */
    private fun showTypeDialog() {
        if (mType == -1) {
            var name = ArrayList<String>()
            if (mRecycleListBean_Type_1!!.data.size > 0) name.add("再生资源")
            if (mRecycleListBean_Type_2!!.data.size > 0) name.add("电子废弃物")
            if (mRecycleListBean_Type_3!!.data.size > 0) name.add("低价回收物")

            MyDialog(mActivity!!).setTitle("选择回收物类型")
                    .setSingleChoiceItems(ArrayAdapter(mActivity, android.R.layout.simple_list_item_single_choice, name), 0, DialogInterface.OnClickListener { dialog, which ->
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
                        activity.mActivity_!!.mLoadDialog!!.hide()
                        ToastUtil.showText("打印完成")
                        activity.UpToServer(activity.time, activity.dealid)
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
}
