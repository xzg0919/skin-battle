package com.skin.params;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/15 11:48
 * @Description:
 */
@Data
public class BoxBattleBean {

    PageBean pageBean;

    /** 皮肤名称 */
    private String boxName ;
    /** 价格 */
    private BigDecimal price ;
    /** 盒子图片 */
    private String boxPic ;
    /** 高中奖概率 */
    private Double highProbability ;
    /** 中中奖概率 */
    private Double middleProbability ;
    /** 低中奖概率 */
    private Double lowProbability ;

    private  Long id ;

    private Long boxBattleId;

    /** 皮肤名称 */
    private String skinName;
    /** 磨损度 */
    private String attritionRate ;
    /** 品级 */
    private Integer level ;
    /** 图片链接 */
    private String picUrl ;
    /** 中奖概率 */
    private Double probability ;


    /**
     * 选择的盒子id  1,3,4,5
     */
    String boxIds;

    Integer userCount;

}
