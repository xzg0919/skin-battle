
/**
* @Title: SbRecyclersCatagory.java
* @Package com.tzj.collect.entity
* @Description: 【】
* @date 2018年3月5日 下午1:44:07
* @version V1.0
* @Company: 上海挺之军科技有限公司
* @Department： 研发部
* @author:[王池][wjc2013481273@163.com]
*/

package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;

/**
* @ClassName: SbRecyclersCatagory
* @Description: 【】
* @date 2018年3月5日 下午1:44:07
* @Company: 上海挺之军科技有限公司
* @Department：研发部
* @author:[王池][wjc2013481273@163.com]
*/
@TableName("sb_recyclers_category")
public class RecyclersCategory extends DataEntity<Long>{

	private Long id;
	private int recyclerId;
	private int categoryId;
	
	
	/**
	* @return recycler_id
	*/
	
	public int getRecycler_id() {
		return recyclerId;
	}


	
	/**
	* @param paramtheparamthe{bare_field_name} to set
	*/
	
	public void setRecycler_id(int recycler_id) {
		this.recyclerId = recycler_id;
	}


	
	/**
	* @return category_id
	*/
	
	public int getCategory_id() {
		return categoryId;
	}


	
	/**
	* @param paramtheparamthe{bare_field_name} to set
	*/
	
	public void setCategory_id(int category_id) {
		this.categoryId = category_id;
	}


	/**
	* @param paramtheparamthe{bare_field_name} to set
	*/
	
	public void setId(Long id) {
		this.id = id;
	}


	@Override
	public Long getId() {
	
		return id;
	}


}
