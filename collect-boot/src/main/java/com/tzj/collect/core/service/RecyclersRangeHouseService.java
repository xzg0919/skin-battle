package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.RecyclersRangeHouse;

import java.util.List;
import java.util.Map;

public interface RecyclersRangeHouseService extends IService<RecyclersRangeHouse> {

    @DS("slave")
    Map<String,Object> companyAreaRecyclerRanges(String companyId);

    @DS("slave")
    Object getAreaRecyclersRange(String cityId, String recycleId, String companyId);

    /**
     * 根据市级Id和回收人员id获取街道、小区信息
     * @author wangcan
     * @param
     * @return
     */
    @DS("slave")
    Object getStreeRecyclersRange(String areaId, String recycleId, String companyId);

    /**
     * 根据街道Id获取小区信息
     * @author wangcan
     * @param
     * @return
     */
    @DS("slave")
    List<Map<String,Object>> getCommunityRecyclersRange(String streetId, String recycleId, String companyId);

}
