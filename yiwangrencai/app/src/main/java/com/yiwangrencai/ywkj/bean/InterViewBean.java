package com.yiwangrencai.ywkj.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/12.
 */

public class InterViewBean implements Serializable {
    private String id;
    private String job_id;
    private String interview_time;
    private String status;
    private String remark;
    private String contacts;
    private String mobile;
    private String phone;
    private String company_name;
    private String job_title;
    private String address;
    private String lines;
    private String time;
    private String salary;
    public InterViewBean(){}
    public InterViewBean(String id,
                         String salary,
                         String time, String lines,
                         String address, String job_title,
                         String company_name, String phone,
                         String mobile, String remark, String contacts,
                         String status, String interview_time, String job_id) {

        this.id = id;
        this.salary = salary;
        this.time = time;
        this.lines = lines;
        this.address = address;
        this.job_title = job_title;
        this.company_name = company_name;
        this.phone = phone;
        this.mobile = mobile;
        this.remark = remark;
        this.contacts = contacts;
        this.status = status;
        this.interview_time = interview_time;
        this.job_id = job_id;
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

    public String getInterview_time() {
        return interview_time;
    }

    public void setInterview_time(String interview_time) {
        this.interview_time = interview_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLines() {
        return lines;
    }

    public void setLines(String lines) {
        this.lines = lines;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
