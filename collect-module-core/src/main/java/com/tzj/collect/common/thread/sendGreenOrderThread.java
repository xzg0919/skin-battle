package com.tzj.collect.common.thread;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.common.constant.MD5Util;
import com.tzj.collect.core.service.AreaService;
import com.tzj.collect.core.service.OrderItemAchService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.OrderItemAch;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

import java.text.SimpleDateFormat;
import java.util.*;

public class sendGreenOrderThread implements Runnable {


    private OrderService orderService;
    private AreaService areaService;
    private OrderItemAchService orderItemAchService;
    private Integer orderId;

    public sendGreenOrderThread(OrderService orderService,AreaService areaService,OrderItemAchService orderItemAchService,Integer orderId){
        this.orderService = orderService;
        this.areaService = areaService;
        this.orderItemAchService = orderItemAchService;
        this.orderId = orderId;
    }
    public static String getResourceNo(String categoryName){
        switch (categoryName){
            case "废玻璃":
                return "1";
            case "废金属":
                return "2";
            case "废塑料":
                return "3";
            case "废纸":
                return "4";
            case "废旧衣物":
                return "5";
            case "废衣服":
                return "5";
            default:   return "4";
        }
    }
    @Override
    public void run() {
        Map<String,Object> body = new HashMap<>();
        List<Map<String,Object>> recycleOrderList = new ArrayList<>();
        Map<String,Object> resultMap = new HashMap<>();
        Map<String,Object> head = new HashMap<>();

        Order order = orderService.selectById(orderId);
        Area street = areaService.selectById(order.getStreetId());
        Area area = areaService.selectById(order.getAreaId());
        List<OrderItemAch> orderItemAches = orderItemAchService.selectByOrderId(orderId);
        double totalWeight = orderItemAches.stream().mapToDouble(orderItemAch -> orderItemAch.getAmount()).sum();

        orderItemAches.stream().forEach(orderItemAch ->{
            Map<String,Object> recycleOrder = new HashMap<>();
            if("五废".equals(orderItemAch.getParentName())){
                recycleOrder.put("resourceNo",sendGreenOrderThread.getResourceNo(orderItemAch.getCategoryName()));
                recycleOrder.put("resourceName",orderItemAch.getCategoryName());
            }else {
                recycleOrder.put("resourceNo",sendGreenOrderThread.getResourceNo(orderItemAch.getParentName()));
                recycleOrder.put("resourceName",orderItemAch.getParentName());
            }
            recycleOrder.put("resourceUnit","kg");
            recycleOrder.put("resourceWeight",orderItemAch.getAmount());
            recycleOrderList.add(recycleOrder);
        });

        //String jiuyuUrl="https://recycletest.greenfortune.sh.cn/reclaim/api/twoNet/receiveRecycleOrderData";
        String jiuyuUrl="https://recycle.greenfortune.sh.cn/reclaimi/api/twoNet/receiveRecycleOrderData";

        String reqCode = "30004";
        head.put("reqTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        head.put("version","1.0");
        head.put("reqCode",reqCode);
        body.put("dataCollectionProvider","125");
        body.put("orderNo",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(new Random().nextInt(899999)+100000));
        body.put("orderType","生活垃圾");
        body.put("orderStatus","完成");
        body.put("orderCreateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getCreateDate()));
        body.put("orderFinishTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        body.put("orderReceiveTime",order.getReceiveTime());
        body.put("orderContact",order.getLinkMan());
        body.put("orderPhoneNum",order.getTel());
        body.put("detailAddress",order.getAddress());
        body.put("orderCreateUserPhone",order.getTel());
        body.put("orderReceiveName","上海铸乾信息科技有限公司");
        body.put("companyName","上海铸乾信息科技有限公司");
        body.put("orderReceivePhone","02180394620");
        body.put("communityCode",street.getCode());
        body.put("communityName",street.getAreaName());
        body.put("committeeCode",street.getCode());
        body.put("committeeName",street.getAreaName());
        body.put("streetCode",street.getCode());
        body.put("streetName",street.getAreaName());
        body.put("areaCode",area.getCode()+"000000");
        body.put("areaName",area.getAreaName());
        body.put("totalWeight",totalWeight);
        body.put("orderActualPoints",order.getGreenCount());
        body.put("recycleOrderList",recycleOrderList);
        resultMap.put("head",head);
        resultMap.put("body",body);
        resultMap.put("sign", MD5Util.encode(JSON.toJSONString(body)+reqCode+"eefbf3cca60830313a95283c368687e3"));
        System.out.println("请求的参数是 ："+JSON.toJSONString(resultMap));
        Response response= null;
        try {
            response = FastHttpClient.post().url(jiuyuUrl).body(JSON.toJSONString(resultMap)).build().execute();
            String resultJson=response.body().string();
            System.out.println("返回的参数是 ："+resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
