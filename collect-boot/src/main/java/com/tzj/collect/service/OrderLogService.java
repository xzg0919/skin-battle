
/**
* @Title: SbOrderLogService.java
* @Package com.tzj.collect.service
* @Description: 【】
* @date 2018年3月5日 下午12:29:03
* @version V1.0
* @Company: 上海挺之军科技有限公司
* @Department： 研发部
* @author:[王池][wjc2013481273@163.com]
*/

package com.tzj.collect.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.OrderLog;

/**
* @ClassName: SbOrderLogService
* @Description: 【】
* @date 2018年3月5日 下午12:29:03
* @Company: 上海挺之军科技有限公司
* @Department：研发部
* @author:[王池][wjc2013481273@163.com]
*/

public interface OrderLogService extends IService<OrderLog>{
	/**
	 * 根据订单Id查询订单日志表
	 * @author 王灿
	 * @param orderId ：订单表主键
	 * @return OrderLog
	 */
	OrderLog selectByOrderId(String orderId);
	
	/**
	 * 数据看板的订单数据
	 * @author 王灿
	 * @param id ：企业Id
	 * @param startTime : 具体某一天的时间
	 * @return Map<String,Object>
	 */
	public Map<String,Object> getOrderData(String id,String startTime);
}
