package com.skin.params;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/11 16:30
 * @Description:
 */
@Data
public class DailyTaskBean {

    /** 标题 */
    private String title ;
    /** 状态 0：启用 1：禁用 */
    private Integer status ;
    /** 任务类型 1：充值  0：消费 */
    private Integer type ;
    /** 金额 */
    private BigDecimal price ;
    /** 获得的金额 */
    private BigDecimal rewardPrice ;

    private  Long id ;

    PageBean pageBean;
}
