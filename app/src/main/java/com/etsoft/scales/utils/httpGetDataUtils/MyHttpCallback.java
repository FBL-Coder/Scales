package com.etsoft.scales.utils.httpGetDataUtils;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;

import com.etsoft.scales.utils.ToastUtil;

import okhttp3.Call;


/**
 * Author：FBL  Time： 2018/8/8.
 * 自定义请求返回监听
 */
public class MyHttpCallback extends HttpCallback {

    private AppCompatActivity mAppCompatActivity;

    public MyHttpCallback(AppCompatActivity activity) {
        mAppCompatActivity = activity;
    }

    @Override
    public void onBitmapSuccess(Bitmap bitmap) {
        super.onBitmapSuccess(bitmap);
    }

    @Override
    public void onFailure(int code, String message) {
        super.onFailure(code, message);
    }

    @Override
    public void onProgress(long currentTotalLen, long totalLen) {

    }

    @Override
    public void onSuccess(ResultDesc resultDesc) {
    }



}
