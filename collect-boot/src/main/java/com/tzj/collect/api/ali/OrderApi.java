package com.tzj.collect.api.ali;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.taobao.api.ApiException;
import com.tzj.collect.api.ali.param.CategoryBean;
import com.tzj.collect.api.ali.param.OrderBean;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.collect.api.common.websocket.WebSocketServer;
import com.tzj.collect.api.enterprise.param.EnterpriseCodeBean;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.entity.*;
import com.tzj.collect.entity.Order.OrderType;
import com.tzj.collect.service.*;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;

/**
 * 订单相关api
 * @Author 王美霞20180305
 **/
@ApiService
public class OrderApi {

	@Autowired
	private EnterpriseCodeService enterpriseCodeService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private CompanyCategoryService priceService;
	@Autowired
	private AsyncService asyncService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private MemberAddressService memberAddressService;
	@Autowired
	private WebSocketServer webSocketServer;
	@Autowired
	private EnterpriseProductService enterpriseProductService;
	@Autowired
	private ApplicationInit applicationInit;
	@Autowired
	private CompanyStreeService companyStreeService;
	@Autowired
	private AliPayService aliPayService;
	@Autowired
	private CompanyStreetBigService companyStreetBigService;
	@Autowired
	private AnsycMyslService ansycMyslService;
	@Autowired
	private CompanyStreetApplianceService companyStreetApplianceService;
	@Autowired
	private CompanyCategoryService companyCategoryService;
	@Autowired
	private AreaService areaService;
	

