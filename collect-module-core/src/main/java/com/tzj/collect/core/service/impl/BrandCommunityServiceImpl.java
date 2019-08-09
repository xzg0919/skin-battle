package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.BrandCommunityMapper;
import com.tzj.collect.core.service.BrandCommunityService;
import com.tzj.collect.entity.BrandCommunity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly=true)
public class BrandCommunityServiceImpl extends ServiceImpl<BrandCommunityMapper, BrandCommunity> implements BrandCommunityService {

    @Resource
    private BrandCommunityMapper brandCommunityMapper;


    @Override
    public List<Map<String, Object>> getBrandCommunityList(String streetId) {
        return brandCommunityMapper.getBrandCommunityList(streetId);
    }

    @Override
    public List<Map<String, Object>> getBrandCommunityLists(String streetId) {
        return brandCommunityMapper.getBrandCommunityLists(streetId);
    }
}
