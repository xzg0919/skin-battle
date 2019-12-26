package com.tzj.collect.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.YhTest;

public interface YhTestMapper extends BaseMapper<YhTest>
{

    /**
     * <p>Created on 2019年6月6日</p>
     * <p>Description:[获取所有表]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return List<YhTest>
     */
    List<YhTest> getAllTables();

    /**
     * <p>Created on 2019年6月6日</p>
     * <p>Description:[获取列信息]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return List<YhTest>
     */
    List<YhTest> getAllColumns(@Param("tableName")String tableName);
}
