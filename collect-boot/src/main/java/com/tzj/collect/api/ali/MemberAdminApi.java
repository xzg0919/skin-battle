package com.tzj.collect.api.ali;


import com.tzj.collect.api.picc.param.PiccInsurancePolicyBean;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.entity.Member;
import com.tzj.collect.service.MemberService;
import com.tzj.collect.service.PiccInsurancePolicyService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;

/**
 * 会员个人中心相关接口
 * @Author 王美霞20180305
 **/
@ApiService
public class MemberAdminApi {

    @Autowired
    private MemberService memberService;
    @Autowired
    private PiccInsurancePolicyService piccInsurancePolicyService;




    /**
     * 获取会员个人中心的相关数据
     * @author 王灿
     * @param
     */
    @Api(name = "admin.memberAdmin", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object memberAdmin() {
        Member member = MemberUtils.getMember();
        return memberService.memberAdmin(member.getId().intValue());
    }

    /**保险Id查询保险详情和用户保单的信息
     * @author 王灿
     * 根据
     * @param
     */
    @Api(name = "admin.insuranceDetal", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object insuranceDetal(PiccInsurancePolicyBean piccInsurancePolicyBean) {
        Member member = MemberUtils.getMember();
        return piccInsurancePolicyService.insuranceDetal(member.getId().intValue(),Integer.parseInt(piccInsurancePolicyBean.getId()));
    }

}
