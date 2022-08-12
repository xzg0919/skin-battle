package com.skin.params;

import lombok.Data;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/11 10:07
 * @Description:
 */
@Data
public class PointBean {

    PageBean pageBean;

    Long userId;

    Integer orderFrom;

    String orderNo;
}
