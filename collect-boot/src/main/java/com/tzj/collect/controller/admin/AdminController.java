package com.tzj.collect.controller.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.ali.param.OrderBean;
import com.tzj.collect.api.ali.param.OrderItemBean;
import com.tzj.collect.api.common.excel.ExcelData;
import com.tzj.collect.api.common.excel.ExcelUtils;
import com.tzj.collect.api.common.websocket.AppWebSocketServer;
import com.tzj.collect.api.common.websocket.WebSocketServer;
import com.tzj.collect.api.common.websocket.XcxWebSocketServer;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Community;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.EnterpriseCode;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.MemberAddress;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.OrderPic;
import com.tzj.collect.service.AliPayService;
import com.tzj.collect.service.AnsycMyslService;
import com.tzj.collect.service.AreaService;
import com.tzj.collect.service.AsyncService;
import com.tzj.collect.service.CategoryService;
import com.tzj.collect.service.CommunityService;
import com.tzj.collect.service.CompanyCategoryService;
import com.tzj.collect.service.CompanyService;
import com.tzj.collect.service.CompanyStreeService;
import com.tzj.collect.service.CompanyStreetApplianceService;
import com.tzj.collect.service.CompanyStreetBigService;
import com.tzj.collect.service.EnterpriseCodeService;
import com.tzj.collect.service.MemberAddressService;
import com.tzj.collect.service.MemberService;
import com.tzj.collect.service.OrderService;
import com.tzj.collect.service.RocketmqMessageService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("admin")
public class AdminController {

	@Autowired
	private WebSocketServer webSocketServer;
	@Autowired
	private EnterpriseCodeService enterpriseCodeService;
	@Autowired
	private AliPayService aliPayService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private AnsycMyslService ansycMyslService;

	@Autowired
	private AppWebSocketServer appWebSocketServer;
	@Autowired
	private XcxWebSocketServer xcxWebSocketServer;
	@Autowired
	private RocketmqMessageService rocketmqMessageService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private AsyncService asyncService;
	@Autowired
	private CompanyCategoryService companyCategoryService;
	@Autowired
	private MemberAddressService memberAddressService;
	@Autowired
	private CommunityService communityService;
	@Autowired
	private  CompanyStreeService companyStreeService;
	@Autowired
	private CompanyStreetBigService companyStreetBigService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CompanyStreetApplianceService companyStreetApplianceService;

	@RequestMapping("/addExcel")
	public String addExcel() {
		return "admin/addExcel";
	}

	@RequestMapping("/love")
	public String love() {
		return "admin/love";
	}

	@RequestMapping("/excel")
	public String excel() {
		return "admin/excel";
	}

	@RequestMapping("/flcx/excel")
	public String flcxExcel() {
		return "admin/flcxExcel";
	}

	@RequestMapping("/area/excel")
	public String areaExcel() {
		return "admin/areaExcel";
	}




	@RequestMapping("/update/rocket")
	public @ResponseBody Object rocket() throws Exception {
		List<Order> orderList = orderService.selectList(new EntityWrapper<Order>().le("complete_date", "2019-05-14 10:36:40").ge("complete_date", "2019-05-14 09:30:01").eq("status_","3").isNotNull("mysl_param"));

		for (Order order:
			 orderList) {
			orderService.myslOrderData(order.getId().toString());
		}
//		for (Order order:orderList) {
//			SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
//			Company company = companyService.selectById(order.getCompanyId());
//			if(null==company||null==company.getAliMns()){
//				continue;
//			}
//			order.setDistributeTime(new Date());
//			order.setStatus(Order.OrderType.TOSEND);
//			orderService.updateById(order);
//			try{
//				Area county = areaService.selectById(order.getAreaId());
//				Area city = areaService.selectById(county.getParentId());
//				Area province = areaService.selectById(city.getParentId());
//				HashMap<String,Object> param=new HashMap<>();
//				param.put("provinceNname",province.getAreaName());
//				param.put("cityName",city.getAreaName());
//				param.put("countyName",county.getAreaName());
//				param.put("orderNo",order.getOrderNo());
//				param.put("orderType","废纺衣物");
//				param.put("channelMemberId","RC20190427231730100044422");
//				param.put("orderAmount",order.getQty());
//				param.put("userName",order.getLinkMan());
//				param.put("userTel", order.getTel());
//				param.put("userAddress",order.getAddress()+order.getFullAddress());
//				param.put("arrivalTime", sim.format(order.getArrivalTime())+" "+("am".equals(order.getArrivalPeriod())?"10:00:00":"16:00:00"));
//				param.put("isCancel","N");
//				RocketMqConst.sendDeliveryOrder(JSON.toJSONString(param),company.getAliMns());
//			}catch (Exception e){
//				e.printStackTrace();
//			}
//		}


		return orderList ;
	}


