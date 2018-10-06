package com.etsoft.scales.adapter.ListViewAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.etsoft.scales.R
import com.etsoft.scales.bean.BlueBoothSSBean
import com.etsoft.scales.bean.Input_Main_List_Bean

/**
 * Author：FBL  Time： 2018/7/23.
 * Mian 搜索蓝牙数据Adapter
 *
 */
class BlueBooth_SSListViewAdapter(list: ArrayList<BlueBoothSSBean>) : BaseAdapter() {

    var mList = ArrayList<BlueBoothSSBean>()
    fun notifyDataSetChanged(list: ArrayList<BlueBoothSSBean>) {
        mList = list
        super.notifyDataSetChanged()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var mViewHolder: ViewHolder
        if (view == null || view.tag == null) {
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_listview_ss_bluebooth, parent, false)
            mViewHolder = ViewHolder(view!!)
            view.tag = mViewHolder
        } else {
            mViewHolder = (view.tag as ViewHolder?)!!
        }
        mViewHolder.Text1.text = "MAC：${mList[position].MAC}"
        mViewHolder.Text2.text = "Name：${mList[position].Name}"
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
        val Text1: TextView = view.findViewById(R.id.Text1)
        val Text2: TextView = view.findViewById(R.id.Text2)
    }
}
