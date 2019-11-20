package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.flcx.FlcxBean;
import com.tzj.collect.core.param.flcx.FlcxTypeBean;
import com.tzj.collect.entity.FlcxType;
import com.tzj.module.easyopen.exception.ApiException;

import java.util.List;
import java.util.Map;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param 
  * @return 
  */
public interface FlcxTypeService extends IService<FlcxType> {
    @DS("slave")
    Map typeList(FlcxBean flcxBean);

    void inputLinAndType(List<Map<String, String>> mapList);
    void updateType(List<Map<String, String>> mapList);

    /**
     * 根据层级获取该层级的所有分类
     * @param typeBean
     * @return
     * @throws ApiException
     */
    @DS("slave")
    Map listAllByType(FlcxTypeBean typeBean);

    /**
     * 根据分类上级id查询 所有子分类
     * @param typeBean
     * @return
     * @throws ApiException
     */
    Map findTypeByParent(FlcxTypeBean typeBean);

}
