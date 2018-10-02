package com.etsoft.scales.utils.httpGetDataUtils;

/**
 * @Description 接口返回数据封装
 * @Author FBL  2017-5-2
 */

public class ResultDesc {

    private int error_code = -1;//返回码
    private int code = -1;//返回码
    private String reason;//返回说明
    private String result;//返回数据

    public ResultDesc(int error_code, String reason, String result) {
        this.error_code = error_code;
        this.reason = reason;
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public int getcode() {
        return code;
    }

    public void setcode(int code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return result;
    }
}
