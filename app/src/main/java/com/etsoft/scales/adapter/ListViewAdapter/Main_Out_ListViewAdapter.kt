package com.etsoft.scales.adapter.ListViewAdapter

import android.annotation.SuppressLint
import android.content.Context
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
class Main_Out_ListViewAdapter(context: Context, bean: OutListBean) : BaseAdapter() {

    var mContext = context
    var mList = bean.data as ArrayList<OutListBean.DataBean>

    fun notifyDataSetChanged(bean: OutListBean) {
        mList = bean.data as ArrayList<OutListBean.DataBean>
        super.notifyDataSetChanged()
    }


    @SuppressLint("NewApi", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var mViewHolder: ViewHolder
        if (view == null || view.tag == null) {
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_listview_outmain, parent, false)
            mViewHolder = ViewHolder(view!!)
            view.tag = mViewHolder
        } else {
            mViewHolder = (view.tag as ViewHolder?)!!
        }
        mViewHolder.mOutTime.text = mList[position]?.update_time
        mViewHolder.mOutFrom.text = mList[position]?.staff_name
        mViewHolder.mOutState.text = if (mList[position]?.recyclingPrice?.status?.toInt() == 1) "已出库" else "未出库"
        mViewHolder.mOutTo.text = mList[position]?.to_place
        mViewHolder.mOutWeight.text = mList[position]?.weight?.toString() + mList[position]?.recyclingPrice?.unit
        mViewHolder.mOutType.text = mList[position]?.recyclingPrice?.name

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
        val mOutTime: TextView = view.findViewById(R.id.Out_Time)
        val mOutFrom: TextView = view.findViewById(R.id.Out_From)
        val mOutState: TextView = view.findViewById(R.id.Out_State)
        val mOutTo: TextView = view.findViewById(R.id.Out_To)
        val mOutWeight: TextView = view.findViewById(R.id.Out_Weight)
        val mOutType: TextView = view.findViewById(R.id.Out_Type)
    }
}
