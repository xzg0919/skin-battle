package com.tzj.collect.api.terminal;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.taobao.api.ApiException;
import com.tzj.collect.core.param.enterprise.EnterpriseTerminalBean;
import com.tzj.collect.core.service.EnterpriseTerminalService;
import com.tzj.collect.core.service.MessageService;
import com.tzj.collect.entity.EnterpriseTerminal;
import com.tzj.collect.core.param.token.TokenBean;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.api.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.*;


@ApiService
public class TerminalApi {
    @Autowired
    private EnterpriseTerminalService enterpriseTerminalService;
    @Autowired
    private MessageService messageService;

    /**
     * 以旧换新终端获取验证码
     * wangcan
     * @param
     * @return
     */
    @Api(name="terminal.giveCaptcha",version="1.0")
    @SignIgnore
    @AuthIgnore
    //@RequiresPermissions(values = TERMINAL_API_COMMON_AUTHORITY)
    public Object giveCaptcha(EnterpriseTerminalBean enterpriseTerminalBean)throws ApiException {
        return messageService.getMessageCode(enterpriseTerminalBean.getTel());
    }

    /**
     * 以旧换新终端登录
     * wangcan
     * @param
     * @return
     */
    @Api(name="terminal.login",version="1.0")
    @SignIgnore
    @AuthIgnore
    //@RequiresPermissions(values = TERMINAL_API_COMMON_AUTHORITY)
    public Object login(EnterpriseTerminalBean enterpriseTerminalBean)throws ApiException {
        EnterpriseTerminal enterpriseTerminal = null;
        Map<String,Object> map = new HashMap<String,Object>();
        //判断是否是验证码登录
        if(!StringUtils.isBlank(enterpriseTerminalBean.getCaptcha())){
            boolean bool = messageService.validMessage(enterpriseTerminalBean.getTel(),enterpriseTerminalBean.getCaptcha());
            //先去验证手机与验证码！！！
            if(bool){
                enterpriseTerminal = enterpriseTerminalService.selectOne(new EntityWrapper<EnterpriseTerminal>().eq("tel", enterpriseTerminalBean.getTel()).eq("del_flag",0));
                if (enterpriseTerminal == null) {
                    throw  new ApiException("账号或验证码码错误");
                }
            }else{
                throw new com.tzj.module.easyopen.exception.ApiException("验证码错误");
            }
        }else {
            enterpriseTerminal = enterpriseTerminalService.selectOne(new EntityWrapper<EnterpriseTerminal>().eq("tel", enterpriseTerminalBean.getTel()).eq("password_",enterpriseTerminalBean.getPassword()).eq("del_flag",0));
            if (enterpriseTerminal == null) {
                throw  new ApiException("账号或密码错误");
            }
        }
        String token = JwtUtils.generateToken(enterpriseTerminal.getId().toString(), TERMINAL_API_EXPRIRE, TERMINAL_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, TERMINAL_API_TOKEN_CYPTO_KEY);
        System.out.println("返回以旧换新终端的token是 ："+securityToken);
        TokenBean tokenBean = new TokenBean();
        tokenBean.setExpire(TERMINAL_API_EXPRIRE);
        tokenBean.setToken(securityToken);
        map.put("tokenBean",tokenBean);
        map.put("enterpriseTerminal",enterpriseTerminal);
        return map;
    }
}
