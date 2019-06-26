package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * 查询关键字
 * @author sgmark
 * @create 2019-06-17 16:13
 **/
@TableName("flcx_lexicon")
public class FlcxLexicon extends DataEntity<Long> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id ;

    /**
     * 回收类型名
     */
    @TableField(value = "name_")
    private String  name;

    private String  engName;//英文名称

    private String recover;//平台回收与否（1:回收，0:不回收）,初始值为0


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

    public String getRecover() {
        return recover;
    }

    public void setRecover(String recover) {
        this.recover = recover;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }
}
