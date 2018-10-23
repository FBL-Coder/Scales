package com.etsoft.scales.utils.httpGetDataUtils;

import android.graphics.Bitmap;

import com.apkfuns.logutils.LogUtils;
import com.etsoft.scales.app.MyApp;
import com.etsoft.scales.netWorkListener.AppNetworkMgr;
import com.etsoft.scales.ui.activity.BaseActivity;
import com.etsoft.scales.utils.ToastUtil;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @Description OkHttp3工具类
 * @Author FBL  2017-5-2
 */
public class OkHttpUtils {

    private static OkHttpUtils mInstance;
    private static OkHttpClient mOkHttpClient;
    private static Platform mPlatform;
    private static BaseActivity mActivity;

    private OkHttpUtils(OkHttpClient okHttpClient, BaseActivity activity) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }
        mPlatform = Platform.get();
        mActivity = activity;
    }

    public static OkHttpUtils initClient(OkHttpClient okHttpClient, BaseActivity activity) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient, activity);
                }
            }
        }else {
            mActivity = activity;
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance() {
        return initClient(null, null);
    }

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static void sendFailResultCallback(final Call call, final String url, final int code, final String message, final HttpCallback callback) {
        if (callback == null) {
            return;
        }
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(code, "URL = " + url + "; 错误信息 = " + message);
            }
        });
    }

    public static void sendSuccessResultCallback(final ResultDesc result, final HttpCallback callback) {
        if (callback == null) {
            return;
        }
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(result);
            }
        });
    }

    public static void sendProgressResultCallback(final long currentTotalLen, final long totalLen, final HttpCallback callback) {
        if (callback == null) {
            return;
        }
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onProgress(currentTotalLen, totalLen);
            }
        });
    }

    public static void sendBitmapSuccessResultCallback(final Bitmap bitmap, final HttpCallback callback) {
        if (callback == null) {
            return;
        }
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onBitmapSuccess(bitmap);
            }
        });
    }

    /**--------------------    同步数据请求    --------------------**/

    /**
     * @param url      请求地址
     * @param callback 请求回调
     * @Description GET请求
     */
    public static void getSync(String url, HttpCallback callback, String tag) {
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.GET, url, null, null, tag);
        OkHttpRequest.doExecute(request, callback);
    }

    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求回调
     * @Description GET请求
     */
    public static void getSync(String url, Map<String, String> params, HttpCallback callback, String tag) {
        if (params != null && !params.isEmpty()) {
            url = OkHttpRequest.appendGetParams(url, params);
        }
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.GET, url, null, null, tag);
        OkHttpRequest.doExecute(request, callback);
    }

    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求回调
     * @Description POST请求
     */
    public static void postSync(String url, Map<String, String> params, HttpCallback callback, String tag) {
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.POST, url, params, null, tag);
        OkHttpRequest.doExecute(request, callback);
    }

    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求回调
     * @Description POST请求
     */
    public static void postSync(String url, String params, HttpCallback callback, String tag) {
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.POST, url, null, params, tag);
        OkHttpRequest.doExecute(request, callback);
    }

    /**--------------------    异步数据请求    --------------------**/

    /**
     * @param url      请求地址
     * @param callback 请求回调
     * @Description GET请求
     */
    public static void getAsyn(String url, HttpCallback callback, String tag) {

        int NETWORK = AppNetworkMgr.getNetworkState(MyApp.Companion.getMApplication().getApplicationContext());
        if (NETWORK == 0) {
            ToastUtil.showText("请检查网络连接");
            if (mActivity != null) {
                mActivity.getMLoadDialog().hide();
            }
            return;
        }

        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.GET, url, null, null, tag);
        if (request == null) {
            return;
        }
        OkHttpRequest.doEnqueue(request, callback);
    }

    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求回调
     * @Description GET请求
     */
    public static void getAsyn(String url, Map<String, String> params, HttpCallback callback, String tag) {
        if (params != null && !params.isEmpty()) {
            url = OkHttpRequest.appendGetParams(url, params);
        }

        int NETWORK = AppNetworkMgr.getNetworkState(MyApp.Companion.getMApplication().getApplicationContext());
        if (NETWORK == 0) {
            ToastUtil.showText("请检查网络连接");
            if (mActivity != null) {
                mActivity.getMLoadDialog().hide();
            }
            return;
        }

        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.GET, url, null, null, tag);
        OkHttpRequest.doEnqueue(request, callback);
    }

    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求回调
     * @Description POST请求
     */
    public static void postAsyn(String url, Map<String, String> params, HttpCallback callback, String tag) {
        int NETWORK = AppNetworkMgr.getNetworkState(MyApp.Companion.getMApplication().getApplicationContext());
        if (NETWORK == 0) {
            ToastUtil.showText("请检查网络连接");
            if (mActivity != null) {
                mActivity.getMLoadDialog().hide();
            }
            return;
        }
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.POST, url, params, null, tag);
        if (request == null) {
            return;
        }
        OkHttpRequest.doEnqueue(request, callback);
    }

    /**
     * @param url      请求地址
     * @param json     json数据格式
     * @param callback 请求回调
     * @Description POST提交JSON数据
     */
    public static void postAsyn(String url, String json, HttpCallback callback, String tag) {

        int NETWORK = AppNetworkMgr.getNetworkState(MyApp.Companion.getMApplication().getApplicationContext());
        if (NETWORK == 0) {
            ToastUtil.showText("请检查网络连接");
            if (mActivity != null) {
                mActivity.getMLoadDialog().destroyDialog();
            }
            return;
        }
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.POST, url, null, json, tag);

        OkHttpRequest.doEnqueue(request, callback);
    }

    /**--------------------    文件上传    --------------------**/

    /**
     * @param url      请求地址
     * @param file     上传文件
     * @param callback 请求回调
     * @Description 单文件上传
     */
    public static void postAsynFile(String url, File file, HttpCallback callback) {
        if (!file.exists()) {
            ToastUtil.showText("文件不存在");
            return;
        }
        Request request = OkHttpRequest.builderFileRequest(url, file, null, null, null, callback);
        OkHttpRequest.doEnqueue(request, callback);
    }

    /**
     * @param url      请求地址
     * @param pic_key  上传图片关键字（约定pic_key如“upload”作为后台接受多张图片的key）
     * @param files    上传文件集合
     * @param params   请求参数
     * @param callback 请求回调
     * @Description 多文件上传
     */
    public static void postAsynFiles(String url, String pic_key, List<File> files, Map<String, String> params, HttpCallback callback) {
        Request request = OkHttpRequest.builderFileRequest(url, null, pic_key, files, params, callback);
        OkHttpRequest.doEnqueue(request, callback);
    }

    /**--------------------    文件下载    --------------------**/

    /**
     * @param url          请求地址
     * @param destFileDir  目标文件存储的文件夹路径，如：Environment.getExternalStorageDirectory().getAbsolutePath()
     * @param destFileName 目标文件存储的文件名，如：gson-2.7.jar
     * @param callback     请求回调
     * @Description 文件下载
     */
    public void downloadAsynFile(String url, String destFileDir, String destFileName, HttpCallback callback, String tag) {
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.GET, url, null, null, tag);
        OkHttpRequest.doDownloadEnqueue(request, destFileDir, destFileName, callback);
    }

    /**
     * @param url          请求地址
     * @param destFileDir  目标文件存储的文件夹路径
     * @param destFileName 目标文件存储的文件名
     * @param params       请求参数
     * @param callback     请求回调
     * @Description 文件下载
     */
    public void downloadAsynFile(String url, String destFileDir, String destFileName, Map<String, String> params, HttpCallback callback, String tag) {
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.POST, url, params, null, tag);
        OkHttpRequest.doDownloadEnqueue(request, destFileDir, destFileName, callback);
    }

    /**--------------------    图片显示    --------------------**/

    /**
     * @param url      请求地址
     * @param callback 请求回调
     * @Description 图片显示
     */
    public static void displayAsynImage(String url, HttpCallback callback, String tag) {
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.GET, url, null, null, tag);
        OkHttpRequest.doDisplayEnqueue(request, callback);
    }

    /**--------------------    流式提交    --------------------**/

    /**
     * @param url      请求地址
     * @param content  提交内容
     * @param callback 请求回调
     * @Description 使用流的方式提交POST请求
     */
    public static void postAsynStream(String url, String content, HttpCallback callback) {
        Request request = OkHttpRequest.builderStreamRequest(url, content);
        OkHttpRequest.doEnqueue(request, callback);
    }

    /**--------------------    Websocket    --------------------**/

    /**
     * @param url 请求地址
     * @Description WebSocket协议首先会发起http请求，握手成功后，转换协议保持长连接，类似心跳
     */
    public static void websocket(String url, String tag) {
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.GET, url, null, null, tag);
        OkHttpRequest.doNewWebSocket(request);
    }

    /**
     * @param tag 请求标签
     * @Description 取消请求
     */
    public static void cancelTag(Object tag) {
        if (tag == null) {
            return;
        }
        synchronized (mOkHttpClient.dispatcher().getClass()) {
            for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                    LogUtils.i(call.request().tag() + "---网络请求取消");
                }
            }
            for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                    LogUtils.i(call.request().tag() + "---网络请求取消");
                }
            }
        }
    }
}
