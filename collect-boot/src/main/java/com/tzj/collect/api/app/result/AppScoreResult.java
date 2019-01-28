package com.tzj.collect.api.app.result;

import java.util.Map;

public class AppScoreResult {
	
	private String score;// 得分
	
	private Map<String, Object> evaList;//评价列表
	
	private String evaRateGroup;//评价
	
	private String avgScore = "0";//平均分
	
	private String percent;//百分比
	
	private String countNum;//计数
	
	private String fiveNum;//五星好评数量
	
	private String oneNum;//差评数量
	
	private String threeNum;//中评数量
	
	private String fiveRate = "0";//五星好评比率
	
	private String oneRate = "0";//差评比率
	
	
	
	public Map<String, Object> getEvaList() {
		return evaList;
	}
	public void setEvaList(Map<String, Object> evaList) {
		this.evaList = evaList;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getEvaRateGroup() {
		return evaRateGroup;
	}
	public void setEvaRateGroup(String evaRateGroup) {
		this.evaRateGroup = evaRateGroup;
	}
	public String getAvgScore() {
		return avgScore;
	}
	public void setAvgScore(String avgScore) {
		this.avgScore = avgScore;
	}
	public String getPercent() {
		return percent;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	public String getCountNum() {
		return countNum;
	}
	public void setCountNum(String countNum) {
		this.countNum = countNum;
	}
	public String getFiveNum() {
		return fiveNum;
	}
	public void setFiveNum(String fiveNum) {
		this.fiveNum = fiveNum;
	}
	public String getOneNum() {
		return oneNum;
	}
	public void setOneNum(String oneNum) {
		this.oneNum = oneNum;
	}
	public String getThreeNum() {
		return threeNum;
	}
	public void setThreeNum(String threeNum) {
		this.threeNum = threeNum;
	}
	public String getFiveRate() {
		return fiveRate;
	}
	public void setFiveRate(String fiveRate) {
		this.fiveRate = fiveRate;
	}
	public String getOneRate() {
		return oneRate;
	}
	public void setOneRate(String oneRate) {
		this.oneRate = oneRate;
	}
	
	
}
