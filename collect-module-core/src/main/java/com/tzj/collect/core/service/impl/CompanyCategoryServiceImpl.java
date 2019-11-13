package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.result.business.BusinessCategoryResult;
import com.tzj.collect.core.mapper.CompanyCategoryMapper;
import com.tzj.collect.core.param.ali.CategoryBean;
import com.tzj.collect.core.param.business.ComIdAndCateOptIdBean;
import com.tzj.collect.core.result.ali.ComCatePrice;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.module.common.utils.StringUtils;
import com.tzj.module.easyopen.exception.ApiException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 回收企业关联ServiceImpl
 *
 * @Author 王灿
 *
 */
@Service
@Transactional(readOnly = true)
public class CompanyCategoryServiceImpl extends ServiceImpl<CompanyCategoryMapper, CompanyCategory> implements CompanyCategoryService {

    @Override
    public int selectCategoryByCompanyId(long id) {

        return this.selectCategoryByCompanyId(id);
    }
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CompanyCategoryMapper comCateMapper;
    @Autowired
    private CompanyServiceService companyServiceService;
    @Autowired
    private CompanyCategoryMapper companyCategoryMapper;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private CompanyStreeService companyStreeService;
    @Autowired
    private CompanyStreetBigService companyStreetBigService;
    @Autowired
    private CompanyCategoryCityService companyCategoryCityService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private CompanyStreetHouseService companyStreetHouseService;
    @Autowired
    private MemberAddressService memberAddressService;
    @Autowired
    private CompanyStreetApplianceService companyStreetApplianceService;
    @Autowired
    private CompanyCategoryCityNameService companyCategoryCityNameService;

    public Map<String, Object> categoryOneListToken(String aliUserId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Category> DIGITAL = null;
        List<Category> BIGTHING = null;
        MemberAddress memberAddress = memberAddressService.getMemberAdderssByAliUserId(aliUserId);
        if (memberAddress != null) {
            Integer cityId = memberAddress.getCityId();
            String appliceCompanyId = companyStreetApplianceService.selectStreetApplianceCompanyId(memberAddress.getStreetId(), memberAddress.getCommunityId());
            Integer bigCompanyId = companyStreetBigService.selectStreetBigCompanyId(memberAddress.getStreetId());
            if (StringUtils.isNotBlank(appliceCompanyId)) {
                DIGITAL = companyCategoryCityNameService.getAppliceCategoryByCompanyId(Integer.parseInt(appliceCompanyId), cityId);
            }
            if (null != bigCompanyId) {
                BIGTHING = companyCategoryCityNameService.getBigCategoryByCompanyId(bigCompanyId, cityId);
            }
        }
        if (null == DIGITAL) {
            DIGITAL = categoryService.topList(0, 1, null);
        }
        if (null == BIGTHING) {
            BIGTHING = categoryService.topList(0, 4, null);
        }
        resultMap.put("DIGITAL", DIGITAL);
        resultMap.put("BIGTHING", BIGTHING);
        return resultMap;
    }

    /**
     * 小程序取得所有二级分类
     *
     * @param
     * @return
     */
    @Override
    public Map<String, Object> categoryTwoList(CategoryBean categoryBean) {
        Map<String, Object> map = new HashMap<>();
        List<ComCatePrice> priceList = null;
        List<ComCatePrice> noPriceList = null;
        priceList = this.getAvgPrice(categoryBean);
        noPriceList = this.getAvgNoPrice(categoryBean);
        map.put("ComCatePriceList", priceList);
        map.put("ComCateNoPriceList", noPriceList);
        map.put("category", categoryService.selectById(categoryBean.getId()));
        return map;
    }

