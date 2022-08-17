package com.skin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/9 14:36
 * @Description:
 */
@TableName("pull_box")
@Data
public class PullBox extends DataEntity{


    /** 皮肤名称 */
    private String skinName ;
    /** 价格 */
    private BigDecimal price ;
    /** 磨损度 */
    private String attritionRate ;
    /** 图片链接 */
    private String skinPic ;

    /** 是否启用 1：是 0：否 */
    @TableField("enable_")
    private Integer enable ;
}
