package com.tzj.collect.api.ali;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.core.param.ali.AliCategoryAttrOptionBean;
import com.tzj.collect.core.param.ali.CategoryAttrBean;
import com.tzj.collect.core.param.ali.CategoryBean;
import com.tzj.collect.core.result.ali.ClassifyAndMoney;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.collect.entity.Category.CategoryType;
import com.tzj.module.api.annotation.*;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.utils.security.CipherTools;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import static com.tzj.collect.common.constant.TokenConst.*;

/**
 * 分类相关api
 * @Author 王美霞20180305
 **/
@ApiService
public class CategoryApi {
	
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CompanyCategoryService priceService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private CompanyCategoryService companyCategoryService;
	@Autowired
	private CompanyServiceService companyServiceService;
	@Autowired
	private CommunityService communityService;
	@Autowired
	private MemberAddressService memberAddressService;
	@Autowired
	private CategoryAttrService categoryAttrService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private CategoryAttrOptionService categoryAttrOptionService;
	@Autowired
	private CompanyStreetBigService companyStreetBigService;
	@Autowired
	private CompanyStreetApplianceService companyStreetApplianceService;
	@Autowired
	private CompanyCategoryAttrOptionCityService companyCategoryAttrOptionCityService;
	@Autowired
	private CompanyCategoryCityService companyCategoryCityService;
	@Autowired
	private CompanyCategoryCityNameService companyCategoryCityNameService;
    /**
     * 取得所有一级分类 
     * @param 
     * @return
     */
	 @Api(name = "category.listTop", version = "1.0")
	 @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	 public List<Category> toplist(CategoryBean categoryBean){
		Serializable title = null;
	 	if (CategoryType.DIGITAL.toString().equals(categoryBean.getTitle())) {
	 		title = CategoryType.DIGITAL.getValue();
		}else if (CategoryType.HOUSEHOLD.toString().equals(categoryBean.getTitle())) {
			title = CategoryType.HOUSEHOLD.getValue();
		}
		 return categoryService.topList(categoryBean.getLevel(), title,"0");
	 }
	
	
	 /**
     * 根据一级分类id取得所有二级分类
     * @param 
     * @return
     */
	 @Api(name = "category.listchild", version = "1.0")
	 @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	 public  Map<String, Object> childlist(CategoryBean categoryBean){
//		 if (categoryBean.getTitle().equals(CategoryType.DIGITAL.name())) {
//			return categoryService.childList(categoryBean.getId());
//		}else if (categoryBean.getTitle().equals(CategoryType.HOUSEHOLD.name())) {
//			//return 
//		}
		return priceService.getPrice(categoryBean);
	 }
	 
