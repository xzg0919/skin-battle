package com.tzj.collect.api.ali;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.common.PostTool;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.common.util.ToolUtils;
import com.tzj.collect.core.param.ali.ProductBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.module.api.annotation.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;

/**
 * 积分商城相关Api
 * @author wangcan
 *
 */
@ApiService
public class ProductApi {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private PointService pointService;
	@Autowired
	private AliPayService aliPayService;
	@Autowired
	private PointListService pointListService;
	@Autowired
	private MemberAddressService memberAddressService;
	@Autowired
	private GoodsProductOrderService goodsProductOrderService;
	/**
     * 返回消耗0能量积分商城列表
     * @author 王灿
     * @param 
     */
    @Api(name = "product.getProductListNo", version = "1.0")
    @SignIgnore
	@AuthIgnore
	@DS("slave")
    public Object getProductList(ProductBean productBean) {
    	EntityWrapper<Product> wrapper = new EntityWrapper<Product>();
    	wrapper.eq("is_marketable", "1");
    	Date date = new Date();
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	wrapper.le("pick_start_date", df.format(date));
    	wrapper.ge("pick_end_date", df.format(date));
    	wrapper.eq("binding_point",0);
    	wrapper.eq("del_flag", 0);
    	wrapper.eq("goods_type", 0);
    	wrapper.like("districts_id", productBean.getCityId());
    	//wrapper.eq("districts_id", productBean.getCityId());
    	wrapper.orderBy("create_date", false);
       return productService.selectList(wrapper);
    }
    /**
     * 返回需要消耗能量商城列表
     * @author 王灿
     * @param 
     */
    @Api(name = "product.getProductListYes", version = "1.0")
    @SignIgnore
	@AuthIgnore
	@DS("slave")
    public Object getProductListYes(ProductBean productBean) {
    	EntityWrapper<Product> wrapper = new EntityWrapper<Product>();
    	wrapper.eq("is_marketable", "1");
    	Date date = new Date();
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	wrapper.le("pick_start_date", df.format(date));
    	wrapper.ge("pick_end_date", df.format(date));
    	//wrapper.ge("binding_point", 1);
    	wrapper.eq("del_flag", 0);
    	wrapper.eq("goods_type", 0);
    	wrapper.like("districts_id", productBean.getCityId());
    	//wrapper.eq("districts_id", productBean.getCityId());
    	wrapper.orderBy("create_date", false);
       return productService.selectList(wrapper);
    }
    /**
     * 实物兑换的商城列表
     * @author 王灿
     * @param 
     */
    @Api(name = "product.getProductGoodsList", version = "1.0")
    @SignIgnore
	@AuthIgnore
	@DS("slave")
    public Object getProductGoodsList(ProductBean productBean) {
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	EntityWrapper<Product> wrapper = new EntityWrapper<Product>();
    	wrapper.eq("is_marketable", "1");
    	Date date = new Date();
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	wrapper.le("pick_start_date", df.format(date));
    	wrapper.ge("pick_end_date", df.format(date));
    	wrapper.ge("binding_point", 1);
    	wrapper.eq("del_flag", 0);
    	wrapper.eq("goods_type", 1);
    	//wrapper.eq("districts_id", productBean.getCityId());
    	wrapper.orderBy("binding_point", true);
    	resultMap.put("product", productService.selectList(wrapper));
    	try {
			//获取当前登录的会员
			Member member = MemberUtils.getMember();
			//获取用户默认地址
			MemberAddress memberAddress = memberAddressService.getMemberAdderssByAliUserId(member.getAliUserId());
			resultMap.put("memberAddress", memberAddress);
		}catch (Exception e){

		}
       return resultMap;
    }
    
