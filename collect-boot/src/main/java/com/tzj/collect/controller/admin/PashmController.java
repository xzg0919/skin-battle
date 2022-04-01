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

    @Resource
    OrderService orderService;

    @Resource
    MyslRequestLogService myslRequestLogService;

    @RequestMapping("notify")
    public Map pashmNotify(@RequestBody PashmBean pashmBean) {

        HashMap resultMap =new HashMap();
        try {
            PashmOrder pashmOrder = pashmOrderService.selectByOrderNo(pashmBean.getOrderNo());
            Order order = orderService.getByOrderNo(pashmBean.getOrderNo());

            if (pashmBean.getWeight() != null && pashmBean.getWeight() != 0) {
                pashmOrder.setWeight(pashmBean.getWeight());
            }

            if (pashmBean.getNormalClothesCount() != null) {
                pashmOrder.setNormalClothesCount(pashmBean.getNormalClothesCount());
            }

            if (pashmBean.getPashmClothesCount() != null) {
                pashmOrder.setPashmClothesCount(pashmBean.getPashmClothesCount());
            }


            if (StringUtils.isNotBlank(pashmBean.getNormalClothesImg())) {
                pashmOrder.setNormalClothesImg(pashmBean.getNormalClothesImg());
            }

            if (StringUtils.isNotBlank(pashmBean.getPashmClothesImg())) {
                pashmOrder.setPashmClothesImg(pashmBean.getPashmClothesImg());
            }

            if (pashmBean.getCode() == 0) {
                order.setReceiveTime(new Date());
                order.setStatus(Order.OrderType.ALREADY);
            }

            if (pashmBean.getCode() == 1) {
                order.setStatus(Order.OrderType.COMPLETE);
                orderService.myslOrderData(order.getId().toString());
                order.setCompleteDate(new Date());
                resultMap.put("fullEnergy",myslRequestLogService.getFullEnergyByOrderNo(order.getOrderNo()));
            }

            if (pashmBean.getCode() == 2) {
                order.setStatus(Order.OrderType.REJECTED);
                order.setCancelReason(pashmBean.getRejectReason());
                order.setCancelTime(new Date());
            }

            orderService.updateById(order);
            pashmOrderService.updateById(pashmOrder);
            resultMap.put("status","success");

        } catch (Exception e) {
            resultMap.put("status","error");
            e.printStackTrace();
        }

        return resultMap;
    }

}
