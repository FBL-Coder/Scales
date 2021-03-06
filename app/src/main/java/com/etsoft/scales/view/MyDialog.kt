package com.etsoft.scales.view

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.etsoft.scales.R

/**
 * Author：FBL  Time： 2018/8/13.
 * 封装TitleView Dialog
 *
 */
class MyDialog : AlertDialog.Builder {
    var tv: TextView? = null

    constructor(context: Context, title: String = "提示") : super(context) {
        init(context, title)
    }

    constructor(context: Context, theme: Int, title: String) : super(context, theme) {
        init(context, title)
    }

    internal fun init(context: Context, title: String) {
        tv = TextView(context)
        setCustomTitle(initTitleView(context, title))
    }

    override fun setTitle(title: CharSequence?): AlertDialog.Builder {
        if (tv === null) {
            tv = TextView(context)
        }
        tv?.text = title
        return super.setTitle(title)
    }

    @SuppressLint("ResourceAsColor", "NewApi")
    private fun initTitleView(context: Context, title: String): View {
        if (tv === null) {
            tv = TextView(context)
        }
        tv?.run {
            background = context.getDrawable(R.color.white_)
            text = title
            gravity = Gravity.CENTER or Gravity.LEFT
            textSize = 20f
            setPadding(20, 20, 20, 20)
            paint.color = R.color.black
            paint.isFakeBoldText = true
            this
        }

        val view = View(context)
        view.background = context.resources.getDrawable(R.color.black)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3)

        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(tv)
        linearLayout.addView(view)
        return linearLayout
    }


}
