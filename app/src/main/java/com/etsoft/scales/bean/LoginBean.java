package com.etsoft.scales.bean;

/**
 * Author：FBL  Time： 2018/9/28.
 */
public class LoginBean {

    /**
     * code : 0
     * msg : OK
     * count : 1
     * data : {"id":1,"phone":"13774374601","name":"mucc002","company_name":"mucc002","position":"mucc002","create_admin_id":1,"create_admin_alias":"张三","update_admin_id":1,"update_admin_alias":"张三","create_time":"2018-09-18 14:55:52","update_time":"2018-09-18 15:32:40","deleted":0,"password":"e10adc3949ba59abbe56e057f20f883e","access_token":"c117392e0d4b2bfba2c9d79c9ff57929","refresh_token":"43c72646eaad8d3e16070c10e1109e86","data":{"outboundTotalWeight":40,"replenishStockTotalWeight":52.05,"replenishStockTotalPayMoney":67.2}}
     */

    private int code;
    private String msg;
    private int count;
    private DataBeanX data;

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

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * id : 1
         * phone : 13774374601
         * name : mucc002
         * company_name : mucc002
         * position : mucc002
         * create_admin_id : 1
         * create_admin_alias : 张三
         * update_admin_id : 1
         * update_admin_alias : 张三
         * create_time : 2018-09-18 14:55:52
         * update_time : 2018-09-18 15:32:40
         * deleted : 0
         * password : e10adc3949ba59abbe56e057f20f883e
         * access_token : c117392e0d4b2bfba2c9d79c9ff57929
         * refresh_token : 43c72646eaad8d3e16070c10e1109e86
         * data : {"outboundTotalWeight":40,"replenishStockTotalWeight":52.05,"replenishStockTotalPayMoney":67.2}
         */

        private int id;
        private String phone;
        private String name;
        private String company_name;
        private String position;
        private int create_admin_id;
        private String create_admin_alias;
        private int update_admin_id;
        private String update_admin_alias;
        private String create_time;
        private String update_time;
        private int deleted;
        private String password;
        private String access_token;
        private String refresh_token;
        private DataBean data;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public int getCreate_admin_id() {
            return create_admin_id;
        }

        public void setCreate_admin_id(int create_admin_id) {
            this.create_admin_id = create_admin_id;
        }

        public String getCreate_admin_alias() {
            return create_admin_alias;
        }

        public void setCreate_admin_alias(String create_admin_alias) {
            this.create_admin_alias = create_admin_alias;
        }

        public int getUpdate_admin_id() {
            return update_admin_id;
        }

        public void setUpdate_admin_id(int update_admin_id) {
            this.update_admin_id = update_admin_id;
        }

        public String getUpdate_admin_alias() {
            return update_admin_alias;
        }

        public void setUpdate_admin_alias(String update_admin_alias) {
            this.update_admin_alias = update_admin_alias;
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

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * outboundTotalWeight : 40
             * replenishStockTotalWeight : 52.05
             * replenishStockTotalPayMoney : 67.2
             */

            private int outboundTotalWeight;
            private double replenishStockTotalWeight;
            private double replenishStockTotalPayMoney;

            public int getOutboundTotalWeight() {
                return outboundTotalWeight;
            }

            public void setOutboundTotalWeight(int outboundTotalWeight) {
                this.outboundTotalWeight = outboundTotalWeight;
            }

            public double getReplenishStockTotalWeight() {
                return replenishStockTotalWeight;
            }

            public void setReplenishStockTotalWeight(double replenishStockTotalWeight) {
                this.replenishStockTotalWeight = replenishStockTotalWeight;
            }

            public double getReplenishStockTotalPayMoney() {
                return replenishStockTotalPayMoney;
            }

            public void setReplenishStockTotalPayMoney(double replenishStockTotalPayMoney) {
                this.replenishStockTotalPayMoney = replenishStockTotalPayMoney;
            }
        }
    }
}

