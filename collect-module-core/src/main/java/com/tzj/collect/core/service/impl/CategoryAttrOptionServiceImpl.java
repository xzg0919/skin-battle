package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CategoryAttrOptionMapper;
import com.tzj.collect.core.param.ali.AliCategoryAttrOptionBean;
import com.tzj.collect.core.param.business.CategoryBean;
import com.tzj.collect.core.service.CategoryAttrOptionService;
import com.tzj.collect.entity.CategoryAttrOption;
import com.tzj.collect.entity.CompanyAccount;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.easyopen.ApiContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Transactional(readOnly=true)
public class CategoryAttrOptionServiceImpl extends ServiceImpl<CategoryAttrOptionMapper, CategoryAttrOption> implements CategoryAttrOptionService {
	
	@Autowired
	private CategoryAttrOptionMapper categoryAttrOptionMapper;
	
	/**
     * 根据分类选项的Id(主键)查询所有的分类选项信息
     * @author 王灿
     * @param   OptionId : 分类选项Id
     * @return CategoryAttrOption : 分类选项信息
     */
	@Override
	public CategoryAttrOption getOptionById(String OptionId) {
		EntityWrapper<CategoryAttrOption> wrapper = new EntityWrapper<CategoryAttrOption>();
		wrapper.eq("id", OptionId);
		wrapper.eq("del_flag", "0");
		return this.selectOne(wrapper);
	}
	
	/**
     * 根据分类属性的Id(categoryAttrId)查询有关所有的分类选项信息
     * @author 王灿
     * @param   categoryAttrId : 分类属性Id
     * @param   companyId : 企业ID
     * @return List<CategoryAttrOption> : 有关所有的分类选项信息
     */
	@Override
	public List<CategoryAttrOption> getOptionByCategoryAttrId(long categoryAttrId,long companyId) {
		
		/*EntityWrapper<CategoryAttrOption> wrapper = new EntityWrapper<CategoryAttrOption>();
		wrapper.eq("category_attr_id", categoryAttrId);
		wrapper.eq("del_flag", "0");
		this.selectList(wrapper);*/
		return categoryAttrOptionMapper.getCategoryAttrOptionList(categoryAttrId, companyId);
		
	}
	/**
	 * 根据分类属性的Id(categoryAttrId)查询有关所有的分类选项信息
	 * @author 王灿
	 * @param   categoryAttrOptionId : 分类属性Id
	 * @param   companyId : 企业ID
	 * @return List<CategoryAttrOption> : 有关所有的分类选项信息
	 */
	@Override
	public AliCategoryAttrOptionBean getCategoryAttrOptionById(long categoryAttrOptionId, long companyId) {

		/*EntityWrapper<CategoryAttrOption> wrapper = new EntityWrapper<CategoryAttrOption>();
		wrapper.eq("category_attr_id", categoryAttrId);
		wrapper.eq("del_flag", "0");
		this.selectList(wrapper);*/
		return categoryAttrOptionMapper.getCategoryAttrOptionById(categoryAttrOptionId, companyId);

	}
	/**
     * 根据分类属性的Id(categoryAttrId)查询有关所有的分类选项信息
     * @author 王灿
     * @return List<CategoryAttrOption> : 有关所有的分类选项信息
     */
	@Override
	public List<CategoryAttrOption> getOptionByCategoryAttrIds(long categoryAttrId) {
		
//		EntityWrapper<CategoryAttrOption> wrapper = new EntityWrapper<CategoryAttrOption>();
//		wrapper.eq("category_attr_id", categoryAttrId);
//		wrapper.eq("del_flag", "0");
//		return this.selectList(wrapper);
		return categoryAttrOptionMapper.getCategoryAttrOptionLists(categoryAttrId);
		
	}

	@Override
	public List<CategoryAttrOption> getDigitName(CategoryBean categoryBean) {
		HttpServletRequest request = ApiContext.getRequest();
		Subject subject = (Subject)request.getAttribute("subject");
		// 接口里面获取 CompanyAccount 的例子
		CompanyAccount companyAccount = (CompanyAccount)subject.getUser();
		return categoryAttrOptionMapper.getDigitName(Integer.parseInt(categoryBean.getId()),companyAccount.getCompanyId());
	}
	

	

}
