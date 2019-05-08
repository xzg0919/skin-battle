package com.tzj.collect.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alipay.api.response.AntMerchantExpandTradeorderSyncResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.ali.param.OrderBean;
import com.tzj.collect.api.common.excel.ExcelData;
import com.tzj.collect.api.common.excel.ExcelUtils;
import com.tzj.collect.api.common.websocket.AppWebSocketServer;
import com.tzj.collect.api.common.websocket.WebSocketServer;
import com.tzj.collect.api.common.websocket.XcxWebSocketServer;
import com.tzj.collect.entity.EnterpriseCode;
import com.tzj.collect.entity.Order;
import com.tzj.collect.service.AliPayService;
import com.tzj.collect.service.AnsycMyslService;
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
	private AnsycMyslService ansycMyslService;

	@Autowired
	private AppWebSocketServer appWebSocketServer;
	@Autowired
	private XcxWebSocketServer xcxWebSocketServer;

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

		OrderBean orderBean = orderService.myslOrderData("10413");
		if (null!=orderBean&&"0".equals(orderBean.getIsRisk())){
			ansycMyslService.updateForest("10413",orderBean.getMyslParam());
		}
		return "操作成功";
	}
}
