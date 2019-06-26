package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * 彩蛋
 * @author sgmark
 * @create 2019-06-17 16:13
 **/
@TableName("flcx_eggshell")
public class FlcxEggshell extends DataEntity<Long> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id ;
    /**
     * 分类名称
     */
    @TableField(value = "lexicon_")
    private String  lexicon;//搜索关键字

    @TableField(value = "describe_")
    private String describe;//彩蛋描述

   @Override
    public Long getId() {

        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getLexicon() {
        return lexicon;
    }

    public void setLexicon(String lexicon) {
        this.lexicon = lexicon;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
