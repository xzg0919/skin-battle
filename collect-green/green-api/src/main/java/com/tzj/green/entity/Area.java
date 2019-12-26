package com.tzj.green.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;


@Data
@TableName("sb_area")
public class Area extends DataEntity<Long> {
	private Long id;
	private Integer parentId;   //父级编号
	private String parentIds;   //所有父级编号
	private String areaName;    //名称
	@TableField(value="sort_")
	private Integer sort;      //排序
	@TableField(value="code_")
	private String code;        //区域编码
	private String type;		//区域类型

	private BigDecimal ratio; //城市系数

}
