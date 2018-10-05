package com.smartdevice.aidltestdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.smartdevice.aidltestdemo.common.MessageType;
import com.smartdevice.aidltestdemo.util.ExecutorFactory;
import com.smartdevice.aidltestdemo.util.StringUtility;

public class PSAMActivityV2 extends BaseActivity implements OnClickListener{

	protected static final String TAG = "MainActivity";
	Button btn_init, btn_random, button_send, button_power_on,
			button_power_off;
	EditText editText1, editText_cmd;
	RadioGroup radioGroupCard,radioGroupPower;

	int cardLocation = 1;
	int powerValue = 2;

	long  fd =0;
	private boolean runFlag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_psamv2);
		initView();
	}

	@Override
	protected void handleStateMessage(Message message) {
		super.handleStateMessage(message);
		switch (message.what){
			case MessageType.BaiscMessage.SEVICE_BIND_SUCCESS:
				try {
					//打开当前（24）IO 口
					mIzkcService.setGPIO(24, 1);
					//打开PSAM电源
					fd=mIzkcService.Open();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case MessageType.BaiscMessage.SEVICE_BIND_FAIL:
				break;
		}
	}


	private void initView() {
		editText1 = (EditText) findViewById(R.id.editText1);
		editText_cmd = (EditText) findViewById(R.id.editText_cmd);

		button_power_on = (Button) findViewById(R.id.button_power_on);
		button_power_on.setOnClickListener(this);

		button_power_off = (Button) findViewById(R.id.button_power_off);
		button_power_off.setOnClickListener(this);

		btn_init = (Button) findViewById(R.id.button_init);
		btn_init.setOnClickListener(this);

		btn_random = (Button) findViewById(R.id.btn_random);
		btn_random.setOnClickListener(this);

		button_send = (Button) findViewById(R.id.button_send);
		button_send.setOnClickListener(this);

		radioGroupCard = (RadioGroup) findViewById(R.id.radioGroupCard);
		radioGroupCard
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						RadioButton radiobutton = (RadioButton) findViewById(group
								.getCheckedRadioButtonId());
						cardLocation = Integer.parseInt(radiobutton
								.getTag().toString());
					}
				});
		radioGroupPower = (RadioGroup) findViewById(R.id.radioGroupPower);
		radioGroupPower.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton power = (RadioButton) findViewById(group.getCheckedRadioButtonId());
				powerValue = Integer.parseInt(power.getTag().toString());
			}
			
		});
		
	}
	
	@Override
	protected void onDestroy() {
		try {
			//关闭当前PSAM卡
			mIzkcService.CloseCard2(fd, true);
			//关闭PSAM电源
			mIzkcService.Close(fd);
			//关闭当前（24）IO 口
			mIzkcService.setGPIO(24, 0);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.button_power_on) {
			openCard();

		} else if (i == R.id.button_power_off) {
			closeCard();

		} else if (i == R.id.button_init) {
			reset();

		} else if (i == R.id.button_send) {
			sendApdu();

		} else if (i == R.id.btn_random) {
			test();

		} else {
		}
		
	}

	private void sendApdu() {
		if (fd > 0) {
			String hexString = editText_cmd.getText().toString();
			byte[] bt = StringUtility.StringToByteArray(hexString);
			byte[] dataBuf;
			try {
				dataBuf = mIzkcService.CardApdu3(fd, bt, bt.length);
				if (dataBuf != null) {
					editText1.setText(StringUtility.ByteArrayToString(dataBuf,
							dataBuf.length));
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	private void test() {
		if (fd > 0) {
			byte[] btRandom = new byte[] { (byte) 0x00, (byte) 0x84,
					(byte) 0x00, (byte) 0x00, (byte) 0x08 };
			byte[] dataBuf;
			try {
				dataBuf = mIzkcService.CardApdu3(fd, btRandom, btRandom.length);
				if (dataBuf != null) {
					editText1.setText(StringUtility.ByteArrayToString(dataBuf,dataBuf.length));
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	private void reset() {
		if (fd > 0) {
			byte[] dataBuf;
			try {
				dataBuf = mIzkcService.ResetCard3(fd,cardLocation, powerValue);
				if (dataBuf != null) {
					editText1.setText(StringUtility.ByteArrayToString(dataBuf,
							dataBuf.length));
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	private void closeCard() {
		int s;
		try {
			s = mIzkcService.CloseCard2(fd, true);
			if (s != -1) {
				Log.i(TAG, "close success!");
				editText1.setText(s+"");
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void openCard() {
		int s;
		try {
			s = mIzkcService.openCard3(fd, cardLocation);
			Log.i(TAG, "psam fd=" + fd);
			if (s != -1) {
				Log.i(TAG, "open success!");
				editText1.setText(s+"");
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
}
