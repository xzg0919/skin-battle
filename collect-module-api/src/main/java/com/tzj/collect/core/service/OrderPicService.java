
/**
* @Title: SbOrderPicService.java
* @Package com.tzj.collect.service
* @Description: 【】
* @date 2018年3月5日 下午12:53:47
* @version V1.0
* @Company: 上海挺之军科技有限公司
* @Department： 研发部
* @author:[王池][wjc2013481273@163.com]
*/

package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.OrderPic;

import java.util.List;


/**
* @ClassName: SbOrderPicService
* @Description: 【】
* @date 2018年3月5日 下午12:53:47
* @Company: 上海挺之军科技有限公司
* @Department：研发部
* @author:[王池][wjc2013481273@163.com]
*/

public interface OrderPicService extends IService<OrderPic>{
	
	/**
	 * 根据订单Id查询订单图片表
	 * @author 王灿
	 * @param orderId:订单主键
	 * @return List<OrderPic>
	 */
	@DS("slave")
	List<OrderPic> selectbyOrderId(int orderId);
}
