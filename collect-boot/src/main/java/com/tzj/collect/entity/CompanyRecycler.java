package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 回收人员和企业关联表
 * 
 * @ClassName: CompanyRecycler
 * @Description: 【】
 * @date 2018年3月15日 上午10:27:29
 * @Company: 上海挺之军科技有限公司
 * @Department：研发部
 * @author:[王池][wjc2013481273@163.com]
 */
@TableName("sb_company_recycler")
public class CompanyRecycler extends DataEntity<Long> {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private Integer recyclerId;// 回收人员id
	private Integer companyId;// 回收企业id
	@TableField(value = "status_")
	private String status = "0";// 状态 0申请 1入住2拒绝 

	public Integer getRecyclerId() {
		return recyclerId;
	}

	public void setRecyclerId(Integer recyclerId) {
		this.recyclerId = recyclerId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public Long getId() {

		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;

	}

}
