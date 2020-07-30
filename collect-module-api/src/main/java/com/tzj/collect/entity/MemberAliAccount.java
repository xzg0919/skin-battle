package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("sb_member_ali_account")
public class MemberAliAccount extends DataEntity<Long> {


    private Long id;

    private String openId;//淘系唯一Id',

    private String aliUserId;//阿里userid',

    private String aliAccount;//支付宝账号',



}
