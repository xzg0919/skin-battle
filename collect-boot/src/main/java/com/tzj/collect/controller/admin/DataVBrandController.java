package com.tzj.collect.controller.admin;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.entity.Community;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.OrderItemAch;
import com.tzj.collect.entity.RecyclersServiceRange;
import com.tzj.collect.service.CommunityService;
import com.tzj.collect.service.OrderItemAchService;
import com.tzj.collect.service.OrderService;
import com.tzj.collect.service.RecyclersServiceRangeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("datav/brand")
public class DataVBrandController {
    @Autowired
    private CommunityService communityService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RecyclersServiceRangeService recyclersServiceRangeService;
    @Autowired
    private OrderItemAchService orderItemAchService;

    @RequestMapping(value = "/get/location",method = RequestMethod.GET)
    public Object getLocation(String streetId){
       List<List<String>> lists = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        list1.add("121.4566254616");
        list1.add("31.2282064065");
        List<String> list2 = new ArrayList<>();
        list2.add("121.4525699615");
        list2.add("31.2343713523");
        List<String> list3 = new ArrayList<>();
        list3.add("121.4593023062");
        list3.add("31.2367106237");
        List<String> list4 = new ArrayList<>();
        list4.add("121.4497804642");
        list4.add("31.2429942622");
        List<String> list5 = new ArrayList<>();
        list5.add("121.4380645752");
        list5.add("31.2369720681");
        List<String> list6 = new ArrayList<>();
        list6.add("121.4434075356");
        list6.add("31.2279449379");
        List<String> list7 = new ArrayList<>();
        list7.add("121.4466369152");
        list7.add("31.2294128226");
        List<String> list8 = new ArrayList<>();
        list8.add("121.4481014013");
        list8.add("31.2241787917");

        lists.add(list1);
        lists.add(list2);
        lists.add(list3);
        lists.add(list4);
        lists.add(list5);
        lists.add(list6);
        lists.add(list7);
        lists.add(list8);
        return JSON.toJSONString(lists);
    }
    @RequestMapping(value = "/get/openCommunity",method = RequestMethod.GET)
    public Object openCommunity(String streetId){
     List<Map<String,Object>> restList = new ArrayList<>();
     Map<String,Object> map = new HashMap<>();
     int count = communityService.selectCount(new EntityWrapper<Community>().eq("area_id", streetId));
     map.put("name","");
     map.put("value",count);
     restList.add(map);
     return restList;
    }

    @RequestMapping(value = "/get/streetOrderNum",method = RequestMethod.GET)
    public Object streetOrderNum(String streetId,String status){
     List<Map<String,Object>> restList = new ArrayList<>();
     Map<String,Object> map = new HashMap<>();
     EntityWrapper<Order> wrapper = new EntityWrapper<>();
     wrapper.eq("street_id",streetId);
     wrapper.ne("company_id",1);
     if("1".equals(status)){
      wrapper.in("status_","0,1");
     }else {
      wrapper.eq("status_",status);
     }
     int count = orderService.selectCount(wrapper);
     map.put("name","");
     map.put("value",count);
     restList.add(map);
     return restList;
    }
    @RequestMapping(value = "/get/streetOrderList",method = RequestMethod.GET)
    public Object streetOrderList(String streetId,String count){
        List<Map<String,Object>> restList = null;
        Map<String,Object> map = null;
        List<Order> ordersList = orderService.selectList(new EntityWrapper<Order>().eq("street_id", streetId).eq("title", 2).eq("status_", 3).orderBy("complete_date", false).last("LIMIT 0," + count));
        if(null != ordersList){
            restList = new ArrayList<>();
            for (Order order:ordersList ) {
                map = new HashMap<>();
                String attribute = "";
                List<OrderItemAch> itemAchList = orderItemAchService.selectList(new EntityWrapper<OrderItemAch>().eq("order_id", order.getId()));
                if (null != itemAchList){
                    for (OrderItemAch orderItemAch:itemAchList) {
                        attribute += orderItemAch.getCategoryName()+",";
                    }
                }
                map.put("area",order.getLinkMan());
                map.put("pv","¥"+order.getAchPrice());
                map.put("attribute", StringUtils.isNoneBlank(attribute)?attribute.substring(0,attribute.length()-1):attribute);
                restList.add(map);
            }
        }

        return restList;
    }


    @RequestMapping(value = "/get/recyclerNum",method = RequestMethod.GET)
    public Object recyclerNum(String streetId){
     List<Map<String,Object>> restList = new ArrayList<>();
     Map<String,Object> map = new HashMap<>();
     EntityWrapper<RecyclersServiceRange> wrapper = new EntityWrapper<>();
     wrapper.eq("area_id",streetId);
     wrapper.eq("del_flag",0);
     int count = recyclersServiceRangeService.selectCount(wrapper);
     map.put("name","");
     map.put("value",count);
     restList.add(map);
     return restList;
    }

    @RequestMapping(value = "/get/orderSum",method = RequestMethod.GET)
    public Object orderSum(String streetId){
     List<Map<String,Object>> restList = new ArrayList<>();
     Map<String,Object> map = new HashMap<>();
     String count = orderItemAchService.orderSum(streetId);
     map.put("name","");
     map.put("value",count);
     restList.add(map);
     return restList;
    }

    @RequestMapping(value = "/get/orderDetialNum",method = RequestMethod.GET)
    public Object orderDetialNum(String streetId){
     List<Map<String,Object>> restList = orderItemAchService.orderDetialNum(streetId);
     return restList;
    }

    @RequestMapping(value = "/get/sevenDayorderNum",method = RequestMethod.GET)
    public Object sevenDayorderNum(String streetId){
        List<Map<String,Object>> restList = orderService.sevenDayorderNum(streetId);
        return restList;
    }

}
