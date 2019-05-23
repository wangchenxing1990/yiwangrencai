package com.yiwangrencai.ywkj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/10/31.
 */

public class PreviewResumeBean implements Serializable {

    /**
     * code : 1
     * msg :
     * data : {"id":"724516","resume_name":"我的简历","language":"cn","name":"朱晓兵1","sex":"1","expectedsalary":8000,"isexpectedsalary":"1","intentionjobs":"安卓","introduction":"低谷和 v 的都看看我家开的代扣代缴大家都会大喊大叫的并不是大口大口军事基地呢宝贝不会自我我南水北调你不好的单家独户吧","mobile":"18317770484","email":"18317770484@163.com","qq":"","top_time":null,"chkphoto_open":"0","avatar":"personalavatar/201710/1507525703bjebf.jpg","address":"","job_status":"1","resume_status":"4","bkresume_status":"2","review":1,"marital":"0","height":"176","place_name":"","longitude":"","latitude":"","xs_status":1,"birthday_name":"1992-10","work_year_name":"2年以上","homeaddress_name":"椒江区","expectedsalary_name":"8000","jobsort_name":"android","jobarea_name":"椒江区,黄岩区,路桥区","education_name":"硕士","census_name":"路桥区","age":"25","resume_workexp":[{"id":710692,"company":"一览云","industry":6,"comkind":4,"scale":"200","starttime":"2013-07-01","endtime":"2015-07-01","post":"系统控制与运维","content":"头艾肯最测网速科目咯","starttime_name":"2013-07","endtime_name":"2015-07","industry_name":"家具、家电、工艺品、玩具","comkind_name":"私营企业","scale_name":"50-200人"},{"id":714092,"company":"人民网","industry":4,"comkind":4,"scale":"1000","starttime":"2011-10-01","endtime":"2016-10-01","post":"特空","content":"了它恩行我也看见了","starttime_name":"2011-10","endtime_name":"2016-10","industry_name":"电子、微电子技术","comkind_name":"私营企业","scale_name":"500-1000人"}],"resume_eduexp":[],"resume_proexp":[{"id":5,"project_name":"促销活动","starttime":"2013-01-01","endtime":"2015-02-01","post":"是风风光光个","content":"x\u2006ch\u2006j\u2006g\u2006c\u2006x\u2006x\u2006f\u2006j\u2006k\u2006b\u2006c\u2006x\u2006f\u2006h\u2006hv\u2006c\u2006cv\u2006bui77","starttime_name":"2013-01","endtime_name":"2015-02"}],"resume_skill":[{"id":563,"skillname":"hhjjjj","degree":3},{"id":18,"skillname":"常规将举办","degree":2}],"resume_cer":[{"id":20510,"certificate_name":"我下个刚刚好","gettime":"2016-01-02","gettime_name":"2016年"},{"id":19799,"certificate_name":"对法院改革","gettime":"2014-01-01","gettime_name":"2014年"},{"id":20511,"certificate_name":"78","gettime":"1901-01-01","gettime_name":"1901年"}],"resume_lang":[{"id":265511,"language":1,"degree":2,"level":0,"language_name":"普通话","level_name":""},{"id":265510,"language":4,"degree":2,"level":44,"language_name":"日语","level_name":"专业八级"},{"id":264970,"language":3,"degree":3,"level":33,"language_name":"英语","level_name":"CET4"}],"resume_other":[{"id":273,"title":"兴趣爱好","content":"llll"},{"id":7,"title":"宗教信仰","content":"xfhujbvvvbjkjb"}]}
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
         * id : 724516
         * resume_name : 我的简历
         * language : cn
         * name : 朱晓兵1
         * sex : 1
         * expectedsalary : 8000
         * isexpectedsalary : 1
         * intentionjobs : 安卓
         * introduction : 低谷和 v 的都看看我家开的代扣代缴大家都会大喊大叫的并不是大口大口军事基地呢宝贝不会自我我南水北调你不好的单家独户吧
         * mobile : 18317770484
         * email : 18317770484@163.com
         * qq :
         * top_time : null
         * chkphoto_open : 0
         * avatar : personalavatar/201710/1507525703bjebf.jpg
         * address :
         * job_status : 1
         * resume_status : 4
         * bkresume_status : 2
         * review : 1
         * marital : 0
         * height : 176
         * place_name :
         * longitude :
         * latitude :
         * xs_status : 1
         * birthday_name : 1992-10
         * work_year_name : 2年以上
         * homeaddress_name : 椒江区
         * expectedsalary_name : 8000
         * jobsort_name : android
         * jobarea_name : 椒江区,黄岩区,路桥区
         * education_name : 硕士
         * census_name : 路桥区
         * age : 25
         * resume_workexp : [{"id":710692,"company":"一览云","industry":6,"comkind":4,"scale":"200","starttime":"2013-07-01","endtime":"2015-07-01","post":"系统控制与运维","content":"头艾肯最测网速科目咯","starttime_name":"2013-07","endtime_name":"2015-07","industry_name":"家具、家电、工艺品、玩具","comkind_name":"私营企业","scale_name":"50-200人"},{"id":714092,"company":"人民网","industry":4,"comkind":4,"scale":"1000","starttime":"2011-10-01","endtime":"2016-10-01","post":"特空","content":"了它恩行我也看见了","starttime_name":"2011-10","endtime_name":"2016-10","industry_name":"电子、微电子技术","comkind_name":"私营企业","scale_name":"500-1000人"}]
         * resume_eduexp : []
         * resume_proexp : [{"id":5,"project_name":"促销活动","starttime":"2013-01-01","endtime":"2015-02-01","post":"是风风光光个","content":"x\u2006ch\u2006j\u2006g\u2006c\u2006x\u2006x\u2006f\u2006j\u2006k\u2006b\u2006c\u2006x\u2006f\u2006h\u2006hv\u2006c\u2006cv\u2006bui77","starttime_name":"2013-01","endtime_name":"2015-02"}]
         * resume_skill : [{"id":563,"skillname":"hhjjjj","degree":3},{"id":18,"skillname":"常规将举办","degree":2}]
         * resume_cer : [{"id":20510,"certificate_name":"我下个刚刚好","gettime":"2016-01-02","gettime_name":"2016年"},{"id":19799,"certificate_name":"对法院改革","gettime":"2014-01-01","gettime_name":"2014年"},{"id":20511,"certificate_name":"78","gettime":"1901-01-01","gettime_name":"1901年"}]
         * resume_lang : [{"id":265511,"language":1,"degree":2,"level":0,"language_name":"普通话","level_name":""},{"id":265510,"language":4,"degree":2,"level":44,"language_name":"日语","level_name":"专业八级"},{"id":264970,"language":3,"degree":3,"level":33,"language_name":"英语","level_name":"CET4"}]
         * resume_other : [{"id":273,"title":"兴趣爱好","content":"llll"},{"id":7,"title":"宗教信仰","content":"xfhujbvvvbjkjb"}]
         */

