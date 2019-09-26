package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.ArrivalTimeLogMapper;
import com.tzj.collect.core.service.ArrivalTimeLogService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.ArrivalTimeLog;
import com.tzj.collect.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;


@Service
public class ArrivalTimeLogServiceImpl extends ServiceImpl<ArrivalTimeLogMapper, ArrivalTimeLog> implements ArrivalTimeLogService {
	@Autowired
	private ArrivalTimeLogMapper arrivalTimeLogMapper;
	@Autowired
	private OrderService orderService;
	
	/**
     * 修改上门回收时间 
     * @author wangcan
     * @param orderId : 订单Id 
     * @param afterDate : 修改后的时间
     * @param afterPeriod : 修改后的 上午am 下午pm
     * @return
     */
	@Override
	@Transactional
	public String sendArrivalTimeLog(Integer orderId,String afterDate,String afterPeriod,String cancleDesc) {
		 //查询订单日志表的数据
		 int arrivalTimeCount = this.selectCount(new EntityWrapper<ArrivalTimeLog>().eq("order_id", orderId));
		 //根据订单Id查询订单表的数据
		 Order order = orderService.selectById(orderId);
		 ArrivalTimeLog arrivalTimeLogs = new ArrivalTimeLog();
		 arrivalTimeLogs.setNum(arrivalTimeCount);
		arrivalTimeLogs.setOrderId(orderId);
		arrivalTimeLogs.setCancleDesc(cancleDesc);
		arrivalTimeLogs.setBeforeDate(order.getArrivalTime());
		arrivalTimeLogs.setBeforePeriod(order.getArrivalPeriod());
		arrivalTimeLogs.setAfterPeriod(afterPeriod);
		try {
			arrivalTimeLogs.setAfterDate(new SimpleDateFormat("yyyy-MM-dd").parse(afterDate));
			order.setArrivalTime(new SimpleDateFormat("yyyy-MM-dd").parse(afterDate));
			order.setArrivalPeriod(afterPeriod);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			arrivalTimeLogMapper.insert(arrivalTimeLogs);
			orderService.updateById(order);
		} catch (Exception e) {
			return "修改回收上门时间失败";	
		}
		 return "修改回收上门时间成功";
	}
}
