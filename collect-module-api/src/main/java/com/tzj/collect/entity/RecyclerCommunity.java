
/**
* @Title: SbRecyclerCommunity.java
* @Package com.tzj.collect.entity
* @Description: 【】
* @date 2018年3月5日 下午1:04:40
* @version V1.0
* @Company: 上海挺之军科技有限公司
* @Department： 研发部
* @author:[王池][wjc2013481273@163.com]
*/

package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;

/**
* @ClassName: SbRecyclerCommunity
* @Description: 【】
* @date 2018年3月5日 下午1:04:40
* @Company: 上海挺之军科技有限公司
* @Department：研发部
* @author:[王池][wjc2013481273@163.com]
*/
@TableName("sb_recycler_community")
public class RecyclerCommunity extends DataEntity<Long>{

	private static final long serialVersionUID = 1L;
	
	private Long id;
    private Long commuityId;
    private Long recyclerId;
    public Long getCommuityId() {
		return commuityId;
	}




	public void setCommuityId(Long commuityId) {
		this.commuityId = commuityId;
	}




	public Long getRecyclerId() {
		return recyclerId;
	}




	public void setRecyclerId(Long recyclerId) {
		this.recyclerId = recyclerId;
	}

	private int areaId;
    private String parentIds;
   
	
	
	
	

	
	/**
	* @return areaId
	*/
	
	public int getAreaId() {
		return areaId;
	}



	
	/**
	* @param paramtheparamthe{bare_field_name} to set
	*/
	
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}



	
	/**
	* @return parentIds
	*/
	
	public String getParentIds() {
		return parentIds;
	}



	
	/**
	* @param paramtheparamthe{bare_field_name} to set
	*/
	
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}




	
	@Override
	public Long getId() {
		
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id=id;
		
	}

	
	
	
}
