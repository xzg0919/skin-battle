package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;

@TableName("sb_company_city_ratio")
@Data
public class CompanyCityRatio extends  DataEntity<Long> {


    private Long id;

    private Integer companyId;

    private Integer cityId;

    private BigDecimal ratio;


}
