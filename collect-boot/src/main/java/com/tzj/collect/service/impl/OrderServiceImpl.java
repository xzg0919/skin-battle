package com.tzj.collect.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.domain.AntMerchantExpandTradeorderSyncModel;
import com.alipay.api.domain.ItemOrder;
import com.alipay.api.domain.OrderExtInfo;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AntMerchantExpandTradeorderSyncResponse;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.ali.param.IdAmountListBean;
import com.tzj.collect.api.ali.param.OrderBean;
import com.tzj.collect.api.ali.param.OrderItemBean;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.collect.api.ali.result.ComCatePrice;
import com.tzj.collect.api.ali.result.CommToken;
import com.tzj.collect.api.app.param.ScoreAppBean;
import com.tzj.collect.api.app.param.TimeBean;
import com.tzj.collect.api.app.result.AppOrderResult;
import com.tzj.collect.api.app.result.AppScoreResult;
import com.tzj.collect.api.app.result.AttrItem;
import com.tzj.collect.api.app.result.EvaluationResult;
import com.tzj.collect.api.business.param.BOrderBean;
import com.tzj.collect.api.business.param.CompanyBean;
import com.tzj.collect.api.business.result.ApiUtils;
import com.tzj.collect.api.business.result.CancelResult;
import com.tzj.collect.api.commom.redis.RedisUtil;
import com.tzj.collect.api.common.async.AsyncRedis;
import com.tzj.collect.api.common.websocket.XcxWebSocketServer;
import com.tzj.collect.api.iot.param.IotParamBean;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.common.thread.NewThreadPoorExcutor;
import com.tzj.collect.common.thread.sendGreenOrderThread;
import com.tzj.collect.common.util.PushUtils;
import com.tzj.collect.common.util.SnUtils;
import com.tzj.collect.common.util.ToolUtils;
import com.tzj.collect.entity.*;
import com.tzj.collect.entity.Category.CategoryType;
import com.tzj.collect.entity.Order.OrderType;
import com.tzj.collect.mapper.OrderMapper;
import com.tzj.collect.service.*;
import com.tzj.collect.service.impl.XingeMessageServiceImp.XingeMessageCode;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 订单ServiceImpl
 * @Author 王灿
 **/
