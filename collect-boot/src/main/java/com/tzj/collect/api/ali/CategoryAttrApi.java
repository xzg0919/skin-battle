package com.tzj.collect.api.ali;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.ali.param.CategoryBean;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.entity.*;
import com.tzj.collect.service.*;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
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
	private CompanyShareService companyShareService;
	@Autowired
	private CompanyCategoryService companyCategoryService;
    /**
     * 根据分类id取得所有分类属性
     * @author 王灿
     * @param
     * @return List<CategoryAttr>
     */
	@Api(name = "categoryAttr.listCategoryAttrs", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object getCategoryAttrList(CategoryBean categoryBean){
		//查询用户的默认地址
		Member member = MemberUtils.getMember();
		System.out.println("------memberId参数是 ："+member.getId()+"------category参数是 ："+categoryBean.getId()+"-------cityId是 ："+categoryBean.getCityId());
		MemberAddress memberAddress = memberAddressService.selectOne(new EntityWrapper<MemberAddress>().eq("is_selected",1).eq("del_flag", 0).eq("member_id", member.getId()).eq("city_id",categoryBean.getCityId()));
    	if(memberAddress==null) {
    		//根据分类id取得所有分类属性
    		return categoryAttrService.getCategoryAttrListss(categoryBean.getId());
    	}
    	//根据分类Id查询父类分类id
    	Category category = categoryService.selectById(categoryBean.getId());
    	Integer communityId = memberAddress.getCommunityId();
    	String companyId = "";
    	String areaId = memberAddress.getAreaId()+"";
		//根据小区Id查询的私海所属企业
		Company company = companyCategoryService.selectCompany(category.getParentId(), communityId);
		if(company == null) {
			//根据分类Id和小区id去公海查询相关企业
			CompanyShare companyShare =	companyShareService.selectOne(new EntityWrapper<CompanyShare>().eq("category_id", category.getParentId()).eq("area_id", areaId));
			if(companyShare==null) {
				return categoryAttrService.getCategoryAttrListss(categoryBean.getId());
			}
			companyId = companyShare.getCompanyId().toString();
		}else {
			companyId = company.getId().toString();
		}
		//根据分类id取得所有分类属性
		return categoryAttrService.getCategoryAttrLists(categoryBean.getId(),Integer.parseInt(companyId));
    }
	
	
	/**
	 * 展示生活垃圾的回首类型,可以修改调用上面的接口
	 */
	
}
