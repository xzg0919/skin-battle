package com.tzj.point.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.exception.ApiException;
import com.tzj.collect.core.param.jfapp.JfappRecyclerBean;
import com.tzj.collect.common.utils.PushUtils;
import com.tzj.collect.entity.JfappRecycler;
import com.tzj.point.mapper.JfappRecyclerMapper;
import com.tzj.collect.core.service.JfappRecyclerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.*;


@Service
@Transactional(readOnly = true)
public class JfappRecyclerServiceImpl extends ServiceImpl<JfappRecyclerMapper, JfappRecycler> implements JfappRecyclerService {


    @Override
    @Transactional
    public Object getRecycleToken(JfappRecyclerBean jfappRecyclerBean) {

        JfappRecycler jfappRecycler = this.selectOne(new EntityWrapper<JfappRecycler>().eq("tel", jfappRecyclerBean.getTel()).eq("password", jfappRecyclerBean.getPassword()));
        if (null==jfappRecycler){
            throw new ApiException("手机号或密码错误");
        }
        if(!jfappRecyclerBean.getTerminalNo().equals(jfappRecycler.getTerminalNo())){
            PushUtils.getAcsResponse(jfappRecycler.getTerminalNo());
        }
        jfappRecycler.setTerminalNo(jfappRecyclerBean.getTerminalNo());
        this.updateById(jfappRecycler);
        String token = JwtUtils.generateToken(jfappRecycler.getId().toString(), JFAPP_API_EXPRIRE, JFAPP_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, JFAPP_API_TOKEN_CYPTO_KEY);
        Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("token",securityToken);
            resultMap.put("jfappRecycler",jfappRecycler);
        return resultMap;
    }
}
