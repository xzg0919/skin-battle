package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sb_imprison_member")
public class ImprisonMember extends DataEntity<Long> {

    private Long id;

    private String aliUserId;

    private Date startDate;

    private Date endDate;

    private String title;


}
