package com.tzj.green.api.app;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.green.entity.Recyclers;
import com.tzj.green.param.RecyclersLoginBean;
import com.tzj.green.param.TokenBean;
import com.tzj.green.service.MessageService;
import com.tzj.green.service.RecyclersService;
import com.tzj.module.api.annotation.*;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.ApiContext;
import com.tzj.module.easyopen.doc.DataType;
import com.tzj.module.easyopen.doc.annotation.ApiDoc;
import com.tzj.module.easyopen.doc.annotation.ApiDocField;
import com.tzj.module.easyopen.doc.annotation.ApiDocMethod;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;

import static com.tzj.green.common.content.TokenConst.*;

/**
 * token api
 *
 * @Author 胡方明（12795880@qq.com）
 *
 */
@ApiService
@ApiDoc(value = "APP端token模块", appModule = "app")
public class AppTokenApi {

    @Resource
    private RecyclersService recyclersService;
    @Resource
    private MessageService messageService;

    /**
     * 回收人员手机登录验证后获取token 忽略token验证，需要sign签名验证
     *
     * @param recyclersLoginBean
     * @return
     */
    @Api(name = "app.token.get", version = "1.0")
    @SignIgnore
    @AuthIgnore //这个api忽略token验证
    @ApiDocMethod(description = "app登录后获取token", results = {
        @ApiDocField(name = "tokenBean", description = "token对象", dataType = DataType.OBJECT, beanClass = TokenBean.class)
    })
    public TokenBean getToken(RecyclersLoginBean recyclersLoginBean) {
        Recyclers recyclers = null;
        if (StringUtils.isBlank(recyclersLoginBean.getCaptcha()) && !StringUtils.isEmpty(recyclersLoginBean.getPassword())) {
            //根据用户名和密码进行登录
            recyclers = recyclersService.selectOne(new EntityWrapper<Recyclers>().eq("tel", recyclersLoginBean.getMobile()).eq("password", recyclersLoginBean.getPassword()).eq("del_flag", 0));
            if (recyclers == null) {
                throw new ApiException("用户名或密码错误");
            } else {
                String token = JwtUtils.generateToken(recyclers.getId().toString(), APP_API_EXPRIRE, APP_API_TOKEN_SECRET_KEY);
                String securityToken = JwtUtils.generateEncryptToken(token, APP_API_TOKEN_CYPTO_KEY);
                System.out.println("返回回收人员的token是 ：" + securityToken);
                TokenBean tokenBean = new TokenBean();
                tokenBean.setExpire(APP_API_EXPRIRE);
                tokenBean.setToken(securityToken);
                return tokenBean;
            }

        } else {
            //先去验证手机与验证码！！！
            if (messageService.validMessage(recyclersLoginBean.getMobile(), recyclersLoginBean.getCaptcha())) {
                recyclers = recyclersService.selectByMobile(recyclersLoginBean.getMobile());
                if (recyclers == null) {
                    recyclers = new Recyclers();
                    recyclers.setName(recyclersLoginBean.getMobile());
                    recyclers.setTel(recyclersLoginBean.getMobile());
                }
                String token = JwtUtils.generateToken(recyclers.getId().toString(), APP_API_EXPRIRE, APP_API_TOKEN_SECRET_KEY);
                String securityToken = JwtUtils.generateEncryptToken(token, APP_API_TOKEN_CYPTO_KEY);
                System.out.println("返回回收人员的token是 ：" + securityToken);
                TokenBean tokenBean = new TokenBean();
                tokenBean.setExpire(APP_API_EXPRIRE);
                tokenBean.setToken(securityToken);
                return tokenBean;
            } else {
                throw new ApiException("验证码错误");
            }
        }
    }

    /**
     * 刷新token 需要token验证，忽略sign签名验证 需要 ALI_API_COMMON_AUTHORITY 权限
     *
     * @return
     */
    @Api(name = "app.token.flush", version = "1.0")
    @RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
    public TokenBean flushToken() {

        Subject subject = ApiContext.getSubject();

        String token = JwtUtils.generateToken(subject.getId(), APP_API_EXPRIRE, APP_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, APP_API_TOKEN_CYPTO_KEY);

        TokenBean tokenBean = new TokenBean();
        tokenBean.setExpire(APP_API_EXPRIRE);
        tokenBean.setToken(securityToken);
        return tokenBean;
    }

}
