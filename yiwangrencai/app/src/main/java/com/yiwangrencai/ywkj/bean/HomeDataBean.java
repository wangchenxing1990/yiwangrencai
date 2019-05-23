package com.yiwangrencai.ywkj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/10/30.
 */

public class HomeDataBean implements Serializable {


    /**
     * code : 1
     * msg :
     * data : {"job":[{"id":"15137733319","job_title":"知识产权顾问","part_status":"1","location_name":"双桥区","salary":"2000-5000","education_name":"大专","work_year_name":"经验不限","company_name":"北京开格知识产权代理有限公司","is_urgent":0},{"id":"44de0a29358","job_title":"电子技术工程师","part_status":"1","location_name":"双桥区","salary":"3000-6000","education_name":"大专","work_year_name":"2年以上","company_name":"承德市创新时代网络有限公司","is_urgent":0},{"id":"71f2c529760","job_title":"暖通工程师","part_status":"1","location_name":"双桥区,承德市","salary":"3000-4000","education_name":"本科","work_year_name":"3年以上","company_name":"河北浩枫供热服务有限公司","is_urgent":0},{"id":"e35cf032613","job_title":"理货员","part_status":"0","location_name":"承德市","salary":"2500-3500","education_name":"学历不限","work_year_name":"经验不限","company_name":"承德大润发商业有限公司","is_urgent":0},{"id":"66868a28594","job_title":"心理老师","part_status":"0","location_name":"承德市","salary":"4000-5000","education_name":"大专","work_year_name":"2年以上","company_name":"承德学大教育培训学校","is_urgent":1},{"id":"fdc52832336","job_title":"承德肯德基餐厅储备经理","part_status":"1","location_name":"双桥区","salary":"2500-3000","education_name":"大专","work_year_name":"经验不限","company_name":"天津肯德基有限公司","is_urgent":0},{"id":"5efac022735","job_title":"收银员","part_status":"0","location_name":"隆化县","salary":"2000-3000","education_name":"学历不限","work_year_name":"经验不限","company_name":"承德枫水湾（国际）森林温泉城","is_urgent":0},{"id":"88ddff28627","job_title":"客服经理","part_status":"1","location_name":"双桥区,双滦区","salary":"4000-5000","education_name":"大专","work_year_name":"3年以上","company_name":"河北隆泰物业服务有限责任公司承德分公司","is_urgent":0},{"id":"1e352133444","job_title":"日语翻译","part_status":"1","location_name":"双桥区","salary":"面议","education_name":"大专","work_year_name":"2年以上","company_name":"承德市唯美服装有限公司","is_urgent":1},{"id":"169a6825562","job_title":"TATA木门诚聘销售顾问","part_status":"1","location_name":"承德市","salary":"3000-4000","education_name":"学历不限","work_year_name":"经验不限","company_name":"TATA木门同创工贸有限公司","is_urgent":0},{"id":"67d8da33463","job_title":"房地产策划总监","part_status":"1","location_name":"双桥区","salary":"1.5万-2万","education_name":"学历不限","work_year_name":"3年以上","company_name":"承德同德行房地产经纪有限公司","is_urgent":0},{"id":"22e02f33391","job_title":"文员、销售代表","part_status":"1","location_name":"承德市,双桥区","salary":"面议","education_name":"学历不限","work_year_name":"经验不限","company_name":"承德市君汇义齿定制有限公司","is_urgent":0},{"id":"1bc5bf33352","job_title":"司机","part_status":"1","location_name":"双桥区","salary":"面议","education_name":"学历不限","work_year_name":"经验不限","company_name":"承德锐杰建筑安装工程有限公司","is_urgent":0},{"id":"a7fe4a33242","job_title":"市场支付推广经理","part_status":"1","location_name":"承德市","salary":"4000-1万","education_name":"高中","work_year_name":"2年以上","company_name":"承德弘顺房产经纪有限公司","is_urgent":0},{"id":"86981231091","job_title":"汽车销售顾问","part_status":"1","location_name":"双滦区","salary":"5000-6000","education_name":"大专","work_year_name":"2年以上","company_name":"承德兴耀别克、雪佛兰、斯柯达、凯迪拉克4S店","is_urgent":0}],"img":["adminad/201710/1508471595x4mbo.jpg","adminad/201710/1508471583cstkm.jpg","adminad/201710/1508471571ndd4w.jpg"],"url":[{"url_type":1,"url":"http://www.0314job.com","eid":"ec4bb0http://www.0314job.com"},{"url_type":1,"url":"http://www.0314job.com","eid":"ec4bb0http://www.0314job.com"},{"url_type":1,"url":"http://www.0314job.com","eid":"ec4bb0http://www.0314job.com"}],"badge":0}
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
         * job : [{"id":"15137733319","job_title":"知识产权顾问","part_status":"1","location_name":"双桥区","salary":"2000-5000","education_name":"大专","work_year_name":"经验不限","company_name":"北京开格知识产权代理有限公司","is_urgent":0},{"id":"44de0a29358","job_title":"电子技术工程师","part_status":"1","location_name":"双桥区","salary":"3000-6000","education_name":"大专","work_year_name":"2年以上","company_name":"承德市创新时代网络有限公司","is_urgent":0},{"id":"71f2c529760","job_title":"暖通工程师","part_status":"1","location_name":"双桥区,承德市","salary":"3000-4000","education_name":"本科","work_year_name":"3年以上","company_name":"河北浩枫供热服务有限公司","is_urgent":0},{"id":"e35cf032613","job_title":"理货员","part_status":"0","location_name":"承德市","salary":"2500-3500","education_name":"学历不限","work_year_name":"经验不限","company_name":"承德大润发商业有限公司","is_urgent":0},{"id":"66868a28594","job_title":"心理老师","part_status":"0","location_name":"承德市","salary":"4000-5000","education_name":"大专","work_year_name":"2年以上","company_name":"承德学大教育培训学校","is_urgent":1},{"id":"fdc52832336","job_title":"承德肯德基餐厅储备经理","part_status":"1","location_name":"双桥区","salary":"2500-3000","education_name":"大专","work_year_name":"经验不限","company_name":"天津肯德基有限公司","is_urgent":0},{"id":"5efac022735","job_title":"收银员","part_status":"0","location_name":"隆化县","salary":"2000-3000","education_name":"学历不限","work_year_name":"经验不限","company_name":"承德枫水湾（国际）森林温泉城","is_urgent":0},{"id":"88ddff28627","job_title":"客服经理","part_status":"1","location_name":"双桥区,双滦区","salary":"4000-5000","education_name":"大专","work_year_name":"3年以上","company_name":"河北隆泰物业服务有限责任公司承德分公司","is_urgent":0},{"id":"1e352133444","job_title":"日语翻译","part_status":"1","location_name":"双桥区","salary":"面议","education_name":"大专","work_year_name":"2年以上","company_name":"承德市唯美服装有限公司","is_urgent":1},{"id":"169a6825562","job_title":"TATA木门诚聘销售顾问","part_status":"1","location_name":"承德市","salary":"3000-4000","education_name":"学历不限","work_year_name":"经验不限","company_name":"TATA木门同创工贸有限公司","is_urgent":0},{"id":"67d8da33463","job_title":"房地产策划总监","part_status":"1","location_name":"双桥区","salary":"1.5万-2万","education_name":"学历不限","work_year_name":"3年以上","company_name":"承德同德行房地产经纪有限公司","is_urgent":0},{"id":"22e02f33391","job_title":"文员、销售代表","part_status":"1","location_name":"承德市,双桥区","salary":"面议","education_name":"学历不限","work_year_name":"经验不限","company_name":"承德市君汇义齿定制有限公司","is_urgent":0},{"id":"1bc5bf33352","job_title":"司机","part_status":"1","location_name":"双桥区","salary":"面议","education_name":"学历不限","work_year_name":"经验不限","company_name":"承德锐杰建筑安装工程有限公司","is_urgent":0},{"id":"a7fe4a33242","job_title":"市场支付推广经理","part_status":"1","location_name":"承德市","salary":"4000-1万","education_name":"高中","work_year_name":"2年以上","company_name":"承德弘顺房产经纪有限公司","is_urgent":0},{"id":"86981231091","job_title":"汽车销售顾问","part_status":"1","location_name":"双滦区","salary":"5000-6000","education_name":"大专","work_year_name":"2年以上","company_name":"承德兴耀别克、雪佛兰、斯柯达、凯迪拉克4S店","is_urgent":0}]
         * img : ["adminad/201710/1508471595x4mbo.jpg","adminad/201710/1508471583cstkm.jpg","adminad/201710/1508471571ndd4w.jpg"]
         * url : [{"url_type":1,"url":"http://www.0314job.com","eid":"ec4bb0http://www.0314job.com"},{"url_type":1,"url":"http://www.0314job.com","eid":"ec4bb0http://www.0314job.com"},{"url_type":1,"url":"http://www.0314job.com","eid":"ec4bb0http://www.0314job.com"}]
         * badge : 0
         */

