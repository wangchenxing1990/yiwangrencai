package com.yiwangrencai.ywkj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/10/8.
 */

public class AreaLocationBean implements Serializable {

    /**
     * code : 1
     * msg :
     * data : {"cid":"331000","name":"台州市","next":1,"nexts":[{"cid":331002,"name":"椒江区","grade":3},{"cid":331003,"name":"黄岩区","grade":3},{"cid":331004,"name":"路桥区","grade":3},{"cid":331021,"name":"玉环县","grade":3},{"cid":331022,"name":"三门县","grade":3},{"cid":331023,"name":"天台县","grade":3},{"cid":331024,"name":"仙居县","grade":3},{"cid":331081,"name":"温岭市","grade":3},{"cid":331082,"name":"临海市","grade":3}]}
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
         * cid : 331000
         * name : 台州市
         * next : 1
         * nexts : [{"cid":331002,"name":"椒江区","grade":3},{"cid":331003,"name":"黄岩区","grade":3},{"cid":331004,"name":"路桥区","grade":3},{"cid":331021,"name":"玉环县","grade":3},{"cid":331022,"name":"三门县","grade":3},{"cid":331023,"name":"天台县","grade":3},{"cid":331024,"name":"仙居县","grade":3},{"cid":331081,"name":"温岭市","grade":3},{"cid":331082,"name":"临海市","grade":3}]
         */

        private String cid;
        private String name;
        private int next;
        private List<NextsBean> nexts;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNext() {
            return next;
        }

        public void setNext(int next) {
            this.next = next;
        }

        public List<NextsBean> getNexts() {
            return nexts;
        }

        public void setNexts(List<NextsBean> nexts) {
            this.nexts = nexts;
        }

        public static class NextsBean {
            /**
             * cid : 331002
             * name : 椒江区
             * grade : 3
             */

            private int cid;
            private String name;
            private int grade;

            public int getCid() {
                return cid;
            }

            public void setCid(int cid) {
                this.cid = cid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getGrade() {
                return grade;
            }

            public void setGrade(int grade) {
                this.grade = grade;
            }
        }
    }
}
