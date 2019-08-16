package com.tzj.point.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.exception.ApiException;
import com.tzj.point.api.app.param.JfappRecyclerBean;
import com.tzj.point.entity.JfappRecycler;
import com.tzj.point.mapper.JfappRecyclerMapper;
import com.tzj.point.service.JfappRecyclerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.tzj.point.common.constant.TokenConst.*;

@Service
@Transactional(readOnly = true)
public class JfappRecyclerServiceImpl extends ServiceImpl<JfappRecyclerMapper, JfappRecycler> implements JfappRecyclerService {


    @Override
    public Object getRecycleToken(JfappRecyclerBean jfappRecyclerBean) {

        JfappRecycler jfappRecycler = this.selectOne(new EntityWrapper<JfappRecycler>().eq("tel", jfappRecyclerBean.getTel()).eq("password", jfappRecyclerBean.getPassword()));
        if (null==jfappRecycler){
            throw new ApiException("手机号或密码错误");
        }
        if(!jfappRecyclerBean.getTerminalNo().equals(jfappRecycler.getTerminalNo())){
            System.out.println("通知上个机器已在另外机器登录");
        }
        String token = JwtUtils.generateToken(jfappRecycler.getId().toString(), JFAPP_API_EXPRIRE, JFAPP_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, JFAPP_API_TOKEN_CYPTO_KEY);
        Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("token",securityToken);
            resultMap.put("jfappRecycler",jfappRecycler);
        return resultMap;
    }
}
