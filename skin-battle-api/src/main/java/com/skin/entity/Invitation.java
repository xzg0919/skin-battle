package com.skin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;


/**
 * admin;
 * @author : http://www.chiner.pro
 * @date : 2022-8-9
 */
@TableName("invitation")
@Data
public class Invitation extends DataEntity<Long>{
    @TableId(type = IdType.AUTO)
    private  Long id ;

    /** 邀请人 */
    Long userId;

    /** 被邀请人 */
    Long inviteUserId;

    String inviteUserName;

    /** 充值金额 */
    BigDecimal rechargeAmount;


}
