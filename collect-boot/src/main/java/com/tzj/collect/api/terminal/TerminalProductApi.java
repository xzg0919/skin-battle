package com.tzj.collect.api.terminal;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.taobao.api.ApiException;
import com.tzj.collect.common.util.EnterpriseTerminalUtils;
import com.tzj.collect.core.param.enterprise.EnterpriseProductBean;
import com.tzj.collect.core.service.EnterpriseProductService;
import com.tzj.collect.entity.EnterpriseProduct;
import com.tzj.collect.entity.EnterpriseTerminal;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.TERMINAL_API_COMMON_AUTHORITY;

@ApiService
public class TerminalProductApi {

    @Autowired
    private EnterpriseProductService enterpriseProductService;

    /**
     * 以旧换新的产品列表
     * 王灿
     * @param enterpriseProductBean
     * @return
     */
    @Api(name = "terminal.terminalProductList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = TERMINAL_API_COMMON_AUTHORITY)
    @DS("slave")
    public Object terminalProductList(EnterpriseProductBean enterpriseProductBean) throws ApiException {
        EnterpriseTerminal enterpriseTerminal = EnterpriseTerminalUtils.getEnterpriseTerminal();
        EntityWrapper<EnterpriseProduct> wrapper = new EntityWrapper<>();
        wrapper.eq("enterprise_id", enterpriseTerminal.getEnterpriseId());
        wrapper.eq("del_flag", 0);
        int count = enterpriseProductService.selectCount(wrapper);
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
    @Api(name = "terminal.terminalProductById", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = TERMINAL_API_COMMON_AUTHORITY)
    @DS("slave")
    public Object terminalProductById(EnterpriseProductBean enterpriseProductBean) {
        return  enterpriseProductService.selectById(enterpriseProductBean.getId());
    }
}
