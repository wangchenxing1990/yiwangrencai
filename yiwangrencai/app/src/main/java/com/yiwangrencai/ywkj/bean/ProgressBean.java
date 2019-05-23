package com.yiwangrencai.ywkj.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/2.
 */

public class ProgressBean implements Serializable {
    private String project_name;
    private String starttime;
    private String endtime;
    private String post;
    private String content;
    private String starttime_name;
    private String endtime_name;
    private String id;

    public ProgressBean() {
    }

    public ProgressBean(String id,String project_name, String endtime_name, String starttime_name, String content, String post, String endtime, String starttime) {
        this.project_name = project_name;
        this.endtime_name = endtime_name;
        this.starttime_name = starttime_name;
        this.content = content;
        this.post = post;
        this.endtime = endtime;
        this.starttime = starttime;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
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

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }
}
