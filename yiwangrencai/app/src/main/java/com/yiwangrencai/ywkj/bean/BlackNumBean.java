package com.yiwangrencai.ywkj.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/8.
 */

public class BlackNumBean implements Serializable {
    private String id;
    private String company_uid;
    private String company_name;
    private String uid;

    public BlackNumBean() {

    }

    public BlackNumBean(String uid,String id, String company_uid, String company_name) {
        this.id = id;
        this.company_uid = company_uid;
        this.company_name = company_name;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany_uid() {
        return company_uid;
    }

    public void setCompany_uid(String company_uid) {
        this.company_uid = company_uid;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
}
