
/**
* @Title: SbOrderPicServiceImpl.java
* @Package com.tzj.collect.service.impl
* @Description: 【】
* @date 2018年3月5日 下午12:55:20
* @version V1.0
* @Company: 上海挺之军科技有限公司
* @Department： 研发部
* @author:[王池][wjc2013481273@163.com]
*/

package com.tzj.collect.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.OrderPic;
import com.tzj.collect.mapper.OrderPicMapper;
import com.tzj.collect.service.OrderPicService;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* @ClassName: SbOrderPicServiceImpl
* @Description: 【】
* @date 2018年3月5日 下午12:55:20
* @Company: 上海挺之军科技有限公司
* @Department：研发部
* @author:[王池][wjc2013481273@163.com]
*/
@Service
@Transactional(readOnly = true)
public class OrderPicServiceImpl extends ServiceImpl<OrderPicMapper, OrderPic> implements OrderPicService{
	
	/**
	 * 根据订单Id查询订单图片表
	 * @author 王灿
	 * @param orderId:订单主键
	 * @return List<OrderPic>
	 */
	@Override
	public List<OrderPic> selectbyOrderId(int orderId) {
		EntityWrapper<OrderPic> wrapper = new EntityWrapper<OrderPic>();
		wrapper.eq("order_id", orderId);
		wrapper.eq("del_flag", "0");
		return this.selectList(wrapper);
	}

}