    /**
     * 小程序取得所有二级分类
     *
     * @param
     * @return
     */
    @Override
    public Map<String, Object> categoryHouseTwoList(CategoryBean categoryBean, String aliUserId) {
        Category category = categoryService.selectById(categoryBean.getId());
        Map<String, Object> map = new HashMap<>();
        List<ComCatePrice> priceList = null;
        List<ComCatePrice> noPriceList = null;
        //根据分类Id和小区Id查询所属企业
        MemberAddress memberAddress = memberAddressService.getMemberAdderssByAliUserId(aliUserId);
        if (null != memberAddress) {
            if ("bigFurniture".equals(categoryBean.getType())) {
                Integer bigCompanyId = companyStreetBigService.selectStreetBigCompanyIdByCategoryId(categoryBean.getId(), memberAddress.getStreetId());
                if (null != bigCompanyId) {
                    priceList = comCateMapper.getBigThingCategoryList(categoryBean.getId(), bigCompanyId, memberAddress.getCityId());
                    noPriceList = new ArrayList<>();
                }
            } else if ("appliance".equals(categoryBean.getType())) {
                String appliceCompanyId = companyStreetApplianceService.selectStreetApplianceCompanyIdByCategoryId(categoryBean.getId(), memberAddress.getStreetId(), memberAddress.getCommunityId());
                if (StringUtils.isNotBlank(appliceCompanyId)) {
                    //获取当前公司下的回收列表
                    priceList = comCateMapper.getBigThingCategoryList(categoryBean.getId(), Integer.parseInt(appliceCompanyId), memberAddress.getCityId());
                    noPriceList = new ArrayList<>();
                }
            }
        }
        if (null == priceList && null == noPriceList) {
            priceList = this.getAvgPrice(categoryBean);
            noPriceList = this.getAvgNoPrice(categoryBean);
        }
        map.put("ComCatePriceList", priceList);
        map.put("ComCateNoPriceList", noPriceList);
        map.put("category", category);
        return map;
    }

    /**
     * 获取价格分页(不分页)
     */
    @Override
    public Map<String, Object> getPrice(CategoryBean categoryBean) {
        Map<String, Object> map = new HashMap<>();

        List<ComCatePrice> priceList = null;
        List<ComCatePrice> noPriceList = null;
        //如果不为空获取当前公司回收价格
        if (categoryBean.getCommunityId() != null && categoryBean.getCommunityId() != 0) {
            Category category = categoryService.selectById(categoryBean.getId());
            //根据分类Id和小区Id查询所属企业
            Company company = this.selectCompanys(categoryBean.getId(), categoryBean.getCommunityId());
            //根据小区Id查询唯一的所属企业
            //CompanyServiceRange companyServiceRange = companyServiceService.selectOne(new EntityWrapper<CompanyServiceRange>().eq("community_id", categoryBean.getCommunityId()));
            //Integer companyId = companyService.getCompanyIdByIds(orderbean.getCommunityId(),orderbean.getCategoryParentId());
            if (company != null) {
                //根据小区Id查询小区信息
                Community community = communityService.selectById(categoryBean.getCommunityId());
                //判断该小区是否免费
                if ("1".equals(community.getIsFree())) {
                    //免费时
                    //生活垃圾时
                    if ("HOUSEHOLD".equals(categoryBean.getTitle())) {
                        priceList = new ArrayList<ComCatePrice>();
                        noPriceList = this.getNoPrice(categoryBean, Integer.parseInt(company.getId().toString()));
                    } else {
                        priceList = this.getOwnnerPrice(categoryBean, Integer.parseInt(company.getId().toString()));
                    }
                    map.put("comIsNull", "2");
                    map.put("isCash", "1");
                    map.put("companyId", company.getId());
                } else {
                    //获取当前公司下的回收列表
                    map.put("comIsNull", "2");
                    map.put("isCash", "0");
                    map.put("companyId", company.getId());
                    priceList = this.getOwnnerPrice(categoryBean, Integer.parseInt(company.getId().toString()));
                    noPriceList = this.getOwnnerNoPrice(categoryBean, Integer.parseInt(company.getId().toString()));
                }
            } else {
                //获取平均价格
                map.put("comIsNull", "1");
                map.put("isCash", "0");
                priceList = this.getAvgPrice(categoryBean);
                noPriceList = this.getAvgNoPrice(categoryBean);
            }
        } else {
            //获取平均价格
            map.put("comIsNull", "0");
            map.put("isCash", "0");
            priceList = this.getAvgPrice(categoryBean);
            noPriceList = this.getAvgNoPrice(categoryBean);
        }
        map.put("ComCatePriceList", priceList);
        map.put("ComCateNoPriceList", noPriceList);

        return map;
    }