	@RequestMapping("/excels")
	public void excels(HttpServletResponse response) throws Exception {
		ExcelData data = new ExcelData();
		data.setName("以旧换新信息数据");

		//添加表头
		List<String> titles = new ArrayList<>();
		//for(String title: excelInfo.getNames())
		titles.add("时间");
		titles.add("姓名");
		titles.add("手机");
		titles.add("商品名称");
		titles.add("商品码");
		data.setTitles(titles);

		List<EnterpriseCode> list = enterpriseCodeService.selectList(new EntityWrapper<EnterpriseCode>().eq("enterprise_id", 1));

		//添加列
		List<List<Object>> rows = new ArrayList();
		List<Object> row = null;
		for (int i = 0; i < list.size(); i++) {
			row = new ArrayList();
			row.add(list.get(i).getCreateDate());
			row.add(list.get(i).getCustomerName());
			row.add(list.get(i).getCustomerTel());
			row.add(list.get(i).getProductName());
			row.add(list.get(i).getInvoiceCode());
			rows.add(row);

		}
		data.setRows(rows);

		SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String fileName = fdate.format(new Date()) + ".xlsx";
		ExcelUtils.exportExcel(response, fileName, data);
	}

	@RequestMapping("/open/socket")
	public  String openSocket() {
		System.out.println("socket");

		return "admin/love";
	}

