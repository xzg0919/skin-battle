package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.RecyclersRangeElectro;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RecyclersRangeElectroMapper extends BaseMapper<RecyclersRangeElectro> {

    List<Map<String,Object>> getRecyclerStreetByTitleId(@Param("recyclerId")String recyclerId, @Param("companyId")String companyId, @Param("areaId")String areaId);

    List<Map<String,Object>> getRecyclerAreaByTitleId(@Param("recyclerId")String recyclerId,@Param("companyId")String companyId);

    Map<String,Object> companyAreaRecyclerRanges(String companyId);

    List<Map<String,Object>> getStreeRecyclersRange(@Param("areaId") String areaId, @Param("recycleId") String recycleId, @Param("companyId") String companyId);

    List<Map<String,Object>> getCommunityRecyclersRange(@Param("streetId") String streetId, @Param("recycleId") String recycleId, @Param("companyId") String companyId);

    List<Map<String,Object>> getAreaRecyclersRange(@Param("cityId") String cityId, @Param("recycleId") String recycleId, @Param("companyId") String companyId);

}
