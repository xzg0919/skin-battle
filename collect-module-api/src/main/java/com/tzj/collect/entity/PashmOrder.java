package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 
 * @ClassName: PashmOrder
 * @author: 向忠国
 * @date: 2018年3月16日 上午11:04:44
 */
@TableName("sb_pashm_order")
@Data
public class PashmOrder extends DataEntity<Long> {
	private Long id;

	Double weight;

	Integer  pashmClothesCount;

	Integer normalClothesCount;

	String pashmClothesImg;

	String normalClothesImg;

	String orderNo;

	String startTime;

	String endTime;



	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
}
