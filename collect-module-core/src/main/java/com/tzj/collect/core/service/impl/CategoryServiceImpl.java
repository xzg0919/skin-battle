package com.tzj.collect.core.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.common.amap.AmapResult;
import com.tzj.collect.common.amap.AmapUtil;
import com.tzj.collect.common.mq.RocketMqConst;
import com.tzj.collect.common.notify.DingTalkNotify;
import com.tzj.collect.core.mapper.CategoryAttrOptionMapper;
import com.tzj.collect.core.mapper.CategoryMapper;
import com.tzj.collect.core.mapper.CompanyCategoryMapper;
import com.tzj.collect.core.param.ali.AliCategoryAttrOptionBean;
import com.tzj.collect.core.param.ali.CategoryAttrBean;
import com.tzj.collect.core.param.business.CategoryBean;
import com.tzj.collect.core.param.business.ComIdAndCateOptIdBean;
import com.tzj.collect.core.param.xianyu.XyCategory;
import com.tzj.collect.core.result.ali.ClassifyAndMoney;
import com.tzj.collect.core.result.ali.ComCatePrice;
import com.tzj.collect.core.result.business.BusinessCategoryResult;
import com.tzj.collect.core.result.business.CategoryResult;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.collect.entity.Category.CategoryType;
import com.tzj.module.common.utils.StringUtils;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CompanyCategoryMapper companyCategoryMapper;
    @Autowired
    private CompanyCategoryService companyCategoryService;
    @Autowired
    private CategoryAttrOptionMapper categoryAttrOptionMapper;
    @Autowired
    private CompanyCategoryAttrOptionCityService companyCategoryAttrOptionCityService;
    @Autowired
    private CompanyCategoryCityService companyCategoryCityService;
    @Autowired
    private MemberAddressService memberAddressService;
    @Autowired
    private CompanyStreetApplianceService companyStreetApplianceService;
    @Autowired
    private CategoryAttrOptionService categoryAttrOptionService;
    @Autowired
    private CompanyStreetBigService companyStreetBigService;
    @Autowired
    private CompanyCategoryCityLocaleService companyCategoryCityLocaleService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private CompanyRecyclerService companyRecyclerService;
    @Autowired
    private CompanyStreetHouseService companyStreetHouseService;
    @Autowired
    private CompanyStreeService companyStreeService;
    @Autowired
    private CompanyCategoryCityNameService companyCategoryCityNameService;
    @Autowired
    private OrderItemAchService orderItemAchService;
    @Autowired
    private  CompanyCityRatioService companyCityRatioService;

    /**
     * 获取一级类的商品
     */
    @Override
    public List<Category> topList(int level, Serializable title, String isFiveKg) {
        EntityWrapper<Category> wraper = new EntityWrapper<Category>();
        wraper.eq("level_", level);
        wraper.eq("title", title);
        wraper.eq("unuseful", "0");
        wraper.orderBy("create_date", false);
        if ("Y".equals(isFiveKg)) {
        }
        return this.selectList(wraper);
    }

    /**
     * 获取一级类的商品
     */
    @Override
    public List<Category> topListApp(int level, Serializable title, Long recId) {
//		EntityWrapper<Category> wraper = new EntityWrapper<Category>();
//		wraper.eq("level_", level);
//		wraper.eq("title", title);
//		return this.selectList(wraper);
        System.out.println(recId);
        return categoryMapper.topListApp(level, (int) title, recId);
    }

    /**
     * 获取一级类的商品
     */
    @Override
    public List<Category> topListApps(int level, Serializable title) {
//		EntityWrapper<Category> wraper = new EntityWrapper<Category>();
//		wraper.eq("level_", level);
//		wraper.eq("title", title);
//		return this.selectList(wraper);
        return categoryMapper.topListApps(level, (int) title);
    }

    @Override
    public List<Category> childList(Integer id) {
        EntityWrapper<Category> wraper = new EntityWrapper<Category>();
        wraper.eq("parent_id", id);
        return this.selectList(wraper);
    }

    /**
     * 根据分类Id（主键）查询分类信息
     *
     * @author 王灿
     * @param categoryId : 分类Id
     * @return Category : 分类信息
     */
    @Override
    @DS("slave")
    public Category getCategoryById(long categoryId) {
        EntityWrapper<Category> wrapper = new EntityWrapper<Category>();
        wrapper.eq("id", categoryId);
        wrapper.eq("del_flag", "0");
        return this.selectOne(wrapper);
    }

    /**
     * 根据分类id查询对应的分类信息和对应的分类属性条数
     *
     * @author 王灿
     * @param categoryId : 分类Id
     */
    @Override
    @DS("slave")
    public Category selectListCategory(String categoryId) {
        return categoryMapper.selectListCategory(categoryId);
    }

    /**
     * 根据分类Id删除分类
     *
     * @author: 王灿
     * @param categoryId : 分类Id
     * @return TokenBean 返回类型
     */
    @Transactional
    @Override
    public boolean deleteByCategoryId(String categoryId) {
        Category category = this.selectById(categoryId);
        category.setDelFlag("1");
        return this.updateById(category);
    }

    /**
     * 回收物明细
     *
     * @author 王灿
     * @param companyId:企业Id
     * @return
     *
     */
    @Override
    public List<CategoryResult> getCategoryData(String companyId) {
        List<Category> categoryChildList = null;
        Set<String> cateChildName = null;
        List<CategoryResult> list = companyCategoryMapper.getCategoryData(companyId);
        if (!list.isEmpty()) {
            for (CategoryResult categoryResult : list) {
                cateChildName = new HashSet<>();
                //根据父类id获取categoryChildList
                categoryChildList = this.childList(Integer.parseInt(categoryResult.getId()));
                for (Category categoryChild : categoryChildList) {
                    cateChildName.add(categoryChild.getName());
                }
                categoryResult.setChildName(cateChildName.toString().replace("]", "").replace("[", "").replace(",", "/").replace(" ", ""));
            }
        }
        return list;
    }

    /**
     * 计算价格
     */
    @Override
    public ClassifyAndMoney reckon(CategoryAttrBean categoryAttrBean) {
        //分类Id
        long categoryId = categoryAttrBean.getCategoryId();
        //根据分类Id查询分类信息
        Category category = this.getCategoryById(categoryId);
        Category parent = this.getCategoryById(category.getParentId());
        BigDecimal price = category.getPrice().setScale(2, BigDecimal.ROUND_DOWN);
        ClassifyAndMoney classifyAndMoney = new ClassifyAndMoney();
        Integer weight = categoryAttrBean.getWeight();
        if (weight > 0) {
            classifyAndMoney.setMoney(price.multiply(new BigDecimal(categoryAttrBean.getWeight())));
        }
        StringBuilder builder = new StringBuilder();
        builder.append(parent.getName());
        builder.append("-");
        builder.append(category.getName());
        classifyAndMoney.setClassify(builder.toString());
        return classifyAndMoney;
    }

    /**
     * 根据companyid查询一级菜单
     *
     * @param companyId
     * @return
     */
    @Override
    @DS("slave")
    public List<Category> getTopList(int companyId, Serializable title) {
        return categoryMapper.getTopList(companyId, title);
    }

    /**
     * 根据parentId查询二级菜单
     *
     * @param parentId
     * @return
     */
    @Override
    @DS("slave")
    public List<Category> getSecondList(String parentId) {
        return categoryMapper.getSecondList(parentId);
    }

    /**
     * 根据parentId查询生活垃圾品类、单价
     *
     * @return
     */
    @Override
    @DS("slave")
    public List<CategoryResult> getHouseHoldDetail(String parentId, String companyId, String cityId) {
        List<CategoryResult> categoryResultList = null;
        categoryResultList = companyCategoryCityService.getCityHouseHoldDetail(parentId, companyId, cityId);
//		if(categoryResultList.isEmpty()){
//			categoryResultList = categoryMapper.getHouseHoldDetail(parentId,companyId);
//		}
        return categoryResultList;
    }

    /**
     * 根据parentId查询生活垃圾品类、单价
     *
     * @return
     */
    @Override
    @DS("slave")
    public List<CategoryResult> getHouseHoldDetailLocale(String parentId, String companyId, String cityId) {
        List<CategoryResult> categoryResultList = null;
        categoryResultList = companyCategoryCityLocaleService.getHouseHoldDetailLocale(parentId, companyId, cityId);
//		if(categoryResultList.isEmpty()){
//			categoryResultList = categoryMapper.getHouseHoldDetail(parentId,companyId);
//		}
        return categoryResultList;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean updatePrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean) throws Exception {
        if (comIdAndCateOptIdBean.getTitle().equals(CategoryType.HOUSEHOLD.name()) || comIdAndCateOptIdBean.getTitle().equals(CategoryType.DIGITAL.name()) || comIdAndCateOptIdBean.getTitle().equals(CategoryType.BIGTHING.name())) {
            List<CategoryBean> priceList = comIdAndCateOptIdBean.getHouseholdPriceList();
            for (CategoryBean categoryBean : priceList) {
                Category category = this.selectById(categoryBean.getId());
                if (category == null) {
                    throw new ApiException("传入的分类Id不存在：" + categoryBean.getId());
                }
                CompanyCategoryCity companyCategoryCity = companyCategoryCityService.selectOne(new EntityWrapper<CompanyCategoryCity>().eq("city_id", comIdAndCateOptIdBean.getCityId()).eq("company_id", comIdAndCateOptIdBean.getCompanyId()).eq("category_id", categoryBean.getId()).eq("del_flag", 0));
                if (companyCategoryCity != null) {
                    companyCategoryCity.setPrice(new BigDecimal(categoryBean.getPrice()));
                    companyCategoryCity.setUpdateDate(new Date());
                    companyCategoryCity.setCityId(comIdAndCateOptIdBean.getCityId());
                    companyCategoryCity.setUpdateBy(comIdAndCateOptIdBean.getCompanyId());
                    //companyCategoryMapper.updateAllColumnById(companyCategory);
                    companyCategoryCityService.updateById(companyCategoryCity);
                } else {
                    companyCategoryCity = new CompanyCategoryCity();
                    if (category != null) {
                        companyCategoryCity.setParentId(category.getParentId());
                        companyCategoryCity.setParentIds(category.getParentIds());
                        companyCategoryCity.setCategoryId(Integer.parseInt(categoryBean.getId()));
                        companyCategoryCity.setCompanyId(comIdAndCateOptIdBean.getCompanyId());
                        companyCategoryCity.setPrice(new BigDecimal(categoryBean.getPrice()));
                        //暂时将单位放进去（之后更新弃置todoing）
                        companyCategoryCity.setUnit("kg");
                        companyCategoryCity.setCityId(comIdAndCateOptIdBean.getCityId());
                        companyCategoryCity.setCreateBy(comIdAndCateOptIdBean.getCompanyId());
                    }
                    companyCategoryCityService.insert(companyCategoryCity);
                    //companyCategoryMapper.insert(companyCategory);
                }
            }
            return true;
        } else {
            try {
                throw new ApiException("修改的总分类不存在，请重新输入");
            } catch (ApiException e) {
                e.getCause();
            }
        }
        return false;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean updateLocalePrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean) throws Exception {
        List<CategoryBean> priceList = comIdAndCateOptIdBean.getHouseholdPriceList();
        for (CategoryBean categoryBean : priceList) {
            Category category = this.selectById(categoryBean.getId());
            if (category == null) {
                throw new ApiException("传入的分类Id不存在：" + categoryBean.getId());
            }
            CompanyCategoryCityLocale companyCategoryCityLocale = companyCategoryCityLocaleService.selectOne(new EntityWrapper<CompanyCategoryCityLocale>().eq("city_id", comIdAndCateOptIdBean.getCityId()).eq("company_id", comIdAndCateOptIdBean.getCompanyId()).eq("category_id", categoryBean.getId()).eq("del_flag", 0));
            if (companyCategoryCityLocale != null) {
                companyCategoryCityLocale.setPrice(new BigDecimal(categoryBean.getPrice()));
                companyCategoryCityLocale.setUpdateDate(new Date());
                companyCategoryCityLocale.setCityId(comIdAndCateOptIdBean.getCityId());
                companyCategoryCityLocale.setUpdateBy(comIdAndCateOptIdBean.getCompanyId());
                //companyCategoryMapper.updateAllColumnById(companyCategory);
                companyCategoryCityLocaleService.updateById(companyCategoryCityLocale);
            } else {
                companyCategoryCityLocale = new CompanyCategoryCityLocale();
                if (category != null) {
                    companyCategoryCityLocale.setParentId(category.getParentId());
                    companyCategoryCityLocale.setParentIds(category.getParentIds());
                    companyCategoryCityLocale.setCategoryId(Integer.parseInt(categoryBean.getId()));
                    companyCategoryCityLocale.setCompanyId(comIdAndCateOptIdBean.getCompanyId());
                    companyCategoryCityLocale.setPrice(new BigDecimal(categoryBean.getPrice()));
                    //暂时将单位放进去（之后更新弃置todoing）
                    companyCategoryCityLocale.setUnit("kg");
                    companyCategoryCityLocale.setCityId(comIdAndCateOptIdBean.getCityId());
                    companyCategoryCityLocale.setCreateBy(comIdAndCateOptIdBean.getCompanyId());
                }
                companyCategoryCityLocaleService.insert(companyCategoryCityLocale);
                //companyCategoryMapper.insert(companyCategory);
            }
        }
        return true;
    }

    @Override
    @DS("slave")
    public Map<String, Object> getDigitalDetail(CategoryBean categoryBean, String companyId) {
        Map<String, Object> returnMap = new HashMap<>();
        List<CategoryAttr> attrOptions = categoryAttrOptionMapper.getDigitNameRePlace(Integer.parseInt(categoryBean.getId()));
        ComIdAndCateOptIdBean comIdAndCateOptIdBean = null;
        List<BusinessCategoryResult> businessCategoryResults = null;
        if (!attrOptions.isEmpty()) {
            for (CategoryAttr categoryAttr : attrOptions) {
                comIdAndCateOptIdBean = new ComIdAndCateOptIdBean();
                comIdAndCateOptIdBean.setCateOptId(categoryAttr.getId().toString());
                comIdAndCateOptIdBean.setCompanyId(companyId);
                businessCategoryResults = companyCategoryAttrOptionCityService.selectComCityCateAttOptPrice(categoryBean.getCityId(), companyId, categoryAttr.getId().toString());
                businessCategoryResults = getBusinessCategoryResult(businessCategoryResults);
                if (businessCategoryResults.isEmpty()) {
                    businessCategoryResults = companyCategoryService.selectComCateAttOptPrice(comIdAndCateOptIdBean);
                }
                categoryAttr.setResultList(businessCategoryResults);
            }
        }
        returnMap.put("List", attrOptions);
        //获取当前公司回收价格
        CompanyCategoryCity companyCategoryCity = companyCategoryCityService.selectOne(new EntityWrapper<CompanyCategoryCity>().eq("company_id", comIdAndCateOptIdBean.getCompanyId()).eq("category_id", categoryBean.getId()).eq("del_flag", "0").eq("city_id", categoryBean.getCityId()));
        if (companyCategoryCity == null) {
            CompanyCategory attrOption = companyCategoryService.selectOne(new EntityWrapper<CompanyCategory>().eq("company_id", comIdAndCateOptIdBean.getCompanyId()).eq("category_id", categoryBean.getId()).eq("del_flag", "0"));
            if (null != attrOption) {
                returnMap.put("Price", attrOption.getPrice());
            } else {
                returnMap.put("Price", BigDecimal.ZERO);
            }
        } else {
            returnMap.put("Price", companyCategoryCity.getPrice());
        }
        return returnMap;
    }

    public List<BusinessCategoryResult> getBusinessCategoryResult(List<BusinessCategoryResult> businessCategoryResultsList) {
        String price = null;
        for (BusinessCategoryResult businessCategoryResult : businessCategoryResultsList) {
            if (businessCategoryResult.getComOptPrice() != null) {
                price = businessCategoryResult.getComOptPrice();
            }
            if (price != null) {
                if (new BigDecimal(price).compareTo(BigDecimal.ZERO) >= 0) {
                    businessCategoryResult.setComOptAddPrice(price);
                } else {
                    businessCategoryResult.setComOptDecprice(new BigDecimal(price).abs().toString());
                }
            }
        }
        return businessCategoryResultsList;
    }

    @Override
    public List<Map<String, Object>> getIsOpenCategory(String companyId) {
        return categoryMapper.getIsOpenCategory(companyId);
    }

    @Override
    public BigDecimal getPrice(String aliUserId, long categoryId, String type, String categoryAttrOptionIds) {
        //所有的分类Id
        System.out.println("预估价格的Ids ：" + categoryAttrOptionIds);
        BigDecimal price = null;
        //获取当前用户的默认地址
        MemberAddress memberAddress = memberAddressService.getMemberAdderssByAliUserId(aliUserId);
        System.out.println("memberAddressId 是：" + memberAddress.getId() + "分类Id：" + categoryId + "预估价格的Ids ：" + categoryAttrOptionIds);
        if (memberAddress == null) {
            throw new com.tzj.module.easyopen.exception.ApiException("暂未添加回收地址");
        }
        //根据分类Id查询父类分类id
        Category category = this.selectById(categoryId);
        String companyId = "";
        if ("DIGITAL".equals(type)) {
            //根据小区Id，分类id和街道id 查询相关企业
            companyId = companyStreetApplianceService.selectStreetApplianceCompanyIdByCategoryId(category.getParentId(), memberAddress.getStreetId(), memberAddress.getCommunityId());
            if (StringUtils.isBlank(companyId)) {
                throw new com.tzj.module.easyopen.exception.ApiException("该区域暂无服务: memberAddressId:" + memberAddress.getId() + "---分类Id：" + category.getParentId() + "--------街道Id：" + memberAddress.getStreetId() + "-----小区Id： " + memberAddress.getCommunityId());
            }
        } else if ("BIGTHING".equals(type)) {
            Integer streetBigCompanyId = companyStreetBigService.selectStreetBigCompanyIdByCategoryId(category.getParentId(), memberAddress.getStreetId());
            if (null == streetBigCompanyId) {
                throw new com.tzj.module.easyopen.exception.ApiException("该区域暂无服务");
            }
            companyId = streetBigCompanyId + "";
        }
        //根据企业Id查询和分类Id查询对应的一条关联记录
        CompanyCategoryCity companyCategoryCity = companyCategoryCityService.selectOne(new EntityWrapper<CompanyCategoryCity>().eq("company_id", companyId).eq("category_id", categoryId).eq("city_id", memberAddress.getCityId()).eq("del_flag", 0));
        if (null == companyCategoryCity) {
            CompanyCategory companyCategory = companyCategoryService.selectOne(new EntityWrapper<CompanyCategory>().eq("company_id", companyId).eq("category_id", categoryId));
            price = new BigDecimal(companyCategory.getPrice()).setScale(2, BigDecimal.ROUND_DOWN);
        } else {
            price = companyCategoryCity.getPrice().setScale(2, BigDecimal.ROUND_DOWN);
        }
        //获取所有分类属性选项Id的集合
        String[] OptionIds = categoryAttrOptionIds.split(",");
        List<String> specialPriceList = new ArrayList<String>();
        AliCategoryAttrOptionBean categoryAttrOptionBean = null;
        for (int i = 0; i < OptionIds.length; i++) {
            //分类属性选项Id和公司id和公司Id查询相关价格数据
            categoryAttrOptionBean = companyCategoryAttrOptionCityService.getCategoryAttrOptionByCityId(OptionIds[i], companyId, memberAddress.getCityId().toString());
            if (null == categoryAttrOptionBean) {
                //分类属性选项Id和公司id查询相关价格数据
                categoryAttrOptionBean = categoryAttrOptionService.getCategoryAttrOptionById(Integer.parseInt(OptionIds[i]), Integer.parseInt(companyId));
            }
            String optionPrice = categoryAttrOptionBean.getPrice().toString();
            //匹配价格里面是否存在特殊字符的价格
            String[] array = optionPrice.split("P");
            if ((array.length - 1) > 0) {
                //如果存在特殊字符
                String specialPrice = optionPrice.substring(1);
                specialPriceList.add(specialPrice);
                continue;
            }
            BigDecimal prices = new BigDecimal(optionPrice).setScale(2, BigDecimal.ROUND_DOWN);
            price = price.add(prices);
        }
        //将取到的特殊价格从小到大排序
        Collections.sort(specialPriceList);
        if (!specialPriceList.isEmpty()) {
            //取得最小的特殊价格
            price = new BigDecimal(specialPriceList.get(0));
        }
        System.out.println("查出预估价格了了:" + price);
        BigDecimal newprice = new BigDecimal("0");
        if (categoryId < 25) {
            if (newprice.compareTo(price) == 1) {
                DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName(),
                        Thread.currentThread().getStackTrace()[1].getMethodName(), "更新会员卡积分失败",
                        RocketMqConst.DINGDING_ERROR, "价格是：" + price + "-----ids: " + categoryAttrOptionIds + "---地址： " + memberAddress.getCityName() + memberAddress.getAreaName() + memberAddress.getStreetName() + memberAddress.getAddress());
                return new BigDecimal("11");
            }
        }
        return price;
    }
    @Override
    @Transactional
    public BigDecimal getPricesAll(String aliUserId, long categoryId, String type, String categoryAttrOptionIds){
        //所有的分类Id
        System.out.println("进入新的计算价格接口预估价格的Ids ：" + categoryAttrOptionIds);
        //获取当前用户的默认地址
        MemberAddress memberAddress = memberAddressService.getMemberAdderssByAliUserId(aliUserId);
        System.out.println("memberAddressId 是：" + memberAddress.getId() + "分类Id：" + categoryId + "预估价格的Ids ：" + categoryAttrOptionIds);
        if (memberAddress == null) {
            throw new com.tzj.module.easyopen.exception.ApiException("暂未添加回收地址");
        }
        //根据分类Id查询父类分类id
        Category category = this.selectById(categoryId);
        String companyId = "";
        if ("DIGITAL".equals(type)) {
            //根据小区Id，分类id和街道id 查询相关企业
            companyId = companyStreetApplianceService.selectStreetApplianceCompanyIdByCategoryId(category.getParentId(), memberAddress.getStreetId(), memberAddress.getCommunityId());
            if (StringUtils.isBlank(companyId)) {
                throw new com.tzj.module.easyopen.exception.ApiException("该区域暂无服务: memberAddressId:" + memberAddress.getId() + "---分类Id：" + category.getParentId() + "--------街道Id：" + memberAddress.getStreetId() + "-----小区Id： " + memberAddress.getCommunityId());
            }
        } else if ("BIGTHING".equals(type)) {
            Integer streetBigCompanyId = companyStreetBigService.selectStreetBigCompanyIdByCategoryId(category.getParentId(), memberAddress.getStreetId());
            if (null == streetBigCompanyId) {
                throw new com.tzj.module.easyopen.exception.ApiException("该区域暂无服务");
            }
            companyId = streetBigCompanyId + "";
        }
        final BigDecimal[] price = {categoryMapper.selectCategoryPriceByCityCompanyId(categoryId, memberAddress.getCityId(), companyId)};
        System.out.println("新的计算价格接口基准价格："+price[0]);
        //获取所有分类属性选项Id的集合
        String[] OptionIds = categoryAttrOptionIds.split(",");
        List<BigDecimal> specialPriceList = new ArrayList<>();
        String finalCompanyId = companyId;
        Arrays.stream(OptionIds).forEach(optionId -> {
            CompanyCategoryAttrOptionCity companyCategoryAttrOptionCity = companyCategoryAttrOptionCityService.getCategoryAttrOptionByCityCompanyId(optionId, memberAddress.getCityId().toString(), finalCompanyId);
            if ("DIGITAL".equals(type)) {
                if ("1".equals(companyCategoryAttrOptionCity.getIsSpecial())){
                    specialPriceList.add(companyCategoryAttrOptionCity.getSpecialPrice());
                }
                price[0] = price[0].add(companyCategoryAttrOptionCity.getAttrOptionPrice());
            } else if ("BIGTHING".equals(type)) {
                price[0] = price[0].multiply(companyCategoryAttrOptionCity.getAttrOptionPrice());
            }
        });
        //将取到的特殊价格从小到大排序
        Collections.sort(specialPriceList);
        if (!specialPriceList.isEmpty()) {
            //取得最小的特殊价格
            return specialPriceList.get(0).setScale(2, BigDecimal.ROUND_DOWN);
        }
        BigDecimal cityRatio = companyCityRatioService.getCityRatioByCompanyCityId(memberAddress.getCityId(), finalCompanyId);
        price[0] = price[0].multiply(cityRatio);
        if ("BIGTHING".equals(type)&&price[0].compareTo(new BigDecimal(98)) == -1){
            return new BigDecimal(98);
        }
        System.out.println("计算后的价格是："+price[0]);
        return price[0].setScale(2, BigDecimal.ROUND_DOWN);
    }

    @Override
    public Object getNoCashOneCategoryList(String orderId) {
        Order order = orderService.selectById(orderId);
        Area area = areaService.selectById(order.getAreaId());
        List<Category> categoryList = companyCategoryCityNameService.getOneCategoryList(order.getCompanyId(), area.getParentId(), "1");
        return categoryList;
    }

    @Override
    public Object getNoCashTwoCategoryList(Integer categoryId, String orderId) {
        Order order = orderService.selectById(orderId);
        Area area = areaService.selectById(order.getAreaId());
        List<Category> categoryList = companyCategoryCityNameService.getTwoCategoryList(null,categoryId, order.getCompanyId(), area.getParentId(), "1");
        categoryList.stream().forEach(category -> {
            category.setPrice(new BigDecimal("0"));
        });
        return categoryList;
    }

    @Override
    public Object getOneCategoryListByOrder(String orderId) {
        Order order = orderService.selectById(orderId);
        Area area = areaService.selectById(order.getAreaId());
        List<Category> categoryList = companyCategoryCityNameService.getOneCategoryList(order.getCompanyId(), area.getParentId(), "0");
        //List<Category> categoryList = companyCategoryCityService.getOneCategoryListByOrder(order.getCompanyId(),area.getParentId());
        return categoryList;
    }

    @Override
    public Object getTwoCategoryListByOrder(Integer categoryId, String orderId) {
        Order order = orderService.selectById(Long.parseLong(orderId));
        Area area = areaService.selectById(order.getAreaId());
        List<Category> categoryList = companyCategoryCityNameService.getTwoCategoryList(orderId, categoryId, order.getCompanyId(), area.getParentId(), "0");
		if ("1".equals(order.getIsCash())){
			categoryList.stream().forEach(category -> {
				category.setPrice(new BigDecimal("0"));
			});
		}
//		List<ComCatePrice> priceList = null;
//		priceList = companyCategoryCityService.getOwnnerPriceAppByCity(categoryId.toString(),order.getCompanyId().toString(),area.getParentId().toString());
//		if (null==priceList||priceList.isEmpty()) {
//			com.tzj.collect.core.param.ali.CategoryBean categoryBean = new com.tzj.collect.core.param.ali.CategoryBean();
//			categoryBean.setId(categoryId);
//			priceList = companyCategoryService.getOwnnerPriceApps(categoryBean, order.getCompanyId());
//		}
        return categoryList;
    }

    @Override
    public Object getOneCategoryListLocale(String location, Long recyclerId) {
        Integer cityId = this.getCityId(location);
        Integer companyId = null;
        List<CompanyRecycler> companyRecyclerList = companyRecyclerService.selectList(new EntityWrapper<CompanyRecycler>().eq("status_", "1").eq("recycler_id", recyclerId).eq("type_", "1"));
        if (companyRecyclerList.isEmpty()) {
            return "您暂未添加回收公司";
        }
        companyId = companyRecyclerList.get(0).getCompanyId();
        List<Category> categoryList = companyCategoryCityNameService.getOneCategoryList(companyId, cityId, "0");
        //List<Category> oneCategoryListLocale = companyCategoryCityLocaleService.getOneCategoryListLocale(companyId, cityId);
        return categoryList;
    }

    @Override
    public Object getTwoCategoryListLocale(String location, Integer categoryId, Long recyclerId) {
        Integer cityId = this.getCityId(location);
        Integer companyId = null;
        List<CompanyRecycler> companyRecyclerList = companyRecyclerService.selectList(new EntityWrapper<CompanyRecycler>().eq("status_", "1").eq("recycler_id", recyclerId).eq("type_", "1"));
        if (companyRecyclerList.isEmpty()) {
            return "您暂未添加回收公司";
        }
        companyId = companyRecyclerList.get(0).getCompanyId();
        List<Category> categoryList = companyCategoryCityNameService.getTwoCategoryListLocal(categoryId, companyId, cityId, "0");
        List<ComCatePrice> priceList = null;
//		priceList = companyCategoryCityLocaleService.getTwoCategoryListLocale(categoryId.toString(),companyId+"",cityId+"");
//		if (priceList.isEmpty()) {
//			com.tzj.collect.core.param.ali.CategoryBean categoryBean = new com.tzj.collect.core.param.ali.CategoryBean();
//			categoryBean.setId(categoryId);
//			priceList = companyCategoryService.getOwnnerPriceApps(categoryBean, companyId);
//		}
        return categoryList;
    }

    public Integer getCityId(String location) {
        Integer cityId = null;
        try {
            AmapResult amap = AmapUtil.getAmap(location);
            if (null != amap) {
                Area street = areaService.selectOne(new EntityWrapper<Area>().eq("code_", amap.getTowncode()));
                if (null != street) {
                    Area area = areaService.selectById(street.getParentId());
                    if (null != area) {
                        cityId = area.getParentId();
                    }
                } else {
                    Area city = areaService.selectOne(new EntityWrapper<Area>().eq("area_name", amap.getCity()));
                    if (null != city) {
                        cityId = city.getId().intValue();
                    }
                }
            }
        } catch (Exception e) {
        }
        return cityId;
    }
    @Override
    public Object getCategoryNewHouseList() {
        Map<String, Object> resultMap = new HashMap<>();
        List<Category> categoryList = this.selectList(new EntityWrapper<Category>().eq("level_", "0").eq("title", "2").eq("unuseful", "0"));
        categoryList.stream().forEach(category -> {
            List<Category> categoryList1 = this.selectList(new EntityWrapper<Category>().eq("parent_id", category.getId()));
            category.setCategoryList(categoryList1);
        });
        categoryList = categoryList.stream().sorted(Comparator.comparing(Category::getCode)).collect(Collectors.toList());
        resultMap.put("categoryList", categoryList);
        return resultMap;
    }
    @Override
    public Object getCategoryNewHouseListByToken(String aliUserId) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Category> categoryList = null;
        MemberAddress memberAdderss = memberAddressService.getMemberAdderssByAliUserId(aliUserId);
        if (null != memberAdderss) {
            String houseceCompanyId = companyStreetHouseService.selectStreetHouseceCompanyId(memberAdderss.getStreetId(), memberAdderss.getCommunityId());
            if (StringUtils.isNotBlank(houseceCompanyId)) {
                categoryList = companyCategoryCityNameService.getHouseCategoryByCompanyId(Integer.parseInt(houseceCompanyId), memberAdderss.getCityId());
            } else {
                Integer fiveCompanyId = companyStreeService.selectStreeCompanyIds(45, memberAdderss.getStreetId());
                if (null != fiveCompanyId) {
                    categoryList = companyCategoryCityNameService.getFiveCategoryByCompanyId(fiveCompanyId, memberAdderss.getCityId());
                    categoryList = categoryList.stream().filter(category -> category.getId() == 45).collect(Collectors.toList());
                    List<Category> categoryLists = this.selectList(new EntityWrapper<Category>().eq("level_", "0").eq("title", "2").eq("unuseful", "0"));
                    categoryLists = categoryLists.stream().filter(category -> category.getId() != 45).collect(Collectors.toList());
                    categoryList.addAll(categoryLists);
                }
            }
        }
        if (null == categoryList) {
            categoryList = this.selectList(new EntityWrapper<Category>().eq("level_", "0").eq("title", "2").eq("unuseful", "0"));
            categoryList.stream().forEach(category -> {
                List<Category> categoryList1 = this.selectList(new EntityWrapper<Category>().eq("parent_id", category.getId()));
                category.setCategoryList(categoryList1);
            });
        }
        categoryList = categoryList.stream().sorted(Comparator.comparing(Category::getCode)).collect(Collectors.toList());
        resultMap.put("categoryList", categoryList);
        return resultMap;
    }
    @Override
    public Category selectByXyItemType(String xyItemType){
        return this.selectOne(new EntityWrapper<Category>().eq("xy_item_type",xyItemType));
    }
    @Override
    public List<Map<String,Object>> selectXyList(){
        return categoryMapper.selectXyList();
    }

    @Override
    public Integer selectApplianceByCompany(Integer companyId) {
        return baseMapper.selectCategoryByCompany(companyId,"1");
    }

    @Override
    public Integer selectHouseByCompany(Integer companyId) {
        return baseMapper.selectCategoryByCompany(companyId,"2");
    }

    @Override
    public Integer selectBigByCompany(Integer companyId) {
        return baseMapper.selectCategoryByCompany(companyId,"4");
    }
}
