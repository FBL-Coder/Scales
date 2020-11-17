package com.etsoft.scales.adapter.ListViewAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.etsoft.scales.R
import com.etsoft.scales.bean.AppInputBean
import com.etsoft.scales.bean.InputRecordListBean
import com.etsoft.scales.bean.RecordNotificationBean
import com.etsoft.scales.bean.UpInputFailedBean

/**
 * Author：FBL  Time： 2018/7/23.
 * 入库失败记录Adapter
 *
 */
class InputFailedRecordListViewAdapter(bean: UpInputFailedBean) : BaseAdapter() {

    var mList = bean.data as ArrayList<AppInputBean>

    fun notifyDataSetChanged(bean: UpInputFailedBean) {
        mList = bean.data as ArrayList<AppInputBean>
        super.notifyDataSetChanged()
    }


    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var mViewHolder: ViewHolder
        if (view == null || view.tag == null) {
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_listview_inputfailedrecord, parent, false)
            mViewHolder = ViewHolder(view!!)
            view.tag = mViewHolder
        } else {
            mViewHolder = (view.tag as ViewHolder?)!!
        }
        mViewHolder.Input_Record_Time.text = "打印时间： " + mList[position]?.time
        mViewHolder.Input_Record_Count.text = "回收条数： " + mList[position]?.recyclings.size.toString() + " 个"

        try {
            var type = ""
            for (i in mList[position]?.recyclings.indices) {
                type += mList[position]?.recyclings[i].typename + "、"
            }
            type = type.substring(0, type.lastIndexOf("、"))
            mViewHolder.Input_Record_Item.text = "回  收  物： $type"
        } catch (e: Exception) {
            mViewHolder.Input_Record_Item.text = "回  收  物： 提取错误"
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
        val Input_Record_Time: TextView = view.findViewById(R.id.Failed_Time)
        val Input_Record_Count: TextView = view.findViewById(R.id.Failed_Count)
        val Input_Record_Item: TextView = view.findViewById(R.id.Failed_Item)
    }
}
