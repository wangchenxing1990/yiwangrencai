package com.yiwangrencai.ywkj.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/20.
 */

public class AreaCode implements Serializable {
    private String name;
    private String cid;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public AreaCode(String name,String cid) {
        this.name = name;
        this.cid = cid;
    }
    public AreaCode(){

    }
}
