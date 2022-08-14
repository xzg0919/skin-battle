package com.skin.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数据Entity类
 * 
 * @author 王存见
 * @version 2016-12-03
 * @param <ID>
 *            主键类型
 */
public abstract class DataEntity<ID> extends AbstractEntity<ID> {

	private static final long serialVersionUID = 1L;

	@TableField(value = "remarks")
	protected String remarks; // 备注
	@TableField(value = "create_date", fill = FieldFill.INSERT)
	protected Date createDate; // 创建日期

	@TableField(value = "update_date", fill = FieldFill.INSERT_UPDATE, update = "NOW()")
	protected Date updateDate; // 更新日期
	@TableField(value = "del_flag", fill = FieldFill.INSERT)
	protected String delFlag = "0";

	@Version
	@TableField(value = "version_" , fill = FieldFill.INSERT, update="%s+1")
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


	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getDate(Date date) {
		if(date ==null){
			return "" ;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}


	public String getCreateDateStr() {
		return getDate(getCreateDate());
	}


	@TableField(exist = false)
	protected  String createDateStr;



}
