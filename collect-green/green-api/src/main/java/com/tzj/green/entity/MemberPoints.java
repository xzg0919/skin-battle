package com.tzj.green.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [用户积分表实体类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Data
@TableName("sb_member_points")
public class MemberPoints extends DataEntity<Long>
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
     * 积分总数量
     */
    private Long tatalPoints;
    /**
     * 积分剩余数量
     */
    private Long remnantPoints;


}
