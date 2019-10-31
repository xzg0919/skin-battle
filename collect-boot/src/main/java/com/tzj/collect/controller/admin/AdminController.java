package com.tzj.collect.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.commom.excel.ExcelData;
import com.tzj.collect.api.commom.excel.ExcelUtils;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.param.business.CityBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("admin")
public class AdminController {

	@Autowired
	private EnterpriseCodeService enterpriseCodeService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private AsyncService asyncService;


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
	@RequestMapping("/area/addExcel")
	public String areaAddExcel() {
		return "admin/addAreaExcel";
	}

	@RequestMapping("/area/excel")
	public String areaExcel() {
		return "admin/areaExcel";
	}

	@RequestMapping("/out/excel")
	public void outExcel(String page,HttpServletResponse response)throws Exception {
		String url = "https://restapi.amap.com/v3/config/district";
		String param = "key=43d675a28f264a241bd878df2c420176&subdistrict=4&page="+page;
		String s = this.sendGet(url, param);
		JSONObject object = JSON.parseObject(s);
		Object districts = object.get("districts");

		ExcelData data = new ExcelData();
		data.setName("以旧换新信息数据");
		//添加表头
		List<String> titles = new ArrayList<>();
		//for(String title: excelInfo.getNames())
		titles.add("省");
		titles.add("citycode");
		titles.add("adcode");
		titles.add("市");
		titles.add("citycode");
		titles.add("adcode");
		titles.add("区");
		titles.add("citycode");
		titles.add("adcode");
		titles.add("街道");
		titles.add("citycode");
		titles.add("adcode");
		data.setTitles(titles);
		//添加列
		List<List<Object>> rows = new ArrayList();
		List<Object> row = null;
		List<Object> objList= (List<Object>) JSONArray.fromObject(districts);
		for ( Object objects:objList) {
			CityBean cityBean=(CityBean)JSONObject.parseObject(objects.toString(), CityBean.class);
			System.out.println(cityBean.getCitycode());
			System.out.println(cityBean.getAdcode());
			System.out.println(cityBean.getName());
			List<CityBean> districts1 = cityBean.getDistricts();
			for (CityBean cityBean1:districts1){
				System.out.println(cityBean1.getCitycode());
				System.out.println(cityBean1.getAdcode());
				System.out.println(cityBean1.getName());
				List<CityBean> districts2 = cityBean1.getDistricts();
				for (CityBean cityBean2:districts2){
					System.out.println(cityBean2.getCitycode());
					System.out.println(cityBean2.getAdcode());
					System.out.println(cityBean2.getName());
					List<CityBean> districts3 = cityBean2.getDistricts();
					for (CityBean cityBean3:districts3){
						System.out.println(cityBean3.getCitycode());
						System.out.println(cityBean3.getAdcode());
						System.out.println(cityBean3.getName());
						List<CityBean> districts4 = cityBean3.getDistricts();
						for (CityBean cityBean4:districts4){
							System.out.println(cityBean4.getCitycode());
							System.out.println(cityBean4.getAdcode());
							System.out.println(cityBean4.getName());
							row=new ArrayList();
							row.add(cityBean1.getName());
							row.add(cityBean1.getCitycode());
							row.add(cityBean1.getAdcode());
							row.add(cityBean2.getName());
							row.add(cityBean2.getCitycode());
							row.add(cityBean2.getAdcode());
							row.add(cityBean3.getName());
							row.add(cityBean3.getCitycode());
							row.add(cityBean3.getAdcode());
							row.add(cityBean4.getName());
							row.add(cityBean4.getCitycode());
							row.add(cityBean4.getAdcode());
							rows.add(row);
						}
					}
				}
			}
		}
		data.setRows(rows);
		SimpleDateFormat fdate=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String fileName=fdate.format(new Date())+".xlsx";
		ExcelUtils.exportExcel(response, fileName, data);
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





	@Autowired
	private MemberService memberService;
	@Autowired
	private CompanyCategoryService priceService;
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
}
