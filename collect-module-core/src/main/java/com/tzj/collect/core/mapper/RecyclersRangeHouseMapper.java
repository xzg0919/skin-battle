package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.RecyclersRangeHouse;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RecyclersRangeHouseMapper extends BaseMapper<RecyclersRangeHouse> {

    Map<String,Object> companyAreaRecyclerRanges(String companyId);

    List<Map<String,Object>> getAreaRecyclersRange(@Param("cityId") String cityId, @Param("recycleId") String recycleId, @Param("companyId") String companyId);

    /**
     * 根据市级Id和回收人员id获取街道、小区信息
     * @author wangcan
     * @param
     * @return
     */
    public  List<Map<String,Object>> getStreeRecyclersRange(@Param("areaId") String areaId, @Param("recycleId") String recycleId, @Param("companyId") String companyId);

    /**
     * 根据街道Id获取小区信息
     * @author wangcan
     * @param
     * @return
     */
    List<Map<String,Object>> getCommunityRecyclersRange(@Param("streetId") String streetId, @Param("recycleId") String recycleId, @Param("companyId") String companyId);

    List<Map<String,Object>> getRecyclerAreaByTitleId(@Param("recyclerId")String recyclerId,@Param("companyId")String companyId);

    List<Map<String,Object>> getRecyclerStreetByTitleId(@Param("recyclerId")String recyclerId,@Param("companyId")String companyId,@Param("areaId")String areaId);

}
