package com.tzj.collect.api.lexicon.param;

/**
 * @author sgmark
 * @create 2019-06-19 10:31
 **/
public class FlcxBean {

    private String name;//查询名称

    private Long typeId = 0L;//类型id

    private String aliUserId;//用户id

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliUserId() {
        return aliUserId;
    }

    public void setAliUserId(String aliUserId) {
        this.aliUserId = aliUserId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
}
