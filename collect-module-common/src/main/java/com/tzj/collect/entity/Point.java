package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("sb_point")
public class Point extends DataEntity<Long> {
	private Long id;
	private Integer memberId;  //会员id
	@TableField(exist=false)
	private Member member;    
	private double point;	//积分
	private double remainPoint;//用户剩余的积分

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public double getPoint() {
		return point;
	}

	public void setPoint(double point) {
		this.point = point;
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public double getRemainPoint() {
		return remainPoint;
	}

	public void setRemainPoint(double remainPoint) {
		this.remainPoint = remainPoint;
	}

}
