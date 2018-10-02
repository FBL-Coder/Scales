package com.etsoft.scales.adapter.ListViewAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.etsoft.scales.R
import com.etsoft.scales.bean.OutListBean

/**
 * Author：FBL  Time： 2018/7/23.
 * Mian 出库记录Adapter
 *
 */
class Main_Out_ListViewAdapter(bean: OutListBean) : BaseAdapter() {

    var mList = bean.data as ArrayList<OutListBean.DataBean>

    fun notifyDataSetChanged(bean: OutListBean) {
        mList = bean.data as ArrayList<OutListBean.DataBean>
        super.notifyDataSetChanged()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var mViewHolder: ViewHolder
        if (view == null || view.tag == null) {
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_listview_inputmain, parent, false)
            mViewHolder = ViewHolder(view!!)
            view.tag = mViewHolder
        } else {
            mViewHolder = (view.tag as ViewHolder?)!!
        }
        mViewHolder.mInformation_Item1.text = mList[position].to_place
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
        val mInformation_Item1: TextView = view.findViewById(R.id.Information_Item_1)
        val mInformation_Item2: TextView = view.findViewById(R.id.Information_Item_2)
        val mInformation_Item3: TextView = view.findViewById(R.id.Information_Item_3)
        val mInformation_Item4: TextView = view.findViewById(R.id.Information_Item_4)
        val mInformation_Item5: TextView = view.findViewById(R.id.Information_Item_5)
        val mInformation_Item6: TextView = view.findViewById(R.id.Information_Item_6)
    }
}
