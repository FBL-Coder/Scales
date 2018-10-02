package com.etsoft.scales.bean;

import java.util.List;

/**
 * Author：FBL  Time： 2018/9/29.
 */
public class RecordNotificationBean {


    /**
     * code : 0
     * msg : OK
     * count : 2
     * data : [{"id":2,"title":"第2则通知","uuid":"b4f54e4beecdef2a7b5025971dda8452","release_time":1538064000,"admin_alias":"张三","admin_id":1,"type":1,"terminal_type":1,"content":"第一则通知","create_time":"2018-09-28 14:31:52","update_time":"2018-09-28 15:48:43","deleted":0},{"id":1,"title":"1,2","uuid":"3b71ab66f19dd59f41cc29992880f67e","release_time":1538105947,"admin_alias":"张三","admin_id":1,"type":1,"terminal_type":2,"content":"01111","create_time":"2018-09-19 09:15:38","update_time":"2018-09-19 09:30:54","deleted":0}]
     */

    private int code;
    private String msg;
    private int count;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 2
         * title : 第2则通知
         * uuid : b4f54e4beecdef2a7b5025971dda8452
         * release_time : 1538064000
         * admin_alias : 张三
         * admin_id : 1
         * type : 1
         * terminal_type : 1
         * content : 第一则通知
         * create_time : 2018-09-28 14:31:52
         * update_time : 2018-09-28 15:48:43
         * deleted : 0
         */

        private double id;
        private String title;
        private String uuid;
        private double release_time;
        private String admin_alias;
        private double admin_id;
        private double type;
        private double terminal_type;
        private String content;
        private String create_time;
        private String update_time;
        private double deleted;

        public double getId() {
            return id;
        }

        public void setId(double id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public double getRelease_time() {
            return release_time;
        }

        public void setRelease_time(double release_time) {
            this.release_time = release_time;
        }

        public String getAdmin_alias() {
            return admin_alias;
        }

        public void setAdmin_alias(String admin_alias) {
            this.admin_alias = admin_alias;
        }

        public double getAdmin_id() {
            return admin_id;
        }

        public void setAdmin_id(double admin_id) {
            this.admin_id = admin_id;
        }

        public double getType() {
            return type;
        }

        public void setType(double type) {
            this.type = type;
        }

        public double getTerminal_type() {
            return terminal_type;
        }

        public void setTerminal_type(double terminal_type) {
            this.terminal_type = terminal_type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public double getDeleted() {
            return deleted;
        }

        public void setDeleted(double deleted) {
            this.deleted = deleted;
        }
    }
}
