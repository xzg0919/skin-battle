package com.tzj.collect.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.core.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 会员取消授权通知
 */
@Controller
@RequestMapping(value = "/member/notify")
public class MemberNotifyController {
    @Autowired
    private MemberService memberService;

    /**
     *  会员取消授权通知
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/alipay")
    public @ResponseBody
    String aliPayNotify(HttpServletRequest request, ModelMap model) {
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
        try{
            System.out.println(JSON.toJSONString(params));
            String notify = "app_id="+params.get("app_id")+"&biz_content="+params.get("biz_content")+"&charset="+params.get("charset")+"&msg_method="+params.get("msg_method")+"&notify_id="+params.get("notify_id")+"&utc_timestamp="+params.get("utc_timestamp")+"&version="+params.get("version");
            boolean flags = AlipaySignature.rsaCheck(notify,params.get("sign"), AlipayConst.ali_public_key,params.get("charset"),params.get("sign_type"));
            //boolean flag = AlipaySignature.rsaCheckV1(params, AlipayConst.ali_public_key, params.get("charset"), params.get("sign_type"));
            if (flags){
                //验签成功后//更新用户信息
                String bizContent = params.get("biz_content");
                Map<String,Object> map = (Map<String,Object>)JSONObject.parse(bizContent);
                memberService.deleteMemberByAliUserId(map.get("user_id").toString());
            }else {
                return "fail";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }




}
