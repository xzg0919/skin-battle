package com.tzj.collect.api.ali.result;

import java.math.BigDecimal;

public class ClassifyAndMoney {
	
	private String classify;// 分类	
	private BigDecimal money;//价格
	
	public String getClassify() {
		return classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	
}
