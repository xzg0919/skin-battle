package com.tzj.collect.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.core.result.third.ThirdOrderResult;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.Community;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2019/8/13
 * Title: TODO
 * Description: TODO
 * Copyright: Copyright (c) 2019
 * Company: 上海铸乾信息科技有限公司
 * Department:研发部
 *
 * @author:Michael_Wang
 * @Version 1.0
 **/
@RestController
@RequestMapping("data/third")
public class Data4ThirdController {
    @Autowired
    private OrderService orderService;


    /**
     * 根据区域id 提供第三方的订单数据 暂时只支持合肥
     * @param areaId
     * @return
     */
    @RequestMapping(value = "/orderStatistics",method = RequestMethod.GET)
    public Object orderStatistics(String areaId,Integer pageNumber,Integer pageSize){
        List<Map<String,Object>> restList = new ArrayList<>();
        List<ThirdOrderResult> map = new ArrayList<>();
        map = orderService.orderStatistics4Third(areaId,pageNumber,pageSize);
        return map;
    }
}
