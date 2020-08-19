
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

package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.OrderOperateMapper;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.service.OrderOperateService;
import com.tzj.collect.entity.OrderOperate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class OrderOperateServiceImpl extends ServiceImpl<OrderOperateMapper, OrderOperate> implements OrderOperateService {
	@Autowired
	private OrderOperateMapper orderOperateMapper;

	@Override
	public List<OrderOperate> selectOperate(OrderBean orderbean) {
		return orderOperateMapper.selectList(new EntityWrapper<OrderOperate>().eq("order_no",orderbean.getOrderNo()).orderBy("create_date",false));
	}
}
