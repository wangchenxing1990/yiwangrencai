package com.yiwangrencai.ywkj.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/1.
 */

public class SearchPartJobBean {

    /**
     * per_page : 15
     * current_page : 1
     * next_page_url : null
     * prev_page_url : null
     * from : 1
     * to : 2
     * data : [{"title":"这是第一条数据","salary_price":"10","id":"47c48631","time":"1天前","company_name":"一网科技HR","city_id_name":"温岭市政府","salary_unit_name":"元/周"},{"title":"发传单","salary_price":"100","id":"d5d83932","time":"1周前","company_name":"Bing","city_id_name":"温岭市政府","salary_unit_name":"元/天"}]
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
         * title : 这是第一条数据
         * salary_price : 10
         * id : 47c48631
         * time : 1天前
         * company_name : 一网科技HR
         * city_id_name : 温岭市政府
         * salary_unit_name : 元/周
         */

        private String title;
        private String salary_price;
        private String id;
        private String time;
        private String company_name;
        private String city_id_name;
        private String salary_unit_name;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSalary_price() {
            return salary_price;
        }

        public void setSalary_price(String salary_price) {
            this.salary_price = salary_price;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getCity_id_name() {
            return city_id_name;
        }

        public void setCity_id_name(String city_id_name) {
            this.city_id_name = city_id_name;
        }

        public String getSalary_unit_name() {
            return salary_unit_name;
        }

        public void setSalary_unit_name(String salary_unit_name) {
            this.salary_unit_name = salary_unit_name;
        }
    }
}
