package com.yiwangrencai.ywkj.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/2.
 */

public class EducaExperBean implements Serializable {
    private String edu_type;
    private String school;
    private String starttime;
    private String endtime;
    private String speciality;
    private String education;
    private String type;
    private String description;
    private String education_name;
    private String starttime_name;
    private String endtime_name;
    private String id;
    public EducaExperBean() {}

    public EducaExperBean(String id,String edu_type, String endtime_name, String starttime_name, String education_name, String description, String type, String education, String speciality, String endtime, String starttime, String school) {
        this.edu_type = edu_type;
        this.endtime_name = endtime_name;
        this.starttime_name = starttime_name;
        this.education_name = education_name;
        this.description = description;
        this.type = type;
        this.education = education;
        this.speciality = speciality;
        this.endtime = endtime;
        this.starttime = starttime;
        this.school = school;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEducation_name() {
        return education_name;
    }

    public void setEducation_name(String education_name) {
        this.education_name = education_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
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

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getEdu_type() {
        return edu_type;
    }

    public void setEdu_type(String edu_type) {
        this.edu_type = edu_type;
    }
}
