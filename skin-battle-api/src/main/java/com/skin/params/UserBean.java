package com.skin.params;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/10 14:56
 * @Description:
 */
@Data
public class UserBean {

    Long id ;
    String nickName ;
    String tel;

    PageBean pageBean;

    BigDecimal point;

      Double highProbability ;
    /** 中中奖概率 */
      Double middleProbability ;
    /** 低中奖概率 */
      Double lowProbability ;

      String avatar;

      String orderNo;
}
