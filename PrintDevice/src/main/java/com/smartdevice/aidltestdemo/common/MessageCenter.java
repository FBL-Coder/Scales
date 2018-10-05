package com.smartdevice.aidltestdemo.common;

import android.os.Handler;
import android.os.Message;

import java.util.List;
import java.util.Vector;

public class MessageCenter {

	private List<Handler> mHandlerList = new Vector<Handler>();
	private static MessageCenter CENTER = null;

	private MessageCenter() {

	}

	public static MessageCenter getInstance() {
		if (CENTER == null) {
			CENTER = new MessageCenter();
		}
		return CENTER;
	}

	public synchronized void addHandler(Handler handler) {
		mHandlerList.add(handler);
	}

	public synchronized void removeHandler(Handler handler) {
		mHandlerList.remove(handler);
	}

	public synchronized void sendMessage(int what, Object obj) {
		Message message = new Message();
		message.obj = obj;
		message.what = what;
		sendMessage(message);
	}
	
	public synchronized void sendMessageWithPre(int what, Object obj, int arg1) {
		Message message = new Message();
		message.obj = obj;
		message.arg1 = arg1;
		message.what = what;
		sendMessage(message);
	}

	public synchronized void sendMessage(Message message) {
		for (Handler handler : mHandlerList) {
			handler.sendMessage(Message.obtain(message));
		}
	}

	public synchronized void sendEmptyMessage(int what) {
		for (Handler handler : mHandlerList) {
			handler.sendEmptyMessage(what);
		}
	}
	
}
