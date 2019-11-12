package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 家电关联街道表
 * @author wangcan
 *
 */
@Data
@TableName("sb_company_street_house")
public class CompanyStreetHouse extends DataEntity<Long>{

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
