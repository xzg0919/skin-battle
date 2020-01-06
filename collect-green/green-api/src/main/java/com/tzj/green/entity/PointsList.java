package com.tzj.green.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [积分流水表实体类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Data
@TableName("sb_points_list")
public class PointsList extends DataEntity<Long>
{
    /**
     * 主键
     */
    private Long id;
    /**
     * 所属的企业Id
     */
    private Long companyId;
    /**
     * 所属回收人员Id
     */
    private Long recyclerId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户卡号
     */
    private String userNo;
    /**
     * userid
     */
    private String aliUserId;
    /**
     * 积分变动数量
     */
    private Long points;
    /**
     * 积分变动类型
     */
    private Long pointsType;
    /**
     * 积分变动说明
     */
    private Long pointsReason;


}
