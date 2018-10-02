package com.etsoft.scales.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow

/**
 * Author：FBL  Time： 2018/7/30.
 * 封装PopupWindow
 */
class MyPopupWindow(context: Context, view: View) : PopupWindow() {

    var mDismissListener: DismissListener? = null

    init {
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        isOutsideTouchable = true
        isFocusable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        contentView = view
    }

    override fun dismiss() {
        super.dismiss()
        if (mDismissListener != null)
            mDismissListener!!.miss()
    }

    override fun setOnDismissListener(onDismissListener: OnDismissListener?) {
        super.setOnDismissListener(onDismissListener)
    }

    interface DismissListener {
        fun miss()
    }
}
