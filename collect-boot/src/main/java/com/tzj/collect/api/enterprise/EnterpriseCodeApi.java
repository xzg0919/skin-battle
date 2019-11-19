package com.tzj.collect.api.enterprise;

import com.tzj.collect.common.util.EnterpriseUtils;
import com.tzj.collect.core.param.enterprise.EnterpriseCodeBean;
import com.tzj.collect.core.service.EnterpriseCodeService;
import com.tzj.collect.entity.EnterpriseAccount;
import static com.tzj.collect.common.constant.TokenConst.ENTERPRISE_API_COMMON_AUTHORITY;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

@ApiService
public class EnterpriseCodeApi {

    @Autowired
    private EnterpriseCodeService enterpriseCodeService;

    /**
     * 以旧换新的券的列表 王灿
     *
     * @param enterpriseCodeBean
     * @return
     */
    @Api(name = "enterprise.enterpriseCodeList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ENTERPRISE_API_COMMON_AUTHORITY)
    public Object enterpriseCodeList(EnterpriseCodeBean enterpriseCodeBean) throws Exception {
        EnterpriseAccount enterpriseAccount = EnterpriseUtils.getEnterpriseAccount();
        return enterpriseCodeService.enterpriseCodeList(enterpriseCodeBean, enterpriseAccount.getEnterpriseId());
    }

    /**
     * 以旧换新的券的详情 王灿
     *
     * @param enterpriseCodeBean
     * @return
     */
    @Api(name = "enterprise.enterpriseCodeDetil", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ENTERPRISE_API_COMMON_AUTHORITY)
    public Object enterpriseCodeDetil(EnterpriseCodeBean enterpriseCodeBean) throws Exception {
        return enterpriseCodeService.enterpriseCodeDetil(enterpriseCodeBean.getId());
    }

}
