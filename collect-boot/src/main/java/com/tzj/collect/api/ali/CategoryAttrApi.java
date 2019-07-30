package com.tzj.collect.api.ali;

import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.core.param.ali.CategoryBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.MemberAddress;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;

/**
 * 分类属性相关api
 * @Author 王美霞20180306
 **/
@ApiService
public class CategoryAttrApi {
	
	@Autowired
	private CategoryAttrService categoryAttrService;
	@Autowired
	private CategoryAttrOptionService categoryAttrOptionService;
	@Autowired
	private CompanyServiceService companyServiceService;
	@Autowired
	private MemberAddressService memberAddressService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CompanyCategoryService companyCategoryService;
	@Autowired
	private CompanyStreetApplianceService companyStreetApplianceService;
    /**
     * 根据分类id取得所有分类属性
     * @author 王灿
     * @param
     * @return List<CategoryAttr>
     */
	@Api(name = "categoryAttr.listCategoryAttrs", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object getCategoryAttrList(CategoryBean categoryBean){
		//查询用户的默认地址
		Member member = MemberUtils.getMember();
		System.out.println("------memberId参数是 ："+member.getAliUserId()+"------category参数是 ："+categoryBean.getId()+"-------cityId是 ："+categoryBean.getCityId());
		MemberAddress memberAddress = memberAddressService.getMemberAdderssByAliUserId(member.getAliUserId());
    	if(memberAddress==null) {
    		//根据分类id取得所有分类属性
    		return categoryAttrService.getCategoryAttrListss(categoryBean.getId());
    	}
    	//根据分类Id查询父类分类id
    	Category category = categoryService.selectById(categoryBean.getId());
		//根据小区Id，分类id和街道id 查询相关企业
		String companyId = companyStreetApplianceService.selectStreetApplianceCompanyId(category.getParentId(),memberAddress.getStreetId(),memberAddress.getCommunityId());
		if(StringUtils.isBlank(companyId)) {
			return "该区域暂无服务";
		}
		//根据分类id取得所有分类属性
		return categoryAttrService.getCategoryAttrLists(categoryBean.getId(),Integer.parseInt(companyId));
    }
	
}
