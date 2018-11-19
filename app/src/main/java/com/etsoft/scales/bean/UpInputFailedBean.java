package com.etsoft.scales.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Author：FBL  Time： 2018/10/22.
 */
public class UpInputFailedBean implements Serializable{

    private List<AppInputBean> data;

    public List<AppInputBean> getData() {
        return data;
    }
    public void setData(List<AppInputBean> data) {
        this.data = data;
    }

}
