package com.tzj.green.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("sb_logs")
public class Logs extends DataEntity<Long>
{
    /**
     * 主键
     */
    private Long id;
    /**
     * 企业id
     */
    private Integer companyId;
    /**
     * 反参
     */
    private String body;
    /**
     * 请求参数
     */
    private String param;

}
