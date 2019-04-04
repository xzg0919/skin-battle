package com.tzj.collect.api.ali;

import com.tzj.collect.api.ali.param.MemberBean;
import com.tzj.collect.entity.Member;
import com.tzj.collect.service.MemberService;
import com.tzj.module.api.annotation.*;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.ApiContext;
import com.tzj.module.easyopen.doc.DataType;
import com.tzj.module.easyopen.doc.annotation.ApiDoc;
import com.tzj.module.easyopen.doc.annotation.ApiDocField;
import com.tzj.module.easyopen.doc.annotation.ApiDocMethod;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import static com.tzj.collect.common.constant.TokenConst.*;
import com.tzj.collect.api.param.TokenBean;

/**
 * token api
 * @Author 胡方明（12795880@qq.com）
 **/
@ApiService
@ApiDoc(value = "token模块",appModule = "ali")
public class TokenApi {

    @Autowired
    private MemberService memberService;

    /**
     * 根据会员信息获取token
     * 忽略token验证，需要sign签名验证
     * @param memberBean
     * @return
     */
    @Api(name = "token.get", version = "1.0")
    @AuthIgnore //这个api忽略token验证
    //@SignIgnore //这个api忽略sign验证以及随机数以及时间戳验证
    @ApiDocMethod(description="获取token",results={
            @ApiDocField(name="tokenBean",description="token对象",dataType= DataType.OBJECT, beanClass=TokenBean.class)
    })
    public TokenBean getToken(MemberBean memberBean) {

        //先根据 ali id 去查询，没有的话新增，有的话直接返回token
        Member member=memberService.findMemberByAliId(memberBean.getAliMemberId());
        if(member==null){
            //新增一条记录
            member=memberService.saveByMemberBean(memberBean);
        }

        String token= JwtUtils.generateToken(member.getId().toString(), ALI_API_EXPRIRE,ALI_API_TOKEN_SECRET_KEY);
        String securityToken=JwtUtils.generateEncryptToken(token,ALI_API_TOKEN_CYPTO_KEY);
        TokenBean tokenBean=new TokenBean();
        tokenBean.setExpire(ALI_API_EXPRIRE);
        tokenBean.setToken(securityToken);
        return tokenBean;
    }

    /**
     * 刷新token
     * 需要token验证，忽略sign签名验证
     * 需要 ALI_API_COMMON_AUTHORITY 权限
     * @return
     */
    @Api(name = "token.flush", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    @ApiDocMethod(description = "刷新token",results = {
            @ApiDocField(name="tokenBean",description="token对象",dataType= DataType.OBJECT, beanClass=TokenBean.class)
    })
    public TokenBean flushToken(){

        HttpServletRequest request= ApiContext.getRequest();
        Subject subject = (Subject) request.getAttribute("subject");

        //接口里面获取  Member 的例子
        Member member= (Member) subject.getUser();

        String token= JwtUtils.generateToken(subject.getId(), ALI_API_EXPRIRE,ALI_API_TOKEN_SECRET_KEY);
        String securityToken=JwtUtils.generateEncryptToken(token,ALI_API_TOKEN_CYPTO_KEY);

        TokenBean tokenBean=new TokenBean();
        tokenBean.setExpire(ALI_API_EXPRIRE);
        tokenBean.setToken(securityToken);
        return tokenBean;
    }
}
