package com.tzj.collect.api.lexicon.param;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息
 *
 * @author sgmark
 * @create 2019-09-11 9:49
 **/
@Data
public class MemberBean implements Serializable {
    private String linkName;//昵称

    private String picUrl;//头像地址

    private String city;//所在城市

    private String mobile;//手机号
}
