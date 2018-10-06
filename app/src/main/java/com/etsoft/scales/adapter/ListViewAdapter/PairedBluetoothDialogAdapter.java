package com.etsoft.scales.adapter.ListViewAdapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.etsoft.scales.R;
import com.etsoft.scales.bean.Blue_OK_Bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author：FBL  Time： 2018/10/6.
 */
public class PairedBluetoothDialogAdapter extends BaseAdapter {
    public static final String TAG = "ListViewAdapter";
    private Context context;
    private List<Blue_OK_Bean> arrayList;

    public PairedBluetoothDialogAdapter(Context context, ArrayList<Blue_OK_Bean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public void notifyDataSetChanged(ArrayList<Blue_OK_Bean> arrayList) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = LayoutInflater.from(context);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.adapter_listview_ss_bluebooth, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.Text1);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.Text2);
            convertView.setTag(holder);
        } else {
            LogUtils.d("not_null " + position);
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText("MAC：" + arrayList.get(position).mac);
        holder.tvAddress.setText("Name：" + arrayList.get(position).name);
        return convertView;
    }

    static class ViewHolder {
        public TextView tvName;
        public TextView tvAddress;
    }
}

