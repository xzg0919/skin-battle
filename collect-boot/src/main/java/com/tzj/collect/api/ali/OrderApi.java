package com.tzj.collect.api.ali;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.tzj.collect.api.commom.constant.WXConst;
import com.tzj.collect.commom.redis.RedisUtil;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.config.ApplicationInit;
import com.tzj.collect.core.mapper.CompanyStreetAppSmallMapper;
import com.tzj.collect.core.param.ali.CategoryBean;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.core.param.app.OrderPayParam;
import com.tzj.collect.core.param.enterprise.EnterpriseCodeBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.collect.entity.Order.OrderType;
import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;
import com.tzj.module.api.annotation.*;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.easyopen.ApiContext;
import com.tzj.module.easyopen.exception.ApiException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import redis.clients.jedis.JedisPool;

/**
 * 订单相关api
 *
 * @Author 王美霞20180305
 *
 */
@ApiService
@Slf4j
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
    @Resource(name= "mqtt4PushOrder")
    private MqttClient mqtt4PushOrder;
    @Autowired
    private CompanyStreetHouseService companyStreetHouseService;
    @Autowired
    private ImprisonMemberService imprisonMemberService;
    @Autowired
    private ImprisonRuleService imprisonRuleService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private MemberService memberService;
    @Resource
    private JedisPool jedisPool;
    @Autowired
    private OrderOperateService orderOperateService;
    @Autowired
    private CompanyStreetAppSmallService companyStreetAppSmallService;
    @Autowired
    private BlackListService blackListService;
    @Autowired
    private WXPayService wxPayService;


    /**
     * 获取会员的未完成订单列表 不分页
     *
     * @author 王灿
     * @param
     * @return List<Order>:未完成的订单列表
     */
    @Api(name = "order.unfinishlist", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public List<Order> orderUnfinishlist() {
        Member member = MemberUtils.getMember();
        List<Order> list = orderService.getUncompleteList(member.getAliUserId());
        return list;
    }

    /**
     * 获取会员的订单列表 分页
     *
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "order.orderlist", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Map<String, Object> orderlist(OrderBean orderBean) {
        Integer status = null;
        PageBean pageBean = orderBean.getPagebean();
        if (!StringUtils.isBlank(orderBean.getStatus())) {
            status = Integer.parseInt(orderBean.getStatus());
        } else {
            status = -1;
        }
        //获取当前登录的会员信息
        Member member = MemberUtils.getMember();
        //根据会员ID回去订单列表
        Map<String, Object> map = orderService.getOrderlist(member.getAliUserId(), status, pageBean.getPageNumber(), pageBean.getPageSize());
        return map;
    }

    /**
     * 根据订单id获取订单详情
     *
     * @author 王灿
     * @param orderbean :
     * @return
     */
    @Api(name = "order.detail", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Map<String, Object> getOrderDetail(OrderBean orderbean) {
        Map<String, Object> map = orderService.selectDetail(orderbean);
        return map;
    }

    /**
     * 取消订单
     *
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "order.cancel", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public String cancelOrder(OrderBean orderbean) {
        Order order = orderService.selectById(orderbean.getId());
        if ("3".equals(order.getStatus().getValue() + "")) {
            return "订单已被完成无法取消";
        }
        String orderInitStatus = order.getStatus().toString();
        order.setStatus(OrderType.CANCEL);
        //取消原因
        order.setCancelReason(orderbean.getCancelReason());
        //取消时间
        order.setCancelTime(new Date());

        OrderOperate orderOperate = new OrderOperate();
        orderOperate.setOrderNo(order.getOrderNo());
        orderOperate.setOperateLog("取消订单");
        orderOperate.setReason(orderbean.getCancelReason());
        orderOperate.setOperatorMan("用户");
        orderOperateService.insert(orderOperate);

        String status = orderService.orderCancel(order, orderInitStatus,mqtt4PushOrder);
        return status;
    }

    /**
     * 完成订单
     *
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "order.completeOrder", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public String completeOrder(OrderBean orderbean) {
        Order order = orderService.selectById(orderbean.getId());
        String orderInitStatus = order.getStatus().toString();
        order.setStatus(OrderType.COMPLETE);
        //完成时间
        order.setCompleteDate(new Date());
        String status = orderService.completeOrder(order, orderInitStatus);
        return status;
    }

    /**
     * 家电下单接口
     *
     * @author 王灿
     * @param
     * @return
     * @throws ApiException
     */
    @Api(name = "order.create", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object createOrder(OrderBean orderbean) throws ApiException {
        Member member = MemberUtils.getMember();
        Map<String, Object> resultMap = null;
        Boolean isImprisonMember = false;
        Boolean isImprisonRule = false;
        if(memberService.checkBlackList(member.getAliUserId(), Order.TitleType.DIGITAL)){
            throw new ApiException("您的账号异常，限制下单，如有疑问请联系客服");
        }
        isImprisonMember = imprisonMemberService.isImprisonMember(member.getAliUserId(), "1");
        if (isImprisonMember) {
            resultMap = new HashMap<>();
            resultMap.put("type", 5);
            resultMap.put("msg", "近期下单次数过多，系统检测异常，如有疑问请联系客服");
            resultMap.put("code", 5);
            return resultMap;
        }
        isImprisonRule = imprisonRuleService.isImprisonRuleByAliUserId(member.getAliUserId(), "1");
        if (isImprisonRule) {
            resultMap = new HashMap<>();
            resultMap.put("type", 5);
            resultMap.put("msg", "近期下单次数过多，系统检测异常，如有疑问请联系客服");
            resultMap.put("code", 5);
            return resultMap;
        }
        //根据当前登录的会员，获取姓名、绿账号和阿里userId
        orderbean.setMemberId(Integer.parseInt(member.getId().toString()));
        orderbean.setGreenCode(member.getGreenCode());
        orderbean.setAliUserId(member.getAliUserId());
        //查询用户的默认地址
        MemberAddress memberAddress = memberAddressService.getMemberAdderssByAliUserId(member.getAliUserId());
        if (memberAddress == null) {
            return "您暂未添加回收地址";
        }
        //根据分类Id查询父类分类id
        Category category = categoryService.selectById(orderbean.getCategoryId());
        Integer communityId = memberAddress.getCommunityId();
        String level = "1";
        String areaId = memberAddress.getAreaId().toString();
        //根据分类Id、街道id和小区Id查询所属企业
        String companyId = companyStreetApplianceService.selectStreetApplianceCompanyIdByCategoryId(category.getParentId(), memberAddress.getStreetId(), communityId);
        if (StringUtils.isBlank(companyId)) {
            return "该区域暂无回收企业";
        }
        orderbean.setAddress(memberAddressService.getMemberAddressById(memberAddress.getId().toString(), member.getAliUserId()));
        orderbean.setCompanyId(Integer.parseInt(companyId));
        orderbean.setLevel(level);
        orderbean.setCommunityId(communityId);
        orderbean.setStreetId(memberAddress.getStreetId());
        //随机生成订单号
        String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);
        if (StringUtils.isNotBlank(orderbean.getAliAccount())){
            orderNo = "XY"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(8999) + 1000);
        }
        orderbean.setOrderNo(orderNo);
        //保存订单
        resultMap = orderService.saveOrder(orderbean,mqtt4PushOrder);
        log.info("家电订单下单成功，订单号:{},品类id:{},街道id:{},小区id:{}",orderNo,category.getParentId(),memberAddress.getStreetId(),communityId);
        //钉钉消息赋值回收公司名称
        Company company = companyService.selectById(companyId);
        if (null != company) {
            //判断是否开启自动派单
            try {
                if ("1".equals(company.getIsOpenOrder())) {
                    orderService.orderSendRecycleByOrderId(Integer.parseInt(resultMap.get("id") + ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            orderbean.setCompanyName(company.getName());
            orderbean.setDingDingUrl(company.getDingDingUrl());
            orderbean.setDingDingSing(company.getDingDingSing());
            try {
                if ("操作成功".equals(resultMap.get("msg") + "")) {
                    if ("true".equals(applicationInit.getIsDd())) {
                        //钉钉通知
                        asyncService.notifyDingDingOrderCreate(orderbean);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //是否有马上回收红包
        if ("Y".equals(orderbean.getIsDelivery())) {
            RedisUtil.SaveOrGetFromRedis saveOrGetFromRedis = new RedisUtil.SaveOrGetFromRedis();
            //保存一个月
            saveOrGetFromRedis.saveInRedis("receive:" + orderNo, UUID.randomUUID().toString(), 30 * 24 * 60 * 60, jedisPool);
        }

        return resultMap;
    }

    /**
     * 根据小区Id和分类Id查询所属的企业
     *
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "order.getCompanyByIds", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object getCompanyByIds(OrderBean orderbean) {
        Member member = MemberUtils.getMember();
        //查询用户的默认地址
        MemberAddress memberAddress = memberAddressService.getMemberAdderssByAliUserId(member.getAliUserId());
        if (memberAddress == null) {
            return "您暂未添加回收地址";
        }
        String companyId = "";
        if ("FIVEKG".equals(orderbean.getType())) {
            //判断该地址是否回收5公斤废纺衣物
            Integer streeCompanyId = companyStreeService.selectStreeCompanyIds(orderbean.getCategoryId(), memberAddress.getStreetId());
            if (null == streeCompanyId) {
                return "该区域暂无回收企业";
            }
            companyId = streeCompanyId + "";
        } else if ("BIGTHING".equals(orderbean.getType())) {
            //判断地址是否有公司回收大件
            Integer streetBigCompanyId = companyStreetBigService.selectStreetBigCompanyIdByCategoryId(orderbean.getCategoryId(), memberAddress.getStreetId());
            if (null == streetBigCompanyId) {
                return "该区域暂无回收企业";
            }
            companyId = streetBigCompanyId + "";
        } else if ("DIGITAL".equals(orderbean.getType())) {
            //根据分类Id和小区id去公海查询相关企业
            String companyId1 = companyStreetApplianceService.selectStreetApplianceCompanyIdByCategoryId(orderbean.getCategoryId(), memberAddress.getStreetId(), memberAddress.getCommunityId());
            if (StringUtils.isBlank(companyId1)) {
                return "该区域暂无回收企业";
            }
            companyId = companyId1.toString();
        } else {
            //根据分类Id和小区Id查询所属企业
            companyId = companyStreetHouseService.selectStreetHouseceCompanyIdByCategoryId(orderbean.getCategoryId(), memberAddress.getStreetId(), memberAddress.getCommunityId());
            if (StringUtils.isBlank(companyId)) {
                //判断该地址是否回收5公斤废纺衣物
                Integer streeCompanyId = companyStreeService.selectStreeCompanyIds(orderbean.getCategoryId(), memberAddress.getStreetId());
                if (null == streeCompanyId) {
                    return "该区域暂无回收企业";
                } else {
                    companyId = streeCompanyId + "";
                }
            }
        }
        Company company1 = companyService.selectById(companyId);
        return company1;
    }

    /**
     * 获取详细价格表并分页 1.有地址(communityId),获取当前公司下的价格 2.没有地址,获取平均价格
     *
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
     *
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "order.XcxSaveOrder", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object XcxSaveOrder(OrderBean orderbean) {
        Member member = MemberUtils.getMember();
        Map<String, Object> resultMap = null;
        if(memberService.checkBlackList(member.getAliUserId(), Order.TitleType.HOUSEHOLD)){
            throw new ApiException("您的账号异常，限制下单，如有疑问请联系客服");
        }
        //查询用户的默认地址
        MemberAddress memberAddress = memberAddressService.getMemberAdderssByAliUserId(member.getAliUserId());
        if (memberAddress == null) {
            return "您暂未添加回收地址";
        }
        //根据分类Id查询父类分类id
        Category category = categoryService.selectById(orderbean.getCategoryId());
        Integer communityId = memberAddress.getCommunityId();
        String companyId = "";
        String level = "";
        String areaId = memberAddress.getAreaId().toString();
        String cityId = memberAddress.getCityId().toString();
        //根据分类Id和小区Id查询所属企业
        companyId = companyStreetHouseService.selectStreetHouseceCompanyIdByCategoryId(category.getParentId(), memberAddress.getStreetId(), memberAddress.getCommunityId());
        if (StringUtils.isBlank(companyId)) {

            System.out.println("六废该区域暂无回收企业" + category.getParentId() + "----" + memberAddress.getStreetId() + "-----" + memberAddress.getCommunityId());
            return "该区域暂无回收企业";
        }
        orderbean.setCompanyId(Integer.parseInt(companyId));
        orderbean.setLevel(level);
        orderbean.setCommunityId(communityId);
        orderbean.setAreaId(Integer.parseInt(areaId));
        orderbean.setStreetId(memberAddress.getStreetId());
        orderbean.setCityId(cityId);
        orderbean.setAddress(memberAddressService.getMemberAddressById(memberAddress.getId().toString(), member.getAliUserId()));
        //随机生成订单号
        String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);
        if (StringUtils.isNotBlank(orderbean.getAliAccount())){
            orderNo = "XY"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(8999) + 1000);
        }
        orderbean.setOrderNo(orderNo);
        resultMap = (Map<String, Object>) orderService.XcxSaveOrder(orderbean, member,mqtt4PushOrder);
        log.info("五废订单下单成功，订单号:{},品类id:{},街道id:{},小区id:{}",orderNo,category.getParentId(),memberAddress.getStreetId(),communityId);
        //钉钉消息赋值回收公司名称
        Company company = companyService.selectById(companyId);
        if (null != company) {
            //判断是否开启自动派单
            try {
                if ("1".equals(company.getIsOpenOrder())) {
                    orderService.orderSendRecycleByOrderId(Integer.parseInt(resultMap.get("id") + ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            orderbean.setCompanyName(company.getName());
            orderbean.setDingDingUrl(company.getDingDingUrl());
            orderbean.setDingDingSing(company.getDingDingSing());
            try {
                if ("操作成功".equals(resultMap.get("msg") + "")) {
                    if ("true".equals(applicationInit.getIsDd())) {
                        //钉钉通知
                        asyncService.notifyDingDingOrderCreate(orderbean);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Map<String, Object> map = new HashMap<>();
        Date date = new Date();
        SimpleDateFormat simp = new SimpleDateFormat("HH");
        String time = simp.format(date);
        if (Integer.parseInt(time) >= 20) {
            map.put("type", 9);
            map.put("msg", "20:00后的订单，次日上午才上门回收哦！");
            map.put("code", resultMap.get("code"));
            map.put("id", resultMap.get("id"));
            map.put("status", resultMap.get("status"));
//			return map;
        } else {
            map.put("type", 9);
            map.put("msg", "操作成功");
            map.put("code", resultMap.get("code"));
            map.put("id", resultMap.get("id"));
            map.put("status", resultMap.get("status"));
        }
        //是否有马上回收红包
        if ("Y".equals(orderbean.getIsDelivery())) {
            RedisUtil.SaveOrGetFromRedis saveOrGetFromRedis = new RedisUtil.SaveOrGetFromRedis();
            //保存一个月
            saveOrGetFromRedis.saveInRedis("receive:" + orderNo, UUID.randomUUID().toString(), 30 * 24 * 60 * 60, jedisPool);
        }

        return map;
    }

    /**
     * 根据以旧换新码查询此码是否存在
     *
     * @return
     */
    @Api(name = "order.isEnterpriseCodeByCode", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object isEnterpriseCodeByCode(EnterpriseCodeBean enterpriseCodeBean) {
        EnterpriseCode enterpriseCode = enterpriseCodeService.selectOne(new EntityWrapper<EnterpriseCode>().eq("code", enterpriseCodeBean.getCode()).eq("del_flag", 0));
        Map<String, Object> map = new HashMap<>();
        if (null != enterpriseCode) {
            if ("0".equals(enterpriseCode.getIsUse())) {
                EnterpriseProduct enterpriseProduct = enterpriseProductService.selectById(enterpriseCode.getProductId());
                if ((enterpriseProduct.getCategoryId() + "").equals(enterpriseCodeBean.getCategoryId())) {
                    map.put("status", "YES");
                    map.put("enterpriseCode", enterpriseCode);
                    return map;
                } else {
                    map.put("status", "NO");
                    map.put("message", "此码不可在该回收类型使用");
                    return map;
                }
            } else {
                map.put("status", "NO");
                map.put("message", "此码已使用");
                return map;
            }
        } else {
            map.put("status", "NO");
            map.put("message", "此码不存在");
            return map;
        }
    }

    /**
     * 保存5公斤废纺衣物的订单
     *
     * @return
     */
    @Api(name = "order.savefiveKgOrder", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object savefiveKgOrder(OrderBean orderbean) throws Exception {
        //获取当前登录的会员
        Member member = MemberUtils.getMember();
        Map<String, Object> resultMap = null;
        Boolean isImprisonMember = false;
        Boolean isImprisonRule = false;
        if(memberService.checkBlackList(member.getAliUserId(), Order.TitleType.FIVEKG)){
            throw new ApiException("您的账号异常，限制下单，如有疑问请联系客服");
        }
        isImprisonMember = imprisonMemberService.isImprisonMember(member.getAliUserId(), "3");
        if (isImprisonMember) {
            resultMap = new HashMap<>();
            resultMap.put("type", 5);
            resultMap.put("msg", "近期下单次数过多，系统检测异常，如有疑问请联系客服");
            resultMap.put("code", 5);
            return resultMap;
        }
        isImprisonRule = imprisonRuleService.isImprisonRuleByAliUserId(member.getAliUserId(), "3");
        if (isImprisonRule) {
            resultMap = new HashMap<>();
            resultMap.put("type", 5);
            resultMap.put("msg", "近期下单次数过多，系统检测异常，如有疑问请联系客服");
            resultMap.put("code", 5);
            return resultMap;
        }
        MemberAddress memberAddress = memberAddressService.getMemberAdderssByAliUserId(member.getAliUserId());
        if (memberAddress == null) {
            return "您暂未添加回收地址";
        }
        //根据分类Id查询父类分类id
        Category category = categoryService.selectById(orderbean.getCategoryId());
        orderbean.setMemberId(member.getId().intValue());
        orderbean.setAliUserId(member.getAliUserId());
        if (null == orderbean.getOrderItemList()) {
            return "请选择详细内容";
        }
        orderbean.setCategoryId(orderbean.getOrderItemList().get(0).getId().intValue());
        orderbean.setCategoryParentIds(orderbean.getOrderItemList().get(0).getParentId());
        Integer communityId = memberAddress.getCommunityId();
        String areaId = memberAddress.getAreaId().toString();
        //判断该地址是否回收5公斤废纺衣物
        Integer streeCompanyId = companyStreeService.selectStreeCompanyIds(45, memberAddress.getStreetId());
        if (streeCompanyId == null) {
            return "该区域暂无回收企业";
        }
        orderbean.setCompanyId(streeCompanyId);
        orderbean.setCommunityId(communityId);
        orderbean.setAreaId(Integer.parseInt(areaId));
        orderbean.setStreetId(memberAddress.getStreetId());
        orderbean.setAddress(memberAddressService.getMemberAddressById(memberAddress.getId().toString(), member.getAliUserId()));
        //随机生成订单号
        String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);
        if (StringUtils.isNotBlank(orderbean.getAliAccount())){
            orderNo = "XYZ"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(8999) + 1000);
        }
        orderbean.setOrderNo(orderNo);
        resultMap = (Map<String, Object>) orderService.savefiveKgOrder(orderbean);
        log.info("五公斤订单下单成功，订单号:{},品类id:{},街道id:{} ",orderNo,45,memberAddress.getStreetId());
        //钉钉消息赋值回收公司名称
        Company company = companyService.selectById(streeCompanyId);
        if (null != company) {
            try {
                //判断是否开启自动派单
                if ("1".equals(company.getIsOpenOrder())) {
                    orderService.tosendfiveKgOrder(Integer.parseInt(resultMap.get("id") + ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            orderbean.setCompanyName(company.getName());
            orderbean.setDingDingUrl(company.getDingDingUrl());
            orderbean.setDingDingSing(company.getDingDingSing());
            try {
                if ("操作成功".equals(resultMap.get("msg") + "")) {
                    if ("true".equals(applicationInit.getIsDd())) {
                        //钉钉通知
                        asyncService.notifyDingDingOrderCreate(orderbean);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //是否有马上回收红包
        if ("Y".equals(orderbean.getIsDelivery())) {
            RedisUtil.SaveOrGetFromRedis saveOrGetFromRedis = new RedisUtil.SaveOrGetFromRedis();
            //保存一个月
            saveOrGetFromRedis.saveInRedis("receive:" + orderNo, UUID.randomUUID().toString(), 30 * 24 * 60 * 60, jedisPool);
        }

        if ("操作成功".equals(resultMap.get("msg") + "")) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", 9);
            map.put("msg", "操作成功");
            map.put("status", "FIVEKG");
            map.put("code", resultMap.get("code"));
            map.put("id", resultMap.get("id"));
            return map;
        } else {
            return resultMap;
        }
    }
    /**
     * 保存小家电的订单
     *
     * @return
     */
    @Api(name = "order.saveSmallAppOrder", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object saveSmallAppOrder(OrderBean orderbean) throws Exception {
        //获取当前登录的会员
        Member member = MemberUtils.getMember();
        Map<String, Object> resultMap = null;
        Boolean isImprisonMember = false;
        Boolean isImprisonRule = false;
        if(memberService.checkBlackList(member.getAliUserId(), Order.TitleType.SMALLDIGITAL)){
            throw new ApiException("您的账号异常，限制下单，如有疑问请联系客服");
        }
        isImprisonMember = imprisonMemberService.isImprisonMember(member.getAliUserId(), "8");
        if (isImprisonMember) {
            resultMap = new HashMap<>();
            resultMap.put("type", 5);
            resultMap.put("msg", "近期下单次数过多，系统检测异常，如有疑问请联系客服");
            resultMap.put("code", 5);
            return resultMap;
        }
        isImprisonRule = imprisonRuleService.isImprisonRuleByAliUserId(member.getAliUserId(), "8");
        if (isImprisonRule) {
            resultMap = new HashMap<>();
            resultMap.put("type", 5);
            resultMap.put("msg", "近期下单次数过多，系统检测异常，如有疑问请联系客服");
            resultMap.put("code", 5);
            return resultMap;
        }
        MemberAddress memberAddress = memberAddressService.getMemberAdderssByAliUserId(member.getAliUserId());
        if (memberAddress == null) {
            return "您暂未添加回收地址";
        }
        //根据分类Id查询父类分类id
//        Category category = categoryService.selectById(orderbean.getCategoryId());
        orderbean.setMemberId(member.getId().intValue());
        orderbean.setAliUserId(member.getAliUserId());
        if (null == orderbean.getOrderItemList()) {
            return "请选择详细内容";
        }
        orderbean.setCategoryId(orderbean.getOrderItemList().get(0).getId().intValue());
        orderbean.setCategoryParentIds(categoryService.getCategoryById(orderbean.getCategoryId()).getParentId().toString());
        Integer communityId = memberAddress.getCommunityId();
        String areaId = memberAddress.getAreaId().toString();
        //判断该地址是否回收5小家电
        Integer streeCompanyId = companyStreetAppSmallService.selectStreetAppSmallCompanyIds(memberAddress.getCityId(), memberAddress.getStreetId());
        if (streeCompanyId == null) {
            return "该区域暂无回收企业";
        }
        orderbean.setCompanyId(streeCompanyId);
        orderbean.setCommunityId(communityId);
        orderbean.setAreaId(Integer.parseInt(areaId));
        orderbean.setStreetId(memberAddress.getStreetId());
        orderbean.setAddress(memberAddressService.getMemberAddressById(memberAddress.getId().toString(), member.getAliUserId()));
        //随机生成订单号
        String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);
        if (StringUtils.isNotBlank(orderbean.getAliAccount())){
            orderNo = "XYZ"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(8999) + 1000);
        }
        orderbean.setOrderNo(orderNo);
        resultMap = (Map<String, Object>) orderService.saveSmallAppOrder(orderbean);
        log.info("小家电订单下单成功，订单号:{},城市id:{},街道id:{} ",orderNo,memberAddress.getCityId(),memberAddress.getStreetId());
        //钉钉消息赋值回收公司名称
        Company company = companyService.selectById(streeCompanyId);
        if (null != company) {
            try {
                //判断是否开启自动派单
                if ("1".equals(company.getIsOpenOrder())) {
                    orderService.tosendfiveKgOrder(Integer.parseInt(resultMap.get("id") + ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            orderbean.setCompanyName(company.getName());
            orderbean.setDingDingUrl(company.getDingDingUrl());
            orderbean.setDingDingSing(company.getDingDingSing());
            try {
                if ("操作成功".equals(resultMap.get("msg") + "")) {
                    if ("true".equals(applicationInit.getIsDd())) {
                        //钉钉通知
                        asyncService.notifyDingDingOrderCreate(orderbean);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //是否有马上回收红包
        if ("Y".equals(orderbean.getIsDelivery())) {
            RedisUtil.SaveOrGetFromRedis saveOrGetFromRedis = new RedisUtil.SaveOrGetFromRedis();
            //保存一个月
            saveOrGetFromRedis.saveInRedis("receive:" + orderNo, UUID.randomUUID().toString(), 30 * 24 * 60 * 60, jedisPool);
        }

        if ("操作成功".equals(resultMap.get("msg") + "")) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", 9);
            map.put("msg", "操作成功");
            map.put("status", "SMALLAPP");
            map.put("code", resultMap.get("code"));
            map.put("id", resultMap.get("id"));
            return map;
        } else {
            return resultMap;
        }
    }

    @Api(name = "order.updateForest", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object updateForest(OrderBean orderbean) {
        //给用户增加蚂蚁能量
        OrderBean orderBean = orderService.myslOrderData(orderbean.getId().toString());
        return "操作成功";
    }

    /**
     * 小程序大家具下单接口
     *
     * @author 王灿
     * @param
     * @return
     * @throws ApiException
     */
    @Api(name = "order.saveBigThingOrder", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object saveBigThingOrder(OrderBean orderbean) throws Exception {
        Member member = MemberUtils.getMember();
        //根据当前登录的会员，获取姓名、绿账号和阿里userId
        orderbean.setMemberId(Integer.parseInt(member.getId().toString()));
        orderbean.setAliUserId(member.getAliUserId());
        //查询用户的默认地址
        Map<String, Object> resultMap = null;
        if(memberService.checkBlackList(member.getAliUserId(), Order.TitleType.BIGTHING)){
            throw new ApiException("您的账号异常，限制下单，如有疑问请联系客服");
        }
        MemberAddress memberAddress = memberAddressService.getMemberAdderssByAliUserId(member.getAliUserId());
        if (memberAddress == null) {
            return "您暂未添加回收地址";
        }
        //根据分类Id查询父类分类id
        Category category = categoryService.selectById(orderbean.getCategoryId());
        Integer communityId = memberAddress.getCommunityId();
        String level = "0";
        String areaId = memberAddress.getAreaId().toString();
        //根据分类Id和小区Id查询所属企业
        Integer streetBigCompanyId = companyStreetBigService.selectStreetBigCompanyIdByCategoryId(category.getParentId(), memberAddress.getStreetId());
        if (streetBigCompanyId == null) {
            return "该区域暂无回收企业";
        }
        orderbean.setCompanyId(streetBigCompanyId);
        orderbean.setLevel(level);
        orderbean.setCommunityId(communityId);
        orderbean.setStreetId(memberAddress.getStreetId());
        orderbean.setAreaId(Integer.parseInt(areaId));
        orderbean.setAddress(memberAddressService.getMemberAddressById(memberAddress.getId().toString(), member.getAliUserId()));
        //随机生成订单号
        String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);
        if (StringUtils.isNotBlank(orderbean.getAliAccount())){
            orderNo = "XY"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(8999) + 1000);
        }
        orderbean.setOrderNo(orderNo);
        //保存订单
        resultMap = orderService.saveBigThingOrder(orderbean,mqtt4PushOrder);
        //钉钉消息赋值回收公司名称
        if (StringUtils.isNoneBlank(streetBigCompanyId + "")) {
            Company company = companyService.selectOne(new EntityWrapper<Company>().eq("id", streetBigCompanyId));
            orderbean.setCompanyName(company.getName());
            orderbean.setDingDingUrl(company.getDingDingUrl());
            orderbean.setDingDingSing(company.getDingDingSing());
            //判断是否开启自动派单
            try {
                if ("1".equals(company.getIsOpenOrder())) {
                    orderService.orderSendRecycleByOrderId(Integer.parseInt(resultMap.get("id") + ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new ApiException("回收公司异常！！！！！");
        }
        if ("操作成功".equals(resultMap.get("msg") + "")) {
            if ("true".equals(applicationInit.getIsDd())) {
                //钉钉通知
                asyncService.notifyDingDingOrderCreate(orderbean);
            }
        }
        //是否有马上回收红包
        if ("Y".equals(orderbean.getIsDelivery())) {
            RedisUtil.SaveOrGetFromRedis saveOrGetFromRedis = new RedisUtil.SaveOrGetFromRedis();
            //保存一个月
            saveOrGetFromRedis.saveInRedis("receive:" + orderNo, UUID.randomUUID().toString(), 30 * 24 * 60 * 60, jedisPool);
        }
        return resultMap;
    }

    /**
     * 小程序第二版订单详情接口
     *
     * @author 王灿
     * @param
     * @return
     * @throws ApiException
     */
    @Api(name = "order.getNewOrderDetail", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object getNewOrderDetail(OrderBean orderbean) throws ApiException {
        return orderService.getNewOrderDetail(orderbean.getId());
    }
    /**
     * @Description 定时定点订单详情
     * @Author yangwenhui
     * @Date 2020-05-28 10:03
     */
    @Api(name = "order.dsddOrderDetail", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Map<String, Object> dsddOrderDetail(OrderBean orderBean) {
        Map<String, Object> map = orderService.dsddOrderDetail(orderBean.getId());
        return map;
    }

    /**
     *
     * @author 王灿
     * @param
     * @return
     * @throws ApiException
     */
    @Api(name = "order.getCollectDetail", version = "1.0")
    @AuthIgnore
    @SignIgnore
    @Cacheable(value = "getCollectDetail", key = "#root.methodName", sync = true)
    public Object getCollectDetail() throws ApiException {
            //注册总用户数量
            long memberCount = memberService.getMemberCount();
            int OrderCount = orderService.selectCount(new EntityWrapper<Order>());
            Long greenCount = (long) 0;
            List<Map<String, Object>> maps = orderService.selectHouseAmount();
            for (Map<String, Object> map : maps) {
                Integer green = 0;
                if ("废塑料".equals(map.get("categoryName"))) {
                    green = 144;
                } else if ("废旧衣物".equals(map.get("categoryName"))) {
                    green = 78;
                } else if ("废纺衣物".equals(map.get("categoryName"))) {
                    green = 78;
                } else if ("废衣服".equals(map.get("categoryName"))) {
                    green = 78;
                } else if ("废纸".equals(map.get("categoryName"))) {
                    green = 100;
                } else if ("废金属".equals(map.get("categoryName"))) {
                    green = 13;
                }
                greenCount += new Double((double) map.get("amount") * green).longValue();
            };
            int counts = orderService.selectCount(new EntityWrapper<Order>().eq("status_", "3").eq("title", "1"));
            int count1 = orderService.selectCount(new EntityWrapper<Order>().eq("status_", "3").eq("title", "1").eq("category_id", "23"));
            int count2 = orderService.selectCount(new EntityWrapper<Order>().eq("status_", "3").eq("title", "1").eq("category_id", "24"));
            greenCount += Long.valueOf(count1 * 987) + Long.valueOf(counts - count1 - count2) * 9763;
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("memberCount", memberCount);
            resultMap.put("OrderCount", OrderCount);
            resultMap.put("greenCount", greenCount);
            return resultMap;
    }

    /**
     * 用户领取现金红包(领取前订单是否完成，该随机数是否准确，是否已被领取)
     *
     * @author sgmark@aliyun.com （抄自答答答）
     * @date 2019/8/12 0012
     * @param orderbean(包含领奖随机数 + 订单编号 )
     * @return
     */
    @Api(name = "order.receiving.money", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Map<String, Object> receivingMoney(OrderBean orderbean) {
        orderbean.setAliUserId(MemberUtils.getMember().getAliUserId());
        return orderService.receivingMoney(orderbean);
    }


    @Autowired
    IotOrderService iotOrderService;



    /**
     * IOT4分类订单列表
     * @param
     * @return
     */
    @Api(name = "order.iot4.orderList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object iot4OrderList(OrderBean orderBean) {
        PageBean pageBean = orderBean.getPagebean();
        //获取当前登录的会员信息
        Member member = MemberUtils.getMember();
        //根据会员ID回去订单列表
        Map<String, Object> map = iotOrderService.findOrderListByUserId(member.getAliUserId(),   pageBean.getPageNumber(), pageBean.getPageSize());
        return map;
    }

    /**
     * iot4分类订单明细
     * @param orderBean
     * @return
     */
    @Api(name = "order.iot4.orderDetail", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object iot4OrderDetail(OrderBean orderBean) {
       return  iotOrderService.getOrderDetail(orderBean.getOrderNo());

    }


    /**
     *  微信回收订单支付
     *
     * @param
     * @return
     */
    @Api(name = "wechat.collect.prePay", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object collectPrePay(OrderPayParam orderPayParam) {
        Order order = orderService.selectOrder(orderPayParam.getOrderId());
        if (order == null || !order.getTitle().equals(Order.TitleType.BIGTHING)
                || order.getStatus().equals(Order.OrderType.COMPLETE) ||  order.getStatus().equals(Order.OrderType.CANCEL) ) {
            throw new ApiException("订单错误");
        }

        WxPayMpOrderResult wxPayMpOrderResult = (WxPayMpOrderResult) wxPayService.prePay(getUserFromToken().getAliUserId(),
                order.getOrderNo(),
                WXConst.FROM_JS_API, order.getAchPrice(), "大件订单", PrepayOrder.COLLECT);
        return wxPayMpOrderResult;
    }



    public static Member getUserFromToken() throws ApiException {
        Subject subject = ApiContext.getSubject();
        if (subject == null) {
            return null;
        }
        Member member = (Member) subject.getUser();
        if (member == null) {
            throw new ApiException("未获取到数据");
        }
        return member;
    }


    @Autowired
    CompanyStreetElectroMobileService companyStreetElectroMobileService;

    /**
     * 电瓶车下单接口
     * @param orderbean
     * @return
     * @throws ApiException
     */
    @Api(name = "order.electroMobile.create", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object createElectroMobileOrder(OrderBean orderbean) throws ApiException {
        Member member = MemberUtils.getMember();
        Map<String, Object> resultMap = null;
        Boolean isImprisonMember = false;
        Boolean isImprisonRule = false;
        if(memberService.checkBlackList(member.getAliUserId(), Order.TitleType.ELECTROMOBILE)){
            throw new ApiException("您的账号异常，限制下单，如有疑问请联系客服");
        }
        isImprisonMember = imprisonMemberService.isImprisonMember(member.getAliUserId(), "9");
        if (isImprisonMember) {
            resultMap = new HashMap<>();
            resultMap.put("type", 5);
            resultMap.put("msg", "近期下单次数过多，系统检测异常，如有疑问请联系客服");
            resultMap.put("code", 5);
            return resultMap;
        }
        isImprisonRule = imprisonRuleService.isImprisonRuleByAliUserId(member.getAliUserId(), "9");
        if (isImprisonRule) {
            resultMap = new HashMap<>();
            resultMap.put("type", 5);
            resultMap.put("msg", "近期下单次数过多，系统检测异常，如有疑问请联系客服");
            resultMap.put("code", 5);
            return resultMap;
        }
        //根据当前登录的会员，获取姓名、绿账号和阿里userId
        orderbean.setMemberId(Integer.parseInt(member.getId().toString()));
        orderbean.setGreenCode(member.getGreenCode());
        orderbean.setAliUserId(member.getAliUserId());
        //查询用户的默认地址
        MemberAddress memberAddress = memberAddressService.getMemberAdderssByAliUserId(member.getAliUserId());
        if (memberAddress == null) {
            return "您暂未添加回收地址";
        }
        //根据分类Id查询父类分类id
        Category category = categoryService.selectById(orderbean.getCategoryId());
        Integer communityId = memberAddress.getCommunityId();
        String level = "1";
        String areaId = memberAddress.getAreaId().toString();
        //根据分类Id、街道id和小区Id查询所属企业
        Integer companyId = companyStreetElectroMobileService.selectCompanyByCategoryId(category.getParentId(), memberAddress.getStreetId());
        if (null == companyId) {
            return "该区域暂无回收企业";
        }
        orderbean.setAddress(memberAddressService.getMemberAddressById(memberAddress.getId().toString(), member.getAliUserId()));
        orderbean.setCompanyId(companyId);
        orderbean.setLevel(level);
        orderbean.setCommunityId(communityId);
        orderbean.setStreetId(memberAddress.getStreetId());
        //随机生成订单号
        String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);
        if (StringUtils.isNotBlank(orderbean.getAliAccount())){
            orderNo = "XY"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(8999) + 1000);
        }
        orderbean.setOrderNo(orderNo);
        //保存订单
        resultMap = orderService.saveElectroMobileOrder(orderbean);
        log.info("电瓶车订单下单成功，订单号:{},品类id:{},街道id:{},小区id:{}",orderNo,category.getParentId(),memberAddress.getStreetId(),communityId);
        //钉钉消息赋值回收公司名称
        Company company = companyService.selectById(companyId);
        if (null != company) {
            //判断是否开启自动派单
            try {
                if ("1".equals(company.getIsOpenOrder())) {
                    orderService.orderSendRecycleByOrderId(Integer.parseInt(resultMap.get("id") + ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            orderbean.setCompanyName(company.getName());
            orderbean.setDingDingUrl(company.getDingDingUrl());
            orderbean.setDingDingSing(company.getDingDingSing());
            try {
                if ("操作成功".equals(resultMap.get("msg") + "")) {
                    if ("true".equals(applicationInit.getIsDd())) {
                        //钉钉通知
                        asyncService.notifyDingDingOrderCreate(orderbean);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //是否有马上回收红包
        if ("Y".equals(orderbean.getIsDelivery())) {
            RedisUtil.SaveOrGetFromRedis saveOrGetFromRedis = new RedisUtil.SaveOrGetFromRedis();
            //保存一个月
            saveOrGetFromRedis.saveInRedis("receive:" + orderNo, UUID.randomUUID().toString(), 30 * 24 * 60 * 60, jedisPool);
        }

        return resultMap;
    }

}
