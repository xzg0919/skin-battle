package com.tzj.collect.core.param.iot;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: xiangzhongguo
 * @Date: 2020/11/21 15:33
 * @Description:
 */
@Data
public class IotOrderVo {

    String orderNo;

    int orderType;

    List<IotOrderDetail> iotOrderDetail;

    @Data
    public  class IotOrderDetail{
        /**
         * 品类名称
         */
        String categoryName;

        public IotOrderDetail() {
        }

        /**
         * 重量
         */
        double weight;

        /**
         * 积分/金额
         */
        BigDecimal amount;
    }


}
