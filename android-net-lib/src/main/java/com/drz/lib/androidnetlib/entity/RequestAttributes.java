package com.drz.lib.androidnetlib.entity;


/**
 * Created by Administrator on 2017/2/16 0016.
 */

public class RequestAttributes {
    public String url;
    public Integer id;
    public String type;
    public Long startTime;
    public Long endTime;
    public int stateCode;
    public  Object tag;

    public RequestAttributes() {
    }

    public RequestAttributes(String url, Integer id, String type, Long startTime, Long endTime, int stateCode, Object tag) {
        this.url = url;
        this.id = id;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.stateCode = stateCode;
        this.tag = tag;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }
}
