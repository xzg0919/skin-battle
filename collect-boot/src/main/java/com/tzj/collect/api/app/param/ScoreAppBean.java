package com.tzj.collect.api.app.param;

import java.util.List;

import com.tzj.collect.api.ali.param.PageBean;

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
