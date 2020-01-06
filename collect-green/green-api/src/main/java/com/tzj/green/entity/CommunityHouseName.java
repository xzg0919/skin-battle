package com.tzj.green.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [社区名称关联表实体类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Data
@TableName("sb_community_house_name")
public class CommunityHouseName extends DataEntity<Long>
{
    /**
     * 主键
     */
    private Long id;
    /**
     * 社区Id
     */
    private Long communityId;
    /**
     * 小区详细地址
     */
    private String houseName;

    @TableField(value = "address_")
    private String address;

    private Integer fixedCardNum;

}
