package com.smartdevice.aidltestdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.smartdevice.aidltestdemo.common.MessageType;
import com.smartdevice.aidltestdemo.util.SystemUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity {

	private ListView listView;
	List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
	public static String PRODUCT_NAME = "product_name";
	private String product_name;
	private boolean runFlag = true;
	private TextView tv_device_model;
	FuncAdapter mFuncAdapter = null;
	ScreenOnOffReceiver mReceiver = null;
	private boolean testFlag = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.listView1);
		tv_device_model = (TextView) findViewById(R.id.tv_device_model);
		product_name = getIntent().getStringExtra(PRODUCT_NAME);
		tv_device_model.setText(R.string.without_get_device_model);
//		mReceiver = new ScreenOnOffReceiver();
//		IntentFilter screenStatusIF = new IntentFilter();
//		screenStatusIF.addAction(Intent.ACTION_SCREEN_ON);
//		screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF);
//		registerReceiver(mReceiver, screenStatusIF);
		mFuncAdapter = new FuncAdapter(MainActivity.this, data);
		listView.setAdapter(mFuncAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = null;
				String idStr = data.get(position).get("id");
				if("printer".equals(idStr)){
					intent = new Intent(MainActivity.this ,
							PrinterActivity.class);
					intent.putExtra(BaseActivity.MODULE_FLAG, 0);
				}else if("Scanner".equals(idStr)) {
					intent = new Intent(MainActivity.this,
							ScannerActivity.class);
					intent.putExtra(BaseActivity.MODULE_FLAG, 4);
				}else if("psam".equals(idStr)){
					if(DEVICE_MODEL==3502||DEVICE_MODEL == 900){
						intent = new Intent(MainActivity.this, PSAMActivity.class);
					}else{
						intent = new Intent(MainActivity.this, PSAMActivity2.class);
					}
					intent.putExtra(BaseActivity.MODULE_FLAG, 1);

				}else if("psamv2".equals(idStr)){
					intent = new Intent(MainActivity.this, PSAMActivityV2.class);
					intent.putExtra(BaseActivity.MODULE_FLAG, 1);

				}else if("magneticcard".equals(idStr)){
					intent = new Intent(MainActivity.this,
							ManeticCardActivity.class);
					intent.putExtra(BaseActivity.MODULE_FLAG, 2);

				}else if("cscreen".equals(idStr)){
					intent = new Intent(MainActivity.this,	CustomerScreenActivity.class);
					intent.putExtra(BaseActivity.MODULE_FLAG, 3);

				}else if("nfc".equals(idStr)){
					intent = new Intent(MainActivity.this,NfcActivity.class);

				}else if("serialport".equals(idStr)){
//					intent = new Intent(MainActivity.this,
//							SerialPortActivity.class);
//					intent.putExtra(BaseActivity.MODULE_FLAG, 7);
					SystemUtil.startActivityForName(MainActivity.this, "com.zkc.serialdebugtools.SerialPortActivity");
				}else if("idcard".equals(idStr)){
					intent = new Intent(MainActivity.this,
							IDCardActivity.class);
					intent.putExtra(BaseActivity.MODULE_FLAG, 5);
				}

				if (intent != null) {
					startActivity(intent);
				}

			}
		});
//		if(DEVICE_MODEL==3502){
			BaseActivity.module_flag = 8;
//		}
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void handleStateMessage(Message message) {
		super.handleStateMessage(message);
		switch (message.what){
			case MessageType.BaiscMessage.SEVICE_BIND_SUCCESS:
				tv_device_model.setText(getString(R.string.device_model) + DEVICE_MODEL);
				initData(DEVICE_MODEL);
				mFuncAdapter.notifyDataSetChanged();
				break;
			case MessageType.BaiscMessage.SEVICE_BIND_FAIL:
				break;
		}
	}

	@Override
	protected void onDestroy() {
		try {
			if(mIzkcService!=null)mIzkcService.setModuleFlag(9);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onDestroy();
	}

	private void initData(int product_name) {

		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put("id", "printer");
		map1.put("name", "打印机 printer");

		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put("id", "Scanner");
		map2.put("name", "扫描-扫描模块 Scanner");

		HashMap<String, String> map3 = new HashMap<String, String>();
		map3.put("id", "camerascanner");
		map3.put("name", "扫描-摄像头 camera scan");

		HashMap<String, String> map4 = new HashMap<String, String>();
		map4.put("id", "psam");
		map4.put("name", "接触式PSAM卡 PSAM Card");
		
		HashMap<String, String> map5 = new HashMap<String, String>();
		map5.put("id", "psamv2");
		map5.put("name", "接触式PSAM卡V2协议 PSAM Card V2");

		HashMap<String, String> map6 = new HashMap<String, String>();
		map6.put("id", "magneticcard");
		map6.put("name", "磁条卡(I2C) Magnetic card");

		HashMap<String, String> map7 = new HashMap<String, String>();
		map7.put("id", "cscreen");
		map7.put("name", "客显屏幕 Customer Screen");
		
		HashMap<String, String> map8 = new HashMap<String, String>();
		map8.put("id", "nfc");
		map8.put("name", "NFC测试 NFC Test");

		HashMap<String, String> map9 = new HashMap<String, String>();
		map9.put("id", "serialport");
		map9.put("name", "串口工具 Serialport tools");
		HashMap<String, String> map11 = new HashMap<String, String>();
		map11.put("id", "idcard");
		map11.put("name", "二代身份证模块 ID Card");
		switch (product_name){
			case 3501:
			case 3503:
				data.add(map2);
				data.add(map3);
				data.add(map4);
				data.add(map5);
				data.add(map8);
				break;
			case 3502:
				data.add(map1);
				data.add(map2);
				data.add(map3);
				data.add(map4);
				data.add(map5);
				data.add(map6);
				data.add(map8);
				data.add(map11);
				break;
			case 3504:
				data.add(map1);
				data.add(map2);
				data.add(map3);
				data.add(map4);
				data.add(map5);
				data.add(map8);
				data.add(map11);
				break;
			case 3505:
			case 3506:
				data.add(map1);
				data.add(map2);
				data.add(map3);
				data.add(map4);
				data.add(map5);
				data.add(map8);
				break;
			case 701:
				data.add(map1);
				data.add(map3);
				data.add(map4);
				data.add(map5);
				data.add(map6);
				data.add(map8);
				break;
			case 800:
				data.add(map1);
				data.add(map3);
				data.add(map4);
				data.add(map5);
				data.add(map6);
				data.add(map7);
				data.add(map8);
				data.add(map11);
				break;
			case 900:
			case 5501:
				data.add(map1);
				data.add(map3);
				data.add(map4);
				data.add(map5);
				data.add(map7);
				data.add(map8);
				break;
			case 888:
				data.add(map1);
				data.add(map2);
				data.add(map3);
				data.add(map4);
				data.add(map5);
				data.add(map7);
				data.add(map8);
				data.add(map11);
				break;
		}
		if(testFlag)data.add(map9);
	}


	public class ScreenOnOffReceiver extends BroadcastReceiver {

		private static final String TAG = "ScreenOnOffReceiver";
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("android.intent.action.SCREEN_ON")) {

			}
		}
	}
}
