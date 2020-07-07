package com.tzj.collect.api.business;

import com.taobao.api.ApiException;
import com.tzj.collect.core.result.business.BusinessCategoryResult;
import com.tzj.collect.common.util.BusinessUtils;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.param.business.CategoryBean;
import com.tzj.collect.core.param.business.ComIdAndCateOptIdBean;
import com.tzj.collect.core.result.business.CategoryResult;
import com.tzj.collect.core.service.CategoryService;
import com.tzj.collect.core.service.CompanyCategoryAttrOptionService;
import com.tzj.collect.core.service.CompanyCategoryService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Category.CategoryType;
import com.tzj.collect.entity.CompanyAccount;
import static com.tzj.collect.common.constant.TokenConst.BUSINESS_API_COMMON_AUTHORITY;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Administrator
 *
 */
@ApiService
public class BusinessCategoryApi {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CompanyCategoryService companyCategoryService;
    @Autowired
    private CompanyCategoryAttrOptionService attrOptionService;
    @Autowired
    private OrderService orderService;

    /**
     * 取得所有一级分类
     *
     * @param
     * @return
     */
    @Api(name = "business.category.toplist", version = "1.0")
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Map<String, List<Category>> getTopList() {
        Map<String, List<Category>> map = new HashMap<String, List<Category>>();
        CompanyAccount companyAccount = getCompanyAccount();
        int companyId = companyAccount.getCompanyId();
        List<Category> digitalList = categoryService.getTopList(companyId, CategoryType.DIGITAL.getValue());
        List<Category> houseHoldList = categoryService.getTopList(companyId, CategoryType.HOUSEHOLD.getValue());
        List<Category> bigList = categoryService.getTopList(companyId, CategoryType.BIGTHING.getValue());
        map.put("digitalList", digitalList);
        map.put("houseHoldList", houseHoldList);
        map.put("bigList", bigList);
        return map;
    }

    //大件一级分类接口
    @Api(name = "business.category.bigtoplist", version = "1.0")
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Map<String, List<Category>> getTopBigthingList() {
        Map<String, List<Category>> map = new HashMap<>();
        CompanyAccount companyAccount = getCompanyAccount();
        int companyId = companyAccount.getCompanyId();
        map.put("bigthingList", categoryService.getTopList(companyId, CategoryType.BIGTHING.getValue()));
        return map;
    }

    /**
     * 根据parentId查询家电数码二级菜单
     *
     * @param
     * @return
     */
    @Api(name = "business.category.digitalsecondlist", version = "1.0")
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public List<CategoryResult> getSecondList(CategoryBean categoryBean) {
//		String parentId = categoryBean.getParentId();
////		return categoryService.getSecondList(parentId);
        String parentId = categoryBean.getParentId();
        CompanyAccount companyAccount = getCompanyAccount();
        String companyId = companyAccount.getCompanyId().toString();
        return categoryService.getHouseHoldDetail(parentId, companyId, categoryBean.getCityId());
    }

    /**
     * 查询家电数码详情
     *
     * @param
     * @return
     */
    @Api(name = "business.category.digitaldetail", version = "1.0")
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Map<String, Object> getDigitalDetail(CategoryBean categoryBean) throws ApiException {
        CompanyAccount companyAccount = getCompanyAccount();
        if (companyAccount != null) {
            return categoryService.getDigitalDetail(categoryBean, companyAccount.getCompanyId().toString());
        } else {
            throw new ApiException("请先登录,再执行更新操作");
        }

        //return categoryAttrOptionService.getDigitName(categoryBean);
    }

    /**
     * 根据parentId查询生活垃圾品类、单价
     *
     * @param
     * @return
     */
    @Api(name = "business.category.householddetail", version = "1.0")
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public List<CategoryResult> getHouseHold(CategoryBean categoryBean) {
        String parentId = categoryBean.getParentId();
        CompanyAccount companyAccount = getCompanyAccount();
        String companyId = companyAccount.getCompanyId().toString();
        return categoryService.getHouseHoldDetail(parentId, companyId, categoryBean.getCityId());
    }

