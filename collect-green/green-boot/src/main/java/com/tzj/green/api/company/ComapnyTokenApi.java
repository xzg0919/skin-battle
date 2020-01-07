package com.tzj.green.api.company;

import com.tzj.green.common.utils.CompanyUtils;
import com.tzj.green.entity.Area;
import com.tzj.green.entity.Company;
import com.tzj.green.param.CompanyBean;
import com.tzj.green.service.AreaService;
import com.tzj.green.service.CompanyService;
import com.tzj.module.api.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import static com.tzj.green.common.content.TokenConst.*;

@ApiService
public class ComapnyTokenApi {


	@Autowired
	private CompanyService companyService;

    /**
     * 回收企业获取token
     * 忽略token验证，需要sign签名验证
     */
    @Api(name = "company.token.get", version = "1.0")
    @SignIgnore
    @AuthIgnore
    //@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getToken(CompanyBean companyBean) {
        return companyService.getToken(companyBean);
    }

    /**
     * 回收企业获取token
     * 忽略token验证，需要sign签名验证
     */
    @Api(name = "company.token.getCompany", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getCompany() {
        Company company = CompanyUtils.getCompanyAccount();
        return companyService.selectById(company.getId());
    }


}
