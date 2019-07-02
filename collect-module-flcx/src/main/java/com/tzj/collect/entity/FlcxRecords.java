package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * 查询关键字记录
 * @author sgmark
 * @create 2019-06-17 16:13
 **/
@TableName("flcx_records")
public class FlcxRecords  {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id ;

    private String lexicons;//搜索关键字(模糊搜索，存的就是搜索前的值)

    private String lexiconAfter;//返回结果

    private Long lexiconsId;//关键字id

    private String aliUserId;//阿里会员id


    public Long getId() {

        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public String getLexicons() {
        return lexicons;
    }

    public void setLexicons(String lexicons) {
        this.lexicons = lexicons;
    }

    public Long getLexiconsId() {
        return lexiconsId;
    }

    public void setLexiconsId(Long lexiconsId) {
        this.lexiconsId = lexiconsId;
    }

    public String getAliUserId() {
        return aliUserId;
    }

    public void setAliUserId(String aliUserId) {
        this.aliUserId = aliUserId;
    }

    public String getLexiconAfter() {
        return lexiconAfter;
    }

    public void setLexiconAfter(String lexiconAfter) {
        this.lexiconAfter = lexiconAfter;
    }

}

