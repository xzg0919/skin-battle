package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.EnterpriseCode;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EnterpriseCodeMapper extends BaseMapper<EnterpriseCode> {

    /**
     * 以旧换新的券的列表
     * 王灿
     * @param
     * @return
     */
    List<Map<String,Object>> enterpriseCodeList(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("isUse") String isUse, @Param("enterpriseId") Integer enterpriseId, @Param("pageStart") Integer pageStart, @Param("pageEnd") Integer pageEnd);
    /**
     * 以旧换新的券的列表数量
     * 王灿
     * @param
     * @return
     */
    Integer enterpriseCodeListCount(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("isUse") String isUse, @Param("enterpriseId") Integer enterpriseId);

    /**
     * 以旧换新的券的详情
     * 王灿
     * @param enterpriseCodeId
     * @return
     */
    Map<String,Object> enterpriseCodeDetil(String enterpriseCodeId);

    List<Map<String,Object>> outEnterpriseCodeExcel(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("isUse") String isUse, @Param("enterpriseId") Integer enterpriseId);
}
