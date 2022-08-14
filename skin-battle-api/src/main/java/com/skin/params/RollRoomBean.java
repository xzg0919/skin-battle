package com.skin.params;

import com.baomidou.mybatisplus.annotation.TableField;
import com.skin.params.PageBean;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: xiang
 * @Date: 2022/8/13 12:37
 * @Description:
 */
@Data
public class RollRoomBean {

    PageBean pageBean;


    Long id ;


    String skinName;


    /** 房间名称 */
    private String name ;
    /** 房间类型 0:官方房间 1：福利房间 */
    private Integer roomType ;
    /** 开奖时间 */
    private String lotteryTime ;
    /** 参与条件 0：充值 1：口令 */
    private Integer conditionType ;
    /** 口令 */
    private String roomPswd ;
    /** 充值金额 */
    private BigDecimal price ;
    /** 房间描述 */
    private String desc ;
    /** 房间图片 */
    private String roomPic ;

    /** 指定中奖用户 */
    private String specifiedUser ;


    Long skinId;
}
