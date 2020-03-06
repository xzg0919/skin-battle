package com.tzj.collect.controller.admin;


import com.alibaba.fastjson.JSON;
import com.qimencloud.api.sceneqimen.request.AlibabaIdleRecycleQuoteGetRequest;
import com.qimencloud.api.sceneqimen.response.AlibabaIdleRecycleQuoteGetResponse;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.internal.spi.SpiUtils;
import com.taobao.api.internal.util.TaobaoUtils;
import com.taobao.api.internal.util.WebUtils;
import com.tzj.collect.core.param.xianyu.*;
import com.tzj.collect.core.service.AreaService;
import com.tzj.collect.core.service.CategoryService;
import com.tzj.collect.core.service.CompanyStreetHouseService;
import com.tzj.collect.core.service.XyCategoryOrderService;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.XyCategoryOrder;
import com.tzj.module.easyopen.exception.ApiException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static com.tzj.collect.common.utils.ToolUtils.appkey;

@RestController
@RequestMapping("xanYu")//咸鱼
public class XanYuController {

    @Autowired
    private AreaService areaService;
    @Autowired
    private CompanyStreetHouseService companyStreetHouseService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private XyCategoryOrderService xyCategoryOrderService;


    /**
     * 给咸鱼提供报价模板
     * @return
     */
    @RequestMapping("/quote/template")
    public Object getQuoteTemplate(HttpServletRequest request){
        Map<String,Object> resultMap = new  HashMap<>();
        Map<String,Object> categoryMap = new  HashMap<>();
        List<Map<String, Object>> answers = categoryService.selectXyList();
        List<XyCategory> xyCategoryList = new ArrayList<>();
                XyCategory xyCategoryName = new XyCategory();
                    xyCategoryName.setId(1L);
                    xyCategoryName.setName("选择品类");
                    xyCategoryName.setQuestionType("MULTICHOICES");
                    xyCategoryName.setRequired(true);
                    xyCategoryName.setAnswers(answers);
                XyCategory xyCategoryNum = new XyCategory();
                    xyCategoryNum.setId(2L);
                    xyCategoryNum.setName("选择重量");
                    xyCategoryNum.setQuestionType("SINGLECHOISE");
                    xyCategoryNum.setRequired(true);
                    xyCategoryNum.setAnswers(getAnswers());
                xyCategoryList.add(xyCategoryName);
                xyCategoryList.add(xyCategoryNum);
            categoryMap.put("prodName","生活垃圾回收");
            categoryMap.put("quoteType","realtime");
            categoryMap.put("spuId",1);
            categoryMap.put("questions",xyCategoryList);
            resultMap.put("template", JSON.toJSONString(categoryMap));
            resultMap.put("errCode","0");
            resultMap.put("errMessage","OK");
            resultMap.put("success",true);
            resultMap.put("supportPrepay",true);
            System.out.println(JSON.toJSONString(resultMap));
            return resultMap;
    }
    /**
     * 给咸鱼提供报价获取
     * @return
     */
    @RequestMapping("/quote/get")
    public Object getQuoteGet(HttpServletRequest request) throws Exception {
        String stream = getInputStream(request);
        return xyCategoryOrderService.insertGetQuoteGet(stream);
    }
    /**
     * 给咸鱼提供回收订单预付款查询
     * @return
     */
    @RequestMapping("/order/prepay/check")
    public Object getOrderPrepayCheck(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("creditPay",true);
        resultMap.put("creditPayAmount",0);
        resultMap.put("createOrder",true);
        resultMap.put("errCode","错误code");
        resultMap.put("errMessage","错误描述");
        resultMap.put("success",true);
        return resultMap;
    }
    /**
     * 给咸鱼提供地址支持校验
     * @return
     */
    @RequestMapping("/address/check")
    public Object getAddressCheck(HttpServletRequest request) throws Exception {
        String stream = getInputStream(request);
        Map<String,Object> resultMap = new HashMap<>();
        Long townId = 10020L;
        Area area = areaService.selectByCode(townId);
        if (null != area){
            Integer companyId = companyStreetHouseService.selectStreetHouseCompanyId(area.getId().intValue());
            if (null != companyId){
                resultMap.put("success",true);
                resultMap.put("errCode","support");
                resultMap.put("errMessage","支持");
                return resultMap;
            }
        }
        resultMap.put("success",false);
        resultMap.put("errCode","not support");
        resultMap.put("errMessage","暂不支持");
        return resultMap;
    }
    /**
     * 图书估价
     * @return
     */
    @RequestMapping("/book/price")
    public Object getBookPrice(HttpServletRequest request,XyPrepay xyPrepay){
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("price",0);
        resultMap.put("oriPrice",0);
        resultMap.put("discount","3.4折");
        resultMap.put("errCode","book_unsupport");
        resultMap.put("errMessage","暂不支持回收");
        resultMap.put("success",true);
        return resultMap;
    }

    public List<Map<String,Object>> getAnswers(){
        List<Map<String,Object>> answersList = new ArrayList<>();
            Map<String,Object> answers = new HashMap<>();
            answers.put("banned",true);
            answers.put("id",0);
            answers.put("name","5kg以下");
            answers.put("type","TEXT");
            Map<String,Object> answers1 = new HashMap<>();
            answers1.put("banned",false);
            answers1.put("id",5);
            answers1.put("name","5kg-10kg");
            answers1.put("type","TEXT");
            Map<String,Object> answers2 = new HashMap<>();
            answers2.put("banned",false);
            answers2.put("id",10);
            answers2.put("name","10kg-15kg");
            answers2.put("type","TEXT");
            Map<String,Object> answers3 = new HashMap<>();
            answers3.put("banned",false);
            answers3.put("id",15);
            answers3.put("name","15kg以上");
            answers3.put("type","TEXT");
            answersList.add(answers);
            answersList.add(answers1);
            answersList.add(answers2);
            answersList.add(answers3);
        return answersList;
    }
    public static String getInputStream(HttpServletRequest request) throws Exception {
        ServletInputStream stream = null;
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            stream = request.getInputStream();
            // 获取响应
            reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw new ApiException("读取返回支付接口数据流出现异常！");
        } finally {
            reader.close();
        }
        return sb.toString();
    }
}