@Service
@Transactional
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

	@Autowired
	private OrderService orderService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CategoryAttrOptionService categoryAttrOptionService;
	@Autowired
	private CategoryAttrService categoryAttrService;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private OrderItemAchService orderItemAchService;
	@Autowired
	private OrderPicService orderPicService;
	@Autowired
	private OrderPicAchService orderPicAchService;
	@Autowired
	private OrderLogService orderLogService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private OrderEvaluationService orderEvaluationService;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private XingeMessageService xingeService;
	@Autowired
	private MemberAddressService memberAddressService;
	@Autowired
	private RecyclerCancelLogService recyclerCancelLogService;
	@Autowired
	private AliPayService aliPayService;
	@Autowired
	private PointService pointService;
	@Autowired
	private PointListService pointListService;
	@Autowired
	private CompanyCategoryService comCatePriceService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private CommunityService communityService;
	@Autowired
	private RecyclersService recyclersService;
	@Autowired
	private AsyncService asyncService;
	@Autowired
	private CompanyRecyclerService companyRecyclerService;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private EnterpriseCodeService enterpriseCodeService;
	@Autowired
	private PiccWaterService piccWaterService;
	@Autowired
	private CompanyCategoryService companyCategoryService;
	@Autowired
	private CompanyStreeService companyStreeService;
	@Autowired
	private CompanyEquipmentService companyEquipmentService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private XcxWebSocketServer xcxWebSocketServer;
	@Autowired
	private AnsycMyslService ansycMyslService;
	@Autowired
	private AsyncRedis asyncRedis;
	@Autowired
	private SendRocketmqMessageService sendRocketmqMessageService;
	@Autowired
	private CompanyCategoryCityService companyCategoryCityService;
	@Autowired
	private AreaService areaService;

	@Override
	public Order getLastestOrderByMember(Integer memberId) {
		return selectOne(new EntityWrapper<Order>().eq("member_id", memberId).orderBy("complete_date", false).last("LIMIT 1"));
	}

	/**
	 * 获取会员的订单列表 分页
	 *
	 * @param memberId :会员表主键
	 * @param num      : 第几页
	 * @param size     : 共多少条
	 * @return
	 * @author 王灿
	 */
	@Override
	public Map<String, Object> getOrderlist(long memberId,Integer status, int num, int size) {
		EntityWrapper<Order> wrapper = new EntityWrapper<Order>();
		wrapper.eq("member_id", memberId);
		wrapper.eq("del_flag", "0");
		wrapper.orderBy("create_date", false);
		wrapper.eq("status_",status);
		int i = this.selectCount(wrapper);
		Map<String, Object> map = new HashMap<String, Object>();
		List<Order> listOrder = orderMapper.getOrderlist((int) memberId,status, (num - 1) * size, size);
		listOrder = this.createName4Ali(listOrder);
		map.put("pageNum", num);
		map.put("count", i);
		map.put("listOrder", listOrder);
		return map;
	}

	/**
	 * 下单接口
	 * 该接口参数复杂 待定中...
	 *
	 * @param orderbean : 订单参数实体
	 * @return long
	 */
	@Transactional
	@Override
	public Map<String,Object> saveOrder(OrderBean orderbean) {
		Map<String,Object> resultMap = new HashMap<>();
		boolean flag = false;
		//获取分类的所有父类编号
		Category category = null;
		EnterpriseCode enterpriseCode = null;
		if (CategoryType.DIGITAL.name().equals(orderbean.getTitle())) {
			category = categoryService.selectById(orderbean.getCategoryId());
			orderbean.setCategoryParentIds(category.getParentIds());
		}
		Order order = new Order();
		try {
			if (StringUtils.isNotBlank(orderbean.getArrivalTime())) {
				order.setArrivalTime(new SimpleDateFormat("yyyy-MM-dd").parse(orderbean.getArrivalTime()));
				order.setArrivalPeriod(orderbean.getArrivalPeriod());
			} else {
				Date date = Calendar.getInstance().getTime();
				int i = Integer.parseInt(new SimpleDateFormat("HH").format(date));
				order.setArrivalTime(date);
				if (i < 12) {
					order.setArrivalPeriod("am");
				} else {
					order.setArrivalPeriod("pm");
				}
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		try {
			order.setMemberId(orderbean.getMemberId());
			order.setCompanyId(orderbean.getCompanyId());
			order.setRecyclerId(orderbean.getRecyclerId());
			order.setOrderNo(orderbean.getOrderNo());
			order.setAreaId(orderbean.getAreaId());
			order.setStreetId(orderbean.getStreetId());
			order.setCommunityId(orderbean.getCommunityId());
			order.setAddress(orderbean.getAddress());
			order.setFullAddress(orderbean.getFullAddress());
			order.setTel(orderbean.getTel());
			order.setLinkMan(orderbean.getLinkMan());
			order.setCategoryId(orderbean.getCategoryId());
			order.setCategoryParentIds(orderbean.getCategoryParentIds());
			order.setIsMysl(orderbean.getIsMysl());
			BigDecimal price = new BigDecimal("0");
			if(price.compareTo(orderbean.getPrice())==1){
				order.setPrice(price);
			}else{
				order.setPrice(orderbean.getPrice());
			}
			order.setUnit(orderbean.getUnit());
			order.setQty(orderbean.getQty());
			order.setLevel(orderbean.getLevel());

			order.setGreenCode(orderbean.getGreenCode());
			order.setAliUserId(orderbean.getAliUserId());
			order.setRemarks(orderbean.getRemarks());
			if (Order.TitleType.DIGITAL.name().equals(orderbean.getTitle())) {
				order.setTitle(Order.TitleType.DIGITAL);
			} else if (Order.TitleType.HOUSEHOLD.name().equals(orderbean.getTitle())) {
				order.setTitle(Order.TitleType.HOUSEHOLD);
			} else {
				order.setTitle(Order.TitleType.DEFUALT);
			}
			if ("1".equals(orderbean.getIsCash())) {
				order.setIsCash(orderbean.getIsCash());
			} else {
				order.setIsCash("0");
			}
			//判断是否有券码
			if(!StringUtils.isBlank(orderbean.getEnterpriseCode())){
				enterpriseCode = enterpriseCodeService.selectOne(new EntityWrapper<EnterpriseCode>().eq("code", orderbean.getEnterpriseCode()).eq("del_flag", 0).eq("is_use",0));
				//判断券码是否存在并且未使用
				if(null!=enterpriseCode){
					order.setEnterpriseCode(orderbean.getEnterpriseCode());
				}
			}
			flag = this.insert(order);
			//更新券码信息
			if(null!=enterpriseCode){
				enterpriseCode.setIsUse("1");
				enterpriseCode.setOrderId(order.getId().intValue());
				enterpriseCodeService.updateById(enterpriseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("msg","FAIL");
			return resultMap;
		}
		long orderId = order.getId();

		//保存orderItem
		Integer ii = this.saveOrderItem(orderId, orderbean, category);
		if (ii < 1) {
			throw new ApiException("回收公司异常！！！！！");
		}

		//储存图片链接
		OrderPic orderPic = orderbean.getOrderPic();
		if (StringUtils.isNoneBlank(orderPic.getOrigPic())) {
			String origPics = orderPic.getOrigPic();
			String picUrl = orderPic.getPicUrl();
			String smallPic = orderPic.getSmallPic();
			String[] origPicss = origPics.split(",");
			String[] picUrls = picUrl.split(",");
			String[] smallPics = smallPic.split(",");
			for (int i = 0; i < origPicss.length; i++) {
				OrderPic orderPicc = new OrderPic();
				orderPicc.setOrigPic(origPicss[i]);
				orderPicc.setPicUrl(picUrls[i]);
				orderPicc.setSmallPic(smallPics[i]);
				orderPicc.setOrderId(Integer.parseInt(orderId + ""));
				flag = orderPicService.insert(orderPicc);
			}
		}
		//储存订单的日志
		OrderLog orderLog = new OrderLog();
		orderLog.setOrderId(Integer.parseInt(orderId + ""));
		orderLog.setOpStatusAfter("INIT");
		orderLog.setOp("待接单");
		flag = orderLogService.insert(orderLog);

		if (flag) {
			resultMap.put("msg","操作成功");
			resultMap.put("code",0);
			resultMap.put("id",orderId);
			return resultMap;
		}
		resultMap.put("msg","操作失败");
		return resultMap;
	}
	/**
	 * 回收员确认上传订单
	 */
	@Transactional
	@Override
	public boolean saveByRecy(OrderBean orderbean) {
		boolean flag = false;
		//获取分类的所有父类编号
		//Category category = null;
		//if (CategoryType.DIGITAL.name().equals(orderbean.getTitle())) {
		//	category = categoryService.selectById(orderbean.getCategoryId());
		//	orderbean.setCategoryParentIds(category.getParentIds());
		//}
		//修改订单中ach_remarks(完成订单描述)
		Order order = this.selectById(orderbean.getId());

		if(!"9000".equals(orderbean.getResultStatus())&&!StringUtils.isBlank(orderbean.getResultStatus())){
			//很具订单号查询订单是否交易成功
			Payment payment = paymentService.selectOne(new EntityWrapper<Payment>().eq("order_sn", order.getOrderNo()));
			if(payment!=null){
				if(StringUtils.isBlank(payment.getTradeNo())){
					return flag;
				}
				AlipayTradeQueryResponse aliPayment = paymentService.getAliPayment(payment.getTradeNo());
				if(!"TRADE_SUCCESS".equals(aliPayment.getTradeStatus())){
					return flag;
				}
			}
		}
		//删除上次储存的记录
		try{
			orderPicAchService.delete(new EntityWrapper<OrderPicAch>().eq("order_id",order.getId()));
			orderItemAchService.delete(new EntityWrapper<OrderItemAch>().eq("order_id",order.getId()));
		}catch (Exception e){
			e.printStackTrace();
		}
		order.setAchRemarks(orderbean.getAchRemarks());
		order.setSignUrl(orderbean.getSignUrl());
		if (StringUtils.isNotBlank(orderbean.getAchPrice())) {
			order.setAchPrice(new BigDecimal(orderbean.getAchPrice()));
		} else {
			order.setAchPrice(order.getPrice());
		}
		orderbean.setIsCash(order.getIsCash());
		Area city = areaService.selectById(order.getAreaId());
		orderbean.setCityId(city.getParentId().toString());
		//保存orderItem
		double amount = this.saveOrderItemAch(orderbean, null);
		order.setGreenCount(amount);
		flag = this.updateById(order);
		orderbean.setAmount(amount);
		//储存图片链接
		OrderPic orderPic = orderbean.getOrderPic();
		if (orderPic != null) {
			String origPics = orderPic.getOrigPic();
			String picUrl = orderPic.getPicUrl();
			String smallPic = orderPic.getSmallPic();
			String[] origPicss = origPics.split(",");
			String[] picUrls = picUrl.split(",");
			String[] smallPics = smallPic.split(",");
			OrderPicAch orderPicc = null;
			for (int i = 0; i < origPicss.length; i++) {
				flag = false;
				orderPicc = new OrderPicAch();
				orderPicc.setOrigPic(origPicss[i]);
				orderPicc.setPicUrl(picUrls[i]);
				orderPicc.setSmallPic(smallPics[i]);
				orderPicc.setOrderId(Integer.parseInt(orderbean.getId() + ""));
				flag = orderPicAchService.insert(orderPicc);
			}
		}
		//该订单是只要能量的订单
		if("1".equals(order.getIsCash())||0==Double.parseDouble(orderbean.getAchPrice())) {
			//修改订单状态
			this.modifyOrderSta(orderbean);
			if("1".equals(order.getIsMysl())){
				//给用户增加蚂蚁能量
				OrderBean orderBean = orderService.myslOrderData(order.getId().toString());
			}
			try {
				if ("上海市".startsWith(order.getAddress())){
					NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new sendGreenOrderThread(orderService,areaService,orderItemAchService,order.getId().intValue())));
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return flag;
	}

	/**
	 * @param
	 * @return List<Order>
	 * @author 王灿
	 */

	private Integer saveOrderItem(long orderId, OrderBean orderbean, Category category) {
		Integer ii = 0;
		EntityWrapper<CompanyCategory> wrapper = null;
		List<CompanyCategory> comPriceList = null;

		OrderItemBean orderItemBean = null;
		OrderItem orderItem = null;
		List<OrderItemBean> idAmount = null;
		CategoryAttrOption categoryAttrOption = null;
		CategoryAttr categoryAttr = null;
		//家电数码不变
		if (CategoryType.DIGITAL.name().equals(orderbean.getTitle())) {
			orderItemBean = orderbean.getOrderItemBean();
				//取出所有的分类选项Id的和
				String categoryAttrOppIds = orderItemBean.getCategoryAttrOppIds();
				String[] oppIds = categoryAttrOppIds.split(",");
				for (int i = 0; i < oppIds.length; i++) {
					//根据分类选项主键查询相关的一条记录
					categoryAttrOption = categoryAttrOptionService.getOptionById(oppIds[i]);
					//根据查出来的分类属性Id查出分类属性的记录
					categoryAttr = categoryAttrService.selectById(categoryAttrOption.getCategoryAttrId());
					//储存订单分类属性明细
					orderItemBean.setOrderId(Integer.parseInt(orderId + ""));
					orderItemBean.setCategoryId(Integer.parseInt(category.getId() + ""));
					orderItemBean.setCategoryName(category.getName());
					orderItemBean.setCategoryAttrId(Integer.parseInt(categoryAttr.getId() + ""));
					orderItemBean.setCategoryAttrName(categoryAttr.getName());
					orderItemBean.setCategoryAttrOppId(Integer.parseInt(categoryAttrOption.getId() + ""));
					orderItemBean.setCategoryAttrOpptionName(categoryAttrOption.getName());
					boolean bool = orderItemService.saveByOrderItem(orderItemBean);
					if (bool) {
						ii++;
					}
			}
		} else if (CategoryType.HOUSEHOLD.name().equals(orderbean.getTitle())) {
			List<IdAmountListBean> listBean = orderbean.getIdAndListList();
			for (IdAmountListBean idAmountListBean : listBean) {
				orderItem = new OrderItem();
				orderItem.setOrderId(Integer.parseInt(orderId + ""));
				orderItem.setParentId(idAmountListBean.getCategoryParentId());
				orderItem.setParentName(idAmountListBean.getCategoryParentName());
				idAmount = idAmountListBean.getIdAndAmount();
				wrapper = new EntityWrapper<CompanyCategory>();
				wrapper.eq("del_flag", "0");
				wrapper.eq("company_id", orderbean.getCompanyId());
				wrapper.eq("parent_id", idAmountListBean.getCategoryParentId());
				comPriceList = comCatePriceService.selectList(wrapper);
				for (OrderItemBean item : idAmount) {
					orderItem.setCategoryId(Integer.parseInt(item.getCategoryId() + ""));
					orderItem.setCategoryName(item.getCategoryName());
					orderItem.setAmount(item.getAmount());
					for (CompanyCategory comPrice : comPriceList) {
						if (comPrice.getCategoryId().equals(item.getCategoryId() + "")) {
							orderItem.setParentIds(comPrice.getParentIds());
							orderItem.setPrice(comPrice.getPrice());
							orderItem.setUnit(comPrice.getUnit());
							boolean bool = orderItemService.insert(orderItem);
							if (bool) {
								ii = ii + 1;
							}
						}
					}
				}
			}
		}
		return ii;
	}

	private double saveOrderItemAch(OrderBean orderbean, Category category) {
		//是否是免费的 0 不是 1是 
		String isCash = orderbean.getIsCash();
		//得到用户的垃圾总量
		double amount = 0;
		EntityWrapper<CompanyCategory> wrapper = null;
		List<CompanyCategory> comPriceList = null;
		OrderItemAch orderItem = null;
		List<OrderItemBean> idAmount = null;
		if (CategoryType.HOUSEHOLD.name().equals(orderbean.getTitle())) {
			List<IdAmountListBean> listBean = orderbean.getIdAndListList();
				for (IdAmountListBean idAmountListBean : listBean) {
					orderItem = new OrderItemAch();
					orderItem.setOrderId(Integer.parseInt(orderbean.getId() + ""));
					orderItem.setParentId(idAmountListBean.getCategoryParentId());
					orderItem.setParentName(idAmountListBean.getCategoryParentName());
					idAmount = idAmountListBean.getIdAndAmount();
					for (OrderItemBean item : idAmount) {
						orderItem.setCategoryId(Integer.parseInt(item.getCategoryId() + ""));
						orderItem.setCategoryName(item.getCategoryName());
						orderItem.setAmount(item.getAmount());
						System.out.println(item.getCategoryName() + " 重量: " + item.getAmount());
						if("1".equals(isCash)){
							amount += item.getAmount()*10;
						}else{
							amount += item.getAmount();
						}
						CompanyCategoryCity companyCategoryCity = companyCategoryCityService.selectOne(new EntityWrapper<CompanyCategoryCity>().eq("company_id", orderbean.getCompanyId()).eq("category_id", item.getCategoryId()).eq("city_id", orderbean.getCityId()));
						CompanyCategory companyCategory = comCatePriceService.selectOne(new EntityWrapper<CompanyCategory>().eq("company_id", orderbean.getCompanyId()).eq("category_id", item.getCategoryId()));
						if (companyCategoryCity!=null) {
							orderItem.setParentIds(companyCategoryCity.getParentIds());
							orderItem.setPrice(companyCategoryCity.getPrice().floatValue());
							orderItem.setUnit(companyCategoryCity.getUnit());
						}else {
							orderItem.setParentIds(companyCategory.getParentIds());
							orderItem.setPrice(companyCategory.getPrice());
							orderItem.setUnit(companyCategory.getUnit());
						}
						orderItemAchService.insert(orderItem);
					}
				}
		} else if (CategoryType.DIGITAL.name().equals(orderbean.getTitle())) {
			//根据订单Id获取电器订单所属分类的绿色能量值
			OrderItem item = orderItemService.selectOne(new EntityWrapper<OrderItem>().eq("order_id", orderbean.getId()).groupBy("order_id"));
			if (item != null) {
				Category categorys = categoryService.selectById(item.getCategoryId());
				if("1".equals(isCash)){
					amount = categorys.getGreenCount()*10;
				}else {
					amount = categorys.getGreenCount();
				}
			}
		}
		System.out.println("回收生活垃圾总重量为 ：" + ApiUtils.privatedoublegetTwoDecimal(amount));
		return ApiUtils.privatedoublegetTwoDecimal(amount);
	}

	/**
	 * 根据订单状态进行分页
	 */
	@Override
	public Page<Order> getOrderPage(String status, PageBean page) {
		Page<Order> pages = new Page<Order>(page.getPageNumber(), page.getPageSize());
		EntityWrapper<Order> wrapper = new EntityWrapper<Order>();
		//wrapper.eq("member_id", memberId);
		wrapper.in("status_", status);
		return this.selectPage(pages, wrapper);
	}

	@Override
	public List<Order> getUncompleteList(long memberId) {
		List<Order> orderList = orderMapper.getUncompleteList(memberId);
		orderList = this.createName4Ali(orderList);
		return orderList;
	}

	public List<Order> createName4Ali(List<Order> orderList) {
		Set<String> attName = null;
		List<ComCatePrice> attrList = null;
		for (Order order : orderList) {
			if (order.getTitle() == Order.TitleType.HOUSEHOLD) {
				attName = new HashSet<>();
				//获得父类名称
				attrList = orderItemService.selectCateName(Integer.parseInt(order.getId().toString()));
				if (attrList != null && attrList.size() > 0) {
					try {
						for (ComCatePrice comCatePrice : attrList) {
							if (null!=comCatePrice){
								attName.add(comCatePrice.getName());
							}
						}
						order.setTitle(Order.TitleType.HOUSEHOLD);
						order.setCateAttName4Page(attName.toString().replace("]", "").replace("[", "").replace(",", "/").replace(" ", ""));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (order.getTitle() == Order.TitleType.DIGITAL) {
				
				//根据分类Id查询父级分类名字
				Category category = categoryService.selectById(order.getCategory().getCategoryId());
				order.setCateAttName4Page(category.getName());
				order.setTitle(Order.TitleType.DIGITAL);
			}else if(order.getTitle() == Order.TitleType.BIGTHING){
                //根据分类Id查询父级分类名字
                Category category = categoryService.selectById(order.getCategory().getCategoryId());
                order.setCateAttName4Page(category.getName());
                order.setTitle(Order.TitleType.BIGTHING);
            }else if(order.getTitle() == Order.TitleType.FIVEKG){
				attName = new HashSet<>();
				//获得父类名称
				attrList = orderItemService.selectCateName(Integer.parseInt(order.getId().toString()));
				if (attrList != null && attrList.size() > 0) {
					try {
						for (ComCatePrice comCatePrice : attrList) {
							attName.add(comCatePrice.getName());
						}
						order.setTitle(Order.TitleType.FIVEKG);
						order.setCateAttName4Page(attName.toString().replace("]", "").replace("[", "").replace(",", "/").replace(" ", ""));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else if (order.getTitle() == Order.TitleType.IOTORDER){
				attName = new HashSet<>();
				//获得父类名称
				attrList = orderItemService.selectCateAchName(Integer.parseInt(order.getId().toString()));
				if (attrList != null && attrList.size() > 0) {
					try {
						for (ComCatePrice comCatePrice : attrList) {
							attName.add(comCatePrice.getName());
						}
						order.setTitle(Order.TitleType.IOTORDER);
						order.setCateAttName4Page(attName.toString().replace("]", "").replace("[", "").replace(",", "/").replace(" ", ""));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}
		return orderList;
	}

	public Order createName4Ali(Order order) {
		Set<String> attName = null;
		List<ComCatePrice> attrList = null;
		if (order.getTitle() == Order.TitleType.HOUSEHOLD) {
			attName = new HashSet<>();
			//获得父类名称
			attrList = orderItemService.selectCateName(Integer.parseInt(order.getId().toString()));
			if (attrList != null && attrList.size() > 0) {
				try {
					for (ComCatePrice comCatePrice : attrList) {
						if (null!=comCatePrice){
							attName.add(comCatePrice.getName());
						}
					}
					order.setTitle(Order.TitleType.HOUSEHOLD);
					order.setCateAttName4Page(attName.toString().replace("]", "").replace("[", "").replace(",", "/").replace(" ", ""));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (order.getTitle() == Order.TitleType.DIGITAL) {
			order.setCateAttName4Page(order.getCategory().getName());
			order.setTitle(Order.TitleType.DIGITAL);
		}else if(order.getTitle() == Order.TitleType.BIGTHING){
            order.setCateAttName4Page(order.getCategory().getName());
            order.setTitle(Order.TitleType.BIGTHING);
        }else if (order.getTitle() == Order.TitleType.FIVEKG){
			attName = new HashSet<>();
			//获得父类名称
			attrList = orderItemService.selectCateName(Integer.parseInt(order.getId().toString()));
			if (attrList != null && attrList.size() > 0) {
				try {
					for (ComCatePrice comCatePrice : attrList) {
						attName.add(comCatePrice.getName());
					}
					order.setTitle(Order.TitleType.FIVEKG);
					order.setCateAttName4Page(attName.toString().replace("]", "").replace("[", "").replace(",", "/").replace(" ", ""));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return order;
	}

	public Order createName4PC(Order order) {
		Category category = order.getCategory();
		;
		if (order.getTitle() == Order.TitleType.HOUSEHOLD) {
			category.setName("生活垃圾");
			order.setCategory(category);
		} else if (order.getTitle() == Order.TitleType.DIGITAL) {
			category.setName("家电数码");
			order.setCategory(category);
		}
		return order;
	}

	public List<AppOrderResult> createName4App(List<AppOrderResult> orderList) {
		Set<String> attName = null;
		List<ComCatePrice> attrList = null;
		List<AppOrderResult> orderLists = new ArrayList<AppOrderResult>();
		for (AppOrderResult order : orderList) {
			//获取当前定单的成交价格
			Object paymentPrice = orderMapper.paymentPriceByOrderId(Integer.parseInt(order.getOrderId()));
			if (paymentPrice ==null){
				paymentPrice=0;
			}
			if (order.getTitle() == CategoryType.HOUSEHOLD) {
				attName = new HashSet<>();
				//获得父类名称(如果订单已完成，名称从完成订单分类中获取)
				if (order.getStatus().equals(OrderType.ALREADY.getValue().toString())) {
					attrList = orderItemAchService.selectCateName(Integer.parseInt(order.getOrderId().toString()));
				} else {
					attrList = orderItemService.selectCateName(Integer.parseInt(order.getOrderId().toString()));
				}
				for (ComCatePrice comCatePrice : attrList) {
					if(null == comCatePrice){
						continue;
					}
					attName.add(comCatePrice.getName());
				}
				order.setTitle(CategoryType.HOUSEHOLD);
				order.setCateAttName4Page(attName.toString().replace("]", "").replace("[", "").replace(",", "/").replace(" ", ""));
				order.setPaymentPrice(paymentPrice);
				orderLists.add(order);
			} else if (order.getTitle() == CategoryType.DIGITAL) {
				order.setCateAttName4Page(order.getCateName());
				order.setTitle(CategoryType.DIGITAL);
				order.setPaymentPrice(paymentPrice);
				orderLists.add(order);
			}
		}
		return orderLists;
	}

	/**
	 * 根据订单id获取订单详情
	 *
	 * @param orderId : 订单主键Id
	 * @return
	 * @author 王灿
	 */
	@Override
	public Order selectOrder(int orderId) {

		return orderMapper.selectOrder(orderId);
	}

	/**
	 * 根据查询条件获取订单
	 *
	 * @param @param status 订单状态
	 * @param @param pageBean
	 * @return Page<Order>    返回类型
	 * @throws
	 * @Title: getSearchOrder
	 * @author 王池
	 */

	@Override
	public Page<Order> getSearchOrder(BOrderBean orderBean, PageBean pageBean) {
		Page<Order> page = new Page<Order>(pageBean.getPageNumber(), pageBean.getPageSize());
		EntityWrapper<Order> wrapper = new EntityWrapper<Order>();
		if (StringUtils.isNotBlank(orderBean.getOrderNo())) {
			wrapper.eq("order_no", orderBean.getOrderNo());
		}
		if (StringUtils.isNotBlank(orderBean.getLinkMan())) {
			wrapper.eq("link_man", orderBean.getLinkMan());
		}
		if (StringUtils.isNotBlank(orderBean.getStatus().toString())) {
			wrapper.eq("status_", orderBean.getStatus());
		}

		return this.selectPage(page, wrapper);
	}

	/**
	 * 根据各种查询条件获取订单 列表
	 *
	 * @param orderBean:查询订单的条件
	 * @param pageBean          : 分页的条件
	 * @return
	 * @author 王灿
	 * @changeby sgmark@aliyun.com
	 */
	@Override
	public Map<String, Object> getOrderLists(BOrderBean orderBean, PageBean pageBean) {
		List<String> statusList = new ArrayList<>();
		String status = getStatus(orderBean.getStatus());
		String title = null;
		if (orderBean.getCategoryType() == null) {
			throw new ApiException("参数错误");
		}else {
			title = Order.TitleType.valueOf(orderBean.getCategoryType()).getValue().toString();
		}
		statusList.add(status);
		String orderNo = orderBean.getOrderNo();
		String linkMan = orderBean.getLinkMan();
		String recyclersName = null;
		Integer companyId = null;
		String startTime = null;
		String endTime = null;
		String isScan = StringUtils.isBlank(orderBean.getIsScan()) ? "0" : orderBean.getIsScan();

		if (orderBean.getRecyclers() != null && !"".equals(orderBean.getRecyclers())) {
			recyclersName = orderBean.getRecyclers().getName();
		}
		if (orderBean.getCompanyId() != null && !"".equals(orderBean.getCompanyId())) {
			companyId = orderBean.getCompanyId();
		}
		if (orderBean.getStartTime() != null && !"".equals(orderBean.getStartTime())) {
			startTime = orderBean.getStartTime();
		}
		if (orderBean.getEndTime() != null && !"".equals(orderBean.getEndTime())) {
			endTime = orderBean.getEndTime();
		}
		int size = pageBean.getPageSize();
		int num = pageBean.getPageNumber();
		Map<String, Object> map = new HashMap<String, Object>();
		Integer count = orderMapper.getOrderListsCount(companyId, statusList, orderNo, linkMan, recyclersName, size, ((num - 1) * size), startTime, endTime, isScan, title);
		List<Order> list = orderMapper.getOrderLists(companyId, statusList, orderNo, linkMan, recyclersName, size, ((num - 1) * size), startTime, endTime, isScan, title);
//		if ("0".equals(status)) {
//			count += orderMapper.getOrderListsCount(companyId, "6", orderNo, linkMan, recyclersName, size,((num-1)*size),startTime,endTime);
//			list.addAll(orderMapper.getOrderLists(companyId,"6",orderNo,linkMan,recyclersName,size,((num-1)*size),startTime,endTime));
//		}
		//判断生活垃圾，还是家用电器，给category.name赋值
		for (Order order : list) {
			createName4PC(order);
//			完成列表预估价格显示不正确
			if (OrderType.COMPLETE.getValue().equals(order.getStatus().getValue()) && order.getTitle() == Order.TitleType.HOUSEHOLD) {
				order.setPrice(order.getAchPrice());
			}
		}

		map.put("count", count);
		map.put("list", list);
		if (CategoryType.BIGTHING.getValue().toString().equals(title)){
			map.put("distributedCount", this.getOrderListsDistribute(orderBean, pageBean).get("count"));// 再处理订单数量
		}
		return map;
	}
	/**
	 * 再处理订单
	  * @author sgmark@aliyun.com
	  * @date 2019/3/25 0025
	  * @param
	  * @return
	  */
	@Override
	public Map<String, Object> getOrderListsDistribute(BOrderBean orderBean, PageBean pageBean) {
		Map map = new HashMap();
		String status = getStatus(orderBean.getStatus());
		Integer companyId = orderBean.getCompanyId();
		String title = Order.TitleType.valueOf(orderBean.getCategoryType()).getValue().toString();


		EntityWrapper entityWrapper = new EntityWrapper();
		entityWrapper.eq("status_", status);
		entityWrapper.eq("title", title);
		entityWrapper.eq("company_id", companyId);
		entityWrapper.eq("is_distributed", 1);
		List<Order> orderList = orderMapper.selectList(entityWrapper);
		orderList.stream().forEach(order -> {
			if (order.getCategoryId() != null) {
				order.setCategory(categoryService.selectById(order.getCategoryId()));
			}
		});
		map.put("count", orderList.size());
		map.put("list", orderList);
		return map;
	}


	/**
	 * iot设备创建订单
	  * @author sgmark@aliyun.com
	  * @date 2019/3/30 0030
	  * @param
	  * @return
	  */
	@Override
	public Map<String, Object> iotCreatOrder(IotParamBean iotParamBean) {
		Map<String, Object> map = new HashMap<>();
		List<IotParamBean.ParentList> parentLists = iotParamBean.getParentLists();
		EntityWrapper<CompanyEquipment> entityWrapper = new EntityWrapper();
		entityWrapper.eq("del_flag", 0);
		entityWrapper.eq("equipment_code", iotParamBean.getEquipmentCode());
		CompanyEquipment companyEquipment = companyEquipmentService.selectOne(entityWrapper);
		if(companyEquipment == null){
			throw new ApiException("当前设备不存在","-9");
		}else if (companyEquipment.getStatus() == 1){
			throw new ApiException("当前设备不可用","-9");
		}
		Order order = new Order();
		order.setCompanyId(Integer.parseInt(companyEquipment.getCompanyId().toString()));
		if (iotParamBean.getMemberId() == null || "".equals(iotParamBean.getMemberId().trim())){
			throw new ApiException("参数错误, 缺少memberId。。", "-9");
		}
		String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);
		order.setOrderNo(orderNo);
        EntityWrapper<Member> entity =  new EntityWrapper<>();
        entity.eq("card_no", iotParamBean.getMemberId()).eq("del_flag", 0);
		Member member = memberService.selectOne(entity);
        if (member == null){
        	throw new ApiException("当前用户不是我们系统会员");
		}
		order.setMemberId(Integer.parseInt(member.getId().toString()));
		order.setIotEquipmentCode(iotParamBean.getEquipmentCode());
//		order.setPrice(iotParamBean.getSumPrice());
		AtomicReference<Float> bottlesCount = new AtomicReference<>((float) 0);
		//查找所有重量
		final double[] score = {0};
		parentLists.stream().forEach(parentList -> {
			parentList.getItemList().stream().forEach(itemList -> {
				if (itemList.getName() == Category.SecondType.BEVERAGE_BOTTLES){
					//峰会只给瓶子蚂蚁森林能量
					bottlesCount.set(itemList.getQuantity());
					score[0] += itemList.getQuantity() * 0.04;//瓶子个数40g/个
					order.setUnit("个");
				}else {
					score[0] += itemList.getQuantity();
					order.setUnit("kg");
				}
			});
		});
//		parentLists.stream().map(IotParamBean.ParentList::getItemList).forEach(itemLists -> {
//			if (itemLists.stream().map(IotParamBean.ItemList::getName).equals(Category.SecondType.BEVERAGE_BOTTLES)){
//				score[0] += 0.04 * itemLists.stream().map(IotParamBean.ItemList::getQuantity).reduce((x, y) -> x+y).get();
//			}else {
//				score[0] += itemLists.stream().map(IotParamBean.ItemList::getQuantity).reduce((x, y) -> x+y).get();
//			}
//		});
		//保留两位小数
		order.setGreenCount(new BigDecimal(score[0]).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		order.setAreaId(companyEquipment.getAreaId());
		order.setStreetId(companyEquipment.getStreetId());
		order.setCommunityId(companyEquipment.getCommunityId());
		order.setAddress(companyEquipment.getAddress());
        order.setTitle(Order.TitleType.IOTORDER);
		order.setStatus(OrderType.COMPLETE);
		order.setPrice(BigDecimal.ZERO);

		//保存id至redis，跳转到订单详情页面
		try {
			Hashtable<String, String> hashTable = null;
			if (null == redisUtil.get("iotMap")){
				hashTable = new Hashtable<>();
			}else {
				hashTable = (Hashtable<String, String>)redisUtil.get("iotMap");
			}
			String iotMemberId = "iot_member_id_" + member.getId();
//            System.out.println(parentLists.stream().allMatch(parentList -> parentList.getItemList().stream().allMatch(itemList -> itemList.getQuantity() != 0.0)));
			if (parentLists.stream().anyMatch(parentList -> parentList.getItemList().stream().anyMatch(itemList -> itemList.getQuantity() == 0.0))){//打开箱门，并没投递任何东西
				hashTable.put(iotMemberId, "empty");
                redisUtil.set("iotMap", hashTable);
				map.put("order_no", order.getOrderNo());
				map.put("equipment_code",iotParamBean.getEquipmentCode());
				map.put("msg", "empty");
                return map;
			}else {
				this.insert(order);
				hashTable.put(iotMemberId, order.getId()+"");
			}
			redisUtil.set("iotMap", hashTable);
		}catch (Exception e){
			e.printStackTrace();
		}

		if(!parentLists.isEmpty()){
			//说明是同步传过来的
			this.saveOrderItemAch(order, parentLists);
		}else {
			throw new ApiException("内容为空，新增失败");
		}
		map.put("order_no", order.getOrderNo());
		map.put("equipment_code",iotParamBean.getEquipmentCode());
		map.put("msg", "CREATED");

		//只给瓶子增加能量，为峰会使用bottlesCount.get().toString()
		this.updateCansForestIot(order, member.getAliUserId(), Float.valueOf(bottlesCount.get()).longValue(), "cans");
		//增加平台积分
		this.updateMemberPoint(order.getMemberId(), order.getOrderNo(),order.getGreenCount(), "智能回收箱投放");
		return map;
	}

	public void updateCansForestIot(Order order, String aliUserId, long count, String type){
		//增加蚂蚁能量
		AntMerchantExpandTradeorderSyncResponse tradeorderSyncResponse = null;
		if (count >= 1) {
			tradeorderSyncResponse = ansycMyslService.updateCansForest(aliUserId, UUID.randomUUID().toString(), count, type);
			//更新order
			if (null != tradeorderSyncResponse) {
				order.setMyslOrderId(tradeorderSyncResponse.getOrderId());
				order.setMyslParam(tradeorderSyncResponse.getParams().toString());
				this.updateById(order);
			}
		}
	}

//	/**
//	 * 初始检验价格计算是否合理
//	 * @author sgmark@aliyun.com
//	 * @date 2018/12/26 0026
//	 * @param
//	 * @return
//	 */
//	public boolean checkSubmit(List<IotParamBean.ParentList> parentLists){
//		//数据库里查询当前回收物价格
//		final BigDecimal[] sumPrice = {BigDecimal.ZERO};
//		final BigDecimal[] sumKg = {BigDecimal.ZERO};
//		stationCateList.stream().forEach(map ->{
//			List<CategoryResult> list = null;
//			list = (List<CategoryResult>) map.get("content");
//			list.stream().forEach(statCate ->{
//				categoryList.stream().forEach(category -> {
//					if (statCate.getId().equals(category.getId())){
//						category.setInPrice(new BigDecimal(statCate.getInPrice()));
//						category.setOutPrice(new BigDecimal(statCate.getOutPrice()));
//						category.setUnit(statCate.getUnit());
//						category.setCateName(statCate.getCategoryName());
//						sumKg[0] = sumKg[0].add(category.getWeight());
//						if (StationCategory.INOUTFLOW.INFLOW.getValue() == inOut)
//							sumPrice[0] = sumPrice[0].add(new BigDecimal(statCate.getInPrice()).multiply(category.getWeight()));
//						else
//							sumPrice[0] = sumPrice[0].add(new BigDecimal(statCate.getOutPrice()).multiply(category.getWeight()));
//					}
//				});
//			});
//		});
//		orderPriceBean.setCategoryList(categoryList);//放入单价单位
//		if (sumPrice[0].compareTo(orderPriceBean.getSumPrice()) == 0 && sumKg[0].compareTo(orderPriceBean.getSumKg()) ==0){//算出值和页面数据不对等
//			return true;
//		}
//		return false;
//	}
	/**
	 * iot设备更新订单
	 * @author sgmark@aliyun.com
	 * @date 2019/3/30 0030
	 * @param
	 * @return
	 */
	@Override
	public Map<String, Object> iotUpdateOrderItemAch(IotParamBean iotParamBean) {
		Map<String, Object> map = new HashMap<>();
		if (iotParamBean.getOrderNo() == null ||  "".equals(iotParamBean.getOrderNo().trim())){
			throw new ApiException("参数错误，order_no为null");
		}
		EntityWrapper entityWrapper = new EntityWrapper();
		entityWrapper.eq("del_flag", 1);
		entityWrapper.eq("order_no", iotParamBean.getOrderNo());
		entityWrapper.eq("iot_equipment_code",iotParamBean.getEquipmentCode());
		Order order = this.selectOne(entityWrapper);
		if (order == null){
			throw new ApiException("找不到所对应的订单");
		}
		List<IotParamBean.ParentList> parentLists = iotParamBean.getParentLists();
		if(parentLists.size() >= 1){
			this.saveOrderItemAch(order, parentLists);
		}else {
			throw new ApiException("内容为空，更新失败");
		}
		map.put("order_no", order.getOrderNo());
		map.put("equipment_code",iotParamBean.getEquipmentCode());
		map.put("msg", "UPDATED");
		return map;
	}

	public void saveOrderItemAch(Order order, List<IotParamBean.ParentList> parentLists){
		parentLists.stream().forEach(parentList -> {
			//找父类名称
			EntityWrapper<Category> parentEntityWrapper = new EntityWrapper<>();
			parentEntityWrapper.eq("del_flag", 0);
			parentEntityWrapper.eq("code_", parentList.getParentName().getValue());//code
			Category categoryParent = categoryService.selectOne(parentEntityWrapper);

			parentList.getItemList().stream().forEach(itemList -> {
				if (itemList.getName().getValue() == null || itemList.getQuantity() < 0){
					throw new ApiException("参数错误, 类型不匹配。。", "-9");
				}
				EntityWrapper<Category> categoryEntityWrapper = new EntityWrapper<>();
				categoryEntityWrapper.eq("del_flag", 0);
				categoryEntityWrapper.eq("code_", itemList.getName().getValue());//code
				Category category = categoryService.selectOne(categoryEntityWrapper);
				if (category != null){
					OrderItemAch orderItemAch = new OrderItemAch();
					orderItemAch.setOrderId(Integer.parseInt(order.getId().toString()));
					orderItemAch.setCategoryId(Integer.parseInt(category.getId().toString()));
					orderItemAch.setCategoryName(category.getName());
					orderItemAch.setParentId(category.getParentId());
					orderItemAch.setParentName(categoryParent.getName());//存父类名称
					if (itemList.getName() == Category.SecondType.BEVERAGE_BOTTLES){
						orderItemAch.setAmount(itemList.getQuantity());//瓶子'个'转化为kg 40g/个
						orderItemAch.setUnit("个");
					}else{
						orderItemAch.setAmount(itemList.getQuantity());
						orderItemAch.setUnit("kg");//转换为kg
					}
					orderItemAch.setCreateDate(new Date());
					orderItemAchService.insert(orderItemAch);
					OrderItem orderItem = new OrderItem();
					orderItem.setOrderId(orderItemAch.getOrderId());
					orderItem.setCategoryId(orderItemAch.getCategoryId());
					orderItem.setCategoryName(orderItemAch.getCategoryName());
					orderItem.setParentId(orderItemAch.getParentId());
					orderItem.setParentName(orderItemAch.getParentName());
					orderItem.setAmount(orderItemAch.getAmount());
					orderItem.setUnit(orderItemAch.getUnit());
					orderItem.setCreateDate(new Date());
					orderItemService.insert(orderItem);
				}
			});
		});
	}

	/**
	 * 导出企业的完成订单的Excel
	 * @param companyId
	 * @return
	 */
	@Override
	public List<Map<String,Object>> outOrderExcel(Integer companyId,String type,String startTime,String endTime){
		return orderMapper.outOrderExcel(companyId,type,startTime,endTime);
	}
	/**
	 * 根据各种查询条件获取订单列表
	 *
	 * @param orderBean 查询订单的条件
	 * @param pageBean  分页的条件
	 * @return
	 */
	public Map<String, Object> getOrderLists2(BOrderBean orderBean, PageBean pageBean) {
		List<String> statusList = new ArrayList<>();
		String status = getStatus(orderBean.getStatus());
		statusList.add(status);
		String orderNo = orderBean.getOrderNo();
		String linkMan = orderBean.getLinkMan();
		String recyclersName = null;
		Integer companyId = null;
		String startTime = null;
		String endTime = null;
		String isScan = StringUtils.isBlank(orderBean.getIsScan()) ? "0" : orderBean.getIsScan();

		if (orderBean.getRecyclers() != null && !"".equals(orderBean.getRecyclers())) {
			recyclersName = orderBean.getRecyclers().getName();
		}
		if (orderBean.getCompanyId() != null && !"".equals(orderBean.getCompanyId())) {
			companyId = orderBean.getCompanyId();
		}
		if (orderBean.getStartTime() != null && !"".equals(orderBean.getStartTime())) {
			startTime = orderBean.getStartTime();
		}

		int size = pageBean.getPageSize();
		int num = pageBean.getPageNumber();
		Map<String, Object> map = new HashMap<>();
		Integer count = orderMapper.getOrderListsCount(companyId, statusList, orderNo, linkMan, recyclersName, size, ((num - 1) * size), startTime, endTime, isScan, null);
		List<Order> list = orderMapper.getOrderLists(companyId, statusList, orderNo, linkMan, recyclersName, size, ((num - 1) * size), startTime, endTime, isScan, null);

		map.put("count", count);
		map.put("list", list);


		return map;
	}


	/**
	 * 根据各种状态查询相订单表相关的条数
	 *
	 * @param status:订单的条件
	 * @param companyId:企业Id
	 * @return
	 * @author 王灿
	 */
	@Override
	public Map<String, Object> selectCountByStatus(String status, Integer companyId, Order.TitleType categoryType) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (categoryType == null){
			throw new ApiException("参数错误");
		}
		int INITCount = this.selectCount(new EntityWrapper<Order>().eq("status_", getStatus("INIT")).eq("del_flag", "0").eq("company_id", companyId).eq("title", categoryType.getValue()));
		int TOSENDCount = this.selectCount(new EntityWrapper<Order>().eq("status_", getStatus("TOSEND")).eq("del_flag", "0").eq("company_id", companyId).eq("title", categoryType.getValue()));
		int ALREADYCount = this.selectCount(new EntityWrapper<Order>().eq("status_", getStatus("ALREADY")).eq("del_flag", "0").eq("company_id", companyId).eq("title", categoryType.getValue()));
		int COMPLETECount = this.selectCount(new EntityWrapper<Order>().eq("status_", getStatus("COMPLETE")).eq("del_flag", "0").eq("company_id", companyId).eq("title", categoryType.getValue()));
		int CANCELCount = this.selectCount(new EntityWrapper<Order>().eq("status_", getStatus("CANCEL")).eq("del_flag", "0").eq("company_id", companyId).eq("title", categoryType.getValue()));
		int REJECTEDCount = this.selectCount(new EntityWrapper<Order>().eq("status_", getStatus("REJECTED")).eq("del_flag", "0").eq("company_id", companyId).eq("title", categoryType.getValue()));
		map.put("INIT", INITCount);
		map.put("TOSEND", TOSENDCount);
		map.put("ALREADY", ALREADYCount);
		map.put("COMPLETE", COMPLETECount);
		map.put("CANCEL", CANCELCount);
		map.put("REJECTED", REJECTEDCount);
		return map;
	}


	public String getStatus(String status) {
		String orderStatus = "";
		switch (status) {
			case "INIT":
				orderStatus = "0";
				break;
			case "TOSEND":
				orderStatus = "1";
				break;
			case "ALREADY":
				orderStatus = "2";
				break;
			case "COMPLETE":
				orderStatus = "3";
				break;
			case "CANCEL":
				orderStatus = "4";
				break;
			case "REJECTED":
				orderStatus = "5";
				break;
			default:
				orderStatus = null;
				break;
		}
		return orderStatus;
	}

	/**
	 * 完成订单
	 *
	 * @param order:订单
	 * @return
	 * @author 王灿
	 */
	@Transactional
	@Override
	public String completeOrder(Order order, String orderInitStatus) {
		boolean bool = this.updateById(order);
		//修改订单日志表的
		OrderLog orderLog = new OrderLog();
		orderLog.setOpStatusBefore(orderInitStatus);
		orderLog.setOpStatusAfter("COMPLETE");
		orderLog.setOp("已完成");
		orderLog.setOrderId(order.getId().intValue());
		orderLogService.insert(orderLog);
		return "SUCCESS";
	}


	/**
	 * 取消订单
	 *
	 * @param order:订单
	 * @return
	 * @author 王灿
	 */
	@Transactional
	@Override
	public String orderCancel(Order order, String orderInitStatus) {
		String status = OrderType.valueOf(orderInitStatus).getValue()+"";
		boolean bool = this.updateById(order);
		if (bool) {
			CommToken commToken = orderMapper.getCommToken(order.getOrderNo());
			if (commToken != null) {
				xingeService.sendPostMessage("您有一笔订单已被取消", "已取消订单来自" + commToken.getCommName() + "，点击查看", commToken.getTencentToken(), XingeMessageCode.cancelOrder);
			}
		}
		if("3".equals(order.getTitle().getValue()+"")&&!"0".equals(status)){
			try{
				Company company = companyService.selectById(order.getCompanyId());
				if(null!=company&&null!=company.getAliMns()){
					HashMap<String,Object> param=new HashMap<>();
					param.put("orderNo",order.getOrderNo());
					param.put("isCancel","Y");
					param.put("cancelReason",order.getCancelReason());
					sendRocketmqMessageService.sendDeliveryOrder(JSON.toJSONString(param),company.getAliMns());
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		//阿里云推送
		if(order.getRecyclerId()!=null&&order.getRecyclerId()!=0){
			Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
			PushUtils.getAcsResponse(recyclers.getTel(),order.getStatus().getValue()+"",order.getTitle().getValue()+"");
		}
		//判断是否有券码
		if(!StringUtils.isBlank(order.getEnterpriseCode())){
			EnterpriseCode enterpriseCode = enterpriseCodeService.selectOne(new EntityWrapper<EnterpriseCode>().eq("code", order.getEnterpriseCode()).eq("del_flag", 0).eq("is_use",1));
			//判断券码是否存在并且未使用
			if(null!=enterpriseCode){
				enterpriseCode.setIsUse("0");
				enterpriseCodeService.updateById(enterpriseCode);
			}
		}
		//新增订单日志表的记录
		OrderLog orderLog = new OrderLog();
		orderLog.setOpStatusBefore(orderInitStatus);
		orderLog.setOpStatusAfter("CANCEL");
		orderLog.setOp("已取消");
		orderLog.setOrderId(order.getId().intValue());
		orderLogService.insert(orderLog);
		return "SUCCESS";
	}

	/**
	 * 订单详情(用户)
	 *
	 * @param orderbean:订单
	 * @return
	 * @author 王灿
	 */
	@Override
	public Map<String, Object> selectDetail(OrderBean orderbean) {
		//查询订单表详情
		Order order1 = this.selectById(orderbean.getId());
		order1.setIsRead("1");
		this.updateById(order1);
		Order order = orderMapper.selectOrder(orderbean.getId());
		if((Order.TitleType.FIVEKG +"") .equals(order.getTitle()+"")){
			order.setQuantity(order.getQty()+"-"+(order.getQty()+5)+"kg");
		}
		if ((Order.OrderType.COMPLETE +"") .equals(order.getStatus()+"")&&null != order.getCompleteDate()){
			try {
				SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				order.setReceiveTime(simple.parse(order.getCompleteDate().toString()));
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		CompanyEquipment companyEquipment = companyEquipmentService.selectOne(new EntityWrapper<CompanyEquipment>().eq("equipment_code", order.getIotEquipmentCode()));
		order.setCompanyEquipment(companyEquipment);
		//查询订单表的关联图片表
		List<OrderPic> orderPicList = orderPicService.selectbyOrderId(orderbean.getId());
		//查询订单表的分了明细表
		List<OrderItem> OrderItemList = orderItemService.selectByOrderId(orderbean.getId());
		//List<ComCatePrice> cateName = orderItemService.selectCateName(orderbean.getId());
		List<Map<String, Object>> listMapObject = new ArrayList<>();
		//Map<String, Object> mapListMap = null;
		Object obj = null;
		if (order.getTitle() == Order.TitleType.HOUSEHOLD||order.getTitle() == Order.TitleType.FIVEKG||order.getTitle() == Order.TitleType.IOTORDER) {
			orderbean.setStatus(order.getStatus().getValue().toString());
			orderbean.setTitle(order.getTitle().toString());
			orderbean.setIsCash(order.getIsCash());

			Map<String, Object> map = this.createPriceAmount(orderbean);
			listMapObject = (List<Map<String, Object>>) map.get("listMapObject");
			obj = map.get("achAmount");

		} else if (order.getTitle() == Order.TitleType.DIGITAL) {
			OrderItemList = orderItemService.selectByOrderId(orderbean.getId());
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Company company = companyService.selectById(order.getCompanyId());
		Payment payment = paymentService.selectOne(new EntityWrapper<Payment>().eq("order_sn", order.getOrderNo()));
		String tradeNo="";
		if(payment!=null){
			tradeNo = payment.getTradeNo();
		}
		order.setCompany(company);
		resultMap.put("order", this.createName4Ali(order));
		resultMap.put("orderPicList", orderPicList);
		resultMap.put("OrderItemList", OrderItemList);
		resultMap.put("list", listMapObject);
		resultMap.put("achAmount", obj);
		resultMap.put("tardeNo", tradeNo);
		return resultMap;
	}

	/**
	 * 订单详情(企业)
	 *
	 * @param orderId:订单
	 * @return
	 * @author 王灿
	 */
	@Override
	public Map<String, Object> selectOrderByBusiness(Integer orderId) {
		//查询订单详情
		//Order order = orderMapper.selectOrder(orderId);
		Order order = createName4Ali(orderMapper.selectOrder(orderId));
		Category category = null;
		String cateName = "";
		//categoryId 再查他的父亲
		if (order.getCategoryId() != null) {
			category = categoryService.getCategoryById(order.getCategoryId());
			if (category != null) {
				Category pCategory = null;
				if (category.getParentId() != null && !"".equals(category.getParentId())) {
					pCategory = categoryService.getCategoryById(category.getParentId());
					if (pCategory != null) {
						cateName = pCategory.getName() + "-" + category.getName();
						category.setName(cateName);
						order.setCategory(category);
						cateName = "";
					}
				}
			}
		}
		int CompanyId = order.getCompanyId();
		//根据企业id查询企业信息
		Company company = companyService.selectById(CompanyId);
		//根据订单Id查询评价信息
		OrderEvaluation OrderEvaluation = orderEvaluationService.selectOne(new EntityWrapper<OrderEvaluation>().eq("order_id", orderId).eq("del_flag", "0"));

		Map<String, Object> resultMap = new HashMap<String, Object>();
		//order.setPrice(order.getAchPrice());
		//resultMap.put("order", order);
		resultMap.put("company", company);
		resultMap.put("OrderEvaluation", OrderEvaluation);
		//System.out.println(11111);
		if (order.getTitle() == Order.TitleType.HOUSEHOLD||order.getTitle() == Order.TitleType.FIVEKG) {
			OrderBean orderBean = new OrderBean();
			orderBean.setId(orderId);
			orderBean.setTitle(order.getTitle().toString());
			orderBean.setStatus(order.getStatus().getValue().toString());
			orderBean.setIsCash(order.getIsCash());

			Map<String, Object> map = this.createPriceAmount4PC(orderBean);
			List<Map<String, Object>> priceAmount = (List<Map<String, Object>>) map.get("listMapObject");
			System.out.println(priceAmount.get(0));
			resultMap.put("priceAmount", priceAmount);
			resultMap.put("greenCount", map.get("greenCount"));

		}

		if (OrderType.COMPLETE.getValue().equals(order.getStatus().getValue())) {
			//回收人员填的图片
			List<OrderPicAch> orderPicAchList = orderPicAchService.selectbyOrderId(orderId);
			//查询订单表的用户填的图片
			List<OrderPic> orderPicList = orderPicService.selectbyOrderId(orderId);
			if (CategoryType.DIGITAL.getValue().equals(order.getTitle().getValue()) || CategoryType.BIGTHING.getValue().equals(order.getTitle().getValue())) {
				List<OrderItem> OrderItemList = orderItemService.selectByOrderId(orderId);
				resultMap.put("OrderItemList", OrderItemList);
			} else {
				//查询订单表的分了明细表
				List<OrderItemAch> orderItemAchList = orderItemAchService.selectByOrderId(orderId);
				resultMap.put("OrderItemList", orderItemAchList);
			}
			if (order.getTitle() == Order.TitleType.HOUSEHOLD){
				order.setPrice(order.getAchPrice());
			}
			resultMap.put("order", order);
			resultMap.put("orderPicList", orderPicAchList);
			resultMap.put("orderUserPicList", orderPicList);
			return resultMap;
		}
		//查询订单表的关联图片表
		List<OrderPic> orderPicList = orderPicService.selectbyOrderId(orderId);
		//查询订单表的分了明细表
		List<OrderItem> OrderItemList = orderItemService.selectByOrderId(orderId);
		resultMap.put("order", order);
		resultMap.put("orderPicList", orderPicList);
		resultMap.put("OrderItemList", OrderItemList);
		//回收员取消任务表中找最新取消任务的回收员名称
		OrderBean o = new OrderBean();
		o.setId(orderId);
		CancelResult cancelResult = recyclerCancelLogService.selectCancel(o);
		if(cancelResult != null){
			resultMap.put("cancelTime", cancelResult.getCancelDate());
			resultMap.put("cancelNameLast", cancelResult.getRecycleName());
		}
		return resultMap;
	}


	/**
	 * 派单、驳回、接单 接口(企业)
	 *
	 * @param
	 * @return
	 * @author 王灿
	 */
	@Transactional
	@Override
	public String updateOrderByBusiness(Integer orderId, String status, String cancelReason, Integer recyclerId) {
		Order order = orderMapper.selectById(orderId);
		//修改订单日志表的
		OrderLog orderLog = new OrderLog();
		orderLog.setOpStatusBefore(order.getStatus().toString());
		orderLog.setOrderId(order.getId().intValue());
		String res = "SUCCESS";
		switch (status) {
			case "REJECTED":
				order.setStatus(OrderType.REJECTED);
				//驳回原因
				order.setCancelReason(cancelReason);
				//驳回时间
				order.setCancelTime(new Date());
				order.setIsRead("0");
				orderLog.setOpStatusAfter("REJECTED");
				orderLog.setOp("已驳回");
				this.updateById(order);
				//判断是否有券码
				if(!StringUtils.isBlank(order.getEnterpriseCode())){
					EnterpriseCode enterpriseCode = enterpriseCodeService.selectOne(new EntityWrapper<EnterpriseCode>().eq("code", order.getEnterpriseCode()).eq("del_flag", 0).eq("is_use",1));
					//判断券码是否存在并且未使用
					if(null!=enterpriseCode){
						enterpriseCode.setIsUse("0");
						enterpriseCodeService.updateById(enterpriseCode);
					}
				}
				try{
					//查询回收人员信息推送消息
					Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
					if(null!=recyclers){
						PushUtils.getAcsResponse(recyclers.getTel(),order.getStatus().getValue()+"",order.getTitle().getValue()+"");
					}
				}catch (Exception e){
					e.printStackTrace();
				}
				try {
					xcxWebSocketServer.pushXcxDetail(order.getMemberId().toString(),"user", this.isUserOrder(order.getMemberId().toString()));
				}catch (Exception e){
					e.printStackTrace();
				}
				break;
			case "ALREADY":
				order.setStatus(OrderType.ALREADY);
				order.setReceiveTime(new Date());
				orderLog.setOpStatusAfter("ALREADY");
				orderLog.setOp("已接单");
				this.updateById(order);
				break;
			case "TOSEND":
				if (order.getStatus().name().equals("INIT")) {
					order.setStatus(OrderType.TOSEND);
				} else {
					throw new ApiException("该订单已被操作，请刷新页面查看状态");
				}

				//异步保存至redis中(订单号，派单时间)
				asyncRedis.saveOrRemoveOrderIdAndTimeFromRedis(order.getId(), recyclerId.longValue(), System.currentTimeMillis(), "save");

				order.setRecyclerId(recyclerId);
				order.setDistributeTime(new Date());
				order.setIsRead("0");
				orderLog.setOpStatusAfter("TOSEND");
				orderLog.setOp("已派单");
				order.setIsDistributed("1");//是否被派送过
				this.updateById(order);//放这里的目的是为了取得order中回收员id
				CommToken commToken = orderMapper.getCommToken(order.getOrderNo());
				if (commToken != null) {
					xingeService.sendPostMessage("您有一笔新的收呗订单", "订单来自" + commToken.getCommName() + "，点击查看", commToken.getTencentToken(), XingeMessageCode.order);
				}
				//查询回收人员信息
				Recyclers recyclers = recyclersService.selectById(recyclerId);
				PushUtils.getAcsResponse(recyclers.getTel(),order.getStatus().getValue()+"",order.getTitle().getValue()+"");
				try {
					xcxWebSocketServer.pushXcxDetail(order.getMemberId().toString(),"user", this.isUserOrder(order.getMemberId().toString()));
				}catch (Exception e){
					e.printStackTrace();
				}
				break;
			default:
				res = "传入的订单状态不对";
				break;
		}
		//boolean bool = this.updateById(order);
		//修改订单日志表的
		orderLogService.insert(orderLog);
		return res;
	}

	@Override
	public Map<String, Object> getAppOrderList(OrderBean orderbean) {
		int count = 0;
		List<AppOrderResult> listOrder = null;
		if (orderbean.getStatus() != null && "6".equals(orderbean.getStatus())) {
			count = orderMapper.getAppOrderCancelTaskCount(orderbean);
			listOrder = orderMapper.getAppOrderCancelTaskList(orderbean, (orderbean.getPagebean().getPageNumber() - 1) * orderbean.getPagebean().getPageSize(), orderbean.getPagebean().getPageSize());
		} else {
			count = orderMapper.getAppOrderCount(orderbean);//得到总数
			listOrder = orderMapper.getAppOrderList(orderbean, (orderbean.getPagebean().getPageNumber() - 1) * orderbean.getPagebean().getPageSize(), orderbean.getPagebean().getPageSize());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		int pageNum = count % orderbean.getPagebean().getPageSize() == 0 ? count / orderbean.getPagebean().getPageSize() : count / orderbean.getPagebean().getPageSize() + 1;
		int currentpage = orderbean.getPagebean().getPageNumber();
		map.put("pageNum", pageNum);
		map.put("count", count);
		map.put("listOrder", this.createName4App(listOrder));
		map.put("currentPage", currentpage > pageNum ? pageNum : currentpage);
		return map;
	}
	/**
	 * 根据订单id查询订单的详细信息
	 */
	@Override
	public  Map<String, Object> getBigOrderDetails(Integer orderId){
		Map<String, Object> resultMap = new HashMap<>();
		AppOrderResult result = orderMapper.getBigOrderDetails(orderId);
		OrderBean orderBean = new OrderBean();
		orderBean.setId(orderId);
		List<AttrItem> bigOrderAttrItem = orderMapper.getOrderItemList(orderBean);
		if("3".equals(result.getStatus())){
			List<Map<String, Object>> list = orderMapper.getOrderAchUrlList(orderBean);
			resultMap.put("bigUrlList",list);
		}else{
			List<AttrItem> orderUrlList = orderMapper.getOrderUrlList(orderBean);
			resultMap.put("bigUrlList",orderUrlList);
		}
		resultMap.put("bigOrder",result);
		resultMap.put("bigItemOrder",bigOrderAttrItem);
		return resultMap;
	}

	@Override
	public AppOrderResult getOrderDetails(OrderBean orderbean) {
		AppOrderResult result = orderMapper.getOrderDetails(orderbean);
		orderbean.setTitle(result.getTitle().toString());
		orderbean.setIsCash(result.getIsCash());
		//根据小区的Id查询精度和纬度
		Community community = communityService.selectById(result.getCommunityId());
		if (community != null) {
			result.setLongitude(community.getLongitude() == null ? null : community.getLongitude().toString());
			result.setLatitude(community.getLatitude() == null ? null : community.getLatitude().toString());
		} else {
			result.setLongitude("0");
			result.setLatitude("0");
		}
		if (result != null) {
			if (orderbean.getStatus() != null && "6".equals(orderbean.getStatus())) {
				result.setStatus("6");
			}
			if (result.getTitle() == CategoryType.HOUSEHOLD) {
				Map<String, Object> map = this.createPriceAmount(orderbean);
				List<Map<String, Object>> priceAndAmount = (List<Map<String, Object>>) map.get("listMapObject");
				result.setPriceAndAmount(priceAndAmount);
				//result.setGreenCount(map.get("greenCount"));
			} else if (result.getTitle() == CategoryType.DIGITAL) {
				orderbean.setId(Integer.parseInt(result.getOrderId()));
				List<AttrItem> orderAttrItem = orderMapper.getOrderItemList(orderbean);
				result.setAttrItemlist(orderAttrItem);

			}
			if ("3".equals(orderbean.getStatus())) {
				List<Map<String, Object>> list = orderMapper.getOrderAchUrlList(orderbean);
				result.setObj(list);
			} else {
				List<AttrItem> orderUrlItem = orderMapper.getOrderUrlList(orderbean);
				result.setObj(orderUrlItem);
			}
		} else {
			throw new ApiException("请检查参数是否正常");
		}
		//根据订单Id查询订单交易价格
		Object paymentPrice = orderMapper.paymentPriceByOrderId(orderbean.getId());
		if(paymentPrice==null){
			paymentPrice = 0;
		}
		result.setPaymentPrice(paymentPrice);
		return result;
	}

	@Override
	public boolean modifyOrder(OrderBean orderBean) {
		Order order = this.selectById(orderBean.getId());
		if (orderBean.getArrivalTime() != null && !"".equals(orderBean.getArrivalTime())) {
			try {
				order.setArrivalTime(new SimpleDateFormat("yyyy-MM-dd").parse(orderBean.getArrivalTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		boolean flag = this.updateById(order);
		if (!flag) {
			throw new ApiException("修改失敗");
		}
		try {
			xcxWebSocketServer.pushXcxDetail(order.getMemberId().toString(),"detalis","上门时间被修改，请及时查看");
		}catch (Exception e){
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public AppOrderResult getRecord(TimeBean timeBean) {
		AppOrderResult result = null;
		if (timeBean.getDate() == null || "".equals(timeBean.getDate())) {
			timeBean.setDate(new Date());
			result = orderMapper.getRecord(timeBean);
			result.setCurrentTime(new SimpleDateFormat("yyyy-MM").format(new Date()));
		} else {
			result = orderMapper.getRecord(timeBean);
			result.setCurrentTime(new SimpleDateFormat("yyyy-MM").format(timeBean.getDate()));
		}
		return result;
	}

	@Override
	public AppScoreResult getScoreEvaRate(ScoreAppBean scoreAppBean) {

		if (scoreAppBean.getScore() != null && !"".equals(scoreAppBean.getScore())) {
			Integer score = Integer.valueOf(scoreAppBean.getScore());
			List<Integer> scoreList = new ArrayList<>();
			if (score >= 4) {
				//scoreAppBean.setScore("'5', '4'");
				scoreList.add(5);
				scoreList.add(4);
			} else if (score == 3) {
				scoreList.add(3);
			} else if (score <= 2) {
				scoreList.add(1);
				scoreList.add(2);
			} else {
				return null;
			}
			scoreAppBean.setScoreList(scoreList);
		}
		AppScoreResult scoreResult = orderMapper.getEvaRate(scoreAppBean);
		Integer count = orderMapper.getRecordNum(scoreAppBean);

		Map<String, Object> map = new HashMap<String, Object>();
		List<EvaluationResult> listEvaluation = orderMapper.getScore(scoreAppBean, (scoreAppBean.getPageBean().getPageNumber() - 1) * scoreAppBean.getPageBean().getPageSize(), scoreAppBean.getPageBean().getPageSize());
		int pageNum = count % scoreAppBean.getPageBean().getPageSize() == 0 ? count / scoreAppBean.getPageBean().getPageSize() : count / scoreAppBean.getPageBean().getPageSize() + 1;
		int currentpage = scoreAppBean.getPageBean().getPageNumber();
		map.put("pageNum", pageNum);
		map.put("count", count);
		map.put("listEvaluation", listEvaluation);
		map.put("currentPage", currentpage > pageNum ? pageNum : currentpage);

		scoreResult.setEvaList(map);
		return scoreResult;
	}
	@Override
	public boolean modifyOrderSta(OrderBean orderBean) {
		OrderLog orderLog = new OrderLog();
		orderLog.setOrderId(orderBean.getId());
		String descrb = "";
		Order order = orderService.selectById(orderBean.getId());
		if((order.getTitle().getValue()+"").equals("1")||(order.getTitle().getValue()+"").equals("4")){
			Category category = categoryService.selectById(order.getCategoryId());
			descrb = category.getName();
		}else{
			descrb = "生活垃圾";
		}

		if (order.getStatus() != null && !"".equals(order.getStatus())) {
			orderLog.setOpStatusBefore(order.getStatus().name());
		}
		order.setUpdateDate(new Date());
		order.setIsRead("0");
		boolean flag = false;
		if (orderBean.getStatus() != null && !"".equals(orderBean.getStatus())) {
			switch (orderBean.getStatus()) {
				case "6"://已取消任务
					Recyclers recycler = recyclersService.selectById(order.getRecyclerId());
					if ("1".equals(order.getStatus().getValue().toString())) {
						//判断是否是回收经理还是回收人员取消订单
						if ("1".equals(recycler.getIsManager())) {
							//是经理
							order.setRecyclerId(0);
							order.setStatus(OrderType.INIT);
						} else {
							//是下属回收人员
							order.setStatus(OrderType.TOSEND);
							order.setRecyclerId(recycler.getParentsId());
							//阿里云推送
							Recyclers recyclers = recyclersService.selectById(recycler.getParentsId());
							PushUtils.getAcsResponse(recyclers.getTel(),order.getStatus().getValue()+"",order.getTitle().getValue()+"");
						}
					} else {
						throw new ApiException("该订单已被操作，请刷新页面查看状态");
					}

					//異步刪除redis裡面派單id
					asyncRedis.saveOrRemoveOrderIdAndTimeFromRedis(order.getId(), recycler.getId(), System.currentTimeMillis(), "remove");

					if (orderBean.getCancelReason() != null && !"".equals(orderBean.getCancelReason())) {
						order.setCancelReason(orderBean.getCancelReason());
					} else {
						throw new ApiException("没有取消原因");
					}
					RecyclerCancelLog cancelLog = new RecyclerCancelLog();
					cancelLog.setCancelReason(orderBean.getCancelReason());
					cancelLog.setRecycleId(recycler.getId().toString());
					cancelLog.setOrderId(order.getId().toString());
					recyclerCancelLogService.insert(cancelLog);


					flag = orderService.updateById(order);
					orderLog.setOpStatusAfter("CANCELTASK");
					orderLog.setOp("已取消任务");
					break;
				case "3"://已完成
					if ("2".equals(order.getStatus().getValue().toString())) {
						order.setStatus(OrderType.COMPLETE);
					} else {
						throw new ApiException("该订单已被操作，请刷新页面查看状态");
					}
					order.setCompleteDate(new Date());
					if(StringUtils.isNotBlank(orderBean.getAchPrice())){
						order.setAchPrice(new BigDecimal(orderBean.getAchPrice()));
					}
					flag = orderService.updateById(order);
					orderLog.setOpStatusAfter("COMPLETE");
					orderLog.setOp("已完成");

					this.updateMemberPoint(order.getMemberId(), order.getOrderNo(), orderBean.getAmount(),descrb);

					break;
				case "2":// 已接单
					if ("1".equals(order.getStatus().getValue().toString())) {
						order.setStatus(OrderType.ALREADY);
					} else {
						throw new ApiException("该订单已被操作，请刷新页面查看状态");
					}
					order.setReceiveTime(new Date());
					flag = orderService.updateById(order);
					orderLog.setOpStatusAfter("ALREADY");
					orderLog.setOp("已接单");
					//查询此单属于具体某个回收人员
					Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
					if (recyclers != null) {
						Company company = companyService.selectById(order.getCompanyId());
						if (company != null) {
							//发送接单短信
							asyncService.sendOrder("垃圾分类回收", order.getTel(), "SMS_142151759", recyclers.getName(), recyclers.getTel(), company.getName());
						}
						//異步刪除redis裡面派單id
						asyncRedis.saveOrRemoveOrderIdAndTimeFromRedis(order.getId(), recyclers.getId(), System.currentTimeMillis(), "remove");
					}
					break;
				default:
					throw new ApiException("不能修改为此状态");
			}
			try {
				xcxWebSocketServer.pushXcxDetail(order.getMemberId().toString(),"user", this.isUserOrder(order.getMemberId().toString()));
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		//修改日志列表
		flag = orderLogService.insert(orderLog);
		return flag;
	}
	@Override
	public String callbackForGround(OrderBean orderBean){
		try {
			OrderLog orderLog = new OrderLog();
			Order order = orderService.selectById(orderBean.getId());
			if (order.getStatus() != null && !"".equals(order.getStatus())) {
				orderLog.setOpStatusBefore(order.getStatus().name());
				if("0".equals(order.getStatus().getValue().toString())) {
					return "初始状态，不允许回调";
				}else if ("3".equals(order.getStatus().getValue().toString())){
					return "已完成，不允许回调";
				}
			}
			order.setUpdateDate(new Date());
			order.setIsRead("0");
			order.setRecyclerId(0);
			order.setStatus(OrderType.INIT);
			order.setCancelReason("订单回调");
			order.setCancelTime(new Date());
			orderLog.setOpStatusAfter(OrderType.INIT.name());
			orderLog.setOp("订单回调");
			orderLog.setOrderId(Integer.parseInt(order.getId().toString()));
			if(!orderService.updateById(order)){
				return "订单已被修改，稍后重试";
			}
			orderLogService.insert(orderLog);
			return "修改成功";
		}catch (Exception e){
			return "订单已被修改，稍后重试";
		}
	}

	/**
	 * @param memberId:用户Id
	 * @param OrderNo:订单编号
	 * @author 王灿
	 */
	public void updateMemberPoint(Integer memberId, String OrderNo, double amount,String descrb) {
		DecimalFormat df   = new DecimalFormat("######0.00");
		amount = Double.parseDouble(df.format(amount));

		Point points = pointService.getPoint(memberId);
		if (points == null) {
			points = new Point();
			points.setMemberId(memberId);
			points.setPoint(amount);
			points.setRemainPoint(amount);
			pointService.insert(points);
		} else {
			points.setPoint(points.getPoint() + amount);
			points.setRemainPoint(points.getRemainPoint() + amount);
			pointService.updateById(points);
		}
		//给用户增加会员卡积分
		try {
			Member member = memberService.selectById(memberId);
			System.out.println("给用户增加的积分是 ：" + amount + "----point: " +Double.parseDouble(df.format(points.getPoint()))+ "");
			aliPayService.updatePoint(member.getAliCardNo(), member.getOpenCardDate(), Double.parseDouble(df.format(points.getPoint())) + "", null, member.getAppId());
		} catch (Exception e) {
			System.out.println("给用户增加积分失败---------------");
		}
		PointList pointList = new PointList();
		pointList.setMemberId(memberId);
		pointList.setPoint(amount + "");
		pointList.setType("0");
		pointList.setDocumentNo(OrderNo);
		pointList.setDescrb(descrb);
		pointListService.insert(pointList);
		//记录用户picc能量
		PiccWater piccWater = new PiccWater();
		piccWater.setMemberId(memberId);
		piccWater.setStatus("0");
		piccWater.setPointCount((int)amount);
		piccWater.setDescrb(descrb);
		piccWaterService.insert(piccWater);
	}


	/**
	 * 订单数据看板折线图
	 *
	 * @param
	 * @return
	 * @author 王灿
	 */
	@Override
	public List<Map<String, Object>> getBrokenLineData(CompanyBean companyBean) {
		//D 查询昨天  W 代表查近一周 M代表查近一个月
		String date = companyBean.getDate();
		String companyId = companyBean.getId();

		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
		String startTime = dft.format(cal.getTime());
		String endTime = null;
		List<Map<String, Object>> list = null;
		if ("D".equals(date)) {
			//表示查昨天的
			list = orderMapper.getBrokenData(companyId, startTime);
		} else if ("W".equals(date)) {
			//表示查近一周的
			cal.setTime(time);
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 8);
			endTime = dft.format(cal.getTime());
			list = orderMapper.getBrokenWeek(companyId, startTime, endTime);
		} else if ("M".equals(date)) {
			//表示查近一个月
			cal.setTime(time);
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 31);
			endTime = dft.format(cal.getTime());
			list = orderMapper.getBrokenMath(companyId, startTime, endTime);
		}

		return list;
	}

	public Map<String, Object> createPriceAmount(OrderBean orderbean) {
		//查询订单表的分了明细表
		List<OrderItemAch> OrderItemAchList = null;
		List<OrderItem> OrderItemList = null;
		//订单是否免费   1是免费
		String isCash = orderbean.getIsCash();
		//判断是否是完成的订单
		if ("3".equals(orderbean.getStatus())) {
			//获取订单完成的数据
			OrderItemAchList = orderItemAchService.selectByOrderId(orderbean.getId());
		} else {
			//未完成订单的数据
			OrderItemList = orderItemService.selectByOrderId(orderbean.getId());
		}
		List<ComCatePrice> cateName = null;
		if (("IOTORDER".equals(orderbean.getTitle())||"FIVEKG".equals(orderbean.getTitle())||"HOUSEHOLD".equals(orderbean.getTitle())) && "3".equals(orderbean.getStatus())) {
			cateName = orderItemService.selectCateAchName(orderbean.getId());
		} else {
			cateName = orderItemService.selectCateName(orderbean.getId());
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> listMapObject = new ArrayList<>();
		List<Map<String, String>> listMap = null;
		Map<String, String> map = null;
		float price = 0l;
		Map<String, Object> mapListMap = null;
		//实际重量
		double achAmount = 0;
		//首先取得分类name, 去重
		for (ComCatePrice name : cateName) {
			String categoryName = "";
			int count = 0;
			price = 0l;
			mapListMap = new HashMap<>();
			listMap = new ArrayList<>();
			//遍历订单的数据，获取价格，名字，和数量
			if ("3".equals(orderbean.getStatus())) {
				for (OrderItemAch list : OrderItemAchList) {
					map = new HashMap<>();
					//listMap = new ArrayList<>();
					if (name.getId() == list.getParentId()) {
						map.put("cateName", list.getCategoryName());
						map.put("price", list.getPrice() + "");
						map.put("unit", list.getUnit());
						map.put("amount", list.getAmount() + "");
						price += list.getPrice() * list.getAmount();
						achAmount += list.getAmount();
						if (list.getPrice() == 0 && !ToolUtils.categoryName.equals(name.getName())) {
							categoryName += list.getCategoryName() + "/";
						} else{
							listMap.add(map);
						}
						count++;
					}
				}
			} else {
				for (OrderItem list : OrderItemList) {
					map = new HashMap<>();
					//listMap = new ArrayList<>();
					if (name.getId() == list.getParentId()) {
						map.put("cateName", list.getCategoryName());
						map.put("price", list.getPrice() + "");
						map.put("unit", list.getUnit());
						map.put("amount", list.getAmount() + "");
						price += list.getPrice() * list.getAmount();
						if (list.getPrice() == 0) {
							categoryName += list.getCategoryName() + "/";
						} else {
							listMap.add(map);
						}
						count++;
					}
				}
			}

			mapListMap.put("categoryName", StringUtils.isBlank(categoryName) ? "" : categoryName.substring(0, categoryName.length() - 1));
			mapListMap.put("count", count);
			mapListMap.put("list", listMap);
			mapListMap.put("name", name == null ? null : name.getName());
			mapListMap.put("price", price);
			listMapObject.add(mapListMap);
		}
		resultMap.put("listMapObject", listMapObject);
		try{
			resultMap.put("achAmount",new DecimalFormat("#.00").format(achAmount));
		}catch(Exception e){
			resultMap.put("achAmount",achAmount);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> getInitDetail(Integer orderId) {
		//未完成订单的数据
		List<OrderItem> OrderItemList = orderItemService.selectByOrderId(orderId);
		List<ComCatePrice> cateName = orderItemService.selectCateName(orderId);
		Map<String, String> map = null;
		float price = 0l;
		List<Map<String, String>> listMap = null;
		Map<String, Object> mapListMap = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> listMapObject = new ArrayList<>();
		//首先取得分类name, 去重
		for (ComCatePrice name : cateName) {
			mapListMap = new HashMap<>();
			price = 0l;
			listMap = new ArrayList<>();
			for (OrderItem list : OrderItemList) {
				map = new HashMap<>();
				//listMap = new ArrayList<>();
				if (name.getId() == list.getParentId()) {
					map.put("cateName", list.getCategoryName());
					map.put("price", list.getPrice() + "");
					map.put("unit", list.getUnit());
					map.put("amount", list.getAmount() + "");
					price += list.getPrice() * list.getAmount();
					listMap.add(map);
				}
			}
			mapListMap.put("list", listMap);
			mapListMap.put("name", name == null ? null : name.getName());
			mapListMap.put("price", price);
			listMapObject.add(mapListMap);
		}
		resultMap.put("listMapObject", listMapObject);
		return resultMap;
	}
	private Map<String, Object> createPriceAmount4PC(OrderBean orderbean) {
		//是否是免费
		String isCash = orderbean.getIsCash();
		//查询订单表的分了明细表
		List<OrderItemAch> OrderItemAchList = null;
		List<OrderItem> OrderItemList = null;
		//判断是否是完成的订单
		if ("3".equals(orderbean.getStatus())) {
			//获取订单完成的数据
			OrderItemAchList = orderItemAchService.selectByOrderId(orderbean.getId());
		} else {
			//未完成订单的数据
			OrderItemList = orderItemService.selectByOrderId(orderbean.getId());
		}
		List<ComCatePrice> cateName = null;
		if (("HOUSEHOLD".equals(orderbean.getTitle())||"FIVEKG".equals(orderbean.getTitle())) && "3".equals(orderbean.getStatus())) {
			cateName = orderItemService.selectCateAchName(orderbean.getId());
		} else {
			cateName = orderItemService.selectCateName(orderbean.getId());
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> listMapObject = new ArrayList<>();
		List<Map<String, String>> listMap = null;
		Map<String, String> map = null;
		float price = 0l;
		Map<String, Object> mapListMap = null;
		//定义绿色能量
		double greenCount = 0;
		//首先取得分类name, 去重
		for (ComCatePrice name : cateName) {
			String categoryName = "";
			price = 0l;
			mapListMap = new HashMap<>();
			listMap = new ArrayList<>();
			//遍历订单的数据，获取价格，名字，和数量
			if ("3".equals(orderbean.getStatus())) {
				for (OrderItemAch list : OrderItemAchList) {
					map = new HashMap<>();
					//listMap = new ArrayList<>();
					if (name.getId() == list.getParentId()) {
						map.put("cateName", list.getCategoryName());
						map.put("price", ApiUtils.doublegetTwoDecimal(list.getPrice()));
						map.put("unit", list.getUnit());
						map.put("amount", ApiUtils.doublegetTwoDecimal(Float.parseFloat(list.getAmount()+"")));
						price += list.getPrice() * list.getAmount();
						//判断是否免费
						if ("1".equals(isCash)) {
							greenCount += list.getAmount() * 2;
						} else {
							greenCount += list.getAmount();
						}
						if (list.getPrice() == 0 && !ToolUtils.categoryName.equals(name.getName())) {
							categoryName += list.getCategoryName() + "/";
						} else{
							listMap.add(map);
						}
					}
				}
			} else {
				for (OrderItem list : OrderItemList) {
					map = new HashMap<>();
					//listMap = new ArrayList<>();
					if (name.getId() == list.getParentId()) {
						map.put("cateName", list.getCategoryName());
						map.put("price", ApiUtils.doublegetTwoDecimal(list.getPrice()));
						map.put("unit", list.getUnit());
						map.put("amount", ApiUtils.doublegetTwoDecimal(Float.parseFloat(list.getAmount()+"")));
						price += list.getPrice() * list.getAmount();
						listMap.add(map);
					}
				}
			}

			mapListMap.put("categoryName", StringUtils.isBlank(categoryName) ? "" : categoryName.substring(0, categoryName.length() - 1));
			mapListMap.put("list", listMap);
			mapListMap.put("name", name == null ? null : name.getName());
			mapListMap.put("price", ApiUtils.doublegetTwoDecimal(price));
			listMapObject.add(mapListMap);
		}
		resultMap.put("listMapObject", listMapObject);
		resultMap.put("greenCount", greenCount);
		return resultMap;
	}

	/**
	 * 扫描用户会员卡卡号完成订单
	 *
	 * @param orderBean
	 * @param recyclers
	 * @return
	 * @author 王灿
	 */
	@Override
	public Object saveOrderByCardNo(OrderBean orderBean, Recyclers recyclers) {
		//根据回收人员信息查询所属关联公司
		CompanyRecycler companyRecycler = companyRecyclerService.selectOne(new EntityWrapper<CompanyRecycler>().eq("recycler_id", recyclers.getId()).eq("status_", "1"));
		//获取用户的详细信息
		Member member = memberService.selectById(orderBean.getMemberId());
		if (member == null) {
			System.out.println(orderBean.getMemberId() + "    查不到此用户");
			return "暂无此用户";
		}
		Order order = new Order();
		order.setMemberId(orderBean.getMemberId());
		order.setCompanyId(companyRecycler.getCompanyId());
		order.setRecyclerId(companyRecycler.getRecyclerId());
		order.setStatus(OrderType.COMPLETE);
		//随机生成订单号
		String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);
		order.setOrderNo(orderNo);
		order.setTel(member.getMobile() == null ? "" : member.getMobile());
		order.setPrice(orderBean.getPrice());
		order.setUnit("kg");
		order.setQty(9999);
		order.setCompleteDate(new Date());
		order.setIsEvaluated("0");
		order.setLevel("0");
		order.setArrivalTime(new Date());
		order.setArrivalPeriod("am");
		order.setAliUserId(member.getAliUserId());
		order.setIsCash("0");
		order.setIsDistributed("0");
		order.setAreaId(0);
		order.setTitle(Order.TitleType.HOUSEHOLD);
		order.setAchPrice(orderBean.getPrice());
		order.setAchRemarks(orderBean.getAchRemarks());
		order.setSignUrl(orderBean.getSignUrl());
		order.setIsScan("1");
		orderService.insert(order);
		System.out.println(order.getId());
		//储存图片链接
		OrderPic orderPic = orderBean.getOrderPic();
		if (orderPic != null) {
			String origPics = orderPic.getOrigPic();
			String picUrl = orderPic.getPicUrl();
			String smallPic = orderPic.getSmallPic();
			String[] origPicss = origPics.split(",");
			String[] picUrls = picUrl.split(",");
			String[] smallPics = smallPic.split(",");
			OrderPic orderPicc = null;
			OrderPicAch orderPiccAch = null;
			for (int i = 0; i < origPicss.length; i++) {
				orderPicc = new OrderPic();
				orderPicc.setOrigPic(origPicss[i]);
				orderPicc.setPicUrl(picUrls[i]);
				orderPicc.setSmallPic(smallPics[i]);
				orderPicc.setOrderId(Integer.parseInt(order.getId() + ""));
				orderPicService.insert(orderPicc);
				orderPiccAch = new OrderPicAch();
				orderPiccAch.setOrigPic(origPicss[i]);
				orderPiccAch.setPicUrl(picUrls[i]);
				orderPiccAch.setSmallPic(smallPics[i]);
				orderPiccAch.setOrderId(Integer.parseInt(order.getId() + ""));
				orderPicAchService.insert(orderPiccAch);
			}
		}
		//得到用户的垃圾总量
		double amount = 0;
		//储存回收人员提交的信息
		List<OrderItemBean> orderItemList = orderBean.getOrderItemList();
		if (!orderItemList.isEmpty()) {
			for (OrderItemBean orderItemBean : orderItemList) {
				//根据分类Id和公司id查询分类信息
				CompanyCategory companyCategory = companyCategoryService.selectOne(new EntityWrapper<CompanyCategory>().eq("category_id", orderItemBean.getCategoryId()).eq("company_id", companyRecycler.getCompanyId()).eq("del_flag", 0));
				Category category = categoryService.selectById(orderItemBean.getCategoryId());
				OrderItem orderItem = new OrderItem();
				orderItem.setOrderId(Integer.parseInt(order.getId() + ""));
				orderItem.setCategoryId(Integer.parseInt(category.getId() + ""));
				orderItem.setCategoryName(category.getName());
				orderItem.setParentId(category.getParentId());
				orderItem.setParentIds(category.getParentIds());
				orderItem.setParentName(orderItemBean.getParentName());
				orderItem.setAmount(orderItemBean.getAmount());
				orderItem.setUnit(category.getUnit());
				orderItem.setPrice(companyCategory.getPrice());
				orderItemService.insert(orderItem);
				OrderItemAch orderItemAch = new OrderItemAch();
				orderItemAch.setOrderId(Integer.parseInt(order.getId() + ""));
				orderItemAch.setCategoryId(Integer.parseInt(category.getId() + ""));
				orderItemAch.setCategoryName(category.getName());
				orderItemAch.setParentId(category.getParentId());
				orderItemAch.setParentIds(category.getParentIds());
				orderItemAch.setParentName(orderItemBean.getParentName());
				orderItemAch.setAmount(orderItemBean.getAmount());
				orderItemAch.setUnit(category.getUnit());
				orderItemAch.setPrice(companyCategory.getPrice());
				orderItemAchService.insert(orderItemAch);
				amount += orderItemBean.getAmount();
			}
		}
		order.setGreenCount(amount);
		this.updateById(order);
		//给用户增加积分
		this.updateMemberPoint(order.getMemberId(), order.getOrderNo(), amount,"定点回收物");

		if (orderBean.getPrice().compareTo(BigDecimal.ZERO)==0){
			//给用户增加蚂蚁能量
			OrderBean orderBeans = orderService.myslOrderData(order.getId().toString());
			return "操作成功";
		}
		return order.getId();
	}
	/**
	 * 回收经理转派订单
	 *
	 * @param orderId    : 订单id
	 * @param recyclerId : 需要转派的回收人员id
	 * @return
	 * @author 王灿
	 */
	public Object distributeOrder(Integer orderId, Integer recyclerId) {
		Order order = this.selectById(orderId);
		if (null == order) {
			return "未找到该订单 id：" + orderId;
		}
		order.setRecyclerId(recyclerId);
		boolean b = this.updateById(order);
		if (!b) {
			return "转派失败";
		}
		Recyclers recyclers = recyclersService.selectById(recyclerId);
		PushUtils.getAcsResponse(recyclers.getTel(),order.getStatus().getValue()+"",order.getTitle().getValue()+"");
		return "转派成功";
	}

	/**
	 * 转派订单列表
	 *
	 * @param recyclerId : 需要转派的回收人员id
	 * @return
	 * @author 王灿
	 */
	public Object distributeOrderList(Integer recyclerId) {
		return orderMapper.distributeOrderList(recyclerId);
	}

	/**
	 * 小程序保存六废订单
	 *
	 * @param
	 * @return
	 * @author 王灿
	 */
	public Object XcxSaveOrder(OrderBean orderBean, Member member) {
		Map<String,Object> resultMap = new HashMap<>();
		Order order = new Order();
		try {
			if (StringUtils.isNotBlank(orderBean.getArrivalTime())) {
				order.setArrivalTime(new SimpleDateFormat("yyyy-MM-dd").parse(orderBean.getArrivalTime()));
				order.setArrivalPeriod(orderBean.getArrivalPeriod());
			} else {
				Date date = Calendar.getInstance().getTime();
				int i = Integer.parseInt(new SimpleDateFormat("HH").format(date));
				order.setArrivalTime(date);
				if (i < 12) {
					order.setArrivalPeriod("am");
				} else if(20 <= i){
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
					order.setArrivalTime(cal.getTime());
					order.setArrivalPeriod("am");
				}else {
					order.setArrivalPeriod("pm");
				}
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

			order.setMemberId(member.getId().intValue());
			order.setCompanyId(orderBean.getCompanyId());
			order.setOrderNo(orderBean.getOrderNo());
			order.setCategoryId(orderBean.getCategoryId());
			order.setAreaId(orderBean.getAreaId());
			order.setStreetId(orderBean.getStreetId());
			order.setCommunityId(orderBean.getCommunityId());
			order.setAddress(orderBean.getAddress());
			order.setFullAddress(orderBean.getFullAddress());
			order.setTel(orderBean.getTel());
			order.setLinkMan(orderBean.getLinkMan());
			order.setUnit("kg");
			order.setQty(9999);
			order.setPrice(BigDecimal.ZERO);
			order.setLevel(orderBean.getLevel());
			order.setGreenCode(orderBean.getGreenCode());
			order.setAliUserId(member.getAliUserId());
			order.setTitle(Order.TitleType.HOUSEHOLD);
			order.setIsMysl(orderBean.getIsMysl());
			if ("1".equals(orderBean.getIsCash())) {
				order.setIsCash(orderBean.getIsCash());
			} else {
				order.setIsCash("0");
			}
			this.insert(order);
			System.out.println(order.getId());
			//储存图片链接
			OrderPic orderPic = orderBean.getOrderPic();
			if (orderPic != null) {
				String origPics = orderPic.getOrigPic();
				String picUrl = orderPic.getPicUrl();
				String smallPic = orderPic.getSmallPic();
				String[] origPicss = origPics.split(",");
				String[] picUrls = picUrl.split(",");
				String[] smallPics = smallPic.split(",");
				OrderPic orderPicc = null;
				for (int i = 0; i < origPicss.length; i++) {
					orderPicc = new OrderPic();
					orderPicc.setOrigPic(origPicss[i]);
					orderPicc.setPicUrl(picUrls[i]);
					orderPicc.setSmallPic(smallPics[i]);
					orderPicc.setOrderId(Integer.parseInt(order.getId() + ""));
					orderPicService.insert(orderPicc);
				}
			}
			//得到用户的垃圾总量
			double amount = 0;
			//储存回收人员提交的信息
			List<OrderItemBean> orderItemList = orderBean.getOrderItemList();
			if (!orderItemList.isEmpty()) {
				for (OrderItemBean orderItemBean : orderItemList) {
					//根据分类Id查询分类信息
					Category category = categoryService.selectById(orderItemBean.getId());
					OrderItem orderItem = new OrderItem();
					orderItem.setOrderId(Integer.parseInt(order.getId() + ""));
					orderItem.setCategoryId(Integer.parseInt(category.getId() + ""));
					orderItem.setCategoryName(category.getName());
					orderItem.setParentId(category.getParentId());
					orderItem.setParentIds(category.getParentIds());
					orderItem.setParentName(orderItemBean.getParentName());
					orderItem.setAmount(1);
					orderItem.setUnit(category.getUnit());
					orderItem.setPrice(StringUtils.isBlank(orderItemBean.getPrice())?category.getPrice().floatValue():Float.parseFloat(orderItemBean.getPrice()));
					orderItemService.insert(orderItem);
					amount += orderItemBean.getAmount();
				}
			}else {
				throw new ApiException("网络异常，请重新提交订单");
			}
		//储存订单的日志
		OrderLog orderLog = new OrderLog();
		orderLog.setOrderId(Integer.parseInt(order.getId().toString()));
		orderLog.setOpStatusAfter("INIT");
		orderLog.setOp("待接单");
		orderLogService.insert(orderLog);

		resultMap.put("msg","操作成功");
		resultMap.put("code",0);
		resultMap.put("id",order.getId());
		return resultMap;
	}
	/**
	 * 保存5公斤废纺衣物的订单
	 * @return
	 */
	@Override
	public Object savefiveKgOrder(OrderBean orderBean) {
		Map<String,Object> resultMap = new HashMap<>();
		boolean flag = false;
		//根据分类Id查询物流公司分类的信息
		CompanyCategory companyCategory = companyCategoryService.selectOne(new EntityWrapper<CompanyCategory>().eq("category_id", orderBean.getCategoryId()).eq("del_flag", 0).eq("company_id", orderBean.getCompanyId()));
		//根据分类Id查询分类信息
		Category categorys = categoryService.selectById(orderBean.getCategoryId());
		Category parentCategory = categoryService.selectById(categorys.getParentId());
		Order order = new Order();
		try{
			order.setArrivalTime(new SimpleDateFormat("yyyy-MM-dd").parse(orderBean.getArrivalTime()));
			order.setArrivalPeriod(orderBean.getArrivalPeriod());
		}catch(Exception e){
			e.printStackTrace();
		}
		try {
			order.setIsMysl(orderBean.getIsMysl());
			order.setMemberId(orderBean.getMemberId());
			order.setOrderNo(orderBean.getOrderNo());
			order.setAreaId(orderBean.getAreaId());
			order.setStreetId(orderBean.getStreetId());
			order.setCommunityId(orderBean.getCommunityId());
			order.setCategoryId(Integer.parseInt(orderBean.getCategoryParentIds()));
			order.setCategoryParentIds(orderBean.getCategoryParentIds());
			order.setUnit(companyCategory.getUnit());
			order.setPrice(new BigDecimal(companyCategory.getPrice()));
			order.setAliUserId(orderBean.getAliUserId());
			order.setTitle(Order.TitleType.FIVEKG);
			order.setCompanyId(orderBean.getCompanyId());

			order.setExpressAmount(new BigDecimal(orderBean.getQty()));
			order.setAddress(orderBean.getAddress());
			order.setFullAddress(orderBean.getFullAddress());
			order.setTel(orderBean.getTel());
			order.setLinkMan(orderBean.getLinkMan());
			order.setQty(orderBean.getQty());
			order.setRemarks(orderBean.getRemarks());
			this.insert(order);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("msg","FAIL");
			return resultMap;
		}

		//储存图片链接
		OrderPic orderPic = orderBean.getOrderPic();
		if (orderPic != null) {
			String origPics = orderPic.getOrigPic();
			String picUrl = orderPic.getPicUrl();
			String smallPic = orderPic.getSmallPic();
			String[] origPicss = origPics.split(",");
			String[] picUrls = picUrl.split(",");
			String[] smallPics = smallPic.split(",");
			OrderPic orderPicc = null;
			for (int i = 0; i < origPicss.length; i++) {
				orderPicc = new OrderPic();
				orderPicc.setOrigPic(origPicss[i]);
				orderPicc.setPicUrl(picUrls[i]);
				orderPicc.setSmallPic(smallPics[i]);
				orderPicc.setOrderId(Integer.parseInt(order.getId() + ""));
				orderPicService.insert(orderPicc);
			}
		}
		//储存回收人员提交的信息
		List<OrderItemBean> orderItemList = orderBean.getOrderItemList();
		if (!orderItemList.isEmpty()) {
			for (OrderItemBean orderItemBean : orderItemList) {
				//根据分类Id查询分类信息
				Category category = categoryService.selectById(orderItemBean.getId());
				OrderItem orderItem = new OrderItem();
				orderItem.setOrderId(Integer.parseInt(order.getId() + ""));
				orderItem.setCategoryId(Integer.parseInt(category.getId() + ""));
				orderItem.setCategoryName(category.getName());
				orderItem.setParentId(category.getParentId());
				orderItem.setParentIds(category.getParentIds());
				orderItem.setParentName(orderItemBean.getParentName());
				orderItem.setAmount(orderBean.getQty());
				orderItem.setUnit(category.getUnit());
				orderItem.setPrice(StringUtils.isBlank(orderItemBean.getPrice())?category.getPrice().floatValue():Float.parseFloat(orderItemBean.getPrice()));
				orderItemService.insert(orderItem);
			}
		}else {
			throw new ApiException("网络异常，请重新提交订单");
		}
		//储存订单的日志
		OrderLog orderLog = new OrderLog();
		orderLog.setOrderId(Integer.parseInt(order.getId().toString()));
		orderLog.setOpStatusAfter("INIT");
		orderLog.setOp("待接单");
		orderLogService.insert(orderLog);

		resultMap.put("msg","操作成功");
		resultMap.put("code",0);
		resultMap.put("id",order.getId());
		return resultMap;
	}

	/**
	 * 小程序大家具下单接口
	 * @param orderbean
	 * @return
	 * @throws com.taobao.api.ApiException
	 */
	@Override
	public Map<String,Object> saveBigThingOrder(OrderBean orderbean) throws com.taobao.api.ApiException{
		Map<String,Object> resultMap = new HashMap<>();
		//获取分类的所有父类编号
		Category category =  categoryService.selectById(orderbean.getCategoryId());
		EnterpriseCode enterpriseCode = null;
		orderbean.setCategoryParentIds(category.getParentIds());
		Order order = new Order();
		try {
			order.setArrivalTime(new SimpleDateFormat("yyyy-MM-dd").parse(orderbean.getArrivalTime()));
			order.setArrivalPeriod(orderbean.getArrivalPeriod());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			order.setMemberId(orderbean.getMemberId());
			order.setCompanyId(orderbean.getCompanyId());
			order.setRecyclerId(orderbean.getRecyclerId());
			order.setOrderNo(orderbean.getOrderNo());
			order.setAreaId(orderbean.getAreaId());
			order.setStreetId(orderbean.getStreetId());
			order.setCommunityId(orderbean.getCommunityId());
			order.setAddress(orderbean.getAddress());
			order.setFullAddress(orderbean.getFullAddress());
			order.setTel(orderbean.getTel());
			order.setLinkMan(orderbean.getLinkMan());
			order.setCategoryId(orderbean.getCategoryId());
			order.setCategoryParentIds(orderbean.getCategoryParentIds());
			order.setIsMysl(orderbean.getIsMysl());
			order.setAchPrice(new BigDecimal("0"));
			BigDecimal price = new BigDecimal("0");
			if(price.compareTo(orderbean.getPrice())==1){
				order.setPrice(price);
			}else{
				order.setPrice(orderbean.getPrice());
			}
			order.setUnit("个");
			order.setQty(1);
			order.setLevel(orderbean.getLevel());

			order.setGreenCode(orderbean.getGreenCode());
			order.setAliUserId(orderbean.getAliUserId());
			order.setRemarks(orderbean.getRemarks());
			order.setTitle(Order.TitleType.BIGTHING);
			if ("1".equals(orderbean.getIsCash())) {
				order.setIsCash(orderbean.getIsCash());
			} else {
				order.setIsCash("0");
			}
			//判断是否有券码
			if(!StringUtils.isBlank(orderbean.getEnterpriseCode())){
				enterpriseCode = enterpriseCodeService.selectOne(new EntityWrapper<EnterpriseCode>().eq("code", orderbean.getEnterpriseCode()).eq("del_flag", 0).eq("is_use",0));
				//判断券码是否存在并且未使用
				if(null!=enterpriseCode){
					order.setEnterpriseCode(orderbean.getEnterpriseCode());
				}
			}
			this.insert(order);
			//更新券码信息
			if(null!=enterpriseCode){
				enterpriseCode.setIsUse("1");
				enterpriseCode.setOrderId(order.getId().intValue());
				enterpriseCodeService.updateById(enterpriseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("msg","FAIL");
			return resultMap;
		}
		long orderId = order.getId();

		//保存orderItem
		OrderItemBean orderItemBean = orderbean.getOrderItemBean();
		//取出所有的分类选项Id的和
		String categoryAttrOppIds = orderItemBean.getCategoryAttrOppIds();
		String[] oppIds = categoryAttrOppIds.split(",");
		for (int i = 0; i < oppIds.length; i++) {
			//根据分类选项主键查询相关的一条记录
			CategoryAttrOption categoryAttrOption  = categoryAttrOptionService.getOptionById(oppIds[i]);
			//根据查出来的分类属性Id查出分类属性的记录
			CategoryAttr categoryAttr = categoryAttrService.selectById(categoryAttrOption.getCategoryAttrId());
			//储存订单分类属性明细
			orderItemBean.setOrderId(Integer.parseInt(orderId + ""));
			orderItemBean.setCategoryId(Integer.parseInt(category.getId() + ""));
			orderItemBean.setCategoryName(category.getName());
			orderItemBean.setCategoryAttrId(Integer.parseInt(categoryAttr.getId() + ""));
			orderItemBean.setCategoryAttrName(categoryAttr.getName());
			orderItemBean.setCategoryAttrOppId(Integer.parseInt(categoryAttrOption.getId() + ""));
			orderItemBean.setCategoryAttrOpptionName(categoryAttrOption.getName());
			orderItemService.saveByOrderItem(orderItemBean);
		}

		//储存图片链接
		OrderPic orderPic = orderbean.getOrderPic();
		if (StringUtils.isNoneBlank(orderPic.getOrigPic())) {
			String origPics = orderPic.getOrigPic();
			String picUrl = orderPic.getPicUrl();
			String smallPic = orderPic.getSmallPic();
			String[] origPicss = origPics.split(",");
			String[] picUrls = picUrl.split(",");
			String[] smallPics = smallPic.split(",");
			for (int i = 0; i < origPicss.length; i++) {
				OrderPic orderPicc = new OrderPic();
				orderPicc.setOrigPic(origPicss[i]);
				orderPicc.setPicUrl(picUrls[i]);
				orderPicc.setSmallPic(smallPics[i]);
				orderPicc.setOrderId(Integer.parseInt(orderId + ""));
				orderPicService.insert(orderPicc);
			}
		}
		//储存订单的日志
		OrderLog orderLog = new OrderLog();
		orderLog.setOrderId(Integer.parseInt(orderId + ""));
		orderLog.setOpStatusAfter("INIT");
		orderLog.setOp("待接单");
		orderLogService.insert(orderLog);
		resultMap.put("msg","操作成功");
		resultMap.put("id",orderId);
		resultMap.put("code",0);
		return resultMap;

	}
	/**
	 * 根据订单传来的状态获取订单列表
	 */
	@Override
	public  Map<String, Object> getBigOrderList(String status,Long recycleId , PageBean pagebean){
		Map<String, Object> result = new HashMap<>();
		List<Map<String, Object>> bigOrderList = null;
		Integer count = 0;
		if("6".equals(status)){
			//转派订单列表
			bigOrderList = orderMapper.getBigOrderTransferList(recycleId.intValue(), (pagebean.getPageNumber() - 1) * pagebean.getPageSize(), pagebean.getPageSize());
			count = orderMapper.getBigOrderTransferCount(recycleId.intValue());
		}else{
			bigOrderList = orderMapper.getBigOrderList(Integer.parseInt(status),recycleId.intValue(),(pagebean.getPageNumber()-1)*pagebean.getPageSize(),pagebean.getPageSize());
			count = orderMapper.getBigOrderCount(Integer.parseInt(status),recycleId.intValue());
		}
		result.put("bigOrderList",bigOrderList);
		result.put("count",count);
		return result;
	}
	@Override
	public  String setAchOrder(OrderBean orderBean){

		Order order = orderService.selectById(orderBean.getId());
		order.setAchRemarks(orderBean.getAchRemarks());
		order.setSignUrl(orderBean.getSignUrl());
		String[] origPicss = orderBean.getPicUrl().split(",");
		for(String picUrl:origPicss){
			OrderPicAch orderPicAch = new OrderPicAch();
			orderPicAch.setOrderId(orderBean.getId());
			orderPicAch.setOrigPic(picUrl);
			orderPicAch.setPicUrl(picUrl);
			orderPicAch.setSmallPic(picUrl);
			orderPicAchService.insert(orderPicAch);
		}
		orderService.updateById(order);
		return "操作成功";
	}
	@Override
	public String saveBigOrderPrice(OrderBean orderBean){
		Order order = orderService.selectById(orderBean.getId());
		Category categorys = categoryService.selectById(order.getCategoryId());
		order.setAchPrice(new BigDecimal(orderBean.getAchPrice()));
		order.setGreenCount(categorys.getGreenCount().doubleValue());
		if(Double.parseDouble(orderBean.getAchPrice())==0){
			orderBean.setStatus("3");
			orderBean.setAmount(categorys.getGreenCount());
			//修改订单状态
			this.modifyOrderSta(orderBean);
			order.setStatus(OrderType.COMPLETE);
		}
		orderService.updateById(order);

		return "操作成功";
	}

	@Override
	public void deleteBigOrderRemarks(Integer orderId){
		orderMapper.deleteBigOrderRemarks(orderId);
	}

	@Override
	public List<Map<String, Object>> sevenDayorderNum(String streetId) {
		return orderMapper.sevenDayorderNum(streetId);
	}
	@Override
	public List<Map<String, Object>> oneDayorderNum(String streetId) {
		return orderMapper.oneDayorderNum(streetId);
	}

	@Override
	public String isUserOrder(String memberId){
//		List<Order> initOrder = orderService.selectList(new EntityWrapper<Order>().eq("member_id", memberId).in("status_", "0,1").eq("is_read", 0));
//		List<Order> alreadyOrder = orderService.selectList(new EntityWrapper<Order>().eq("member_id", memberId).eq("status_", 2).eq("is_read", 0));
//		List<Order> completeOrder = orderService.selectList(new EntityWrapper<Order>().eq("member_id", memberId).eq("status_", 3).eq("is_read", 0));
//		List<Order> cancleOrder = orderService.selectList(new EntityWrapper<Order>().eq("member_id", memberId).in("status_", "4,5").eq("is_read", 0));
//		Map<String,Object> orderMap = new HashMap<>();
//		orderMap.put("initOrder",initOrder.size());
//		orderMap.put("alreadyOrder",alreadyOrder.size());
//		orderMap.put("completeOrder",completeOrder.size());
//		orderMap.put("cancleOrder",cancleOrder.size());
//		return JSON.toJSONString(orderMap);
		return null;
	}

	@Override
	public OrderBean myslOrderData(String orderId) {
		Order order = orderService.selectById(orderId);
		if(!StringUtils.isBlank(order.getMyslOrderId())){
			return null;
		}
		Map<String, Object> digitalMap = null;
		List<Map<String, Object>> houseList  = null;
		//判断订单是否是电器
		if((Order.TitleType.DIGITAL.getValue()+"").equals(order.getTitle().getValue()+"")){
			digitalMap = orderItemService.selectItemOne(Integer.parseInt(orderId));
			if (null == digitalMap || digitalMap.isEmpty() ){
				return null;
			}
		}else if((Order.TitleType.HOUSEHOLD.getValue()+"").equals(order.getTitle().getValue()+"")||(Order.TitleType.FIVEKG.getValue()+"").equals(order.getTitle().getValue()+"")){
			houseList = orderItemAchService.selectItemSumAmount(Integer.parseInt(orderId));
		}else {
			return null;
		}
		long amount = 0;
		AntMerchantExpandTradeorderSyncModel model = new AntMerchantExpandTradeorderSyncModel();
		model.setBuyerId(order.getAliUserId());
		model.setSellerId(AlipayConst.SellerId);
		model.setOutBizType("RECYCLING");
		model.setOutBizNo(order.getOrderNo());
		List<ItemOrder> orderItemList = new ArrayList<ItemOrder>();
		//如果是电器
		if((Category.CategoryType.DIGITAL.getValue()+"").equals(order.getTitle().getValue()+"")){
			ItemOrder itemOrder = new ItemOrder();
			itemOrder.setItemName(digitalMap.get("name").toString());
			itemOrder.setQuantity((long)1);
			List<OrderExtInfo> extInfo = new ArrayList<>();
			OrderExtInfo orderExtInfo = new OrderExtInfo();
			orderExtInfo.setExtKey("ITEM_TYPE");
			orderExtInfo.setExtValue(digitalMap.get("aliItemType").toString());
			extInfo.add(orderExtInfo);
			itemOrder.setExtInfo(extInfo);
			orderItemList.add(itemOrder);
		}else if((Order.TitleType.HOUSEHOLD.getValue()+"").equals(order.getTitle().getValue()+"")||(Order.TitleType.FIVEKG.getValue()+"").equals(order.getTitle().getValue()+"")){
			for (Map<String, Object> itemMap:houseList) {
				if (null==itemMap.get("aliItemType")){
					continue;
				}
				amount += (long)Math.floor((double)itemMap.get("amount"));
				ItemOrder itemOrder = new ItemOrder();
				itemOrder.setItemName(itemMap.get("parentName").toString());
				itemOrder.setQuantity((long)Math.floor((double)itemMap.get("amount")));
				List<OrderExtInfo> extInfo = new ArrayList<>();
				OrderExtInfo orderExtInfo = new OrderExtInfo();
				orderExtInfo.setExtKey("ITEM_TYPE");
				orderExtInfo.setExtValue(itemMap.get("aliItemType").toString());
				extInfo.add(orderExtInfo);
				itemOrder.setExtInfo(extInfo);
				orderItemList.add(itemOrder);
			}
		}
		if (null == orderItemList || orderItemList.isEmpty() ){
			return null;
		}
		model.setItemOrderList(orderItemList);
		order.setMyslParam(JSON.toJSONString(model));
		OrderBean orderBean = new OrderBean();
		if (amount<=30000){
			orderBean.setIsRisk("0");
			order.setIsRisk("0");
		}else {
			order.setIsRisk("1");
			orderBean.setIsRisk("1");
		}
		orderService.updateById(order);
		//给用户增加能量
		ansycMyslService.updateForest(order.getId().toString(),JSON.toJSONString(model));
		orderBean.setMyslParam(JSON.toJSONString(model));
		return orderBean;
	}

	/**	根据手机号查询订单列表
	  * @author sgmark@aliyun.com
	  * @date 2019/6/3 0003
	  * @param
	  * @return
	  */
	@Override
	public Map<String, Object> getOrderListByPhone(OrderBean orderbean) {
		int count = 0;
		List<AppOrderResult> listOrder = null;
		if (null != orderbean.getTel()){
			orderbean.setTel("%"+orderbean.getTel()+"%");
		}
		count = orderMapper.getOrderCountByPhone(orderbean);//得到总数
		listOrder = orderMapper.getOrderListByPhone(orderbean, (orderbean.getPagebean().getPageNumber() - 1) * orderbean.getPagebean().getPageSize(), orderbean.getPagebean().getPageSize());
		Map<String, Object> map = new HashMap<String, Object>();
		int pageNum = count % orderbean.getPagebean().getPageSize() == 0 ? count / orderbean.getPagebean().getPageSize() : count / orderbean.getPagebean().getPageSize() + 1;
		int currentpage = orderbean.getPagebean().getPageNumber();
		map.put("pageNum", pageNum);
		map.put("listOrder", this.createName4App(listOrder));
		map.put("count", count);
		map.put("currentPage", currentpage > pageNum ? pageNum : currentpage);
		return map;
	}
	/** 根据手机号查询大件订单列表
	  * @author sgmark@aliyun.com
	  * @date 2019/6/4 0004
	  * @param 
	  * @return 
	  */
	@Override
	public Map<String, Object> getBigOrderListByPhone(OrderBean orderbean) {
		Map<String, Object> result = new HashMap<>();
		if (null != orderbean.getTel() && orderbean.getTel().length() < 11){
			orderbean.setTel("%"+orderbean.getTel()+"%");
		}
		List<Map<String, Object>> bigOrderList = null;
		Integer count = 0;
		bigOrderList = orderMapper.getBigOrderListByPhone(orderbean.getRecyclerId(), orderbean.getTel(), (orderbean.getPagebean().getPageNumber() - 1) * orderbean.getPagebean().getPageSize(), orderbean.getPagebean().getPageSize());
		count = orderMapper.getBigOrderCountByPhone(orderbean.getRecyclerId(), orderbean.getTel());
		result.put("bigOrderList", bigOrderList);
		result.put("count", count);
		return result;
	}

	@Transactional
	@Override
	public Object orderUpdateStatus(String companyId, String orderId) {
        OrderLog orderLog = new OrderLog();
        Order order = this.selectById(orderId);
        orderLog.setOpStatusBefore(order.getStatus().name());
        order.setStatus(OrderType.INIT);
        order.setRecyclerId(0);
        this.updateById(order);
        //新增订单日志表的记录
        orderLog.setOpStatusAfter(order.getStatus().name());
        orderLog.setOp("已初始");
        orderLog.setOrderId(order.getId().intValue());
        orderLogService.insert(orderLog);
        return "操作成功";
	}
	/**
	 * 根据订单id自动派单给具体的回收人员
	 * @param orderId
	 * @return
	 */
	public String orderSendRecycleByOrderId(Integer orderId){

		List<Recyclers> sendOrderRecyclersList = recyclersService.getSendOrderRecyclersList(orderId);
		if(!sendOrderRecyclersList.isEmpty()){
			Order order = this.selectById(orderId);
			order.setRecyclerId(sendOrderRecyclersList.get(0).getId().intValue());
			order.setStatus(OrderType.TOSEND);
			order.setDistributeTime(new Date());
			this.updateById(order);
		}
		return "操作成功";
	}

	/**
	 * 派发五公斤订单
	 * @param orderId
	 * @return
	 */
	public Object tosendfiveKgOrder(Integer orderId){
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		Order order = orderService.selectById(orderId);
		Company company = companyService.selectById(order.getCompanyId());
		if(null==company||null==company.getAliMns()){
			return "该企业无法回收五公斤";
		}
		order.setDistributeTime(new Date());
		order.setStatus(Order.OrderType.TOSEND);
		orderService.updateById(order);
		try{
			Area county = areaService.selectById(order.getAreaId());
			Area city = areaService.selectById(county.getParentId());
			Area province = areaService.selectById(city.getParentId());
			HashMap<String,Object> param=new HashMap<>();
			param.put("provinceNname",province.getAreaName());
			param.put("cityName",city.getAreaName());
			param.put("countyName",county.getAreaName());
			param.put("orderNo",order.getOrderNo());
			param.put("orderType","废纺衣物");
			param.put("channelMemberId","RC20190427231730100044422");
			param.put("orderAmount",order.getQty());
			param.put("userName",order.getLinkMan());
			param.put("userTel", order.getTel());
			param.put("userAddress",order.getAddress()+order.getFullAddress());
			param.put("arrivalTime", sim.format(order.getArrivalTime())+" "+("am".equals(order.getArrivalPeriod())?"10:00:00":("pm".equals(order.getArrivalPeriod())?"16:00:00":order.getArrivalPeriod().substring(0,2)+":00:00")));
			param.put("isCancel","N");
			sendRocketmqMessageService.sendDeliveryOrder(JSON.toJSONString(param),company.getAliMns());
		}catch (Exception e){
			e.printStackTrace();
		}
		return "操作成功";
	}

	@Override
	public Boolean selectOrderByImprisonRule(String aliUserId,String title,Integer orderNum,Integer dateNum){
		Boolean isImprisonRule = false;
		Date endDate = new Date();
		Date startDate = ToolUtils.RmDateByNow(endDate,dateNum-1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Order> orders = this.selectList(new EntityWrapper<Order>().eq("ali_user_id", aliUserId).eq("title", title).between("create_date", sdf.format(startDate)+" 00:00:01", sdf.format(endDate)+" 23:59:59"));
		if(orders.size() >= orderNum){
			isImprisonRule = true;
		}
		return isImprisonRule;
	}


	public Object recallOrder(Integer orderId,Long recyclerId){
		String status = "5";
		String recrcleTel = "";
		Order order = this.selectById(orderId);
		if((OrderType.TOSEND+"").equals(order.getStatus()+"")||(Order.OrderType.ALREADY+"").equals(order.getStatus()+"")) {
			Recyclers recyclers = recyclersService.selectById(recyclerId);
			Recyclers recycler = recyclersService.selectById(order.getRecyclerId());
			recrcleTel = recycler.getTel();
			if (null == order) {
				return "未找到该订单 id：" + orderId;
			}
			order.setRecyclerId(recyclerId.intValue());
			order.setStatus(OrderType.TOSEND);
			boolean b = this.updateById(order);
			if (!b) {
				return "转派失败";
			}
			PushUtils.getAcsResponse(recrcleTel, status, order.getTitle().getValue() + "");
			PushUtils.getAcsResponse(recyclers.getTel(), order.getStatus().getValue() + "", order.getTitle().getValue() + "");
			return "操作成功";
		}else {
			return "订单已完成或已取消，不可操作";
		}
	}



}