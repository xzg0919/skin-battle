package com.tzj.green.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [活动回收人员关联表实体类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Data
@TableName("sb_product_recycler")
public class ProductRecycler extends DataEntity<Long>
{
    /**
     * 主键
     */
    private Long id;
    /**
     * 公司id
     */
    private Long productId;
    /**
     * 回收人员id
     */
    private Long recyclersId;


}
