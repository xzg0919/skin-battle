package com.skin.params;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/11 16:53
 * @Description:
 */
@Data
public class RedPacketBean {

    private  Long id ;
    /** 充值金额 */
    private BigDecimal price ;
    /** 标题 */
    private String title ;
    /** 开始时间 */
    private Date startTime ;
    /** 结束时间 */
    private Date endTime ;
    /** 红包金额开始 */
    private BigDecimal redPacketBegin ;
    /** 红包金额截止 */
    private BigDecimal redPacketEnd ;
    /** 状态 0：正常 1：停用 */
    private Integer status;

    PageBean pageBean;
}
