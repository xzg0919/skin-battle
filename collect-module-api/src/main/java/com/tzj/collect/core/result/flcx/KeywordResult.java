package com.tzj.collect.core.result.flcx;

import java.io.Serializable;

/**
 * 关键字查询
 *
 * @author sgmark
 * @create 2019-06-25 16:27
 **/
public class KeywordResult implements Serializable {
    //返回的关键字
    private  String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
