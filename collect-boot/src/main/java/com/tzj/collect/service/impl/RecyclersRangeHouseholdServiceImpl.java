package com.tzj.collect.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.RecyclersRangeHousehold;
import com.tzj.collect.mapper.RecyclersRangeHouseholdMapper;
import com.tzj.collect.service.RecyclersRangeHouseholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly = true)
public class RecyclersRangeHouseholdServiceImpl extends ServiceImpl<RecyclersRangeHouseholdMapper, RecyclersRangeHousehold> implements RecyclersRangeHouseholdService {
    @Autowired
    private RecyclersRangeHouseholdMapper recyclersRangeHouseholdMapper;

    /**
     * 根据市级Id和回收人员id获取区域信息
     * @author wangcan
     * @param
     * @return
     */
    @Override
    @DS("slave")
    public Object getAreaRecyclersRange(String cityId,String recycleId,String companyId){
        return recyclersRangeHouseholdMapper.getAreaRecyclersRange(cityId,recycleId,companyId);
    }
    /**
     * 根据市级Id和回收人员id获取街道，小区信息
     * @author wangcan
     * @param
     * @return
     */
    @Override
    @DS("slave")
    public Object getStreeRecyclersRange(String areaId,String recycleId,String companyId){
        return recyclersRangeHouseholdMapper.getStreeRecyclersRange(areaId,recycleId,companyId);
    }

    /**
     * 根据街道Id获取小区信息
     * @author wangcan
     * @param
     * @return
     */
    @Override
    @DS("slave")
    public List<Map<String,Object>> getCommunityRecyclersRange(String streetId, String recycleId, String companyId){
        return recyclersRangeHouseholdMapper.getCommunityRecyclersRange(streetId,recycleId,companyId);
    }
    @Override
    @DS("slave")
    public Integer selectAreaRangeCount(String companyId,String recyclerId){
        return recyclersRangeHouseholdMapper.selectAreaRangeCount(companyId,recyclerId);
    }
}
