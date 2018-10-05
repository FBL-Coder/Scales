package com.etsoft.scales.bean;

import java.util.List;

/**
 * Author：FBL  Time： 2018/10/5.
 */
public class ServerStationBean {


    /**
     * code : 0
     * msg : OK
     * count : 2
     * data : [{"id":2,"name":"1112","functionary":"5puv0qgd7fgrbrepl3ltdq7n54","functionary_phone":"13774374601","start_work_time":"11:10:00","start_work_second":40200,"end_work_time":"11:10:00","end_work_second":40200,"address":"13774374601","companie":"111","admin_alias":"张三","admin_id":1,"create_time":"2018-09-18 11:06:46","update_time":"2018-09-28 16:59:23","deleted":0,"lat":"0.0000000","lng":"0.0000000"},{"id":1,"name":"111","functionary":"5puv0qgd7fgrbrepl3ltdq7n56","functionary_phone":"13774374601","start_work_time":"11:10:00","start_work_second":11,"end_work_time":"11:10:00","end_work_second":11,"address":"13774374601","companie":"111","admin_alias":"张三","admin_id":1,"create_time":"2018-09-18 10:00:03","update_time":"2018-09-18 11:11:34","deleted":0,"lat":"0.0000000","lng":"0.0000000"}]
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
         * name : 1112
         * functionary : 5puv0qgd7fgrbrepl3ltdq7n54
         * functionary_phone : 13774374601
         * start_work_time : 11:10:00
         * start_work_second : 40200
         * end_work_time : 11:10:00
         * end_work_second : 40200
         * address : 13774374601
         * companie : 111
         * admin_alias : 张三
         * admin_id : 1
         * create_time : 2018-09-18 11:06:46
         * update_time : 2018-09-28 16:59:23
         * deleted : 0
         * lat : 0.0000000
         * lng : 0.0000000
         */

        private int id;
        private String name;
        private String functionary;
        private String functionary_phone;
        private String start_work_time;
        private int start_work_second;
        private String end_work_time;
        private int end_work_second;
        private String address;
        private String companie;
        private String admin_alias;
        private int admin_id;
        private String create_time;
        private String update_time;
        private int deleted;
        private String lat;
        private String lng;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFunctionary() {
            return functionary;
        }

        public void setFunctionary(String functionary) {
            this.functionary = functionary;
        }

        public String getFunctionary_phone() {
            return functionary_phone;
        }

        public void setFunctionary_phone(String functionary_phone) {
            this.functionary_phone = functionary_phone;
        }

        public String getStart_work_time() {
            return start_work_time;
        }

        public void setStart_work_time(String start_work_time) {
            this.start_work_time = start_work_time;
        }

        public int getStart_work_second() {
            return start_work_second;
        }

        public void setStart_work_second(int start_work_second) {
            this.start_work_second = start_work_second;
        }

        public String getEnd_work_time() {
            return end_work_time;
        }

        public void setEnd_work_time(String end_work_time) {
            this.end_work_time = end_work_time;
        }

        public int getEnd_work_second() {
            return end_work_second;
        }

        public void setEnd_work_second(int end_work_second) {
            this.end_work_second = end_work_second;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCompanie() {
            return companie;
        }

        public void setCompanie(String companie) {
            this.companie = companie;
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

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }


    }
}
