package com.tzj.green.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [公司分类关联表实体类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Data
@TableName("sb_company_category")
public class CompanyCategory extends DataEntity<Long>
{
    /**
     * 主键
     */
    private Long id;
    /**
     * 公司id
     */
    private Long companyId;
    /**
     * 分类id
     */
    private Long categoryId;
    /**
     * 父级编号
     */
    private Long parentId;
    /**
     * 
     */
    private String parentName;
    /**
     * 所有父级编号
     */
    private String parentIds;
    /**
     * 增值积分
     */
    private Long addPoints;
    /**
     * 减值积分
     */
    private Long subtractPoints;
    /**
     * 计量单位
     */
    private String unit;

}
