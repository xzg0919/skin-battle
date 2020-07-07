package com.tzj.collect.controller.admin;


import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("member")
public class MemberController {




    @RequestMapping("/get/token")
    public String getAliUserId(HttpServletRequest request){
        Map<String,String> paramMap = new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Iterator iterator = parameterMap.keySet().iterator();iterator.hasNext();){
            String key = iterator.next().toString();
            String[] values = parameterMap.get(key);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            paramMap.put(key,valueStr);
            System.out.println(key+"======================"+valueStr);
        }
        return JSON.toJSONString(paramMap);
    }

}
