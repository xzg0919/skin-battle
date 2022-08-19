package com.skin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/18 14:18
 * @Description:
 */

@Data
@TableName("vip_reward")
public class VipReward extends DataEntity {


    Long userId;

    @TableField("level_")
    Integer level;

    BigDecimal reward;


}
