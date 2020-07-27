package com.etsoft.scales.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.R
import com.etsoft.scales.SaveKey
import com.etsoft.scales.Server.BlueUtils
import com.etsoft.scales.Server.BlueUtils.Companion.isReadData
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.app.MyApp.Companion.mBluetoothDataIsEnable
import com.etsoft.scales.bean.Input_Main_List_Bean
import com.etsoft.scales.bean.RecycleListBean
import com.etsoft.scales.ui.fragment.home.InputMainFragment.Companion.ADDITEM_CODE
import com.etsoft.scales.utils.AppSharePreferenceMgr
import com.etsoft.scales.utils.BlueBoothState
import com.etsoft.scales.utils.MoneyValueFilter
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.view.MyDialog
import kotlinx.android.synthetic.main.activity_add_input.*
import java.lang.Exception
import java.lang.ref.WeakReference
import java.math.RoundingMode
import java.text.DecimalFormat


/**
 * Author：FBL  Time： 2018/9/30.
 * 添加入库数据
 */
class AddInputAvtivity : BaseActivity() {

    private var mHandler: MyHandler? = null
    private var mData: RecycleListBean.DataBean? = null
    private var mType = -1
    private var mChushiNum = -1

    override fun setView(): Int {
        return R.layout.activity_add_input
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHandler = MyHandler(this)
        initData()
    }

    private fun initData() {

        mData = intent.getSerializableExtra("data") as RecycleListBean.DataBean?
        mType = intent.getIntExtra("type", -1)
        LogUtils.i("回收物ID--${mData?.id}")


        Input_ChuShi.visibility = View.VISIBLE
        if (mBluetoothDataIsEnable && mData?.id != 70 && mData?.id != 71) {
            isReadData = true
            BlueUtils.readBlueData(mHandler!!, MyApp.mBluetoothSocket!!)
        } else {
            Add_Input_KG.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    Add_Input_5.isChecked = false
                    Add_Input_10.isChecked = false
                    Add_Input_20.isChecked = false
                    mChushiNum = -1
                    Add_Input_KG_OK.text = s
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
        }
        Add_Input_KG.filters = arrayOf<InputFilter>(MoneyValueFilter().setDigits(1))

        Add_Input_RadioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (Add_Input_KG.text.toString().isEmpty()) {
                Add_Input_5.isChecked = false
                Add_Input_10.isChecked = false
                Add_Input_20.isChecked = false
                ToastUtil.showText("称台重量为空，不可除杂")
                return@setOnCheckedChangeListener
            }
            when (checkedId) {
                R.id.Add_Input_5 -> {
                    mChushiNum = 5
                }
                R.id.Add_Input_10 -> {
                    mChushiNum = 10
                }
                R.id.Add_Input_20 -> {
                    mChushiNum = 20
                }
            }
            if (!Add_Input_KG.text.toString().isEmpty()) {
                var mWeight = Add_Input_KG.text.toString().toDouble()
                mWeight -= mWeight * mChushiNum / 100

                Add_Input_KG_OK.text = DecimalFormat("0.0").run {
                    roundingMode = RoundingMode.DOWN
                    this
                }.format(mWeight)
            }
        }



