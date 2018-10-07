//package com.etsoft.scales
//
//import java.io.IOException
//import java.io.InputStream
//import java.io.OutputStream
//import java.util.UUID
//import android.os.AsyncTask
//import android.os.Bundle
//import android.os.Handler
//import android.os.Message
//import android.app.Activity
//import android.bluetooth.BluetoothAdapter
//import android.bluetooth.BluetoothDevice
//import android.bluetooth.BluetoothSocket
//import android.util.Log
//import android.view.Menu
//import android.view.View
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//
// class text : Activity() {
//
//    //定义组件
//    var statusLabel: TextView
//     var btnConnect: Button
//    internal var btnSend: Button
//    internal var btnQuit: Button
//    internal var etReceived: EditText
//    internal var etSend: EditText
//
//    //device var
//    private var mBluetoothAdapter: BluetoothAdapter? = null
//
//    private var btSocket: BluetoothSocket? = null
//
//    private var outStream: OutputStream? = null
//
//    private var inStream: InputStream? = null
//
//
//    private var rThread: ReceiveThread? = null  //数据接收线程
//
//    //接收到的字符串
//    internal var ReceiveData = ""
//
//    internal var handler: MyHandler
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        //首先调用初始化函数
//        Init()
//        InitBluetooth()
//
//        handler = MyHandler()
//
//        btnConnect.setOnClickListener {
//            //判断蓝牙是否打开
//            if (!mBluetoothAdapter!!.isEnabled) {
//                mBluetoothAdapter!!.enable()
//            }
//            mBluetoothAdapter!!.startDiscovery()
//
//            //创建连接
//            ConnectTask().execute(address)
//        }
//
//
//        btnQuit.setOnClickListener {
//            // TODO Auto-generated method stub
//
//            if (btSocket != null) {
//                try {
//                    btSocket!!.close()
//                    btSocket = null
//                    if (rThread != null) {
//                        rThread!!.join()
//                    }
//                    statusLabel.text = "当前连接已断开"
//                    //                      etReceived.setText("");
//                } catch (e: IOException) {
//
//                    e.printStackTrace()
//                } catch (e: InterruptedException) {
//
//                    e.printStackTrace()
//                }
//
//            }
//        }
//
//        btnSend.setOnClickListener {
//            // TODO Auto-generated method stub
//            SendInfoTask().execute(etSend.text.toString())
//        }
//    }
//
//    fun Init() {
//        statusLabel = this.findViewById<View>(R.id.textView1) as TextView
//        btnConnect = this.findViewById(R.id.button1) as Button
//        btnSend = this.findViewById(R.id.button2) as Button
//        btnQuit = this.findViewById(R.id.button3) as Button
//        etSend = this.findViewById<View>(R.id.editText1) as EditText
//        etReceived = this.findViewById(R.id.editText2) as EditText
//    }
//
//    fun InitBluetooth() {
//        //得到一个蓝牙适配器
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
//        if (mBluetoothAdapter == null) {
//            Toast.makeText(this, "你的手机不支持蓝牙", Toast.LENGTH_LONG).show()
//            finish()
//            return
//        }
//
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    //连接蓝牙设备的异步任务
//    internal inner class ConnectTask : AsyncTask<String, String, String>() {
//
//
//        override fun doInBackground(vararg params: String): String {
//            // TODO Auto-generated method stub
//            val device = mBluetoothAdapter!!.getRemoteDevice(params[0])
//
//            try {
//
//                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID)
//
//
//                btSocket!!.connect()
//
//                Log.e("error", "ON RESUME: BT connection established, data transfer link open.")
//
//            } catch (e: IOException) {
//
//                try {
//                    btSocket!!.close()
//                    return "Socket 创建失败"
//
//                } catch (e2: IOException) {
//
//                    Log.e("error", "ON RESUME: Unable to close socket during connection failure", e2)
//                    return "Socket 关闭失败"
//                }
//
//            }
//
//            //取消搜索
//            mBluetoothAdapter!!.cancelDiscovery()
//
//            try {
//                outStream = btSocket!!.outputStream
//
//            } catch (e: IOException) {
//                Log.e("error", "ON RESUME: Output stream creation failed.", e)
//                return "Socket 流创建失败"
//            }
//
//
//            return "蓝牙连接正常,Socket 创建成功"
//        }
//
//        override//这个方法是在主线程中运行的，所以可以更新界面
//        fun onPostExecute(result: String) {
//            // TODO Auto-generated method stub
//
//            //连接成功则启动监听
//            rThread = ReceiveThread()
//
//            rThread!!.start()
//
//            statusLabel.text = result
//
//            super.onPostExecute(result)
//        }
//
//
//    }
//
//    //发送数据到蓝牙设备的异步任务
//    internal inner class SendInfoTask : AsyncTask<String, String, String>() {
//
//        override fun onPostExecute(result: String) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result)
//
//            statusLabel.text = result
//
//            //将发送框清空
//            etSend.setText("")
//        }
//
//        override fun doInBackground(vararg arg0: String): String {
//            // TODO Auto-generated method stub
//
//            if (btSocket == null) {
//                return "还没有创建连接"
//            }
//
//            if (arg0[0].length > 0)
//            //不是空白串
//            {
//                //String target=arg0[0];
//
//                val msgBuffer = arg0[0].toByteArray()
//
//                try {
//                    //  将msgBuffer中的数据写到outStream对象中
//                    outStream!!.write(msgBuffer)
//
//                } catch (e: IOException) {
//                    Log.e("error", "ON RESUME: Exception during write.", e)
//                    return "发送失败"
//                }
//
//            }
//
//            return "发送成功"
//        }
//
//    }
//
//
//    //从蓝牙接收信息的线程
//    internal inner class ReceiveThread : Thread() {
//
//        var buffer = ""
//
//        override fun run() {
//
//            while (btSocket != null) {
//                //定义一个存储空间buff
//                val buff = ByteArray(1024)
//                try {
//                    inStream = btSocket!!.inputStream
//                    println("waitting for instream")
//                    inStream!!.read(buff) //读取数据存储在buff数组中
//                    //                        System.out.println("buff receive :"+buff.length);
//
//
//                    processBuffer(buff, 1024)
//
//                    //System.out.println("receive content:"+ReceiveData);
//                } catch (e: IOException) {
//
//                    e.printStackTrace()
//                }
//
//            }
//        }
//
//        private fun processBuffer(buff: ByteArray, size: Int) {
//            var length = 0
//            for (i in 0 until size) {
//                if (buff[i] > '\u0000'.toByte()) {
//                    length++
//                } else {
//                    break
//                }
//            }
//
//            //          System.out.println("receive fragment size:"+length);
//
//            val newbuff = ByteArray(length)  //newbuff字节数组，用于存放真正接收到的数据
//
//            for (j in 0 until length) {
//                newbuff[j] = buff[j]
//            }
//
//            ReceiveData = ReceiveData + String(newbuff)
//            Log.e("Data", ReceiveData)
//            //          System.out.println("result :"+ReceiveData);
//            val msg = Message.obtain()
//            msg.what = 1
//            handler.sendMessage(msg)  //发送消息:系统会自动调用handleMessage( )方法来处理消息
//
//        }
//
//    }
//
//
//    //更新界面的Handler类
//    internal inner class MyHandler : Handler() {
//
//        override fun handleMessage(msg: Message) {
//
//            when (msg.what) {
//                1 -> etReceived.setText(ReceiveData)
//            }
//        }
//    }
//
//    override fun onDestroy() {
//        // TODO Auto-generated method stub
//        super.onDestroy()
//
//        try {
//            if (rThread != null) {
//
//                btSocket!!.close()
//                btSocket = null
//
//                rThread!!.join()
//            }
//
//            this.finish()
//
//        } catch (e: IOException) {
//            // TODO Auto-generated catch block
//            e.printStackTrace()
//        } catch (e: InterruptedException) {
//            // TODO Auto-generated catch block
//            e.printStackTrace()
//        }
//
//    }
//
//
//        //这条是蓝牙串口通用的UUID，不要更改
//        private val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
//
//        private val address = "00:15:83:45:68:5A" // <==要连接的目标蓝牙设备MAC地址
//
//}
