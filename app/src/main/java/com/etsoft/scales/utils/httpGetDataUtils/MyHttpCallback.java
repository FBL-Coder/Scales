package com.etsoft.scales.utils.httpGetDataUtils;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;

import com.apkfuns.logutils.LogUtils;
import com.etsoft.scales.ui.activity.BaseActivity;
import com.etsoft.scales.utils.ToastUtil;


/**
 * Author：FBL  Time： 2018/8/8.
 * 自定义请求返回监听
 */
public class MyHttpCallback extends HttpCallback {

    private BaseActivity mAppCompatActivity;

    public MyHttpCallback(BaseActivity activity) {
        mAppCompatActivity = activity;
    }

    @Override
    public void onBitmapSuccess(Bitmap bitmap) {
        super.onBitmapSuccess(bitmap);
    }

    @Override
    public void onFailure(int code, String message) {
        LogUtils.e("请求错误：code =" + code + "  msg = " + message);
        super.onFailure(code, message);
    }

    @Override
    public void onProgress(long currentTotalLen, long totalLen) {

    }

    @Override
    public void onSuccess(ResultDesc resultDesc) {
    }


}
