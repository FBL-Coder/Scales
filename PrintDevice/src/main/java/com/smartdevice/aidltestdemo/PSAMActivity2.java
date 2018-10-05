package com.smartdevice.aidltestdemo;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class PSAMActivity2 extends BaseActivity implements OnClickListener{

	protected static final String TAG = "MainActivity";
	Button btn_init, btn_random, button_send, button_power_on,
			button_power_off;
	EditText editText1, editText_cmd;
	RadioGroup radioGroupCard;

	int cardLocation = 2;

	int[] fd = new int[1];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_psam2);
		initView();
		
		
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
						cardLocation = 2 + 16 * Integer.parseInt(radiobutton
								.getTag().toString());
					}
				});
	}

	@Override
	protected void onDestroy() {
		try {
			mIzkcService.CloseCard2(fd[0], false);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onDestroy();
	}

	/**
	 * @Title:bytes2HexString
	 * @Description:字节数组转16进制字符串
	 * @param b
	 *            字节数组
	 * @return 16进制字符串
	 * @throws
	 */
	public static String bytes2HexString(byte[] b) {
		StringBuffer result = new StringBuffer();
		String hex;
		for (int i = 0; i < b.length; i++) {
			hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			result.append(hex.toUpperCase());
		}
		return result.toString();
	}

	public static String bytes2HexString(byte[] b, int len) {
		StringBuffer result = new StringBuffer();
		String hex;
		for (int i = 0; i < len; i++) {
			hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			result.append(hex.toUpperCase());
		}
		return result.toString();
	}

	/**
	 * @Title:hexString2Bytes
	 * @Description:16进制字符串转字节数组
	 * @param src
	 *            16进制字符串
	 * @return 字节数组
	 * @throws
	 */
	public static byte[] hexString2Bytes(String src) {
		int l = src.length() / 2;
		byte[] ret = new byte[l];
		for (int i = 0; i < l; i++) {
			ret[i] = (byte) Integer
					.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
		}
		return ret;
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
		if (fd[0] > 0) {
			String hexString = editText_cmd.getText().toString();
			byte[] bt = hexString2Bytes(hexString);
			byte[] dataBuf = new byte[256];
			int[] dataBufLen = new int[1];
			try {
				mIzkcService.CardApdu2(fd[0], bt, bt.length, dataBuf,
						dataBufLen);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (dataBuf != null) {
				editText1.setText(bytes2HexString(dataBuf,
						dataBufLen[0]).toString());
			}
		}
	}

	private void test() {
		if (fd[0] > 0) {
			byte[] btRandom = new byte[] { (byte) 0x00, (byte) 0x84,
					(byte) 0x00, (byte) 0x00, (byte) 0x08 };
			// //editText_cmd.setText(bytes2HexString(btRandom).toString());
			byte[] dataBuf = new byte[256];
			int[] dataBufLen = new int[1];
			try {
				mIzkcService.CardApdu2(fd[0], btRandom, btRandom.length,
						dataBuf, dataBufLen);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (dataBuf != null) {
				editText1.setText(bytes2HexString(dataBuf,
						dataBufLen[0]).toString());
			}
		}
	}

	private void closeCard() {
		int s;
		try {
			s = mIzkcService.CloseCard2(fd[0],false);
			fd[0] = 0;
			if (s != -1) {
				editText1.setText(s+"");
				Log.i(TAG, "close success!");
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void reset() {
		if (fd[0] > 0) {
			byte[] btRandom = new byte[] {};
			// //editText_cmd.setText(bytes2HexString(btRandom).toString());
			byte[] dataBuf = new byte[256];
			int[] dataBufLen = new int[1];
			// PSAMhelper.CardApdu(fd[0], btRandom,
			// btRandom.length,dataBuf, dataBufLen);
			try {
				mIzkcService.ResetCard2(fd[0], dataBuf, dataBufLen);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (dataBuf != null) {
				editText1.setText(bytes2HexString(dataBuf,
						dataBufLen[0]).toString());
			}
		}
	}

	private void openCard() {
		int s;
		try {
			s = mIzkcService.openCard2(fd, cardLocation);
			editText1.setText(s+"");
			Log.i(TAG, "psam fd=" + fd[0]);
			if (s != -1) {
				Log.i(TAG, "open success!");
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
