package com.yiwangrencai.ywkj.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/22.
 */

public class AreaCounty implements Serializable {
    private String name;
    private String next;
    private String nexts;
    private String cid;
    public AreaCounty() {

    }
    public AreaCounty(String name, String next, String nexts,String cid) {
        this.name = name;
        this.next = next;
        this.nexts = nexts;
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getNexts() {
        return nexts;
    }

    public void setNexts(String nexts) {
        this.nexts = nexts;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
