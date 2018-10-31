package com.etsoft.scales.ui.activity

import com.etsoft.scales.utils.ToastUtil
import io.github.xudaojie.qrcodelib.CaptureActivity


/**
 * Author：FBL  Time： 2018/10/31.
 */
class SimpleCaptureActivity : CaptureActivity() {

    override fun handleResult(resultString: String?) {
        if (resultString.equals("")) {
            ToastUtil.showText("扫描失败")
            restartPreview()
        } else {
            // TODO: 16/9/17 ...
//        restartPreview();
        }
    }
}