 	/**
     * 根据分类 的选择的分类属性的分类选项计算价格
     * @author 王灿
     * @param   categoryAttrBean : 分类属性
     * @return BigDecimal : 预估价格
     */
	@Api(name = "categoryAttr.computeValue", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object computeValue(CategoryAttrBean categoryAttrBean){
		Member member = MemberUtils.getMember();
		//分类Id
		long categoryId = categoryAttrBean.getCategoryId();
		//所有的分类选项id
		String categoryAttrOptionPrice = categoryAttrBean.getCategoryAttrOptionPrices();
		System.out.println("所有的价格是 ： "+categoryAttrOptionPrice);
		//获取所有分类的集合
		String [] OptionPrice = categoryAttrOptionPrice.split(",");
		//查询用户的默认地址
    	MemberAddress memberAddress = memberAddressService.getMemberAdderssByAliUserId(member.getAliUserId());
    	if(memberAddress==null) {
    		return "该区域暂无回收企业";
    	}
    	//根据分类Id查询父类分类id
    	Category category = categoryService.selectById(categoryId);
    	//根据小区Id，分类id和街道id 查询相关企业
		String companyId = companyStreetApplianceService.selectStreetApplianceCompanyIdByCategoryId(category.getParentId(),memberAddress.getStreetId(),memberAddress.getCommunityId());
		if(StringUtils.isBlank(companyId)) {
				return "该区域暂无回收企业";
		}
		//根据企业Id查询和分类Id查询对应的一条关联记录
    	CompanyCategory companyCategory  = companyCategoryService.selectOne(new EntityWrapper<CompanyCategory>().eq("company_id",companyId).eq("category_id",categoryId));
    	BigDecimal price = new BigDecimal(companyCategory.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
		//
    	List<String> specialPriceList = new ArrayList<String>();
		for(int i=0;i<OptionPrice.length;i++) {
			//匹配价格里面是否存在特殊字符的价格
			String[] array = OptionPrice[i].split("P");
			if((array.length-1)>0) {
				//如果存在特殊字符
				String specialPrice = OptionPrice[i].substring(1);
				specialPriceList.add(specialPrice);
				continue;
			}
			BigDecimal prices = new BigDecimal(OptionPrice[i]).setScale(2, BigDecimal.ROUND_HALF_UP);
			price = price.add(prices);
		}
		//将取到的特殊价格从小到大排序
		Collections.sort(specialPriceList);
		if(!specialPriceList.isEmpty()) {
			//取得最小的特殊价格
			price = new BigDecimal(specialPriceList.get(0));
		}
		System.out.println("查出预估价格了了:"+price);
		BigDecimal newprice = new BigDecimal("0");
		if(newprice.compareTo(price)==1){
			return 10;
		}else{
			return price;
		}
	}
	
	@Api(name = "categoryAttr.computeValue4house", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public ClassifyAndMoney computeValue4Household(CategoryAttrBean categoryAttrBean) {
		return categoryService.reckon(categoryAttrBean);
	}
	
	/**
     * 根据分类Id和小区Id查询唯一的所属企业Id
     * @author 王灿
     * @param   categoryAttrBean : 分类属性
     * @return BigDecimal : 预估价格
     */
	@Api(name = "category.communityBycompany", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object communityBycompany(CategoryAttrBean categoryAttrBean){
		//分类Id
		long categoryId = categoryAttrBean.getCategoryId();
		//小区Id
		String communityId = categoryAttrBean.getCommunityId();
		Integer companyId = companyService.getCompanyIdByIds(Integer.parseInt(communityId),(int)categoryId);
    	if(companyId==null || companyId ==0) {
    		return "你所选的街道没有企业";
    	}
    	return "选择成功";
	}

	/**
	 * 小程序取得所有电器一级分类
	 * @param
	 * @return
	 */
	@Api(name = "category.categoryOneList", version = "1.0")
	@SignIgnore
	@AuthIgnore
	@DS("slave")
	public Object categoryOneList(CategoryBean categoryBean){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("DIGITAL",categoryService.topList(0, 1,categoryBean.getIsFiveKg()));
		resultMap.put("HOUSEHOLD",categoryService.topList(0, 2,categoryBean.getIsFiveKg()));
		resultMap.put("BIGTHING",categoryService.topList(0, 4,categoryBean.getIsFiveKg()));
		return resultMap;
	}
	/**
	 * 小程序取得所有电器大件一级分类需要token
	 * @param
	 * @return
	 */
	@Api(name = "category.categoryOneListToken", version = "1.0")
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object categoryOneListToken(){
		Member member = MemberUtils.getMember();
		String aliUserId = member.getAliUserId();
		return priceService.categoryOneListToken(aliUserId);
	}
	/**
	 * 小程序取得所有二级分类
	 * @param
	 * @return
	 */
	@Api(name = "category.categoryTwoList", version = "1.0")
	@SignIgnore
	@AuthIgnore
	public Object categoryTwoList(CategoryBean categoryBean){
		return priceService.categoryTwoList(categoryBean);

	}
	/**
	 * 小程序取得所有二级分类(需要token)
	 * @param
	 * @return
	 */
	@Api(name = "category.categoryHouseTwoList", version = "1.0")
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object categoryHouseTwoList(CategoryBean categoryBean){
		Member member = MemberUtils.getMember();
		String aliUserId = member.getAliUserId();
		return priceService.categoryHouseTwoList(categoryBean,aliUserId);

	}
	/**
	 * 小程序根据电器分类id取得所有分类属性
	 * @param
	 * @return
	 */
	@Api(name = "category.listCategoryAttrsXCX", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object listCategoryAttrsXCX(CategoryBean categoryBean){
		Member member = MemberUtils.getMember();
		String aliUserId = member.getAliUserId();
		return categoryAttrService.getCategoryAttrListss(categoryBean.getId(),aliUserId,categoryBean.getType());
	}
	/**
	 * 小程序计算价格
	 * @param
	 * @return
	 */
	@Api(name = "category.getPrices", version = "1.0")
	@SignIgnore
	@AuthIgnore
	public Object getPrices(CategoryAttrBean categoryAttrBean){
		//分类Id
		long categoryId = categoryAttrBean.getCategoryId();
		//获取token
		String token = categoryAttrBean.getToken();
		BigDecimal price = null;
		if(StringUtils.isBlank(token)){
			//所有的分类选项价格
			String categoryAttrOptionPrice = categoryAttrBean.getCategoryAttrOptionPrices();
			//获取所有分类价格的集合
			String [] OptionPrice = categoryAttrOptionPrice.split(",");
			//根据分类Id查询
			Category category  = categoryService.selectOne(new EntityWrapper<Category>().eq("id",categoryId));
			price = category.getMarketPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
			//
			List<String> specialPriceList = new ArrayList<String>();
			for(int i=0;i<OptionPrice.length;i++) {
				//匹配价格里面是否存在特殊字符的价格
				String[] array = OptionPrice[i].split("P");
				if((array.length-1)>0) {
					//如果存在特殊字符
					String specialPrice = OptionPrice[i].substring(1);
					specialPriceList.add(specialPrice);
					continue;
				}
				BigDecimal prices = new BigDecimal(OptionPrice[i]).setScale(2, BigDecimal.ROUND_HALF_UP);
				price = price.add(prices);
			}
			//将取到的特殊价格从小到大排序
			Collections.sort(specialPriceList);
			if(!specialPriceList.isEmpty()) {
				//取得最小的特殊价格
				price = new BigDecimal(specialPriceList.get(0));
			}
		}else{
			String tokenCyptoKey = ALI_API_TOKEN_CYPTO_KEY;
			String key = CipherTools.initKey(tokenCyptoKey);
			String decodeToken = CipherTools.decrypt(token, key);
			Claims claims = JwtUtils.getClaimByToken(decodeToken, ALI_API_TOKEN_SECRET_KEY);
			String memberId = claims.getSubject();
			price = categoryService.getPrice(memberId,categoryId,categoryAttrBean.getType(),categoryAttrBean.getCategoryAttrOptionids());
		}
		return price;
	}
	/**
	 * 小程序获取首页分类列表
	 * @param
	 * @return
	 */
	@Api(name = "category.getXCategoryList", version = "1.0")
	@SignIgnore
	@AuthIgnore
	@DS("slave")
	public Object getXCategoryList(){
		Map<String,Object> map = new HashMap<>();
		List<Category> DQcategoryList = categoryService.selectList(new EntityWrapper<Category>().eq("title", 1).isNull("parent_id"));
		List<Category> LFcategories = categoryService.selectList(new EntityWrapper<Category>().eq("title", 2).eq("parent_id", 0).eq("unuseful", 0));
		List<Category> FiveCategory = categoryService.selectList(new EntityWrapper<Category>().eq("parent_id", 45));
		map.put("DQcategoryList",DQcategoryList);
		map.put("LFcategories",LFcategories);
		map.put("FiveCategory",FiveCategory);
		return  map;
	}

	/**
	* 小程序最新获取生活分类列表
	* @param
	* @return
	*/
	@Api(name = "category.categoryNewHouseList", version = "1.0")
	@SignIgnore
	@AuthIgnore
	public Object getCategoryNewHouseList(){
		return categoryService.getCategoryNewHouseList();
	}
	/**
	 * 小程序最新获取生活分类列表
	 * @param
	 * @return
	 */
	@Api(name = "category.categoryNewHouseListByToken", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object getCategoryNewHouseListByToken(){
		Member member = MemberUtils.getMember();
		String aliUserId = member.getAliUserId();
		return categoryService.getCategoryNewHouseListByToken(aliUserId);
	}

	/**
	 * 小程序计算价格
	 * @param
	 * @return
	 */
	@Api(name = "category.getPricesAll", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object getPricesAll(CategoryAttrBean categoryAttrBean){
		Member member = MemberUtils.getMember();
		return categoryService.getPricesAll(member.getAliUserId(),categoryAttrBean.getCategoryId(),categoryAttrBean.getType(),categoryAttrBean.getCategoryAttrOptionids());
	}

}
