package com.etsoft.scales.adapter.ListViewAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.etsoft.scales.R
import com.etsoft.scales.bean.RecordNotificationBean

/**
 * Author：FBL  Time： 2018/7/23.
 * Mian 出库记录Adapter
 *
 */
class RecordNotificationListViewAdapter(bean: RecordNotificationBean) : BaseAdapter() {

    var mList = bean.data as ArrayList<RecordNotificationBean.DataBean>

    fun notifyDataSetChanged(bean: RecordNotificationBean) {
        mList = bean.data as ArrayList<RecordNotificationBean.DataBean>
        super.notifyDataSetChanged()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var mViewHolder: ViewHolder
        if (view == null || view.tag == null) {
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_listview_notification_item, parent, false)
            mViewHolder = ViewHolder(view!!)
            view.tag = mViewHolder
        } else {
            mViewHolder = (view.tag as ViewHolder?)!!
        }
        mViewHolder.NotiFication_ID.text = mList[position].uuid.subSequence(mList[position].uuid.length - 4, mList[position].uuid.length)
        mViewHolder.NotiFication_Admin.text = mList[position].admin_alias
        mViewHolder.NotiFication_Title.text = mList[position].title
        mViewHolder.NotiFication_Type.text = when (mList[position].type) {
            1.0 -> "物价调整"
            2.0 -> "其他"
            else -> "未知"
        }
        mViewHolder.NotiFication_Time.text = mList[position].create_time
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
        val NotiFication_ID: TextView = view.findViewById(R.id.NotiFication_ID)
        val NotiFication_Admin: TextView = view.findViewById(R.id.NotiFication_Admin)
        val NotiFication_Title: TextView = view.findViewById(R.id.NotiFication_Title)
        val NotiFication_Type: TextView = view.findViewById(R.id.NotiFication_Type)
        val NotiFication_Time: TextView = view.findViewById(R.id.NotiFication_Time)
    }
}
