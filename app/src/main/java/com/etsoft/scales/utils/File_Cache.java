package com.etsoft.scales.utils;

import android.content.Context;
import android.os.Handler;

import com.apkfuns.logutils.LogUtils;
import com.etsoft.scales.app.MyApp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 作者：FBL  时间： 2016/7/11.
 * 数据本地缓存；（文件形式）
 */
public class File_Cache {

    private static Handler mHandler;
    public static int WriteOk = 1000;
    public static void setHandler(Handler handler){
        mHandler = handler;
    }

    /**
     * 数据缓存——文件缓存；
     *
     * @param data
     */
    public static void writeFileToSD(String data, String name) {
        if (name != null && !("".equals(name))) {
            try {
                FileOutputStream fos = MyApp.Companion.getMApplication().openFileOutput(name + ".txt", Context.MODE_PRIVATE);
                fos.write(data.getBytes());
                fos.close();
                LogUtils.i("本地写入成功");
            } catch (Exception e) {
                LogUtils.i("本地写入失败 = " + e);
                return;
            }
            if (mHandler!= null){
                mHandler.sendEmptyMessage(WriteOk);
            }
        }
    }

    /**
     * 数据缓存——读取缓存数据
     *
     * @return
     * @throws IOException
     */

    public static String readFile(String name) throws IOException {
        String data = "";
        if (name != null && !"".equals(name)) {
            try {
                FileInputStream fin = MyApp.Companion.getMApplication().openFileInput(name + ".txt");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fin, "utf-8"));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    data += line;
                }

            } catch (Exception e) {
                LogUtils.i("本地读取失败 = " + e);
                return "";
            }
            LogUtils.i("本地读取成功");
        }
        return data;
    }
}
