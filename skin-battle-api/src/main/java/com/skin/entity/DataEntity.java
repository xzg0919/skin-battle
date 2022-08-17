package com.skin.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;


@Data
public abstract class DataEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 主键 */
	@TableId(type = IdType.AUTO)
	private Long id ;

	@TableField(value = "remarks")
	protected String remarks;


	@JSONField( format="yyyy-MM-dd HH:mm:ss" )
	@TableField(value = "create_date", fill = FieldFill.INSERT)
	protected Date createDate;

	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@TableField(value = "update_date", fill = FieldFill.INSERT_UPDATE, update = "NOW()")
	protected Date updateDate;

	@TableLogic
	@TableField(value = "del_flag", fill = FieldFill.INSERT)
	protected String delFlag ;

	@Version
	@TableField(value = "version_" , fill = FieldFill.INSERT)
	private Integer version;

	public DataEntity() {
		super();
		this.delFlag = "0";
		this.version = 1;
	}

	@TableField(value = "create_by",fill = FieldFill.INSERT)
	protected  String createBy;

	@TableField(value = "update_by",fill = FieldFill.INSERT_UPDATE)
	protected String updateBy;


	public String getDate(Date date) {
		if(date ==null){
			return "" ;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}



}