    /**
     * 根据parentId查询定点的生活垃圾品类、单价
     *
     * @param
     * @return
     */
    @Api(name = "business.category.houseHoldDetailLocale", version = "1.0")
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public List<CategoryResult> getHouseHoldDetailLocale(CategoryBean categoryBean) {
        String parentId = categoryBean.getParentId();
        CompanyAccount companyAccount = getCompanyAccount();
        String companyId = companyAccount.getCompanyId().toString();
        return categoryService.getHouseHoldDetailLocale(parentId, companyId, categoryBean.getCityId());
    }

    /**
     * 更新价格
     *
     * @param comIdAndCateOptIdBean
     * @return
     * @throws ApiException
     */
    @Api(name = "business.category.updateprice", version = "1.0")
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public boolean updatePrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean) throws Exception {
        CompanyAccount companyAccount = getCompanyAccount();
        if (companyAccount != null) {
            comIdAndCateOptIdBean.setCompanyId(companyAccount.getCompanyId().toString());
        } else {
            throw new ApiException("请先登录,再执行更新操作");
        }
        return categoryService.updatePrice(comIdAndCateOptIdBean);
    }

    /**
     * 更新定点分类的价格
     *
     * @param comIdAndCateOptIdBean
     * @return
     * @throws ApiException
     */
    @Api(name = "business.category.updateLocalePrice", version = "1.0")
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public boolean updateLocalePrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean) throws Exception {
        CompanyAccount companyAccount = getCompanyAccount();
        if (companyAccount != null) {
            comIdAndCateOptIdBean.setCompanyId(companyAccount.getCompanyId().toString());
        } else {
            throw new ApiException("请先登录,再执行更新操作");
        }
        return categoryService.updateLocalePrice(comIdAndCateOptIdBean);
    }

    public CompanyAccount getCompanyAccount() {

        // 接口里面获取 CompanyAccount 的例子
        CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
        return companyAccount;
    }

    /**
     * 根据公司id及分类属性选项id查找公司对	选项价格列表(家电数码)
     *
     * @param comIdAndCateOptIdBean
     * @return
     */
    @Api(name = "business.category.comcateoptprice", version = "1.0")
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public List<BusinessCategoryResult> selectComCateAttOptPrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean) {
        comIdAndCateOptIdBean.setCompanyId(this.getCompanyAccount().getCompanyId().toString());
        return companyCategoryService.selectComCateAttOptPrice(comIdAndCateOptIdBean);
    }

    @Api(name = "business.category.modifycomoptprice", version = "1.0")
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public boolean modifyComCateAttOptPrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean) throws Exception {
        CompanyAccount companyAccount = getCompanyAccount();
        if (companyAccount != null) {
            comIdAndCateOptIdBean.setCompanyId(companyAccount.getCompanyId().toString());
        } else {
            throw new ApiException("请先登录,再执行更新操作");
        }
        return attrOptionService.modifyComCateAttOptPrice(comIdAndCateOptIdBean);
    }

    /**
     * 提交价格集合
     *
     * @param
     * @return
     * @throws ApiException
     */
    @Api(name = "business.category.updatecomcateattroptprice", version = "1.0")
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public boolean updateComCateAttrOptPrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean) throws Exception {

        return attrOptionService.updateComCateAttOptPrice(comIdAndCateOptIdBean.getCategoryAttrOptionBeanList(), comIdAndCateOptIdBean.getCityId());
    }

    /**
     * 根据OrderId订单查询一级分类
     *
     * @author wangcan
     * @return
     */
    @Api(name = "business.category.getOneCategoryByOrder", version = "1.0")
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getOneCategoryByOrder(CategoryBean categoryBean) {
        return categoryService.getOneCategoryListByOrder(categoryBean.getOrderId());
    }

    /**
     * 根据一级分类Id和订单id查询二级分类
     *
     * @author wangcan
     * @return
     */
    @Api(name = "business.category.getTwoCategoryByOrder", version = "1.0")
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getTwoCategoryByOrder(CategoryBean categoryBean) {
        return categoryService.getTwoCategoryListByOrder(Integer.parseInt(categoryBean.getId()), categoryBean.getOrderId());
    }

    /**
     * 重新编辑重量
     *
     * @param orderBean
     * @return
     */
    @Api(name = "business.order.updateOrderAchItem", version = "1.0")
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public boolean updateOrderAchItem(OrderBean orderBean) {
        return orderService.updateOrderAchItem(orderBean);
    }

}