        when (mType) {
            2 -> {
                Add_Input_Num.hint = "0"
                Add_Input_Type.text = mData?.name
                Add_Input_DanWei.text = mData?.unit
                Add_Input_DanJia.text = mData?.price.toString()
                Add_Input_KG.isEnabled = !mBluetoothDataIsEnable
                Add_Input_KG.isFocusable = !mBluetoothDataIsEnable
                if (mData?.id == 70 || mData?.id == 71) {
                    Add_Input_KG.setText("0.1")
                    Add_Input_Num.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            try {
                                LogUtils.i("输入数量---$s")
                                var wg = 0.1 * s.toString().toInt()
                                LogUtils.i("输入数量转INt---${s.toString().toInt()}")
                                LogUtils.i("总质量INt---$wg")
                                Add_Input_KG.setText(wg.toFloat().toString())
                            } catch (e: Exception) {
                                Add_Input_KG.setText("0.1")
                            }
                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        }

                    })
                }
            }
            else -> {
                Add_Input_Type.text = mData?.name
                Add_Input_DanWei.text = mData?.unit
                Add_Input_DanJia.text = mData?.price.toString()
                Add_Input_KG.isEnabled = !mBluetoothDataIsEnable
                Add_Input_KG.isFocusable = !mBluetoothDataIsEnable
            }
        }
        var serverName = AppSharePreferenceMgr.get(SaveKey.SERVERSTATION_NAME, "未选择") as String
        Input_ServerStation.text = serverName
        if (serverName == "未选择") {
            Input_ServerStation.setTextColor(Color.RED)
        } else Input_ServerStation.setTextColor(Color.BLACK)

        Input_ServerStation.setOnClickListener {
            val text = if (AppSharePreferenceMgr.get(SaveKey.SERVERSTATION_ID, -1) == -1) "是否前往选择服务站?" else "是否重新选择服务站?"
            MyDialog(this).setMessage(text)
                    .setNegativeButton("取消") { dialog, which ->
                        dialog.dismiss()
                    }.setPositiveButton("选择") { dialog, which ->
                        dialog.dismiss()
                        startActivity(Intent(this, ServerStationActivity::class.java))
                        finish()
                    }.create().run {
                        window.attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        this
                    }.show()
        }

        Add_Input_Cancle.setOnClickListener { finish() }

        Add_Input_Ok.setOnClickListener {
            var weight_tv = Add_Input_KG.text.toString()
            var weight_tv_ok = Add_Input_KG_OK.text.toString()
            var num = Add_Input_Num.text.toString()
            if (weight_tv == "0.0" || weight_tv == "0" || weight_tv == "") {
                ToastUtil.showText("货物重量不能为空")
                return@setOnClickListener
            }
            if (mType == 2) {
                if (num == "0" || num == "") {
                    ToastUtil.showText("货物数量不能为空")
                    return@setOnClickListener
                }
            }

            setResult(ADDITEM_CODE, intent
                    .run {
                        putExtra("ResultData", Input_Main_List_Bean().run {
                            type = mData?.name
                            price = mData?.price.toString()
                            unit = mData?.unit
                            typeid = mData?.id.toString()
                            total = if (unit == "KG" || unit == "kg" || unit == "Kg" || unit == "kG") {
                                DecimalFormat("0.0").format(mData!!.price * weight_tv_ok.toDouble())
                            } else {
                                DecimalFormat("0.0").format(mData!!.price * num.toDouble())
                            }
                            mType_type = mType
                            weightValid = weight_tv_ok.toFloat().toString()
                            number = num
                            weightTotal = weight_tv.toFloat().toString()
                            this
                        })
                        this
                    })
            finish()
        }
    }


    /**
     * Handler 静态内部类，防止内存泄漏
     */
    @SuppressLint("SetTextI18n")
    private class MyHandler(activity: AddInputAvtivity) : Handler() {
        private var activityWeakReference: WeakReference<AddInputAvtivity> = WeakReference<AddInputAvtivity>(activity)
        override fun handleMessage(msg: Message) {
            val activity = activityWeakReference.get()
            if (activity != null) {
                when (msg.what) {
                    BlueBoothState.BLUE_READDATA_SUCCESS -> {
                        if (msg.obj != null)
                            BlueUtils.disposeData(this, msg.obj as ByteArray)
                    }
                    BlueBoothState.BLUE_OBTAINDATA_ERROR -> {
                        ToastUtil.showText("蓝牙数据出错,请稍后在试")
                    }
                    BlueBoothState.BLUE_DISPOSEDATA_SUCCESS -> {
                        if (msg.obj != null) {
                            var mWeight = msg.obj as Double
                            var mWeight_Ok = mWeight

                            if (activity.mChushiNum > 0) {
                                mWeight_Ok = mWeight - mWeight * activity.mChushiNum / 100
                            }

                            activity.Add_Input_KG.setText(DecimalFormat("0.0").run {
                                roundingMode = RoundingMode.DOWN
                                this
                            }.format(mWeight))
                            activity.Add_Input_KG_OK.text = DecimalFormat("0.0").run {
                                roundingMode = RoundingMode.DOWN
                                this
                            }.format(mWeight_Ok)
                        }
                    }
                }
            }
        }
    }

    override fun onStop() {
        isReadData = false
        super.onStop()
    }
}
