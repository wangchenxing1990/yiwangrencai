package com.yiwangrencai.ywkj.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yiwangrencai.ywkj.bean.BookBean;
import com.yiwangrencai.ywkj.bean.EducaExperBean;
import com.yiwangrencai.ywkj.bean.LangBean;
import com.yiwangrencai.ywkj.bean.OtherBean;
import com.yiwangrencai.ywkj.bean.ProgressBean;
import com.yiwangrencai.ywkj.bean.SkillBean;
import com.yiwangrencai.ywkj.bean.WorkExperience;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/8.
 */

public class parsResumeShowData {

    // public List<WorkExperience> listWorkExp = new ArrayList();

    /**
     * 解析工作经历的数据
     */
    public static List<WorkExperience> parsenArrayData(JSONArray workexpArray) {
        List<WorkExperience> listWorkExp = new ArrayList();
        // listWorkExp.clear();
        for (int i = 0; i < workexpArray.size(); i++) {
            WorkExperience workExperience = new WorkExperience();
            JSONObject jsonObject = JSON.parseObject(workexpArray.getString(i));
            workExperience.setCompany(jsonObject.getString("company"));
            workExperience.setPost(jsonObject.getString("post"));
            workExperience.setContent(jsonObject.getString("content"));
            workExperience.setStarttime_name(jsonObject.getString("starttime_name"));
            workExperience.setEndtime_name(jsonObject.getString("endtime_name"));
            workExperience.setIndustry_name(jsonObject.getString("industry_name"));
            workExperience.setComkind_name(jsonObject.getString("comkind_name"));
            workExperience.setScale_name(jsonObject.getString("scale_name"));
            workExperience.setId(jsonObject.getString("id"));
            workExperience.setStarttime(jsonObject.getString("starttime"));
            workExperience.setEndtime(jsonObject.getString("endtime"));
            workExperience.setIndustry(jsonObject.getString("industry"));
            workExperience.setComkind(jsonObject.getString("comkind"));
            workExperience.setScale(jsonObject.getString("scale"));

            listWorkExp.add(workExperience);
        }
        return listWorkExp;
    }


    /**
     * 解析教育背景数据
     *
     * @param educationArray
     */
    public static List<EducaExperBean> parenEducationData(JSONArray educationArray) {
        List<EducaExperBean> educationList = new ArrayList();
        for (int i = 0; i < educationArray.size(); i++) {
            EducaExperBean educaBean = new EducaExperBean();
            JSONObject json = JSON.parseObject(educationArray.getString(i));
            educaBean.setSchool(json.getString("school"));
            educaBean.setStarttime_name(json.getString("starttime_name"));
            educaBean.setEndtime_name(json.getString("endtime_name"));
            educaBean.setEducation_name(json.getString("education_name"));
            educaBean.setDescription(json.getString("description"));
            educaBean.setSpeciality(json.getString("speciality"));
            educaBean.setEdu_type(json.getString("edu_type"));
            educaBean.setType(json.getString("type"));
            educaBean.setEducation(json.getString("education"));
            educaBean.setId(json.getString("id"));

            educationList.add(educaBean);

        }
        return educationList;
    }


    /**
     * 解析项目经历的数据
     *
     * @param progressArray
     */
    public static List<ProgressBean> parsenProgress(JSONArray progressArray) {
        List<ProgressBean> progressList = new ArrayList<>();
        for (int i = 0; i < progressArray.size(); i++) {
            ProgressBean progressBean = new ProgressBean();
            JSONObject json = JSON.parseObject(progressArray.getString(i));
            progressBean.setProject_name(json.getString("project_name"));
            progressBean.setStarttime_name(json.getString("starttime_name"));
            progressBean.setEndtime_name(json.getString("endtime_name"));
            progressBean.setPost(json.getString("post"));
            progressBean.setContent(json.getString("content"));
            progressBean.setEndtime(json.getString("endtime"));
            progressBean.setStarttime(json.getString("starttime"));
            progressBean.setId(json.getString("id"));

            progressList.add(progressBean);
        }
        return progressList;
    }


    /**
     * 解析语言技能数据
     *
     * @param langueArray
     */
    public static List<LangBean> parsenLangArray(JSONArray langueArray) {
        List<LangBean> langList = new ArrayList();
        for (int i = 0; i < langueArray.size(); i++) {

            LangBean langBean = new LangBean();
            JSONObject jsons = JSON.parseObject(langueArray.getString(i));
            langBean.setLanguage(jsons.getString("language"));
            langBean.setDegree(jsons.getString("degree"));
            langBean.setLevel(jsons.getString("level"));
            langBean.setLanguage_name(jsons.getString("language_name"));
            langBean.setLevel_name(jsons.getString("level_name"));
            langBean.setId(jsons.getString("id"));

            langList.add(langBean);

        }
        return langList;
    }


    /**
     * 解析技能专长
     *
     * @param skillArray
     */
    public static List<SkillBean> parsenSkillArray(JSONArray skillArray) {
        List<SkillBean> skillList = new ArrayList<>();
        for (int i = 0; i < skillArray.size(); i++) {
            SkillBean skillBean = new SkillBean();
            JSONObject jsonss = JSON.parseObject(skillArray.getString(i));
            skillBean.setSkillname(jsonss.getString("skillname"));
            skillBean.setDegree(jsonss.getString("degree"));
            skillBean.setId(jsonss.getString("id"));

            skillList.add(skillBean);
        }

        return skillList;
    }

    /**
     * 解析证书的数据
     *
     * @param bookArray
     */
    public static List<BookBean> parsenBookArray(JSONArray bookArray) {
        List<BookBean> bookList = new ArrayList();
        for (int i = 0; i < bookArray.size(); i++) {
            BookBean bookBean = new BookBean();
            JSONObject json = JSON.parseObject(bookArray.getString(i));
            bookBean.setCertificate_name(json.getString("certificate_name"));
            bookBean.setGettime(json.getString("gettime"));
            bookBean.setGettime_name(json.getString("gettime_name"));
            bookBean.setId(json.getString("id"));

            bookList.add(bookBean);
        }
        return bookList;
    }

    private List<OtherBean> otherList = new ArrayList<>();

    /**
     * 解析其他信息的数据
     *
     * @param otherArray
     */
    public static  List<OtherBean> parsenOtherArray(JSONArray otherArray) {
        List<OtherBean> otherList = new ArrayList<>();
        for (int i = 0; i < otherArray.size(); i++) {
            OtherBean otherBean = new OtherBean();

            JSONObject json = JSON.parseObject(otherArray.getString(i));
            otherBean.setTitle(json.getString("title"));
            otherBean.setContent(json.getString("content"));
            otherBean.setId(json.getString("id"));
            otherList.add(otherBean);
        }
        return otherList;
    }
}
