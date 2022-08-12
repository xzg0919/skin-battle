package com.skin.params;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/10 13:00
 * @Description:
 */

@Data
public class CdkBean {

    String code;

    PageBean pageBean;


    Long id ;


    BigDecimal cdkVal ;
}
