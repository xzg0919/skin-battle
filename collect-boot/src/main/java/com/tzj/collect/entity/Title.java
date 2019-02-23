package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 回收类型表
 */
@TableName("sb_title")
public class Title extends  DataEntity<Long>{

    private Long id ;

    /**
     * 回收类型名
     */
    @TableField(value = "name_")
    private String  name;

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
}
