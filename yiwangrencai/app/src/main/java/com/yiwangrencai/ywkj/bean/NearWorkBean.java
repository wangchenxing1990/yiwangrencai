package com.yiwangrencai.ywkj.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/9.
 */

public class NearWorkBean implements Serializable {
    private String id;
    private String job_title;
    private String com_id;
    private String education;
    private String work_year;
    private String distance;
    private String salary;
    private String company_name;

    public NearWorkBean(){

    }

    public NearWorkBean(String id, String company_name, String salary, String distance, String work_year, String education, String com_id, String job_title) {
        this.id = id;
        this.company_name = company_name;
        this.salary = salary;
        this.distance = distance;
        this.work_year = work_year;
        this.education = education;
        this.com_id = com_id;
        this.job_title = job_title;
    }

    public NearWorkBean(String id){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getWork_year() {
        return work_year;
    }

    public void setWork_year(String work_year) {
        this.work_year = work_year;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getCom_id() {
        return com_id;
    }

    public void setCom_id(String com_id) {
        this.com_id = com_id;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }
}
