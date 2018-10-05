package com.smartdevice.aidltestdemo.common;

public class MessageType {

	public interface BaiscMessage{
		
		int BASE = 0x00000010;
		
		int SEVICE_BIND_SUCCESS = BASE + 1;
		int SEVICE_BIND_FAIL = BASE + 2;
		int GET_IDETIFY_INFO_SUCCESS = BASE + 3;
		int DETECT_PRINTER_FAIL = BASE + 4;
		int DETECT_PRINTER_SUCCESS = BASE + 5;
		int PRINTER_LINK_TIMEOUT = BASE + 6;
		int SCAN_RESULT_GET_SUCCESS = BASE + 7;
		
	}

	
}
