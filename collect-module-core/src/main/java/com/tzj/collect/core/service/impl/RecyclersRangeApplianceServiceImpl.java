package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.RecyclersRangeApplianceMapper;
import com.tzj.collect.core.service.RecyclersRangeApplianceService;
import com.tzj.collect.entity.RecyclersRangeAppliance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly = true)
public class RecyclersRangeApplianceServiceImpl extends ServiceImpl<RecyclersRangeApplianceMapper, RecyclersRangeAppliance> implements RecyclersRangeApplianceService {

    @Autowired
    private RecyclersRangeApplianceMapper recyclersRangeApplianceMapper;

    /**
     * 根据市级Id和回收人员id获取区域信息
     * @author wangcan
     * @param
     * @return
     */
    @Override
    public Object getAreaRecyclersRange(String cityId,String recycleId,String companyId){
        return recyclersRangeApplianceMapper.getAreaRecyclersRange(cityId,recycleId,companyId);
    }

    /**
     * 根据市级Id和回收人员id获取街道，小区信息
     * @author wangcan
     * @param
     * @return
     */
    @Override
    public Object getStreeRecyclersRange(String areaId,String recycleId,String companyId){
        return recyclersRangeApplianceMapper.getStreeRecyclersRange(areaId,recycleId,companyId);
    }

    /**
     * 根据街道Id获取小区信息
     * @author wangcan
     * @param
     * @return
     */
    @Override
    public List<Map<String,Object>> getCommunityRecyclersRange(String streetId, String recycleId, String companyId){
        return recyclersRangeApplianceMapper.getCommunityRecyclersRange(streetId,recycleId,companyId);
    }

    @Override
    public Map<String,Object> companyAreaRecyclerRanges(String companyId){
        return recyclersRangeApplianceMapper.companyAreaRecyclerRanges(companyId);
    }

}
