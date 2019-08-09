package com.tzj.collect.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.core.service.AreaService;
import com.tzj.collect.core.service.AsyncService;
import com.tzj.collect.core.service.GoodsProductOrderService;
import com.tzj.collect.core.service.ProductService;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.CityType;
import com.tzj.collect.entity.GoodsProductOrder;
import com.tzj.collect.entity.GoodsProductOrder.GoodsState;
import com.tzj.collect.entity.Product;
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
@RequestMapping("/product/order")
public class ProductOrderController {
	
	@Autowired
	private GoodsProductOrderService goodsProductOrderService;
	@Autowired
	private ProductService productService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private AsyncService asyncService;
	
	/**
	 * 代发货的商品订单列表
	 * @author 王灿
	 * @param mobile : 手机号
	 * @param productName : 商品名字
	 * @return
	 */
	@RequestMapping("/getProduct/orderLists")
	public String getProductOrderList(String mobile,String productName,final ModelMap model) {
		EntityWrapper wrapper = new EntityWrapper<GoodsProductOrder>();
		wrapper.eq("goods_state", 0);
		wrapper.eq("del_flag", 0);  
		if(StringUtils.isNotBlank(productName)) {
			wrapper.like("product_name", productName);	
		}
		if(StringUtils.isNotBlank(mobile)) {
			wrapper.like("mobile", mobile);	
		}
		List<GoodsProductOrder> list = goodsProductOrderService.selectList(wrapper);
		model.addAttribute("ProductOrderList", list);
		model.addAttribute("count",list.size());
		model.addAttribute("companyList", this.getCompanyList());
		return "admin/productList"; 
	}
	
	/**
	 * 已发货的商品订单列表
	 * @author 王灿
	 * @param mobile : 手机号
	 * @param productName : 商品名字
	 * @return
	 */
	@RequestMapping("/sendProductOrderList")
	public String sendProductOrderList(String mobile,String productName,final ModelMap model) {
		EntityWrapper wrapper = new EntityWrapper<GoodsProductOrder>();
		wrapper.eq("goods_state", 1);
		wrapper.eq("del_flag", 0);  
		if(StringUtils.isNotBlank(productName)) {
			wrapper.like("product_name", productName);	
		}
		if(StringUtils.isNotBlank(mobile)) {
			wrapper.like("mobile", mobile);	
		}
		wrapper.orderBy("create_date", false);
		List<GoodsProductOrder> list = goodsProductOrderService.selectList(wrapper);
		model.addAttribute("ProductOrderList", list);
		return "admin/productSendList"; 
	}
	
	/**
	 * 代发货的商品订单列表
	 * @author 王灿
	 * @param id : 手机号
	 * @param orderNum : 订单号
	 * @param orderCompany : 快递公司名称
	 * @return
	 */
	@RequestMapping("/updateProductOrderById")
	public String updateProductOrderById(String id,String orderNum,String orderCompany,final ModelMap model) {
		GoodsProductOrder goodsProductOrder = goodsProductOrderService.selectById(id);
		goodsProductOrder.setOrderNum(orderNum);
		goodsProductOrder.setOrderCompany(orderCompany);
		goodsProductOrder.setGoodsState(GoodsState.TOSEND);
		goodsProductOrderService.updateById(goodsProductOrder);
		//发送接单短信
		asyncService.sendOrderProduct("垃圾分类回收", goodsProductOrder.getMobile(), "SMS_142949225",goodsProductOrder.getProductName(), orderCompany, orderNum);
		return "redirect:getProduct/orderLists";
	}
	/**
	 * 商品列表
	 * @author 王灿
	 * @return
	 */
	@RequestMapping("/getProductList")
	public String getProductList(String id,String cityName,String cityId,String orderCompany,final ModelMap model) {
		Area area = null;
		//根据城市名获取城市信息
		if(!StringUtils.isBlank(cityId)) {
			area = areaService.selectById(cityId);
		}else {
			area = areaService.selectOne(new EntityWrapper<Area>().eq("area_name", StringUtils.isBlank(cityName)?"上海市":cityName));
		}
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		EntityWrapper wrapper = new EntityWrapper<Product>();
		wrapper.eq("del_flag", 0);
    	wrapper.ge("pick_end_date", df.format(date)); 
    	wrapper.orderBy("pick_start_date", true);  
    	wrapper.like("districts_id", area.getId().toString());
    	List<Product> productList = productService.selectList(wrapper);
    	//获取城市列表
    	List<CityType> cityList = CityType.getALL();
    	model.addAttribute("productList", productList);
    	model.addAttribute("cityList", cityList);
    	model.addAttribute("cityName", area.getAreaName());
		return "admin/productCity";
	}
	/**
	 * 更新商品适用名称
	 * @author 王灿
	 * @return
	 */
	@RequestMapping("/updateProductShopName")
	public String updateProductShopName(String id,String shopName,final ModelMap model) {
		String cityId = "";
		if(StringUtils.isNotBlank(id)) {
			Product product = productService.selectById(id);
			product.setAddress(shopName);
			productService.updateById(product);
			cityId = product.getDistrictsId();
		}
		return "redirect:getProductList?cityId="+cityId;
	}
	/**
	 * 更新商品兑换所需积分
	 * @author 王灿
	 * @return
	 */
	@RequestMapping("/updateProductPoint")
	public @ResponseBody String updateProductPoint(String id,String point,final ModelMap model) {
		if(StringUtils.isNotBlank(id)) {
			Product product = productService.selectById(id);
			product.setBindingPoint(StringUtils.isBlank(point)?0:Integer.parseInt(point.replace(",","")));
			productService.updateById(product);
		}
		return "success";
	}
	
	public List<String> getCompanyList(){
		List<String> companyList = new ArrayList<String>();
			companyList.add("韵达快递");
			companyList.add("邮政快递");
			companyList.add("顺丰快递");
		return companyList;
	}
}
