package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 关键字对应类型
 * @author sgmark
 * @create 2019-06-17 16:13
 **/
@TableName("flcx_lexicon_type")
@Data
public class FlcxLexiconType  extends DataEntity<Long> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id ;

    private Long areaId;//地区id

    private Long lexiconId;//关键字id

    private Long typeId;//类型id

    private Long parentId;//类型父级id

    public Long getId() {

        return id;
    }

}
