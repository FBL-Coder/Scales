package com.etsoft.scales.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Author：FBL  Time： 2018/11/7.
 */
public class GiftListBean implements Serializable{


    /**
     * code : 0
     * msg : OK
     * count : 3
     * data : [{"id":3,"name":"低价格塑料水杯","inventory":1000,"create_time":"2018-11-07 09:28:14","update_time":"2018-11-07 09:28:14","deleted":0,"condition":0},{"id":2,"name":"钢丝球","inventory":1000,"create_time":"2018-11-07 09:27:22","update_time":"2018-11-07 09:27:22","deleted":0,"condition":0},{"id":1,"name":"鲜花","inventory":1000,"create_time":"2018-11-07 09:27:09","update_time":"2018-11-07 09:27:09","deleted":0,"condition":0}]
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

    public static class DataBean implements Serializable {
        /**
         * id : 3
         * name : 低价格塑料水杯
         * inventory : 1000
         * create_time : 2018-11-07 09:28:14
         * update_time : 2018-11-07 09:28:14
         * deleted : 0
         * condition : 0
         */

        private int id;
        private String name;
        private int inventory;
        private String create_time;
        private String update_time;
        private int deleted;
        private int condition;

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

        public int getInventory() {
            return inventory;
        }

        public void setInventory(int inventory) {
            this.inventory = inventory;
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

        public int getCondition() {
            return condition;
        }

        public void setCondition(int condition) {
            this.condition = condition;
        }
    }
}
