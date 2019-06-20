package com.tzj.collect.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.RecyclersRangeBig;
import com.tzj.collect.mapper.RecyclersRangeBigMapper;
import com.tzj.collect.service.RecyclersRangeBigService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class RecyclersRangeBigServiceImpl extends ServiceImpl<RecyclersRangeBigMapper, RecyclersRangeBig> implements RecyclersRangeBigService {
    @Autowired
    private RecyclersRangeBigMapper recyclersRangeBigMapper;

    /**
     * 根据市级Id和回收人员id获取区域信息
     * @author wangcan
     * @param
     * @return
     */
    @Override
    public Object getAreaRecyclersRange(String cityId,String recycleId,String companyId){
        return recyclersRangeBigMapper.getAreaRecyclersRange(cityId,recycleId,companyId);
    }

    /**
     * 根据市级Id和回收人员id获取街道，小区信息
     * @author wangcan
     * @param
     * @return
     */
    @Override
    public Object getStreeRecyclersRange(String areaId,String recycleId,String companyId){
        return recyclersRangeBigMapper.getStreeRecyclersRange(areaId,recycleId,companyId);
    }

    /**
     * 根据街道Id获取小区信息
     * @author wangcan
     * @param
     * @return
     */
    @Override
    public List<Map<String,Object>> getCommunityRecyclersRange(String streetId, String recycleId, String companyId){
        return recyclersRangeBigMapper.getCommunityRecyclersRange(streetId,recycleId,companyId);
    }
}
