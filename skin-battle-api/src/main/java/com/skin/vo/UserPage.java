package com.skin.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/10 15:36
 * @Description:
 */
@Data
public class UserPage {


    Long id ;

    String uid;

    String createDate;

    String nickName;

    String tel;

    String  email;

    BigDecimal point;
}
