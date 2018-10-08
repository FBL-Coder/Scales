package com.etsoft.scales

import android.util.Log

import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.util.Timer
import java.util.TimerTask

/**
 * Author：FBL  Time： 2018/10/7.
 */
object test1 {

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {

        Timer().schedule(object : TimerTask() {
            override fun run() {
                println("时间戳 :" + System.currentTimeMillis() + "测试次数")
                cancel()
            }
        }, 0, 2000)

        val buffer = ByteArray(1024)
        val inStream = BufferedInputStream(object : InputStream() {
            @Throws(IOException::class)
            override fun read(): Int {
                return 0
            }
        })
        while (inStream.read(buffer) != -1) {


            //下面做正常读取的操作
        }


        Integer.toHexString(255)

        val a: Byte = 62


        java.lang.Byte.toString(a)

    }


}
