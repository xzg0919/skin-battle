package com.tzj.green.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.green.entity.CompanyCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [公司分类关联表映射类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
public interface CompanyCategoryMapper extends BaseMapper<CompanyCategory>
{

    List<Map<String,Object>> getCompanyCategoryById(@Param("companyId") Long companyId,@Param("parentId") String parentId,@Param("level") String level);

    List<Map<String,Object>> getAppCompanyCategoryById(@Param("companyId") Long companyId,@Param("parentId") String parentId,@Param("level") String level);

}