package com.skin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/17 13:47
 * @Description:
 */

@TableName("verify_message")
@Data
public class VerifyMessage extends DataEntity {




    String verifyCode;

    @TableField("to_")
    String  to;

    Integer isUse =0;
}
