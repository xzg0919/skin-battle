
package com.tzj.collect.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tzj.collect.controller.admin.XanYuController;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Aspect
@Component
public class XianyuSignAspect {


    @Pointcut("@annotation(com.tzj.collect.annotation.XianyuSign)")
    public void XianyuSign(){}


    /**
     * api日志处理
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("XianyuSign()")
    public Object checkAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Date requestTime = new Date();
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request=(HttpServletRequest)args[0];
        //String inputStream = XanYuController.getInputStream(request);

        Map requestParams = new HashMap();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Iterator iter = parameterMap.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) parameterMap.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            requestParams.put(name, valueStr);
        }
        requestParams.put("value","123456");
        args =new Object[2];
        args[0] = requestParams;
       // System.out.println(inputStream);
       Object response   =   joinPoint.proceed(args);
       return response;
    }



}

