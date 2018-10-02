package com.etsoft.scales.adapter.ListViewAdapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.etsoft.scales.R
import com.etsoft.scales.bean.Input_Main_List_Bean

/**
 * Author：FBL  Time： 2018/7/23.
 * Mian 入库数据Adapter
 *
 */
class Main_Input_ListViewAdapter(list: ArrayList<Input_Main_List_Bean>) : BaseAdapter() {

    var mList = ArrayList<Input_Main_List_Bean>()

    init {
        initData(list)
    }

    private fun initData(list: ArrayList<Input_Main_List_Bean>) {
        mList.add(Input_Main_List_Bean().run {
            id = "编号"
            type = "类型"
            weight = "重量"
            unit = "单位"
            price = "单位"
            total = "总价"
            this
        })
        mList.addAll(list)
        mList.add(Input_Main_List_Bean())
    }

    fun notifyDataSetChanged(list: ArrayList<Input_Main_List_Bean>) {
        mList.clear()
        initData(list)
        super.notifyDataSetChanged()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if (position <= mList.size - 2) {
            var view = convertView
            var mViewHolder: ViewHolder
            if (view == null || view.tag == null) {
                view = LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_listview_inputmain, parent, false)
                mViewHolder = ViewHolder(view!!)
                view.tag = mViewHolder
            } else {
                mViewHolder = (view.tag as ViewHolder?)!!
            }
            mViewHolder.mInformation_Item1.text = mList[position].id
            mViewHolder.mInformation_Item2.text = mList[position].type
            mViewHolder.mInformation_Item3.text = mList[position].weight
            mViewHolder.mInformation_Item4.text = mList[position].unit
            if (position == 0) {//防止标题带符号
                mViewHolder.mInformation_Item5.text = mList[position].price
                mViewHolder.mInformation_Item6.text = mList[position].total
            } else {
                mViewHolder.mInformation_Item5.text = "￥${mList[position].price}"
                mViewHolder.mInformation_Item6.text = "￥${mList[position].total}"
                mViewHolder.mInformation_Item6.setTextColor(Color.BLACK)
            }
            return view!!
        } else {//添加尾部添加布局
            return LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_listview_inputmain_add, parent, false)
        }

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
