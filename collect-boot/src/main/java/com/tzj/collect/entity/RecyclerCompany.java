package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 *
 * 回收人员和企业关联表
 *
 * @Author 王灿
 **/
@TableName("sb_recycler_company")
@Data
public class RecyclerCompany extends DataEntity<Long> {
    private Long id;
    /**
     * 回收人员Id
     */
    private Integer recyclerId;
    /**
     * 回收企业Id
     */
    private Integer companyId;
    /**
     * 状态 0申请 1入住  2拒绝'
     */
    @TableField(value = "status_")
    private String status = "0";
    /**
     *申请的类型1家电、2生活垃圾、4大件'
     */
    private String title;
    /**
     *是否企业经理 1 经理 0 普通员工
     */
    private String isManager;
    /**
     *回收经理所在城市
     */
    private Integer city;
    /**
     *回收人员所在省份
     */
    private Integer province;
    /**
     *属于哪位业务经理 业务经理为空
     */
    private Integer parentsId;
}
