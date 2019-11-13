package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CompanyCategoryAttrOptionMapper;
import com.tzj.collect.core.mapper.CompanyCategoryMapper;
import com.tzj.collect.core.param.business.CategoryAttrOptionBean;
import com.tzj.collect.core.param.business.ComIdAndCateOptIdBean;
import com.tzj.collect.core.param.business.CompanyCategoryAttrOptionBean;
import com.tzj.collect.core.service.CompanyCategoryAttrOptionCityService;
import com.tzj.collect.core.service.CompanyCategoryAttrOptionService;
import com.tzj.collect.core.service.CompanyCategoryService;
import com.tzj.collect.entity.Category.CategoryType;
import com.tzj.collect.entity.CompanyAccount;
import com.tzj.collect.entity.CompanyCategory;
import com.tzj.collect.entity.CompanyCategoryAttrOption;
import com.tzj.collect.entity.CompanyCategoryAttrOptionCity;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.easyopen.ApiContext;
import com.tzj.module.easyopen.exception.ApiException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyCategoryAttrOptionServiceImpl extends ServiceImpl<CompanyCategoryAttrOptionMapper, CompanyCategoryAttrOption> implements CompanyCategoryAttrOptionService {

    @Autowired
    private CompanyCategoryMapper comCateMapper;
    @Autowired
    private CompanyCategoryService companyCategoryService;
    @Autowired
    private CompanyCategoryAttrOptionCityService companyCategoryAttrOptionCityService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean modifyComCateAttOptPrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean) throws ApiException {
        if (comIdAndCateOptIdBean.getTitle() == null || comIdAndCateOptIdBean.getCategoryId() == null || comIdAndCateOptIdBean.getCateOptId() == null) {
            throw new ApiException("请传递正确的参数");
        } else if (CategoryType.DIGITAL.name().equals(comIdAndCateOptIdBean.getTitle())) {
            EntityWrapper<CompanyCategoryAttrOption> wraper = new EntityWrapper<CompanyCategoryAttrOption>();
            wraper.eq("company_id", comIdAndCateOptIdBean.getCompanyId());
            wraper.eq("category_attr_option_id", comIdAndCateOptIdBean.getCateOptId());
            wraper.eq("del_flag", "0");
            CompanyCategoryAttrOption attrOption = this.selectOne(wraper);
            if (attrOption != null) {
                attrOption.setAttrOptionPrice(new BigDecimal(comIdAndCateOptIdBean.getCateOptPrice()));
                attrOption.setUpdateBy(comIdAndCateOptIdBean.getCompanyId());
                attrOption.setUpdateDate(new Date());
                this.updateAllColumnById(attrOption);
            } else {
                attrOption = new CompanyCategoryAttrOption();
                attrOption.setCompanyId(Long.parseLong(comIdAndCateOptIdBean.getCompanyId()));
                attrOption.setAttrOptionPrice(new BigDecimal(comIdAndCateOptIdBean.getCateOptPrice()));
                attrOption.setCategoryAttrOptionId(Long.parseLong(comIdAndCateOptIdBean.getCateOptId()));
                this.insert(attrOption);
            }

            //判断实际价格是否小于零，若为真rollback
            boolean flag = false;
            BigDecimal minPrice = new BigDecimal(comCateMapper.priceIsAvailable(comIdAndCateOptIdBean));
            if (minPrice.compareTo(BigDecimal.ZERO) >= 0) {
                return true;
            } else {
                EntityWrapper<CompanyCategory> wraper2 = new EntityWrapper<>();
                wraper2.eq("company_id", comIdAndCateOptIdBean.getCompanyId());
                wraper2.eq("category_id", comIdAndCateOptIdBean.getCategoryId());
                wraper2.eq("del_flag", "0");
                List<CompanyCategory> comCateList = comCateMapper.selectList(wraper2);
                if (comCateList.size() > 0) {
                    CompanyCategory companyCategory = comCateList.get(0);
                    BigDecimal guidePrice = new BigDecimal(companyCategory.getPrice());
                    if (guidePrice.add(minPrice).compareTo(BigDecimal.ZERO) >= 0) {
                        flag = true;
                    }
                }
            }
            if (!flag) {
                throw new ApiException("当前价格不可使用,请确认再操作");
            } else {
                return true;
            }
        } else {
            throw new ApiException("只能修改家电数码的分类属性价格");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateComCateAttOptPrice(List<CategoryAttrOptionBean> categoryAttrOptionBeanList, String cityId) throws Exception {
        Subject subject = ApiContext.getSubject();
        // 接口里面获取 CompanyAccount 的例子
        CompanyAccount companyAccount = (CompanyAccount) subject.getUser();
        if (companyAccount != null) {
            //判断总价格是否大于0
            if (!categoryAttrOptionBeanList.isEmpty()) {
                double price = 0;
                for (CategoryAttrOptionBean categoryAttrOptionBean : categoryAttrOptionBeanList) {
                    List<CompanyCategoryAttrOptionBean> companyCategoryAttrOptionBeanList = categoryAttrOptionBean.getCompanyCategoryAttrOptionBeanList();
                    for (CompanyCategoryAttrOptionBean companyCategoryAttrOptionBean : companyCategoryAttrOptionBeanList) {
                        EntityWrapper<CompanyCategoryAttrOptionCity> wraper = new EntityWrapper<CompanyCategoryAttrOptionCity>();
                        wraper.eq("company_id", companyAccount.getCompanyId());
                        wraper.eq("category_attr_option_id", companyCategoryAttrOptionBean.getAttOptionId());
                        wraper.eq("city_id", cityId);
                        wraper.eq("del_flag", "0");
                        CompanyCategoryAttrOptionCity companyCategoryAttrOptionCity = companyCategoryAttrOptionCityService.selectOne(wraper);
                        if (companyCategoryAttrOptionCity != null) {
                            companyCategoryAttrOptionCity.setAttrOptionPrice(new BigDecimal(companyCategoryAttrOptionBean.getComOptPrice()));
                            companyCategoryAttrOptionCity.setUpdateBy(companyAccount.getCompanyId().toString());
                            companyCategoryAttrOptionCity.setUpdateDate(new Date());
                            companyCategoryAttrOptionCity.setSpecialPrice(StringUtils.isBlank(companyCategoryAttrOptionBean.getSpecialPrice()) ? BigDecimal.ZERO : new BigDecimal(companyCategoryAttrOptionBean.getSpecialPrice()));
                            companyCategoryAttrOptionCityService.updateById(companyCategoryAttrOptionCity);
                        } else {
                            companyCategoryAttrOptionCity = new CompanyCategoryAttrOptionCity();
                            companyCategoryAttrOptionCity.setCityId(cityId);
                            companyCategoryAttrOptionCity.setCompanyId(companyAccount.getCompanyId().toString());
                            companyCategoryAttrOptionCity.setAttrOptionPrice(new BigDecimal(companyCategoryAttrOptionBean.getComOptPrice()));
                            companyCategoryAttrOptionCity.setCategoryAttrOptionId(Integer.parseInt(companyCategoryAttrOptionBean.getAttOptionId()));
                            companyCategoryAttrOptionCity.setSpecialPrice(StringUtils.isBlank(companyCategoryAttrOptionBean.getSpecialPrice()) ? BigDecimal.ZERO : new BigDecimal(companyCategoryAttrOptionBean.getSpecialPrice()));
                            companyCategoryAttrOptionCityService.insert(companyCategoryAttrOptionCity);
                        }
                    }
                }
                return true;
            } else {
                throw new ApiException("集合为空!");
            }
        } else {
            throw new ApiException("请先登录,再执行更新操作");
        }
    }

}
