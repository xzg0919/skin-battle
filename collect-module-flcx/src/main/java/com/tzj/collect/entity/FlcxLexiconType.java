package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * 关键字对应类型
 * @author sgmark
 * @create 2019-06-17 16:13
 **/
@TableName("flcx_lexicon_type")
public class FlcxLexiconType  {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id ;

    private Long areaId;//地区id

    private Long lexiconId;//关键字id

    private Long typeId;//类型id
    /**
     * 回收类型名
     */
    @TableField(value = "name_")
    private String  name;

    private String recover;//平台回收与否（1:回收，0:不回收）,初始值为0



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

    public String getRecover() {
        return recover;
    }

    public void setRecover(String recover) {
        this.recover = recover;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getLexiconId() {
        return lexiconId;
    }

    public void setLexiconId(Long lexiconId) {
        this.lexiconId = lexiconId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
}
