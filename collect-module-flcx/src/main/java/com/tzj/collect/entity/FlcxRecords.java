package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询关键字记录
 * @author sgmark
 * @create 2019-06-17 16:13
 **/
@TableName("flcx_records")
public class FlcxRecords implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id ;

    private String lexicons;//搜索关键字(模糊搜索，存的就是搜索前的值)

    @TableField(value = "lexicon_after")
    private String lexiconAfter;//返回结果

    @TableField(value = "lexicons_id")
    private Long lexiconsId;//关键字id

    @TableField(value = "ali_user_id")
    private String aliUserId;//阿里会员id

    private String city;    //城市


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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @TableField(value = "create_date")
    private Date createDate; // 创建日期

    @TableField(value = "update_date")
    private Date updateDate; // 更新日期

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}

