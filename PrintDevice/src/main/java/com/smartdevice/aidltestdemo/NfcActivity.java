package com.smartdevice.aidltestdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.smartdevice.aidltestdemo.nfc.NFCAcmdActivity;
import com.smartdevice.aidltestdemo.nfc.NFCActivity;

public class NfcActivity extends Activity implements OnClickListener {

	private Button btn_nfca, btn_nfca_cmd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc);
		initView();
	}

	private void initView() {

		btn_nfca = (Button) findViewById(R.id.btn_nfca);
		btn_nfca_cmd = (Button) findViewById(R.id.btn_nfca_cmd);
		btn_nfca.setOnClickListener(this);
		btn_nfca_cmd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		int i = v.getId();
		if (i == R.id.btn_nfca) {
			intent = new Intent(this, NFCActivity.class);

		} else if (i == R.id.btn_nfca_cmd) {
			intent = new Intent(this, NFCAcmdActivity.class);

		} else {
		}
		
		if(intent!=null){
			startActivity(intent);;
		}

	}
}
