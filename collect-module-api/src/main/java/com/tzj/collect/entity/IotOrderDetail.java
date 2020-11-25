package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName: IotOrderDetail
 * @author: 向忠国
 * @date: 2018年3月16日 上午11:04:44
 * <p>
 * iot订单明细
 */
@Data
@TableName("sb_iot_order_detail")
public class IotOrderDetail extends DataEntity<Long> {
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
     * 订单id
     */
    private Long orderId;


    /**
     * 名称
     */
    @TableField(value = "name_")
    private String name;

    /**
     * 品类id
     */
    private Long categoryId;


    /**
     * 单位
     */
    private String unit;

    public IotOrderDetail() {
    }

    /**
     * 积分值/钱
     */
    private BigDecimal amount;

    public IotOrderDetail(Long orderId, String name, Long categoryId, String unit, double unitAmount, Double unitPoint, BigDecimal unitPrice) {
        this.orderId = orderId;
        this.name = name;
        this.categoryId = categoryId;
        this.unit = unit;
        this.unitAmount = unitAmount;
        this.unitPoint = unitPoint;
        this.unitPrice = unitPrice;
    }

    /**
     * 积分单价
     */
    Double unitPoint;

    /**
     * 单价
     */
    BigDecimal unitPrice;


    /**
     * 重量/个数
     */
    double unitAmount;

}
