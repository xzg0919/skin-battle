package com.skin.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.TakeOrder;

import java.math.BigDecimal;
import java.util.Map;

public interface TakeOrderService extends IService<TakeOrder> {

      Page<Map<String,Object>>  userTakeOrderPage(Integer pageNum, Integer pageSize, Long userId,String orderNo);


      BigDecimal totalPrice(Long userId);


      Page<TakeOrder> getPage(Integer pageNum, Integer pageSize,String email,String nickName);


      void changeStatus(Long id,Integer status);

      BigDecimal totalPrice();

      BigDecimal totalPrice(String dateBegin,String dateEnd);

      Integer takeOrderCount(String dateBegin,String dateEnd);

      Integer takeOrderCount();

      BigDecimal totalPrice( Integer status,Integer source);

      BigDecimal totalPrice(String dateBegin,String dateEnd,Integer status,Integer source);
}
