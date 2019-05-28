package com.etsoft.scales.adapter.ListViewAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.etsoft.scales.R
import com.etsoft.scales.bean.RecycleListBean
import com.etsoft.scales.bean.ServerStationBean

/**
 * Author：FBL  Time： 2018/7/23.
 * 选择回收为列表Adapter
 *
 */
class SelectDialogListViewAdapter(list: List<RecycleListBean.DataBean>) : BaseAdapter() {

    var mList = list
    var clickPosition = -1

    fun notifyDataSetChanged(selectposition: Int, list: List<RecycleListBean.DataBean>) {
        mList = list
        clickPosition = selectposition
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var mViewHolder: ViewHolder
        if (view == null || view.tag == null) {
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_select_dialog, parent, false)
            mViewHolder = ViewHolder(view!!)
            view.tag = mViewHolder
        } else {
            mViewHolder = (view.tag as ViewHolder?)!!
        }
        mViewHolder.textView1.text = mList[position].name
        mViewHolder.textView2.text = "￥ " + mList[position].price

        if (position == clickPosition)
            mViewHolder.ImageView1.setImageResource(R.drawable.ic_radio_button_checked_black_24dp)
        else mViewHolder.ImageView1.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp)

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
        val textView1: TextView = view.findViewById(R.id.textView1)
        val textView2: TextView = view.findViewById(R.id.textView2)
        val ImageView1: ImageView = view.findViewById(R.id.ImageView1)
    }
}
