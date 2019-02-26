package com.tzj.collect.service.impl;

import com.alipay.api.response.AlipayTradeQueryResponse;
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
import com.tzj.collect.common.constant.PushConst;
import com.tzj.collect.common.util.PushUtils;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 订单ServiceImpl
 * @Author 王灿
 **/
@Service
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
	public String saveOrder(OrderBean orderbean) {
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
			order.setCommunityId(orderbean.getCommunityId());
			order.setAddress(orderbean.getAddress());
			order.setFullAddress(orderbean.getFullAddress());
			order.setTel(orderbean.getTel());
			order.setLinkMan(orderbean.getLinkMan());
			order.setCategoryId(orderbean.getCategoryId());
			order.setCategoryParentIds(orderbean.getCategoryParentIds());
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
			if (CategoryType.DIGITAL.name().equals(orderbean.getTitle())) {
				order.setTitle(CategoryType.DIGITAL);
			} else if (CategoryType.HOUSEHOLD.name().equals(orderbean.getTitle())) {
				order.setTitle(CategoryType.HOUSEHOLD);
			} else {
				order.setTitle(CategoryType.DEFUALT);
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
			return "FAIL";
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

//    	//根据小区的Id/用户的名字/用户的手机号  查询用户是否存在此地址
//    	MemberAddress memberAddress= memberAddressService.selectOne(new EntityWrapper<MemberAddress>().eq("del_flag", 0).eq("member_id", orderbean.getMemberId()).eq("community_id", orderbean.getCommunityId()).eq("name_", orderbean.getLinkMan()).eq("tel", orderbean.getTel()));
//    	//判断地址是否存在
//    	if(null!=memberAddress) {
//    		//查出用户的默认地址
//        	MemberAddress memberAddressk= memberAddressService.selectOne(new EntityWrapper<MemberAddress>().eq("is_selected",1).eq("del_flag", 0).eq("member_id", orderbean.getMemberId()).eq("city_id", orderbean.getCityId()));
//        	if(memberAddressk!=null) {
//        		memberAddressk.setIsSelected(0);
//        		//memberAddressMapper.updateById(memberAddressk);
//        		memberAddressService.update(memberAddressk, new EntityWrapper<MemberAddress>().eq("is_selected",1).eq("del_flag", 0).eq("member_id", orderbean.getMemberId()).eq("city_id", orderbean.getCityId()));
//        	}
//        	memberAddress.setIsSelected(1);
//        	flag = memberAddressService.updateById(memberAddress);
//    	}else {
//    		MemberAddress newNemberAddress = new MemberAddress();
//    		newNemberAddress.setMemberId(orderbean.getMemberId().toString());
//    		newNemberAddress.setName(orderbean.getLinkMan());
//    		newNemberAddress.setAreaId(orderbean.getAreaId());
//    		newNemberAddress.setStreetId(orderbean.getStreetId());
//    		newNemberAddress.setCommunityId(orderbean.getCommunityId());
//    		newNemberAddress.setAddress(orderbean.getAddress());
//    		newNemberAddress.setHouseNumber(orderbean.getFullAddress());
//    		newNemberAddress.setTel(orderbean.getTel());
//    		newNemberAddress.setIsSelected(1);
//    		flag = memberAddressService.insert(newNemberAddress);
//    	}
		if (flag) {
			return "操作成功";
		}
		return "操作失败";
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
		if("1".equals(order.getIsCash())) {
			//修改订单状态
			this.modifyOrderSta(orderbean);
			//判断是否有券码完成转账
			if (!StringUtils.isBlank(order.getEnterpriseCode())) {
				EnterpriseCode enterpriseCode = enterpriseCodeService.selectOne(new EntityWrapper<EnterpriseCode>().eq("code", order.getEnterpriseCode()).eq("del_flag", 0).eq("is_use", 1));
				//判断券码是否存在并且未使用
				if (null != enterpriseCode) {
					//储存转账信息
					Payment payment = new Payment();
					payment.setAliUserId(order.getAliUserId());
					payment.setTradeNo("1");
					payment.setPrice(enterpriseCode.getPrice());
					payment.setRecyclersId(order.getRecyclerId().longValue());
					payment.setOrderSn(order.getOrderNo());
					paymentService.insert(payment);
					//给用户转账
					paymentService.transfer(payment);
					enterpriseCode.setReceiveDate(new Date());
					enterpriseCode.setIsUse("2");
					enterpriseCodeService.updateById(enterpriseCode);
				}
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
				wrapper = new EntityWrapper<CompanyCategory>();
				wrapper.eq("company_id", orderbean.getCompanyId());
				wrapper.eq("parent_id", idAmountListBean.getCategoryParentId());
				comPriceList = comCatePriceService.selectList(wrapper);
				for (OrderItemBean item : idAmount) {
					orderItem.setCategoryId(Integer.parseInt(item.getCategoryId() + ""));
					orderItem.setCategoryName(item.getCategoryName());
					orderItem.setAmount(item.getAmount());
					System.out.println(item.getCategoryName() + " 重量: " + item.getAmount());
					//判断此单是否是免费
					if ("1".equals(isCash)) {
						amount += item.getAmount()*2;
					} else if(ToolUtils.categoryName.equals(item.getCategoryName())){
						amount += item.getAmount();
					}
					for (CompanyCategory comPrice : comPriceList) {
						if (comPrice.getCategoryId().equals(item.getCategoryId() + "")) {
							orderItem.setParentIds(comPrice.getParentIds());
							orderItem.setPrice(comPrice.getPrice());
							orderItem.setUnit(comPrice.getUnit());
							orderItemAchService.insert(orderItem);
						}
					}
				}
			}
		} else if (CategoryType.DIGITAL.name().equals(orderbean.getTitle())) {
			//根据订单Id获取电器订单所属分类的绿色能量值
			OrderItem item = orderItemService.selectOne(new EntityWrapper<OrderItem>().eq("order_id", orderbean.getId()).groupBy("order_id"));
			if (item != null) {
				Category categorys = categoryService.selectById(item.getCategoryId());
				amount = categorys.getGreenCount();
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
			if (order.getTitle() == CategoryType.HOUSEHOLD) {
				attName = new HashSet<>();
				//获得父类名称
				attrList = orderItemService.selectCateName(Integer.parseInt(order.getId().toString()));
				if (attrList != null && attrList.size() > 0) {
					try {
						for (ComCatePrice comCatePrice : attrList) {
							attName.add(comCatePrice.getName());
						}
						order.setTitle(CategoryType.HOUSEHOLD);
						order.setCateAttName4Page(attName.toString().replace("]", "").replace("[", "").replace(",", "/").replace(" ", ""));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (order.getTitle() == CategoryType.DIGITAL) {
				
				//根据分类Id查询父级分类名字
				Category category = categoryService.selectById(order.getCategory().getCategoryId());
				order.setCateAttName4Page(category.getName());
				order.setTitle(CategoryType.DIGITAL);
			}

		}
		return orderList;
	}

	public Order createName4Ali(Order order) {
		Set<String> attName = null;
		List<ComCatePrice> attrList = null;
		if (order.getTitle() == CategoryType.HOUSEHOLD) {
			attName = new HashSet<>();
			//获得父类名称
			attrList = orderItemService.selectCateName(Integer.parseInt(order.getId().toString()));
			if (attrList != null && attrList.size() > 0) {
				try {
					for (ComCatePrice comCatePrice : attrList) {
						attName.add(comCatePrice.getName());
					}
					order.setTitle(CategoryType.HOUSEHOLD);
					order.setCateAttName4Page(attName.toString().replace("]", "").replace("[", "").replace(",", "/").replace(" ", ""));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (order.getTitle() == CategoryType.DIGITAL) {
			order.setCateAttName4Page(order.getCategory().getName());
			order.setTitle(CategoryType.DIGITAL);
		}
		return order;
	}

	public Order createName4PC(Order order) {
		Category category = order.getCategory();
		;
		if (order.getTitle() == CategoryType.HOUSEHOLD) {
			category.setName("生活垃圾");
			order.setCategory(category);
		} else if (order.getTitle() == CategoryType.DIGITAL) {
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
	 * @changeby zhangqiang
	 */
	@Override
	public Map<String, Object> getOrderLists(BOrderBean orderBean, PageBean pageBean) {
		List<String> statusList = new ArrayList<>();
		String status = getStatus(orderBean.getStatus());
		String title = null;
		if (orderBean.getCategoryType() == null) {
			throw new ApiException("参数错误");
		}else {
			title = orderBean.getCategoryType().getValue().toString();
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
			if (OrderType.COMPLETE.getValue().equals(order.getStatus().getValue())) {
				order.setPrice(order.getAchPrice());
			}
		}
		map.put("count", count);
		map.put("list", list);
		return map;
	}

	/**
	 * 导出企业的完成订单的Excel
	 * @param companyId
	 * @return
	 */
	@Override
	public List<Map<String,Object>> outOrderExcel(Integer companyId,String startTime,String endTime){
		return orderMapper.outOrderExcel(companyId,startTime,endTime);
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
	public Map<String, Object> selectCountByStatus(String status, Integer companyId, CategoryType categoryType) {
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
		boolean bool = this.updateById(order);
		if (bool) {
			CommToken commToken = orderMapper.getCommToken(order.getOrderNo());
			if (commToken != null) {
				xingeService.sendPostMessage("您有一笔订单已被取消", "已取消订单来自" + commToken.getCommName() + "，点击查看", commToken.getTencentToken(), XingeMessageCode.cancelOrder);
			}
		}
		//阿里云推送
		if(order.getRecyclerId()!=null&&order.getRecyclerId()!=0){
			Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
			PushUtils.getAcsResponse(recyclers.getTel(),PushConst.APP_TITLE,PushConst.APP_CODE_03_MESSAGE,PushConst.APP_CODE_03,PushConst.APP_CODE_03_MESSAGE);
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
		Order order = orderMapper.selectOrder(orderbean.getId());
		//查询订单表的关联图片表
		List<OrderPic> orderPicList = orderPicService.selectbyOrderId(orderbean.getId());
		//查询订单表的分了明细表
		List<OrderItem> OrderItemList = orderItemService.selectByOrderId(orderbean.getId());
		//List<ComCatePrice> cateName = orderItemService.selectCateName(orderbean.getId());
		List<Map<String, Object>> listMapObject = new ArrayList<>();
		//Map<String, Object> mapListMap = null;
		Object obj = null;
		if (order.getTitle() == CategoryType.HOUSEHOLD) {
			orderbean.setStatus(order.getStatus().getValue().toString());
			orderbean.setTitle(order.getTitle().toString());
			orderbean.setIsCash(order.getIsCash());

			Map<String, Object> map = this.createPriceAmount(orderbean);
			listMapObject = (List<Map<String, Object>>) map.get("listMapObject");
			obj = map.get("greenCount");

		} else if (order.getTitle() == CategoryType.DIGITAL) {
			OrderItemList = orderItemService.selectByOrderId(orderbean.getId());
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Company company = companyService.selectById(order.getCompanyId());
		order.setCompany(company);
		resultMap.put("order", this.createName4Ali(order));
		resultMap.put("orderPicList", orderPicList);
		resultMap.put("OrderItemList", OrderItemList);
		resultMap.put("list", listMapObject);
		resultMap.put("greenCount", obj);
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
		if (order.getTitle() == CategoryType.HOUSEHOLD) {
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
			if (CategoryType.DIGITAL.getValue().equals(order.getTitle().getValue())) {
				List<OrderItem> OrderItemList = orderItemService.selectByOrderId(orderId);
				resultMap.put("OrderItemList", OrderItemList);
			} else {
				//查询订单表的分了明细表
				List<OrderItemAch> orderItemAchList = orderItemAchService.selectByOrderId(orderId);
				resultMap.put("OrderItemList", orderItemAchList);
			}
			order.setPrice(order.getAchPrice());
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

				order.setRecyclerId(recyclerId);
				order.setDistributeTime(new Date());
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
				PushUtils.getAcsResponse(recyclers.getTel(), PushConst.APP_TITLE,PushConst.APP_CODE_01_MESSAGE,PushConst.APP_CODE_01,PushConst.APP_CODE_01_MESSAGE);
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
		EntityWrapper<Order> wrapper = new EntityWrapper<Order>();
		wrapper.eq("recycler_id", orderBean.getRecyclerId());
		if (orderBean.getId() != null && !"".equals(orderBean.getId())) {
			wrapper.eq("id", orderBean.getId());
		} else if (orderBean.getOrderNo() != null && !"".equals(orderBean.getOrderNo())) {
			wrapper.eq("order_no", orderBean.getOrderNo());
		} else {
			return false;
		}
		Order order = this.selectById(orderBean.getId());
		if (orderBean.getArrivalTime() != null && !"".equals(orderBean.getArrivalTime())) {
			try {
				order.setArrivalTime(new SimpleDateFormat("yyyy-MM-dd").parse(orderBean.getArrivalTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		boolean flag = this.update(order, wrapper);
		if (!flag) {
			throw new ApiException("修改失敗");
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
		if((order.getTitle().getValue()+"").equals("1")){
			Category category = categoryService.selectById(order.getCategoryId());
			descrb = category.getName();
		}else{
			descrb = "生活垃圾";
		}

		if (order.getStatus() != null && !"".equals(order.getStatus())) {
			orderLog.setOpStatusBefore(order.getStatus().name());
		}
		order.setUpdateDate(new Date());
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
							PushUtils.getAcsResponse(recyclers.getTel(),PushConst.APP_TITLE,PushConst.APP_CODE_04_MESSAGE,PushConst.APP_CODE_04,PushConst.APP_CODE_04_MESSAGE);
						}
					} else {
						throw new ApiException("该订单已被操作，请刷新页面查看状态");
					}
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
					}
					break;
				default:
					throw new ApiException("不能修改为此状态");
			}
		}
		//修改日志列表
		flag = orderLogService.insert(orderLog);
		return flag;
	}

	/**
	 * @param memberId:用户Id
	 * @param OrderNo:订单编号
	 * @author 王灿
	 */
	public void updateMemberPoint(Integer memberId, String OrderNo, double amount,String descrb) {
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
			System.out.println("给用户增加的积分是 ：" + amount + "----point: " + points.getPoint() + "");
			aliPayService.updatePoint(member.getAliCardNo(), member.getOpenCardDate(), points.getRemainPoint() + "", null, member.getAppId());
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
		if ("HOUSEHOLD".equals(orderbean.getTitle()) && "3".equals(orderbean.getStatus())) {
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
						//判断此单是否是免费
						if ("1".equals(isCash)) {
							greenCount += list.getAmount();
						} else {
							greenCount += list.getAmount();
						}
						if (list.getPrice() == 0 && !ToolUtils.categoryName.equals(list.getCategoryName())) {
							categoryName += list.getCategoryName() + "/";
						} else {
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
		resultMap.put("greenCount", greenCount);
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
		if ("HOUSEHOLD".equals(orderbean.getTitle()) && "3".equals(orderbean.getStatus())) {
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
						map.put("price", list.getPrice() + "");
						map.put("unit", list.getUnit());
						map.put("amount", list.getAmount() + "");
						price += list.getPrice() * list.getAmount();
						//判断是否免费
						if ("1".equals(isCash)) {
							greenCount += list.getAmount() * 2;
						} else if (ToolUtils.categoryName.equals(list.getCategoryName())) {
							greenCount += list.getAmount();
						}
						if (list.getPrice() == 0 && !ToolUtils.categoryName.equals(list.getCategoryName())) {
							categoryName += list.getCategoryName() + "/";
						} else {
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
						map.put("price", list.getPrice() + "");
						map.put("unit", list.getUnit());
						map.put("amount", list.getAmount() + "");
						price += list.getPrice() * list.getAmount();
						listMap.add(map);
					}
				}
			}

			mapListMap.put("categoryName", StringUtils.isBlank(categoryName) ? "" : categoryName.substring(0, categoryName.length() - 1));
			mapListMap.put("list", listMap);
			mapListMap.put("name", name == null ? null : name.getName());
			mapListMap.put("price", price);
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
		order.setUnit("计量单位");
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
		order.setTitle(CategoryType.HOUSEHOLD);
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
		//给用户增加积分
		this.updateMemberPoint(order.getMemberId(), order.getOrderNo(), amount,"定点回收物");

		if (orderBean.getPrice().compareTo(BigDecimal.ZERO)==0){
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
		PushUtils.getAcsResponse(recyclers.getTel(),PushConst.APP_TITLE,PushConst.APP_CODE_02_MESSAGE,PushConst.APP_CODE_02,PushConst.APP_CODE_02_MESSAGE);
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
				} else {
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
			order.setCommunityId(orderBean.getCommunityId());
			order.setAddress(orderBean.getAddress());
			order.setFullAddress(orderBean.getFullAddress());
			order.setTel(orderBean.getTel());
			order.setLinkMan(orderBean.getLinkMan());
			order.setUnit("计量单位");
			order.setQty(9999);
			order.setPrice(BigDecimal.ZERO);
			order.setLevel(orderBean.getLevel());
			order.setGreenCode(orderBean.getGreenCode());
			order.setAliUserId(member.getAliUserId());
			order.setTitle(CategoryType.HOUSEHOLD);
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

		return "操作成功";
	}
	/**
	 * 保存5公斤废纺衣物的订单
	 * @return
	 */
	@Override
	public Object savefiveKgOrder(OrderBean orderBean) {
		boolean flag = false;
		//根据分类Id查询物流公司分类的信息
		CompanyCategory companyCategory = companyCategoryService.selectOne(new EntityWrapper<CompanyCategory>().eq("category_id", orderBean.getCategoryId()).eq("del_flag", 0).eq("company_id", orderBean.getCompanyId()));
		Order order = new Order();
		try{
			order.setArrivalTime(new SimpleDateFormat("yyyy-MM-dd").parse(orderBean.getArrivalTime()));
			order.setArrivalPeriod(orderBean.getArrivalPeriod());
		}catch(Exception e){
			e.printStackTrace();
		}

		try {
			order.setMemberId(orderBean.getMemberId());
			order.setOrderNo(orderBean.getOrderNo());
			order.setAreaId(orderBean.getAreaId());
			order.setCommunityId(orderBean.getCommunityId());
			order.setCategoryId(orderBean.getCategoryId());
			order.setCategoryParentIds(orderBean.getCategoryParentIds());
			order.setUnit(companyCategory.getUnit());
			order.setPrice(new BigDecimal(companyCategory.getPrice()));
			order.setAliUserId(orderBean.getAliUserId());
			order.setTitle(CategoryType.FIVEKG);
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
			return "FAIL";
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



		return "操作成功";
	}

}