package com.etsoft.scales.bean;

/**
 * Author：FBL  Time： 2018/10/11.
 */
public class NotificationInfoBean {


    /**
     * code : 0
     * msg : OK
     * count : 1
     * data : {"id":1,"title":"1,2","uuid":"3b71ab66f19dd59f41cc29992880f67e","release_time":"2018-09-28 11:39:07","admin_alias":"张三","admin_id":1,"type":1,"terminal_type":2,"content":"01111","create_time":"2018-09-19 09:15:38","update_time":"2018-09-19 09:30:54","deleted":0}
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
         * id : 1
         * title : 1,2
         * uuid : 3b71ab66f19dd59f41cc29992880f67e
         * release_time : 2018-09-28 11:39:07
         * admin_alias : 张三
         * admin_id : 1
         * type : 1
         * terminal_type : 2
         * content : 01111
         * create_time : 2018-09-19 09:15:38
         * update_time : 2018-09-19 09:30:54
         * deleted : 0
         */

        private int id;
        private String title;
        private String uuid;
        private String release_time;
        private String admin_alias;
        private int admin_id;
        private int type;
        private int terminal_type;
        private String content;
        private String create_time;
        private String update_time;
        private int deleted;

        public int getId() {
            return id;
        }

        public void setId(int id) {
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

        public String getRelease_time() {
            return release_time;
        }

        public void setRelease_time(String release_time) {
            this.release_time = release_time;
        }

        public String getAdmin_alias() {
            return admin_alias;
        }

        public void setAdmin_alias(String admin_alias) {
            this.admin_alias = admin_alias;
        }

        public int getAdmin_id() {
            return admin_id;
        }

        public void setAdmin_id(int admin_id) {
            this.admin_id = admin_id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getTerminal_type() {
            return terminal_type;
        }

        public void setTerminal_type(int terminal_type) {
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

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }
    }
}
