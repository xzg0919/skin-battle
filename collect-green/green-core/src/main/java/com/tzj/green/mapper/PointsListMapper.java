package com.tzj.green.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.green.entity.PointsList;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [积分流水表映射类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 *
 * @author [王灿]
 * @version 1.0
 */
public interface PointsListMapper extends BaseMapper<PointsList> {

    List<Map<String, Object>> getPointsListByCompanyId(@Param("provinceId") String provinceId,@Param("companyId") Long companyId, @Param("pointsType") String pointsType,
                                                       @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("name") String name,
                                                       @Param("tel") String tel, @Param("cityId") String cityId, @Param("areaId") String areaId,
                                                       @Param("streetId") String streetId, @Param("communityId") String communityId, @Param("communityHouseId") String communityHouseId,
                                                       @Param("pageStart") Integer pageStart, @Param("pageSize") Integer pageSize);

    Integer getPointsListCount(@Param("provinceId") String provinceId,@Param("companyId") Long companyId, @Param("pointsType") String pointsType,
                                                       @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("name") String name,
                                                       @Param("tel") String tel, @Param("cityId") String cityId, @Param("areaId") String areaId,
                                                       @Param("streetId") String streetId, @Param("communityId") String communityId, @Param("communityHouseId") String communityHouseId);


}