package com.skin.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.TakeOrder;

import java.math.BigDecimal;
import java.util.Map;

public interface TakeOrderService extends IService<TakeOrder> {

      Page<Map<String,Object>>  userTakeOrderPage(Integer pageNum, Integer pageSize, Long userId,String orderNo);


    BigDecimal totalPrice(Long userId);
}
