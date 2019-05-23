package com.yiwangrencai.ywkj.bean;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/21.
 */

public class AreaCity implements Serializable {
    private String name;
    private String grade;
    private String next;
    private String cid;

    public AreaCity() {

    }
    public AreaCity(String name, String grade, String next,String cid) {
        this.name = name;
        this.grade = grade;
        this.next = next;
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
