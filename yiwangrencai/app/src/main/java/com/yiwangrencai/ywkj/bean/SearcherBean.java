package com.yiwangrencai.ywkj.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/22.
 */

public class SearcherBean implements Serializable {
    private String id;
    private String search_name;
    private String updated_at;

    public SearcherBean(String id, String search_name, String updated_at) {
        this.id = id;
        this.search_name = search_name;
        this.updated_at = updated_at;
    }
    public SearcherBean() {

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getSearch_name() {
        return search_name;
    }

    public void setSearch_name(String search_name) {
        this.search_name = search_name;
    }
}