    /**
     * @author wangcan
     * @param categoryBean:传入小区CommunityId，id(分类ID，大的分类) 获取价格分页(不分页)
     */
    @Override
    public Map<String, Object> getTowCategoryList(CategoryBean categoryBean) {
        Map<String, Object> map = new HashMap<>();

        List<ComCatePrice> priceList = null;
        //判断是否免费
        String isCash = categoryBean.getIsCash();
        Order order = orderService.selectById(categoryBean.getOrderId());
        Area area = areaService.selectById(order.getAreaId());
        //如果不为空获取当前公司回收价格
        if (order.getCompanyId() != null) {
            //获取当前公司下的回收列表
            map.put("comIsNull", true);
            map.put("companyId", order.getCompanyId());
            priceList = companyCategoryCityService.getOwnnerPriceAppByCity(categoryBean.getId().toString(), order.getCompanyId().toString(), area.getParentId().toString());
            if (priceList.isEmpty()) {
                priceList = this.getOwnnerPriceApp(categoryBean, order.getCompanyId());
            }

        } else {
            //获取平均价格
            map.put("comIsNull", false);
            priceList = this.getAvgPriceApp(categoryBean);
        }
        map.put("ComCatePriceList", priceList);

        return map;
    }

    @Override
    public List<ComCatePrice> getOwnnerPrice(CategoryBean categoryBean, Integer companyId) {
        return comCateMapper.getOwnnerPrice(categoryBean, companyId);
    }

    public List<ComCatePrice> getOwnnerNoPrice(CategoryBean categoryBean, Integer companyId) {
        return comCateMapper.getOwnnerNoPrice(categoryBean, companyId);
    }

    public List<ComCatePrice> getNoPrice(CategoryBean categoryBean, Integer companyId) {
        return comCateMapper.getNoPrice(categoryBean, companyId);
    }

    @Override
    public List<ComCatePrice> getOwnnerPriceApp(CategoryBean categoryBean, Integer companyId) {
        return comCateMapper.getOwnnerPriceApp(categoryBean, companyId);
    }

    @Override
    public List<ComCatePrice> getOwnnerPriceApps(CategoryBean categoryBean, Integer companyId) {
        return comCateMapper.getOwnnerPriceApps(categoryBean, companyId);
    }

    @Override
    public List<ComCatePrice> getAvgPrice(CategoryBean categoryBean) {
        return comCateMapper.getAvgPrice(categoryBean);
    }

    public List<ComCatePrice> getAvgNoPrice(CategoryBean categoryBean) {
        return comCateMapper.getAvgNoPrice(categoryBean);
    }

    public List<ComCatePrice> getAvgPriceApp(CategoryBean categoryBean) {
        return comCateMapper.getAvgPriceApp(categoryBean);
    }

    @Override
    public List<BusinessCategoryResult> selectComCateAttOptPrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean) {
        String cateOptId = comIdAndCateOptIdBean.getCateOptId();

        String companyId = comIdAndCateOptIdBean.getCompanyId();
        try {
            if (cateOptId == null || "".equals(cateOptId)) {
                throw new ApiException("cateOptId不能为空");
            }
            if (companyId == null || "".equals(companyId)) {
                throw new ApiException("请先登录");
            }
        } catch (ApiException e) {
            e.getCause();
        }
        List<BusinessCategoryResult> list = comCateMapper.selectComCateAttOptPrice(comIdAndCateOptIdBean);
        String price = null;
        for (BusinessCategoryResult businessCategoryResult : list) {
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
        return list;
    }

    @Override
    public CompanyCategory selectByCategoryId(int categoryId) {
        return comCateMapper.selectByCategoryId(categoryId);
    }

    /**
     * 更新生活垃圾价格
     *
     * @param
     * @return
     */
    @Override
    public int updatePrice(CompanyCategory companyCategory) {
        return companyCategoryMapper.updateHousePrice(companyCategory);
    }

    /**
     * 添加关联表
     */
    @Override
    public int insertPrice(CompanyCategory companyCategory) {
        return companyCategoryMapper.insertPrice(companyCategory);
    }

    @Override
    public CompanyCategory selectPriceByAttrId(String id, String companyId) {
        return companyCategoryMapper.selectPriceByAttrId(id, companyId);
    }

    @Override
    public Company selectCompanys(Integer categoryId, Integer communityId) {
        return companyCategoryMapper.selectCompanys(categoryId, communityId);
    }

    public Company selectCompanyByTitle(String title, Integer communityId) {
        return companyCategoryMapper.selectCompanyByTitle(title, communityId);
    }

}
