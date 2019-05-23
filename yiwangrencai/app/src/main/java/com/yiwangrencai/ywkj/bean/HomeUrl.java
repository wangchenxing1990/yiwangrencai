package com.yiwangrencai.ywkj.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/20.
 */

public class HomeUrl implements Serializable {
    private String url_type;
    private String id;
    private String url;
    private String eid;

    public HomeUrl() {

    }

    public HomeUrl(String url_type, String id, String url) {
        this.url_type = url_type;
        this.id = id;
        this.url = url;
        this.eid = eid;
    }

    public String getUrl_type() {
        return url_type;
    }

    public void setUrl_type(String url_type) {
        this.url_type = url_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }
}
