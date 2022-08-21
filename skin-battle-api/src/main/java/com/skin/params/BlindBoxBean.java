package com.skin.params;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/15 10:49
 * @Description:
 */
@Data
public class BlindBoxBean {

    PageBean pageBean;

    private Long id ;

    /** 皮肤名称 */
    private String boxName ;

    /** 盒子类型 **/
    private Long boxType;
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
    /** 折扣 */
    private Integer discount ;
    /** 皮肤图片 */
    private String skinPic ;

    private Long boxId;
    /** 皮肤名称 */
    private String skinName;
    /** 磨损度 */
    private String attritionRate ;
    /** 品级 */
    @TableField("level_")
    private Integer level ;
    /** 图片链接 */
    private String picUrl ;
    /** 中奖概率 */
    private Double probability ;

    private Integer num;

}
