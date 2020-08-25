package com.tzj.collect.controller.admin;


import com.taobao.api.ApiException;
import com.tzj.collect.common.utils.ToolUtils;
import com.tzj.collect.core.service.CompanyEquipmentService;
import com.tzj.collect.core.service.MemberService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.Member;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.utils.security.CipherTools;
import io.jsonwebtoken.Claims;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.*;

/**
 * 奥图IOT设备机器
 */
@RestController
@RequestMapping("equipment")
public class AoTuIotController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CompanyEquipmentService companyEquipmentService;

    @RequestMapping("/memberLogin")
    public Object memberLogin(HttpServletRequest request,User user){
        Map<String,Object> resultMap = new HashMap<>();
        String aliUserId = null;
        try {
            aliUserId = ToolUtils.getAliUserIdByOrderNo(user.getQrCode());
        }catch (Exception e){
            resultMap.put("msg","登录失败");
            resultMap.put("status","400");
            return resultMap;
        }
        Member member = memberService.selectMemberByAliUserId(aliUserId);
        if (null == member){
            resultMap.put("msg","登录失败");
            resultMap.put("status","400");
            return resultMap;
        }
        user.setUserPhone(member.getMobile());
        user.setUserName(member.getLinkName());
        user.setUserImg(member.getPicUrl());
        String token = JwtUtils.generateToken(member.getAliUserId(), ALI_API_EXPRIRE, ALI_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, ALI_API_TOKEN_CYPTO_KEY);
        user.setToken(securityToken);
        resultMap.put("msg","登录成功");
        resultMap.put("status","0");
        resultMap.put("user",user);
        return resultMap;

    }
    @RequestMapping("/submitWeight")
    public Object submitWeight(HttpServletRequest request,User user){
        String aliUserId = null;
        try{
            String key = CipherTools.initKey(ALI_API_TOKEN_CYPTO_KEY);
            String decodeToken = CipherTools.decrypt(user.getToken(), key);
            Claims claims = JwtUtils.getClaimByToken(decodeToken, ALI_API_TOKEN_SECRET_KEY);
            aliUserId = claims.getSubject();
        }catch (Exception e){
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("respCode","1");
            resultMap.put("respInfo",0);
            return resultMap;
        }
        return orderService.uploadCategoryByAoTu(aliUserId,user.getEquipmentCode(),User.getCategoryNameById(user.getRubbishId()),user.getRubbishWeight());
    }
    @RequestMapping("/upLoadEquipmentCoordinates")
    public Object uploadEquipmentCoordinates(HttpServletRequest request,User user){
        Map<String, String> resultMap = new HashMap<>();
        try{
            companyEquipmentService.uploadEquipmentCoordinates(1,user.getEquipmentCode(),user.getEquipmentLongitude(),user.getEquipmentLatitude());
        }catch (Exception e){
            e.printStackTrace();
        }
        resultMap.put("respCode","0");
        resultMap.put("respInfo",null);
        return resultMap;
    }

    public Map<String,String> getRequestMap(HttpServletRequest request){
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        return params;
    }

}
@Data
class User{
    private String userImg ;
    private String userPhone;
    private String userName;
    private String token;
    private String qrCode;
    private String equipmentCode;
    private Integer rubbishId;
    private Double rubbishWeight;
    private Double equipmentLongitude;
    private Double equipmentLatitude;

    public static String getCategoryNameById(Integer categoryId){
        String categoryName = null;
        switch (categoryId){
            case 101:   categoryName = "废纸";
                break;
            case 102:   categoryName = "废塑料";
                break;
            case 103:   categoryName = "废纺织品";
                break;
            case 104:   categoryName = "废金属";
                break;
        }
        return categoryName;
    }

}