	/**
     * 获取会员的未完成订单列表 不分页
     * @author 王灿
     * @param 
     * @return List<Order>:未完成的订单列表
     */
    @Api(name = "order.unfinishlist", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public List<Order> orderUnfinishlist() {
    	Member member = MemberUtils.getMember();
    	List<Order> list = orderService.getUncompleteList(member.getId());
       return list;
    }
    
    /**
     * 获取会员的订单列表 分页
     * @author 王灿
     * @param 
     * @return
     */
    @Api(name = "order.orderlist", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Map<String,Object> orderlist(OrderBean orderBean){
		Integer status = null;
		PageBean pageBean = orderBean.getPagebean();
		if(!StringUtils.isBlank(orderBean.getStatus())){
			status = Integer.parseInt(orderBean.getStatus());
		}else{
			status = -1;
		}
		//获取当前登录的会员信息
    	Member member = MemberUtils.getMember();
    	//根据会员ID回去订单列表
    	Map<String,Object> map = orderService.getOrderlist(member.getId(),status,pageBean.getPageNumber(),pageBean.getPageSize());
    	return map;
    }
    
    
    /**
     * 根据订单id获取订单详情
     * @author 王灿
     * @param orderbean : 
     * @return
     */
    @Api(name = "order.detail", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Map<String,Object> getOrderDetail(OrderBean orderbean){
    	Map<String,Object> map = orderService.selectDetail(orderbean);
    	return map;
    }
    
        
    /**
     * 取消订单
     * @author 王灿
     * @param 
     * @return
     */
    @Api(name = "order.cancel", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public String cancelOrder(OrderBean orderbean){
    	Order order = orderService.selectById(orderbean.getId());
    	if("3".equals(order.getStatus().getValue()+"")){
			return "订单已被完成无法取消";
		}
    	String orderInitStatus = order.getStatus().toString();
    	order.setStatus(OrderType.CANCEL);
    	//取消原因
    	order.setCancelReason(orderbean.getCancelReason());
    	//取消时间
    	order.setCancelTime(new Date());
    	String status = orderService.orderCancel(order,orderInitStatus);
    	return status;
    }
    
    /**
     * 完成订单
     * @author 王灿
     * @param 
     * @return
     */
    @Api(name = "order.completeOrder", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public String completeOrder(OrderBean orderbean){
    	Order order = orderService.selectById(orderbean.getId());
    	String orderInitStatus = order.getStatus().toString();
    	order.setStatus(OrderType.COMPLETE);
    	//完成时间
    	order.setCompleteDate(new Date());
    	String status = orderService.completeOrder(order,orderInitStatus);
    	return status;
    }
    
    /**
     * 下单接口
     * @author 王灿
     * @param 
     * @return
     * @throws ApiException 
     */
    @Api(name = "order.create", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object createOrder(OrderBean orderbean) throws ApiException{
    	Member member = MemberUtils.getMember();
    	//根据当前登录的会员，获取姓名、绿账号和阿里userId
    	orderbean.setMemberId(Integer.parseInt(member.getId().toString()));
    	orderbean.setGreenCode(member.getGreenCode());
    	orderbean.setAliUserId(member.getAliUserId());
    	//查询用户的默认地址
    	MemberAddress memberAddress = memberAddressService.getMemberAdderssByMemberId(member.getId().toString());
    	if(memberAddress==null) {
    		return "您暂未添加回收地址";
    	}
    	//根据分类Id查询父类分类id
    	Category category = categoryService.selectById(orderbean.getCategoryId());
    	Integer communityId = memberAddress.getCommunityId();
    	String level = "1";
    	String areaId = memberAddress.getAreaId().toString();
		//根据分类Id、街道id和小区Id查询所属企业
		String companyId = companyStreetApplianceService.selectStreetApplianceCompanyId(category.getParentId(), memberAddress.getStreetId(), communityId);
		if (StringUtils.isBlank(companyId)){
			return "该区域暂无回收企业";
		}
		orderbean.setAddress(memberAddressService.getMemberAddressById(memberAddress.getId().toString()));
    	orderbean.setCompanyId(Integer.parseInt(companyId));
    	orderbean.setLevel(level);
    	orderbean.setCommunityId(communityId);
		orderbean.setStreetId(memberAddress.getStreetId());
    	//随机生成订单号
    	String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(new Random().nextInt(899999)+100000);
    	orderbean.setOrderNo(orderNo);
    	//保存订单
    	Map<String,Object> resultMap = orderService.saveOrder(orderbean);
    	//钉钉消息赋值回收公司名称
		Company company = companyService.selectById(companyId);
		if(null != company){
			//判断是否开启自动派单
			if("1".equals(company.getIsOpenOrder())){
				orderService.orderSendRecycleByOrderId((Integer) resultMap.get("id"));
			}
			orderbean.setCompanyName(company.getName());
			orderbean.setDingDingUrl(company.getDingDingUrl());
			if("操作成功".equals(resultMap.get("msg")+"")) {
				if("true".equals(applicationInit.getIsDd())) {
					//钉钉通知
					asyncService.notifyDingDingOrderCreate(orderbean);
				}
			}
		}
    	try {
			webSocketServer.sendInfo(companyId, "你有新订单了");
		} catch (Exception e) {
			e.printStackTrace();
		}

    	return resultMap;
    }
	/**
     * 根据小区Id和分类Id查询所属的企业
     * @author 王灿
     * @param 
     * @return
     */
    @Api(name = "order.getCompanyByIds", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object getCompanyByIds(OrderBean orderbean){
    	Member member = MemberUtils.getMember();
    	//查询用户的默认地址
    	MemberAddress memberAddress = memberAddressService.getMemberAdderssByMemberId(member.getId().toString());
    	if(memberAddress==null) {
    		return "您暂未添加回收地址";
    	}
    	String companyId = "";
		if("FIVEKG".equals(orderbean.getType())){
			//判断该地址是否回收5公斤废纺衣物
			Integer streeCompanyId = companyStreeService.selectStreeCompanyIds(orderbean.getCategoryId(), memberAddress.getStreetId());
			if(null==streeCompanyId){
				return "该区域暂无回收企业";
			}
			companyId = streeCompanyId+"";
		}else if("BIGTHING".equals(orderbean.getType())){
			//判断地址是否有公司回收大件
			Integer streetBigCompanyId = companyStreetBigService.selectStreetBigCompanyId(orderbean.getCategoryId(),memberAddress.getStreetId());
			if(null==streetBigCompanyId){
				return "该区域暂无回收企业";
			}
			companyId = streetBigCompanyId+"";
		}else if("DIGITAL".equals(orderbean.getType())){
			//根据分类Id和小区id去公海查询相关企业
			String companyId1 = companyStreetApplianceService.selectStreetApplianceCompanyId(orderbean.getCategoryId(), memberAddress.getStreetId(),memberAddress.getCommunityId());
			if(StringUtils.isBlank(companyId1)) {
				return "该区域暂无回收企业";
			}
			companyId = companyId1.toString();
		}else {
			//根据分类Id和小区Id查询所属企业
			Company companys = companyCategoryService.selectCompany(orderbean.getCategoryId(),memberAddress.getCommunityId());
			if(null==companys){
				//判断该地址是否回收5公斤废纺衣物
				Integer streeCompanyId = companyStreeService.selectStreeCompanyIds(orderbean.getCategoryId(), memberAddress.getStreetId());
				if(null==streeCompanyId){
					return "该区域暂无回收企业";
				}else {
					companyId = streeCompanyId+"";
				}
			}else {
				companyId = companys.getId().toString();
			}
		}
		Company company1 = companyService.selectById(companyId);
		return company1;
    }
    /**
     * 获取详细价格表并分页
     * 1.有地址(communityId),获取当前公司下的价格
     * 2.没有地址,获取平均价格
     * @param
     * @return
     */
    @Api(name = "order.getprice", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Map<String, Object> getPrice(CategoryBean categoryBean) {
		return priceService.getPrice(categoryBean); 
	}
	/**
	 * 小程序保存六废订单
	 * @author 王灿
	 * @param
	 * @return
	 */
	@Api(name = "order.XcxSaveOrder", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object XcxSaveOrder(OrderBean orderbean){
		Member member = MemberUtils.getMember();
		//查询用户的默认地址
		MemberAddress memberAddress = memberAddressService.getMemberAdderssByMemberId(member.getId().toString());
		if(memberAddress==null) {
			return "您暂未添加回收地址";
		}
		//根据分类Id查询父类分类id
		Category category = categoryService.selectById(orderbean.getCategoryId());
		Integer communityId = memberAddress.getCommunityId();
		String companyId = "";
		String level = "";
		String areaId = memberAddress.getAreaId().toString();
		//根据分类Id和小区Id查询所属企业
		Company companys = priceService.selectCompany(category.getParentId(),communityId);
		if(companys == null) {
			return "该区域暂无回收企业";
		}else {
			companyId = companys.getId().toString();
		}
		//Integer companyId = companyService.getCompanyIdByIds(orderbean.getCommunityId(),orderbean.getCategoryParentId());
		orderbean.setCompanyId(Integer.parseInt(companyId));
		orderbean.setLevel(level);
		orderbean.setCommunityId(communityId);
		orderbean.setAreaId(Integer.parseInt(areaId));
		orderbean.setStreetId(memberAddress.getStreetId());
		orderbean.setAddress(memberAddressService.getMemberAddressById(memberAddress.getId().toString()));
		//随机生成订单号
		String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(new Random().nextInt(899999)+100000);
		orderbean.setOrderNo(orderNo);
		Map<String,Object> resultMap = (Map<String,Object>)orderService.XcxSaveOrder(orderbean,member);
		//钉钉消息赋值回收公司名称
		Company company = companyService.selectById(companyId);
		if(null != company){
			//判断是否开启自动派单
			if("1".equals(company.getIsOpenOrder())){
				orderService.orderSendRecycleByOrderId((Integer) resultMap.get("id"));
			}
			orderbean.setCompanyName(company.getName());
			orderbean.setDingDingUrl(company.getDingDingUrl());
			if("操作成功".equals(resultMap.get("msg")+"")) {
				if("true".equals(applicationInit.getIsDd())) {
					//钉钉通知
					asyncService.notifyDingDingOrderCreate(orderbean);
				}
			}
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
			map.put("type",9);
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
	/**
	 * 根据以旧换新码查询此码是否存在
	 * @return
	 */
	@Api(name = "order.isEnterpriseCodeByCode", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object isEnterpriseCodeByCode(EnterpriseCodeBean enterpriseCodeBean){
		EnterpriseCode enterpriseCode = enterpriseCodeService.selectOne(new EntityWrapper<EnterpriseCode>().eq("code", enterpriseCodeBean.getCode()).eq("del_flag", 0));
		Map<String,Object> map = new HashMap<>();
		if(null!=enterpriseCode){
			if("0".equals(enterpriseCode.getIsUse())){
				EnterpriseProduct enterpriseProduct = enterpriseProductService.selectById(enterpriseCode.getProductId());
				if((enterpriseProduct.getCategoryId()+"").equals(enterpriseCodeBean.getCategoryId())){
					map.put("status","YES");
					map.put("enterpriseCode",enterpriseCode);
					return map;
				}else {
					map.put("status","NO");
					map.put("message","此码不可在该回收类型使用");
					return map;
				}
			}else{
				map.put("status","NO");
				map.put("message","此码已使用");
				return map;
			}
		}else{
			map.put("status","NO");
			map.put("message","此码不存在");
			return map;
		}
	}
	/**
	 * 保存5公斤废纺衣物的订单
	 * @return
	 */
	@Api(name = "order.savefiveKgOrder", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object savefiveKgOrder(OrderBean orderbean){
		//获取当前登录的会员
		Member member = MemberUtils.getMember();
		MemberAddress memberAddress = memberAddressService.getMemberAdderssByMemberId(member.getId().toString());
		if(memberAddress==null) {
			return "您暂未添加回收地址";
		}
		//根据分类Id查询父类分类id
		Category category = categoryService.selectById(orderbean.getCategoryId());
		orderbean.setMemberId(member.getId().intValue());
		orderbean.setAliUserId(member.getAliUserId());
		if(null == orderbean.getOrderItemList()){
			return "请选择详细内容";
		}
		orderbean.setCategoryId(orderbean.getOrderItemList().get(0).getId().intValue());
		orderbean.setCategoryParentIds(orderbean.getOrderItemList().get(0).getParentId());
		Integer communityId = memberAddress.getCommunityId();
		String areaId = memberAddress.getAreaId().toString();
		//判断该地址是否回收5公斤废纺衣物
		Integer streeCompanyId = companyStreeService.selectStreeCompanyIds(45, memberAddress.getStreetId());
		if (streeCompanyId == null ){
			return "该区域暂无回收企业";
		}
		orderbean.setCompanyId(streeCompanyId);
		orderbean.setCommunityId(communityId);
		orderbean.setAreaId(Integer.parseInt(areaId));
		orderbean.setStreetId(memberAddress.getStreetId());
		orderbean.setAddress(memberAddressService.getMemberAddressById(memberAddress.getId().toString()));
		//随机生成订单号
		String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(new Random().nextInt(899999)+100000);
		orderbean.setOrderNo(orderNo);
		Map<String,Object> resultMap = (Map<String,Object>)orderService.savefiveKgOrder(orderbean);
		//钉钉消息赋值回收公司名称
		Company company = companyService.selectById(streeCompanyId);
		if(null != company){
			//判断是否开启自动派单
			if("1".equals(company.getIsOpenOrder())){
				orderService.tosendfiveKgOrder((Integer) resultMap.get("id"));
			}
			orderbean.setCompanyName(company.getName());
			orderbean.setDingDingUrl(company.getDingDingUrl());
			if("操作成功".equals(resultMap.get("msg")+"")) {
				if("true".equals(applicationInit.getIsDd())) {
					//钉钉通知
					asyncService.notifyDingDingOrderCreate(orderbean);
				}
			}
		}
		if("操作成功".equals(resultMap.get("msg")+"")){
			Map<String,Object> map = new HashMap<>();
			map.put("type",9);
			map.put("msg","操作成功");
			map.put("code",resultMap.get("code"));
			map.put("id",resultMap.get("id"));
			return map;
		}else {
			return resultMap;
		}
	}
	@Api(name = "order.updateForest", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object updateForest(OrderBean orderbean){
		//给用户增加蚂蚁能量
		OrderBean orderBean = orderService.myslOrderData(orderbean.getId().toString());
		return "操作成功";
	}

	/**
	 * 小程序大家具下单接口
	 * @author 王灿
	 * @param
	 * @return
	 * @throws ApiException
	 */
	@Api(name = "order.saveBigThingOrder", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object saveBigThingOrder(OrderBean orderbean) throws ApiException{
		Member member = MemberUtils.getMember();
		//根据当前登录的会员，获取姓名、绿账号和阿里userId
		orderbean.setMemberId(Integer.parseInt(member.getId().toString()));
		orderbean.setAliUserId(member.getAliUserId());
		//查询用户的默认地址
		MemberAddress memberAddress = memberAddressService.getMemberAdderssByMemberId(member.getId().toString());
		if(memberAddress==null) {
			return "您暂未添加回收地址";
		}
		//根据分类Id查询父类分类id
		Category category = categoryService.selectById(orderbean.getCategoryId());
		Integer communityId = memberAddress.getCommunityId();
		String level = "0";
		String areaId = memberAddress.getAreaId().toString();
		//根据分类Id和小区Id查询所属企业
		Integer streetBigCompanyId = companyStreetBigService.selectStreetBigCompanyId(category.getParentId(), memberAddress.getStreetId());
		if(streetBigCompanyId == null) {
				return "该区域暂无回收企业";
		}
		orderbean.setCompanyId(streetBigCompanyId);
		orderbean.setLevel(level);
		orderbean.setCommunityId(communityId);
		orderbean.setStreetId(memberAddress.getStreetId());
		orderbean.setAreaId(Integer.parseInt(areaId));
		orderbean.setAddress(memberAddressService.getMemberAddressById(memberAddress.getId().toString()));
		//随机生成订单号
		String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(new Random().nextInt(899999)+100000);
		orderbean.setOrderNo(orderNo);
		//保存订单
		Map<String,Object> resultMap = orderService.saveBigThingOrder(orderbean);
		//钉钉消息赋值回收公司名称
		if (StringUtils.isNoneBlank(streetBigCompanyId+"")) {
			Company company = companyService.selectOne(new EntityWrapper<Company>().eq("id", streetBigCompanyId));
			orderbean.setCompanyName(company.getName());
			orderbean.setDingDingUrl(company.getDingDingUrl());
		}else{
			throw new ApiException("回收公司异常！！！！！");
		}
		if("操作成功".equals(resultMap.get("msg")+"")) {
			if("true".equals(applicationInit.getIsDd())) {
				//钉钉通知
				asyncService.notifyDingDingOrderCreate(orderbean);
			}
		}
		try {
			webSocketServer.sendInfo(streetBigCompanyId+"", "你有新订单了");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

}
