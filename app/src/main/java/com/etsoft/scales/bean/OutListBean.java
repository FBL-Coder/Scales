package com.etsoft.scales.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Author：FBL  Time： 2018/9/28.
 */
public class OutListBean implements Serializable{

    /**
     * code : 0
     * msg : OK
     * count : 5
     * data : [{"id":1,"to_place":"1dsadas","admin_id":1,"admin_alias":"张三","recycling_price_id":1,"recycling_price_name":"1,2","weight":10,"create_time":"2018-09-26 13:35:13","update_time":"2018-09-26 14:28:01","deleted":1,"delivery_time":1537942672,"staff_id":1,"staff_name":null,"recyclingPrice":{"id":1,"uuid":"709907f3b68b4e214033fca4830cbd79","status":1,"name":"废铁","price":1.2,"unit":"kg","doubleegral":1,"create_time":"2018-09-19 10:59:40","update_time":"2018-09-28 11:04:01","deleted":0,"inventory":79,"delivery_time":1538103841}},{"id":3,"to_place":"XXXX","admin_id":null,"admin_alias":null,"recycling_price_id":1,"recycling_price_name":"废铁","weight":10,"create_time":"2018-09-28 11:01:28","update_time":"2018-09-28 11:01:28","deleted":0,"delivery_time":1538101537,"staff_id":1,"staff_name":"mucc002","recyclingPrice":{"id":1,"uuid":"709907f3b68b4e214033fca4830cbd79","status":1,"name":"废铁","price":1.2,"unit":"kg","doubleegral":1,"create_time":"2018-09-19 10:59:40","update_time":"2018-09-28 11:04:01","deleted":0,"inventory":79,"delivery_time":1538103841}},{"id":4,"to_place":"XXXX","admin_id":null,"admin_alias":null,"recycling_price_id":1,"recycling_price_name":"废铁","weight":10,"create_time":"2018-09-28 11:02:06","update_time":"2018-09-28 11:02:06","deleted":0,"delivery_time":1538103688,"staff_id":1,"staff_name":"mucc002","recyclingPrice":{"id":1,"uuid":"709907f3b68b4e214033fca4830cbd79","status":1,"name":"废铁","price":1.2,"unit":"kg","doubleegral":1,"create_time":"2018-09-19 10:59:40","update_time":"2018-09-28 11:04:01","deleted":0,"inventory":79,"delivery_time":1538103841}},{"id":5,"to_place":"XXXX","admin_id":null,"admin_alias":null,"recycling_price_id":1,"recycling_price_name":"废铁","weight":10,"create_time":"2018-09-28 11:03:35","update_time":"2018-09-28 11:03:35","deleted":0,"delivery_time":1538103726,"staff_id":1,"staff_name":"mucc002","recyclingPrice":{"id":1,"uuid":"709907f3b68b4e214033fca4830cbd79","status":1,"name":"废铁","price":1.2,"unit":"kg","doubleegral":1,"create_time":"2018-09-19 10:59:40","update_time":"2018-09-28 11:04:01","deleted":0,"inventory":79,"delivery_time":1538103841}},{"id":6,"to_place":"XXXX","admin_id":null,"admin_alias":null,"recycling_price_id":1,"recycling_price_name":"废铁","weight":10,"create_time":"2018-09-28 11:04:01","update_time":"2018-09-28 11:04:01","deleted":0,"delivery_time":1538103815,"staff_id":1,"staff_name":"mucc002","recyclingPrice":{"id":1,"uuid":"709907f3b68b4e214033fca4830cbd79","status":1,"name":"废铁","price":1.2,"unit":"kg","doubleegral":1,"create_time":"2018-09-19 10:59:40","update_time":"2018-09-28 11:04:01","deleted":0,"inventory":79,"delivery_time":1538103841}}]
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

    public static class DataBean implements Serializable{
        /**
         * id : 1
         * to_place : 1dsadas
         * admin_id : 1
         * admin_alias : 张三
         * recycling_price_id : 1
         * recycling_price_name : 1,2
         * weight : 10
         * create_time : 2018-09-26 13:35:13
         * update_time : 2018-09-26 14:28:01
         * deleted : 1
         * delivery_time : 1537942672
         * staff_id : 1
         * staff_name : null
         * recyclingPrice : {"id":1,"uuid":"709907f3b68b4e214033fca4830cbd79","status":1,"name":"废铁","price":1.2,"unit":"kg","doubleegral":1,"create_time":"2018-09-19 10:59:40","update_time":"2018-09-28 11:04:01","deleted":0,"inventory":79,"delivery_time":1538103841}
         */

        private double id;
        private String to_place;
        private double admin_id;
        private String admin_alias;
        private double recycling_price_id;
        private String recycling_price_name;
        private double weight;
        private String create_time;
        private String update_time;
        private double deleted;
        private long delivery_time;
        private double staff_id;
        private String staff_name;
        private RecyclingPriceBean recyclingPrice;

        public double getId() {
            return id;
        }

        public void setId(double id) {
            this.id = id;
        }

        public String getTo_place() {
            return to_place;
        }

        public void setTo_place(String to_place) {
            this.to_place = to_place;
        }

        public double getAdmin_id() {
            return admin_id;
        }

        public void setAdmin_id(double admin_id) {
            this.admin_id = admin_id;
        }

        public String getAdmin_alias() {
            return admin_alias;
        }

        public void setAdmin_alias(String admin_alias) {
            this.admin_alias = admin_alias;
        }

        public double getRecycling_price_id() {
            return recycling_price_id;
        }

        public void setRecycling_price_id(double recycling_price_id) {
            this.recycling_price_id = recycling_price_id;
        }

        public String getRecycling_price_name() {
            return recycling_price_name;
        }

        public void setRecycling_price_name(String recycling_price_name) {
            this.recycling_price_name = recycling_price_name;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
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

        public long getDelivery_time() {
            return delivery_time;
        }

        public void setDelivery_time(long delivery_time) {
            this.delivery_time = delivery_time;
        }

        public double getStaff_id() {
            return staff_id;
        }

        public void setStaff_id(double staff_id) {
            this.staff_id = staff_id;
        }

        public String getStaff_name() {
            return staff_name;
        }

        public void setStaff_name(String staff_name) {
            this.staff_name = staff_name;
        }

        public RecyclingPriceBean getRecyclingPrice() {
            return recyclingPrice;
        }

        public void setRecyclingPrice(RecyclingPriceBean recyclingPrice) {
            this.recyclingPrice = recyclingPrice;
        }

        public static class RecyclingPriceBean implements Serializable{
            /**
             * id : 1
             * uuid : 709907f3b68b4e214033fca4830cbd79
             * status : 1
             * name : 废铁
             * price : 1.2
             * unit : kg
             * doubleegral : 1
             * create_time : 2018-09-19 10:59:40
             * update_time : 2018-09-28 11:04:01
             * deleted : 0
             * inventory : 79
             * delivery_time : 1538103841
             */

            private double id;
            private String uuid;
            private double status;
            private String name;
            private double price;
            private String unit;
            private double doubleegral;
            private String create_time;
            private String update_time;
            private double deleted;
            private double inventory;
            private double delivery_time;

            public double getId() {
                return id;
            }

            public void setId(double id) {
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

            public double getdoubleegral() {
                return doubleegral;
            }

            public void setdoubleegral(double doubleegral) {
                this.doubleegral = doubleegral;
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

            public double getDelivery_time() {
                return delivery_time;
            }

            public void setDelivery_time(double delivery_time) {
                this.delivery_time = delivery_time;
            }
        }
    }
}
