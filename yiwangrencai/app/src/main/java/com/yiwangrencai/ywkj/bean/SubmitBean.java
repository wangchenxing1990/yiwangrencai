package com.yiwangrencai.ywkj.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/12.
 */

public class SubmitBean implements Serializable {
    private String id;
    private String job_id;
    private String status;
    private String created_at;
    private String job_title;
    private String company_name;

    public SubmitBean(String id, String job_id, String status, String created_at, String job_title, String company_name) {
        this.id = id;
        this.job_id = job_id;
        this.status = status;
        this.created_at = created_at;
        this.job_title = job_title;
        this.company_name = company_name;
    }
    public SubmitBean( ) {

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
}
