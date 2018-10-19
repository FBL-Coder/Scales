package com.etsoft.scales.bean;

import java.util.List;

/**
 * Author：FBL  Time： 2018/9/30.
 */
public class RecycleListBean {


    /**
     * code : 0
     * msg : OK
     * count : 11
     * data : [{"id":6,"uuid":"38d87922d7d38dd5326a292a3b5aa486","status":1,"name":"废木材","price":1.2,"unit":"kg","integral":15,"create_time":"2018-10-10 09:44:33","update_time":"2018-10-16 14:26:03","deleted":0,"inventory":78.35,"delivery_time":1539671163,"type":1,"recycling_classification_id":1,"note":"","before_price":0,"recycling_classification_name":"纸品类","key":"feimucai"}]
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
         * id : 6
         * uuid : 38d87922d7d38dd5326a292a3b5aa486
         * status : 1
         * name : 废木材
         * price : 1.2
         * unit : kg
         * integral : 15
         * create_time : 2018-10-10 09:44:33
         * update_time : 2018-10-16 14:26:03
         * deleted : 0
         * inventory : 78.35
         * delivery_time : 1539671163
         * type : 1
         * recycling_classification_id : 1
         * note :
         * before_price : 0
         * recycling_classification_name : 纸品类
         * key : feimucai
         */

        private int id;
        private String uuid;
        private int status;
        private String name;
        private double price;
        private String unit;
        private int integral;
        private String create_time;
        private String update_time;
        private int deleted;
        private double inventory;
        private long delivery_time;
        private int type;
        private int recycling_classification_id;
        private String note;
        private double before_price;
        private String recycling_classification_name;
        private String key;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
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

        public double getInventory() {
            return inventory;
        }

        public void setInventory(double inventory) {
            this.inventory = inventory;
        }

        public long getDelivery_time() {
            return delivery_time;
        }

        public void setDelivery_time(long delivery_time) {
            this.delivery_time = delivery_time;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getRecycling_classification_id() {
            return recycling_classification_id;
        }

        public void setRecycling_classification_id(int recycling_classification_id) {
            this.recycling_classification_id = recycling_classification_id;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public double getBefore_price() {
            return before_price;
        }

        public void setBefore_price(double before_price) {
            this.before_price = before_price;
        }

        public String getRecycling_classification_name() {
            return recycling_classification_name;
        }

        public void setRecycling_classification_name(String recycling_classification_name) {
            this.recycling_classification_name = recycling_classification_name;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}
