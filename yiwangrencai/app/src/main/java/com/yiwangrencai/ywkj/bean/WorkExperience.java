package com.yiwangrencai.ywkj.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/2.
 */

public class WorkExperience implements Serializable {
    private String id;
    private String company;
    private String industry;
    private String comkind;
    private String scale;
    private String post;
    private String content;
    private String starttime_name;
    private String endtime_name;
    private String industry_name;
    private String comkind_name;
    private String scale_name;
    private String starttime;
    private String endtime;
    public WorkExperience(){}
    public WorkExperience(String starttime,String endtime,String id,String scale_name, String comkind_name, String industry_name, String endtime_name, String starttime_name, String content, String post, String scale, String comkind, String industry, String company) {
        this.id = id;
        this.scale_name = scale_name;
        this.comkind_name = comkind_name;
        this.industry_name = industry_name;
        this.endtime_name = endtime_name;
        this.starttime_name = starttime_name;
        this.content = content;
        this.post = post;
        this.scale = scale;
        this.comkind = comkind;
        this.industry = industry;
        this.company = company;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getScale_name() {
        return scale_name;
    }

    public void setScale_name(String scale_name) {
        this.scale_name = scale_name;
    }

    public String getComkind_name() {
        return comkind_name;
    }

    public void setComkind_name(String comkind_name) {
        this.comkind_name = comkind_name;
    }

    public String getIndustry_name() {
        return industry_name;
    }

    public void setIndustry_name(String industry_name) {
        this.industry_name = industry_name;
    }

    public String getEndtime_name() {
        return endtime_name;
    }

    public void setEndtime_name(String endtime_name) {
        this.endtime_name = endtime_name;
    }

    public String getStarttime_name() {
        return starttime_name;
    }

    public void setStarttime_name(String starttime_name) {
        this.starttime_name = starttime_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getComkind() {
        return comkind;
    }

    public void setComkind(String comkind) {
        this.comkind = comkind;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }
}
