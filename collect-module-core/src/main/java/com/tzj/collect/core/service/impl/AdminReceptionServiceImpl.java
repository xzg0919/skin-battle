package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.AdminReceptionMapper;
import com.tzj.collect.core.service.AdminReceptionService;
import com.tzj.collect.entity.AdminReception;
import static com.tzj.common.constant.TokenConst.*;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.exception.ApiException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminReceptionServiceImpl extends ServiceImpl<AdminReceptionMapper, AdminReception> implements AdminReceptionService {

    @Override
    public Object getAdminReceptionToken(String userName, String password) {

        AdminReception adminReception = this.selectOne(new EntityWrapper<AdminReception>().eq("username", userName).eq("password", password).eq("del_flag", 0));
        if (null == adminReception) {
            throw new ApiException("用户名或密码错误");
        }
        Map<String, Object> resultMap = new HashMap<>();
        String token = JwtUtils.generateToken(adminReception.getId().toString(), ADMIN_RECEPTION_API_EXPRIRE, ADMIN_RECEPTION_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, ADMIN_RECEPTION_API_TOKEN_CYPTO_KEY);
        System.out.println("token是 : " + securityToken);
        resultMap.put("token", securityToken);
        resultMap.put("adminReception", adminReception);
        return resultMap;
    }

}