        private String id;
        private String resume_name;
        private String language;
        private String name;
        private String sex;
        private int expectedsalary;
        private String isexpectedsalary;
        private String intentionjobs;
        private String introduction;
        private String mobile;
        private String email;
        private String qq;
        private Object top_time;
        private String chkphoto_open;
        private String avatar;
        private String address;
        private String job_status;
        private String resume_status;
        private String bkresume_status;
        private int review;
        private String marital;
        private String height;
        private String place_name;
        private String longitude;
        private String latitude;
        private int xs_status;
        private String birthday_name;
        private String work_year_name;
        private String homeaddress_name;
        private String expectedsalary_name;
        private String jobsort_name;
        private String jobarea_name;
        private String education_name;
        private String census_name;
        private String age;
        private List<ResumeWorkexpBean> resume_workexp;
        private List<?> resume_eduexp;
        private List<ResumeProexpBean> resume_proexp;
        private List<ResumeSkillBean> resume_skill;
        private List<ResumeCerBean> resume_cer;
        private List<ResumeLangBean> resume_lang;
        private List<ResumeOtherBean> resume_other;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getResume_name() {
            return resume_name;
        }

        public void setResume_name(String resume_name) {
            this.resume_name = resume_name;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getExpectedsalary() {
            return expectedsalary;
        }

        public void setExpectedsalary(int expectedsalary) {
            this.expectedsalary = expectedsalary;
        }

        public String getIsexpectedsalary() {
            return isexpectedsalary;
        }

        public void setIsexpectedsalary(String isexpectedsalary) {
            this.isexpectedsalary = isexpectedsalary;
        }

        public String getIntentionjobs() {
            return intentionjobs;
        }

        public void setIntentionjobs(String intentionjobs) {
            this.intentionjobs = intentionjobs;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public Object getTop_time() {
            return top_time;
        }

        public void setTop_time(Object top_time) {
            this.top_time = top_time;
        }

        public String getChkphoto_open() {
            return chkphoto_open;
        }

        public void setChkphoto_open(String chkphoto_open) {
            this.chkphoto_open = chkphoto_open;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getJob_status() {
            return job_status;
        }

        public void setJob_status(String job_status) {
            this.job_status = job_status;
        }

        public String getResume_status() {
            return resume_status;
        }

        public void setResume_status(String resume_status) {
            this.resume_status = resume_status;
        }

        public String getBkresume_status() {
            return bkresume_status;
        }

        public void setBkresume_status(String bkresume_status) {
            this.bkresume_status = bkresume_status;
        }

        public int getReview() {
            return review;
        }

        public void setReview(int review) {
            this.review = review;
        }

        public String getMarital() {
            return marital;
        }

        public void setMarital(String marital) {
            this.marital = marital;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getPlace_name() {
            return place_name;
        }

        public void setPlace_name(String place_name) {
            this.place_name = place_name;
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

        public int getXs_status() {
            return xs_status;
        }

        public void setXs_status(int xs_status) {
            this.xs_status = xs_status;
        }

        public String getBirthday_name() {
            return birthday_name;
        }

        public void setBirthday_name(String birthday_name) {
            this.birthday_name = birthday_name;
        }

        public String getWork_year_name() {
            return work_year_name;
        }

        public void setWork_year_name(String work_year_name) {
            this.work_year_name = work_year_name;
        }

        public String getHomeaddress_name() {
            return homeaddress_name;
        }

        public void setHomeaddress_name(String homeaddress_name) {
            this.homeaddress_name = homeaddress_name;
        }

        public String getExpectedsalary_name() {
            return expectedsalary_name;
        }

        public void setExpectedsalary_name(String expectedsalary_name) {
            this.expectedsalary_name = expectedsalary_name;
        }

        public String getJobsort_name() {
            return jobsort_name;
        }

        public void setJobsort_name(String jobsort_name) {
            this.jobsort_name = jobsort_name;
        }

        public String getJobarea_name() {
            return jobarea_name;
        }

        public void setJobarea_name(String jobarea_name) {
            this.jobarea_name = jobarea_name;
        }

        public String getEducation_name() {
            return education_name;
        }

        public void setEducation_name(String education_name) {
            this.education_name = education_name;
        }

        public String getCensus_name() {
            return census_name;
        }

        public void setCensus_name(String census_name) {
            this.census_name = census_name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public List<ResumeWorkexpBean> getResume_workexp() {
            return resume_workexp;
        }

        public void setResume_workexp(List<ResumeWorkexpBean> resume_workexp) {
            this.resume_workexp = resume_workexp;
        }

        public List<?> getResume_eduexp() {
            return resume_eduexp;
        }

        public void setResume_eduexp(List<?> resume_eduexp) {
            this.resume_eduexp = resume_eduexp;
        }

        public List<ResumeProexpBean> getResume_proexp() {
            return resume_proexp;
        }

        public void setResume_proexp(List<ResumeProexpBean> resume_proexp) {
            this.resume_proexp = resume_proexp;
        }

        public List<ResumeSkillBean> getResume_skill() {
            return resume_skill;
        }

        public void setResume_skill(List<ResumeSkillBean> resume_skill) {
            this.resume_skill = resume_skill;
        }

        public List<ResumeCerBean> getResume_cer() {
            return resume_cer;
        }

        public void setResume_cer(List<ResumeCerBean> resume_cer) {
            this.resume_cer = resume_cer;
        }

        public List<ResumeLangBean> getResume_lang() {
            return resume_lang;
        }

        public void setResume_lang(List<ResumeLangBean> resume_lang) {
            this.resume_lang = resume_lang;
        }

        public List<ResumeOtherBean> getResume_other() {
            return resume_other;
        }

        public void setResume_other(List<ResumeOtherBean> resume_other) {
            this.resume_other = resume_other;
        }

        public static class ResumeWorkexpBean {
            /**
             * id : 710692
             * company : 一览云
             * industry : 6
             * comkind : 4
             * scale : 200
             * starttime : 2013-07-01
             * endtime : 2015-07-01
             * post : 系统控制与运维
             * content : 头艾肯最测网速科目咯
             * starttime_name : 2013-07
             * endtime_name : 2015-07
             * industry_name : 家具、家电、工艺品、玩具
             * comkind_name : 私营企业
             * scale_name : 50-200人
             */

            private int id;
            private String company;
            private int industry;
            private int comkind;
            private String scale;
            private String starttime;
            private String endtime;
            private String post;
            private String content;
            private String starttime_name;
            private String endtime_name;
            private String industry_name;
            private String comkind_name;
            private String scale_name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public int getIndustry() {
                return industry;
            }

            public void setIndustry(int industry) {
                this.industry = industry;
            }

            public int getComkind() {
                return comkind;
            }

            public void setComkind(int comkind) {
                this.comkind = comkind;
            }

            public String getScale() {
                return scale;
            }

            public void setScale(String scale) {
                this.scale = scale;
            }

            public String getStarttime() {
                return starttime;
            }

            public void setStarttime(String starttime) {
                this.starttime = starttime;
            }

            public String getEndtime() {
                return endtime;
            }

            public void setEndtime(String endtime) {
                this.endtime = endtime;
            }

            public String getPost() {
                return post;
            }

            public void setPost(String post) {
                this.post = post;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getStarttime_name() {
                return starttime_name;
            }

            public void setStarttime_name(String starttime_name) {
                this.starttime_name = starttime_name;
            }

            public String getEndtime_name() {
                return endtime_name;
            }

            public void setEndtime_name(String endtime_name) {
                this.endtime_name = endtime_name;
            }

            public String getIndustry_name() {
                return industry_name;
            }

            public void setIndustry_name(String industry_name) {
                this.industry_name = industry_name;
            }

            public String getComkind_name() {
                return comkind_name;
            }

            public void setComkind_name(String comkind_name) {
                this.comkind_name = comkind_name;
            }

            public String getScale_name() {
                return scale_name;
            }

            public void setScale_name(String scale_name) {
                this.scale_name = scale_name;
            }
        }

        public static class ResumeProexpBean {
            /**
             * id : 5
             * project_name : 促销活动
             * starttime : 2013-01-01
             * endtime : 2015-02-01
             * post : 是风风光光个
             * content : x ch j g c x x f j k b c x f h hv c cv bui77
             * starttime_name : 2013-01
             * endtime_name : 2015-02
             */

            private int id;
            private String project_name;
            private String starttime;
            private String endtime;
            private String post;
            private String content;
            private String starttime_name;
            private String endtime_name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getProject_name() {
                return project_name;
            }

            public void setProject_name(String project_name) {
                this.project_name = project_name;
            }

            public String getStarttime() {
                return starttime;
            }

            public void setStarttime(String starttime) {
                this.starttime = starttime;
            }

            public String getEndtime() {
                return endtime;
            }

            public void setEndtime(String endtime) {
                this.endtime = endtime;
            }

            public String getPost() {
                return post;
            }

            public void setPost(String post) {
                this.post = post;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getStarttime_name() {
                return starttime_name;
            }

            public void setStarttime_name(String starttime_name) {
                this.starttime_name = starttime_name;
            }

            public String getEndtime_name() {
                return endtime_name;
            }

            public void setEndtime_name(String endtime_name) {
                this.endtime_name = endtime_name;
            }
        }

        public static class ResumeSkillBean {
            /**
             * id : 563
             * skillname : hhjjjj
             * degree : 3
             */

            private int id;
            private String skillname;
            private int degree;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getSkillname() {
                return skillname;
            }

            public void setSkillname(String skillname) {
                this.skillname = skillname;
            }

            public int getDegree() {
                return degree;
            }

            public void setDegree(int degree) {
                this.degree = degree;
            }
        }

        public static class ResumeCerBean {
            /**
             * id : 20510
             * certificate_name : 我下个刚刚好
             * gettime : 2016-01-02
             * gettime_name : 2016年
             */

            private int id;
            private String certificate_name;
            private String gettime;
            private String gettime_name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCertificate_name() {
                return certificate_name;
            }

            public void setCertificate_name(String certificate_name) {
                this.certificate_name = certificate_name;
            }

            public String getGettime() {
                return gettime;
            }

            public void setGettime(String gettime) {
                this.gettime = gettime;
            }

            public String getGettime_name() {
                return gettime_name;
            }

            public void setGettime_name(String gettime_name) {
                this.gettime_name = gettime_name;
            }
        }

        public static class ResumeLangBean {
            /**
             * id : 265511
             * language : 1
             * degree : 2
             * level : 0
             * language_name : 普通话
             * level_name :
             */

            private int id;
            private int language;
            private int degree;
            private int level;
            private String language_name;
            private String level_name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getLanguage() {
                return language;
            }

            public void setLanguage(int language) {
                this.language = language;
            }

            public int getDegree() {
                return degree;
            }

            public void setDegree(int degree) {
                this.degree = degree;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public String getLanguage_name() {
                return language_name;
            }

            public void setLanguage_name(String language_name) {
                this.language_name = language_name;
            }

            public String getLevel_name() {
                return level_name;
            }

            public void setLevel_name(String level_name) {
                this.level_name = level_name;
            }
        }

        public static class ResumeOtherBean {
            /**
             * id : 273
             * title : 兴趣爱好
             * content : llll
             */

            private int id;
            private String title;
            private String content;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
