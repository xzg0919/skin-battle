package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * 对应类型
 * @author sgmark
 * @create 2019-06-17 16:13
 **/
@TableName("flcx_type")
public class FlcxType extends DataEntity<Long> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id ;
    /**
     * 类型名称
     */
    @TableField(value = "name_")
    private String  name;

    private Long parent_id;//所属父类
    @TableField(value = "level_")
    private Integer level;

    private String describe;//描述

    private String isHarmful;//是否有害；1：有害

    private String imgUrl;//分类图片
   @Override
    public Long getId() {

        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
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
}
