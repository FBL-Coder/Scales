package com.smartdevice.aidltestdemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.smartdevice.aidltestdemo.common.CmdDialog;
import com.smartdevice.aidltestdemo.common.MessageType;
import com.smartdevice.aidltestdemo.language.LanguageModel;
import com.smartdevice.aidltestdemo.language.SpinnerAdapterLanguage;
import com.smartdevice.aidltestdemo.printer.PrinterHelper;
import com.smartdevice.aidltestdemo.printer.entity.SupermakerBill;
import com.smartdevice.aidltestdemo.util.ExecutorFactory;
import com.smartdevice.aidltestdemo.util.FileUtil;
import com.smartdevice.aidltestdemo.util.PatternMatcher;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PrinterActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    public static final String TAG = "PrinterActivity";
    private Button btnQrCode, btnBarCode, btnPrintPic, btnPrint, btnUnicode, btn_wordToPic,
            btnSelectPic, btn_normal, btn_vAmaplify, btn_hAmaplify, btn_amaplify,
            btn_wordAlginLeft, btn_wordAlginMiddle, btn_wordAlginRight,
            btn_picAlginLeft, btn_picAlginMiddle, btn_picAlginRight,
            btnPrintModelOne, btnPrintModelTwo, btnPrintModelThree,
            btnPrintPicGray, btnPrintPicRaster, btnPrintUnicode1F30, btnOpenPower, btnClosePower, btnMutiThreadPrint;
    private TextView tv_printStatus, tv_printer_soft_version;
    private EditText et_printText;
    private ImageView iv_printPic;
    private Bitmap mBitmap = null;
    private CheckBox mAutoOutputPaper, cbAutoPrint;
    RadioGroup rg_fontGroup;
    private static final int REQUEST_EX = 1;
    private int fontType = 0;

    private String printTextString = "";
    private boolean checkedPicFlag = false;
    boolean isPrint = true;
    long startTimes = 0;
    long endTimes = 0;
    long timeSpace = 0;

    @Override
    protected void onStop() {
        super.onStop();
    }

    //线程运行标志 the running flag of thread
    private boolean runFlag = true;
    //打印机检测标志 the detect flag of printer
    private boolean detectFlag = false;
    //打印机连接超时时间 link timeout of printer
    private float PINTER_LINK_TIMEOUT_MAX = 30 * 1000L;
    //标签打印标记 the flag of tag print
    private boolean autoOutputPaper = false;
    String text;
    private Spinner spinnerLanguage, spinner_pic_style;
    //自动打印线程 thread of auto printer
    private AutoPrintThread mAutoPrintThread = null;
    /**
     * 图片打印类型
     * image type of print
     */
    int imageType = 0;
    final String[] imageTypeArray = new String[]{"POINT", "GRAY", "RASTER"};
    private RadioGroup radio_cut;
    DetectPrinterThread mDetectPrinterThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer);
        initView();
        initEvent();
        mDetectPrinterThread = new DetectPrinterThread();
        mDetectPrinterThread.start();
