package com.tzj.collect.config;


import com.alibaba.fastjson.JSON;
import com.taobao.api.internal.spi.SpiUtils;
import com.taobao.api.internal.util.StringUtils;
import com.taobao.api.internal.util.TaobaoUtils;
import com.taobao.api.internal.util.WebUtils;
import com.tzj.collect.controller.admin.XanYuController;
import com.tzj.module.easyopen.exception.ApiException;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.*;

import static com.taobao.api.internal.spi.SpiUtils.*;
import static com.tzj.module.api.annotation.CheckSign.json;

@Component
public class XyInterceptor  implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {


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
            System.out.println(name + " : " +valueStr );
        }
        request.setAttribute("value","123456789");
        System.out.println("进入方法之前");
        this.checkSign(request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        System.out.println("返回参数之前");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        System.out.println("返回参数之后");

    }



    public Map<String,Object> getResult(Map<String,Object> map){
        map.put("flag","false");
        map.put("errCode","sign-check-failure");
        map.put("errMessage","Illegal request");
        map.put("sub_code","sign-check-failure");
        map.put("sub_message","Illegal request");
        System.out.println(JSON.toJSONString(map));
        return map;
    }

    public boolean checkSign(HttpServletRequest request) throws Exception {
        boolean a = false;
        boolean b = false;
        boolean c = false;
        String secret = "6b8fb4524e18b154851fa46c67768ef3";
        try {
            String inputStream = XanYuController.getInputStream(request);
            Map<String, Object> objectMap = (Map<String, Object>) JSONObject.fromObject(inputStream);
            Map requestParams = new HashMap();
            for (Iterator iter = objectMap.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String values = objectMap.get(name)+"";
                //乱码解决，这段代码在出现乱码时使用。
                //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
                requestParams.put(name,values);
            }
            c = SpiUtils.checkSign4TextRequest(request, requestParams.get("body")+"",secret);
            b = SpiUtils.checkSign4FileRequest(request,requestParams,secret);
            a = SpiUtils.checkSign4FormRequest(request, secret);//这里执行验签逻辑

            System.out.println("验签淘宝奇门的签名 a："+a+"  b: "+b+"  c: "+c);
        }catch (Exception e){
            e.printStackTrace();
        }
        return a;
    }
    private static String sign(Map<String, String> params, String body, String secret, String charset) throws IOException {
        StringBuilder sb = new StringBuilder(secret);
        sb.append(getParamStrFromMap(params));
        if (body != null) {
            sb.append(body);
        }

        sb.append(secret);
        byte[] bytes = TaobaoUtils.encryptMD5(sb.toString().getBytes(charset));
        return TaobaoUtils.byte2hex(bytes);
    }
    private static String getParamStrFromMap(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            String[] keys = (String[])params.keySet().toArray(new String[0]);
            Arrays.sort(keys);

            for(int i = 0; i < keys.length; ++i) {
                String name = keys[i];
                if (!"sign".equals(name)) {
                    sb.append(name);
                    sb.append((String)params.get(name));
                }
            }
        }
        return sb.toString();
    }

}
