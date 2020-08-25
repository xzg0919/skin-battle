package com.tzj.collect.controller.admin;


import com.tzj.collect.commom.redis.RedisUtil;
import com.tzj.collect.core.param.ali.UserBean;
import com.tzj.collect.entity.Member;
import com.tzj.module.api.utils.JwtUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.*;

@RestController
@RequestMapping("hardware")
public class AoTuHardwareController {


    @Resource
    private RedisUtil redisUtil;



    @RequestMapping("/theHeartbeatPacketsUpdateEquipment")
    public Object getQRCodeUrl(HttpServletRequest request, UserBean user){
        Map<String,Object> resultMap = new HashMap<>();
        Object o = redisUtil.get(user.getEquipmentCode());
        if (null == o){
            resultMap.put("respInfo",null);
            resultMap.put("respCode","0");
            return resultMap;
        }
        Member member = (Member)o;
        String token = JwtUtils.generateToken(member.getAliUserId(), ALI_API_EXPRIRE, ALI_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, ALI_API_TOKEN_CYPTO_KEY);
        resultMap.put("token",securityToken);
        resultMap.put("respCode","1");
        Map<String,Object> map = new HashMap<>();
        map.put("userPhone",member.getMobile());
        map.put("userName",member.getLinkName());
        resultMap.put("respInfo",map);
        redisUtil.del(user.getEquipmentCode());
        return resultMap;
    }



}
