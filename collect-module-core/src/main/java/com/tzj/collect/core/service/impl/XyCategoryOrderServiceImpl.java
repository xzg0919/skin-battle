package com.tzj.collect.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.XyCategoryOrderMapper;
import com.tzj.collect.core.service.XyCategoryOrderService;
import com.tzj.collect.entity.XyCategoryOrder;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class XyCategoryOrderServiceImpl extends ServiceImpl<XyCategoryOrderMapper, XyCategoryOrder> implements XyCategoryOrderService {


    @Override
    public Object insertGetQuoteGet(String stream) {
        Map<String, Object> objectMap = (Map<String, Object>) JSONObject.fromObject(stream);
        Integer quoteId = Integer.parseInt(objectMap.get("quoteId")+"");
        Map<String, Object> map = (Map<String, Object>) JSONObject.fromObject(objectMap.get("questionnaire"));
        List<Map<String,Object>> questions = (List<Map<String,Object>>)map.get("questions");
        questions.stream().forEach(s->{
            List<Map<String, Object>> answers = (List<Map<String, Object>>) s.get("answers");
            answers.stream().forEach(m->{
                XyCategoryOrder xyCategoryOrder = new XyCategoryOrder();
                xyCategoryOrder.setQuoteId(quoteId);
                xyCategoryOrder.setParentId(Integer.parseInt(s.get("id")+""));
                xyCategoryOrder.setParentName(s.get("name")+"");
                xyCategoryOrder.setCategoryId(Integer.parseInt(m.get("id")+""));
                xyCategoryOrder.setCategoryName(m.get("name")+"");
                this.insert(xyCategoryOrder);
            });
        });
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("price",1);
        resultMap.put("quoteId",quoteId);
        resultMap.put("spuId",objectMap.get("spuid"));
        resultMap.put("success",true);
        resultMap.put("errCode","0");
        resultMap.put("errMessage","OK");
        System.out.println(JSON.toJSONString(resultMap));
        return resultMap;
    }
}
