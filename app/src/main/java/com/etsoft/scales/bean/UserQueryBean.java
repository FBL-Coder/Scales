package com.etsoft.scales.bean;

/**
 * Author：FBL  Time： 2018/10/10.
 */
public class UserQueryBean {


    /**
     * code : 0
     * msg : OK
     * count : 1
     * data : {"id":12,"green_id":null,"name":"测试2","phone":"17611110000","source":2,"create_time":"2018-10-10 14:23:52","update_time":"2018-10-10 14:23:52","county":"西安县","town":"西安镇","street":"西安大道","neighborhood":"西安社区","community":"西安小区","building_no":23,"door_no":1122,"sex":0,"integral":0,"deleted":0,"user_excel_id":0}
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
         * id : 12
         * green_id : null
         * name : 测试2
         * phone : 17611110000
         * source : 2
         * create_time : 2018-10-10 14:23:52
         * update_time : 2018-10-10 14:23:52
         * county : 西安县
         * town : 西安镇
         * street : 西安大道
         * neighborhood : 西安社区
         * community : 西安小区
         * building_no : 23
         * door_no : 1122
         * sex : 0
         * integral : 0
         * deleted : 0
         * user_excel_id : 0
         */

        private int id;
        private Object green_id;
        private String name;
        private String phone;
        private int source;
        private String create_time;
        private String update_time;
        private String county;
        private String town;
        private String street;
        private String neighborhood;
        private String community;
        private int building_no;
        private int door_no;
        private int sex;
        private int integral;
        private int deleted;
        private int user_excel_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getGreen_id() {
            return green_id;
        }

        public void setGreen_id(Object green_id) {
            this.green_id = green_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
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

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getNeighborhood() {
            return neighborhood;
        }

        public void setNeighborhood(String neighborhood) {
            this.neighborhood = neighborhood;
        }

        public String getCommunity() {
            return community;
        }

        public void setCommunity(String community) {
            this.community = community;
        }

        public int getBuilding_no() {
            return building_no;
        }

        public void setBuilding_no(int building_no) {
            this.building_no = building_no;
        }

        public int getDoor_no() {
            return door_no;
        }

        public void setDoor_no(int door_no) {
            this.door_no = door_no;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public int getUser_excel_id() {
            return user_excel_id;
        }

        public void setUser_excel_id(int user_excel_id) {
            this.user_excel_id = user_excel_id;
        }
    }
}
