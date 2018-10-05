package com.smartdevice.aidltestdemo;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.smartdevice.aidltestdemo.util.StringUtility;

public class PSAMActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener{
	
	public static final String TAG = PSAMActivity.class.getSimpleName();
	Button btn_init, btn_random, btn_send,btn_power_on,btn_power_off;
	EditText et_receiver, et_cmd;
	RadioGroup radioGroupCard, radioGroupPower;
	int cardLocation = 1;
	int powerValue = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_psam);
		initView();
		initEvent();
	}

	private void initEvent() {
		btn_init.setOnClickListener(this);
		btn_random.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		btn_power_on.setOnClickListener(this);
		btn_power_off.setOnClickListener(this);
		radioGroupCard.setOnCheckedChangeListener(this);
		radioGroupPower.setOnCheckedChangeListener(this);
	}

	private void initView() {
		btn_init = (Button) findViewById(R.id.btn_init);
		btn_random = (Button) findViewById(R.id.btn_random);
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_power_on = (Button) findViewById(R.id.btn_power_on);
		btn_power_off = (Button) findViewById(R.id.btn_power_off);
		
		radioGroupCard = (RadioGroup) findViewById(R.id.radioGroupCard);
		radioGroupPower = (RadioGroup) findViewById(R.id.radioGroupPower);
		
		et_receiver = (EditText) findViewById(R.id.et_display);
		et_cmd = (EditText) findViewById(R.id.et_cmd);
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.btn_init) {
			reset();

		} else if (i == R.id.btn_random) {
			testRandom();

		} else if (i == R.id.btn_send) {
			sendApdu();

		} else if (i == R.id.btn_power_on) {
			openPower();

		} else if (i == R.id.btn_power_off) {
			closePower();

		} else {
		}
		
	}
	
	@Override
	protected void onStop() {
		try {
			mIzkcService.CloseCard();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onStop();
	}
	
	private void openPower() {
		int s;
		try {
			s = mIzkcService.openCard(cardLocation);
			et_receiver.setText(s+"");
			if (s != 0) {
				Log.i(TAG, "open success!");
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void testRandom(){
		byte[] btRandom = new byte[] { (byte) 0x00, (byte) 0x84,
				(byte) 0x00, (byte) 0x00, (byte) 0x08 };
		byte[] dataBuf;
		try {
			dataBuf = mIzkcService.CardApdu(btRandom);
			if (dataBuf != null) {
				et_receiver.setText(StringUtility.ByteArrayToString(dataBuf, dataBuf.length));
			};
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void closePower(){
		int s;
		try {
			s = mIzkcService.CloseCard();
			if (s != 0) {
				Log.i(TAG, "close success!");
			}
			et_receiver.setText(s+"");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void reset() {	
		try {
			byte[] dataBuf=mIzkcService.ResetCard(powerValue);
			if (dataBuf != null) {
				et_receiver.setText(StringUtility.ByteArrayToString(dataBuf, dataBuf.length));
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void sendApdu(){
		String hexString = et_cmd.getText().toString();
		byte[] bt = StringUtility.StringToByteArray(hexString);
		try {
			byte[] dataBuf = mIzkcService.CardApdu(bt);
			if (dataBuf != null) {
				et_receiver.setText(StringUtility.ByteArrayToString(dataBuf, dataBuf.length));
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if(group.getId() == R.id.radioGroupCard){
			RadioButton card = (RadioButton) findViewById(group
					.getCheckedRadioButtonId());
			cardLocation = Integer.parseInt(card.getTag().toString());
			closePower();
		}else if(group.getId() == R.id.radioGroupPower){
			RadioButton power = (RadioButton) findViewById(group.getCheckedRadioButtonId());
			powerValue = Integer.parseInt(power.getTag().toString());
		}
		
	}


}
