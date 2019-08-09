package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

@TableName("sb_xcx_source_num")
@Data
public class XcxSourceNum  extends DataEntity<Long>
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
     * 出现的次数
     */
    private Integer num;
    /**
     * 出现的次数
     */
    private Date times;
    /**
     * url的code
     */
    @TableField(value = "name_")
    private String name;


}
