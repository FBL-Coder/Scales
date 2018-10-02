package com.etsoft.scales.bean;

import java.util.List;

/**
 * Author：FBL  Time： 2018/9/30.
 */
public class RecycleListBean {


    /**
     * code : 0
     * msg : OK
     * count : 2
     * data : [{"id":2,"uuid":"111111","status":1,"name":"废玻璃","price":6,"unit":"KG","integral":0,"create_time":"1970-01-01 08:00:00","update_time":"2018-09-28 11:01:16","deleted":0,"inventory":0,"delivery_time":0,"key":"feiboli"},{"id":1,"uuid":"709907f3b68b4e214033fca4830cbd79","status":1,"name":"废铁","price":1.2,"unit":"kg","integral":1,"create_time":"2018-09-19 10:59:40","update_time":"2018-09-29 11:47:00","deleted":0,"inventory":77,"delivery_time":1538192820,"key":"feitie"}]
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
         * uuid : 111111
         * status : 1
         * name : 废玻璃
         * price : 6
         * unit : KG
         * integral : 0
         * create_time : 1970-01-01 08:00:00
         * update_time : 2018-09-28 11:01:16
         * deleted : 0
         * inventory : 0
         * delivery_time : 0
         * key : feiboli
         */

        private int id;
        private String uuid;
        private double status;
        private String name;
        private double price;
        private String unit;
        private double integral;
        private String create_time;
        private String update_time;
        private double deleted;
        private double inventory;
        private long delivery_time;
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

        public double getStatus() {
            return status;
        }

        public void setStatus(double status) {
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

        public double getIntegral() {
            return integral;
        }

        public void setIntegral(double integral) {
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

        public double getDeleted() {
            return deleted;
        }

        public void setDeleted(double deleted) {
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

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}
