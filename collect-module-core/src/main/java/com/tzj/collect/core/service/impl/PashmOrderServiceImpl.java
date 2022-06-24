package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.AdminMapper;
import com.tzj.collect.core.mapper.PashmOrderMapper;
import com.tzj.collect.core.param.business.PashmBean;
import com.tzj.collect.core.service.AdminService;
import com.tzj.collect.core.service.MyslRequestLogService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.core.service.PashmOrderService;
import com.tzj.collect.entity.Admin;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.PashmOrder;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.*;

@Service
@Transactional(readOnly = true)
public class PashmOrderServiceImpl extends ServiceImpl<PashmOrderMapper, PashmOrder> implements PashmOrderService {



    @Override
    public PashmOrder selectByOrderNo(String orderNo){
        return this.selectOne(new EntityWrapper<PashmOrder>().eq("order_no",orderNo));
    }

    @Resource
    PashmOrderService pashmOrderService;

    @Resource
    OrderService orderService;

    @Resource
    MyslRequestLogService myslRequestLogService;


    @Transactional
    @Override
    public HashMap<String,Object> savePashmOrder(PashmBean pashmBean) {

        HashMap<String,Object> resultMap =new HashMap();
        PashmOrder pashmOrder = pashmOrderService.selectByOrderNo(pashmBean.getOrderNo());
        Order order = orderService.getByOrderNo(pashmBean.getOrderNo());

        // 羊绒只有一个环境 正式测试会存在订单数据不一致
        if(order ==null){
            resultMap.put("status","success");
            return resultMap;
        }
        if(order.getStatus().equals(Order.OrderType.COMPLETE) || order.getStatus().equals(Order.OrderType.REJECTED)){
            resultMap.put("status","success");
            return resultMap;
        }
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

        if (pashmBean.getCode() == 1) {
            orderService.myslOrderData(order.getId().toString());
            resultMap.put("fullEnergy",myslRequestLogService.getFullEnergyByOrderNo(order.getOrderNo()));
            //重新获取order
            order = orderService.getByOrderNo(pashmBean.getOrderNo());
            order.setStatus(Order.OrderType.COMPLETE);
            order.setCompleteDate(new Date());
        }


        if (pashmBean.getCode() == 0) {
            order.setReceiveTime(new Date());
            order.setStatus(Order.OrderType.ALREADY);
        }

        if (pashmBean.getCode() == 3) {
            try {
                order.setArrivalTime(new SimpleDateFormat("yyyy-MM-dd").parse(pashmBean.getArrivalTime()));
                pashmOrder.setStartTime(pashmBean.getStartTime());
                pashmOrder.setEndTime(pashmBean.getEndTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        if (pashmBean.getCode() == 2) {
            order.setStatus(Order.OrderType.REJECTED);
            order.setCancelReason(pashmBean.getRejectReason());
            order.setCancelTime(new Date());
        }

        orderService.updateById(order);
        pashmOrderService.updateById(pashmOrder);
        resultMap.put("status","success");
        return resultMap;
    }
}
