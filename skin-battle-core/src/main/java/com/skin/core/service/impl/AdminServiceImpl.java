package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.core.mapper.AdminMapper;
import com.skin.core.service.AdminService;
import com.skin.entity.Admin;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.skin.common.constant.TokenConst.*;


@Service
@Transactional(readOnly = true)
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;




    @Override
    public Object getToken(String userCode, String password) {

        Admin admin = this.getOne(new QueryWrapper<Admin>().eq("user_code", userCode).eq("user_pswd", password).eq("del_flag", 0));
        if (null == admin) {
            throw new ApiException("用户名或密码错误");
        }
        Map<String, Object> resultMap = new HashMap<>();
        String token = JwtUtils.generateToken(admin.getId().toString(), ADMIN_API_EXPRIRE, ADMIN_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, ADMIN_API_TOKEN_CYPTO_KEY);
        resultMap.put("token", securityToken);
        resultMap.put("Admin", admin);
        return resultMap;
    }
}
