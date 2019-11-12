package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.core.result.admin.RecruitExpressResult;
import com.tzj.collect.entity.RecruitExpress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RecruitExpressMapper extends BaseMapper<RecruitExpress> {

    List<RecruitExpressResult> getRecruitList(@Param("type")String type, @Param("cooperationType")String cooperationType, @Param("enterprise")String enterprise, @Param("city")String city, @Param("mobile")String mobile, @Param("startTime")String startTime, @Param("endTime")String endTime, @Param("startPage")Integer startPage, @Param("pageSize")Integer pageSize);

    Integer getRecruitCount(@Param("type")String type, @Param("cooperationType")String cooperationType, @Param("enterprise")String enterprise, @Param("city")String city, @Param("mobile")String mobile, @Param("startTime")String startTime, @Param("endTime")String endTime);

    List<RecruitExpressResult> getRecruitListOutExcel(@Param("type")String type, @Param("cooperationType")String cooperationType, @Param("enterprise")String enterprise, @Param("city")String city, @Param("mobile")String mobile, @Param("startTime")String startTime, @Param("endTime")String endTime);
}
