package com.tzj.collect.controller.admin;

import com.tzj.collect.core.param.business.PashmBean;
import com.tzj.collect.core.service.MyslRequestLogService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.core.service.PashmOrderService;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.PashmOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/3/16 14:08
 * @Description:
 */
@RequestMapping("pashm")
@RestController
public class PashmController {

    @Resource
    PashmOrderService pashmOrderService;


    @RequestMapping("notify")
    public Map pashmNotify(@RequestBody PashmBean pashmBean) {
        HashMap<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = pashmOrderService.savePashmOrder(pashmBean);
        } catch (Exception e) {
            resultMap.put("status", "error");
            e.printStackTrace();
        }
        return resultMap;

    }

}
