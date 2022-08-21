package com.skin.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.TakeOrder;

import java.math.BigDecimal;
import java.util.List;
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




      List<TakeOrder> getLastPageList();

      Page<TakeOrder> getUserPackage(Long userId,Integer pageNo,Integer pageSize);


      void userTake(Long userId,Long packageId);

      void recycle(Long userId,Long packageId);

      void recycleBatch(Long userId,String  packageIds);

      Page<TakeOrder> recentTakeOrder(Long boxId,Integer pageNo,Integer pageSize);
}
