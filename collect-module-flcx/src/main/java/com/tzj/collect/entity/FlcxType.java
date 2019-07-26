package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 对应类型
 * @author sgmark
 * @create 2019-06-17 16:13
 **/
@TableName("flcx_type")
public class FlcxType extends DataEntity<Long>{
    @TableId(value = "id", type = IdType.AUTO)
    private Long id ;
    /**
     * 类型名称
     */
    @TableField(value = "name_")
    private String  name;

    private String  engName;//英文名称

    private Long parentId;//所属父类
    @TableField(value = "level_")
    private Integer level;
    @TableField(value = "sort_")
    private Integer sort;// 排序

    private String describe;//描述

    private String isHarmful;//是否有害；1：有害

    private String imgUrl;//分类图片

    public Long getId() {

        return id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getIsHarmful() {
        return isHarmful;
    }

    public void setIsHarmful(String isHarmful) {
        this.isHarmful = isHarmful;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
