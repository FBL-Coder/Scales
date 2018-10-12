package com.etsoft.scales.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etsoft.scales.app.MyApp;
import com.etsoft.scales.netWorkListener.AppNetworkMgr;
import com.etsoft.scales.netWorkListener.NetBroadcastReceiver;
import com.etsoft.scales.R;


/**
 * Author：FBL  Time： 2018/3/26.
 * 自定义 TitleBar
 */

public class TitleBar extends RelativeLayout {
    private Context context;
    private ImageView back;
    private ImageView moor;
    private TextView title;
    private TextView TitleBar_Net;

    public TitleBar(Context context) {
        super(context, null, 0);
        this.context = context;
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        this.context = context;
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.titlebar_view, this);
        back = view.findViewById(R.id.back_titlebar);
        title = view.findViewById(R.id.title_titlebar);
        moor = view.findViewById(R.id.moor_titlebar);
    }

    public TextView getTitle() {
        if (title == null)
            initView();
        return title;
    }

    public ImageView getBack() {
        if (back == null)
            initView();
        return back;
    }

    public ImageView getMoor() {
        if (moor == null)
            initView();
        return moor;
    }
}
