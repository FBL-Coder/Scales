package com.smartdevice.aidltestdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

/**
 * @ClassName: FuncAdapter.java
 * @Description: 功能列表适配器
 * @author zkc-soft
 * Created by Administrator on 2017/3/25 10:26
 */

public class FuncAdapter extends BaseAdapter{
    private Context mContext;
    private List<HashMap<String, String>> data;
    private LayoutInflater mLayoutInflater;
    public FuncAdapter(Context mContext, List<HashMap<String, String>> data) {
        this.mContext = mContext;
        this.data = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if(data!=null)
            return data.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(data!=null)
            return data.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(R.layout.adapter_listview, null);
        TextView tv_name = (TextView) convertView.findViewById(R.id.textview_itemname);
        tv_name.setText(data.get(position).get("name"));
        return convertView;
    }
}
