package com.tzj.collect.controller.admin;

import com.alipay.api.response.AntMerchantExpandTradeorderSyncResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.common.excel.ExcelData;
import com.tzj.collect.api.common.excel.ExcelUtils;
import com.tzj.collect.api.common.websocket.AppWebSocketServer;
import com.tzj.collect.api.common.websocket.WebSocketServer;
import com.tzj.collect.entity.EnterpriseCode;
import com.tzj.collect.entity.Order;
import com.tzj.collect.service.AliPayService;
import com.tzj.collect.service.EnterpriseCodeService;
import com.tzj.collect.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	private AppWebSocketServer appWebSocketServer;

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


	@RequestMapping("/geviMysl")
	public @ResponseBody  Object  geviMysl(){

		int i = 0;
		List<Order> orders = orderService.selectList(new EntityWrapper<Order>().eq("status_", 3).le("create_date", "2019-04-22 23:00:00").ge("create_date", "2019-04-16 00:00:01").in("company_id", "2,3,4,5,6,7,8,9,10").eq("title", 3).eq("is_mysl",1));
		for (Order order: orders
			 ) {
				AntMerchantExpandTradeorderSyncResponse response = aliPayService.updateForest(order.getId().toString());
				i++;
			System.out.println(i);
		}
		return "操作成功";
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

	@RequestMapping("/product/order")
	public @ResponseBody
	String updateProducrOrder() {
		System.out.println("进来了2");
		try {
			appWebSocketServer.sendInfo("121", "你是回收经理");
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
}
