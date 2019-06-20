
/**
* @Title: SbOrderLogServiceImpl.java
* @Package com.tzj.collect.service.impl
* @Description: 【】
* @date 2018年3月5日 下午12:32:28
* @version V1.0
* @Company: 上海挺之军科技有限公司
* @Department： 研发部
* @author:[王池][wjc2013481273@163.com]
*/

package com.tzj.collect.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.OrderLog;
import com.tzj.collect.mapper.AreaMapper;
import com.tzj.collect.mapper.OrderLogMapper;
import com.tzj.collect.mapper.OrderMapper;
import com.tzj.collect.service.OrderLogService;
import com.tzj.module.common.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @ClassName: SbOrderLogServiceImpl
* @Description: 【】
* @date 2018年3月5日 下午12:32:28
* @Company: 上海挺之军科技有限公司
* @Department：研发部
* @author:[王池][wjc2013481273@163.com]
*/
@Service
@Transactional(readOnly = true)
public class OrderLogServiceImpl extends ServiceImpl<OrderLogMapper, OrderLog> implements OrderLogService{
	@Autowired
	private OrderLogMapper OrderLogMapper;
	@Autowired
	private OrderMapper Ordermapper;
	@Autowired
	private AreaMapper areaMapper;
	
	/**
	 * 根据订单Id查询订单日志表
	 * @author 王灿
	 * @param orderId ：订单表主键
	 * @return OrderLog
	 */
	@Override
	@DS("slave")
	public OrderLog selectByOrderId(String orderId) {
		EntityWrapper<OrderLog> wrapper = new EntityWrapper<OrderLog>();
		wrapper.eq("order_id", orderId);
		wrapper.eq("del_flag", "0");
		return this.selectOne(wrapper);
	}
	
	/**
	 * 数据看板的订单数据
	 * @author 王灿
	 * @param id ：企业Id
	 * @param startTime : 具体某一天的时间
	 * @return Map<String,Object>
	 */
	@Override
	@DS("slave")
	public Map<String, Object> getOrderData(String id, String startTime) {
		if(StringUtils.isBlank(startTime)) {
			Calendar date = Calendar.getInstance();
			date.setTime(new Date());
			date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
			startTime = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
		}
		//获取接单数量
		Integer alreadyNum = OrderLogMapper.getOrderData(id, startTime, "ALREADY");
		//获取完成订单数量
		Integer completeNum = OrderLogMapper.getOrderData(id, startTime, "COMPLETE");
		//获取企业完成数量和预估价格
		Order order = Ordermapper.getDataBoard(id, "3");
		//获取小区信息
		Map<String,Object> map = areaMapper.getAred(id);
		map.put("totalCompleteNum", order.getMemberId());
		map.put("priceNum", order.getCompanyId());
		map.put("alreadyNum", alreadyNum);
		map.put("completeNum", completeNum);
		return map;
	}

}
