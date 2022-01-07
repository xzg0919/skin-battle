package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 电瓶车关联街道表
 * @author wangcan
 *
 */
@TableName("sb_company_street_electro_mobile")
@Data
public class CompanyStreetElectroMobile extends DataEntity<Long>{

    private Long id;
    /**
     * 公司Id
     */
    private Integer companyId;
    /**
     * 街道Id
     */
    private Integer streetId;
    /**
     * 行政区Id
     */
    private Integer areaId;



}
