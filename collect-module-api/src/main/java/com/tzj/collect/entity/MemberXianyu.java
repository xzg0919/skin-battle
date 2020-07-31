package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import javafx.scene.chart.PieChart;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@TableName("sb_member_xianyu")
@Data
public class MemberXianyu extends DataEntity<Long> {

    private Long Id;

    private String linkName;//昵称

    private String picUrl;//头像连接

    private String mobile;//手机号

    private String cardNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);//会员卡号

    private String openId;//淘系唯一id

    private String aliUserId = "30"+new SimpleDateFormat("yyMMddHHmmss").format(new Date()) + (new Random().nextInt(89) + 10);//ali唯一id，如果没有，给他虚拟一个

    private String accessToken;

    private String aliAccount;//支付宝账号



}
