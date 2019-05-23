package com.yiwangrencai.ywkj.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/2.
 */

public class BookBean implements Serializable {
    private String certificate_name;
    private String gettime;
    private String gettime_name;
    private String id;
    public BookBean() {

    }

    public BookBean(String id,String gettime_name, String gettime, String certificate_name) {
        this.gettime_name = gettime_name;
        this.gettime = gettime;
        this.certificate_name = certificate_name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCertificate_name() {
        return certificate_name;
    }

    public void setCertificate_name(String certificate_name) {
        this.certificate_name = certificate_name;
    }

    public String getGettime_name() {
        return gettime_name;
    }

    public void setGettime_name(String gettime_name) {
        this.gettime_name = gettime_name;
    }

    public String getGettime() {
        return gettime;
    }

    public void setGettime(String gettime) {
        this.gettime = gettime;
    }




}