//		mReceiver = new ScreenOnOffReceiver();
//		IntentFilter screenStatusIF = new IntentFilter();
//		screenStatusIF.addAction(Intent.ACTION_SCREEN_ON);
//		screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF);
//		registerReceiver(mReceiver, screenStatusIF);
    }

    class DetectPrinterThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (runFlag) {
                float start_time = SystemClock.currentThreadTimeMillis();
                float end_time = 0;
                float time_lapse = 0;
                if (detectFlag) {
                    //检测打印是否正常 detect if printer is normal
                    try {
                        if (mIzkcService != null) {
                            String printerSoftVersion = mIzkcService.getFirmwareVersion1();
                            if (TextUtils.isEmpty(printerSoftVersion)) {
                                mIzkcService.setModuleFlag(module_flag);
                                end_time = SystemClock.currentThreadTimeMillis();
                                time_lapse = end_time - start_time;
//								enableOrDisEnableKey(false);
                                if (time_lapse > PINTER_LINK_TIMEOUT_MAX) {
                                    detectFlag = false;
                                    //打印机连接超时 printer link timeout
                                    sendEmptyMessage(MessageType.BaiscMessage.PRINTER_LINK_TIMEOUT);
                                }
                            } else {
                                //打印机连接成功 printer link success
                                sendMessage(MessageType.BaiscMessage.DETECT_PRINTER_SUCCESS, printerSoftVersion);
                                detectFlag = false;
                            }
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                }
                SystemClock.sleep(1);
            }
        }
    }

    @Override
    protected void onResume() {
        //开始检测打印机 begin to detect printer
        detectFlag = true;
        enableOrDisEnableKey(false);
        super.onResume();
    }

    @Override
    protected void handleStateMessage(Message message) {
        super.handleStateMessage(message);
        switch (message.what) {
            //服务绑定成功 service bind success
            case MessageType.BaiscMessage.SEVICE_BIND_SUCCESS:
//				Toast.makeText(this, getString(R.string.service_bind_success), Toast.LENGTH_SHORT).show();
                break;
            //服务绑定失败 service bind fail
            case MessageType.BaiscMessage.SEVICE_BIND_FAIL:
//				Toast.makeText(this, getString(R.string.service_bind_fail), Toast.LENGTH_SHORT).show();
                break;
            //打印机连接成功 printer link success
            case MessageType.BaiscMessage.DETECT_PRINTER_SUCCESS:
                String msg = (String) message.obj;
                checkPrintStateAndDisplayPrinterInfo(msg);
                break;
            //打印机连接超时 printer link timeout
            case MessageType.BaiscMessage.PRINTER_LINK_TIMEOUT:
                Toast.makeText(this, getString(R.string.printer_link_timeout), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void checkPrintStateAndDisplayPrinterInfo(String msg) {
        enableOrDisEnableKey(true);
        if (DEVICE_MODEL == 800) radio_cut.setVisibility(View.VISIBLE);
        if (!checkedPicFlag) generateBarCode();
        String status;
        String aidlServiceVersion;
        try {
            mIzkcService.getPrinterStatus();
            status = mIzkcService.getPrinterStatus();
            tv_printStatus.setText(status);
            aidlServiceVersion = mIzkcService.getServiceVersion();
            tv_printer_soft_version.setText(msg + "\nAIDL Service Version:" + aidlServiceVersion);
            //打印自检信息
//			mIzkcService.printerSelfChecking();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initView() {
        btnBarCode = (Button) findViewById(R.id.btnBarCode);
        btnBarCode.requestFocus();
        btnQrCode = (Button) findViewById(R.id.btnQrCode);
        btnPrintPic = (Button) findViewById(R.id.btnPrintPic);
        btnPrint = (Button) findViewById(R.id.btnPrint);
        btnUnicode = (Button) findViewById(R.id.btnUnicode);
        btn_wordToPic = (Button) findViewById(R.id.btn_wordToPic);
        btnSelectPic = (Button) findViewById(R.id.btnSelectPic);
        btn_normal = (Button) findViewById(R.id.btn_normal);
        btn_amaplify = (Button) findViewById(R.id.btn_amplify);
        btn_vAmaplify = (Button) findViewById(R.id.btn_vAmaplify);
        btn_hAmaplify = (Button) findViewById(R.id.btn_hAmplify);
        rg_fontGroup = (RadioGroup) findViewById(R.id.rg_fontGroup);
        btn_wordAlginLeft = (Button) findViewById(R.id.btn_wordAlginLeft);
        btn_wordAlginMiddle = (Button) findViewById(R.id.btn_wordAlginMiddle);
        btn_wordAlginRight = (Button) findViewById(R.id.btn_wordAlginRight);
        btn_picAlginLeft = (Button) findViewById(R.id.btn_picAlginLeft);
        btn_picAlginMiddle = (Button) findViewById(R.id.btn_picAlginMiddle);
        btn_picAlginRight = (Button) findViewById(R.id.btn_picAlginRight);

        btnPrintModelOne = (Button) findViewById(R.id.btnPrintModelOne);
        btnPrintModelTwo = (Button) findViewById(R.id.btnPrintModelTwo);
        btnPrintModelThree = (Button) findViewById(R.id.btnPrintModelThree);
        btnMutiThreadPrint = (Button) findViewById(R.id.btnMutiThreadPrint);
        btnPrintPicGray = (Button) findViewById(R.id.btnPrintPicGray);
        btnPrintPicRaster = (Button) findViewById(R.id.btnPrintPicRaster);
        btnPrintUnicode1F30 = (Button) findViewById(R.id.btnPrintUnicode1F30);
        btnOpenPower = (Button) findViewById(R.id.btnOpenPower);
        btnClosePower = (Button) findViewById(R.id.btnClosePower);
        tv_printer_soft_version = (TextView) findViewById(R.id.tv_printer_soft_version);
        spinnerLanguage = (Spinner) findViewById(R.id.spinner_language);
        spinner_pic_style = (Spinner) findViewById(R.id.spinner_pic_style);

        tv_printStatus = (TextView) findViewById(R.id.tv_printStatus);
        et_printText = (EditText) findViewById(R.id.et_printText);
        iv_printPic = (ImageView) findViewById(R.id.iv_printPic);
        printTextString = getString(R.string.test_print_text);
        et_printText.setText(printTextString);
        et_printText.setSelection(et_printText.getText().toString().length());
        mAutoOutputPaper = (CheckBox) findViewById(R.id.cb_auto_out_paper);
        radio_cut = (RadioGroup) findViewById(R.id.radio_cut);
        radio_cut.setVisibility(View.GONE);
        cbAutoPrint = (CheckBox) findViewById(R.id.cb_auto_print);
        cbAutoPrint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isPrint = true;
                    if (mAutoPrintThread != null) {
                        mAutoPrintThread.interrupt();
                        mAutoPrintThread = null;
                    }
                    mAutoPrintThread = new AutoPrintThread();
                    mAutoPrintThread.start();
                } else {
                    isPrint = false;
                }
            }
        });
        mAutoOutputPaper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    autoOutputPaper = true;
                } else {
                    autoOutputPaper = false;
                }
            }
        });
        radio_cut.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    if (mIzkcService != null) {
                        if (checkedId == R.id.radioButton_cut) {
                            mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x08, 0x08});
                            mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x07, 0x07});

                        } else if (checkedId == R.id.radioButton_cutall) {
                            mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x08, 0x15});
                            mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x07, 0x08});

                        } else {
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        SpinnerAdapterLanguage adapter = new SpinnerAdapterLanguage(this, R.layout.adapter_listview, getData());
        spinnerLanguage.setAdapter(adapter);
        spinnerLanguage.setOnItemSelectedListener(this);
        spinner_pic_style.setAdapter(new ArrayAdapter<String>(this, R.layout.adapter_listview, R.id.textview_itemname, imageTypeArray));
        spinner_pic_style.setOnItemSelectedListener(this);
        spinner_pic_style.setSelection(0);
    }

    /**
     * 构造SimpleAdapter的第二个参数，类型为List<Map<?,?>>
     *
     * @param
     * @return
     */
    private List<LanguageModel> getData() {
        Resources res = getResources();
        String[] cmdStr = res.getStringArray(R.array.language);
        List<LanguageModel> languageModelList = new ArrayList<>();
        for (int i = 0; i < cmdStr.length; i++) {
            String[] cmdArray = cmdStr[i].split(",");
            if (cmdArray.length == 3) {
                LanguageModel languageModel = new LanguageModel();
                languageModel.code = Integer.parseInt(cmdArray[0]);
                languageModel.language = cmdArray[1];
                languageModel.description = cmdArray[1] + " " + cmdArray[2];
                languageModelList.add(languageModel);
            }
        }
        return languageModelList;
    }

    private void initEvent() {
        btnBarCode.setOnClickListener(this);
        btnQrCode.setOnClickListener(this);
        btnPrintPic.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
        btnUnicode.setOnClickListener(this);
        btnSelectPic.setOnClickListener(this);
        btn_wordToPic.setOnClickListener(this);
        btn_normal.setOnClickListener(this);
        btn_vAmaplify.setOnClickListener(this);
        btn_hAmaplify.setOnClickListener(this);
        btn_amaplify.setOnClickListener(this);
        rg_fontGroup.setOnCheckedChangeListener(this);
        btn_wordAlginLeft.setOnClickListener(this);
        btn_wordAlginMiddle.setOnClickListener(this);
        btn_wordAlginRight.setOnClickListener(this);
        btn_picAlginLeft.setOnClickListener(this);
        btn_picAlginMiddle.setOnClickListener(this);
        btn_picAlginRight.setOnClickListener(this);
        btnPrintModelOne.setOnClickListener(this);
        btnPrintModelTwo.setOnClickListener(this);
        btnPrintModelThree.setOnClickListener(this);
        btnMutiThreadPrint.setOnClickListener(this);
        btnPrintUnicode1F30.setOnClickListener(this);
        btnPrintPicRaster.setOnClickListener(this);
        btnPrintPicGray.setOnClickListener(this);
        btnClosePower.setOnClickListener(this);
        btnOpenPower.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnBarCode) {
            generateBarCode();

        } else if (i == R.id.btnQrCode) {
            generateQrCode();

        } else if (i == R.id.btn_wordToPic) {
            wordToPic();

        } else if (i == R.id.btnPrintPic) {
            printPic();

        } else if (i == R.id.btnPrint) {
            printGBKText();

        } else if (i == R.id.btnUnicode) {
            printUnicode();

        } else if (i == R.id.btnSelectPic) {
            selectPic();

        } else if (i == R.id.btn_normal) {
            printVamplify(0);

        } else if (i == R.id.btn_vAmaplify) {
            printVamplify(1);

        } else if (i == R.id.btnMutiThreadPrint) {
            MutiThreadPrint();

        } else if (i == R.id.btn_hAmplify) {
            printVamplify(2);

        } else if (i == R.id.btn_amplify) {
            printVamplify(3);

        } else if (i == R.id.btn_wordAlginLeft) {
            printTextAlgin(0);

        } else if (i == R.id.btn_wordAlginMiddle) {
            printTextAlgin(1);

        } else if (i == R.id.btn_wordAlginRight) {
            printTextAlgin(2);

        } else if (i == R.id.btn_picAlginLeft) {
            printBitmapAlgin(0);

        } else if (i == R.id.btn_picAlginMiddle) {
            printBitmapAlgin(1);

        } else if (i == R.id.btn_picAlginRight) {
            printBitmapAlgin(2);

        } else if (i == R.id.btnPrintModelOne) {
            printPurcase(false, false);
            if (DEVICE_MODEL == 800) {
                try {
                    //半切
                    mIzkcService.sendRAWData("printer", new byte[]{0x0a, 0x0a, 0x1b, 0x69});
                    mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x08, 0x08});
                    mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x07, 0x07});
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        } else if (i == R.id.btnPrintModelTwo) {
            printPurcase(true, false);
            if (DEVICE_MODEL == 800) {
                try {
                    //全切
                    mIzkcService.sendRAWData("printer", new byte[]{0x0a, 0x0a, 0x1b, 0x69});
                    mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x08, 0x13});
                    mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x07, 0x07});
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        } else if (i == R.id.btnPrintModelThree) {
            printPurcase(true, true);

//            case R.id.btnSuperPrinter:
//                superPrint();
//                break;
        } else if (i == R.id.btnPrintPicGray) {
            printBitmapGray();

        } else if (i == R.id.btnPrintPicRaster) {
            printBitmapRaster();

        } else if (i == R.id.btnPrintUnicode1F30) {
            printBitmapUnicode1F30();

        } else if (i == R.id.btnOpenPower) {
            if (mIzkcService != null) try {
                mIzkcService.setModuleFlag(8);
                SystemClock.sleep(1000);
                mIzkcService.setModuleFlag(module_flag);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        } else if (i == R.id.btnClosePower) {
            if (mIzkcService != null) try {
                mIzkcService.setModuleFlag(9);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        } else {
        }
    }

    boolean run;
    private void MutiThreadPrint() {
        showStopMutiThreadPrintDialog();
        run = true;
        ExecutorFactory.executeThread(new Runnable() {
            @Override
            public void run() {
                while(run){
                   printPic();
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }


            }
        });
        ExecutorFactory.executeThread(new Runnable() {
            @Override
            public void run() {
                while(run){
                   printGBKText();
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }

            }
        });
        ExecutorFactory.executeThread(new Runnable() {
            @Override
            public void run() {
                while(run){
                    printPurcase(true, false);
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }

            }
        });
        ExecutorFactory.executeThread(new Runnable() {
            @Override
            public void run() {
                while(run){
                    printPic();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }

            }
        });

    }

    private void showStopMutiThreadPrintDialog() {
        AlertDialog.Builder builderDialog = new AlertDialog.Builder(this);
        builderDialog.setMessage("是否停止多线程打印......");
        builderDialog.setCancelable(false);
        builderDialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                run = false;
            }
        });
        builderDialog.show();

    }

    private void enableOrDisEnableKey(boolean enable) {
        btnPrint.setEnabled(enable);
        btnUnicode.setEnabled(enable);
        btnPrintPic.setEnabled(enable);
        btnPrintModelOne.setEnabled(enable);
        btnPrintModelTwo.setEnabled(enable);
        btnPrintModelThree.setEnabled(enable);
        spinner_pic_style.setEnabled(enable);
        spinnerLanguage.setEnabled(enable);
    }

    private void printBitmapUnicode1F30() {
        text = et_printText.getText().toString() + "\n";
        try {
            mIzkcService.printUnicode_1F30(text);
            if (autoOutputPaper) {
                mIzkcService.generateSpace();
            }
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

    private void printBitmapRaster() {
        try {
            if (mBitmap != null) {
                mIzkcService.printRasterImage(mBitmap);
                if (autoOutputPaper) {
                    mIzkcService.generateSpace();
                }
            }
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

    private void printBitmapGray() {
        try {
            if (mBitmap != null) {
                mIzkcService.printImageGray(mBitmap);
                if (autoOutputPaper) {
                    mIzkcService.generateSpace();
                }
            }
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

    private void superPrint() {
        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "printData.txt";
        //读取模板数据，按行保存
        File file = new File(path);
        String[] contents;
        String content = "";
        String new_content = "";
        if (file.exists()) {
            //获取文件内容
            content = FileUtil.convertCodeAndGetText(file);
            tv_printStatus.setText(content);
            Log.e("data", content);
        } else {
            Toast.makeText(this, R.string.print_mode_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }
        //匹配
        if (content.contains(PatternMatcher.SHOP_NAME)) {
            //替换
            new_content = content.replace(PatternMatcher.SHOP_NAME, "智谷电");
        }
        if (content.contains(PatternMatcher.CASHIER_NAME)) {
            new_content = new_content.replace(PatternMatcher.CASHIER_NAME, "林XX");
        }
        if (content.contains(PatternMatcher.TABLE_NAME)) {
            new_content = new_content.replace(PatternMatcher.TABLE_NAME, "15");
        }
        if (content.contains(PatternMatcher.ORDER_NAME)) {
            new_content = new_content.replace(PatternMatcher.ORDER_NAME, "576323258");
        }
        if (content.contains(PatternMatcher.ORDER_TIME)) {
            new_content = new_content.replace(PatternMatcher.ORDER_TIME, "2017年4月1日17:51:06");
        }
        if (content.contains(PatternMatcher.GOODS_NAME)) {
            new_content = new_content.replace(PatternMatcher.GOODS_NAME, "和田大枣");
        }
        if (content.contains(PatternMatcher.UNIT_PRICE)) {
            new_content = new_content.replace(PatternMatcher.UNIT_PRICE, "12￥");
        }
        if (content.contains(PatternMatcher.SUB_TOTAL)) {
            new_content = new_content.replace(PatternMatcher.SUB_TOTAL, "1200￥");
        }
        if (content.contains(PatternMatcher.GOODS_COUNT)) {
            new_content = new_content.replace(PatternMatcher.GOODS_COUNT, "100");
        }
        if (content.contains(PatternMatcher.TOTAL_PRICE)) {
            new_content = new_content.replace(PatternMatcher.TOTAL_PRICE, "1200￥");
        }
        if (content.contains(PatternMatcher.TOTAL_COUNT)) {
            new_content = new_content.replace(PatternMatcher.TOTAL_COUNT, "12");
        }
        if (content.contains(PatternMatcher.CAN_RECEIVER)) {
            new_content = new_content.replace(PatternMatcher.CAN_RECEIVER, "1200￥");
        }
        if (content.contains(PatternMatcher.PAY)) {
            new_content = new_content.replace(PatternMatcher.PAY, "1200￥");
        }
        if (content.contains(PatternMatcher.REAL_RECEIVER)) {
            new_content = new_content.replace(PatternMatcher.REAL_RECEIVER, "1200￥");
        }
        if (content.contains(PatternMatcher.CHARGE)) {
            new_content = new_content.replace(PatternMatcher.CHARGE, "0.0￥");
        }
        if (content.contains(PatternMatcher.VIP_NAME)) {
            new_content = new_content.replace(PatternMatcher.VIP_NAME, "李XX");
        }
        if (content.contains(PatternMatcher.VIP_NUMBER)) {
            new_content = new_content.replace(PatternMatcher.VIP_NUMBER, "1111111");
        }
        if (content.contains(PatternMatcher.BLANCE)) {
            new_content = new_content.replace(PatternMatcher.BLANCE, "77￥");
        }
        if (content.contains(PatternMatcher.INTEGRAL)) {
            new_content = new_content.replace(PatternMatcher.INTEGRAL, "1300");
        }
        if (content.contains(PatternMatcher.SHOP_ADDRESS)) {
            new_content = new_content.replace(PatternMatcher.SHOP_ADDRESS, "桃源居家乐福");
        }
        if (content.contains(PatternMatcher.ITEM)) {
            new_content = new_content.replace(PatternMatcher.ITEM, "");
        }
        tv_printStatus.setText(new_content);
        //打印
        try {
            mIzkcService.printGBKText(new_content);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void printPurcase(boolean hasStartPic, boolean hasEndPic) {
        SupermakerBill bill = PrinterHelper.getInstance(this).getSupermakerBill(mIzkcService, hasStartPic, hasEndPic);
        PrinterHelper.getInstance(this).printPurchaseBillModelOne(mIzkcService, bill, imageType);
    }

    private void printBitmapAlgin(int alginStyle) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zkc);
//		Bitmap bitmap1 = resizeImage(bitmap, 376, 120);
        try {
            mIzkcService.printBitmapAlgin(bitmap, 376, 120, alginStyle);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void printTextAlgin(int alginStyle) {
        String pString = "智能打印\n";
        try {
            mIzkcService.printTextAlgin(pString, 0, 1, alginStyle);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void printVamplify(int type) {
        try {
            mIzkcService.printTextWithFont(text, fontType, type);
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }


    private void printFont(int type) {
        try {
//			mIzkcService.setTypeface(type);
//			mIzkcService.printGBKText(text);
            mIzkcService.printTextWithFont(text, type, 0);
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        checkedPicFlag = true;
        if (requestCode == REQUEST_EX && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            iv_printPic.setImageURI(selectedImage);
            mBitmap = BitmapFactory.decodeFile(picturePath);
            iv_printPic.setImageBitmap(mBitmap);
            if (mBitmap.getHeight() > 384) {
                iv_printPic.setImageBitmap(resizeImage(mBitmap, 384, 384));
            }
            cursor.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        if (width >= newWidth) {
            float scaleWidth = ((float) newWidth) / width;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleWidth);
            Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                    height, matrix, true);
            return resizedBitmap;
        } else {
            Bitmap bitmap2 = Bitmap.createBitmap(newWidth, newHeight,
                    bitmap.getConfig());
            Canvas canvas = new Canvas(bitmap2);
            canvas.drawColor(Color.WHITE);

            canvas.drawBitmap(BitmapOrg, (newWidth - width) / 2, 0, null);

            return bitmap2;
        }
    }

    private void selectPic() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_EX);

    }

    private void generateQrCode() {
        try {
            mBitmap = mIzkcService.createQRCode("http://www.sznewbest.com", 384, 384);
            iv_printPic.setImageBitmap(mBitmap);
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

    private void generateBarCode() {
        try {
            mBitmap = mIzkcService.createBarCode("4333333367", 1, 384, 120, true);
            iv_printPic.setImageBitmap(mBitmap);
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

    private void printUnicode() {
        text = et_printText.getText().toString() + "\n";
        try {
            mIzkcService.printUnicodeText(text);
            if (autoOutputPaper) {
                mIzkcService.generateSpace();
            }
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

    private void printGBKText() {
        text = et_printText.getText().toString();
        try {
//			mIzkcService.printerInit();
//			mIzkcService.sendRAWData("printer", new byte[] { 0x1E, 0x04, 0x00, (byte) 0xBF, (byte) 0xD8, (byte) 0xD6, (byte) 0xC6});
            mIzkcService.printGBKText(text);
//			mIzkcService.printGBKText("\nАа Бб Вв Гг Дд  Ее Ёё  Жж Зз Ии Йй Кк Лл Мм Нн Оо Пп Рр Сс Тт Уу Фф Хх Цц Чч Шш Щщ ъ ы ь Ээ Юю Яя\n");
            if (autoOutputPaper) {
                mIzkcService.generateSpace();
            }
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

    private void printPic() {
        try {
            if (mBitmap != null) {
                switch (imageType) {
                    case 0:
                        mIzkcService.printBitmap(mBitmap);
                        break;
                    case 1:
                        mIzkcService.printImageGray(mBitmap);
                        break;
                    case 2:
                        mIzkcService.printRasterImage(mBitmap);
                        break;
                }
                if (autoOutputPaper) {
                    mIzkcService.generateSpace();
                }
            }
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_fontOne) {
            fontType = 0;
        } else if (checkedId == R.id.rb_fontTwo) {
            fontType = 1;
        }
    }

    private void wordToPic() {
        String str = et_printText.getText().toString();
        mBitmap = Bitmap.createBitmap(384, 30, Config.ARGB_8888);
        Canvas canvas = new Canvas(mBitmap);
        canvas.drawColor(Color.WHITE);
        TextPaint textPaint = new TextPaint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(25.0F);
        StaticLayout layout = new StaticLayout(str, textPaint,
                mBitmap.getWidth(), Alignment.ALIGN_NORMAL, (float) 1.0,
                (float) 0.0, true);
        layout.draw(canvas);
        iv_printPic.setImageBitmap(mBitmap);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int i = parent.getId();
        if (i == R.id.spinner_language) {
            setPrinterLanguage(position);

        } else if (i == R.id.spinner_pic_style) {
            imageType = position;

        }
    }

    private void getOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, "发送指令");
        return super.onCreateOptionsMenu(menu);
    }

    private void showCmd() {
        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "cmd.txt";
        //读取模板数据，按行保存
        File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(PrinterActivity.this,
                    "请按规定格式将指令保存在名为cmd.txt文件中，并复制在终端根目录下",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        CmdDialog cmdDialog = new CmdDialog(this, new CmdDialog.DialogCallBack() {
            @Override
            public void submit(String cmd) {
                String cmds = cmd;
                if (mIzkcService != null) {
                    byte[] buffer = hexStringToBytes(cmds);
                    try {
                        mIzkcService.sendRAWData("printer", buffer);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        cmdDialog.show();
    }

    public static byte[] hexStringToBytes(String hexString) {
        hexString = hexString.toLowerCase();
        String[] hexStrings = hexString.split(" ");
        byte[] bytes = new byte[hexStrings.length];
        for (int i = 0; i < hexStrings.length; i++) {
            char[] hexChars = hexStrings[i].toCharArray();
            bytes[i] = (byte) (charToByte(hexChars[0]) << 4 | charToByte(hexChars[1]));
        }
        return bytes;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789abcdef".indexOf(c);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            showCmd();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setPrinterLanguage(int position) {
        LanguageModel map = (LanguageModel) spinnerLanguage.getItemAtPosition(position);
        String languageStr = map.language;
        //语言描述
        String description = map.description;
        //语言指令
        int code = map.code;
        Log.d(TAG, "onItemClick: spinner_language=" + description + "," + code);

        //发送语言切换指令
//		byte[] cmd_language=new byte[]{0x1B,0x74,0x00};
//		cmd_language[2]=(byte)code;
        try {
//            mIzkcService.sendRAWData("print", cmd_language);
            if (mIzkcService != null) mIzkcService.setPrinterLanguage(languageStr, code);
            //设置打印语言
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onDestroy() {
        //禁止打印
        if (mIzkcService != null) try {
            mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x03, 0x00,
                    (byte) 0xBF, (byte) 0xD8, (byte) 0xD6, (byte) 0xC6});
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (mAutoPrintThread != null) {
            mAutoPrintThread.interrupt();
            mAutoPrintThread = null;
        }
        if (mDetectPrinterThread != null) {
            runFlag = false;
            mDetectPrinterThread.interrupt();
            mDetectPrinterThread = null;
        }
        super.onDestroy();
    }

    private class AutoPrintThread extends Thread {
        public void run() {
            // Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),
            // R.drawable.order1);
            // Bitmap myBitmap = resizeImage(mBitmap, 384, 748);
            // Bitmap printBitmap = getBitmapPrint(myBitmap);
            while (isPrint) {
                startTimes = 0;
                endTimes = 0;
                timeSpace = 0;
                startTimes = System.currentTimeMillis();
                try {
                    if (cbAutoPrint.isChecked() && mIzkcService != null) {
                        printPurcase(false, true);
                        if (radio_cut.getCheckedRadioButtonId() == R.id.radioButton_cut) {
                            mIzkcService.sendRAWData("printer", new byte[]{0x1b, 0x6d});
                        } else if (radio_cut.getCheckedRadioButtonId() == R.id.radioButton_cutall) {
                            mIzkcService.sendRAWData("printer", new byte[]{0x1b, 0x69});
                        }
                        endTimes = System.currentTimeMillis();
                        timeSpace = Math.abs(endTimes - startTimes - 4000);
                        SystemClock.sleep(timeSpace);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
