package com.tzj.collect.api.app;

import com.tzj.collect.commom.redis.RedisUtil;
import com.tzj.collect.common.util.RecyclersUtils;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.result.app.AppOrderResult;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.easyopen.ApiContext;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.tzj.collect.common.constant.TokenConst.APP_API_COMMON_AUTHORITY;

/**
 * 订单接口列表
 * @author Michael_Wang
 *
 */
@ApiService
public class AppOrderApi {
	@Autowired
	private OrderService orderService;
	@Autowired
	private AsyncService asyncService;
	@Autowired
	private OrderPicService orderPicService;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private RecyclersService recyclersService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private RedisUtil redisUtil;
	@Resource(name = "mqtt4PushOrder")
	private MqttClient mqtt4PushOrder;
	
	// 接口里面获取 Recyclers
	public Recyclers getRecycler() {
		Subject subject=ApiContext.getSubject();;
		Recyclers recyclers = (Recyclers) subject.getUser();
		return recyclers;
	}
	/**
	 * 回收人员清运订单列表
	 * @author: sgmark@aliyun.com
	 * @Date: 2019/12/11 0011
	 * @Param:
	 * @return:
	 */
	@Api(name = "app.order.list", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Map<String, Object> orderList(OrderBean orderBean){
		return orderService.selectIotRecList(RecyclersUtils.getRecycler().getId(), orderBean.getStatus(), orderBean.getPagebean());
	}
	/**
	 * 根据订单传来的状态获取订单列表
	 */
	@Api(name = "app.order.getorderlist", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Map<String,Object> getOrderList(OrderBean orderbean){
		orderbean.setRecyclerId(Integer.valueOf(this.getRecycler().getId().toString()));
		Map<String,Object> pageOrder = orderService.getAppOrderList(orderbean);
		return pageOrder;
	}
	/**
	 * 根据订单传来的状态获取订单列表
	 */
	@Api(name = "app.order.list.phone", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Map<String,Object> getOrderListByPhone(OrderBean orderbean){
		orderbean.setRecyclerId(Integer.valueOf(this.getRecycler().getId().toString()));
		Map<String,Object> pageOrder = orderService.getOrderListByPhone(orderbean);
		return pageOrder;
	}
	/**
	 * 得到订单详情
	 * @param orderbean
	 * @return
	 */
	@Api(name = "app.order.getorderdetails", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public AppOrderResult getOrderDetails(OrderBean orderbean){
		orderbean.setRecyclerId(Integer.valueOf(this.getRecycler().getId().toString()));
		AppOrderResult pageOrder = orderService.getOrderDetails(orderbean);
		return pageOrder;
	}
	
	/**
	 * 根据orderId 或者orderNo 修改 订单修改上门时间
	 */
	@Api(name = "app.order.modify", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public boolean modify(OrderBean orderBean){
		orderBean.setRecyclerId(Integer.valueOf(this.getRecycler().getId().toString()));
		return orderService.modifyOrder(orderBean);
	}
	/**
	 * 修改狀態通用接口(取消，完成，已接單)
	 * @param orderBean
	 * @return
	 */
	@Api(name = "app.order.modifyallsta", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public boolean modifyOrderSta(OrderBean orderBean){
		orderBean.setRecyclerId(Integer.valueOf(this.getRecycler().getId().toString()));
		return orderService.modifyOrderSta(orderBean,mqtt4PushOrder);
	}
	/**
	 * 回收员确认上传订单
	 * @param orderBean
	 * @return
	 */
	@Api(name = "app.order.savebyrecy", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public boolean saveByRecy(OrderBean orderBean) {
		System.out.println("进完成接口了");
		return orderService.saveByRecy(orderBean);
	}

	@Api(name = "app.order.getCommissionsPriceByOrderId", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object getCommissionsPriceByOrderId(OrderBean orderBean) {
		return orderService.getCommissionsPriceByOrderId(orderBean);
	}
	/**
	 * 返回回收人员支付的价格
	 * @param orderBean
	 * @return
	 */
	@Api(name = "app.order.getPriceByOrderId", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object getPriceByOrderId(OrderBean orderBean) {
		return orderService.getPriceByOrderId(orderBean,mqtt4PushOrder);
	}
	/**
	 * 回收员订单重量提交信息
	 * @param orderBean
	 * @return
	 */
	@Api(name = "app.order.editByAdminOrder", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object editByAdminOrder(OrderBean orderBean) {
		return orderService.editByAdminOrder(orderBean);
	}
	
	/**
	 * 扫描用户会员卡卡号完成订单
	 * @author 王灿
	 * @param orderBean
	 * @return
	 */
	@Api(name = "app.order.saveOrderByCardNo", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object saveOrderByCardNo(OrderBean orderBean) {
		Recyclers recycler = recyclersService.selectById(RecyclersUtils.getRecycler());
		return orderService.saveOrderByCardNo(orderBean,recycler);
	}
	/**
	 * 回收经理转派订单
	 * @author 王灿
	 * @param orderBean
	 * @return
	 */
	@Api(name = "app.order.distributeOrder", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object distributeOrder(OrderBean orderBean) {
		return orderService.distributeOrder(orderBean.getId(),orderBean.getRecyclerId());
	}
	/**
	 * 转派订单列表
	 * @author 王灿
	 * @return
	 */
	@Api(name = "app.order.distributeOrderList", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object distributeOrderList() {
		Recyclers recycler = recyclersService.selectById(RecyclersUtils.getRecycler());
		List<Map<String,Object>> orderList = (List<Map<String,Object>>)orderService.distributeOrderList(recycler.getId().intValue());
		for (Map<String,Object> map:orderList) {
			Category category = null;
			if(map.get("categoryId")!=null){
				category = categoryService.selectById(map.get("categoryId") + "");
			}
			List<OrderPic> orderPicList = orderPicService.selectbyOrderId((int) map.get("id"));
			List<OrderItem> orderItemList = orderItemService.selectByOrderId((int) map.get("id"));
			String itemName = "";
			if(!orderItemList.isEmpty()){
				for (OrderItem orderItem:orderItemList){
					if(!itemName.equals(orderItem.getCategoryName()+"/")){
						itemName += orderItem.getCategoryName()+"/";
					}
				}
			}
			map.put("category",category);
			map.put("orderPicList",orderPicList);
			map.put("itemName",itemName.substring(0,itemName.length()-1));
		}
		return orderList;
	}
	/**
	 * 回收经理转派订单
	 * @author 王灿
	 * @return
	 */
	@Api(name = "app.order.pullCollectMoney", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object pullCollectMoney(OrderBean orderBean) {
		Order order = orderService.selectById(orderBean.getId());
		Object o = redisUtil.get(order.getOrderNo());
		if (null!=o){
			return "您已提醒用户，请在12小时以后再次点击提醒";
		}
		if (null == order){
			return "找不到该订单";
		}
		redisUtil.set(order.getOrderNo(), UUID.randomUUID(),60*60*12);
		return "操作成功";
	}

	/**
	 * 回收经理撤销订单
	 * @author 王灿
	 * @return
	 */
	@Api(name = "app.order.recallOrder", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object recallOrder(OrderBean orderBean) {
		Recyclers recycler = recyclersService.selectById(RecyclersUtils.getRecycler());
		return orderService.recallOrder(orderBean.getId(),recycler.getId());
	}


}

