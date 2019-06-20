package com.tzj.collect.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.PiccNum;
import org.apache.ibatis.annotations.Param;

public interface PiccNumMapper extends BaseMapper<PiccNum> {

    @DS("slave")
    Integer selectSumOutNum(long piccConpanyId);
    @DS("slave")
    Integer selectOutNum(long piccConpanyId);
    @DS("slave")
    Integer selectSumErrorNum(long piccConpanyId);
    @DS("slave")
    Integer selectErrorNum(long piccConpanyId);
    @DS("slave")
    Integer selectSumSuccessNum(long piccConpanyId);
    @DS("slave")
    Integer selectSuccessNum(long piccConpanyId);
    @DS("slave")
    Integer selectInvalidNum(@Param("piccConpanyId") long piccConpanyId,@Param("startTime") String startTime,@Param("endTime") String endTime);

}