	@RequestMapping("/product/order")
	public @ResponseBody
	String updateProducrOrder(String id) {
		System.out.println("进来了2");
		try {
			xcxWebSocketServer.sendInfo(id,"", "你是回收经理");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "admin/love";
	}

	@RequestMapping("/upload/file")
	public String uploadFile(String name) {
		System.out.println("name  " + name);
		return "admin/admin";
	}

	@RequestMapping("/test/mysl")
	public @ResponseBody String mysl(String orderId) {

		OrderBean orderBean = orderService.myslOrderData(orderId);
		return "操作成功";
	}


	@RequestMapping("/getDD")
	public @ResponseBody  String getDD()throws Exception{
		System.out.println("等待前");
		OrderBean orderBean = new OrderBean();
		orderBean.setOrderNo("测试订单");
		orderBean.setCompanyName("测试企业名字");
		orderBean.setDingDingUrl("https://oapi.dingtalk.com/robot/send?access_token=c41ce5b249d8627a88f0b9b00615fd55a5ce48d6a447a891eef25fe03e514cd9");
		//钉钉通知
		asyncService.notifyDingDingOrderCreate(orderBean);
		Thread.sleep(3000);
		return "操作成功";
	}

	@RequestMapping("/ceMemberAddress")
	public @ResponseBody Object ceMemberAddress() throws Exception {

		MemberAddress memberAddress = memberAddressService.selectOne(new EntityWrapper<MemberAddress>().eq("is_selected",1).eq("del_flag", 0).eq("member_id", 8401).eq("city_id", "737"));
		if(null==memberAddress) {
			memberAddress =new MemberAddress();
			memberAddress.setCommunityId(0);
			memberAddress.setAddress("请选择地址");
			memberAddress.setAreaId(0);
			memberAddress.setCreateDate(new Date());
			memberAddress.setHouseNumber("");
		}
		Integer communityId = memberAddress.getCommunityId();
		//根据小区Id查询小区信息
		Community community = communityService.selectById(communityId);
		//判断小区是否有定点回收
		if(community!=null&& StringUtils.isNotBlank(community.getFixedPointTime())&&StringUtils.isNotBlank(community.getFixedPointAddress())) {
			memberAddress.setIsFixedPoint("1");
		}else {
			memberAddress.setIsFixedPoint("0");
		}
		//判断地址是否有公司回收六废
		String companyId = companyStreetApplianceService.selectStreetApplianceCompanyId(25,memberAddress.getStreetId(), memberAddress.getCommunityId());
		if(StringUtils.isBlank(companyId)){
			memberAddress.setIsHousehold("N");
		}else{
			memberAddress.setIsHousehold("Y");
		}
		//判断该地址是否回收5公斤废纺衣物
		Integer streeCompanyId = companyStreeService.selectStreeCompanyIds(45, memberAddress.getStreetId());
		if (null != streeCompanyId){
			memberAddress.setIsFiveKg("Y");
		}else{
			memberAddress.setIsFiveKg("N");
		}
		//判断地址是否有公司回收电器
		String companyIds = companyStreetApplianceService.selectStreetApplianceCompanyId(9,memberAddress.getStreetId(), memberAddress.getCommunityId());
		if(StringUtils.isBlank(companyIds)){
			memberAddress.setIsDigital("N");
		}else {
			memberAddress.setIsDigital("Y");
		}
		//判断地址是否有公司回收大件
		Integer streetBigCompanyId = companyStreetBigService.selectStreetBigCompanyId(78,memberAddress.getStreetId());
		if(null != streetBigCompanyId){
			memberAddress.setIsDigThing("Y");
		}else {
			memberAddress.setIsDigThing("N");
		}
		return  memberAddress;

	}

	@RequestMapping("/ceMember")
	public @ResponseBody Object ceMember() throws Exception {

		Random random = new Random();
		int i = random.nextInt(200000);
		Member member1 = memberService.selectById(i);
		Member member = new Member();
		if (null!=member){
			member.setMobile(member1.getMobile());
		}
		member.setName(i+"");
		member.setMobile("123456789");
		member.setBirthday("测试哦");
		member.setGender("f");
		member.setIsCertified("F");
		member.setCity("上海市");
		member.setLinkName("王灿");
		member.setPicUrl("测试");
		member.setAliUserId(i+"");
		return memberService.insert(member);
	}

	@RequestMapping("/saveOrder")
	public @ResponseBody Object saveOrder(){
		OrderBean orderBean = new OrderBean();
		orderBean.setCategoryId(35);
		orderBean.setCityId("737");
		orderBean.setArrivalTime("2019-05-16");
		orderBean.setArrivalPeriod("pm");
		orderBean.setAddress("上海市上海区");
		orderBean.setFullAddress("c测试详细地址");
		orderBean.setTel("18633336235");
		orderBean.setLinkMan("王**");
		orderBean.setGreenCode("12233213");
		orderBean.setIsMysl("1");
		OrderPic orderPic = new OrderPic();
		orderPic.setOrigPic("www.baudu.com,www.baudu.com,www.baudu.com");
		orderPic.setPicUrl("www.baudu.com,www.baudu.com,www.baudu.com");
		orderPic.setSmallPic("www.baudu.com,www.baudu.com,www.baudu.com");
		orderBean.setOrderPic(orderPic);
		List<OrderItemBean> orderItems = new ArrayList<>();
		OrderItemBean orderItemBean1 = new OrderItemBean();
		OrderItemBean orderItemBean2 = new OrderItemBean();

		orderItemBean1.setId((long)35);
		orderItemBean2.setId((long)36);
		orderItemBean1.setParentName("");
		orderItemBean2.setParentName("");
		orderItemBean1.setPrice("3");
		orderItemBean2.setPrice("3");
		orderItemBean1.setAmount((double)3);
		orderItemBean2.setAmount((double)3);
		orderItems.add(orderItemBean1);
		orderItems.add(orderItemBean2);
		orderBean.setOrderItemList(orderItems);

		Member member = memberService.selectById(8401);
		//查询用户的默认地址
		MemberAddress memberAddress = memberAddressService.selectOne(new EntityWrapper<MemberAddress>().eq("is_selected",1).eq("del_flag", 0).eq("member_id", member.getId()).eq("city_id", orderBean.getCityId()));
		if(memberAddress==null) {
			return "您暂未添加回收地址";
		}
		//根据分类Id查询父类分类id
		Category category = categoryService.selectById(orderBean.getCategoryId());
		Integer communityId = memberAddress.getCommunityId();
		String companyId = "1";
		String level = "0";
		String areaId = memberAddress.getAreaId().toString();
		//根据分类Id和小区Id查询所属企业
//		Company companys = priceService.selectCompany(category.getParentId(),communityId);
//		if(companys == null) {
//			//根据分类Id和小区id去公海查询相关企业
//			CompanyShare companyShare =	companyShareService.selectOne(new EntityWrapper<CompanyShare>().eq("category_id", category.getParentId()).eq("area_id", areaId));
//			if(companyShare==null) {
//				return "该区域暂无回收企业";
//			}
//			companyId = companyShare.getCompanyId().toString();
//			level = "1";
//		}else {
//			companyId = companys.getId().toString();
//			level = "0";
//		}
		//Integer companyId = companyService.getCompanyIdByIds(orderbean.getCommunityId(),orderbean.getCategoryParentId());
		orderBean.setCompanyId(Integer.parseInt(companyId));
		orderBean.setLevel(level);
		orderBean.setCommunityId(communityId);
		orderBean.setAreaId(Integer.parseInt(areaId));
		orderBean.setStreetId(memberAddress.getStreetId());
		//随机生成订单号
		String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(new Random().nextInt(899999)+100000);
		orderBean.setOrderNo(orderNo);
		Map<String,Object> resultMap = (Map<String,Object>)orderService.XcxSaveOrder(orderBean,member);
		//钉钉消息赋值回收公司名称
		if (companyId != null && !"".equals(companyId)) {
			Company company = companyService.selectOne(new EntityWrapper<Company>().eq("id", companyId));
			orderBean.setCompanyName(company.getName());
			orderBean.setDingDingUrl(company.getDingDingUrl());
		}else{
			return "回收公司异常";
		}
		try {
			webSocketServer.sendInfo(companyId, "你有新订单了");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String,Object> map = new HashMap<>();
		Date date = new Date();
		SimpleDateFormat simp = new SimpleDateFormat("HH");
		String time = simp.format(date);
		if (Integer.parseInt(time)>= 20){
			map.put("type",8);
			map.put("msg","20:00后的订单，次日上午才上门回收哦！");
			map.put("code",resultMap.get("code"));
			map.put("id",resultMap.get("id"));
			return map;
		}
		map.put("type",9);
		map.put("msg","操作成功");
		map.put("code",resultMap.get("code"));
		map.put("id",resultMap.get("id"));
		return map;

	}

	@Autowired
	private MemberService memberService;
	@Autowired
	private CompanyCategoryService priceService;

}