        private int badge;
        private List<JobBean> job;
        private List<String> img;
        private List<UrlBean> url;

        public int getBadge() {
            return badge;
        }

        public void setBadge(int badge) {
            this.badge = badge;
        }

        public List<JobBean> getJob() {
            return job;
        }

        public void setJob(List<JobBean> job) {
            this.job = job;
        }

        public List<String> getImg() {
            return img;
        }

        public void setImg(List<String> img) {
            this.img = img;
        }

        public List<UrlBean> getUrl() {
            return url;
        }

        public void setUrl(List<UrlBean> url) {
            this.url = url;
        }

        public static class JobBean {
            /**
             * id : 15137733319
             * job_title : 知识产权顾问
             * part_status : 1
             * location_name : 双桥区
             * salary : 2000-5000
             * education_name : 大专
             * work_year_name : 经验不限
             * company_name : 北京开格知识产权代理有限公司
             * is_urgent : 0
             */

            private String id;
            private String job_title;
            private String part_status;
            private String location_name;
            private String salary;
            private String education_name;
            private String work_year_name;
            private String company_name;
            private int is_urgent;

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

            public String getPart_status() {
                return part_status;
            }

            public void setPart_status(String part_status) {
                this.part_status = part_status;
            }

            public String getLocation_name() {
                return location_name;
            }

            public void setLocation_name(String location_name) {
                this.location_name = location_name;
            }

            public String getSalary() {
                return salary;
            }

            public void setSalary(String salary) {
                this.salary = salary;
            }

            public String getEducation_name() {
                return education_name;
            }

            public void setEducation_name(String education_name) {
                this.education_name = education_name;
            }

            public String getWork_year_name() {
                return work_year_name;
            }

            public void setWork_year_name(String work_year_name) {
                this.work_year_name = work_year_name;
            }

            public String getCompany_name() {
                return company_name;
            }

            public void setCompany_name(String company_name) {
                this.company_name = company_name;
            }

            public int getIs_urgent() {
                return is_urgent;
            }

            public void setIs_urgent(int is_urgent) {
                this.is_urgent = is_urgent;
            }
        }

        public static class UrlBean {
            /**
             * url_type : 1
             * url : http://www.0314job.com
             * eid : ec4bb0http://www.0314job.com
             */

            private int url_type;
            private String url;
            private String eid;

            public int getUrl_type() {
                return url_type;
            }

            public void setUrl_type(int url_type) {
                this.url_type = url_type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getEid() {
                return eid;
            }

            public void setEid(String eid) {
                this.eid = eid;
            }
        }
    }
}
