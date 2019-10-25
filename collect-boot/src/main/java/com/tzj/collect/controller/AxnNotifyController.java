package com.tzj.collect.controller;


import com.alibaba.fastjson.JSON;
import com.tzj.collect.core.service.AliBindAxnService;
import com.tzj.collect.core.service.BindAxnService;
import org.apache.commons.lang3.StringUtils;
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
 * 电话关闭通知
 */
@Controller
@RequestMapping(value = "/axn/notify")
public class AxnNotifyController {

    @Autowired
    private AliBindAxnService aliBindAxnService;
    @Autowired
    private BindAxnService bindAxnService;


    @RequestMapping(value = "/alipay")
    public void aliPayAxnNotify(HttpServletRequest request) {


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
        System.out.println(JSON.toJSONString(params));
        String sub_id = params.get("sub_id");
        String secret_no = params.get("secret_no");
        String phone_no_a = params.get("phone_no");
        String phone_no_b = params.get("peer_no");
        if (!StringUtils.isBlank(sub_id)&&!StringUtils.isBlank(secret_no)){
            aliBindAxnService.deleteAnxPhone(sub_id,secret_no);
            bindAxnService.updateBySubsId(sub_id,secret_no);
        }
    }

}
