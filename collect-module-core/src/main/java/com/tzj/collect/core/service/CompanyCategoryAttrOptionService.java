package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.taobao.api.ApiException;
import com.tzj.collect.core.param.business.CategoryAttrOptionBean;
import com.tzj.collect.core.param.business.ComIdAndCateOptIdBean;
import com.tzj.collect.entity.CompanyCategoryAttrOption;

import java.util.List;

public interface CompanyCategoryAttrOptionService extends IService<CompanyCategoryAttrOption>{
	/**
	 * 修改公司分类属性类别价格
	 * @param comIdAndCateOptIdBean
	 * @return
	 * @throws ApiException 
	 */
	boolean modifyComCateAttOptPrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean) throws ApiException;

	boolean updateComCateAttOptPrice(List<CategoryAttrOptionBean> categoryAttrOptionBeanList, String cityId) throws ApiException;
	
	//boolean modifyCompanyCategoryAttrOptPrice(List<CategoryAttrOptionBean> categoryAttrOptionBeanList) throws ApiException;
}
