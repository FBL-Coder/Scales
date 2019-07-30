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
     * count : 36952
     * data : [{"id":982174,"user_id":1,"user_name":"默认","user_phone":"17611110000","service_point_id":22,"staff_id":3,"weight":12,"recycling_price_id":74,"recycling_price":{"id":74,"uuid":"481a70e4abe26ac7596297d9638c1b15","status":1,"name":"自行车（大）","price":7,"unit":"辆","integral":0,"create_time":"2018-10-23 12:50:23","update_time":"2019-07-29 16:03:23","deleted":0,"inventory":765.7,"delivery_time":0,"type":2,"recycling_classification_id":4,"note":null,"before_price":7,"sort":99,"group_id":3},"pay_money":84,"pay_type":1,"integral":8400,"create_time":"2019-07-29 15:44:00","update_time":"2019-07-29 15:44:00","unit_price":7,"date":"2019:07:29","transaction_no":"2019072986258724","deleted":0,"practical_pay_money":84,"number":1,"service_point":{"id":22,"name":"贝港六区","functionary":"张凤","functionary_phone":"17317390767","start_work_time":"13:30:00","start_work_second":48600,"end_work_time":"15:30:00","end_work_second":55800,"address":"贝港南区694号东侧","companie":"载盛物资","admin_alias":"胡辉","admin_id":1,"create_time":"2018-10-29 11:01:56","update_time":"2018-10-29 15:19:17","deleted":0,"lat":"30.9170980","lng":"121.4515020","type":1,"sort":8}},{"id":982148,"user_id":1,"user_name":"默认","user_phone":"17611110000","service_point_id":10,"staff_id":3,"weight":7.5,"recycling_price_id":85,"recycling_price":{"id":85,"uuid":"2feca92c2a69558df8ee36cf086bacc0","status":1,"name":"纸板1.2","price":1.2,"unit":"KG","integral":0,"create_time":"2018-11-23 12:02:16","update_time":"2019-06-28 09:30:34","deleted":0,"inventory":100004296,"delivery_time":0,"type":1,"recycling_classification_id":1,"note":null,"before_price":1.2,"sort":99,"group_id":0},"pay_money":9,"pay_type":1,"integral":900,"create_time":"2019-06-28 09:21:00","update_time":"2019-06-28 09:21:00","unit_price":1.2,"date":"2019:06:28","transaction_no":"2019062884908334","deleted":0,"practical_pay_money":9,"number":1,"service_point":{"id":10,"name":"贝港八区（申凡苑）","functionary":"张凤","functionary_phone":"17317390767","start_work_time":"08:30:00","start_work_second":30600,"end_work_time":"10:30:00","end_work_second":37800,"address":"16号","companie":"载盛物资","admin_alias":"","admin_id":7,"create_time":"2018-10-18 15:23:28","update_time":"2019-05-27 10:29:10","deleted":0,"lat":"30.9150260","lng":"121.4561060","type":1,"sort":8}},{"id":982147,"user_id":1,"user_name":"默认","user_phone":"17611110000","service_point_id":10,"staff_id":3,"weight":1.9,"recycling_price_id":40,"recycling_price":{"id":40,"uuid":"d2f45006c423c096790bb102efef825e","status":1,"name":"塑料1","price":1,"unit":"KG","integral":0,"create_time":"2018-10-22 08:06:26","update_time":"2019-06-28 09:21:53","deleted":0,"inventory":9636.5,"delivery_time":0,"type":1,"recycling_classification_id":2,"note":null,"before_price":1,"sort":99,"group_id":0},"pay_money":1.9,"pay_type":1,"integral":150,"create_time":"2019-06-28 09:21:00","update_time":"2019-06-28 09:21:00","unit_price":1,"date":"2019:06:28","transaction_no":"2019062884908334","deleted":0,"practical_pay_money":1.5,"number":1,"service_point":{"id":10,"name":"贝港八区（申凡苑）","functionary":"张凤","functionary_phone":"17317390767","start_work_time":"08:30:00","start_work_second":30600,"end_work_time":"10:30:00","end_work_second":37800,"address":"16号","companie":"载盛物资","admin_alias":"","admin_id":7,"create_time":"2018-10-18 15:23:28","update_time":"2019-05-27 10:29:10","deleted":0,"lat":"30.9150260","lng":"121.4561060","type":1,"sort":8}},{"id":982143,"user_id":1,"user_name":"默认","user_phone":"17611110000","service_point_id":10,"staff_id":3,"weight":13.9,"recycling_price_id":85,"recycling_price":{"id":85,"uuid":"2feca92c2a69558df8ee36cf086bacc0","status":1,"name":"纸板1.2","price":1.2,"unit":"KG","integral":0,"create_time":"2018-11-23 12:02:16","update_time":"2019-06-28 09:30:34","deleted":0,"inventory":100004296,"delivery_time":0,"type":1,"recycling_classification_id":1,"note":null,"before_price":1.2,"sort":99,"group_id":0},"pay_money":16.68,"pay_type":1,"integral":1650,"create_time":"2019-06-28 09:20:00","update_time":"2019-06-28 09:20:00","unit_price":1.2,"date":"2019:06:28","transaction_no":"2019062884827234","deleted":0,"practical_pay_money":16.5,"number":1,"service_point":{"id":10,"name":"贝港八区（申凡苑）","functionary":"张凤","functionary_phone":"17317390767","start_work_time":"08:30:00","start_work_second":30600,"end_work_time":"10:30:00","end_work_second":37800,"address":"16号","companie":"载盛物资","admin_alias":"","admin_id":7,"create_time":"2018-10-18 15:23:28","update_time":"2019-05-27 10:29:10","deleted":0,"lat":"30.9150260","lng":"121.4561060","type":1,"sort":8}},{"id":982142,"user_id":1,"user_name":"默认","user_phone":"17611110000","service_point_id":10,"staff_id":3,"weight":6.4,"recycling_price_id":37,"recycling_price":{"id":37,"uuid":"f35ae725a917cf685aa8fccce54881b5","status":1,"name":"废铁","price":1,"unit":"KG","integral":0,"create_time":"2018-10-22 08:05:05","update_time":"2019-06-28 09:20:04","deleted":0,"inventory":55816,"delivery_time":0,"type":1,"recycling_classification_id":3,"note":null,"before_price":0.8,"sort":99,"group_id":0},"pay_money":6.4,"pay_type":1,"integral":600,"create_time":"2019-06-28 09:20:00","update_time":"2019-06-28 09:20:00","unit_price":1,"date":"2019:06:28","transaction_no":"2019062884800756","deleted":0,"practical_pay_money":6,"number":1,"service_point":{"id":10,"name":"贝港八区（申凡苑）","functionary":"张凤","functionary_phone":"17317390767","start_work_time":"08:30:00","start_work_second":30600,"end_work_time":"10:30:00","end_work_second":37800,"address":"16号","companie":"载盛物资","admin_alias":"","admin_id":7,"create_time":"2018-10-18 15:23:28","update_time":"2019-05-27 10:29:10","deleted":0,"lat":"30.9150260","lng":"121.4561060","type":1,"sort":8}},{"id":982140,"user_id":1,"user_name":"默认","user_phone":"17611110000","service_point_id":10,"staff_id":3,"weight":7.5,"recycling_price_id":85,"recycling_price":{"id":85,"uuid":"2feca92c2a69558df8ee36cf086bacc0","status":1,"name":"纸板1.2","price":1.2,"unit":"KG","integral":0,"create_time":"2018-11-23 12:02:16","update_time":"2019-06-28 09:30:34","deleted":0,"inventory":100004296,"delivery_time":0,"type":1,"recycling_classification_id":1,"note":null,"before_price":1.2,"sort":99,"group_id":0},"pay_money":9,"pay_type":1,"integral":900,"create_time":"2019-06-28 09:18:00","update_time":"2019-06-28 09:18:00","unit_price":1.2,"date":"2019:06:28","transaction_no":"2019062884736134","deleted":0,"practical_pay_money":9,"number":1,"service_point":{"id":10,"name":"贝港八区（申凡苑）","functionary":"张凤","functionary_phone":"17317390767","start_work_time":"08:30:00","start_work_second":30600,"end_work_time":"10:30:00","end_work_second":37800,"address":"16号","companie":"载盛物资","admin_alias":"","admin_id":7,"create_time":"2018-10-18 15:23:28","update_time":"2019-05-27 10:29:10","deleted":0,"lat":"30.9150260","lng":"121.4561060","type":1,"sort":8}},{"id":982139,"user_id":1,"user_name":"默认","user_phone":"17611110000","service_point_id":10,"staff_id":3,"weight":13,"recycling_price_id":72,"recycling_price":{"id":72,"uuid":"6f76d3b7d7c9f682e3b65b3c8f51e9fe","status":1,"name":"微波炉（烤箱）","price":5,"unit":"只","integral":0,"create_time":"2018-10-23 12:49:28","update_time":"2019-06-28 09:18:29","deleted":0,"inventory":545.5,"delivery_time":0,"type":2,"recycling_classification_id":4,"note":null,"before_price":5,"sort":99,"group_id":0},"pay_money":65,"pay_type":1,"integral":6500,"create_time":"2019-06-28 09:18:00","update_time":"2019-06-28 09:18:00","unit_price":5,"date":"2019:06:28","transaction_no":"2019062884705753","deleted":0,"practical_pay_money":65,"number":1,"service_point":{"id":10,"name":"贝港八区（申凡苑）","functionary":"张凤","functionary_phone":"17317390767","start_work_time":"08:30:00","start_work_second":30600,"end_work_time":"10:30:00","end_work_second":37800,"address":"16号","companie":"载盛物资","admin_alias":"","admin_id":7,"create_time":"2018-10-18 15:23:28","update_time":"2019-05-27 10:29:10","deleted":0,"lat":"30.9150260","lng":"121.4561060","type":1,"sort":8}},{"id":982134,"user_id":1,"user_name":"默认","user_phone":"17611110000","service_point_id":10,"staff_id":3,"weight":15.1,"recycling_price_id":37,"recycling_price":{"id":37,"uuid":"f35ae725a917cf685aa8fccce54881b5","status":1,"name":"废铁","price":1,"unit":"KG","integral":0,"create_time":"2018-10-22 08:05:05","update_time":"2019-06-28 09:20:04","deleted":0,"inventory":55816,"delivery_time":0,"type":1,"recycling_classification_id":3,"note":null,"before_price":0.8,"sort":99,"group_id":0},"pay_money":15.1,"pay_type":1,"integral":1500,"create_time":"2019-06-28 09:17:00","update_time":"2019-06-28 09:17:00","unit_price":1,"date":"2019:06:28","transaction_no":"2019062884628618","deleted":0,"practical_pay_money":15,"number":1,"service_point":{"id":10,"name":"贝港八区（申凡苑）","functionary":"张凤","functionary_phone":"17317390767","start_work_time":"08:30:00","start_work_second":30600,"end_work_time":"10:30:00","end_work_second":37800,"address":"16号","companie":"载盛物资","admin_alias":"","admin_id":7,"create_time":"2018-10-18 15:23:28","update_time":"2019-05-27 10:29:10","deleted":0,"lat":"30.9150260","lng":"121.4561060","type":1,"sort":8}},{"id":982133,"user_id":1,"user_name":"默认","user_phone":"17611110000","service_point_id":10,"staff_id":3,"weight":2.1,"recycling_price_id":36,"recycling_price":{"id":36,"uuid":"450c3eca67cfe2ab5015a03aad875cbf","status":1,"name":"不锈钢2","price":2,"unit":"KG","integral":0,"create_time":"2018-10-22 08:04:40","update_time":"2019-06-28 09:17:13","deleted":0,"inventory":3303.1,"delivery_time":0,"type":1,"recycling_classification_id":3,"note":null,"before_price":2,"sort":99,"group_id":0},"pay_money":4.2,"pay_type":1,"integral":400,"create_time":"2019-06-28 09:17:00","update_time":"2019-06-28 09:17:00","unit_price":2,"date":"2019:06:28","transaction_no":"2019062884628618","deleted":0,"practical_pay_money":4,"number":1,"service_point":{"id":10,"name":"贝港八区（申凡苑）","functionary":"张凤","functionary_phone":"17317390767","start_work_time":"08:30:00","start_work_second":30600,"end_work_time":"10:30:00","end_work_second":37800,"address":"16号","companie":"载盛物资","admin_alias":"","admin_id":7,"create_time":"2018-10-18 15:23:28","update_time":"2019-05-27 10:29:10","deleted":0,"lat":"30.9150260","lng":"121.4561060","type":1,"sort":8}},{"id":982135,"user_id":1,"user_name":"默认","user_phone":"17611110000","service_point_id":10,"staff_id":3,"weight":5.2,"recycling_price_id":31,"recycling_price":{"id":31,"uuid":"d16c3538558e08b303f65dfbc1ab0dfd","status":1,"name":"黄铜16","price":16,"unit":"KG","integral":0,"create_time":"2018-10-22 08:02:31","update_time":"2019-06-28 09:17:13","deleted":0,"inventory":311.7,"delivery_time":0,"type":1,"recycling_classification_id":3,"note":null,"before_price":16,"sort":99,"group_id":0},"pay_money":83.2,"pay_type":1,"integral":8300,"create_time":"2019-06-28 09:17:00","update_time":"2019-06-28 09:17:00","unit_price":16,"date":"2019:06:28","transaction_no":"2019062884628618","deleted":0,"practical_pay_money":83,"number":1,"service_point":{"id":10,"name":"贝港八区（申凡苑）","functionary":"张凤","functionary_phone":"17317390767","start_work_time":"08:30:00","start_work_second":30600,"end_work_time":"10:30:00","end_work_second":37800,"address":"16号","companie":"载盛物资","admin_alias":"","admin_id":7,"create_time":"2018-10-18 15:23:28","update_time":"2019-05-27 10:29:10","deleted":0,"lat":"30.9150260","lng":"121.4561060","type":1,"sort":8}}]
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

    public static class DataBean  implements Serializable{
        /**
         * id : 982174
         * user_id : 1
         * user_name : 默认
         * user_phone : 17611110000
         * service_point_id : 22
         * staff_id : 3
         * weight : 12
         * recycling_price_id : 74
         * recycling_price : {"id":74,"uuid":"481a70e4abe26ac7596297d9638c1b15","status":1,"name":"自行车（大）","price":7,"unit":"辆","integral":0,"create_time":"2018-10-23 12:50:23","update_time":"2019-07-29 16:03:23","deleted":0,"inventory":765.7,"delivery_time":0,"type":2,"recycling_classification_id":4,"note":null,"before_price":7,"sort":99,"group_id":3}
         * pay_money : 84
         * pay_type : 1
         * integral : 8400
         * create_time : 2019-07-29 15:44:00
         * update_time : 2019-07-29 15:44:00
         * unit_price : 7
         * date : 2019:07:29
         * transaction_no : 2019072986258724
         * deleted : 0
         * practical_pay_money : 84
         * number : 1
         * service_point : {"id":22,"name":"贝港六区","functionary":"张凤","functionary_phone":"17317390767","start_work_time":"13:30:00","start_work_second":48600,"end_work_time":"15:30:00","end_work_second":55800,"address":"贝港南区694号东侧","companie":"载盛物资","admin_alias":"胡辉","admin_id":1,"create_time":"2018-10-29 11:01:56","update_time":"2018-10-29 15:19:17","deleted":0,"lat":"30.9170980","lng":"121.4515020","type":1,"sort":8}
         */

        private int id;
        private int user_id;
        private String user_name;
        private String user_phone;
        private int service_point_id;
        private int staff_id;
        private double weight;
        private int recycling_price_id;
        private RecyclingPriceBean recycling_price;
        private double pay_money;
        private int pay_type;
        private int integral;
        private String create_time;
        private String update_time;
        private double unit_price;
        private String date;
        private String transaction_no;
        private int deleted;
        private double practical_pay_money;
        private int number;
        private ServicePointBean service_point;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
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

        public int getService_point_id() {
            return service_point_id;
        }

        public void setService_point_id(int service_point_id) {
            this.service_point_id = service_point_id;
        }

        public int getStaff_id() {
            return staff_id;
        }

        public void setStaff_id(int staff_id) {
            this.staff_id = staff_id;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public int getRecycling_price_id() {
            return recycling_price_id;
        }

        public void setRecycling_price_id(int recycling_price_id) {
            this.recycling_price_id = recycling_price_id;
        }

        public RecyclingPriceBean getRecycling_price() {
            return recycling_price;
        }

        public void setRecycling_price(RecyclingPriceBean recycling_price) {
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

        public String getTransaction_no() {
            return transaction_no;
        }

        public void setTransaction_no(String transaction_no) {
            this.transaction_no = transaction_no;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public double getPractical_pay_money() {
            return practical_pay_money;
        }

        public void setPractical_pay_money(double practical_pay_money) {
            this.practical_pay_money = practical_pay_money;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public ServicePointBean getService_point() {
            return service_point;
        }

        public void setService_point(ServicePointBean service_point) {
            this.service_point = service_point;
        }

        public static class RecyclingPriceBean  implements Serializable{
            /**
             * id : 74
             * uuid : 481a70e4abe26ac7596297d9638c1b15
             * status : 1
             * name : 自行车（大）
             * price : 7
             * unit : 辆
             * integral : 0
             * create_time : 2018-10-23 12:50:23
             * update_time : 2019-07-29 16:03:23
             * deleted : 0
             * inventory : 765.7
             * delivery_time : 0
             * type : 2
             * recycling_classification_id : 4
             * note : null
             * before_price : 7
             * sort : 99
             * group_id : 3
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
            private int delivery_time;
            private int type;
            private int recycling_classification_id;
            private Object note;
            private double before_price;
            private int sort;
            private int group_id;

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

            public int getDelivery_time() {
                return delivery_time;
            }

            public void setDelivery_time(int delivery_time) {
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

            public Object getNote() {
                return note;
            }

            public void setNote(Object note) {
                this.note = note;
            }

            public double getBefore_price() {
                return before_price;
            }

            public void setBefore_price(double before_price) {
                this.before_price = before_price;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getGroup_id() {
                return group_id;
            }

            public void setGroup_id(int group_id) {
                this.group_id = group_id;
            }
        }

        public static class ServicePointBean  implements Serializable{
            /**
             * id : 22
             * name : 贝港六区
             * functionary : 张凤
             * functionary_phone : 17317390767
             * start_work_time : 13:30:00
             * start_work_second : 48600
             * end_work_time : 15:30:00
             * end_work_second : 55800
             * address : 贝港南区694号东侧
             * companie : 载盛物资
             * admin_alias : 胡辉
             * admin_id : 1
             * create_time : 2018-10-29 11:01:56
             * update_time : 2018-10-29 15:19:17
             * deleted : 0
             * lat : 30.9170980
             * lng : 121.4515020
             * type : 1
             * sort : 8
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
            private int type;
            private int sort;

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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }
        }
    }
}
