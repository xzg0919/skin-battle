package com.tzj.collect.core.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.RecyclersRangeHouseMapper;
import com.tzj.collect.core.service.RecyclersRangeHouseService;
import com.tzj.collect.entity.RecyclersRangeHouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly=true)
public class RecyclersRangeHouseServiceImpl extends ServiceImpl<RecyclersRangeHouseMapper, RecyclersRangeHouse> implements RecyclersRangeHouseService {

    @Autowired
    private RecyclersRangeHouseMapper recyclersRangeHouseMapper;


    @Override
    public Map<String,Object> companyAreaRecyclerRanges(String companyId){
        return recyclersRangeHouseMapper.companyAreaRecyclerRanges(companyId);
    }

    @Override
    public Object getAreaRecyclersRange(String cityId,String recycleId,String companyId){
        return recyclersRangeHouseMapper.getAreaRecyclersRange(cityId,recycleId,companyId);
    }

    /**
     * 根据市级Id和回收人员id获取街道，小区信息
     * @author wangcan
     * @param
     * @return
     */
    @Override
    public Object getStreeRecyclersRange(String areaId,String recycleId,String companyId){
        return recyclersRangeHouseMapper.getStreeRecyclersRange(areaId,recycleId,companyId);
    }

    /**
     * 根据街道Id获取小区信息
     * @author wangcan
     * @param
     * @return
     */
    @Override
    public List<Map<String,Object>> getCommunityRecyclersRange(String streetId, String recycleId, String companyId){
        return recyclersRangeHouseMapper.getCommunityRecyclersRange(streetId,recycleId,companyId);
    }

    public List<Map<String,Object>> getRecyclerAreaByTitleId(String recyclerId,String companyId){
        return recyclersRangeHouseMapper.getRecyclerAreaByTitleId(recyclerId,companyId);
    }

    public List<Map<String,Object>> getRecyclerStreetByTitleId(String recyclerId,String companyId,String areaId){
        return recyclersRangeHouseMapper.getRecyclerStreetByTitleId(recyclerId,companyId,areaId);
    }
}
