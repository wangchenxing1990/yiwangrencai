package com.yiwangrencai.ywkj.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/10.
 */

public class CompanyJobBean implements Serializable {
    private String id;
    private String job_id;
    private String com_id;
    private String job_title;
    private String updatetime;
    private String job_status;
    private String location_name;
    private String salary;
    private String education_name;
    private String work_year_name;
    private String company_name;
    private String job_status_name;
    private String is_urgent;
    private String part_status;
    private String logo;



    private String ecom_id;
    private String time;
    private String industry_name;
    private String region_name;

    public CompanyJobBean(){

    }
    public CompanyJobBean(String ecom_id,String time,String industry_name,
                          String region_name,String part_status,String job_id,String id,
                          String is_urgent, String job_status_name, String company_name,
                          String work_year_name, String education_name, String salary,
                          String location_name, String job_status, String updatetime,
                          String job_title, String com_id,String logo) {
        this.id = id;
        this.is_urgent = is_urgent;
        this.job_status_name = job_status_name;
        this.company_name = company_name;
        this.work_year_name = work_year_name;
        this.education_name = education_name;
        this.salary = salary;
        this.location_name = location_name;
        this.job_status = job_status;
        this.updatetime = updatetime;
        this.job_title = job_title;
        this.com_id = com_id;
        this.part_status = part_status;
        this.ecom_id = ecom_id;
        this.time = time;
        this.industry_name = industry_name;
        this.region_name = region_name;
        this.logo = logo;
    }

    public String getEcom_id() {
        return ecom_id;
    }

    public void setEcom_id(String ecom_id) {
        this.ecom_id = ecom_id;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public String getIndustry_name() {
        return industry_name;
    }

    public void setIndustry_name(String industry_name) {
        this.industry_name = industry_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPart_status() {
        return part_status;
    }

    public void setPart_status(String part_status) {
        this.part_status = part_status;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_urgent() {
        return is_urgent;
    }

    public void setIs_urgent(String is_urgent) {
        this.is_urgent = is_urgent;
    }

    public String getJob_status_name() {
        return job_status_name;
    }

    public void setJob_status_name(String job_status_name) {
        this.job_status_name = job_status_name;
    }

    public String getWork_year_name() {
        return work_year_name;
    }

    public void setWork_year_name(String work_year_name) {
        this.work_year_name = work_year_name;
    }

    public String getEducation_name() {
        return education_name;
    }

    public void setEducation_name(String education_name) {
        this.education_name = education_name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getJob_status() {
        return job_status;
    }

    public void setJob_status(String job_status) {
        this.job_status = job_status;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getCom_id() {
        return com_id;
    }

    public void setCom_id(String com_id) {
        this.com_id = com_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
