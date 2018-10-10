package com.etsoft.scales.bean;

import java.util.List;

/**
 * Author：FBL  Time： 2018/10/10.
 */
public class AppInputBean {

    /**
     * phone : 13774374601
     * servicePointId : 1
     * staffId : 1
     * recyclings : [{"recyclingPriceId":"1","weight":"10"},{"recyclingPriceId":"2","weight":"10"}]
     */

    private String phone;
    private String servicePointId;
    private String staffId;
    private List<RecyclingsBean> recyclings;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getServicePointId() {
        return servicePointId;
    }

    public void setServicePointId(String servicePointId) {
        this.servicePointId = servicePointId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public List<RecyclingsBean> getRecyclings() {
        return recyclings;
    }

    public void setRecyclings(List<RecyclingsBean> recyclings) {
        this.recyclings = recyclings;
    }

    public static class RecyclingsBean {
        /**
         * recyclingPriceId : 1
         * weight : 10
         */

        private String recyclingPriceId;
        private String weight;

        public String getRecyclingPriceId() {
            return recyclingPriceId;
        }

        public void setRecyclingPriceId(String recyclingPriceId) {
            this.recyclingPriceId = recyclingPriceId;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }
    }
}
