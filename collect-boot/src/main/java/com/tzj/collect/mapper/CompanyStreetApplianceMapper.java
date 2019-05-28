package com.tzj.collect.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.CompanyStreetAppliance;
import org.apache.ibatis.annotations.Param;

public interface CompanyStreetApplianceMapper extends BaseMapper<CompanyStreetAppliance> {

    /**
     * 根据分类id和街道id查询所属公司
     * @param categoryId
     * @param streetId
     * @return
     */
    Integer selectStreetApplianceCompanyId(@Param("categoryId") Integer categoryId,@Param("streetId")Integer streetId);


}
