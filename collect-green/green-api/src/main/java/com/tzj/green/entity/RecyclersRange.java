package com.tzj.green.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [回收人员服务范围表实体类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Data
@TableName("sb_recyclers_range")
public class RecyclersRange extends DataEntity<Long>
{
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 回收人员id
     */
    private Long recyclersId;
    /**
     * 所属的企业Id
     */
    private Long companyId;
    /**
     * 所属居委Id
     */
    private Long communityId;
    /**
     * 所属小区Id
     */
    private Long communityHouseId;


}
