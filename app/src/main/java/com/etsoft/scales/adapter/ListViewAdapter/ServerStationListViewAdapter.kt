package com.etsoft.scales.adapter.ListViewAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.etsoft.scales.R
import com.etsoft.scales.bean.ServerStationBean

/**
 * Author：FBL  Time： 2018/7/23.
 * 服务站点列表Adapter
 *
 */
class ServerStationListViewAdapter(bean: ServerStationBean) : BaseAdapter() {

    var mList = bean.data as ArrayList<ServerStationBean.DataBean>

    fun notifyDataSetChanged(bean: ServerStationBean) {
        mList = bean.data as ArrayList<ServerStationBean.DataBean>
        super.notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var mViewHolder: ViewHolder
        if (view == null || view.tag == null) {
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_listview_serverstation_item, parent, false)
            mViewHolder = ViewHolder(view!!)
            view.tag = mViewHolder
        } else {
            mViewHolder = (view.tag as ViewHolder?)!!
        }
        mViewHolder.ServerStation_Admin.text = mList[position].admin_alias
        mViewHolder.ServerStation_address.text = mList[position].address
        mViewHolder.ServerStation_Name.text = mList[position].name
        mViewHolder.ServerStation_Time.text = mList[position].create_time
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
        val ServerStation_Admin: TextView = view.findViewById(R.id.ServerStation_Admin)
        val ServerStation_address: TextView = view.findViewById(R.id.ServerStation_address)
        val ServerStation_Name: TextView = view.findViewById(R.id.ServerStation_Name)
        val ServerStation_Time: TextView = view.findViewById(R.id.ServerStation_Time)
    }
}
