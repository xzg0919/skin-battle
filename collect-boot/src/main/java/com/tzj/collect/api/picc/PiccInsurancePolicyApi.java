package com.tzj.collect.api.picc;

import com.taobao.api.ApiException;
import com.tzj.collect.api.picc.param.PiccInsurancePolicyBean;
import com.tzj.collect.common.util.PiccCompanyUtils;
import com.tzj.collect.entity.PiccCompany;
import com.tzj.collect.entity.PiccInsurancePolicyAgreement;
import com.tzj.collect.entity.PiccInsurancePolicyContent;
import com.tzj.collect.service.PiccInsurancePolicyAgreementService;
import com.tzj.collect.service.PiccInsurancePolicyContentService;
import com.tzj.collect.service.PiccInsurancePolicyService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tzj.collect.common.constant.TokenConst.PICC_API_COMMON_AUTHORITY;

/**
 * picc相关产品表
 */
@ApiService
public class PiccInsurancePolicyApi {

    @Autowired
    private PiccInsurancePolicyService piccInsurancePolicyService;
    @Autowired
    private PiccInsurancePolicyContentService piccInsurancePolicyContentService;
    @Autowired
    private PiccInsurancePolicyAgreementService piccInsurancePolicyAgreementService;

    /**
     * picc新增/修改 产品
     * wangcan
     * @param
     * @return
     */
    @Api(name="picc.updateInsurance",version="1.0")
    @SignIgnore
    @RequiresPermissions(values = PICC_API_COMMON_AUTHORITY)
    public Object updateInsurance(PiccInsurancePolicyBean piccInsurancePolicyBean) throws ApiException{
        PiccCompany piccCompany = PiccCompanyUtils.getPiccCompany();
        return  piccInsurancePolicyService.updateInsurance(piccCompany.getId(),piccInsurancePolicyBean);
    }

    /**
     * picc保险产品详细信息
     * wangcan
     * @param
     * @return
     */
    @Api(name="picc.getInsurancePolicy",version="1.0")
    @SignIgnore
    @RequiresPermissions(values = PICC_API_COMMON_AUTHORITY)
    public Object getInsurancePolicy() throws ApiException{
        PiccCompany piccCompany = PiccCompanyUtils.getPiccCompany();
        return  piccInsurancePolicyService.getInsurancePolicy(piccCompany.getId());
    }

    /**
     * picc删除产品协议
     * wangcan
     * @param
     * @return
     */
    @Api(name="picc.deleteInsuranceAgreement",version="1.0")
    @SignIgnore
    @RequiresPermissions(values = PICC_API_COMMON_AUTHORITY)
    public Object deleteInsuranceAgreement(PiccInsurancePolicyBean piccInsurancePolicyBean) throws ApiException{
        PiccInsurancePolicyAgreement piccInsurancePolicyAgreement = piccInsurancePolicyAgreementService.selectById(piccInsurancePolicyBean.getId());
        piccInsurancePolicyAgreement.setDelFlag("1");
        piccInsurancePolicyAgreementService.updateById(piccInsurancePolicyAgreement);
        return "操作成功";
    }
    /**
     * picc删除产品内容
     * wangcan
     * @param
     * @return
     */
    @Api(name="picc.deleteInsuranceContent",version="1.0")
    @SignIgnore
    @RequiresPermissions(values = PICC_API_COMMON_AUTHORITY)
    public Object deleteInsuranceContent(PiccInsurancePolicyBean piccInsurancePolicyBean) throws ApiException{
        PiccInsurancePolicyContent piccInsurancePolicyContent = piccInsurancePolicyContentService.selectById(piccInsurancePolicyBean.getId());
        piccInsurancePolicyContent.setDelFlag("1");
        piccInsurancePolicyContentService.updateById(piccInsurancePolicyContent);
        return "操作成功";
    }

}
