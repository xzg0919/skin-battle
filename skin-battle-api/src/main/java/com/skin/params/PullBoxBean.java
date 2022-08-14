package com.skin.params;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: xiang
 * @Date: 2022/8/14 13:12
 * @Description:
 */
@Data
public class PullBoxBean {
    private  Long id;

    /** 皮肤名称 */
    private String skinName ;
    /** 价格 */
    private BigDecimal price ;
    /** 磨损度 */
    private String attritionRate ;
    /** 图片链接 */
    private String skinPic ;

    PageBean pageBean;

    /** 中奖概率 */
    private Double probability ;

    Long skinId;
}
