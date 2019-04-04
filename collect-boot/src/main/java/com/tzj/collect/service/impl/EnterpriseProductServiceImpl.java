package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.taobao.api.ApiException;
import com.tzj.collect.api.enterprise.param.EnterpriseProductBean;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.EnterpriseProduct;
import com.tzj.collect.mapper.EnterpriseProductMapper;
import com.tzj.collect.service.CategoryService;
import com.tzj.collect.service.EnterpriseProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class EnterpriseProductServiceImpl extends ServiceImpl<EnterpriseProductMapper,EnterpriseProduct> implements EnterpriseProductService {

    @Autowired
    private EnterpriseProductMapper enterpriseProductMapper;
    @Autowired
    private CategoryService categoryService;

    /**
     * 更改/新增以旧换新的产品信息
     * 王灿
     * @param enterpriseProductBean
     * @return
     */
    @Transactional
    @Override
    public Object updateEnterpriseProduct(EnterpriseProductBean enterpriseProductBean,Integer enterpriseId) throws ApiException{
        EnterpriseProduct enterpriseProduct;
        //判断是否是新增还是修改
        if(StringUtils.isBlank(enterpriseProductBean.getId())){
            //新增
            enterpriseProduct = new EnterpriseProduct();
        }else{
            //修改
            enterpriseProduct = this.selectById(enterpriseProductBean.getId());
        }
        enterpriseProduct.setSubsidiesPrice(enterpriseProductBean.getSubsidiesPrice().setScale(2, BigDecimal.ROUND_DOWN));
        enterpriseProduct.setPrice(enterpriseProductBean.getPrice().setScale(2, BigDecimal.ROUND_DOWN));
        enterpriseProduct.setPicUrl(enterpriseProductBean.getPicUrl());
        enterpriseProduct.setName(enterpriseProductBean.getName());
        enterpriseProduct.setEnterpriseId(enterpriseId);
        Category category = categoryService.selectById(enterpriseProductBean.getCategoryId());
        enterpriseProduct.setCategoryName(category.getName());
        enterpriseProduct.setCategoryId(enterpriseProductBean.getCategoryId());
        boolean b = false;
        try{
            b = this.insertOrUpdate(enterpriseProduct);
        }catch (Exception e){
            throw new ApiException("更新/新增失败");
        }
        if(!b){
            return "操作失败";
        }
        return "操作成功";
    }
}
