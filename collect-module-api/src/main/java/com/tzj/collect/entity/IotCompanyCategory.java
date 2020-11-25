package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName: IotCompanyCategory
 * @author: 向忠国
 * @date: 2018年3月16日 上午11:04:44
 * <p>
 * iot公司回收的品类
 */
@Data
@TableName("sb_iot_company_category")
public class IotCompanyCategory extends DataEntity<Long> {
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }


    /**
     * 名称
     */
    @TableField(value = "name_")
    private String name;


    /**
     * 分类id
     */
    private Long categoryId;


    /**
     * 计量单位
     */
    private String unit;


    /**
     * 积分值
     */
    private Double unitPoint;


    /**
     * 金额
     */
    private BigDecimal unitPrice;

    /**
     * 公司id
     */
   private Long companyId;


    /**
     * 分类编号
     */
   private String categoryCode;

}
