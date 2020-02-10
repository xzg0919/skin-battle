package com.tzj.green.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.green.entity.Member;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [用户会员表映射类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
public interface MemberMapper extends BaseMapper<Member>
{

    List<Map<String,Object>> getMemberList(@Param("companyId")Long companyId, @Param("startTime")String startTime, @Param("endTime")String endTime, @Param("provinceId")String provinceId, @Param("cityId")String cityId, @Param("areaId")String areaId, @Param("streetId")String streetId, @Param("communityId")String communityId, @Param("communityHouseId")String communityHouseId, @Param("name")String name, @Param("tel")String tel, @Param("pageStart")Integer pageStart, @Param("pageSize")Integer pageSize);

    Integer getMemberCount(@Param("companyId")Long companyId, @Param("startTime")String startTime, @Param("endTime")String endTime, @Param("provinceId")String provinceId, @Param("cityId")String cityId, @Param("areaId")String areaId, @Param("streetId")String streetId, @Param("communityId")String communityId, @Param("communityHouseId")String communityHouseId, @Param("name")String name, @Param("tel")String tel);

    Member selectByAliUserId(@Param("aliUserId") String aliUserId);

    Map<String, Object> memberInfo(@Param("realNo")String realNo, @Param("tel") String tel);
}