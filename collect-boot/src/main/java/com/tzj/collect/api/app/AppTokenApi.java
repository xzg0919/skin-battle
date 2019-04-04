package com.tzj.collect.api.app;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.app.param.RecyclersBean;
import com.tzj.collect.api.app.param.RecyclersLoginBean;
import com.tzj.collect.api.param.TokenBean;
import com.tzj.collect.common.util.RecyclersUtils;
import com.tzj.collect.entity.Recyclers;
import com.tzj.collect.service.MessageService;
import com.tzj.collect.service.RecyclersService;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static com.tzj.collect.common.constant.TokenConst.*;

/**
 * token api
 *
 * @Author 胡方明（12795880@qq.com）
 **/
@ApiService
@ApiDoc(value = "APP端token模块",appModule = "app")
public class AppTokenApi {

    @Autowired
    private RecyclersService recyclersService;
    @Autowired
    private MessageService messageService;

    /**
     * 回收人员手机登录验证后获取token
     * 忽略token验证，需要sign签名验证
     *
     * @param recyclersLoginBean
     * @return
     */
    @Api(name = "app.token.get", version = "1.0")
    @SignIgnore
    @AuthIgnore //这个api忽略token验证
    @ApiDocMethod(description="app登录后获取token",results={
            @ApiDocField(name="tokenBean",description="token对象",dataType= DataType.OBJECT, beanClass=TokenBean.class)
    })
    public TokenBean getToken(RecyclersLoginBean recyclersLoginBean) {
        Recyclers recyclers = null;
        if(StringUtils.isBlank(recyclersLoginBean.getCaptcha())){
            //根据用户名和密码进行登录
            recyclers = recyclersService.selectOne(new EntityWrapper<Recyclers>().eq("tel", recyclersLoginBean.getMobile()).eq("password", recyclersLoginBean.getPassword()).eq("del_flag", 0));
            if(recyclers==null){
                throw new ApiException("用户名或密码错误");
            }else{
                recyclers.setTencentToken(recyclersLoginBean.getXgtoken());
                recyclersService.insertOrUpdate(recyclers);
                String token = JwtUtils.generateToken(recyclers.getId().toString(), APP_API_EXPRIRE, APP_API_TOKEN_SECRET_KEY);
                String securityToken = JwtUtils.generateEncryptToken(token, APP_API_TOKEN_CYPTO_KEY);
                System.out.println("返回回收人员的token是 ："+securityToken);
                TokenBean tokenBean = new TokenBean();
                tokenBean.setExpire(APP_API_EXPRIRE);
                tokenBean.setToken(securityToken);
                return tokenBean;
            }

        }else{
            //先去验证手机与验证码！！！
            if(messageService.validMessage(recyclersLoginBean.getMobile(),recyclersLoginBean.getCaptcha())){
               recyclers = recyclersService.selectByMobile(recyclersLoginBean.getMobile());
                if (recyclers == null) {
                    recyclers = new Recyclers();
                    recyclers.setName(recyclersLoginBean.getMobile());
                    recyclers.setTel(recyclersLoginBean.getMobile());
                }
                recyclers.setTencentToken(recyclersLoginBean.getXgtoken());
                recyclersService.insertOrUpdate(recyclers);
                String token = JwtUtils.generateToken(recyclers.getId().toString(), APP_API_EXPRIRE, APP_API_TOKEN_SECRET_KEY);
                String securityToken = JwtUtils.generateEncryptToken(token, APP_API_TOKEN_CYPTO_KEY);
                System.out.println("返回回收人员的token是 ："+securityToken);
                TokenBean tokenBean = new TokenBean();
                tokenBean.setExpire(APP_API_EXPRIRE);
                tokenBean.setToken(securityToken);
                return tokenBean;
            }else{
                throw new ApiException("验证码错误");
            }
        }
    }

    /**
     * 刷新token
     * 需要token验证，忽略sign签名验证
     * 需要 ALI_API_COMMON_AUTHORITY 权限
     *
     * @return
     */
    @Api(name = "app.token.flush", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
    public TokenBean flushToken() {

    	Subject subject=ApiContext.getSubject();

        //接口里面获取  Recyclers 的例子
        Recyclers recyclers = (Recyclers) subject.getUser();

        String token = JwtUtils.generateToken(subject.getId(), APP_API_EXPRIRE, APP_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, APP_API_TOKEN_CYPTO_KEY);

        TokenBean tokenBean = new TokenBean();
        tokenBean.setExpire(APP_API_EXPRIRE);
        tokenBean.setToken(securityToken);
        return tokenBean;
    }

    /**
     * 获取回收人员的授权信息
     *
     * @return
     */
    @Api(name = "app.token.getAuthUrl", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
    public String getAuthUrl() throws Exception {
        String targetId = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(new Random().nextInt(899999)+100000);

        String authUrl = "apiname=com.alipay.account.auth&app_id=2017022805948218&app_name=mc&auth_type=AUTHACCOUNT&biz_type=openservice&method=alipay.open.auth.sdk.code.get&pid=2088421446748174&product_id=APP_FAST_LOGIN&scope=kuaijie&sign_type=RSA2&target_id="+targetId;
        String encodeAuthUrl = URLEncoder.encode(authUrl,"utf-8");
        return  authUrl+"&sign="+encodeAuthUrl;
    }

    /**
     * 获取回收人员的授权信息
     *
     * @return
     */
    @Api(name = "app.token.getAuthCode", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
    public String getAuthCode(RecyclersBean recyclersBean) throws com.taobao.api.ApiException {
        Recyclers recycler = RecyclersUtils.getRecycler();
        return  recyclersService.getAuthCode(recyclersBean.getAuthCode(),recycler.getId());
    }

    }
