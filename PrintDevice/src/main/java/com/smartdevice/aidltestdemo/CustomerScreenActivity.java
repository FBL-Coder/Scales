package com.smartdevice.aidltestdemo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import com.smartdevice.aidltestdemo.R;
import com.smartdevice.aidltestdemo.common.MessageType;

public class CustomerScreenActivity extends BaseActivity implements
		OnClickListener {

	private Button btn_dot, btn_rgb565, btn_rgb565_location, btn_updatelogo;
	private CheckBox cb_auto;
	private ImageView iv_diaplay;
	ThreadAuto _thAuto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cscreen);
		initView();
		initEvent();
		enableWiget(false);
	}

	@Override
	protected void handleStateMessage(Message message) {
		super.handleStateMessage(message);
		switch (message.what){
			case MessageType.BaiscMessage.SEVICE_BIND_SUCCESS:
				if(mIzkcService!=null){
					//打开客显屏 open the Customer Screen
					try {
						mIzkcService.openBackLight(1);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				enableWiget(true);
				break;
			case MessageType.BaiscMessage.SEVICE_BIND_FAIL:

				break;
		}
	}

	private void initEvent() {
		btn_dot.setOnClickListener(this);
		btn_rgb565.setOnClickListener(this);
		btn_rgb565_location.setOnClickListener(this);
		btn_updatelogo.setOnClickListener(this);
		cb_auto.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					_thAuto = new ThreadAuto();
					_thAuto.start();
				} else {
					if (_thAuto != null) {
						_thAuto.interrupt();
						_thAuto = null;
					}
				}

			}
		});
	}

	private void initView() {
		btn_dot = (Button) findViewById(R.id.btn_dot);
		btn_rgb565 = (Button) findViewById(R.id.btn_rgb565);
		btn_rgb565_location = (Button) findViewById(R.id.btn_rgb565_location);
		btn_updatelogo = (Button) findViewById(R.id.btn_updatelogo);
		cb_auto = (CheckBox) findViewById(R.id.cb_auto);
		iv_diaplay = (ImageView) findViewById(R.id.iv_display);

	}

	private void enableWiget(boolean flag){
		btn_dot.setEnabled(flag);
		btn_rgb565.setEnabled(flag);
		btn_rgb565_location.setEnabled(flag);
		btn_updatelogo.setEnabled(flag);
		cb_auto.setEnabled(false);
	}

	@Override
	protected void onStop() {
		if (_thAuto != null) {
			_thAuto.interrupt();
			_thAuto = null;
		}
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.btn_dot) {
			showDot();

		} else if (i == R.id.btn_rgb565) {
			showRGB565();

		} else if (i == R.id.btn_rgb565_location) {
			showRGb565Location();

		} else if (i == R.id.btn_updatelogo) {
			updateLogo();

		} else {
		}

	}

	private void updateLogo() {
		Bitmap bmRGB565 = BitmapFactory.decodeResource(getResources(),
				R.drawable.fj);
//		Bitmap mBitmap = copressTo100k(bmRGB565);
		try {
			if (mIzkcService.updateLogo(bmRGB565)) {
				iv_diaplay.setImageBitmap(bmRGB565);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	private Bitmap copressTo100k(Bitmap mBitmap) {
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			int options = 80;
			while (out.toByteArray().length / 1024 > 120) {
				options -= 3;
				if (options < 0) {
					break;
				}
				out.reset();
				mBitmap.compress(Bitmap.CompressFormat.JPEG, options, out);
			}
			

			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mBitmap;
	}

	private void updateLogoByPath() {
		// Bitmap bmRGB565 = BitmapFactory.decodeResource(getResources(),
		// R.drawable.minions);
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + "fj.jpg";
		if (new File(path).exists()) {
			try {
				if (mIzkcService.updateLogoByPath(path)) {
					Bitmap bmRGB565 = BitmapFactory.decodeFile(path);
					iv_diaplay.setImageBitmap(bmRGB565);
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void showRGb565Location() {
		Bitmap bmRGB565 = BitmapFactory.decodeResource(getResources(),
				R.drawable.hello);
		try {
			if (mIzkcService.showRGB565ImageCenter(bmRGB565)) {
				iv_diaplay.setImageBitmap(bmRGB565);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showRGB565() {
		Bitmap bmRGB565 = BitmapFactory.decodeResource(getResources(),
				R.drawable.minions);
		try {
			if (mIzkcService.showRGB565Image(bmRGB565)) {
				iv_diaplay.setImageBitmap(bmRGB565);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showDot() {
		if (colorIndex < colorArray.length - 1) {
			colorIndex++;
		} else {
			colorIndex = 0;
		}
		Bitmap bm = getBitmap();
		try {
			if (mIzkcService.showDotImage(colorArray[colorIndex], Color.BLACK,
					bm)) {
				iv_diaplay.setImageBitmap(bm);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private Bitmap getBitmap() {
		Bitmap bitmap = Bitmap.createBitmap(480, 272, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(80);

		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		String ss = df.format(new Date());
		canvas.drawColor(Color.WHITE);
		canvas.drawText(ss, 10, 100, paint);
		// imageView1.setImageBitmap(bitmap);
		return bitmap;
	}

	int[] colorArray = new int[] { Color.BLUE, Color.GREEN, Color.BLUE,
			Color.YELLOW, Color.WHITE };
	int colorIndex = 0;

	public class ThreadAuto extends Thread {
		@Override
		public void run() {
			super.run();
			while (!interrupted()) {
				if (colorIndex < colorArray.length - 1) {
					colorIndex++;
				} else {
					colorIndex = 0;
				}
				Bitmap bm = getBitmap();
				if (mIzkcService != null) {
					try {
						mIzkcService.showDotImage(colorArray[colorIndex], Color.BLACK, bm);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				SystemClock.sleep(2000);
			}
		}
	}
}
