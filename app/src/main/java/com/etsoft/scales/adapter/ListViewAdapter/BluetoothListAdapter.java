package com.etsoft.scales.adapter.ListViewAdapter;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.etsoft.scales.R;
import com.etsoft.scales.bean.BlueBoothDevicesBean;
import com.etsoft.scales.ui.activity.AddDevActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：FBL  Time： 2018/10/6.
 */
public class BluetoothListAdapter extends BaseAdapter {
    private Context context;
    private List<BlueBoothDevicesBean> arrayList;
    private int mType;
    private onConnectClick mOnConnectClick;

    public BluetoothListAdapter(Context context, ArrayList<BlueBoothDevicesBean> arrayList, int type) {
        this.context = context;
        this.arrayList = arrayList;
        this.mType = type;
    }

    public void notifyDataSetChanged(ArrayList<BlueBoothDevicesBean> arrayList) {
        this.arrayList = arrayList;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = LayoutInflater.from(context);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.adapter_listview_ss_bluebooth, null);
            holder.tvName = convertView.findViewById(R.id.Text1);
            holder.tvAddress = convertView.findViewById(R.id.Text2);
            holder.button = convertView.findViewById(R.id.button);
            convertView.setTag(holder);
        } else {
            LogUtils.d("not_null " + position);
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText("MAC：" + arrayList.get(position).MAC);
        holder.tvAddress.setText("Name：" + arrayList.get(position).Name);
        if (mType == AddDevActivity.LIST_CONNECT) {
            holder.button.setText("连接");
        } else
            holder.button.setText("配对");
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnConnectClick.onClick(v, position);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        public TextView tvName;
        public TextView tvAddress;
        public AppCompatButton button;
    }


    public void setOnConnectClick(onConnectClick onConnectClick) {
        mOnConnectClick = onConnectClick;
    }

    public interface onConnectClick {
        void onClick(View view, int position);
    }
}
