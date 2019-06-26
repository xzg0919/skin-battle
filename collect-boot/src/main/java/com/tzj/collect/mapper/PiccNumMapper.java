package com.tzj.collect.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.PiccNum;
import org.apache.ibatis.annotations.Param;

public interface PiccNumMapper extends BaseMapper<PiccNum> {


    Integer selectSumOutNum(long piccConpanyId);

    Integer selectOutNum(long piccConpanyId);

    Integer selectSumErrorNum(long piccConpanyId);

    Integer selectErrorNum(long piccConpanyId);

    Integer selectSumSuccessNum(long piccConpanyId);

    Integer selectSuccessNum(long piccConpanyId);

    Integer selectInvalidNum(@Param("piccConpanyId") long piccConpanyId,@Param("startTime") String startTime,@Param("endTime") String endTime);

}
