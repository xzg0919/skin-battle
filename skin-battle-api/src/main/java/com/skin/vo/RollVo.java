package com.skin.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/24 10:41
 * @Description:
 */
@Data
public class RollVo {
    private String name ;
    private Integer roomType ;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date lotteryTime ;
    private Integer conditionType ;
    private String roomPswd ;
    private BigDecimal price ;
    private String desc ;
    private String roomPic ;
    /** 房间状态  1：待开奖 2：已结束 */
    Integer roomStatus ;
    Integer userCount;
    Integer awardCount;
    BigDecimal awardPrice;
    Integer isJoin;

}
