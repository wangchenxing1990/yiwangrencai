package com.yiwangrencai.ywkj.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/26.
 */

public class NearJobBean {


    /**
     * per_page : 15
     * current_page : 1
     * next_page_url : http://192.168.1.117:1024/api/v1/user/near_job?page=2
     * prev_page_url : null
     * from : 1
     * to : 15
     * data : [{"id":"dd7c819","job_title":"职位名称7(椒江区)test123","com_id":26,"education":0,"work_year":11,"distance":"0米","salary":"1000-1500","company_name":"一网科技cms"},{"id":"4f04d5115","job_title":"asd","com_id":26,"education":0,"work_year":0,"distance":"0米","salary":"2000-2000","company_name":"一网科技cms"},{"id":"640b9688","job_title":"asdfadsfads","com_id":26,"education":0,"work_year":0,"distance":"0米","salary":"3000-7000","company_name":"一网科技cms"},{"id":"06efed79","job_title":"sdfg","com_id":26,"education":0,"work_year":0,"distance":"0米","salary":"1000-1000","company_name":"一网科技cms"},{"id":"9ebb5778","job_title":"sdfg","com_id":26,"education":0,"work_year":0,"distance":"0米","salary":"1000-1000","company_name":"一网科技cms"},{"id":"43d4b577","job_title":"刚回家","com_id":26,"education":0,"work_year":0,"distance":"0米","salary":"2500-2500","company_name":"一网科技cms"},{"id":"57bc1661","job_title":"Iopuiop","com_id":26,"education":0,"work_year":0,"distance":"0米","salary":"7000-9000","company_name":"一网科技cms"},{"id":"c07049146","job_title":"产品","com_id":26,"education":0,"work_year":11,"distance":"0米","salary":"1万-25000","company_name":"一网科技cms"},{"id":"b406d4142","job_title":"黑胡椒","com_id":26,"education":0,"work_year":0,"distance":"0米","salary":"3500-6000","company_name":"一网科技cms"},{"id":"e2f018141","job_title":"嘎嘎嘎","com_id":26,"education":50,"work_year":12,"distance":"0米","salary":"3500-8000","company_name":"一网科技cms"},{"id":"80563d140","job_title":"光华","com_id":26,"education":20,"work_year":10,"distance":"0米","salary":"5000-6000","company_name":"一网科技cms"},{"id":"ac145f24","job_title":"职位职位","com_id":26,"education":0,"work_year":0,"distance":"0米","salary":"1500-1500","company_name":"一网科技cms"},{"id":"c948e0138","job_title":"Android","com_id":26,"education":0,"work_year":0,"distance":"0米","salary":"3500-4000","company_name":"一网科技cms"},{"id":"ee4e9823","job_title":"Asd","com_id":26,"education":20,"work_year":0,"distance":"0米","salary":"2000-3500","company_name":"一网科技cms"},{"id":"8d0d71137","job_title":"Android","com_id":26,"education":0,"work_year":0,"distance":"0米","salary":"3500-4000","company_name":"一网科技cms"}]
     * code : 1
     * msg :
     */

    private int per_page;
    private int current_page;
    private String next_page_url;
    private Object prev_page_url;
    private int from;
    private int to;
    private String code;
    private String msg;
    private List<DataBean> data;

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public Object getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(Object prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : dd7c819
         * job_title : 职位名称7(椒江区)test123
         * com_id : 26
         * education : 0
         * work_year : 11
         * distance : 0米
         * salary : 1000-1500
         * company_name : 一网科技cms
         */

        private String id;
        private String job_title;
        private int com_id;
        private int education;
        private int work_year;
        private String distance;
        private String salary;
        private String company_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getJob_title() {
            return job_title;
        }

        public void setJob_title(String job_title) {
            this.job_title = job_title;
        }

        public int getCom_id() {
            return com_id;
        }

        public void setCom_id(int com_id) {
            this.com_id = com_id;
        }

        public int getEducation() {
            return education;
        }

        public void setEducation(int education) {
            this.education = education;
        }

        public int getWork_year() {
            return work_year;
        }

        public void setWork_year(int work_year) {
            this.work_year = work_year;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getSalary() {
            return salary;
        }

        public void setSalary(String salary) {
            this.salary = salary;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }
    }
}
