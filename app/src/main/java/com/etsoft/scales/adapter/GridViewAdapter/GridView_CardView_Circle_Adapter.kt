package com.etsoft.scales.adapter.GridViewAdapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.etsoft.scales.bean.CareFragment_Bean
import com.etsoft.scales.R
import com.etsoft.scales.bean.RecycleListBean

/**
 * Author：FBL  Time： 2018/7/23.
 * GridView适配器  圆形卡片布局
 */
class GridView_CardView_Circle_Adapter(list: ArrayList<CareFragment_Bean>) : BaseAdapter() {

    var mList = list
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var mViewHolder: ViewHolder
        if (view == null) {
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_gridview_cardview_circle, parent, false)
            mViewHolder = ViewHolder(view!!)
            view.tag = mViewHolder
        } else {
            mViewHolder = (view.tag as ViewHolder?)!!
        }
        mViewHolder.run {
            mCard_CardView.setBackgroundColor(Color.parseColor(mList[position].color))
            mImageView.setImageResource(mList[position].image!!)
            mTextView.text = mList[position].tite!!
        }
        return view!!
    }

    override fun getItem(position: Int): Any {
        return mList[position]
    }

    override fun getItemId(position: Int): Long {

        return position.toLong()
    }

    override fun getCount(): Int {
        return mList.size
    }


    class ViewHolder(view: View) {
        val mCard_CardView = view.findViewById<RelativeLayout>(R.id.Card_CardView)
        val mImageView: ImageView = view.findViewById(R.id.Card_Iv)
        val mTextView: TextView = view.findViewById(R.id.Card_Tv)
    }
}
