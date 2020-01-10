package com.tzj.green.api.aliApi;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.taobao.api.ApiException;
import com.tzj.green.entity.Member;
import com.tzj.green.param.MemberBean;
import com.tzj.green.service.AreaService;
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

    /**
     *获取省份列表
     */
    @Api(name = "member.getMemberInfo", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object getMemberByAliUserId(MemberBean memberBean) {
        return memberService.getMemberByAliUserId(memberBean.getAliUserId());
    }

    @Api(name = "member.getRealNo", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object getRealNo(MemberBean memberBean){
        return memberService.getRealNoByAliUserId(memberBean.getAliUserId());
    }
    @Api(name = "member.getSecurityCode", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public String getMessageCode(MemberBean memberBean) {
        return messageService.getMessageCode(memberBean.getTel());
    }
    @Api(name = "member.BindRealCode", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object scanRealCode(MemberBean memberBean) throws ApiException {
        String realNo = memberBean.getRealNo();
        if(StringUtils.isBlank(realNo)){
            return "扫描实体卡失败";
        }
        Member member = memberService.selectOne(new EntityWrapper<Member>().eq("ali_user_id", memberBean.getAliUserId()));
        if(member == null){
            return "未登录";
        }
        if(StringUtils.isNotBlank(member.getRealNo())){
            return "已绑定实体卡,勿重复绑定";
        }
        if(messageService.validMessage(memberBean.getTel(), memberBean.getSecurityCode())){
            member.setRealNo(realNo);
            memberService.updateById(member);
            return "绑定成功";
        }
        return "验证码错误";

    }


}
