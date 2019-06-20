package com.tzj.collect.api.enterprise;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.taobao.api.ApiException;
import com.tzj.collect.api.enterprise.param.EnterpriseProductBean;
import com.tzj.collect.common.util.EnterpriseUtils;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.EnterpriseAccount;
import com.tzj.collect.entity.EnterpriseProduct;
import com.tzj.collect.service.CategoryService;
import com.tzj.collect.service.EnterpriseProductService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.*;

/**
 * 依旧换新产品信息
 */
@ApiService
public class EnterpriseProductApi  extends  Exception{
    @Autowired
    private EnterpriseProductService enterpriseProductService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 更改/新增以旧换新的产品信息
     *
     * 王灿
     * @param enterpriseProductBean
     * @return
     */
    @Api(name = "enterprise.updateEnterpriseProduct", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ENTERPRISE_API_COMMON_AUTHORITY)
    public Object updateEnterpriseProduct(EnterpriseProductBean enterpriseProductBean) throws ApiException {
        EnterpriseAccount enterpriseAccount = EnterpriseUtils.getEnterpriseAccount();
        return  enterpriseProductService.updateEnterpriseProduct(enterpriseProductBean,enterpriseAccount.getEnterpriseId());
    }

    /**
     * 以旧换新的产品列表
     * 王灿
     * @param enterpriseProductBean
     * @return
     */
    @Api(name = "enterprise.EnterpriseProductList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ENTERPRISE_API_COMMON_AUTHORITY)
    @DS("slave")
    public Object EnterpriseProductList(EnterpriseProductBean enterpriseProductBean) throws ApiException {
        EnterpriseAccount enterpriseAccount = EnterpriseUtils.getEnterpriseAccount();
        EntityWrapper<EnterpriseProduct> wrapper = new EntityWrapper<>();
        wrapper.eq("enterprise_id", enterpriseAccount.getEnterpriseId());
        wrapper.eq("del_flag", 0);
        int count = enterpriseProductService.selectCount(wrapper);
        wrapper.orderBy("create_date",false);
        wrapper.last(" LIMIT "+((enterpriseProductBean.getPageBean().getPageNumber()-1)*enterpriseProductBean.getPageBean().getPageSize())+","+enterpriseProductBean.getPageBean().getPageSize());
        List<EnterpriseProduct> enterpriseProducts = enterpriseProductService.selectList(wrapper);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("enterpriseProducts",enterpriseProducts);
        map.put("count",count);
        map.put("pageNumber",enterpriseProductBean.getPageBean().getPageNumber());
        return  map;
    }

    /**
     * 以旧换新根据产品Id查询产品的详细信息
     * 王灿
     * @param enterpriseProductBean
     * @return
     */
    @Api(name = "enterprise.EnterpriseProductById", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ENTERPRISE_API_COMMON_AUTHORITY)
    public Object EnterpriseProductById(EnterpriseProductBean enterpriseProductBean) {
        return  enterpriseProductService.selectById(enterpriseProductBean.getId());
    }
    /**
     * 获取分类列表
     * 王灿
     * @param
     * @return
     */
    @Api(name = "enterprise.categoryList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ENTERPRISE_API_COMMON_AUTHORITY)
    public Object categoryList() {
        return categoryService.selectList(new EntityWrapper<Category>().eq("level_",0).eq("title",1).eq("del_flag",0));
    }

    /**
     * 删除商品
     * 王灿
     * @param
     * @return
     */
    @Api(name = "enterprise.deleteEnterpriseProduct", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ENTERPRISE_API_COMMON_AUTHORITY)
    public Object deleteEnterpriseProduct(EnterpriseProductBean enterpriseProductBean) {
        EnterpriseProduct enterpriseProduct = enterpriseProductService.selectById(enterpriseProductBean.getId());
        enterpriseProduct.setDelFlag("1");
        enterpriseProductService.updateById(enterpriseProduct);
        return "操作成功";
    }


}
