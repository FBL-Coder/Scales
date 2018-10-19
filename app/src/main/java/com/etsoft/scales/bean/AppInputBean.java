package com.etsoft.scales.bean;

import java.util.List;

/**
 * Author：FBL  Time： 2018/10/10.
 */
public class AppInputBean {

    /**
     * phone : 17611110000
     * type : 1
     * recyclings : [{"recyclingPriceId":"6","weight":"1"},{"recyclingPriceId":"4","weight":"2"},{"recyclingPriceId":"1","weight":"1"}]
     * servicePointId : 3
     * staffId : 1
     * gifts : [{"giftId":"1","number":"10"}]
     */

    private String phone;
    private String type;
    private String servicePointId;
    private String staffId;
    private List<RecyclingsBean> recyclings;
    private List<GiftsBean> gifts;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<GiftsBean> getGifts() {
        return gifts;
    }

    public void setGifts(List<GiftsBean> gifts) {
        this.gifts = gifts;
    }

    public static class RecyclingsBean {
        /**
         * recyclingPriceId : 6
         * weight : 1
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

    public static class GiftsBean {
        /**
         * giftId : 1
         * number : 10
         */

        private String giftId;
        private String number;

        public String getGiftId() {
            return giftId;
        }

        public void setGiftId(String giftId) {
            this.giftId = giftId;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
