package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * 对应类型
 * @author sgmark
 * @create 2019-06-17 16:13
 **/
@TableName("flcx_img")
public class FlcxImg extends DataEntity<Long>{



    @TableId(value = "id", type = IdType.AUTO)
    private Long id ;
    /**
     * 分类名称
     */
    @TableField(value = "name_")
    private String  name;

    private Long typeId;//所属父类

    private String imgUrl;//分类图片

    public Long getId() {

        return id;
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

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
