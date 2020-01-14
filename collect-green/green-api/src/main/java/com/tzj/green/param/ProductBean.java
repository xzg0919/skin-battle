package com.tzj.green.param;

import lombok.Data;

import java.util.List;

@Data
public class ProductBean {

    /**
     * 主键
     */
    private String id;
    /**
     * 所属的企业Id
     */
    private String companyId;
    /**
     * 活动名称
     */
    private String name;
    /**
     * 活动开始时间
     */
    private String pickStartDate;
    /**
     * 活动结束时间
     */
    private String pickEndDate;
    /**
     * 具体地址Id
     */
    private String houseNameId;
    /**
     * 活动说明
     */
    private String detail;
    /**
     * 兑换总数量
     */
    private String exchangeNum;
    /**
     * 兑换总数量
     */
    private String exchangePoints;
    /**
     * 是否下架 0不下架  1下架
     */
    private String isLower;

    private PageBean pageBean;

    private List<ProductGoodsBean> productGoodsBeanList;

    private List<String> recyclerIds;

    private String productNo;

    private String startTime;

    private String endTime;

    private String tel;

    private String type;

    /**
     * 纬度
     */
    private Double lat;

    /**
     * 经度
     */
    private Double lng;

    private String aliUserId;

}
