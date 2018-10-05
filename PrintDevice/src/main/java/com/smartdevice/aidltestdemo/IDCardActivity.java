package com.smartdevice.aidltestdemo;

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartdevice.aidltestdemo.common.MessageType;
import com.smartdevice.aidltestdemo.util.ExecutorFactory;

/**
 * @author zkc-soft
 *         Created by Administrator on 2017/4/12 15:16
 * @ClassName: IDCardActivity.java
 * @Description: 身份信息页面
 */

public class IDCardActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_getIdentifyInfo;
    private TextView tv_sender_info;
    private ImageView img_header;
    boolean linkSuccess = false;
    private Button btnTurnOn, btnTurnOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify);
        initView();
    }

    @Override
    protected void handleStateMessage(Message message) {
        super.handleStateMessage(message);
        switch (message.what) {
            case MessageType.BaiscMessage.SEVICE_BIND_SUCCESS:
                linkSuccess = true;
//                try {
//                    mIzkcService.turnOn();
                    btn_getIdentifyInfo.setEnabled(true);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
                break;
            case MessageType.BaiscMessage.SEVICE_BIND_FAIL:
                linkSuccess = false;
                break;
            case MessageType.BaiscMessage.GET_IDETIFY_INFO_SUCCESS:
                btn_getIdentifyInfo.setEnabled(true);
                String idInfo = (String) message.obj;
                if (!TextUtils.isEmpty(idInfo)) {
                    tv_sender_info.setText(idInfo);
                    try {
                        img_header.setImageBitmap(mIzkcService.getHeader());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void initView() {
        btn_getIdentifyInfo = (Button) findViewById(R.id.btn_getIdentifyInfo);
        btn_getIdentifyInfo.setEnabled(false);
//        tv_idInfo = (TextView) findViewById(R.id.tv_idInfo);
        btn_getIdentifyInfo.setOnClickListener(this);
//        btnTurnOn = (Button) findViewById(R.id.btnTurnOn);
//        btnTurnOff = (Button) findViewById(R.id.btnTurnOff);
//        btnTurnOn.setOnClickListener(this);
//        btnTurnOff.setOnClickListener(this);
        tv_sender_info = (TextView) findViewById(R.id.tv_sender_info);
        img_header = (ImageView) findViewById(R.id.img_header);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ExecutorFactory.executeLogic(new Runnable() {
            @Override
            public void run() {
                try {
                    SystemClock.sleep(2000);
                    if(mIzkcService!=null)mIzkcService.turnOn();

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mIzkcService != null) {
            try {
                mIzkcService.turnOff();
                mIzkcService.setModuleFlag(8);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_getIdentifyInfo) {
            btn_getIdentifyInfo.setEnabled(false);
            tv_sender_info.setText("");
            if (linkSuccess) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = null;
                        try {
                            result = mIzkcService.getIdentifyInfo();
                            if (TextUtils.isEmpty(result)) return;
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        sendMessage(MessageType.BaiscMessage.GET_IDETIFY_INFO_SUCCESS, result);
                    }
                }).start();
            } else {
                Toast.makeText(this, "未连接", Toast.LENGTH_SHORT).show();
            }

//                case R.id.btnTurnOn:
//                    mIzkcService.turnOn();
//                    break;
//                case R.id.btnTurnOff:
//                    mIzkcService.turnOff();
//                    break;
        }
    }
}
