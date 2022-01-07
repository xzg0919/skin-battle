package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.RecyclersRangeApplianceMapper;
import com.tzj.collect.core.mapper.RecyclersRangeElectroMapper;
import com.tzj.collect.core.service.RecyclersRangeApplianceService;
import com.tzj.collect.core.service.RecyclersRangeElectroService;
import com.tzj.collect.entity.RecyclersRangeAppliance;
import com.tzj.collect.entity.RecyclersRangeElectro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly = true)
public class RecyclersRangeElectroServiceImpl extends ServiceImpl<RecyclersRangeElectroMapper, RecyclersRangeElectro> implements RecyclersRangeElectroService {


    @Override
    public List<Map<String, Object>> getRecyclerStreetByTitleId(String recyclerId, String companyId, String areaId) {
        return baseMapper.getRecyclerStreetByTitleId(recyclerId,companyId,areaId);
    }

    @Override
    public List<Map<String,Object>> getRecyclerAreaByTitleId(String recyclerId,String companyId){
        return baseMapper.getRecyclerAreaByTitleId(recyclerId,companyId);
    }

    @Override
    public Map<String,Object> companyAreaRecyclerRanges(String companyId){
        return baseMapper.companyAreaRecyclerRanges(companyId);
    }

    @Override
    public Object getStreeRecyclersRange(String areaId,String recycleId,String companyId){
        return baseMapper.getStreeRecyclersRange(areaId,recycleId,companyId);
    }
    @Override
    public List<Map<String,Object>> getCommunityRecyclersRange(String streetId, String recycleId, String companyId){
        return baseMapper.getCommunityRecyclersRange(streetId,recycleId,companyId);
    }

    @Override
    public Object getAreaRecyclersRange(String cityId,String recycleId,String companyId){
        return baseMapper.getAreaRecyclersRange(cityId,recycleId,companyId);
    }

}
