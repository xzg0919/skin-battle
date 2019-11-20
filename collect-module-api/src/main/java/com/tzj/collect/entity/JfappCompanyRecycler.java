package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.tzj.collect.entity.DataEntity;
import lombok.Data;


@TableName("sb_jfapp_company_recycler")
@Data
public class JfappCompanyRecycler extends DataEntity<Long> {

    private Long id;
    //积分回收人员id
    private String recyclerId;
    //积分回收企业id
    private String companyId;
    //状态 申请 入住  拒绝
    @TableField(value = "status_")
    private String status;


}
