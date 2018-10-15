package com.etsoft.scales.bean;

/**
 * Author：FBL  Time： 2018/10/15.
 */
public class InputOkBean {


    /**
     * code : 0
     * msg : OK
     * count : 0
     * data : {"transaction_no":"2018101509458890","company":"两网融合"}
     */

    private int code;
    private String msg;
    private int count;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * transaction_no : 2018101509458890
         * company : 两网融合
         */

        private String transaction_no;
        private String company;

        public String getTransaction_no() {
            return transaction_no;
        }

        public void setTransaction_no(String transaction_no) {
            this.transaction_no = transaction_no;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }
    }
}
