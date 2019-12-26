package com.tzj.collect.core.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.YhTest;

/**
 * 
 * <p>Created on 2019年6月7日</p>
 * <p>Title:       [杭州绿账]_[]_[]</p>
 * <p>Description: []</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢][yanghuan1937@aliyun.com]
 * @version        1.0
 */
public interface YhTestService extends IService<YhTest>
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
     * <p>Description:[生成代码]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return String
     */
    String makeCode(String tableInfo,String auth);
}
