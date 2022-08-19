package com.skin.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/18 14:49
 * @Description:
 */
@Data
public class VipCheckList {

    String vipLevel;

    BigDecimal condition;

    BigDecimal reward;

    Integer isReceive;
    public void isReceive(boolean present) {
        this.isReceive = present?1:0;
    }
}
