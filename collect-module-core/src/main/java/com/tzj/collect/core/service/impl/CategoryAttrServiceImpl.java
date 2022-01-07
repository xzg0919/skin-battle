package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CategoryAttrMapper;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly=true)
public class CategoryAttrServiceImpl extends ServiceImpl<CategoryAttrMapper, CategoryAttr>  implements CategoryAttrService {
	
	@Autowired
	private CategoryAttrService categoryAttrService;
	@Autowired
	private CategoryAttrOptionService categoryAttrOptionService;
	@Autowired
	private MemberAddressService memberAddressService;
	@Autowired
	private CompanyStreetApplianceService companyStreetApplianceService;
	@Autowired
	private CompanyStreetBigService companyStreetBigService;
	/**
     * 根据分类id取得所有分类属性
     * @author 王灿
     * @return List<CategoryAttr>
     */
	@Override
	public List<CategoryAttr> getCategoryAttrLists(int categoryId,long companyId) {
		EntityWrapper<CategoryAttr> wraper = new EntityWrapper<CategoryAttr>();
		wraper.eq("category_id", categoryId);
		wraper.eq("del_flag", "0");
		List<CategoryAttr> list = this.selectList(wraper);
		//根据分类属性Id取 分类属性选项
				for(int i=0;i<list.size();i++){
					List<CategoryAttrOption> optionList = categoryAttrOptionService.getOptionByCategoryAttrId((long)list.get(i).getId(),companyId);
					list.get(i).setCategoryAttrOptionList(optionList);
					list.get(i).setCategoryId(categoryId);
				}
		return list;
	}

	@Autowired
	CompanyStreetElectroMobileService companyStreetElectroMobileService;
	/**
     * 根据分类id取得所有分类属性
     * @author 王灿
     * @return List<CategoryAttr>
     */
	@Override
	public List<CategoryAttr> getCategoryAttrListss(int categoryId,String aliUserId,String type) {
		//根据分类Id和和地址信息查询所属企业
		MemberAddress memberAddress = memberAddressService.getMemberAdderssByAliUserId(aliUserId);
		String companyId = "";
		if (null != memberAddress) {
			if ("bigFurniture".equals(type)) {
				companyId = companyStreetBigService.selectStreetBigCompanyId(memberAddress.getStreetId())+"";
			}else if ("appliance".equals(type)) {
				companyId = companyStreetApplianceService.selectStreetApplianceCompanyId(memberAddress.getStreetId(), memberAddress.getCommunityId());
			}
			else if ("electromobile".equals(type)) {
				companyId = String.valueOf(companyStreetElectroMobileService.selectCompanyByStreetId(memberAddress.getStreetId()));
			}
		}
		EntityWrapper<CategoryAttr> wraper = new EntityWrapper<CategoryAttr>();
		wraper.eq("category_id", categoryId);
		wraper.eq("del_flag", "0");
		List<CategoryAttr> list = this.selectList(wraper);
		//根据分类属性Id取 分类属性选项
		if (StringUtils.isNotBlank(companyId)){
			for(int i=0;i<list.size();i++){
				List<CategoryAttrOption> optionList = categoryAttrOptionService.getOptionByCategoryAttrIdByCompanyId((long)list.get(i).getId(),companyId,memberAddress.getCityId());
				list.get(i).setCategoryAttrOptionList(optionList);
				list.get(i).setCategoryId(categoryId);
			}
		}else {
			for(int i=0;i<list.size();i++){
				List<CategoryAttrOption> optionList = categoryAttrOptionService.getOptionByCategoryAttrIds((long)list.get(i).getId());
				list.get(i).setCategoryAttrOptionList(optionList);
				list.get(i).setCategoryId(categoryId);
			}
		}
		return list;
	}
	
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
	@Transactional
	@Override
	public String savaByCategorys(Integer categoryId, String categoryAttrId, String categoryAttrName,
			String categoryAttrOptionIds, String categoryAttrOptionNames, String categoryAttrOptionPrices) {
		//将 分类选项Id 分类选项价格 分类选项调整价格 分割成数组
    	String [] OptionIds =null;
    	if(StringUtils.isNotBlank(categoryAttrOptionIds)) {
    		OptionIds = categoryAttrOptionIds.split(",");
    	}
		String [] OptionNames = categoryAttrOptionNames.split(",");
		String [] OptionPrices = categoryAttrOptionPrices.split(",");    		
    	CategoryAttr categoryAttr = null;
    	CategoryAttrOption categoryAttrOption = null;
    	//判断属性Id是否为空  如果为空那么就是新增 反之更新
    	if(StringUtils.isNotBlank(categoryAttrId)) {
    		//根据属性Id查询分类属性表，更新属性名称
    		categoryAttr = this.selectById(categoryAttrId);
    		categoryAttr.setName(categoryAttrName);
    		categoryAttrService.updateById(categoryAttr);   		
    		for(int i=0;i<OptionIds.length;i++) {
    			categoryAttrOption = categoryAttrOptionService.selectById(OptionIds[i]);
    			categoryAttrOption.setName(OptionNames[i]);
    			categoryAttrOption.setPrice(new BigDecimal(OptionPrices[i]));
    			categoryAttrOptionService.updateById(categoryAttrOption);
    		}
    	}else {
    		categoryAttr = new CategoryAttr();
    		categoryAttr.setCategoryId(categoryId);
    		categoryAttr.setName(categoryAttrName);
    		categoryAttrService.insert(categoryAttr);
    		for(int i=0;i<OptionNames.length;i++) {
    			categoryAttrOption = new CategoryAttrOption();
    			categoryAttrOption.setCategoryAttrId(categoryAttr.getId());
    			categoryAttrOption.setName(OptionNames[i]);
    			categoryAttrOption.setPrice(new BigDecimal(OptionPrices[i]));
    			categoryAttrOptionService.insert(categoryAttrOption);
    		}
    	}
		return "SUCCESS";
	}

}
