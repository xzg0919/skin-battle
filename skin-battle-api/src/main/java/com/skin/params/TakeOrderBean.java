package com.skin.params;

import lombok.Data;

import java.util.List;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/15 14:42
 * @Description:
 */
@Data
public class TakeOrderBean {
    PageBean pageBean;

    String nickName;

    String email;

    Long id ;

    Integer status;


    List<Long> packageIds;
}
