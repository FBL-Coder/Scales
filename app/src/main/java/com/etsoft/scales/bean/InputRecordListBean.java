package com.etsoft.scales.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Author：FBL  Time： 2018/9/29.
 */
public class InputRecordListBean implements Serializable {


    /**
     * code : 0
     * msg : OK
     * count : 8
     * data : [{"id":1,"user_id":1,"user_name":"123456","user_phone":"13","service_point_id":1,"staff_id":1,"weight":1,"recycling_price_id":1,"recycling_price":1,"pay_money":6,"pay_type":1,"integral":600,"create_time":"2018-09-21 09:06:56","update_time":"2018-09-21 10:59:38","unit_price":1.2,"date":"2018:09:21","recyclingPrice":{"id":1,"uuid":"709907f3b68b4e214033fca4830cbd79","status":1,"name":"废铁","price":1.2,"unit":"kg","integral":1,"create_time":"2018-09-19 10:59:40","update_time":"2018-09-29 11:47:00","deleted":0,"inventory":77,"delivery_time":1538192820},"servicePoint":{"id":1,"name":"111","functionary":"5puv0qgd7fgrbrepl3ltdq7n56","functionary_phone":"13774374601","start_work_time":"11:10:00","start_work_second":11,"end_work_time":"11:10:00","end_work_second":11,"address":"13774374601","companie":"111","admin_alias":"张三","admin_id":1,"create_time":"2018-09-18 10:00:03","update_time":"2018-09-18 11:11:34","deleted":0,"lat":"0.0000000","lng":"0.0000000"}},{"id":2,"user_id":1,"user_name":"123456","user_phone":"13","service_point_id":1,"staff_id":1,"weight":0.5,"recycling_price_id":1,"recycling_price":1,"pay_money":0.6,"pay_type":1,"integral":60,"create_time":"2018-09-21 09:15:24","update_time":"2018-09-21 09:15:24","unit_price":1.2,"date":"2018:09:21","recyclingPrice":{"id":1,"uuid":"709907f3b68b4e214033fca4830cbd79","status":1,"name":"废铁","price":1.2,"unit":"kg","integral":1,"create_time":"2018-09-19 10:59:40","update_time":"2018-09-29 11:47:00","deleted":0,"inventory":77,"delivery_time":1538192820},
     * "servicePoint":{"id":1,"name":"111","functionary":"5puv0qgd7fgrbrepl3ltdq7n56","functionary_phone":"13774374601","start_work_time":"11:10:00","start_work_second":11,"end_work_time":"11:10:00","end_work_second":11,"address":"13774374601","companie":"111","admin_alias":"张三","admin_id":1,"create_time":"2018-09-18 10:00:03","update_time":"2018-09-18 11:11:34","deleted":0,"lat":"0.0000000","lng":"0.0000000"}}]
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
         * user_id : 1
         * user_name : 123456
         * user_phone : 13
         * service_point_id : 1
         * staff_id : 1
         * weight : 1
         * recycling_price_id : 1
         * recycling_price : 1
         * pay_money : 6
         * pay_type : 1
         * integral : 600
         * create_time : 2018-09-21 09:06:56
         * update_time : 2018-09-21 10:59:38
         * unit_price : 1.2
         * date : 2018:09:21
         * recyclingPrice : {"id":1,"uuid":"709907f3b68b4e214033fca4830cbd79","status":1,"name":"废铁","price":1.2,"unit":"kg","integral":1,"create_time":"2018-09-19 10:59:40","update_time":"2018-09-29 11:47:00","deleted":0,"inventory":77,"delivery_time":1538192820}
         * servicePodouble : {"id":1,"name":"111","functionary":"5puv0qgd7fgrbrepl3ltdq7n56","functionary_phone":"13774374601","start_work_time":"11:10:00","start_work_second":11,"end_work_time":"11:10:00","end_work_second":11,"address":"13774374601","companie":"111","admin_alias":"张三","admin_id":1,"create_time":"2018-09-18 10:00:03","update_time":"2018-09-18 11:11:34","deleted":0,"lat":"0.0000000","lng":"0.0000000"}
         */

