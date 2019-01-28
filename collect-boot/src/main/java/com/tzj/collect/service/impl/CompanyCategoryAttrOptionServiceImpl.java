package com.tzj.collect.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.taobao.api.ApiException;
import com.tzj.collect.api.business.param.CategoryAttrOptionBean;
import com.tzj.collect.api.business.param.ComIdAndCateOptIdBean;
import com.tzj.collect.api.business.param.CompanyCategoryAttrOptionBean;
import com.tzj.collect.entity.Category.CategoryType;
import com.tzj.collect.entity.CompanyAccount;
import com.tzj.collect.entity.CompanyCategory;
import com.tzj.collect.entity.CompanyCategoryAttrOption;
import com.tzj.collect.mapper.CompanyCategoryAttrOptionMapper;
import com.tzj.collect.mapper.CompanyCategoryMapper;
import com.tzj.collect.service.CompanyCategoryAttrOptionService;
import com.tzj.collect.service.CompanyCategoryService;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.easyopen.ApiContext;
@Service
public class CompanyCategoryAttrOptionServiceImpl extends ServiceImpl<CompanyCategoryAttrOptionMapper, CompanyCategoryAttrOption> implements CompanyCategoryAttrOptionService {
	@Autowired
	private CompanyCategoryMapper comCateMapper;
	@Autowired
	private CompanyCategoryService companyCategoryService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean modifyComCateAttOptPrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean) throws ApiException {
		if (comIdAndCateOptIdBean.getTitle() == null || comIdAndCateOptIdBean.getCategoryId() == null || comIdAndCateOptIdBean.getCateOptId() == null) {
			throw new ApiException("请传递正确的参数");
		}else if (CategoryType.DIGITAL.name().equals(comIdAndCateOptIdBean.getTitle())) {
			EntityWrapper<CompanyCategoryAttrOption> wraper = new EntityWrapper<CompanyCategoryAttrOption>();
			wraper.eq("company_id", comIdAndCateOptIdBean.getCompanyId());
			wraper.eq("category_attr_option_id", comIdAndCateOptIdBean.getCateOptId());
			wraper.eq("del_flag", "0");
			CompanyCategoryAttrOption attrOption = this.selectOne(wraper);
			if (attrOption != null) {
				attrOption.setAttrOptionPrice(new BigDecimal(comIdAndCateOptIdBean.getCateOptPrice()));
				attrOption.setUpdateBy(comIdAndCateOptIdBean.getCompanyId());
				attrOption.setUpdateDate(new Date());
				this.updateAllColumnById(attrOption);
			}else {
				attrOption = new CompanyCategoryAttrOption();
				attrOption.setCompanyId(Long.parseLong(comIdAndCateOptIdBean.getCompanyId()));
				attrOption.setAttrOptionPrice(new BigDecimal(comIdAndCateOptIdBean.getCateOptPrice()));
				attrOption.setCategoryAttrOptionId(Long.parseLong(comIdAndCateOptIdBean.getCateOptId()));
				this.insert(attrOption);
			}
			
			//判断实际价格是否小于零，若为真rollback
			boolean flag = false;
			BigDecimal minPrice = new BigDecimal(comCateMapper.priceIsAvailable(comIdAndCateOptIdBean));
			if (minPrice.compareTo(BigDecimal.ZERO) >= 0) {
				return true;
			}else{
				EntityWrapper<CompanyCategory> wraper2 = new EntityWrapper<>();
				wraper2.eq("company_id", comIdAndCateOptIdBean.getCompanyId());
				wraper2.eq("category_id", comIdAndCateOptIdBean.getCategoryId());
				wraper2.eq("del_flag", "0");
				List<CompanyCategory> comCateList = comCateMapper.selectList(wraper2);
				if (comCateList.size() > 0) {
					CompanyCategory companyCategory = comCateList.get(0);
					BigDecimal guidePrice = new BigDecimal(companyCategory.getPrice()) ;
					if (guidePrice.add(minPrice).compareTo(BigDecimal.ZERO) >= 0) {
						flag = true;
					}
				}
			}
			if (!flag) {
				throw new ApiException("当前价格不可使用,请确认再操作");
			}else{
				return true;
			}
		}else{
			throw new ApiException("只能修改家电数码的分类属性价格");
		}
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateComCateAttOptPrice(List<CategoryAttrOptionBean> categoryAttrOptionBeanList) throws ApiException {
		Subject subject=ApiContext.getSubject();
		// 接口里面获取 CompanyAccount 的例子
		CompanyAccount companyAccount = (CompanyAccount)subject.getUser();
		if (companyAccount != null) {
			//判断总价格是否大于0
			if(categoryAttrOptionBeanList!=null&&categoryAttrOptionBeanList.size()>0) {
				String id = categoryAttrOptionBeanList.get(0).getId().toString();
				CompanyCategory companyCategory = companyCategoryService.selectPriceByAttrId(id,companyAccount.getCompanyId().toString());
				if(companyCategory!=null) {
					double price = (double) companyCategory.getPrice();
					for (CategoryAttrOptionBean categoryAttrOptionBean : categoryAttrOptionBeanList) {
						List<CompanyCategoryAttrOptionBean> companyCategoryAttrOptionBeanList = categoryAttrOptionBean.getCompanyCategoryAttrOptionBeanList();
						double minPrice = Double.parseDouble(companyCategoryAttrOptionBeanList.get(0).getComOptPrice());
						double newPrice = 0;
						for (int i = 1; i < companyCategoryAttrOptionBeanList.size(); i++) {
							newPrice = Double.parseDouble(companyCategoryAttrOptionBeanList.get(i).getComOptPrice());
							if(minPrice>newPrice) {
								minPrice = newPrice;
							}
						}
						price+=minPrice;
					}
					if(price<=0) {
						throw new ApiException("总价格应大于0");
					}else {
						for (CategoryAttrOptionBean categoryAttrOptionBean : categoryAttrOptionBeanList) {
							List<CompanyCategoryAttrOptionBean> companyCategoryAttrOptionBeanList = categoryAttrOptionBean.getCompanyCategoryAttrOptionBeanList();
							for (CompanyCategoryAttrOptionBean companyCategoryAttrOptionBean : companyCategoryAttrOptionBeanList) {
								CompanyCategoryAttrOption companyCategoryAttrOption = new CompanyCategoryAttrOption();
								companyCategoryAttrOption.setCompanyId(companyAccount.getCompanyId().longValue());
								companyCategoryAttrOption.setAttrOptionPrice(new BigDecimal(companyCategoryAttrOptionBean.getComOptPrice()));
								companyCategoryAttrOption.setCategoryAttrOptionId(Long.valueOf(companyCategoryAttrOptionBean.getAttOptionId()));
								EntityWrapper<CompanyCategoryAttrOption> wraper = new EntityWrapper<CompanyCategoryAttrOption>();
								wraper.eq("company_id", companyCategoryAttrOption.getCompanyId());
								wraper.eq("category_attr_option_id", companyCategoryAttrOption.getCategoryAttrOptionId());
								wraper.eq("del_flag", "0");
								CompanyCategoryAttrOption attrOption = this.selectOne(wraper);
								if (attrOption != null) {
									attrOption.setAttrOptionPrice(companyCategoryAttrOption.getAttrOptionPrice());
									attrOption.setUpdateBy(companyCategoryAttrOption.getCompanyId().toString());
									attrOption.setUpdateDate(new Date());
									attrOption.setSpecialPrice(StringUtils.isBlank(companyCategoryAttrOptionBean.getSpecialPrice())?BigDecimal.ZERO:new BigDecimal(companyCategoryAttrOptionBean.getSpecialPrice()));
									this.updateAllColumnById(attrOption);
								}else {
									attrOption = new CompanyCategoryAttrOption();
									attrOption.setCompanyId(companyCategoryAttrOption.getCompanyId());
									attrOption.setAttrOptionPrice(companyCategoryAttrOption.getAttrOptionPrice());
									attrOption.setCategoryAttrOptionId(companyCategoryAttrOption.getCategoryAttrOptionId());
									attrOption.setSpecialPrice(StringUtils.isBlank(companyCategoryAttrOptionBean.getSpecialPrice())?BigDecimal.ZERO:new BigDecimal(companyCategoryAttrOptionBean.getSpecialPrice()));
									this.insert(attrOption);
								}
							}
						}
						return true;
					}
				}else {
					throw new ApiException("获取不到价格!");
				}
			}else {
				throw new ApiException("集合为空!");
			}
		}else {
			throw new ApiException("请先登录,再执行更新操作");
		}
	}


	
	
}
