package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;


@Data
@TableName("sb_mysl_request_log")
public class MyslRequestLog extends DataEntity<Long> {
	private Long id;

	String orderNo;

	String myslParams;

	String myslResult;

	Integer fullEnergy;

	String outBizNo;

}
