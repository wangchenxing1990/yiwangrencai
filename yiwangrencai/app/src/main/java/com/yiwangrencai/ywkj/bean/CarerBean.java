package com.yiwangrencai.ywkj.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/17.
 */

public class CarerBean implements Serializable{

    private String name;
    private String category;
    private String id;
    private String title;
    private String created_at;
    private String readnum;
    private String img;

    public CarerBean(String img,String readnum,String created_at,String title,String id,String name, String category) {
        this.name = name;
        this.category = category;
        this.id = id;
        this.title = title;
        this.created_at = created_at;
        this.readnum = readnum;
        this.img = img;
    }
    public CarerBean() {

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getReadnum() {
        return readnum;
    }

    public void setReadnum(String readnum) {
        this.readnum = readnum;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
