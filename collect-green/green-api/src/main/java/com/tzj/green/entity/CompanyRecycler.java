package com.tzj.green.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [回收人员和企业关联表实体类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Data
@TableName("sb_company_recycler")
public class CompanyRecycler extends DataEntity<Long>
{
    /**
     * 主键
     */
    private Long id;
    /**
     * 回收人员id
     */
    private Long recyclerId;
    /**
     * 回收企业id
     */
    private Long companyId;
    /**
     * 状态 申请 入住  拒绝
     */
    @TableField(value = "status_")
    private String status;
    /**
     * 身份级别 0社区分类员   1社区回收员   2社区管理员
     */
    private String cardType;

}
