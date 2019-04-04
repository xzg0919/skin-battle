package com.tzj.collect.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.taobao.api.ApiException;
import com.tzj.collect.api.business.param.CategoryAttrOptionBean;
import com.tzj.collect.api.business.param.ComIdAndCateOptIdBean;
import com.tzj.collect.entity.CompanyCategoryAttrOption;

public interface CompanyCategoryAttrOptionService extends IService<CompanyCategoryAttrOption>{
	/**
	 * 修改公司分类属性类别价格
	 * @param comIdAndCateOptIdBean
	 * @return
	 * @throws ApiException 
	 */
	boolean modifyComCateAttOptPrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean) throws ApiException;

	boolean updateComCateAttOptPrice(List<CategoryAttrOptionBean> categoryAttrOptionBeanList) throws ApiException;
	
	//boolean modifyCompanyCategoryAttrOptPrice(List<CategoryAttrOptionBean> categoryAttrOptionBeanList) throws ApiException;
}
