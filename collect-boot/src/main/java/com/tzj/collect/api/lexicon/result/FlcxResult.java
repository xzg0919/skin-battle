package com.tzj.collect.api.lexicon.result;

import java.util.List;
import java.util.Map;

/**
 * @author sgmark
 * @create 2019-06-19 10:39
 **/
public class FlcxResult {

    private Long id;

    private String recover;//是否回收

    private String lexicon;//查询结果关键字

    private Long lexiconId;//关键字id lexiconId

    private String parentName;//大分类名称

    private String engName;//英文名称

    private List<String> imgUrl;//子分类图片地址

    private String describe;//上描述

    private List<Map<String, Object>> nameLists;//小分类名称地址

    private String remarks;//后描述

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<String> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(List<String> imgUrl) {
        this.imgUrl = imgUrl;
    }



    public List<Map<String, Object>> getNameLists() {
        return nameLists;
    }

    public void setNameLists(List<Map<String, Object>> nameLists) {
        this.nameLists = nameLists;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLexicon() {
        return lexicon;
    }

    public void setLexicon(String lexicon) {
        this.lexicon = lexicon;
    }

    public Long getLexiconId() {
        return lexiconId;
    }

    public void setLexiconId(Long lexiconId) {
        this.lexiconId = lexiconId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecover() {
        return recover;
    }

    public void setRecover(String recover) {
        this.recover = recover;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }
}