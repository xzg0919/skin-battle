package com.tzj.collect.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.collect.entity.GoodsProductOrder.GoodsState;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class reBackOrderController {
	

	@Autowired
	private OrderService orderService;

	/**
	 *
	 * @return
	 */
	@RequestMapping("/order/reBackOrderPage")
	public String getProductOrderList() {

		return "/admin/reBackOrderPage";
	}
	/**
	 *
	 * @return
	 */
	@RequestMapping("/order/reBackOrder")
	@ResponseBody
	public String getProductOrderList(String orderNo) {
		Order order_no = orderService.selectOne(new EntityWrapper<Order>().eq("order_no", orderNo));
		if(null == order_no){
			return "订单不存在";
		}
		if("1,2".contains(order_no.getStatus().getValue().toString())){
			order_no.setStatus(Order.OrderType.INIT);
			orderService.updateById(order_no);
			return "撤销成功";
		}else{
			return "撤销失败";
		}

	}


}
