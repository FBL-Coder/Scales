package com.etsoft.scales.adapter.ListViewAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.etsoft.scales.R
import com.etsoft.scales.bean.GiftListBean
import com.etsoft.scales.utils.ToastUtil

/**
 * Author：FBL  Time： 2018/7/23.
 * 兑换礼品listview适配器
 *
 */
class Gift_ListViewAdapter(bean: GiftListBean) : BaseAdapter() {

    var mBean = bean


    fun notifyDataSetChanged(bean: GiftListBean) {
        mBean = bean
        super.notifyDataSetChanged()
    }

    fun getGiftNum(): GiftListBean {
        return mBean!!
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var mViewHolder: ViewHolder
        if (view == null || view.tag == null) {
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_listview_gift, parent, false)
            mViewHolder = ViewHolder(view!!)
            view.tag = mViewHolder
        } else {
            mViewHolder = (view.tag as ViewHolder?)!!
        }
        mViewHolder.GiftItemNum.text = mBean.data[position].condition.toString() + "/" + mBean.data[position].inventory
        mViewHolder.GiftItemName.text = mBean.data[position].name
        mViewHolder.GiftItemReduce.setOnClickListener {

            if (mBean.data[position].condition == 0) {
                ToastUtil.showText("到底啦~~")
            } else {
                mBean.data[position].condition--
            }
            mViewHolder.GiftItemNum.text = mBean.data[position].condition.toString() + "/" + mBean.data[position].inventory
        }
        mViewHolder.GiftItemAdd.setOnClickListener {
            if (mBean.data[position].condition == mBean.data[position].inventory) {
                ToastUtil.showText("没库存了~~")
            } else {
                mBean.data[position].condition++
            }
            mViewHolder.GiftItemNum.text = mBean.data[position].condition.toString() + "/" + mBean.data[position].inventory
        }


        return view!!
    }

    override fun getItem(position: Int): Any {
        return mBean.data[position]
    }

    override fun getItemId(position: Int): Long {

        return position.toLong()
    }

    override fun getCount(): Int {
        return mBean.data.size
    }


    class ViewHolder(view: View) {
        val GiftItemName: TextView = view.findViewById(R.id.GiftItemName)
        val GiftItemReduce: TextView = view.findViewById(R.id.GiftItemReduce)
        val GiftItemNum: TextView = view.findViewById(R.id.GiftItemNum)
        val GiftItemAdd: TextView = view.findViewById(R.id.GiftItemAdd)
    }
}
