package com.tzj.collect.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@TableName("sb_point_list")
@Data
public class PointList extends DataEntity<Long> {

	private Long id;
	private Integer memberId;  //会员id
	/**
	 * 阿里user_id 用户唯一标识
	 */
	private String aliUserId;
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


	public String getDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
}
