package com.etsoft.scales.adapter.ListViewAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.etsoft.scales.bean.Information_Bean
import com.etsoft.scales.R

/**
 * Author：FBL  Time： 2018/7/23.
 * InFormation listview  adapter
 *
 */
class Inforamation_List_Adapter(list: ArrayList<Information_Bean>) : BaseAdapter() {

    var mList = list
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var mViewHolder: ViewHolder
        if (view == null) {
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_listview_inforamation, parent, false)
            mViewHolder = ViewHolder(view!!)
            view.tag = mViewHolder
        } else {
            mViewHolder = (view.tag as ViewHolder?)!!
        }
        mViewHolder.mInformation_Title_Item.text = mList[position].title!!
        mViewHolder.mInformation_Time_Item.text = mList[position].time!!

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
        val mInformation_Title_Item: TextView = view.findViewById(R.id.Information_Title_Item)
        val mInformation_Time_Item: TextView = view.findViewById(R.id.Information_Time_Item)
    }
}
