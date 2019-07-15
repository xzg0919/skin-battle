package com.tzj.collect.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("sb_point_list")
public class PointList extends DataEntity<Long> {

	private Long id;
	private Integer memberId;  //会员id
	private String point;  //积分
	private String type;  //类型  0代表此记录用户增加积分 1代表用户消耗积分
	private String documentNo;   //单据号
	private String descrb;   //描述
	@TableField(exist = false)
    private Member member;
	
	/**
	 * 返回创建时间（用于返回页面）
	 * @return
	 */
	@TableField(exist = false)
	private String createDatePage;
	
	public String getCreateDatePage() {		
		
		return this.getDate(createDate);
	}

	public void setCreateDatePage(String createDatePage) {
		this.createDatePage = createDatePage;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public String getDescrb() {
		return descrb;
	}

	public void setDescrb(String descrb) {
		this.descrb = descrb;
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
	public String getDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
}
