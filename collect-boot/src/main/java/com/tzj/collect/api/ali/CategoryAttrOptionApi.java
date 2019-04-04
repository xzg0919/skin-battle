package com.tzj.collect.api.ali;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.entity.CategoryAttr;
import com.tzj.collect.entity.CategoryAttrOption;
import com.tzj.collect.entity.CompanyServiceRange;
import com.tzj.collect.service.CategoryAttrOptionService;
import com.tzj.collect.service.CompanyServiceService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;

/**
 * 分类属性相关api
 * @Author 王美霞20180306
 **/
@ApiService
public class CategoryAttrOptionApi {
	
	@Autowired
	private CategoryAttrOptionService categoryAttrOptionService;
	@Autowired
	private CompanyServiceService companyServiceService;
	/**
     * 根据分类 的属性取 分类属性选项
     * @author 
     * @param  Categorybean 
     * @return List<CategoryAttr>
     */
	@Api(name = "categoryAttrOption.listCategoryAttrOption", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object getCategoryAttrOptionList(CategoryAttr categoryAttr){
		if(categoryAttr.getCommunityId()==null) {
			return "请传入小区id";
		}
		//根据小区Id查询唯一的所属企业Id
		CompanyServiceRange companyServiceRange = companyServiceService.selectOne(new EntityWrapper<CompanyServiceRange>().eq("community_id", categoryAttr.getCommunityId()));
    	if(companyServiceRange==null) {
    		return "您的小区暂无回收企业";
    	}
		List<CategoryAttrOption> categoryAttrOptionList =categoryAttrOptionService.getOptionByCategoryAttrId((long)categoryAttr.getId(),Integer.parseInt(companyServiceRange.getCompanyId()));
    	return categoryAttrOptionList;
    }
	
	
	
	
}
