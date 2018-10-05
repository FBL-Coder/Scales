package com.etsoft.scales.adapter.ListViewAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.etsoft.scales.R
import com.etsoft.scales.bean.InputRecordListBean
import com.etsoft.scales.bean.RecordNotificationBean

/**
 * Author：FBL  Time： 2018/7/23.
 * Mian 入库记录Adapter
 *
 */
class InputRecordListViewAdapter(bean: InputRecordListBean) : BaseAdapter() {

    var mList = bean.data as ArrayList<InputRecordListBean.DataBean>

    fun notifyDataSetChanged(bean: InputRecordListBean) {
        mList = bean.data as ArrayList<InputRecordListBean.DataBean>
        super.notifyDataSetChanged()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var mViewHolder: ViewHolder
        if (view == null || view.tag == null) {
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_listview_inputrecord, parent, false)
            mViewHolder = ViewHolder(view!!)
            view.tag = mViewHolder
        } else {
            mViewHolder = (view.tag as ViewHolder?)!!
        }
        mViewHolder.Input_Record_Time.text = mList[position].create_time
        mViewHolder.Input_Record_Staff.text = mList[position].servicePoint.admin_alias.toString()
        mViewHolder.Input_Record_DanHao.text = mList[position].servicePoint.functionary
        mViewHolder.Input_Record_State.text = mList[position].recyclingPrice.status.toString()
        mViewHolder.Input_Record_Money.text = "￥"+mList[position].unit_price.toString()
        mViewHolder.Input_Record_Type.text = mList[position].recyclingPrice.name.toString()
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
        val Input_Record_Time: TextView = view.findViewById(R.id.Input_Record_Time)
        val Input_Record_Staff: TextView = view.findViewById(R.id.Input_Record_Staff)
        val Input_Record_DanHao: TextView = view.findViewById(R.id.Input_Record_DanHao)
        val Input_Record_State: TextView = view.findViewById(R.id.Input_Record_State)
        val Input_Record_Money: TextView = view.findViewById(R.id.Input_Record_Money)
        val Input_Record_Type: TextView = view.findViewById(R.id.Input_Record_Type)
    }
}
