package com.yiwangrencai.ywkj.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */

public class PartDetailBean {

    /**
     * code : 1
     * msg :
     * data : {"id":"32","uid":"193","com_id":"102","title":"发传单","type_id":"11","long_term":"0","time_start":"2017-05-27","time_end":"2017-05-31","need_num":"50","sex":"0","salary_price":"100","address":"椒江市民广场","longitude":"121.419594","latitude":"28.661584","coordinate_address":"市府大道","content":"发发发发发发发发传单","contacts":"阿斯顿发","mobile":"13333333333","phone":"","send":0,"company_name":"Bing","other_job":[{"id":"1a827431","com_id":"26","title":"这是第一条数据"},{"id":"d4a3ac34","com_id":"26","title":"送货员"}],"profile":"Sfdhglkjshflgkjshdflkgjhslkjdfhg","city_id_name":"温岭市政府","salary_unit_name":"元/天","salary_method_name":"日结","free_time":"16,17,18,19,20,21","education_name":"不限"}
     */

    private String code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 32
         * uid : 193
         * com_id : 102
         * title : 发传单
         * type_id : 11
         * long_term : 0
         * time_start : 2017-05-27
         * time_end : 2017-05-31
         * need_num : 50
         * sex : 0
         * salary_price : 100
         * address : 椒江市民广场
         * longitude : 121.419594
         * latitude : 28.661584
         * coordinate_address : 市府大道
         * content : 发发发发发发发发传单
         * contacts : 阿斯顿发
         * mobile : 13333333333
         * phone :
         * send : 0
         * company_name : Bing
         * other_job : [{"id":"1a827431","com_id":"26","title":"这是第一条数据"},{"id":"d4a3ac34","com_id":"26","title":"送货员"}]
         * profile : Sfdhglkjshflgkjshdflkgjhslkjdfhg
         * city_id_name : 温岭市政府
         * salary_unit_name : 元/天
         * salary_method_name : 日结
         * free_time : 16,17,18,19,20,21
         * education_name : 不限
         */

        private String id;
        private String uid;
        private String com_id;
        private String title;
        private String type_id;
        private String long_term;
        private String time_start;
        private String time_end;
        private String need_num;
        private String sex;
        private String salary_price;
        private String address;
        private String longitude;
        private String latitude;
        private String coordinate_address;
        private String content;
        private String contacts;
        private String mobile;
        private String phone;
        private int send;
        private String company_name;
        private String profile;
        private String city_id_name;
        private String salary_unit_name;
        private String salary_method_name;
        private String free_time;
        private String education_name;
        private List<OtherJobBean> other_job;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getCom_id() {
            return com_id;
        }

        public void setCom_id(String com_id) {
            this.com_id = com_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType_id() {
            return type_id;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }

        public String getLong_term() {
            return long_term;
        }

        public void setLong_term(String long_term) {
            this.long_term = long_term;
        }

        public String getTime_start() {
            return time_start;
        }

        public void setTime_start(String time_start) {
            this.time_start = time_start;
        }

        public String getTime_end() {
            return time_end;
        }

        public void setTime_end(String time_end) {
            this.time_end = time_end;
        }

        public String getNeed_num() {
            return need_num;
        }

        public void setNeed_num(String need_num) {
            this.need_num = need_num;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSalary_price() {
            return salary_price;
        }

        public void setSalary_price(String salary_price) {
            this.salary_price = salary_price;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getCoordinate_address() {
            return coordinate_address;
        }

        public void setCoordinate_address(String coordinate_address) {
            this.coordinate_address = coordinate_address;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public int getSend() {
            return send;
        }

        public void setSend(int send) {
            this.send = send;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
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

        public String getSalary_method_name() {
            return salary_method_name;
        }

        public void setSalary_method_name(String salary_method_name) {
            this.salary_method_name = salary_method_name;
        }

        public String getFree_time() {
            return free_time;
        }

        public void setFree_time(String free_time) {
            this.free_time = free_time;
        }

        public String getEducation_name() {
            return education_name;
        }

        public void setEducation_name(String education_name) {
            this.education_name = education_name;
        }

        public List<OtherJobBean> getOther_job() {
            return other_job;
        }

        public void setOther_job(List<OtherJobBean> other_job) {
            this.other_job = other_job;
        }

        public static class OtherJobBean {
            /**
             * id : 1a827431
             * com_id : 26
             * title : 这是第一条数据
             */

            private String id;
            private String com_id;
            private String title;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCom_id() {
                return com_id;
            }

            public void setCom_id(String com_id) {
                this.com_id = com_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
