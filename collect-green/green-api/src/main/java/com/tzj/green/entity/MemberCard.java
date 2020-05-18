package com.tzj.green.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("sb_member_card")
public class MemberCard extends DataEntity<Long> {

    private Long id;

    private String memberCard;





}
