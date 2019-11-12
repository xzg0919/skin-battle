package com.tzj.collect.api.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.core.param.admin.CategoryAttrBean;
import com.tzj.collect.core.param.admin.CategoryBean;
import com.tzj.collect.core.param.admin.CompanyCategoryBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.CategoryAttr;
import com.tzj.collect.entity.CategoryAttrOption;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tzj.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

@ApiService
public class AdminCategoryApi {
	
	@Autowired
	private CompanyCategoryService companyCategoryService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CategoryAttrService categoryAttrService;
	@Autowired
	private CategoryAttrOptionService categoryAttrOptionService;
	@Autowired
	private CompanyCategoryCityNameService companyCategoryCityNameService;
	
	/**
     * 查询分类列表
     * @author: 王灿
     * @param  
     * @return TokenBean    返回类型     
     */
    @Api(name = "category.getCategoryList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public List<Category> getCategoryList(){
		EntityWrapper<Category> wrapper = new EntityWrapper<Category>();
			wrapper.eq("del_flag", "0");
			wrapper.isNull("parent_id");
			List<Category> list = categoryService.selectList(wrapper);
    	return list;
    }
    
    /**
     * 新增一条分类(父类)
     * @author: 王灿
     * @param  
     * @return TokenBean    返回类型  
     */
    @Api(name = "category.insertCategory", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public String insertCategory(CompanyCategoryBean companyCategoryBean) {
    	//获取前端传过来的分类名字
    	String name = companyCategoryBean.getCategory().getName();
    	Category category = new Category();
    	category.setName(name);
    	//新增一条父级分类
    	categoryService.insert(category);
    	/*//获取父级分类的Id
    	long categoryId = category.getId();
    	//储存父级分类和企业进行关联
    	CompanyCategory companyCategory = new CompanyCategory();
    	companyCategory.setCompanyId(companyCategoryBean.getCompanyId());
    	companyCategory.setCategoryId(categoryId+"");
    	companyCategoryService.insert(companyCategory);*/
    	return "SUCCESS";
    }
    /**
     * 根据分类Id删除分类
     * @author: 王灿
     * @param  
     * @return TokenBean    返回类型  
     */
    @Api(name = "category.deleteCategory", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public String deleteCategory(CompanyCategoryBean companyCategoryBean) {
    	//删除分类
    	categoryService.deleteByCategoryId(companyCategoryBean.getCategoryId());
    	return "SUCCESS";
    }
    
    /**
     * 根据父类分类Id查询所属的子分类
     * @author: 王灿
     * @param  
     * @return TokenBean    返回类型  
     */
    @Api(name = "category.selectCategoryFatherId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public List<Category> selectCategoryFatherId(CategoryBean categoryBean) {
    	List<Category> list = categoryService.selectList(new EntityWrapper<Category>().eq("parent_id", categoryBean.getId()).eq("del_flag", "0"));
    	return list;
    }
    
    
    /**
     * 根据父类分类Id新增一个子分类
     * @author: 王灿
     * @param  
     * @return TokenBean    返回类型  
     */
    @Api(name = "category.saveCategoryFatherId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public String saveCategoryFatherId(CategoryBean categoryBean) {
    	Category category = new Category();
    	category.setIcon(categoryBean.getIcon());
    	category.setName(categoryBean.getName());
    	category.setParentId(categoryBean.getId().intValue());
    	category.setParentIds(categoryBean.getId()+"_");
    	category.setLevel(1);
    	categoryService.insert(category);
    	return "SUCCESS";
    }
    
    /**
     * 根据分类Id查询分类信息及相关所有的分类属性信息
     * @author: 王灿
     * @param  
     * @return TokenBean    返回类型  
     */
    @Api(name = "category.selectByCategoryId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Map<String,Object> selectByCategoryId(CategoryBean categoryBean){
    	Category category = categoryService.selectOne(new EntityWrapper<Category>().eq("id", categoryBean.getId()).eq("del_flag", "0"));
    	List<CategoryAttr> list = categoryAttrService.selectList(new EntityWrapper<CategoryAttr>().eq("category_id", categoryBean.getId()).eq("del_flag", "0"));
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	resultMap.put("category", category);
    	resultMap.put("CategoryAttrList", list);
    	return resultMap;
    }
    
    /**
     * 根据分类属性Id查询分类分类选项信息
     * @author: 王灿
     * @param  
     * @return TokenBean    返回类型  
     */
    @Api(name = "category.selectByCategoryAttrId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public List<CategoryAttrOption> selectByCategoryAttrId(CategoryAttrBean categoryAttrBean){
    	List<CategoryAttrOption> list = categoryAttrOptionService.selectList(new EntityWrapper<CategoryAttrOption>().eq("category_attr_id", categoryAttrBean.getId()).eq("del_flag", "0"));
    	return list;
    }
    
    /**
     * 根据分类属性Id删除相关联的分类属性信息
     * @author: 王灿
     * @param  
     * @return TokenBean    返回类型  
     */
    @Api(name = "category.delectByCategoryAttrId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public String delectByCategoryAttrId(CategoryAttrBean categoryAttrBean) {
    	CategoryAttr categoryAttr = categoryAttrService.selectById(categoryAttrBean.getId());
    	categoryAttr.setDelFlag("1");
    	categoryAttrService.updateById(categoryAttr);
    	return "SUCCESS";
    }
    
    /**
     * 根据分类选项Id删除相关联的分类选项信息
     * @author: 王灿
     * @param  
     * @return TokenBean    返回类型  
     */
    @Api(name = "category.delectByCategoryAttrOptionId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public String delectByCategoryAttrOptionId(CategoryAttrBean categoryAttrBean) {
    	CategoryAttrOption categoryAttrOption = categoryAttrOptionService.selectById(categoryAttrBean.getCategoryAttrOptionId());
    	categoryAttrOption.setDelFlag("1");
    	categoryAttrOptionService.updateById(categoryAttrOption);
    	return "SUCCESS";
    }
    
    /**
     * 根据分类Id更新分类信息(基准价和市场价)
     * @author: 王灿
     * @param  
     * @return TokenBean    返回类型  
     */
    @Api(name = "category.updateByCategoryId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public String updateByCategoryId(CategoryBean categoryBean) {
    	Category category = categoryService.selectById(categoryBean.getId());
    	category.setPrice(categoryBean.getPrice());
    	category.setMarketPrice(categoryBean.getMarketPrice());
    	categoryService.updateById(category);
    	return "SUCCESS";
    }
    
    /**
     * 新增/更新 分类属性和分类选项数据
     * @author: 王灿
     * @param  
     * @return TokenBean    返回类型  
     */
    @Api(name = "category.savaByCategorys", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public String savaByCategorys(CategoryAttrBean categoryAttrBean) {
    	//取得分类Id
    	int categoryId = categoryAttrBean.getCategoryId();
    	//取得分类属性Id
    	String categoryAttrId = categoryAttrBean.getId();
    	//取得更改/新增 后的分类属性名称
    	String categoryAttrName = categoryAttrBean.getName();
    	//取得所有的分类选项id
    	String categoryAttrOptionIds = categoryAttrBean.getCategoryAttrOptionIds();
    	//取得所有的分类选项名字
    	String categoryAttrOptionNames = categoryAttrBean.getCategoryAttrOptionNames();
    	//取得所有分类选项调整价格
    	String categoryAttrOptionPrices = categoryAttrBean.getCategoryAttrOptionPrices();
    	
    	String statu = categoryAttrService.savaByCategorys(categoryId,categoryAttrId,categoryAttrName,categoryAttrOptionIds,categoryAttrOptionNames,categoryAttrOptionPrices);
    	
    	return statu;
    }
    /**
     * 根据服务商Id和城市Id和类型获取相关的分类列表
     * @author: 王灿
     * @param
     * @return TokenBean    返回类型
     */
    @Api(name = "category.getCategoryListByCompanyCityId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getCategoryListByCompanyCityId(CategoryBean categoryBean) {
        return companyCategoryCityNameService.getCategoryListByCompanyCityId(categoryBean);
    }

    /**
     * 根据服务商Id和城市Id和二级类型Id获取删除或添加相关分类
     * @author: 王灿
     * @param
     * @return TokenBean    返回类型
     */
    @Api(name = "category.saveOrDeleteCategoryById", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object saveOrDeleteCategoryById(CategoryBean categoryBean) {
        return companyCategoryCityNameService.saveOrDeleteCategoryById(categoryBean);
    }
    
}	