    /**
     * 查询券的详情
     * @author 王灿
     * @param 
     */
    @Api(name = "product.getProductDetail", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object getProductDetail(ProductBean productBean) {
    	
       return productService.selectById(productBean.getId());
    }
    
    /**
     * 给用户发券
     * @author 王灿
     * @param 
     */
    @Api(name = "product.sendVoucher", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object sendVoucher(ProductBean productBean) {
    	//获取当前登录的会员
    	Member member = MemberUtils.getMember();
    	//查询用户积分
    	Point point = pointService.getPoint(member.getAliUserId());
    	//查询此券需要消耗多少能量兑换
    	Product product = productService.selectById(productBean.getId());
    	//判断是否需要积分兑换
    	if(product.getBindingPoint()!=0) {
    		//判断用户积分是否足够
    		if(point==null) {
    			return "您的绿色能量不足";
    		}
    		if(point.getRemainPoint() < product.getBindingPoint()){
    			return "您的绿色能量不足";
    		}
    		
    	}
    	if(productBean.getId().length()>2){
			String param = "productId="+productBean.getId()+"&userAliId="+member.getAliUserId();
			System.out.println("给用户发券的参数:"+param);
			//调用给用户发券接口
			String body = PostTool.postB(ToolUtils.greenH5, param);
			Object obj = JSONObject.parse(body);
			Map<String,Object> resultMap = (Map<String,Object>)obj;
			//判断发券是否成功
			if(StringUtils.isNotBlank(body)&&(resultMap.get("flag")!=null)&&!"0".equals(resultMap.get("flag"))) {
				//发券失败
				return resultMap.get("msg");
			}
		}
		//发券成功时......更新已兑换数量+1
		product.setBindingQuantity(product.getBindingQuantity()+1);
		productService.updateById(product);
		//用户剩余积分
		double remainPoint=0;
		if(point!=null) {
			remainPoint = point.getRemainPoint()-((double)product.getBindingPoint());
			//扣除用户本地积分
			point.setRemainPoint(remainPoint);
			pointService.updateById(point);
		}
		System.out.println("给用户发券扣除的积分是 ："+product.getBindingPoint() + "----剩余point是 : "+remainPoint+"");
		//给用户增加会员卡积分
		try {
		//给用户会员卡扣除相应积分
		aliPayService.updatePoint(member.getAliCardNo(), member.getOpenCardDate(), remainPoint+"", null,member.getAppId());
		}catch(Exception e) {
			System.out.println("扣除用户积分失败---------------");
		}
		//增加相应的积分记录
		if(product.getBindingPoint()!=0) {
			PointList pointList =new  PointList();
			pointList.setAliUserId(member.getAliUserId());
			pointList.setPoint("-"+product.getBindingPoint());
			pointList.setType("1");
			pointList.setDocumentNo(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(new Random().nextInt(899999)+100000));
			pointList.setDescrb(product.getBrand());
			pointListService.insert(pointList);
		}
       return "领券成功,请到支付宝卡包查看!";
    }
    
    /**
     * 给用户发放实物
     * @author 王灿
     * @param 
     */
    @Api(name = "product.sendGoodsProduct", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object sendGoodsProduct(ProductBean productBean) {
    	//获取当前登录的会员
    	Member member = MemberUtils.getMember();
    	//查询用户积分
    	Point point = pointService.getPoint(member.getAliUserId());
    	//查询此券需要消耗多少能量兑换
    	Product product = productService.selectById(productBean.getId());
    	//判断是否需要积分兑换
    	if(product.getBindingPoint()!=0) {
    		//判断用户积分是否足够
    		if(point==null) {
    			return "您的绿色能量不足";
    		}
    		if(point.getRemainPoint() < product.getBindingPoint()){
    			return "您的绿色能量不足";
    		}
    		
    	}
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	try {
			if((new Date()).getTime() < df.parse(product.getPickStartDate()+"").getTime()&&(new Date()).getTime() > df.parse(product.getPickEndDate()+"").getTime()) {
				return "不在活动时间内，无法领取";
			}
		} catch (ParseException e) {
			
			e.printStackTrace();
		} 
    	//查询用户是否兑换过此商品
    	int count = goodsProductOrderService.selectCount(new EntityWrapper<GoodsProductOrder>().eq("ali_user_id", member.getAliUserId()).eq("product_id", productBean.getId()));
    	if(count >= Integer.parseInt(product.getPickLimitTotal())) {
    		return "您兑换超出限制，不可再兑换了";
    	}
    	MemberAddress memberAddress = null;
    	if(StringUtils.isBlank(productBean.getUserName())||StringUtils.isBlank(productBean.getMobile())||StringUtils.isBlank(productBean.getAddress())) {
    		//获取用户默认地址
    		memberAddress = memberAddressService.getMemberAdderssByAliUserId(member.getAliUserId());
    	}else {
    		//根据条件查看地址是否存在
			MemberAddress select = new MemberAddress();
				select.setName(productBean.getUserName());
				select.setTel(productBean.getMobile());
				select.setAddress(productBean.getAddress());
				select.setDelFlag("0");
    		memberAddress = memberAddressService.selectMemberAddressByAliUserIdOne(select);
    	}
    	if(memberAddress==null) {
    		return "您暂未添加收货信息";
    	}
    	//给用户发放实物
    	return goodsProductOrderService.sendGoodsProduct(product,member,point,memberAddress);
    }
    public static void main(String[] args) {
    	String productId = "0d1588036d4440c2bc66b4749ce3a6e6";
    	String aliUserId = "2088212854989662";
    	String param = "productId="+productId+"&userAliId="+aliUserId;
    	System.out.println("给用户发券的参数:"+param);
    	//调用给用户发券接口
    	String body = PostTool.postB(ToolUtils.greenH5, param);
    	System.out.println(body);
	}
}
