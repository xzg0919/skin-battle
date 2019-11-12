package com.tzj.collect.core.param.app;

import com.tzj.collect.core.param.ali.PageBean;

import java.util.List;

public class ScoreAppBean {
	
	private String recyclerId;
	
	private String score = "5";
	
	private PageBean pageBean;
	
	private List<Integer> scoreList;
 
	
	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	public List<Integer> getScoreList() {
		return scoreList;
	}

	public void setScoreList(List<Integer> scoreList) {
		this.scoreList = scoreList;
	}

	public String getRecyclerId() {
		return recyclerId;
	}

	public void setRecyclerId(String recyclerId) {
		this.recyclerId = recyclerId;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
	
}
