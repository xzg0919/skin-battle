package com.tzj.collect.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.CategoryAttr;

import java.util.List;

public interface CategoryAttrService extends IService<CategoryAttr> {
	
	/**
     * 根据分类id取得所有分类属性
     * @author 王灿
     * @param  CategoryId : 分类id
     * @param pageBean : 分页 
     * @return List<CategoryAttr>
     */
	@DS("slave")
	List<CategoryAttr> getCategoryAttrLists(int CategoryId,long companyId);
	/**
     * 根据分类id取得所有分类属性
     * @author 王灿
     * @param  CategoryId : 分类id
     * @param pageBean : 分页 
     * @return List<CategoryAttr>
     */
	@DS("slave")
	List<CategoryAttr> getCategoryAttrListss(int CategoryId);
	
	 /**
     * 新增/更新 分类属性和分类选项数据
     * @author: 王灿
     * @param  categoryId 分类Id
     * @param  categoryAttrId 分类属性Id
     * @param  categoryAttrName 分类属性名字
     * @param  categoryAttrOptionIds 所有的分类选项Id
     * @param  categoryAttrOptionNames 所有的分类选项名字
     * @param  categoryAttrOptionPrices 所有的分类选项价格
     * @return String    返回类型  
     */
	String savaByCategorys(Integer categoryId,String categoryAttrId,String categoryAttrName,String categoryAttrOptionIds,String categoryAttrOptionNames,String categoryAttrOptionPrices);
}
