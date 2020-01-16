package com.tzj.green.api.aliApi;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.taobao.api.ApiException;
import com.tzj.green.entity.Member;
import com.tzj.green.param.MemberBean;
import com.tzj.green.service.AreaService;
import com.tzj.green.service.MemberPointsService;
import com.tzj.green.service.MemberService;
import com.tzj.green.service.MessageService;
import com.tzj.module.api.annotation.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.Mergeable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tzj.green.common.content.TokenConst.BUSINESS_API_COMMON_AUTHORITY;

@ApiService
public class MemberApi {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MemberPointsService memberPointsService;

    /**
     * 获取用户实体卡
     * @param memberBean
     * @return
     */
    @Api(name = "member.getRealNo", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object getRealNo(MemberBean memberBean){
        if(StringUtils.isBlank(memberBean.getAliUserId())){
            return "未传aliUserId";
        }
        return memberService.getRealNoByAliUserId(memberBean.getAliUserId());
    }

    /**
     * 获取验证码
     * @param memberBean
     * @return
     */
    @Api(name = "member.getSecurityCode", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public String getMessageCode(MemberBean memberBean) {
        return messageService.getMessageCode(memberBean.getTel());
    }

    /**
     * 绑定实体卡
     * @param memberBean
     * @return
     * @throws ApiException
     */
    @Api(name = "member.BindRealCode", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object scanRealCode(MemberBean memberBean) throws ApiException {
       return memberService.scanRealCode(memberBean);
    }
    /**
     * 获取用户积分信息
     * @param memberBean
     * @return
     * @throws ApiException
     */
    @Api(name = "member.getAllPoints", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object getAllPoints(MemberBean memberBean) throws ApiException {
        return memberService.getAllPoints(memberBean);
    }


}
