package com.etsoft.scales.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.apkfuns.logutils.LogUtils
import com.etsoft.scales.R
import com.etsoft.scales.app.MyApp
import com.etsoft.scales.utils.ToastUtil
import com.etsoft.scales.utils.UtilHelpers
import com.etsoft.scales.view.MyDialog
import com.etsoft.scales.view.ProgressBarDialog
import com.githang.statusbar.StatusBarCompat

/**
 * Author：FBL  Time： 2018/7/20.
 */
abstract class BaseActivity : AppCompatActivity() {

    var mLoadDialog: ProgressBarDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            MyApp.mApplication!!.setActivities(this)
            StatusBarCompat.setStatusBarColor(this, resources.getColor(R.color.AppTheme_color))
            initDialog()
        } catch (e: Exception) {
            LogUtils.d("数据异常--->$e")
        }
    }

    fun initDialog() {
        mLoadDialog = ProgressBarDialog(this)
    }

    /**
     * 权限申请
     *
     * 一个或多个权限请求结果回调
     * 在用户点击不在提醒后，的回调操作
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (i in grantResults.indices) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                //在用户已经拒绝授权的情况下，如果shouldShowRequestPermissionRationale返回false则
                // 可以推断出用户选择了“不在提示”选项，在这种情况下需要引导用户至设置页手动授权
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                    //解释原因，并且引导用户至设置页手动授权
                    var dialig = MyDialog(this)
                            .setMessage("获取权限失败，请设置后再拨打！")
                            .setPositiveButton("去设置") { dialog, which ->
                                //引导用户至设置页手动授权
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts("package", applicationContext.packageName, null)
                                intent.data = uri
                                startActivity(intent)
                            }
                            .setNegativeButton("取消") { dialog, which ->
                                //引导用户手动授权，权限请求失败
                            }.setOnCancelListener {
                                //引导用户手动授权，权限请求失败
                            }.show().run {
                                var attr = window.attributes
                                attr.height = ViewGroup.LayoutParams.WRAP_CONTENT
                                attr.width = ViewGroup.LayoutParams.WRAP_CONTENT
                                attr.gravity = Gravity.CENTER
                                window.attributes = attr
                                this
                            }


                } else {
                    ToastUtil.showText("权限获取失败")
                }
                break
            }
        }
    }

    /**
     * 软键盘 点击周围隐藏
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                val view = currentFocus
                UtilHelpers.hideKeyboard(ev, view, this@BaseActivity)//调用方法判断是否需要隐藏键盘
            }
            else -> {
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    companion object {

        inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
            val snack = Snackbar.make(this, message, length)
            snack.f()
            snack.show()
        }

        fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
            setAction(action, listener)
            color?.let { setActionTextColor(color) }
        }
    }


    override fun onDestroy() {
        mLoadDialog!!.destroyDialog()
        super.onDestroy()
    }

}
