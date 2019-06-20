package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.RecyclersRangeHousehold;

import java.util.List;
import java.util.Map;

public interface RecyclersRangeHouseholdService extends IService<RecyclersRangeHousehold> {

    /**
     * 根据市级Id和回收人员id获取区域信息
     * @author wangcan
     * @param
     * @return
     */
    Object getAreaRecyclersRange(String cityId,String recycleId,String companyId);
    /**
     * 根据市级Id和回收人员id获取街道、小区信息
     * @author wangcan
     * @param
     * @return
     */
    Object getStreeRecyclersRange(String areaId,String recycleId,String companyId);
    /**
     * 根据街道Id获取小区信息
     * @author wangcan
     * @param
     * @return
     */
    List<Map<String,Object>> getCommunityRecyclersRange(String streetId, String recycleId, String companyId);

    Integer selectAreaRangeCount(String companyId,String recyclerId);

}
