package com.etsoft.scales;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Author：FBL  Time： 2018/10/7.
 */
public class test1 {

    public static void main(String[] args) throws IOException {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("时间戳 :" + System.currentTimeMillis() + "测试次数");
                cancel();
            }
        }, 0, 2000);

        byte[] buffer = new byte[1024];
        InputStream inStream = new BufferedInputStream(new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        });
        while ((inStream.read(buffer) != -1)){


            //下面做正常读取的操作
        }
    }


}
