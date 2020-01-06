package com.tzj.green.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [积分流水明细实体类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Data
@TableName("sb_points_list_item")
public class PointsListItem extends DataEntity<Long>
{
    /**
     * 主键
     */
    private Long id;
    /**
     * 积分流水id
     */
    private Long pointsListId;
    /**
     * 分类id
     */
    private Long categoryId;
    /**
     * 分类名称
     */
    private String categoryName;
    /**
     * 父级编号
     */
    private Long parentId;
    /**
     * '父类name'
     */
    private String parentName;
    /**
     * 分类id的parent_ids
     */
    private String parentIds;
    /**
     * 积分数量
     */
    private Long points;
    /**
     * 数量
     */
    private Long amount;



}
