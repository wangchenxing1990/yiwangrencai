package com.yiwangrencai.ywkj.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/3.
 */

public class PartApplyBean {
    /**
     * per_page : 15
     * current_page : 1
     * next_page_url : null
     * prev_page_url : null
     * from : 1
     * to : 3
     * data : [{"id":20,"pt_job_id":"47c48631","status":3,"created_at":"2017-06-30 11:10:52","title":"这是第一条数据","company_name":"一网科技HR"},{"id":19,"pt_job_id":"47c48631","status":2,"created_at":"2017-06-30 10:14:13","title":"这是第一条数据","company_name":"一网科技HR"},{"id":18,"pt_job_id":"d5d83932","status":2,"created_at":"2017-05-31 14:18:52","title":"发传单","company_name":"Bing"}]
     * code : 1
     * msg :
     */

    private int per_page;
    private int current_page;
    private Object next_page_url;
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

    public Object getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(Object next_page_url) {
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
         * id : 20
         * pt_job_id : 47c48631
         * status : 3
         * created_at : 2017-06-30 11:10:52
         * title : 这是第一条数据
         * company_name : 一网科技HR
         */

        private int id;
        private String pt_job_id;
        private int status;
        private String created_at;
        private String title;
        private String company_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPt_job_id() {
            return pt_job_id;
        }

        public void setPt_job_id(String pt_job_id) {
            this.pt_job_id = pt_job_id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }
    }
}
