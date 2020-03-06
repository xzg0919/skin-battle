package com.tzj.green.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.green.entity.CompanyCommunity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [社区表映射类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
public interface CompanyCommunityMapper extends BaseMapper<CompanyCommunity>
{

   List<Map<String,Object>> getCompanyCommunityList(@Param("communityNo") String communityNo,@Param("companyId") Long companyId,@Param("provinceId") String provinceId,@Param("cityId") String  cityId, @Param("areaId") String areaId, @Param("streetId") String streetId,@Param("communityName") String communityName,@Param("houseName") String houseName,@Param("pageStartNum") Integer pageStartNum, @Param("pageSize") Integer pageSize);

    Integer getCompanyCommunityCount(@Param("communityNo") String communityNo,@Param("companyId") Long companyId,@Param("provinceId") String provinceId,@Param("cityId") String  cityId, @Param("areaId") String areaId, @Param("streetId") String streetId,@Param("communityName") String communityName,@Param("houseName") String houseName);

    List<Map<String,Object>> getRecyclerListByHouseId(@Param("communityHouseId")String communityHouseId,@Param("companyId")Long companyId);

}