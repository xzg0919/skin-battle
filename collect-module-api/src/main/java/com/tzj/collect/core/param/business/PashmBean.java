package com.tzj.collect.core.param.business;

import lombok.Data;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/3/16 14:11
 * @Description:
 */
@Data
public class PashmBean {

    /** 0:接单  1:完成  2:驳回  3:修改预约时间*/
    Integer code;

    Double weight;

    Integer  pashmClothesCount;

    Integer normalClothesCount;

    String pashmClothesImg;

    String normalClothesImg;


    String orderNo;

    String rejectReason;

    String arrivalTime;

    String startTime;

    String endTime;
}
