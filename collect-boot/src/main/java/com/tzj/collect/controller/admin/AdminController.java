package com.tzj.collect.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.response.AntMerchantExpandTradeorderSyncResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.ali.param.OrderBean;
import com.tzj.collect.api.common.excel.ExcelData;
import com.tzj.collect.api.common.excel.ExcelUtils;
import com.tzj.collect.api.common.websocket.AppWebSocketServer;
import com.tzj.collect.api.common.websocket.WebSocketServer;
import com.tzj.collect.api.common.websocket.XcxWebSocketServer;
import com.tzj.collect.common.constant.RocketMqConst;
import com.tzj.collect.entity.*;
import com.tzj.collect.service.*;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

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



	@RequestMapping("/update/rocket")
	public @ResponseBody Object rocket() throws Exception {
		List<Order> orderList = orderService.selectList(new EntityWrapper<Order>().le("create_date", "2019-05-10 23:59:40").ge("create_date", "2019-05-10 00:00:01").eq("title", 3).in("status_","0,1"));
		for (Order order:orderList) {
			SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
			Company company = companyService.selectById(order.getCompanyId());
			if(null==company||null==company.getAliMns()){
				continue;
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
				param.put("arrivalTime", sim.format(order.getArrivalTime())+" "+("am".equals(order.getArrivalPeriod())?"10:00:00":"16:00:00"));
				param.put("isCancel","N");
				RocketMqConst.sendDeliveryOrder(JSON.toJSONString(param),company.getAliMns());
			}catch (Exception e){
				e.printStackTrace();
			}
		}


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
	public @ResponseBody String mysl() {

		OrderBean orderBean = orderService.myslOrderData("14378");
		return "操作成功";
	}


}
