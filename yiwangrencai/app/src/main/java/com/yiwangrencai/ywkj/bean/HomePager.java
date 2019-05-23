package com.yiwangrencai.ywkj.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/6.
 */

public class HomePager implements Serializable {
    private String id;
    private String job;
    private String educatoin;
    private String workYear;
    private String locationName;
    private String salary;
    private String companyName;

    public HomePager() {

    }
    public HomePager(String id,String job,String educatoin,String workYear,String locationName,String salary,String companyName) {
        this.id=id;
        this.job=job;
        this.educatoin=educatoin;
        this.workYear=workYear;
        this.locationName=locationName;
        this.salary=salary;
        this.companyName=companyName;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id=id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getEducatoin() {
        return educatoin;
    }

    public void setEducatoin(String educatoin) {
        this.educatoin = educatoin;
    }
    public String getWorkYear() {
        return workYear;
    }

    public void setWorkYear(String workYear) {
        this.workYear = workYear;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