        private double id;
        private double user_id;
        private String user_name;
        private String user_phone;
        private double service_point_id;
        private double staff_id;
        private double weight;
        private double recycling_price_id;
        private double recycling_price;
        private double pay_money;
        private int  pay_type;
        private double integral;
        private String create_time;
        private String update_time;
        private double unit_price;
        private String date;
        private RecyclingPriceBean recyclingPrice;
        private ServicePointBean servicePoint;

        public double getId() {
            return id;
        }

        public void setId(double id) {
            this.id = id;
        }

        public double getUser_id() {
            return user_id;
        }

        public void setUser_id(double user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_phone() {
            return user_phone;
        }

        public void setUser_phone(String user_phone) {
            this.user_phone = user_phone;
        }

        public double getService_point_id() {
            return service_point_id;
        }

        public void setService_point_id(double service_point_id) {
            this.service_point_id = service_point_id;
        }

        public double getStaff_id() {
            return staff_id;
        }

        public void setStaff_id(double staff_id) {
            this.staff_id = staff_id;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public double getRecycling_price_id() {
            return recycling_price_id;
        }

        public void setRecycling_price_id(double recycling_price_id) {
            this.recycling_price_id = recycling_price_id;
        }

        public double getRecycling_price() {
            return recycling_price;
        }

        public void setRecycling_price(double recycling_price) {
            this.recycling_price = recycling_price;
        }

        public double getPay_money() {
            return pay_money;
        }

        public void setPay_money(double pay_money) {
            this.pay_money = pay_money;
        }

        public int getPay_type() {
            return pay_type;
        }

        public void setPay_type(int pay_type) {
            this.pay_type = pay_type;
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

        public double getUnit_price() {
            return unit_price;
        }

        public void setUnit_price(double unit_price) {
            this.unit_price = unit_price;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public RecyclingPriceBean getRecyclingPrice() {
            return recyclingPrice;
        }

        public void setRecyclingPrice(RecyclingPriceBean recyclingPrice) {
            this.recyclingPrice = recyclingPrice;
        }

        public ServicePointBean getServicePoint() {
            return servicePoint;
        }

        public void setServicePoint(ServicePointBean servicePoint) {
            this.servicePoint = servicePoint;
        }

        public static class RecyclingPriceBean implements Serializable{
            /**
             * id : 1
             * uuid : 709907f3b68b4e214033fca4830cbd79
             * status : 1
             * name : 废铁
             * price : 1.2
             * unit : kg
             * integral : 1
             * create_time : 2018-09-19 10:59:40
             * update_time : 2018-09-29 11:47:00
             * deleted : 0
             * inventory : 77
             * delivery_time : 1538192820
             */

            private double id;
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
        }

        public static class ServicePointBean implements Serializable{
            /**
             * id : 1
             * name : 111
             * functionary : 5puv0qgd7fgrbrepl3ltdq7n56
             * functionary_phone : 13774374601
             * start_work_time : 11:10:00
             * start_work_second : 11
             * end_work_time : 11:10:00
             * end_work_second : 11
             * address : 13774374601
             * companie : 111
             * admin_alias : 张三
             * admin_id : 1
             * create_time : 2018-09-18 10:00:03
             * update_time : 2018-09-18 11:11:34
             * deleted : 0
             * lat : 0.0000000
             * lng : 0.0000000
             */

            private double id;
            private String name;
            private String functionary;
            private String functionary_phone;
            private String start_work_time;
            private double start_work_second;
            private String end_work_time;
            private double end_work_second;
            private String address;
            private String companie;
            private String admin_alias;
            private double admin_id;
            private String create_time;
            private String update_time;
            private double deleted;
            private String lat;
            private String lng;

            public double getId() {
                return id;
            }

            public void setId(double id) {
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

            public double getStart_work_second() {
                return start_work_second;
            }

            public void setStart_work_second(double start_work_second) {
                this.start_work_second = start_work_second;
            }

            public String getEnd_work_time() {
                return end_work_time;
            }

            public void setEnd_work_time(String end_work_time) {
                this.end_work_time = end_work_time;
            }

            public double getEnd_work_second() {
                return end_work_second;
            }

            public void setEnd_work_second(double end_work_second) {
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

            public double getAdmin_id() {
                return admin_id;
            }

            public void setAdmin_id(double admin_id) {
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

            public double getDeleted() {
                return deleted;
            }

            public void setDeleted(double deleted) {
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
}
