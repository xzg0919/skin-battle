package com.tzj.green.param;

import lombok.Data;

/**
 * @author sgmark
 * @create 2020-01-15 15:39
 **/
@Data
public class MemberGoodsBean {

    private Long goodsId;//商品id

    private Long productId;//活动id

    private Long amount;//数量

    private String realNo;//卡号

    private Long recId;

}
