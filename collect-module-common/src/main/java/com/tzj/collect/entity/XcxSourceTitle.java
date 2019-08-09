package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@TableName("sb_xcx_source_title")
@Data
public class XcxSourceTitle extends DataEntity<Long>
{
    /**
     *  id
     */
    private Long id;
    /**
     * url的code
     */
    private String code;
    /**
     * url的code
     */
    @TableField(value = "name_")
    private String name;


}
