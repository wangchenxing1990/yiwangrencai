package com.yiwangrencai.ywkj.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/4.
 */

public class LangOptionBean implements Serializable {
    private String opt_name;
    private String opt_id;
    public LangOptionBean() {

    }
    public LangOptionBean(String opt_name, String opt_id) {
        this.opt_name = opt_name;
        this.opt_id = opt_id;
    }

    public String getOpt_name() {
        return opt_name;
    }

    public void setOpt_name(String opt_name) {
        this.opt_name = opt_name;
    }

    public String getOpt_id() {
        return opt_id;
    }

    public void setOpt_id(String opt_id) {
        this.opt_id = opt_id;
    }
}
