package com.tzj.collect.api.ali;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.ali.param.AliCategoryAttrOptionBean;
import com.tzj.collect.api.ali.param.CategoryAttrBean;
import com.tzj.collect.api.ali.param.CategoryBean;
import com.tzj.collect.api.ali.result.ClassifyAndMoney;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.entity.*;
import com.tzj.collect.entity.Category.CategoryType;
import com.tzj.collect.service.*;
import com.tzj.module.api.annotation.*;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.utils.security.CipherTools;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;
import static com.tzj.collect.common.constant.TokenConst.ALI_API_TOKEN_CYPTO_KEY;
import static com.tzj.collect.common.constant.TokenConst.ALI_API_TOKEN_SECRET_KEY;

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
	private CompanyShareService companyShareService;
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
    /**
     * 取得所有一级分类 
     * @param 
     * @return
     */
	 @Api(name = "category.listTop", version = "1.0")
	 @SignIgnore
	 @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	 public List<Category> toplist(CategoryBean categoryBean){
		Serializable title = null;
	 	if (CategoryType.DIGITAL.toString().equals(categoryBean.getTitle())) {
	 		title = CategoryType.DIGITAL.getValue();
		}else if (CategoryType.HOUSEHOLD.toString().equals(categoryBean.getTitle())) {
			title = CategoryType.HOUSEHOLD.getValue();
		}
		 return categoryService.topList(categoryBean.getLevel(), title);		 				 
	 }
	
	
	 /**
     * 根据一级分类id取得所有二级分类
     * @param 
     * @return
     */
	 @Api(name = "category.listchild", version = "1.0")
	 @SignIgnore
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
    @SignIgnore
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
    	MemberAddress memberAddress = memberAddressService.selectOne(new EntityWrapper<MemberAddress>().eq("is_selected",1).eq("del_flag", 0).eq("member_id", member.getId()).eq("city_id", categoryAttrBean.getCityId()));
    	if(memberAddress==null) {
    		return "您暂未添加回收地址";
    	}
    	//根据分类Id查询父类分类id
    	Category category = categoryService.selectById(categoryId);
    	Integer communityId = memberAddress.getCommunityId();
    	String companyId = "";
    	String areaId = memberAddress.getAreaId()+"";
		//根据小区Id查询的私海所属企业
		Company company = companyCategoryService.selectCompany(category.getParentId(), communityId);
		if(company == null) {
			//根据分类Id和小区id去公海查询相关企业
			CompanyShare companyShare =	companyShareService.selectOne(new EntityWrapper<CompanyShare>().eq("category_id", category.getParentId()).eq("area_id", areaId));
			if(companyShare==null) {
				return "您地址所在区域暂无回收企业";
			}
			companyId = companyShare.getCompanyId().toString();
		}else {
			companyId = company.getId().toString();
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
    @SignIgnore
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
	public Object categoryOneList(){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("DIGITAL",categoryService.topList(0, 1));
		resultMap.put("HOUSEHOLD",categoryService.topList(0, 2));
		return resultMap;
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
	@SignIgnore
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object categoryHouseTwoList(CategoryBean categoryBean){
		return priceService.categoryHouseTwoList(categoryBean);

	}
	/**
	 * 小程序根据电器分类id取得所有分类属性
	 * @param
	 * @return
	 */
	@Api(name = "category.listCategoryAttrsXCX", version = "1.0")
	@SignIgnore
	@AuthIgnore
	public Object listCategoryAttrsXCX(CategoryBean categoryBean){
		return categoryAttrService.getCategoryAttrListss(categoryBean.getId());
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
			//所有的分类Id
			String categoryAttrOptionIds = categoryAttrBean.getCategoryAttrOptionids();
			System.out.println("预估价格的Ids ："+categoryAttrOptionIds);
			String tokenCyptoKey = ALI_API_TOKEN_CYPTO_KEY;
			String key = CipherTools.initKey(tokenCyptoKey);
			String decodeToken = CipherTools.decrypt(token, key);
			Claims claims = JwtUtils.getClaimByToken(decodeToken, ALI_API_TOKEN_SECRET_KEY);
			String memberId = claims.getSubject();
			//获取当前用户的默认地址
			MemberAddress memberAddress = memberAddressService.selectOne(new EntityWrapper<MemberAddress>().eq("is_selected",1).eq("del_flag", 0).eq("member_id", memberId).eq("city_id",categoryAttrBean.getCityId()));
			if(memberAddress==null){
				return "暂未添加回收地址";
			}
			//根据分类Id查询父类分类id
			Category category = categoryService.selectById(categoryId);
			Integer communityId = memberAddress.getCommunityId();
			String companyId = "";
			String areaId = memberAddress.getAreaId()+"";
			//根据小区Id查询的私海所属企业
			Company company = companyCategoryService.selectCompany(category.getParentId(), communityId);
			if(company == null) {
				//根据分类Id和小区id去公海查询相关企业
				CompanyShare companyShare =	companyShareService.selectOne(new EntityWrapper<CompanyShare>().eq("category_id", category.getParentId()).eq("area_id", areaId));
				if(companyShare==null) {
					return "所在区域暂无回收企业";
				}
				companyId = companyShare.getCompanyId().toString();
			}else {
				companyId = company.getId().toString();
			}
			//根据企业Id查询和分类Id查询对应的一条关联记录
			CompanyCategory companyCategory  = companyCategoryService.selectOne(new EntityWrapper<CompanyCategory>().eq("company_id",companyId).eq("category_id",categoryId));
			price = new BigDecimal(companyCategory.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
			//获取所有分类属性选项Id的集合
			String [] OptionIds = categoryAttrOptionIds.split(",");
			List<String> specialPriceList = new ArrayList<String>();
			for(int i=0;i<OptionIds.length;i++) {
				//分类属性选项Id和公司id查询相关价格数据
				AliCategoryAttrOptionBean categoryAttrOptionById = categoryAttrOptionService.getCategoryAttrOptionById(Integer.parseInt(OptionIds[i]), Integer.parseInt(companyId));
				String optionPrice = categoryAttrOptionById.getPrice().toString();
				//匹配价格里面是否存在特殊字符的价格
				String[] array = optionPrice.split("P");
				if((array.length-1)>0) {
					//如果存在特殊字符
					String specialPrice = optionPrice.substring(1);
					specialPriceList.add(specialPrice);
					continue;
				}
				BigDecimal prices = new BigDecimal(optionPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
				price = price.add(prices);
			}
			//将取到的特殊价格从小到大排序
			Collections.sort(specialPriceList);
			if(!specialPriceList.isEmpty()) {
				//取得最小的特殊价格
				price = new BigDecimal(specialPriceList.get(0));
			}
		}
		System.out.println("查出预估价格了了:"+price);
		BigDecimal newprice = new BigDecimal("0");
		if(newprice.compareTo(price)==1){
			return 10;
		}else{
			return price;
		}
	}
}
