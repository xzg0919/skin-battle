package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.RecyclersRangeAppliance;
import com.tzj.collect.entity.RecyclersRangeElectro;

import java.util.List;
import java.util.Map;

public interface RecyclersRangeElectroService extends IService<RecyclersRangeElectro> {

    List<Map<String,Object>> getRecyclerStreetByTitleId(String recyclerId,String companyId,String areaId);

    List<Map<String,Object>> getRecyclerAreaByTitleId(String recyclerId,String companyId);

    Map<String,Object> companyAreaRecyclerRanges(String companyId);

    Object getStreeRecyclersRange(String areaId, String recycleId, String companyId);

    List<Map<String,Object>> getCommunityRecyclersRange(String streetId, String recycleId, String companyId);

    Object getAreaRecyclersRange(String cityId, String recycleId, String companyId);
}
