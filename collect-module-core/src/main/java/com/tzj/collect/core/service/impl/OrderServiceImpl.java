package com.tzj.collect.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.domain.*;
import com.alipay.api.response.*;
import com.alipay.api.domain.AntMerchantExpandTradeorderSyncModel;
import com.alipay.api.domain.ItemOrder;
import com.alipay.api.domain.OrderExtInfo;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AntMerchantExpandTradeorderSyncResponse;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.commom.redis.RedisUtil;
import com.tzj.collect.common.amap.AmapResult;
import com.tzj.collect.common.amap.AmapUtil;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.common.constant.MiniTemplatemessageUtil;
import com.tzj.collect.common.notify.DingTalkNotify;
import com.tzj.collect.common.push.PushUtils;
import com.tzj.collect.common.util.BusinessUtils;
import com.tzj.collect.common.utils.ToolUtils;
import com.tzj.collect.core.mapper.OrderMapper;
import com.tzj.collect.core.param.admin.LjAdminBean;
import com.tzj.collect.core.param.admin.VoucherBean;
import com.tzj.collect.core.param.ali.*;
import com.tzj.collect.core.param.app.ScoreAppBean;
import com.tzj.collect.core.param.app.TimeBean;
import com.tzj.collect.core.param.business.BOrderBean;
import com.tzj.collect.core.param.business.CompanyBean;
import com.tzj.collect.core.param.iot.IotParamBean;
import com.tzj.collect.core.param.xianyu.QiMemOrder;
import com.tzj.collect.core.result.ali.ComCatePrice;
import com.tzj.collect.core.result.app.AppOrderResult;
import com.tzj.collect.core.result.app.AppScoreResult;
import com.tzj.collect.core.result.app.AttrItem;
import com.tzj.collect.core.result.app.EvaluationResult;
import com.tzj.collect.core.result.business.ApiUtils;
import com.tzj.collect.core.result.business.CancelResult;
import com.tzj.collect.core.result.third.ThirdOrderResult;
import com.tzj.collect.core.service.*;
import com.tzj.collect.core.thread.NewThreadPoorExcutor;
import com.tzj.collect.core.thread.sendGreenOrderThread;
import com.tzj.collect.entity.*;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Category.CategoryType;
import com.tzj.collect.entity.CategoryAttrOption;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.Order.OrderType;
import com.tzj.collect.entity.OrderItem;
import com.tzj.module.common.utils.DateUtils;
import com.tzj.module.easyopen.exception.ApiException;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.tzj.collect.entity.Payment.STATUS_PAYED;
import static com.tzj.collect.entity.Payment.STATUS_TRANSFER;

/**
 * 订单ServiceImpl
 *
 * @Author 王灿
 *
 */
@Service
@Transactional
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
    private MapService mapService;
    @Autowired
    private CompanyStreetHouseService companyStreetHouseService;
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
    @Autowired
    private CompanyEquipmentService companyEquipmentService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AnsycMyslService ansycMyslService;
    @Autowired
    private SendRocketmqMessageService sendRocketmqMessageService;
    @Autowired
    private CompanyCategoryCityService companyCategoryCityService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private CompanyCategoryCityLocaleService companyCategoryCityLocaleService;
    @Autowired
    private OrderCancleExamineService orderCancleExamineService;
    @Autowired
    private OrderComplaintService orderComplaintService;
    @Autowired
    private VoucherMemberService voucherMemberService;
    @Resource
    private LineQrCodeOrderService lineQrCodeOrderService;
    @Autowired
    private XyCategoryOrderService xyCategoryOrderService;
    @Autowired
    private QiMemService qiMemService;
    @Autowired
    private DsddRecyclePositionService dsddRecyclePositionService;
    @Autowired
    private DsddPaymentService dsddPaymentService;
    @Autowired
    private OrderOperateService orderOperateService;
    @Resource
    private JedisPool jedisPool;

    protected final static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    /**
     * 获取会员的订单列表 分页
     *
     * @param num : 第几页
     * @param size : 共多少条
     * @return
     * @author 王灿
     */
    @Override
    public Map<String, Object> getOrderlist(String aliUserId, Integer status, int num, int size) {
        EntityWrapper<Order> wrapper = new EntityWrapper<Order>();
        wrapper.eq("ali_user_id", aliUserId);
        wrapper.eq("del_flag", "0");
        wrapper.orderBy("create_date", false);
        wrapper.eq("status_", status);
        int i = this.selectCount(wrapper);
        Map<String, Object> map = new HashMap<String, Object>();
        List<Order> listOrder = orderMapper.getOrderlist(aliUserId, status, (num - 1) * size, size);
        listOrder = this.createName4Ali(listOrder);
        map.put("pageNum", num);
        map.put("count", i);
        map.put("listOrder", listOrder);
        return map;
    }

    /**
     * 下单接口 该接口参数复杂 待定中...
     *
     * @param orderbean : 订单参数实体
     * @return long
     */
    @Transactional
    @Override
    public Map<String, Object> saveOrder(OrderBean orderbean,MqttClient mqtt4PushOrder) {
        //查询价格
        BigDecimal price = categoryService.getPricesAll(orderbean.getAliUserId(), orderbean.getCategoryId(), "DIGITAL", orderbean.getOrderItemBean().getCategoryAttrOppIds());
        Map<String, Object> resultMap = new HashMap<>();
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
            order.setStreetId(orderbean.getStreetId());
            order.setCommunityId(orderbean.getCommunityId());
            order.setAddress(orderbean.getAddress());
            order.setFullAddress(orderbean.getFullAddress());
            order.setTel(orderbean.getTel());
            order.setLinkMan(orderbean.getLinkMan());
            order.setCategoryId(orderbean.getCategoryId());
            order.setCategoryParentIds(orderbean.getCategoryParentIds());
            if(StringUtils.isNotBlank(orderbean.getIsMysl())){
                order.setIsMysl(orderbean.getIsMysl());
            }else{
                order.setIsMysl("1");
            }
            order.setPrice(price);
            order.setUnit(orderbean.getUnit());
            order.setQty(orderbean.getQty());
            order.setLevel(orderbean.getLevel());
            if (StringUtils.isNotBlank(orderbean.getAliAccount())){
                order.setOrderFrom("2");
                order.setAliAccount(orderbean.getAliAccount());
            }
            order.setGreenCode(orderbean.getGreenCode());
            order.setAliUserId(orderbean.getAliUserId());
            order.setRemarks(orderbean.getRemarks());
            if (Order.TitleType.DIGITAL.name().equals(orderbean.getTitle())) {
                order.setTitle(Order.TitleType.DIGITAL);
            } else if (Order.TitleType.HOUSEHOLD.name().equals(orderbean.getTitle())) {
                order.setTitle(Order.TitleType.HOUSEHOLD);
            } else {
                order.setTitle(Order.TitleType.DEFUALT);
            }
            if ("1".equals(orderbean.getIsCash())) {
                order.setIsCash(orderbean.getIsCash());
            } else {
                order.setIsCash("0");
            }
            order.setEnterpriseCode(orderbean.getEnterpriseCode());
            order.setFormId(orderbean.getFormId());
            order.setPriceT(orderbean.getPrice());
            flag = this.insert(order);
            //将券码跟订单进行绑定
            if (StringUtils.isNoneBlank(orderbean.getVoucherId())) {
                boolean bool = voucherMemberService.updateVoucherUseing(order.getId(), order.getOrderNo(), order.getAliUserId(), Long.parseLong(orderbean.getVoucherId()));
                if (!bool) {
                    throw new ApiException("该券不存在或不是可使用状态");
                }
            }
            //判断是否有以旧换新券码
            if (!StringUtils.isBlank(orderbean.getEnterpriseCode())) {
                enterpriseCode = enterpriseCodeService.selectOne(new EntityWrapper<EnterpriseCode>().eq("code", orderbean.getEnterpriseCode()).eq("del_flag", 0).eq("is_use", 0));
                //判断券码是否存在并且未使用
                if (null != enterpriseCode) {
                    enterpriseCode.setIsUse("1");
                    enterpriseCode.setOrderId(order.getId().intValue());
                    enterpriseCodeService.updateById(enterpriseCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("msg", "FAIL");
            return resultMap;
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
        //保存分享码
        if (!StringUtils.isEmpty(orderbean.getShareCode())){
            lineQrCodeOrderService.insertQrCodeOrder(order.getOrderNo(), orderbean.getShareCode());
        }
        //储存订单的日志
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(Integer.parseInt(orderId + ""));
        orderLog.setOpStatusAfter("INIT");
        orderLog.setOp("待接单");
        flag = orderLogService.insert(orderLog);
        try {
            if ((Order.TitleType.BIGTHING + "").equals(order.getTitle() + "")) {
                asyncService.sendOpenAppMini(order.getAliUserId(), order.getFormId(), MiniTemplatemessageUtil.orderTemplateId, MiniTemplatemessageUtil.page, order.getOrderNo(), "平台已受理", "大件回收");
            } else if ((Order.TitleType.DIGITAL + "").equals(order.getTitle() + "")) {
                asyncService.sendOpenAppMini(order.getAliUserId(), order.getFormId(), MiniTemplatemessageUtil.orderTemplateId, MiniTemplatemessageUtil.page, order.getOrderNo(), "平台已受理", "家电回收");
            } else {
                asyncService.sendOpenAppMini(order.getAliUserId(), order.getFormId(), MiniTemplatemessageUtil.orderTemplateId, MiniTemplatemessageUtil.page, order.getOrderNo(), "平台已受理", "生活垃圾");
            }
            //推送订单消息
            asyncService.pushOrder(order,mqtt4PushOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (flag) {
            resultMap.put("msg", "操作成功");
            resultMap.put("code", 0);
            resultMap.put("id", orderId);
            resultMap.put("status", order.getTitle());
            return resultMap;
        }
        resultMap.put("msg", "操作失败");
        return resultMap;
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

        if (!"9000".equals(orderbean.getResultStatus()) && !StringUtils.isBlank(orderbean.getResultStatus())) {
            //很具订单号查询订单是否交易成功
            Payment payment = paymentService.selectOne(new EntityWrapper<Payment>().eq("order_sn", order.getOrderNo()));
            if (payment != null) {
                if (StringUtils.isBlank(payment.getTradeNo())) {
                    return flag;
                }
                AlipayTradeQueryResponse aliPayment = paymentService.getAliPayment(payment.getTradeNo());
                if (!"TRADE_SUCCESS".equals(aliPayment.getTradeStatus())) {
                    return flag;
                }
            }
        }
        //删除上次储存的记录
        try {
            //保存orderItem
            if ("0".equals(order.getIsItemAch())) {
                orderItemAchService.delete(new EntityWrapper<OrderItemAch>().eq("order_id", order.getId()));
            }
            orderPicAchService.delete(new EntityWrapper<OrderPicAch>().eq("order_id", order.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        order.setAchRemarks(orderbean.getAchRemarks());
        order.setSignUrl(orderbean.getSignUrl());
        //新需求：回收人员app生活垃圾板块 增加选项-用户是否平铺整理 1-否 2-是
        if ("2".equals(order.getTitle().getValue().toString())){
            order.setCleanUp(orderbean.getCleanUp());
        }

        orderbean.setIsCash(order.getIsCash());
        Area city = areaService.selectById(order.getAreaId());
        orderbean.setCityId(city.getParentId().toString());
        //保存orderItem
        if ("0".equals(order.getIsItemAch())) {
            double amount = this.saveOrderItemAch(orderbean, order);
            order.setGreenCount(amount);
        }
        flag = this.updateById(order);
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
        return flag;
    }
    @Transactional
    @Override
    public Object getCommissionsPriceByOrderId(OrderBean orderbean){
        Order order = this.selectById(orderbean.getId());
        if ("1".equals(order.getTitle().getValue().toString())){
            CompanyCategory companyCategory = companyCategoryService.selectCompanyCategory(order.getCompanyId().toString(), order.getCategoryId().toString());
            if(companyCategory!=null) {
               logger.info("订单编号:"+order.getOrderNo()+", 设置佣金系数： adminCommissions: "+companyCategory.getAdminCommissions()+", companyCommissions:"+companyCategory.getCompanyCommissions());
            }
            Category category = categoryService.selectById(order.getCategoryId());
            order.setGreenCount(category.getGreenCount().doubleValue());
            order.setAdminCommissions(companyCategory==null?BigDecimal.ZERO:companyCategory.getAdminCommissions());
            order.setCompanyCommissions(companyCategory==null?BigDecimal.ZERO:companyCategory.getCompanyCommissions());
            order.setCommissionsPrice(companyCategory==null?BigDecimal.ZERO:companyCategory.getAdminCommissions().setScale(2, BigDecimal.ROUND_DOWN));
            order.setBackCommissionsPrice(companyCategory==null?BigDecimal.ZERO:companyCategory.getCompanyCommissions().setScale(2, BigDecimal.ROUND_DOWN));
        }if ("2".equals(order.getTitle().getValue().toString())){
            List<OrderItemAch> orderItemAches = orderItemAchService.selectByOrderId(order.getId().intValue());
            final BigDecimal[] commissionsPrice = {BigDecimal.ZERO};
            final BigDecimal[] backCommissionsPrice = {BigDecimal.ZERO};
            final Double[] greenCount = {0.00};
            orderItemAches.stream().forEach(orderItemAch -> {
                CompanyCategory companyCategory = companyCategoryService.selectCompanyCategory(order.getCompanyId().toString(), orderItemAch.getCategoryId().toString());
                Category category = categoryService.selectById(orderItemAch.getCategoryId());
                if(companyCategory!=null) {
                    logger.info("订单编号:"+order.getOrderNo()+", 设置佣金系数： adminCommissions: "+companyCategory.getAdminCommissions()+", companyCommissions:"+companyCategory.getCompanyCommissions());
                }
                if ("0".equals(order.getIsCash())){
                    greenCount[0] += category.getGreenCount()*orderItemAch.getAmount()*orderItemAch.getCleanUp();
                    commissionsPrice[0] = commissionsPrice[0].add(companyCategory==null?BigDecimal.ZERO:companyCategory.getAdminCommissions().multiply(new BigDecimal(orderItemAch.getAmount())));
                    backCommissionsPrice[0] = backCommissionsPrice[0].add(companyCategory==null?BigDecimal.ZERO:companyCategory.getCompanyCommissions().multiply(new BigDecimal(orderItemAch.getAmount())));
                }else {
                    greenCount[0] += category.getFreeGreenCount()*orderItemAch.getAmount()*orderItemAch.getCleanUp();
                    commissionsPrice[0] = commissionsPrice[0].add(companyCategory==null?BigDecimal.ZERO:companyCategory.getFreeCommissions().multiply(new BigDecimal(orderItemAch.getAmount())));
                    backCommissionsPrice[0] = backCommissionsPrice[0].add(companyCategory==null?BigDecimal.ZERO:companyCategory.getCompanyCommissions().multiply(new BigDecimal(orderItemAch.getAmount())));
                }
                orderItemAch.setAdminCommissions(companyCategory==null?BigDecimal.ZERO:companyCategory.getAdminCommissions());
                orderItemAch.setFreeCommissions(companyCategory==null?BigDecimal.ZERO:companyCategory.getFreeCommissions());
                orderItemAch.setCompanyCommissions(companyCategory==null?BigDecimal.ZERO:companyCategory.getCompanyCommissions());
                orderItemAchService.updateById(orderItemAch);
            });
            DecimalFormat df=new DecimalFormat(".##");
            order.setGreenCount(Double.parseDouble(df.format(greenCount[0])));
            order.setBackCommissionsPrice(backCommissionsPrice[0]);
            order.setCommissionsPrice(commissionsPrice[0].setScale(2, BigDecimal.ROUND_DOWN));
        }
        boolean b = this.updateById(order);
        if(!b){
            DingTalkNotify.sendMessage("订单编号:"+order.getOrderNo()+", 设置佣金错误", "https://oapi.dingtalk.com/robot/send?access_token=2f810a6a9488ce73a9936074ae1331e1df15f326c560d1945fae3b7fcf1565d8&&access=recy");
            throw new ApiException("佣金设置有误，请重试");
        }

        return order.getCommissionsPrice().setScale(2, BigDecimal.ROUND_DOWN);
    }

    @Transactional
    @Override
    public Object getPriceByOrderId(OrderBean orderbean,MqttClient mqtt4PushOrder){
        Order order = this.selectById(orderbean.getId());
        order.setAchPrice(new BigDecimal(orderbean.getAchPrice()));
        //如果设置的佣金为0 判断是否是设置佣金错了，重新设置佣金
        if(order.getCommissionsPrice().compareTo(BigDecimal.ZERO)==0) {
            if ("1".equals(order.getTitle().getValue().toString())) {
                CompanyCategory companyCategory = companyCategoryService.selectCompanyCategory(order.getCompanyId().toString(), order.getCategoryId().toString());
                if (companyCategory != null) {
                    logger.info("订单编号:" + order.getOrderNo() + ", 设置佣金系数： adminCommissions: " + companyCategory.getAdminCommissions() + ", companyCommissions:" + companyCategory.getCompanyCommissions());
                }
                Category category = categoryService.selectById(order.getCategoryId());
                order.setGreenCount(category.getGreenCount().doubleValue());
                order.setAdminCommissions(companyCategory == null ? BigDecimal.ZERO : companyCategory.getAdminCommissions());
                order.setCompanyCommissions(companyCategory == null ? BigDecimal.ZERO : companyCategory.getCompanyCommissions());
                order.setCommissionsPrice(companyCategory == null ? BigDecimal.ZERO : companyCategory.getAdminCommissions().setScale(2, BigDecimal.ROUND_DOWN));
                order.setBackCommissionsPrice(companyCategory == null ? BigDecimal.ZERO : companyCategory.getCompanyCommissions().setScale(2, BigDecimal.ROUND_DOWN));
            }
            if ("2".equals(order.getTitle().getValue().toString())) {
                List<OrderItemAch> orderItemAches = orderItemAchService.selectByOrderId(order.getId().intValue());
                final BigDecimal[] commissionsPrice = {BigDecimal.ZERO};
                final BigDecimal[] backCommissionsPrice = {BigDecimal.ZERO};
                final Double[] greenCount = {0.00};
                orderItemAches.stream().forEach(orderItemAch -> {
                    CompanyCategory companyCategory = companyCategoryService.selectCompanyCategory(order.getCompanyId().toString(), orderItemAch.getCategoryId().toString());
                    Category category = categoryService.selectById(orderItemAch.getCategoryId());
                    if (companyCategory != null) {
                        logger.info("订单编号:" + order.getOrderNo() + ", 设置佣金系数： adminCommissions: " + companyCategory.getAdminCommissions() + ", companyCommissions:" + companyCategory.getCompanyCommissions());
                    }
                    if ("0".equals(order.getIsCash())) {
                        greenCount[0] += category.getGreenCount() * orderItemAch.getAmount() * orderItemAch.getCleanUp();
                        commissionsPrice[0] = commissionsPrice[0].add(companyCategory == null ? BigDecimal.ZERO : companyCategory.getAdminCommissions().multiply(new BigDecimal(orderItemAch.getAmount())));
                        backCommissionsPrice[0] = backCommissionsPrice[0].add(companyCategory == null ? BigDecimal.ZERO : companyCategory.getCompanyCommissions().multiply(new BigDecimal(orderItemAch.getAmount())));
                    } else {
                        greenCount[0] += category.getFreeGreenCount() * orderItemAch.getAmount() * orderItemAch.getCleanUp();
                        commissionsPrice[0] = commissionsPrice[0].add(companyCategory == null ? BigDecimal.ZERO : companyCategory.getFreeCommissions().multiply(new BigDecimal(orderItemAch.getAmount())));
                        backCommissionsPrice[0] = backCommissionsPrice[0].add(companyCategory == null ? BigDecimal.ZERO : companyCategory.getCompanyCommissions().multiply(new BigDecimal(orderItemAch.getAmount())));
                    }
                    orderItemAch.setAdminCommissions(companyCategory == null ? BigDecimal.ZERO : companyCategory.getAdminCommissions());
                    orderItemAch.setFreeCommissions(companyCategory == null ? BigDecimal.ZERO : companyCategory.getFreeCommissions());
                    orderItemAch.setCompanyCommissions(companyCategory == null ? BigDecimal.ZERO : companyCategory.getCompanyCommissions());
                    orderItemAchService.updateById(orderItemAch);
                });
                DecimalFormat df = new DecimalFormat(".##");
                order.setGreenCount(Double.parseDouble(df.format(greenCount[0])));
                order.setBackCommissionsPrice(backCommissionsPrice[0]);
                order.setCommissionsPrice(commissionsPrice[0].setScale(2, BigDecimal.ROUND_DOWN));
            }
        }
        boolean b = this.updateById(order);
        //如果设置佣金或价格失效 报错
        if(!b){
            DingTalkNotify.sendMessage("订单编号:"+order.getOrderNo()+", 设置成交价或佣金错误！ ", "https://oapi.dingtalk.com/robot/send?access_token=2f810a6a9488ce73a9936074ae1331e1df15f326c560d1945fae3b7fcf1565d8&&access=recy");
            throw new ApiException("佣金设置有误，请重试");
        }
        if (order.getAchPrice().add(order.getCommissionsPrice()).compareTo(BigDecimal.ZERO)==0){
            //修改订单状态
            orderbean.setAmount(order.getGreenCount());
            orderbean.setAchPrice(order.getAchPrice().toString());
            orderbean.setStatus("3");
            this.modifyOrderSta(orderbean,mqtt4PushOrder);
            //如果是咸鱼的单子，就通知咸鱼订单完成
            if (StringUtils.isNotBlank(order.getTaobaoNo())){
                QiMemOrder qiMemOrder = new QiMemOrder();
                qiMemOrder.setConfirmFee("0");
                qiMemOrder.setBizOrderId(order.getOrderNo());
                qiMemOrder.setOrderStatus(QiMemOrder.QI_MEM_ORDER_SUCCESS);
                Double amount = orderMapper.getAmountByOrderId(order.getId());
                qiMemOrder.setQuantity(amount.toString());
                boolean bool = qiMemService.updateQiMemOrder(qiMemOrder);
            }
            if ("1".equals(order.getIsMysl())) {
                //给用户增加蚂蚁能量
                orderService.myslOrderData(order.getId().toString());
            }
            try {
                if (order.getAddress().startsWith("上海市") && (Order.TitleType.HOUSEHOLD + "").equals(order.getTitle() + "")) {
                    NewThreadPoorExcutor.getThreadPoor().execute(new Thread(new sendGreenOrderThread(orderService, areaService, orderItemAchService, order.getId().intValue())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return order.getAchPrice().add(order.getCommissionsPrice()).setScale(2, BigDecimal.ROUND_DOWN);
    }

    @Override
    public Object editByAdminOrder(OrderBean orderBean) {
        Map<String, Object> resultMap = new HashMap<>();
        Order order = this.selectById(orderBean.getId());
        List<OrderItemAch> orderItemAchList = orderItemAchService.selectByOrderId(orderBean.getId());
        resultMap.put("order", order);
        resultMap.put("orderItemAchList", orderItemAchList);
        return resultMap;
    }

    public boolean updateOrderAchItem(OrderBean orderBean) {
        boolean flag = false;
        Order order = this.selectById(orderBean.getId());
        //删除上次储存的记录
        try {
            orderItemAchService.delete(new EntityWrapper<OrderItemAch>().eq("order_id", order.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //保存orderItem
        orderBean.setTitle("HOUSEHOLD");
        orderBean.setIsCash(order.getIsCash());
        Area city = areaService.selectById(order.getAreaId());
        orderBean.setCityId(city.getParentId().toString());
        orderBean.setCompanyId(order.getCompanyId());
        double amount = this.saveOrderItemAch(orderBean, null);
        order.setAchPrice(new BigDecimal(orderBean.getAchPrice()));
        order.setGreenCount(amount);
        order.setIsItemAch("1");
        flag = this.updateById(order);

        return flag;
    }

    /**
     * @param @return List<Order>
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

	private double saveOrderItemAch(OrderBean orderbean, Order order) {
		//是否是免费的 0 不是 1是
		String isCash = orderbean.getIsCash();
		//得到用户的垃圾总量
		double amount = 0.0;
		OrderItemAch orderItemAch = null;
		List<OrderItemBean> idAmount = null;
		if (CategoryType.HOUSEHOLD.name().equals(orderbean.getTitle())) {
			List<IdAmountListBean> listBean = orderbean.getIdAndListList();
				for (IdAmountListBean idAmountListBean : listBean) {
                    orderItemAch = new OrderItemAch();
                    orderItemAch.setOrderId(Integer.parseInt(orderbean.getId() + ""));
                    orderItemAch.setParentId(idAmountListBean.getCategoryParentId());
                    orderItemAch.setParentName(idAmountListBean.getCategoryParentName());
					idAmount = idAmountListBean.getIdAndAmount();
					for (OrderItemBean item : idAmount) {
                        orderItemAch.setCategoryId(Integer.parseInt(item.getCategoryId() + ""));
                        orderItemAch.setCategoryName(item.getCategoryName());
                        orderItemAch.setAmount(item.getAmount());
                        //新需求：增加评价 用户是否平铺整理 1-否 2-是
                        orderItemAch.setCleanUp(orderbean.getCleanUp());
						System.out.println(item.getCategoryName() + " 重量: " + item.getAmount());
                        OrderItem orderItem = orderItemService.selectOne(new EntityWrapper<OrderItem>().eq("category_id", item.getCategoryId()).eq("order_id", order.getId()));
                        CompanyCategoryCity companyCategoryCity = companyCategoryCityService.selectOne(new EntityWrapper<CompanyCategoryCity>().eq("company_id", orderbean.getCompanyId()).eq("category_id", item.getCategoryId()).eq("city_id", orderbean.getCityId()));
						CompanyCategory companyCategory = comCatePriceService.selectOne(new EntityWrapper<CompanyCategory>().eq("company_id", orderbean.getCompanyId()).eq("category_id", item.getCategoryId()));
                        Category categorys = categoryService.selectById(item.getCategoryId());
                        if (null != orderItem){
                            orderItemAch.setParentIds(orderItem.getParentIds());
                            orderItemAch.setPrice(orderItem.getPrice());
                            orderItemAch.setUnit(orderItem.getUnit());
                        }else if (companyCategoryCity!=null) {
                            orderItemAch.setParentIds(companyCategoryCity.getParentIds());
                            orderItemAch.setPrice(companyCategoryCity.getPrice().floatValue());
                            orderItemAch.setUnit(companyCategoryCity.getUnit());
						}else {
                            orderItemAch.setParentIds(companyCategory!=null?companyCategory.getParentIds():categorys.getParentIds());
                            orderItemAch.setPrice(companyCategory!=null?companyCategory.getPrice():categorys.getPrice().floatValue());
                            orderItemAch.setUnit(companyCategory!=null?companyCategory.getUnit():categorys.getUnit());
						}
						if ("1".equals(isCash)){
                            orderItemAch.setPrice(0);
                            amount += categorys.getFreeGreenCount();
                        }else{
                            amount += categorys.getGreenCount();
                        }
						orderItemAchService.insert(orderItemAch);
					}
				}
		}else {
		    if(null!=order.getCategoryId()){
                Category category = categoryService.selectById(order.getCategoryId());
                amount = category.getGreenCount().doubleValue();
            }
        }
		return ApiUtils.privatedoublegetTwoDecimal(amount);
	}

    /**
     * 根据订单状态进行分页
     */
    @Override
    public Page<Order> getOrderPage(String status, PageBean page) {
        Page<Order> pages = new Page<Order>(page.getPageNumber(), page.getPageSize());
        EntityWrapper<Order> wrapper = new EntityWrapper<Order>();
        wrapper.in("status_", status);
        return this.selectPage(pages, wrapper);
    }

    @Override
    public List<Order> getUncompleteList(String aliUserId) {
        List<Order> orderList = orderMapper.getUncompleteList(aliUserId);
        orderList = this.createName4Ali(orderList);
        return orderList;
    }

    public List<Order> createName4Ali(List<Order> orderList) {
        Set<String> attName = null;
        List<ComCatePrice> attrList = null;
        for (Order order : orderList) {
            if ("1".equals(order.getOrderFrom())) {
                switch (order.getTitle().getValue().toString()){
                    case "1":
                        order.setCategoryTitle("废弃家电");
                        break;
                    case "2":
                        order.setCategoryTitle("生活垃圾");
                        break;
                    case "7":
                        order.setCategoryTitle("生活垃圾/废弃家电");
                        break;
                }
            } else {
                if (order.getTitle() == Order.TitleType.HOUSEHOLD) {
                    attName = new HashSet<>();
                    //获得父类名称
                    attrList = orderItemService.selectCateName(Integer.parseInt(order.getId().toString()));
                    if (attrList != null && attrList.size() > 0) {
                        try {
                            for (ComCatePrice comCatePrice : attrList) {
                                if (null != comCatePrice) {
                                    attName.add(comCatePrice.getName());
                                }
                            }
                            order.setOrderFrom("0");
                            order.setTitle(Order.TitleType.HOUSEHOLD);
                            order.setCateAttName4Page(attName.toString().replace("]", "").replace("[", "").replace(",", "/").replace(" ", ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (order.getTitle() == Order.TitleType.DIGITAL) {

                    //根据分类Id查询父级分类名字
                    Category category = categoryService.selectById(order.getCategory().getCategoryId());
                    order.setCateAttName4Page(category.getName());
                    order.setTitle(Order.TitleType.DIGITAL);
                    order.setOrderFrom("0");
                } else if (order.getTitle() == Order.TitleType.BIGTHING) {
                    //根据分类Id查询父级分类名字
                    Category category = categoryService.selectById(order.getCategory().getCategoryId());
                    order.setCateAttName4Page(category.getName());
                    order.setTitle(Order.TitleType.BIGTHING);
                    order.setOrderFrom("0");
                } else if (order.getTitle() == Order.TitleType.FIVEKG) {
                    attName = new HashSet<>();
                    //获得父类名称
                    attrList = orderItemService.selectCateName(Integer.parseInt(order.getId().toString()));
                    if (attrList != null && attrList.size() > 0) {
                        try {
                            for (ComCatePrice comCatePrice : attrList) {
                                attName.add(comCatePrice.getName());
                            }
                            order.setOrderFrom("0");
                            order.setTitle(Order.TitleType.FIVEKG);
                            order.setCateAttName4Page(attName.toString().replace("]", "").replace("[", "").replace(",", "/").replace(" ", ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (order.getTitle() == Order.TitleType.IOTORDER) {
                    attName = new HashSet<>();
                    //获得父类名称
                    attrList = orderItemService.selectCateAchName(Integer.parseInt(order.getId().toString()));
                    if (attrList != null && attrList.size() > 0) {
                        try {
                            for (ComCatePrice comCatePrice : attrList) {
                                attName.add(comCatePrice.getName());
                            }
                            order.setOrderFrom("0");
                            order.setTitle(Order.TitleType.IOTORDER);
                            order.setCateAttName4Page(attName.toString().replace("]", "").replace("[", "").replace(",", "/").replace(" ", ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else if (order.getTitle() == Order.TitleType.SMALLDIGITAL) {
                    attName = new HashSet<>();
                    attrList = orderItemService.selectCateName(Integer.parseInt(order.getId().toString()));
                    if (attrList != null && attrList.size() > 0) {
                        try {
                            for (ComCatePrice comCatePrice : attrList) {
                                attName.add(comCatePrice.getName());
                            }
                            order.setOrderFrom("0");
                            order.setTitle(Order.TitleType.SMALLDIGITAL);
                            order.setCateAttName4Page(attName.toString().replace("]", "").replace("[", "").replace(",", "/").replace(" ", ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return orderList;
    }

    public Order createName4Ali(Order order) {
        Set<String> attName = null;
        List<ComCatePrice> attrList = null;
        if (order.getTitle() == Order.TitleType.HOUSEHOLD) {
            attName = new HashSet<>();
            //获得父类名称
            attrList = orderItemService.selectCateName(Integer.parseInt(order.getId().toString()));
            if (attrList != null && attrList.size() > 0) {
                try {
                    for (ComCatePrice comCatePrice : attrList) {
                        if (null != comCatePrice) {
                            attName.add(comCatePrice.getName());
                        }
                    }
                    order.setTitle(Order.TitleType.HOUSEHOLD);
                    order.setCateAttName4Page(attName.toString().replace("]", "").replace("[", "").replace(",", "/").replace(" ", ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (order.getTitle() == Order.TitleType.DIGITAL) {
            order.setCateAttName4Page(order.getCategory().getName());
            order.setTitle(Order.TitleType.DIGITAL);
        } else if (order.getTitle() == Order.TitleType.BIGTHING) {
            order.setCateAttName4Page(order.getCategory().getName());
            order.setTitle(Order.TitleType.BIGTHING);
        } else if (order.getTitle() == Order.TitleType.FIVEKG) {
            attName = new HashSet<>();
            //获得父类名称
            attrList = orderItemService.selectCateName(Integer.parseInt(order.getId().toString()));
            if (attrList != null && attrList.size() > 0) {
                try {
                    for (ComCatePrice comCatePrice : attrList) {
                        attName.add(comCatePrice.getName());
                    }
                    order.setTitle(Order.TitleType.FIVEKG);
                    order.setCateAttName4Page(attName.toString().replace("]", "").replace("[", "").replace(",", "/").replace(" ", ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return order;
    }

    public Order createName4PC(Order order) {
        Category category = order.getCategory();
        if (null==category){
            category = new Category();
        }
        if (order.getTitle() == Order.TitleType.HOUSEHOLD) {
            category.setName("生活垃圾");
            order.setCategory(category);
        } else if (order.getTitle() == Order.TitleType.DIGITAL) {
            category.setName("家电数码");
            order.setCategory(category);
        }else if (order.getTitle() == Order.TitleType.BIGTHING) {
            category.setName("大件回收");
            order.setCategory(category);
        }
        return order;
    }

    public List<AppOrderResult> createName4App(List<AppOrderResult> orderList) {
        Set<String> attName = null;
        List<ComCatePrice> attrList = null;
        List<AppOrderResult> orderLists = new ArrayList<AppOrderResult>();
        for (AppOrderResult order : orderList) {
            boolean tenGreen = this.isTenGreen(this.selectById(order.getOrderId()));
            if (tenGreen) {
                order.setIsTenGreen("1");
            } else {
                order.setIsTenGreen("0");
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
                    if (null == comCatePrice) {
                        continue;
                    }
                    attName.add(comCatePrice.getName());
                }
                order.setTitle(CategoryType.HOUSEHOLD);
                order.setCateAttName4Page(attName.toString().replace("]", "").replace("[", "").replace(",", "/").replace(" ", ""));
                orderLists.add(order);
            } else if (order.getTitle() == CategoryType.DIGITAL) {
                order.setCateAttName4Page(order.getCateName());
                order.setTitle(CategoryType.DIGITAL);
                orderLists.add(order);
            }else if (order.getTitle() == CategoryType.BIGTHING) {
                order.setCateAttName4Page(order.getCateName());
                order.setTitle(CategoryType.BIGTHING);
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
     * @return Page<Order> 返回类型
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
     * @param pageBean : 分页的条件
     * @return
     * @author 王灿
     * @changeby sgmark@aliyun.com
     */
    @Override
    public Map<String, Object> getOrderLists(BOrderBean orderBean, PageBean pageBean) {
        List<String> statusList = new ArrayList<>();
        String status = getStatus(orderBean.getStatus());
        String title = null;
        if (orderBean.getCategoryType() != null && !"".equals(orderBean.getCategoryType())) {
            title = Order.TitleType.valueOf(orderBean.getCategoryType()).getValue().toString();
        }
        statusList.add(status);
        String orderNo = orderBean.getOrderNo();
        String linkMan = orderBean.getLinkMan();
        String recyclersName = null;
        Integer companyId = null;
        String startTime = null;
        String endTime = null;
        String reInit = null;
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
        if (orderBean.getReInit() != null && !"".equals(orderBean.getReInit())) {
            reInit = orderBean.getReInit();
        }
        int size = pageBean.getPageSize();
        int num = pageBean.getPageNumber();
        Map<String, Object> map = new HashMap<String, Object>();
        if(orderBean.getStatus().equals("CANCEL") && !Order.TitleType.FIVEKG.equals(title) && !Order.TitleType.SMALLDIGITAL.equals(title)){
            statusList.add("5");
        }
            Integer count = orderMapper.getOrderListsCount(companyId, statusList, orderNo, linkMan, recyclersName, size, ((num - 1) * size), startTime, endTime, isScan, title,reInit);
            List<Order> list = orderMapper.getOrderLists(companyId, statusList, orderNo, linkMan, recyclersName, size, ((num - 1) * size), startTime, endTime, isScan, title,reInit);

            if(orderBean.getStatus().equals("INIT")&&!"1".equals(reInit)){
                Integer rejectedCount = orderMapper.getReOrderListsCount(companyId, statusList, orderNo, linkMan, recyclersName, size, ((num - 1) * size), startTime, endTime, isScan, title,reInit);
                map.put("reCount", rejectedCount);
            }

//		if ("0".equals(status)) {
//			count += orderMapper.getOrderListsCount(companyId, "6", orderNo, linkMan, recyclersName, size,((num-1)*size),startTime,endTime);
//			list.addAll(orderMapper.getOrderLists(companyId,"6",orderNo,linkMan,recyclersName,size,((num-1)*size),startTime,endTime));
//		}
            //判断生活垃圾，还是家用电器，给category.name赋值
            for (Order order : list) {
                createName4PC(order);
//			完成列表预估价格显示不正确
                /*if (OrderType.COMPLETE.getValue().equals(order.getStatus().getValue()) && order.getTitle() == Order.TitleType.HOUSEHOLD&& order.getAchPrice()!=null) {
                    order.setPrice(order.getAchPrice());
                }*/
            }
            map.put("count", count);
            map.put("list", list);
            if (CategoryType.BIGTHING.getValue().toString().equals(title)) {
                map.put("distributedCount", this.getOrderListsDistribute(orderBean, pageBean).get("count"));// 再处理订单数量
            }

        return map;
    }

    /**
     * 再处理订单
     *
     * @author sgmark@aliyun.com
     * @date 2019/3/25 0025
     * @param
     * @return
     */
    @Override
    public Map<String, Object> getOrderListsDistribute(BOrderBean orderBean, PageBean pageBean) {
        Map map = new HashMap();
        String status = getStatus(orderBean.getStatus());
        Integer companyId = orderBean.getCompanyId();
        String title = Order.TitleType.valueOf(orderBean.getCategoryType()).getValue().toString();

        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("status_", status);
        entityWrapper.eq("title", title);
        entityWrapper.eq("company_id", companyId);
        entityWrapper.eq("is_distributed", 1);
        List<Order> orderList = orderMapper.selectList(entityWrapper);
        orderList.stream().forEach(order -> {
            if (order.getCategoryId() != null) {
                order.setCategory(categoryService.selectById(order.getCategoryId()));
            }
            OrderCancleExamine orderCancleExamine = orderCancleExamineService.selectOne(new EntityWrapper<OrderCancleExamine>().eq("order_no", order.getOrderNo()));
            if (null != orderCancleExamine) {
                order.setExamineReason(orderCancleExamine.getCancleReason());
                order.setExamineStatus(orderCancleExamine.getStatus());
            }
        });
        map.put("count", orderList.size());
        map.put("list", orderList);
        return map;
    }

    /**
     * iot设备创建订单
     *
     * @author sgmark@aliyun.com
     * @date 2019/3/30 0030
     * @param
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> iotCreatOrder(IotParamBean iotParamBean) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> nameListMap = new ArrayList<>();
        List<IotParamBean.ParentList> parentLists = iotParamBean.getParentLists();
        EntityWrapper<CompanyEquipment> entityWrapper = new EntityWrapper();
        entityWrapper.eq("del_flag", 0);
        entityWrapper.eq("equipment_code", iotParamBean.getEquipmentCode());
        CompanyEquipment companyEquipment = companyEquipmentService.selectOne(entityWrapper);
        if (companyEquipment == null) {
            throw new ApiException("当前设备不存在", "-9");
        }
        Order order = new Order();
        order.setCompanyId(Integer.parseInt(companyEquipment.getCompanyId().toString()));
        if (iotParamBean.getMemberId() == null || "".equals(iotParamBean.getMemberId().trim())) {
            throw new ApiException("参数错误, 缺少memberId。。", "-9");
        }
        String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);
        order.setOrderNo(orderNo);
        //EntityWrapper<Member> entity =  new EntityWrapper<>();
        //entity.eq("card_no", iotParamBean.getMemberId()).eq("del_flag", 0);
        //entity.eq("card_no", iotParamBean.getMemberId()).eq("del_flag", 0);
        String aliUserId = ToolUtils.getAliUserIdByOrderNo(iotParamBean.getMemberId());
        System.out.println("---------------ali_uid2:" +aliUserId+"--------------------------");
        Member member = memberService.selectMemberByAliUserId(aliUserId);
        //Member member = memberService.selectOne(entity);
        if (member == null) {
            throw new ApiException("当前用户不是我们系统会员");
        }
        order.setMemberId(Integer.parseInt(member.getId().toString()));
        order.setIotEquipmentCode(iotParamBean.getEquipmentCode());
//		order.setPrice(iotParamBean.getSumPrice());
        AtomicReference<Float> bottlesCount = new AtomicReference<>((float) 0);
        //查找所有重量
        final double[] score = {0};
        parentLists.stream().forEach(parentList -> {
            nameListMap.addAll(!CollectionUtils.isEmpty(parentList.getNameList()) ? parentList.getNameList() : new ArrayList<>());
            parentList.getItemList().stream().forEach(itemList -> {
                if (itemList.getName() == Category.SecondType.BEVERAGE_BOTTLES) {
                    //峰会只给瓶子蚂蚁森林能量
                    bottlesCount.set(itemList.getQuantity());
                    score[0] += itemList.getQuantity() * 1;//瓶子个数40g/个(改为1积分一个（updateDate:20190815）)
                    order.setUnit("个");
                } else {
                    score[0] += itemList.getQuantity();
                    order.setUnit("kg");
                }
            });
        });
//		parentLists.stream().map(IotParamBean.ParentList::getItemList).forEach(itemLists -> {
//			if (itemLists.stream().map(IotParamBean.ItemList::getName).equals(Category.SecondType.BEVERAGE_BOTTLES)){
//				score[0] += 0.04 * itemLists.stream().map(IotParamBean.ItemList::getQuantity).reduce((x, y) -> x+y).get();
//			}else {
//				score[0] += itemLists.stream().map(IotParamBean.ItemList::getQuantity).reduce((x, y) -> x+y).get();
//			}
//		});
        //保留两位小数
        order.setGreenCount(new BigDecimal(score[0]).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
        order.setAreaId(companyEquipment.getAreaId());
        order.setStreetId(companyEquipment.getStreetId());
        order.setCommunityId(companyEquipment.getCommunityId());
        order.setAddress(companyEquipment.getAddress());
        order.setTitle(Order.TitleType.IOTORDER);
        order.setStatus(OrderType.COMPLETE);
        order.setPrice(BigDecimal.ZERO);
        order.setAliUserId(aliUserId);
        //保存id至redis，跳转到订单详情页面
        try {
            Hashtable<String, Object> hashTable = null;
            if (null == redisUtil.get("iotMap:" + aliUserId)) {
                hashTable = new Hashtable<>();
            } else {
                hashTable = (Hashtable<String, Object>) redisUtil.get("iotMap:" + aliUserId);
            }
            System.out.println("iotParamBean:-------------------" + iotParamBean.getParentLists().toString() + "");
//            System.out.println(parentLists.stream().allMatch(parentList -> parentList.getItemList().stream().allMatch(itemList -> itemList.getQuantity() != 0.0)));
            if (parentLists.isEmpty() || parentLists.stream().anyMatch(parentList -> parentList.getItemList().stream().anyMatch(itemList -> itemList.getQuantity() == 0.0))) {//打开箱门，并没投递任何东西
                hashTable.put("orderId", "empty");
                hashTable.put("nameList", nameListMap);
                redisUtil.set("iotMap:" + aliUserId, hashTable, 500);
                map.put("order_no", order.getOrderNo());
                map.put("equipment_code", iotParamBean.getEquipmentCode());
                map.put("msg", "empty");
                return map;
            } else {
                this.insert(order);
                hashTable.put("orderId", order.getId());
                hashTable.put("nameList", nameListMap);
                redisUtil.set("iotMap:" + aliUserId, hashTable, 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!parentLists.isEmpty()) {
            //说明是同步传过来的
             this.saveOrderItemAch(order, parentLists);
        } else {
            throw new ApiException("内容为空，新增失败");
        }
        map.put("order_no", order.getOrderNo());
        map.put("equipment_code", iotParamBean.getEquipmentCode());
        map.put("msg", "CREATED");

        //只给瓶子增加能量，为峰会使用bottlesCount.get().toString()
        this.updateCansForestIot(order, member.getAliUserId(), Float.valueOf(bottlesCount.get()).longValue(), "cans");
        //增加平台积分
        this.updateMemberPoint(order.getAliUserId(), order.getOrderNo(), order.getGreenCount(), "智能回收箱投放");
        return map;
    }

    public void updateCansForestIot(Order order, String aliUserId, long count, String type) {
        //增加蚂蚁能量
        AlipayEcoActivityRecycleSendResponse tradeorderSyncResponse = null;
        if (count >= 1) {
            tradeorderSyncResponse = ansycMyslService.updateCansForest(aliUserId, UUID.randomUUID().toString(), count, type);
            //更新order
            if (null != tradeorderSyncResponse) {
                order.setMyslOrderId(tradeorderSyncResponse.getFullEnergy()+"");
                order.setMyslParam(tradeorderSyncResponse.getParams().toString());
                this.updateById(order);
            }
        }
    }

//	/**
//	 * 初始检验价格计算是否合理
//	 * @author sgmark@aliyun.com
//	 * @date 2018/12/26 0026
//	 * @param
//	 * @return
//	 */
//	public boolean checkSubmit(List<IotParamBean.ParentList> parentLists){
//		//数据库里查询当前回收物价格
//		final BigDecimal[] sumPrice = {BigDecimal.ZERO};
//		final BigDecimal[] sumKg = {BigDecimal.ZERO};
//		stationCateList.stream().forEach(map ->{
//			List<CategoryResult> list = null;
//			list = (List<CategoryResult>) map.get("content");
//			list.stream().forEach(statCate ->{
//				categoryList.stream().forEach(category -> {
//					if (statCate.getId().equals(category.getId())){
//						category.setInPrice(new BigDecimal(statCate.getInPrice()));
//						category.setOutPrice(new BigDecimal(statCate.getOutPrice()));
//						category.setUnit(statCate.getUnit());
//						category.setCateName(statCate.getCategoryName());
//						sumKg[0] = sumKg[0].add(category.getWeight());
//						if (StationCategory.INOUTFLOW.INFLOW.getValue() == inOut)
//							sumPrice[0] = sumPrice[0].add(new BigDecimal(statCate.getInPrice()).multiply(category.getWeight()));
//						else
//							sumPrice[0] = sumPrice[0].add(new BigDecimal(statCate.getOutPrice()).multiply(category.getWeight()));
//					}
//				});
//			});
//		});
//		orderPriceBean.setCategoryList(categoryList);//放入单价单位
//		if (sumPrice[0].compareTo(orderPriceBean.getSumPrice()) == 0 && sumKg[0].compareTo(orderPriceBean.getSumKg()) ==0){//算出值和页面数据不对等
//			return true;
//		}
//		return false;
//	}
    /**
     * iot设备更新订单
     *
     * @author sgmark@aliyun.com
     * @date 2019/3/30 0030
     * @param
     * @return
     */
    @Override
    public Map<String, Object> iotUpdateOrderItemAch(IotParamBean iotParamBean) {
        Map<String, Object> map = new HashMap<>();
        if (iotParamBean.getOrderNo() == null || "".equals(iotParamBean.getOrderNo().trim())) {
            throw new ApiException("参数错误，order_no为null");
        }
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("del_flag", 1);
        entityWrapper.eq("order_no", iotParamBean.getOrderNo());
        entityWrapper.eq("iot_equipment_code", iotParamBean.getEquipmentCode());
        Order order = this.selectOne(entityWrapper);
        if (order == null) {
            throw new ApiException("找不到所对应的订单");
        }
        List<IotParamBean.ParentList> parentLists = iotParamBean.getParentLists();
        if (parentLists.size() >= 1) {
            this.saveOrderItemAch(order, parentLists);
        } else {
            throw new ApiException("内容为空，更新失败");
        }
        map.put("order_no", order.getOrderNo());
        map.put("equipment_code", iotParamBean.getEquipmentCode());
        map.put("msg", "UPDATED");
        return map;
    }

	public void saveOrderItemAch(Order order, List<IotParamBean.ParentList> parentLists){
		parentLists.stream().forEach(parentList -> {
			//找父类名称
			EntityWrapper<Category> parentEntityWrapper = new EntityWrapper<>();
			parentEntityWrapper.eq("del_flag", 0);
			parentEntityWrapper.eq("code_", parentList.getParentName().getValue());//code
			Category categoryParent = categoryService.selectOne(parentEntityWrapper);

			parentList.getItemList().stream().forEach(itemList -> {
				if (itemList.getName().getValue() == null || itemList.getQuantity() < 0){
					throw new ApiException("参数错误, 类型不匹配。。", "-9");
				}
				EntityWrapper<Category> categoryEntityWrapper = new EntityWrapper<>();
				categoryEntityWrapper.eq("del_flag", 0);
				categoryEntityWrapper.eq("code_", itemList.getName().getValue());//code
				Category category = categoryService.selectOne(categoryEntityWrapper);
				if (category != null){
					OrderItemAch orderItemAch = new OrderItemAch();
					orderItemAch.setOrderId(Integer.parseInt(order.getId().toString()));
					orderItemAch.setCategoryId(Integer.parseInt(category.getId().toString()));
					orderItemAch.setCategoryName(category.getName());
					orderItemAch.setParentId(category.getParentId());
					orderItemAch.setParentName(categoryParent.getName());//存父类名称
					if (itemList.getName() == Category.SecondType.BEVERAGE_BOTTLES){
						orderItemAch.setAmount(itemList.getQuantity());//瓶子'个'转化为kg 40g/个
						orderItemAch.setUnit("个");
					}else{
						orderItemAch.setAmount(itemList.getQuantity());
						orderItemAch.setUnit("kg");//转换为kg
					}
					orderItemAch.setCreateDate(new Date());
					orderItemAchService.insert(orderItemAch);
					OrderItem orderItem = new OrderItem();
					orderItem.setOrderId(orderItemAch.getOrderId());
					orderItem.setCategoryId(orderItemAch.getCategoryId());
					orderItem.setCategoryName(orderItemAch.getCategoryName());
					orderItem.setParentId(orderItemAch.getParentId());
					orderItem.setParentName(orderItemAch.getParentName());
					orderItem.setAmount(orderItemAch.getAmount());
					orderItem.setUnit(orderItemAch.getUnit());
					orderItem.setCreateDate(new Date());
					orderItemService.insert(orderItem);
				}
			});
		});
	}

	/**
	 * 导出企业的完成订单的Excel
	 * @param companyId
	 * @return
	 */
	@Override
	public List<Map<String,Object>> outOrderExcel(Integer companyId,String type,String startTime,String endTime,String recyclerName){
		if ("2".equals(type)){
			return orderMapper.outOrderExcelHouse(companyId,startTime,endTime,recyclerName);
		}
		return orderMapper.outOrderExcel(companyId,type,startTime,endTime,recyclerName);
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
		Integer count = orderMapper.getOrderListsCount(companyId, statusList, orderNo, linkMan, recyclersName, size, ((num - 1) * size), startTime, endTime, isScan, null,null);
		List<Order> list = orderMapper.getOrderLists(companyId, statusList, orderNo, linkMan, recyclersName, size, ((num - 1) * size), startTime, endTime, isScan, null,null);

		map.put("count", count);
		map.put("list", list);


		return map;
	}


	/**
	 * 根据各种状态查询相订单表相关的条数
	 *
	 * @param
	 * @param
	 * @return
	 * @author 王灿
	 */
	@Override
	public Map<String, Object> selectCountByStatus(BOrderBean orderBean) {
        Map<String, Object> map = new HashMap<String, Object>();
        Integer companyId = orderBean.getCompanyId();
        if(StringUtils.isNotBlank(orderBean.getCategoryType())){
            Order.TitleType categoryType = Order.TitleType.valueOf(orderBean.getCategoryType());
            EntityWrapper<Order> orderEntityWrapper = new EntityWrapper<Order>();
            if(Order.TitleType.FIVEKG.getValue().equals(categoryType.getValue())){
                orderEntityWrapper.and(" title in (3,8) ");
            }else{
                orderEntityWrapper.eq("title", categoryType.getValue());
            }
            int INITCount = this.selectCount(orderEntityWrapper.eq("status_", getStatus("INIT")).eq("del_flag", "0").eq("company_id", companyId).ne("order_from", 1));
            orderEntityWrapper = new EntityWrapper<Order>();
            if(Order.TitleType.FIVEKG.getValue().equals(categoryType.getValue())){
                orderEntityWrapper.and(" title in (3,8) ");
            }else{
                orderEntityWrapper.eq("title", categoryType.getValue());
            }
            int TOSENDCount = this.selectCount(orderEntityWrapper.eq("status_", getStatus("TOSEND")).eq("del_flag", "0").eq("company_id", companyId).ne("order_from", 1));
            orderEntityWrapper = new EntityWrapper<Order>();
            if(Order.TitleType.FIVEKG.getValue().equals(categoryType.getValue())){
                orderEntityWrapper.and(" title in (3,8) ");
            }else{
                orderEntityWrapper.eq("title", categoryType.getValue());
            }
            int ALREADYCount = this.selectCount(orderEntityWrapper.eq("status_", getStatus("ALREADY")).eq("del_flag", "0").eq("company_id", companyId).ne("order_from", 1));
            orderEntityWrapper = new EntityWrapper<Order>();
            if(Order.TitleType.FIVEKG.getValue().equals(categoryType.getValue())){
                orderEntityWrapper.and(" title in (3,8) ");
            }else{
                orderEntityWrapper.eq("title", categoryType.getValue());
            }
            int COMPLETECount = this.selectCount(orderEntityWrapper.eq("status_", getStatus("COMPLETE")).eq("del_flag", "0").eq("company_id", companyId).ne("order_from", 1));
            orderEntityWrapper = new EntityWrapper<Order>();
            if(Order.TitleType.FIVEKG.getValue().equals(categoryType.getValue())){
                orderEntityWrapper.and(" title in (3,8) ");
            }else{
                orderEntityWrapper.eq("title", categoryType.getValue());
            }
            int CANCELCount = this.selectCount(orderEntityWrapper.eq("status_", getStatus("CANCEL")).eq("del_flag", "0").eq("company_id", companyId).ne("order_from", 1));
            orderEntityWrapper = new EntityWrapper<Order>();
            if(Order.TitleType.FIVEKG.getValue().equals(categoryType.getValue())){
                orderEntityWrapper.and(" title in (3,8) ");
            }else{
                orderEntityWrapper.eq("title", categoryType.getValue());
            }
            int REJECTEDCount = this.selectCount(orderEntityWrapper.eq("status_", getStatus("REJECTED")).eq("del_flag", "0").eq("company_id", companyId).ne("order_from", 1));

            map.put("INIT", INITCount);
            map.put("TOSEND", TOSENDCount);
            map.put("ALREADY", ALREADYCount);
            map.put("COMPLETE", COMPLETECount);
            map.put("CANCEL", CANCELCount);
            map.put("REJECTED", REJECTEDCount);
        }else {
            int INITCount = this.selectCount(new EntityWrapper<Order>().eq("status_", getStatus("INIT")).eq("del_flag", "0").eq("company_id", companyId).ne("order_from", 1).in("title", 1,2,4));
            int TOSENDCount = this.selectCount(new EntityWrapper<Order>().eq("status_", getStatus("TOSEND")).eq("del_flag", "0").eq("company_id", companyId).ne("order_from", 1).in("title",1,2,4));
            int ALREADYCount = this.selectCount(new EntityWrapper<Order>().eq("status_", getStatus("ALREADY")).eq("del_flag", "0").eq("company_id", companyId).ne("order_from", 1).in("title",1,2,4));
            int COMPLETECount = this.selectCount(new EntityWrapper<Order>().eq("status_", getStatus("COMPLETE")).eq("del_flag", "0").eq("company_id", companyId).ne("order_from", 1).in("title",1,2,4));
            int CANCELCount = this.selectCount(new EntityWrapper<Order>().eq("status_", getStatus("CANCEL")).eq("del_flag", "0").eq("company_id", companyId).ne("order_from", 1).in("title",1,2,4));
            int REJECTEDCount = this.selectCount(new EntityWrapper<Order>().eq("status_", getStatus("REJECTED")).eq("del_flag", "0").eq("company_id", companyId).ne("order_from", 1).in("title",1,2,4));

            map.put("INIT", INITCount);
            map.put("TOSEND", TOSENDCount);
            map.put("ALREADY", ALREADYCount);
            map.put("COMPLETE", COMPLETECount);
            map.put("CANCEL", CANCELCount);
            map.put("REJECTED", REJECTEDCount);
        }
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
	public String orderCancel(Order order, String orderInitStatus,MqttClient mqtt4PushOrder) {
		String status = OrderType.valueOf(orderInitStatus).getValue()+"";
		boolean bool = this.updateById(order);
//		if (bool) {
//			CommToken commToken = orderMapper.getCommToken(order.getOrderNo());
//			if (commToken != null) {
//				xingeService.sendPostMessage("您有一笔订单已被取消", "已取消订单来自" + commToken.getCommName() + "，点击查看", commToken.getTencentToken(), XingeMessageServiceImp.XingeMessageCode.cancelOrder);
//			}
//		}
        //如果是咸鱼的单子，就通知咸鱼订单取消
        if (StringUtils.isNotBlank(order.getTaobaoNo())){
            QiMemOrder qiMemOrder = new QiMemOrder();
            qiMemOrder.setBizOrderId(order.getOrderNo());
            qiMemOrder.setOrderStatus(QiMemOrder.QI_MEM_ORDER_CANCEL);
            qiMemOrder.setReason(order.getCancelReason());
            bool = qiMemService.updateQiMemOrder(qiMemOrder);
        }
		if("3".equals(order.getTitle().getValue()+"")&&!"0".equals(status)){
			try{
				Company company = companyService.selectById(order.getCompanyId());
				if(null!=company&&null!=company.getAliMns()){
					HashMap<String,Object> param=new HashMap<>();
					param.put("orderNo",order.getOrderNo());
					param.put("isCancel","Y");
					param.put("cancelReason",order.getCancelReason());
					sendRocketmqMessageService.sendDeliveryOrder(JSON.toJSONString(param),company.getAliMns());
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		//阿里云推送
		if(order.getRecyclerId()!=null&&order.getRecyclerId()!=0){
			Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
			PushUtils.getAcsResponse(recyclers.getTel(),order.getStatus().getValue()+"",order.getTitle().getValue()+"");
		}
		//判断是否有以旧换新券码
		if(!StringUtils.isBlank(order.getEnterpriseCode())){
			EnterpriseCode enterpriseCode = enterpriseCodeService.selectOne(new EntityWrapper<EnterpriseCode>().eq("code", order.getEnterpriseCode()).eq("del_flag", 0).eq("is_use",1));
			//判断券码是否存在并且未使用
			if(null!=enterpriseCode){
				enterpriseCode.setIsUse("0");
				enterpriseCodeService.updateById(enterpriseCode);
			}
		}
		//将券码进行释放
		VoucherMember voucherMember = voucherMemberService.selectOne(new EntityWrapper<VoucherMember>().eq("order_no", order.getOrderNo()).eq("ali_user_id", order.getAliUserId()));
		if(null != voucherMember){
			voucherMemberService.updateVoucherCreate(voucherMember.getId());
		}
		//推送取消订单
        asyncService.pushOrder(order,mqtt4PushOrder);
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
		Order order1 = this.selectById(orderbean.getId());
		order1.setIsRead("1");
		this.updateById(order1);
		Order order = orderMapper.selectOrder(orderbean.getId());
		if((Order.TitleType.FIVEKG +"") .equals(order.getTitle()+"")){
			order.setQuantity(order.getQty()+"-"+(order.getQty()+5)+"kg");
		}
		if ((Order.OrderType.COMPLETE +"") .equals(order.getStatus()+"")&&null != order.getCompleteDate()){
			try {
				SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				order.setReceiveTime(simple.parse(order.getCompleteDate().toString()));
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		CompanyEquipment companyEquipment = companyEquipmentService.selectOne(new EntityWrapper<CompanyEquipment>().eq("equipment_code", order.getIotEquipmentCode()));
		order.setCompanyEquipment(companyEquipment);
		//查询订单表的关联图片表
		List<OrderPic> orderPicList = orderPicService.selectbyOrderId(orderbean.getId());
		//查询订单表的分了明细表
		List<OrderItem> OrderItemList = orderItemService.selectByOrderId(orderbean.getId());
		//List<ComCatePrice> cateName = orderItemService.selectCateName(orderbean.getId());
		List<Map<String, Object>> listMapObject = new ArrayList<>();
		//Map<String, Object> mapListMap = null;
		Object obj = null;
		if (order.getTitle() == Order.TitleType.HOUSEHOLD||order.getTitle() == Order.TitleType.FIVEKG||order.getTitle() == Order.TitleType.SMALLDIGITAL||order.getTitle() == Order.TitleType.IOTORDER) {
			orderbean.setStatus(order.getStatus().getValue().toString());
			orderbean.setTitle(order.getTitle().toString());
			orderbean.setIsCash(order.getIsCash());

			Map<String, Object> map = this.createPriceAmount(orderbean);
			listMapObject = (List<Map<String, Object>>) map.get("listMapObject");
			obj = map.get("achAmount");

		} else if (order.getTitle() == Order.TitleType.DIGITAL) {
			OrderItemList = orderItemService.selectByOrderId(orderbean.getId());
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Company company = companyService.selectById(order.getCompanyId());
		Payment payment = paymentService.selectOne(new EntityWrapper<Payment>().eq("order_sn", order.getOrderNo()));
		String tradeNo="";
		if(payment!=null){
			tradeNo = payment.getTradeNo();
		}
		order.setCompany(company);
		resultMap.put("order", this.createName4Ali(order));
		resultMap.put("orderPicList", orderPicList);
		resultMap.put("OrderItemList", OrderItemList);
		resultMap.put("list", listMapObject);
		resultMap.put("achAmount", obj);
		resultMap.put("tardeNo", tradeNo);
		return resultMap;
	}

    /**
     * 订单详情(企业)老接口
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
                    }
                }
            }
        }
        int CompanyId = order.getCompanyId();
        //根据企业id查询企业信息
        Company company = companyService.selectById(CompanyId);
        //根据订单Id查询评价信息
        OrderEvaluation OrderEvaluation = orderEvaluationService.selectOne(new EntityWrapper<OrderEvaluation>().eq("order_id", orderId).eq("del_flag", "0"));
        OrderCancleExamine orderCancleExamine = orderCancleExamineService.selectOne(new EntityWrapper<OrderCancleExamine>().eq("order_no", order.getOrderNo()));
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //order.setPrice(order.getAchPrice());
        //resultMap.put("order", order);
        resultMap.put("company", company);
        resultMap.put("OrderEvaluation", OrderEvaluation);
        resultMap.put("orderCancleExamine", orderCancleExamine);
        //System.out.println(11111);
        if (order.getTitle() == Order.TitleType.HOUSEHOLD||order.getTitle() == Order.TitleType.FIVEKG ||order.getTitle() == Order.TitleType.SMALLDIGITAL || order.getTitle() == Order.TitleType.IOTORDER) {
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
            if (CategoryType.DIGITAL.getValue().equals(order.getTitle().getValue()) || CategoryType.BIGTHING.getValue().equals(order.getTitle().getValue())) {
                List<OrderItem> OrderItemList = orderItemService.selectByOrderId(orderId);
                resultMap.put("OrderItemList", OrderItemList);
            } else {
                //查询订单表的分了明细表
                List<OrderItemAch> orderItemAchList = orderItemAchService.selectByOrderId(orderId);
                resultMap.put("OrderItemList", orderItemAchList);
            }
            if (order.getTitle() == Order.TitleType.HOUSEHOLD){
                order.setPrice(order.getAchPrice());
            }
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
        //回收员取消任务表中找最新取消任务的回收员名称
        OrderBean o = new OrderBean();
        o.setId(orderId);
        CancelResult cancelResult = recyclerCancelLogService.selectCancel(o);
        if(cancelResult != null){
            resultMap.put("cancelTime", cancelResult.getCancelDate());
            resultMap.put("cancelNameLast", cancelResult.getRecycleName());
        }
        //优惠券详情
        resultMap.putAll(this.voucherInfo(order));
        return resultMap;
    }

	/**
	 * 订单详情(企业)新接口
	 *
	 * @param orderId:订单
	 * @return
	 * @author 王灿
	 */
	@Override
	public Map<String, Object> selectOrderByBusiness1(Integer orderId) {
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
					}
				}
			}
		}
		int CompanyId = order.getCompanyId();
		//根据企业id查询企业信息
		Company company = companyService.selectById(CompanyId);
		//根据订单Id查询评价信息
		OrderEvaluation orderEvaluation = orderEvaluationService.selectOne(new EntityWrapper<OrderEvaluation>().eq("order_id", orderId).eq("del_flag", "0"));

        if (orderEvaluation == null){
            orderEvaluation = new OrderEvaluation();
            orderEvaluation.setContent("暂无评价");
        }
		OrderCancleExamine orderCancleExamine = orderCancleExamineService.selectOne(new EntityWrapper<OrderCancleExamine>().eq("order_no", order.getOrderNo()));

        int initCount = orderComplaintService.selectCount(new EntityWrapper<OrderComplaint>().eq("order_no", order.getOrderNo()).eq("type_", "0"));
        int TosendCount = orderComplaintService.selectCount(new EntityWrapper<OrderComplaint>().eq("order_no", order.getOrderNo()).eq("type_", "1"));
        int AlreadyCount = orderComplaintService.selectCount(new EntityWrapper<OrderComplaint>().eq("order_no", order.getOrderNo()).eq("type_", "2"));

		Map<String, Object> resultMap = new HashMap<String, Object>();

		resultMap.put("company", company);                          //企业信息
		resultMap.put("OrderEvaluation", orderEvaluation);         //评价信息
		resultMap.put("orderCancleExamine", orderCancleExamine);
        resultMap.put("dingOrder", initCount+TosendCount+AlreadyCount);//催单总次数
		if(initCount!=0){
            resultMap.put("initComplaint", "催派" + initCount + "次");
        }
        if(TosendCount!=0){
            resultMap.put("TosendCount", "催接" + TosendCount + "次");
        }
        if(AlreadyCount!=0){
            resultMap.put("AlreadyCount", "催收" + AlreadyCount + "次");
        }
		//System.out.println(11111);
		if (order.getTitle() == Order.TitleType.HOUSEHOLD ||order.getTitle() == Order.TitleType.SMALLDIGITAL ||order.getTitle() == Order.TitleType.FIVEKG || order.getTitle() == Order.TitleType.IOTORDER) {
			OrderBean orderBean = new OrderBean();
			orderBean.setId(orderId);
			orderBean.setTitle(order.getTitle().toString());
			orderBean.setStatus(order.getStatus().getValue().toString());
			orderBean.setIsCash(order.getIsCash());

			Map<String, Object> map = this.createPriceAmount4PC(orderBean);
            Map<String, Object> map1 = this.getInitDetail(orderId);
			List<Map<String, Object>> priceAmount = (List<Map<String, Object>>) map.get("listMapObject");
            BigDecimal bigDecimal = new BigDecimal(0);
            if(priceAmount.size()>0){
                for(Map map5:priceAmount){
                    bigDecimal = bigDecimal.add(new BigDecimal(map5.get("amount").toString()));
                }
            }
            List<Map<String, Object>> priceAmount1 = (List<Map<String, Object>>) map1.get("listMapObject");

			resultMap.put("priceAmount", priceAmount);  //生活垃圾实际详情
            resultMap.put("userPriceAmount", priceAmount1); //生活垃圾询价详情
			resultMap.put("greenCount", map.get("greenCount")); //积分
            resultMap.put("sumAmount", bigDecimal.toString()); //生活垃圾总重量
		}else{
            List<OrderItem> OrderItemList = orderItemService.selectByOrderId(orderId);
            List<OrderItem> OrderItemName = orderItemService.selectByOrderIdName(orderId);
                List<Map<String, Object>> list1 = new ArrayList<>();
                List<Map<String, Object>> list3 = new ArrayList<>();
                for(OrderItem list2:OrderItemName){
                    Map<String, Object> map = new HashMap<String, Object>();
                    Map<String, Object> map1 = new HashMap<String, Object>();
                    List<String>listOpption = new ArrayList();
                    for(OrderItem list:OrderItemList) {
                        if(list2.getCategoryName().equals(list.getCategoryName())){
                            listOpption.add(list.getCategoryAttrOpptionName());
                        }
                    }
                    Category category1 = categoryService.getCategoryById(list2.getCategoryId());

                    map.put("name",category1.getName());
                    map.put("opption",listOpption);
                    map.put("price",order.getAchPrice());
                    map.put("remarks",order.getAchRemarks());
                    list1.add(map);

                    map1.put("name",category1.getName());
                    map1.put("opption",listOpption);
                    map1.put("price",order.getPrice());
                    map1.put("remarks",order.getRemarks());
                    list3.add(map1);

                }
            resultMap.put("realAmount", list1);  //家电实际详情
            resultMap.put("userAmount", list3); //家电询价详情
		}

		if (OrderType.COMPLETE.getValue().equals(order.getStatus().getValue())) {
			//回收人员填的图片
			List<OrderPicAch> orderPicAchList = orderPicAchService.selectbyOrderId(orderId);
			//查询订单表的用户填的图片
			List<OrderPic> orderPicList = orderPicService.selectbyOrderId(orderId);
			if (CategoryType.DIGITAL.getValue().equals(order.getTitle().getValue()) || CategoryType.BIGTHING.getValue().equals(order.getTitle().getValue())) {
				List<OrderItem> OrderItemList = orderItemService.selectByOrderId(orderId);
				resultMap.put("OrderItemList", OrderItemList);  //大件 家电
			} else {
				//查询订单表的分了明细表
				List<OrderItemAch> orderItemAchList = orderItemAchService.selectByOrderId(orderId);
				resultMap.put("OrderItemList", orderItemAchList); //生活垃圾
			}
            Recyclers recyclers = order.getRecyclers();
            Recyclers recyclerManager = new Recyclers();
            if(recyclers!=null){
                CompanyRecycler recycler = companyRecyclerService.selectOne(new EntityWrapper<CompanyRecycler>().eq("recycler_id",recyclers.getId()));
                if(("0").equals(recycler.getIsManager())){
                    recyclerManager = recyclersService.selectById(recycler.getParentsId());
                    recyclers.setIsManager("0");
                    order.setRecyclers(recyclers);
                }else{
                    recyclers.setIsManager("1");
                    order.setRecyclers(recyclers);
                }
            }
            Payment payment = new Payment();
            payment = paymentService.selectOne(new EntityWrapper<Payment>().eq("order_sn",order.getOrderNo()).eq("status_","2"));
            resultMap.put("payment", payment);
            resultMap.put("recyclerManager", recyclerManager); //业务经理信息
			resultMap.put("order", order); //订单信息
			resultMap.put("orderPicList", orderPicAchList); //实际完成
			resultMap.put("orderUserPicList", orderPicList); //询价
            resultMap.putAll(this.voucherInfo(order));
			return resultMap;
		}
        if (OrderType.REJECTED.getValue().equals(order.getStatus().getValue())){
            OrderCancleExamine orderCancleExamineList = orderCancleExamineService.selectOne(new EntityWrapper<OrderCancleExamine>().eq("order_no", order.getOrderNo()).orderBy("create_date",false));
            if(orderCancleExamineList!=null){
                resultMap.put("cancleExamineTime", orderCancleExamineList.getCreateTime()); //
            }else{
                resultMap.put("cancleExamineTime", order.getCancelTime());
            }
        }
		//查询订单表的关联图片表
		List<OrderPic> orderPicList = orderPicService.selectbyOrderId(orderId);
		//查询订单表的分了明细表
		List<OrderItem> OrderItemList = orderItemService.selectByOrderId(orderId);
        Recyclers recyclers = order.getRecyclers();
        Recyclers recyclerManager = new Recyclers();
        if(recyclers!=null){
            CompanyRecycler recycler = companyRecyclerService.selectOne(new EntityWrapper<CompanyRecycler>().eq("recycler_id",recyclers.getId()));
            if(("0").equals(recycler.getIsManager())){
                recyclerManager = recyclersService.selectById(recycler.getParentsId());
                recyclers.setIsManager("0");
                order.setRecyclers(recyclers);
            }else{
                recyclers.setIsManager("1");
                order.setRecyclers(recyclers);
            }
        }
        resultMap.put("recyclerManager", recyclerManager);
		resultMap.put("order", order);
		resultMap.put("orderUserPicList", orderPicList);
		resultMap.put("OrderItemList", OrderItemList);
		//回收员取消任务表中找最新取消任务的回收员名称
		OrderBean o = new OrderBean();
		o.setId(orderId);
		CancelResult cancelResult = recyclerCancelLogService.selectCancel(o);
		if(cancelResult != null){
			resultMap.put("cancelTime", cancelResult.getCancelDate());
			resultMap.put("cancelNameLast", cancelResult.getRecycleName());
		}
		//优惠券详情
		resultMap.putAll(this.voucherInfo(order));
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
	public String updateOrderByBusiness(Integer orderId, String status, String cancelReason, Integer recyclerId,MqttClient mqtt4PushOrder) {
		Order order = orderMapper.selectById(orderId);

        //新增操作流水日志
        OrderOperate orderOperate = new OrderOperate();
        orderOperate.setOrderNo(order.getOrderNo());

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
				order.setIsRead("0");
				orderLog.setOpStatusAfter("REJECTED");
				orderLog.setOp("已驳回");
				this.updateById(order);

                orderOperate.setOperateLog("取消订单");
                orderOperate.setReason(cancelReason);
                orderOperate.setOperatorMan("平台");
				//取消订单消息推送
				asyncService.pushOrder(order,mqtt4PushOrder);

				//判断是否有以旧换新券码
				if(!StringUtils.isBlank(order.getEnterpriseCode())){
					EnterpriseCode enterpriseCode = enterpriseCodeService.selectOne(new EntityWrapper<EnterpriseCode>().eq("code", order.getEnterpriseCode()).eq("del_flag", 0).eq("is_use",1));
					//判断券码是否存在并且未使用
					if(null!=enterpriseCode){
						enterpriseCode.setIsUse("0");
						enterpriseCodeService.updateById(enterpriseCode);
					}
				}
				//将券码进行释放
				VoucherMember voucherMember = voucherMemberService.selectOne(new EntityWrapper<VoucherMember>().eq("order_no", order.getOrderNo()).eq("ali_user_id", order.getAliUserId()));
				if(null != voucherMember){
					voucherMemberService.updateVoucherCreate(voucherMember.getId());
				}
				try{
					//查询回收人员信息推送消息
					Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
					if(null!=recyclers){
						PushUtils.getAcsResponse(recyclers.getTel(),order.getStatus().getValue()+"",order.getTitle().getValue()+"");
					}
				}catch (Exception e){
					e.printStackTrace();
				}
				try {
					if((Order.TitleType.BIGTHING+"").equals(order.getTitle()+"")){
						asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId, MiniTemplatemessageUtil.page,order.getOrderNo(),"已取消","大件回收");
					}else if ((Order.TitleType.DIGITAL+"").equals(order.getTitle()+"")){
						asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId,MiniTemplatemessageUtil.page,order.getOrderNo(),"已取消","家电回收");
					}else {
						asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId,MiniTemplatemessageUtil.page,order.getOrderNo(),"已取消","生活垃圾");
					}
				}catch (Exception e){
					e.printStackTrace();
				}
				break;
			case "ALREADY":
				order.setStatus(OrderType.ALREADY);
				order.setReceiveTime(new Date());
				orderLog.setOpStatusAfter("ALREADY");
                orderLog.setOp("已接单");
                this.updateById(order);
                //接单消息推送
                asyncService.pushOrder(order,mqtt4PushOrder);
                break;
			case "TOSEND":
                if (order.getStatus().name().equals("INIT")) {
                    order.setStatus(OrderType.TOSEND);
                } else if(order.getStatus().name().equals("TOSEND")){
                    CompanyRecycler recycler = companyRecyclerService.selectOne(new EntityWrapper<CompanyRecycler>().eq("recycler_id",order.getRecyclerId()));
                    if(recycler!=null){
                        if("1".equals(recycler.getIsManager())){
                            order.setStatus(OrderType.TOSEND);
                        }else{
                            throw new ApiException("该订单已被业务经理转派给下属回收人员");
                        }
                    }else{
                        throw new ApiException("未找到该回收人员");
                    }
                }else{
                    throw new ApiException("该订单已被操作，请刷新页面查看状态");
                }
				//异步保存至redis中(订单号，派单时间)
//				asyncRedis.saveOrRemoveOrderIdAndTimeFromRedis(order.getId(), recyclerId.longValue(), System.currentTimeMillis(), "save");
                order.setAchPrice(BigDecimal.ZERO);
				order.setRecyclerId(recyclerId);
				order.setDistributeTime(new Date());
				order.setIsRead("0");
				orderLog.setOpStatusAfter("TOSEND");
				orderLog.setOp("已派单");
				order.setIsDistributed("1");//是否被派送过
				this.updateById(order);//放这里的目的是为了取得order中回收员id
				//查询回收人员信息
				Recyclers recyclers = recyclersService.selectById(recyclerId);

                orderOperate.setOperateLog("派单给业务经理-"+recyclers.getName());
                orderOperate.setReason("/");
                Company company = companyService.selectById(BusinessUtils.getCompanyAccount().getCompanyId());
                orderOperate.setOperatorMan(company.getName());

				PushUtils.getAcsResponse(recyclers.getTel(),order.getStatus().getValue()+"",order.getTitle().getValue()+"");
				break;
			default:
				res = "传入的订单状态不对";
				break;
		}
		//boolean bool = this.updateById(order);
		//修改订单日志表的
        orderOperateService.insert(orderOperate);
		orderLogService.insert(orderLog);
		return res;
	}

	@Override
    @DS("slave_1")
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
	/**
	 * 根据订单id查询订单的详细信息
	 */
	@Override
	public  Map<String, Object> getBigOrderDetails(Integer orderId){
		Map<String, Object> resultMap = new HashMap<>();
		AppOrderResult result = orderMapper.getBigOrderDetails(orderId);
		OrderBean orderBean = new OrderBean();
		orderBean.setId(orderId);
		List<AttrItem> bigOrderAttrItem = orderMapper.getOrderItemList(orderBean);
		if("3".equals(result.getStatus())){
			List<Map<String, Object>> list = orderMapper.getOrderAchUrlList(orderBean);
			resultMap.put("bigUrlList",list);
		}else{
			List<AttrItem> orderUrlList = orderMapper.getOrderUrlList(orderBean);
			resultMap.put("bigUrlList",orderUrlList);
		}
		resultMap.put("bigOrder",result);
		resultMap.put("bigItemOrder",bigOrderAttrItem);
		Payment payment = paymentService.selectByOrderSn(result.getOrderNo());
		resultMap.put("paymentNo",payment==null?null:payment.getTradeNo());
		return resultMap;
	}

	@Override
	public AppOrderResult getOrderDetails(OrderBean orderbean) {
		AppOrderResult result = orderMapper.getOrderDetails(orderbean);
		boolean tenGreen = this.isTenGreen(this.selectById(result.getOrderId()));
		if (tenGreen){
			result.setIsTenGreen("1");
		}else {
			result.setIsTenGreen("0");
		}
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
			} else{
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
		Payment payment = paymentService.selectByOrderSn(result.getOrderNo());
		result.setPaymentNo(payment==null?null:payment.getTradeNo());
		result.setPaymentPrice(paymentPrice);
		return result;
	}

	@Override
	public boolean modifyOrder(OrderBean orderBean) {
		Order order = this.selectById(orderBean.getId());
         Recyclers recyclers = recyclersService.selectById(orderBean.getRecyclerId());
		if (orderBean.getArrivalTime() != null && !"".equals(orderBean.getArrivalTime())) {
			try {
				order.setArrivalTime(new SimpleDateFormat("yyyy-MM-dd").parse(orderBean.getArrivalTime()));

                OrderOperate orderOperate = new OrderOperate();
                orderOperate.setOrderNo(order.getOrderNo());
                orderOperate.setOperateLog("预约时间由"+order.getArrivalTimePage()+" "
                        +"变更为"+" "+orderBean.getArrivalTime()+" "+("am".equals(order.getArrivalPeriod())?"上午":"下午"));
                orderOperate.setReason("/");
                orderOperate.setOperatorMan("回收人员-"+recyclers.getName());
                orderOperateService.insert(orderOperate);

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		boolean flag = this.updateById(order);
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
    @Transactional
	public boolean modifyOrderByPayment(OrderBean orderBean,VoucherMember voucherMember) {
		Order order = orderService.selectById(orderBean.getId());
		order.setStatus(OrderType.COMPLETE);
		order.setCompleteDate(new Date());
		if (null!=voucherMember){
            order.setVoucherMemberId(voucherMember.getId().toString());
        }
		if(StringUtils.isNotBlank(orderBean.getDiscountPrice())){
			order.setDiscountPrice(new BigDecimal(orderBean.getDiscountPrice()));
		}
        boolean flag = orderService.updateById(order);
		if(flag) {
            // key 格式 recyclerDayTimes:yyyyMMdd:回收人员id
            String key = "recyclerDayTimes:" + DateUtils.formatDate(new Date(), "yyyyMMdd") + ":" + order.getRecyclerId();
            if (redisUtil.hasKey(key)) {
                  redisUtil.increment(key);
            } else {
                //初始化并设置一天时效
                redisUtil.set(key, 1, 24 * 60 * 60);
            }
        }
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderBean.getId());
		orderLog.setOpStatusAfter("COMPLETE");
		orderLog.setOp("已完成");
        //修改日志列表
        orderLogService.insert(orderLog);
		try {
            if(null != voucherMember){
                //通知支付宝该券码已核销
                VoucherBean voucherBean = new VoucherBean();
                voucherBean.setOrderId(order.getId());
                voucherBean.setOrderNo(order.getOrderNo());
                voucherBean.setVoucherMemberId(voucherMember.getId());
                voucherMemberService.voucherUse(voucherBean);
            }
            String descrb = "";
            if((order.getTitle().getValue()+"").equals("1")||(order.getTitle().getValue()+"").equals("4")){
                Category category = categoryService.selectById(order.getCategoryId());
                descrb = category.getName();
            }else{
                descrb = "生活垃圾";
            }
            this.updateMemberPoint(order.getAliUserId(), order.getOrderNo(), orderBean.getAmount(),descrb);
            Wrapper<CompanyRecycler> wrapper = new EntityWrapper<CompanyRecycler>().eq("recycler_id", order.getRecyclerId()).eq("company_id", order.getCompanyId()).eq("status_", "1");
            if((Order.TitleType.BIGTHING+"").equals(order.getTitle()+"")){
                wrapper.eq("type_","4");
            }else {
                wrapper.eq("type_","1");
            }
            CompanyRecycler companyRecycler = companyRecyclerService.selectOne(wrapper);
            if (!"1".equals(companyRecycler.getIsManager())) {
                //阿里云推送
                Recyclers recyclers = recyclersService.selectById(companyRecycler.getParentsId());
                PushUtils.getAcsResponse(recyclers.getTel(),"3",order.getTitle().getValue()+"");
            }
            Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
            OrderOperate orderOperate = new OrderOperate();
            orderOperate.setOrderNo(order.getOrderNo());
            orderOperate.setOperateLog("完成订单");
            orderOperate.setOperatorMan("回收人员-"+recyclers.getName());
            orderOperate.setReason("/");
            orderOperateService.insert(orderOperate);
            if((Order.TitleType.BIGTHING+"").equals(order.getTitle()+"")){
				asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId, MiniTemplatemessageUtil.page,order.getOrderNo(),"已完成","大件回收");
			}else if ((Order.TitleType.DIGITAL+"").equals(order.getTitle()+"")){
				asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId,MiniTemplatemessageUtil.page,order.getOrderNo(),"已完成","家电回收");
			}else {
				asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId,MiniTemplatemessageUtil.page,order.getOrderNo(),"已完成","生活垃圾");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return true;
	}
	@Override
	public boolean modifyOrderSta(OrderBean orderBean,MqttClient mqtt4PushOrder) {
		OrderLog orderLog = new OrderLog();
		orderLog.setOrderId(orderBean.getId());
		String descrb = "";
		Order order = orderService.selectById(orderBean.getId());
		Recyclers recycler = recyclersService.selectById(order.getRecyclerId());
        //新增操作流水日志
        OrderOperate orderOperate = new OrderOperate();
        orderOperate.setOrderNo(order.getOrderNo());

		CompanyRecycler companyRecycler = companyRecyclerService.selectOne(new EntityWrapper<CompanyRecycler>().eq("recycler_id", order.getRecyclerId()).eq("company_id", order.getCompanyId()).eq("status_", "1"));
		if((order.getTitle().getValue()+"").equals("1")||(order.getTitle().getValue()+"").equals("4")){
			Category category = categoryService.selectById(order.getCategoryId());
			descrb = category.getName();
		}else{
			descrb = "生活垃圾";
		}

		if (order.getStatus() != null && !"".equals(order.getStatus())) {
			orderLog.setOpStatusBefore(order.getStatus().name());
		}
		order.setUpdateDate(new Date());
		order.setIsRead("0");
		boolean flag = false;
		if (orderBean.getStatus() != null && !"".equals(orderBean.getStatus())) {
			switch (orderBean.getStatus()) {
				case "6"://已取消任务
					if ("1".equals(order.getStatus().getValue().toString())) {
						//判断是否是回收经理还是回收人员取消订单
						if ("1".equals(companyRecycler.getIsManager())) {
							//是经理
							order.setRecyclerId(0);
                            orderOperate.setOperateLog("取消任务");
                            orderOperate.setOperatorMan("业务经理-"+recycler.getName());
							order.setStatus(OrderType.INIT);
						} else {
							//是下属回收人员
							order.setStatus(OrderType.TOSEND);
                            orderOperate.setOperateLog("取消任务");
                            orderOperate.setOperatorMan("回收人员-"+recycler.getName());
							order.setRecyclerId(companyRecycler.getParentsId());
							//阿里云推送
							Recyclers recyclers = recyclersService.selectById(companyRecycler.getParentsId());
							PushUtils.getAcsResponse(recyclers.getTel(),order.getStatus().getValue()+"",order.getTitle().getValue()+"");
						}
					} else {
						throw new ApiException("该订单已被操作，请刷新页面查看状态");
					}

					//異步刪除redis裡面派單id
//					asyncRedis.saveOrRemoveOrderIdAndTimeFromRedis(order.getId(), recycler.getId(), System.currentTimeMillis(), "remove");

					if (orderBean.getCancelReason() != null && !"".equals(orderBean.getCancelReason())) {
						order.setCancelReason(orderBean.getCancelReason());
                        orderOperate.setReason(orderBean.getCancelReason());
					} else {
                        orderOperate.setReason("没有取消原因");
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
					//取消订单消息推送
					asyncService.pushOrder(order,mqtt4PushOrder);
					break;
				case "3"://已完成
					if ("2".equals(order.getStatus().getValue().toString())) {
						order.setStatus(OrderType.COMPLETE);
					} else {
						throw new ApiException("该订单已被操作，请刷新页面查看状态");
					}
					order.setCompleteDate(new Date());
					if(StringUtils.isNotBlank(orderBean.getAchPrice())){
						order.setAchPrice(new BigDecimal(orderBean.getAchPrice()));
					}
					flag = orderService.updateById(order);
                   if(flag) {
                       // key 格式 recyclerDayTimes:yyyyMMdd:回收人员id
                       String key = "recyclerDayTimes:" + DateUtils.formatDate(new Date(), "yyyyMMdd") + ":" + order.getRecyclerId();
                       if (redisUtil.hasKey(key)) {
                           redisUtil.increment(key);
                       } else {
                           //初始化并设置一天时效
                           redisUtil.set(key, 1, 24 * 60 * 60);
                       }
                   }
					//完成订单消息推送
                    asyncService.pushOrder(order,mqtt4PushOrder);
					orderLog.setOpStatusAfter("COMPLETE");
					orderLog.setOp("已完成");

                    orderOperate.setOperateLog("完成订单");
                    orderOperate.setOperatorMan("回收人员-"+recycler.getName());
                    orderOperate.setReason("/");

					this.updateMemberPoint(order.getAliUserId(), order.getOrderNo(), orderBean.getAmount(),descrb);
					if (!"1".equals(companyRecycler.getIsManager())) {
						//阿里云推送
						Recyclers recyclers = recyclersService.selectById(companyRecycler.getParentsId());
						PushUtils.getAcsResponse(recyclers.getTel(),"3",order.getTitle().getValue()+"");
					}
					try {
						if((Order.TitleType.BIGTHING+"").equals(order.getTitle()+"")){
							asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId, MiniTemplatemessageUtil.page,order.getOrderNo(),"已完成","大件回收");
						}else if ((Order.TitleType.DIGITAL+"").equals(order.getTitle()+"")){
							asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId,MiniTemplatemessageUtil.page,order.getOrderNo(),"已完成","家电回收");
						}else {
							asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId,MiniTemplatemessageUtil.page,order.getOrderNo(),"已完成","生活垃圾");
						}
					}catch (Exception e){
						e.printStackTrace();
					}
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

                    orderOperate.setOperateLog("确认接单");
                    orderOperate.setOperatorMan("回收人员-"+recyclers.getName());
                    orderOperate.setReason("/");

					if (recyclers != null) {
						Company company = companyService.selectById(order.getCompanyId());
						if (company != null) {
							//发送接单短信
							asyncService.sendOrder("垃圾分类回收", order.getTel(), "SMS_213082494", recyclers.getName(), recyclers.getTel(), company.getName());
						}
						//異步刪除redis裡面派單id
//						asyncRedis.saveOrRemoveOrderIdAndTimeFromRedis(order.getId(), recyclers.getId(), System.currentTimeMillis(), "remove");
					}
					//接单订单消息推送
					asyncService.pushOrder(order,mqtt4PushOrder);
					if (!"1".equals(companyRecycler.getIsManager())) {
						//阿里云推送
						Recyclers recyclerss = recyclersService.selectById(companyRecycler.getParentsId());
						PushUtils.getAcsResponse(recyclerss.getTel(),"2",order.getTitle().getValue()+"");
					}
					try {
						if((Order.TitleType.BIGTHING+"").equals(order.getTitle()+"")){
							asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId,MiniTemplatemessageUtil.page,order.getOrderNo(),"已接单","大件回收");
						}else if ((Order.TitleType.DIGITAL+"").equals(order.getTitle()+"")){
							asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId,MiniTemplatemessageUtil.page,order.getOrderNo(),"已接单","家电回收");
						}else {
							asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId,MiniTemplatemessageUtil.page,order.getOrderNo(),"已接单","生活垃圾");
						}
					}catch (Exception e){
						e.printStackTrace();
					}
					break;
				default:
					throw new ApiException("不能修改为此状态");
			}
		}
        orderOperateService.insert(orderOperate);
		//修改日志列表
		flag = orderLogService.insert(orderLog);
		return flag;
	}
	@Override
	public String callbackForGround(OrderBean orderBean){
		try {
			OrderLog orderLog = new OrderLog();
			Order order = orderService.selectById(orderBean.getId());
			if (order.getStatus() != null && !"".equals(order.getStatus())) {
				orderLog.setOpStatusBefore(order.getStatus().name());
				if("0".equals(order.getStatus().getValue().toString())) {
					return "初始状态，不允许回调";
				}else if ("3".equals(order.getStatus().getValue().toString())){
					return "已完成，不允许回调";
				}
			}
			order.setUpdateDate(new Date());
			order.setIsRead("0");
			order.setRecyclerId(0);
			order.setStatus(OrderType.INIT);
			order.setCancelReason("订单回调");
			order.setCancelTime(new Date());
			orderLog.setOpStatusAfter(OrderType.INIT.name());
			orderLog.setOp("订单回调");
			orderLog.setOrderId(Integer.parseInt(order.getId().toString()));
			if(!orderService.updateById(order)){
				return "订单已被修改，稍后重试";
			}
            //新增操作流水日志
            OrderOperate orderOperate = new OrderOperate();
            orderOperate.setOrderNo(order.getOrderNo());
            orderOperate.setOperateLog("撤回订单");
            orderOperate.setReason("/");
            Company company = companyService.selectById(BusinessUtils.getCompanyAccount().getCompanyId());
            orderOperate.setOperatorMan(company.getName());
            orderOperateService.insert(orderOperate);
			orderLogService.insert(orderLog);
			return "修改成功";
		}catch (Exception e){
			return "订单已被修改，稍后重试";
		}
	}

	/**
	 * @param OrderNo:订单编号
	 * @author 王灿
	 */
	@Override
    public void updateMemberPoint(String aliUserId, String OrderNo, double amount, String descrb) {
		DecimalFormat df   = new DecimalFormat("######0.00");
        Order order = orderService.getByOrderNo(OrderNo);
        if(order!=null && order.getRecyclerId()!=null && recyclersService.selectById(order.getRecyclerId()).getIsAddPoint()=='1'){
            return;
        }
        amount = Double.parseDouble(df.format(amount));
		Point points = pointService.getPoint(aliUserId);
		if (points == null) {
			points = new Point();
			points.setAliUserId(aliUserId);
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
			Member member = memberService.selectMemberByAliUserId(aliUserId);
			System.out.println("给用户增加的积分是 ：" + amount + "----point: " +Double.parseDouble(df.format(points.getRemainPoint()))+ "");
			aliPayService.updatePoint(member.getAliCardNo(), member.getOpenCardDate(), Double.parseDouble(df.format(points.getRemainPoint())) + "", null, member.getAppId());
		} catch (Exception e) {
			System.out.println("给用户增加积分失败---------------");
		}
		PointList pointList = new PointList();
		pointList.setAliUserId(aliUserId);
		pointList.setPoint(amount + "");
		pointList.setType("0");
		pointList.setDocumentNo(OrderNo);
		pointList.setDescrb(descrb);
		pointListService.insert(pointList);
		//记录用户picc能量
        if (amount  > 0){
            PiccWater piccWater = new PiccWater();
            piccWater.setAliUserId(aliUserId);
            piccWater.setStatus("0");
            piccWater.setPointCount((int) amount);
            piccWater.setDescrb(descrb);
            piccWaterService.insert(piccWater);
        }
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
	    Order order = this.selectById(orderbean.getId());
	    if (null==order){
	        throw new RuntimeException("订单不存在");
        }
	    Integer companyId = order.getCompanyId();
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
		if (("IOTORDER".equals(orderbean.getTitle())||"FIVEKG".equals(orderbean.getTitle()) || "SMALLDIGITAL".equals(orderbean.getTitle()) ||"HOUSEHOLD".equals(orderbean.getTitle())) && "3".equals(orderbean.getStatus())) {
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
		//实际重量
		double achAmount = 0;
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
						if("0".equals(isCash)){
                            map.put("commission", list.getAdminCommissions() + "");
                        }else{
                            map.put("commission", list.getFreeCommissions() + "");
                        }
						map.put("unit", list.getUnit());
						map.put("amount", list.getAmount() + "");
						price += list.getPrice() * list.getAmount();
						achAmount += list.getAmount();
						listMap.add(map);
						count++;
					}
				}
			} else {
				for (OrderItem list : OrderItemList) {
					map = new HashMap<>();
					//listMap = new ArrayList<>();
					if (name.getId() == list.getParentId()) {
					    CompanyCategory companyCategory = companyCategoryService.selectOne(new EntityWrapper<CompanyCategory>().eq("category_id",list.getCategoryId()).eq("company_id",companyId));
					    if(companyCategory==null){
                            map.put("commission", "0.00");
                        }else{
                            if("0".equals(isCash)){
                                map.put("commission", companyCategory.getAdminCommissions() + "");
                            }else{
                                map.put("commission", companyCategory.getFreeCommissions() + "");
                            }
                        }
					    map.put("cateName", list.getCategoryName());
						map.put("price", list.getPrice() + "");
						map.put("unit", list.getUnit());
						map.put("amount", list.getAmount() + "");
						price += list.getPrice() * list.getAmount();
						listMap.add(map);
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
		try{
			resultMap.put("achAmount",new DecimalFormat("#.00").format(achAmount));
		}catch(Exception e){
			resultMap.put("achAmount",achAmount);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> getInitDetail(Integer orderId) {
		//未完成订单的数据
		List<OrderItem> OrderItemList = orderItemService.selectByOrderId(orderId);
		Order order = orderService.selectById(orderId);
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
					if(order.getIsCash().equals("1")){
                        map.put("price", "0.00");
                    }else{
                        map.put("price", list.getPrice() + "");
                    }
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
		if (("HOUSEHOLD".equals(orderbean.getTitle())||"FIVEKG".equals(orderbean.getTitle()) ||"SMALLDIGITAL".equals(orderbean.getTitle())) && "3".equals(orderbean.getStatus())) {
			cateName = orderItemService.selectCateAchName(orderbean.getId());
		} else {
			cateName = orderItemService.selectCateName(orderbean.getId());
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> listMapObject = new ArrayList<>();
		List<Map<String, String>> listMap = null;
		Map<String, String> map = null;
		float price = 0l;
        float amount = 0l;
		Map<String, Object> mapListMap = null;
		//定义绿色能量
		double greenCount = 0;
		//首先取得分类name, 去重
		for (ComCatePrice name : cateName) {
			String categoryName = "";
            amount = 0l;
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
                        if(isCash.equals("1")){
                            map.put("price", "0.00");
                        }else{
                            map.put("price", ApiUtils.doublegetTwoDecimal(list.getPrice()));
                        }
						map.put("unit", list.getUnit());
						map.put("amount", ApiUtils.doublegetTwoDecimal(Float.parseFloat(list.getAmount()+"")));
						price += list.getPrice() * list.getAmount();
                        amount += list.getAmount();
						//判断是否免费
						if ("1".equals(isCash)) {
							greenCount += list.getAmount() * 2;
						} else {
							greenCount += list.getAmount();
						}
						listMap.add(map);
					}
				}
			} else {
				for (OrderItem list : OrderItemList) {
					map = new HashMap<>();
					//listMap = new ArrayList<>();
					if (name.getId() == list.getParentId()) {
						map.put("cateName", list.getCategoryName());
                        if(isCash.equals("1")){
                            map.put("price", "0.00");
                        }else{
                            map.put("price", ApiUtils.doublegetTwoDecimal(list.getPrice()));
                        }
						map.put("unit", list.getUnit());
						map.put("amount", ApiUtils.doublegetTwoDecimal(Float.parseFloat(list.getAmount()+"")));
						price += list.getPrice() * list.getAmount();
                        amount += list.getAmount();
						listMap.add(map);
					}
				}
			}

			mapListMap.put("categoryName", StringUtils.isBlank(categoryName) ? "" : categoryName.substring(0, categoryName.length() - 1));
			mapListMap.put("list", listMap);
			mapListMap.put("name", name == null ? null : name.getName());
			mapListMap.put("price", ApiUtils.doublegetTwoDecimal(price));
            mapListMap.put("amount", ApiUtils.doublegetTwoDecimal(amount));
			listMapObject.add(mapListMap);
		}
		resultMap.put("listMapObject", listMapObject);
		resultMap.put("greenCount", greenCount);
		return resultMap;
	}

	public Integer getCityId(String location) {
		Integer cityId = null;
		//优化代码
		if(StringUtils.isBlank(location)){
		    return null;
        }
		try {
			AmapResult amap = AmapUtil.getAmap(location);
			if(null!= amap) {
				Area street = areaService.selectOne(new EntityWrapper<Area>().eq("code_", amap.getTowncode()));
				if (null != street){
					Area area = areaService.selectById(street.getParentId());
					if (null != area){
						cityId = area.getParentId();
					}
				}else {
					Area city = areaService.selectOne(new EntityWrapper<Area>().eq("area_name", amap.getCity()));
					if (null != city){
						cityId = city.getId().intValue();
					}
				}
			}
		} catch (Exception e) {
		}
		return  cityId;
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
		Integer cityId = this.getCityId(orderBean.getLocation());
		//根据回收人员信息查询所属关联公司
		CompanyRecycler companyRecycler = companyRecyclerService.selectOne(new EntityWrapper<CompanyRecycler>().eq("recycler_id", recyclers.getId()).eq("status_", "1").eq("type_","1"));
		//获取用户的详细信息
		Member member = memberService.selectMemberByAliUserId(orderBean.getAliUserId());
		if (member == null) {
		    if(StringUtils.isNotBlank(orderBean.getSpecial()) && "special".equals(orderBean.getSpecial())){
                member=new Member();
                member.setAliUserId(orderBean.getAliUserId());
                memberService.insertMember(member);
            }else {
                System.out.println(orderBean.getMemberId() + "    查不到此用户");
                return "暂无此用户";
            }
		}
		Order order = new Order();
		if(orderBean.getMemberId()!=null){
            order.setMemberId(orderBean.getMemberId());
        }else{
            order.setMemberId(member.getId().intValue());
        }
		order.setCompanyId(companyRecycler.getCompanyId());
		order.setRecyclerId(companyRecycler.getRecyclerId());
		order.setStatus(OrderType.COMPLETE);
		//随机生成订单号
		String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);
		order.setOrderNo(orderNo);
		order.setTel(member.getMobile() == null ? "" : member.getMobile());
		order.setPrice(orderBean.getPrice());
		order.setUnit("kg");
		order.setQty(9999);
		order.setCompleteDate(new Date());
		order.setIsEvaluated("0");
		order.setLevel("0");
		order.setAliUserId(member.getAliUserId());
		order.setIsCash("0");
		order.setIsDistributed("0");
		order.setAreaId(0);
		order.setTitle(Order.TitleType.HOUSEHOLD);
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
				float price = 0;
				if (!"1".equals(orderItemBean.getIsCash())){
					CompanyCategoryCityLocale companyCategoryCityLocale = companyCategoryCityLocaleService.selectOne(new EntityWrapper<CompanyCategoryCityLocale>().eq("company_id", order.getCompanyId()).eq("city_id", cityId).eq("category_id", orderItemBean.getCategoryId()));
					if (null == companyCategoryCityLocale){
						//根据分类Id和公司id查询分类信息
						CompanyCategory companyCategory = companyCategoryService.selectOne(new EntityWrapper<CompanyCategory>().eq("category_id", orderItemBean.getCategoryId()).eq("company_id", companyRecycler.getCompanyId()).eq("del_flag", 0));
						if (null != companyCategory){
							price = companyCategory.getPrice();
						}
					}else {
						price = companyCategoryCityLocale.getPrice().floatValue();
					}
				}
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
				orderItem.setPrice(price);
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
				orderItemAch.setPrice(price);
				orderItemAchService.insert(orderItemAch);
				amount += orderItemBean.getAmount();
			}
		}
		order.setGreenCount(amount);
		this.updateById(order);
		//给用户增加积分
		this.updateMemberPoint(order.getAliUserId(), order.getOrderNo(), amount,"定点回收物");

		if (orderBean.getPrice().compareTo(BigDecimal.ZERO)==0){
			//给用户增加蚂蚁能量
			OrderBean orderBeans = orderService.myslOrderData(order.getId().toString());
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
	@Override
    public Object distributeOrder(Integer orderId, Integer recyclerId) {
		Order order = this.selectById(orderId);

		if (null == order) {
			return "未找到该订单 id：" + orderId;
		}
        Recyclers recycler = recyclersService.selectById(order.getRecyclerId());
        OrderOperate orderOperate = new OrderOperate();
        orderOperate.setOrderNo(order.getOrderNo());
        orderOperate.setOperatorMan("业务经理-"+recycler.getName());

		order.setRecyclerId(recyclerId);
		boolean b = this.updateById(order);
		if (!b) {
			return "转派失败";
		}
		Recyclers recyclers = recyclersService.selectById(recyclerId);
        //新增操作流水日志
        orderOperate.setOperateLog("将订单转派给 回收人员-"+recyclers.getName());
        orderOperate.setReason("/");
        orderOperateService.insert(orderOperate);
		PushUtils.getAcsResponse(recyclers.getTel(),order.getStatus().getValue()+"",order.getTitle().getValue()+"");
		return "转派成功";
	}

	/**
	 * 转派订单列表
	 *
	 * @param recyclerId : 需要转派的回收人员id
	 * @return
	 * @author 王灿
	 */
	@Override
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
	@Override
    public Object XcxSaveOrder(OrderBean orderBean, Member member, MqttClient mqtt4PushOrder) {
		Map<String,Object> resultMap = new HashMap<>();
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
				} else if(20 <= i){
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
					order.setArrivalTime(cal.getTime());
					order.setArrivalPeriod("am");
				}else {
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
			order.setStreetId(orderBean.getStreetId());
			order.setCommunityId(orderBean.getCommunityId());
			order.setAddress(orderBean.getAddress());
			order.setFullAddress(orderBean.getFullAddress());
			order.setTel(orderBean.getTel());
			order.setLinkMan(orderBean.getLinkMan());
			order.setUnit("kg");
			order.setQty(9999);
			order.setPrice(BigDecimal.ZERO);
			order.setLevel(orderBean.getLevel());
			order.setGreenCode(orderBean.getGreenCode());
			order.setAliUserId(member.getAliUserId());
			order.setTitle(Order.TitleType.HOUSEHOLD);
			if ("1".equals(orderBean.getIsCash())) {
                order.setIsCash(orderBean.getIsCash());
                order.setIsMysl(orderBean.getIsCash());
            } else {
                order.setIsCash("0");
                order.setIsMysl("0");
            }
			order.setFormId(orderBean.getFormId());
            if (StringUtils.isNotBlank(orderBean.getAliAccount())){
                order.setOrderFrom("2");
                order.setAliAccount(orderBean.getAliAccount());
            }
			this.insert(order);
			//将券码跟订单进行绑定
			if (StringUtils.isNoneBlank(orderBean.getVoucherId())){
				boolean bool = voucherMemberService.updateVoucherUseing(order.getId(), order.getOrderNo(),order.getAliUserId(),Long.parseLong(orderBean.getVoucherId()));
				if (!bool){
					throw new ApiException("该券不存在或不是可使用状态");
				}
			}
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
					Category parentCategory = categoryService.selectById(category.getParentId());
					float price = 0;
					CompanyCategoryCity companyCategoryCity = companyCategoryCityService.selectOne(new EntityWrapper<CompanyCategoryCity>().eq("company_id", order.getCompanyId()).eq("city_id", orderBean.getCityId()).eq("category_id", orderItemBean.getId()).eq("del_flag", "0"));
					if (null == companyCategoryCity){
						CompanyCategory companyCategory = companyCategoryService.selectOne(new EntityWrapper<CompanyCategory>().eq("company_id", order.getCompanyId()).eq("category_id", orderItemBean.getId()).eq("del_flag", "0"));
						price = companyCategory.getPrice();
					}else {
						price = companyCategoryCity.getPrice().floatValue();
					}
					OrderItem orderItem = new OrderItem();
					orderItem.setOrderId(Integer.parseInt(order.getId() + ""));
					orderItem.setCategoryId(Integer.parseInt(category.getId() + ""));
					orderItem.setCategoryName(category.getName());
					orderItem.setParentId(category.getParentId());
					orderItem.setParentIds(category.getParentIds());
					orderItem.setParentName(parentCategory.getName());
					orderItem.setAmount(1);
					orderItem.setUnit(category.getUnit());
					orderItem.setPrice(price);
					orderItemService.insert(orderItem);
					amount += orderItemBean.getAmount();
				}
			}else {
				throw new ApiException("网络异常，请重新提交订单");
			}
        //保存分享码
        if (!StringUtils.isEmpty(orderBean.getShareCode())){
            lineQrCodeOrderService.insertQrCodeOrder(order.getOrderNo(), orderBean.getShareCode());
        }
		//储存订单的日志
		OrderLog orderLog = new OrderLog();
		orderLog.setOrderId(Integer.parseInt(order.getId().toString()));
		orderLog.setOpStatusAfter("INIT");
		orderLog.setOp("待接单");
		orderLogService.insert(orderLog);
		try {
			if((Order.TitleType.BIGTHING+"").equals(order.getTitle()+"")){
				asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId, MiniTemplatemessageUtil.page,order.getOrderNo(),"平台已受理","大件回收");
			}else if ((Order.TitleType.DIGITAL+"").equals(order.getTitle()+"")){
				asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId,MiniTemplatemessageUtil.page,order.getOrderNo(),"平台已受理","家电回收");
			}else {
				asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId,MiniTemplatemessageUtil.page,order.getOrderNo(),"平台已受理","生活垃圾");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		resultMap.put("msg","操作成功");
		resultMap.put("code",0);
		resultMap.put("id",order.getId());
		resultMap.put("status",order.getTitle());
		//生活垃圾下单消息推送
		asyncService.pushOrder(order,mqtt4PushOrder);
		return resultMap;
	}
	/**
	 * 保存5公斤废纺衣物的订单
	 * @return
	 */
	@Override
	public Object savefiveKgOrder(OrderBean orderBean) {
		Map<String,Object> resultMap = new HashMap<>();
		boolean flag = false;
		//根据分类Id查询物流公司分类的信息
		CompanyCategory companyCategory = companyCategoryService.selectOne(new EntityWrapper<CompanyCategory>().eq("category_id", orderBean.getCategoryId()).eq("del_flag", 0).eq("company_id", orderBean.getCompanyId()));
		//根据分类Id查询分类信息
		Category categorys = categoryService.selectById(orderBean.getCategoryId());
		Category parentCategory = categoryService.selectById(categorys.getParentId());
		Order order = new Order();
		try{
			order.setArrivalTime(new SimpleDateFormat("yyyy-MM-dd").parse(orderBean.getArrivalTime()));
			order.setArrivalPeriod(orderBean.getArrivalPeriod());
		}catch(Exception e){
			e.printStackTrace();
		}
		try {
			order.setIsMysl(orderBean.getIsMysl());
			order.setMemberId(orderBean.getMemberId());
			order.setOrderNo(orderBean.getOrderNo());
			order.setAreaId(orderBean.getAreaId());
			order.setStreetId(orderBean.getStreetId());
			order.setCommunityId(orderBean.getCommunityId());
			order.setCategoryId(Integer.parseInt(orderBean.getCategoryParentIds()));
			order.setCategoryParentIds(orderBean.getCategoryParentIds());
			order.setUnit(companyCategory.getUnit());
			order.setPrice(new BigDecimal(companyCategory.getPrice()));
			order.setAliUserId(orderBean.getAliUserId());
			order.setTitle(Order.TitleType.FIVEKG);
			order.setCompanyId(orderBean.getCompanyId());
			order.setExpressAmount(new BigDecimal(orderBean.getQty()));
			order.setAddress(orderBean.getAddress());
			order.setFullAddress(orderBean.getFullAddress());
			order.setTel(orderBean.getTel());
			order.setLinkMan(orderBean.getLinkMan());
			order.setQty(orderBean.getQty());
			order.setRemarks(orderBean.getRemarks());
			order.setFormId(orderBean.getFormId());
            if (StringUtils.isNotBlank(orderBean.getAliAccount())){
                order.setOrderFrom("2");
                order.setAliAccount(orderBean.getAliAccount());
            }
			this.insert(order);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("msg","FAIL");
			return resultMap;
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
				Category categoryPar = categoryService.selectById(category.getParentId());
				if(45!=category.getParentId()){
					throw new ApiException("系统参数异常，请返回重新下单");
				}
				OrderItem orderItem = new OrderItem();
				orderItem.setOrderId(Integer.parseInt(order.getId() + ""));
				orderItem.setCategoryId(Integer.parseInt(category.getId() + ""));
				orderItem.setCategoryName(category.getName());
				orderItem.setParentId(category.getParentId());
				orderItem.setParentIds(category.getParentIds());
				orderItem.setParentName(categoryPar.getName());
				orderItem.setAmount(orderBean.getQty());
				orderItem.setUnit(category.getUnit());
				orderItem.setPrice(StringUtils.isBlank(orderItemBean.getPrice())?category.getPrice().floatValue():Float.parseFloat(orderItemBean.getPrice()));
				orderItemService.insert(orderItem);
			}
		}else {
			throw new ApiException("网络异常，请重新提交订单");
		}
        //保存分享码
        if (!StringUtils.isEmpty(orderBean.getShareCode())){
            lineQrCodeOrderService.insertQrCodeOrder(order.getOrderNo(), orderBean.getShareCode());
        }
		//储存订单的日志
		OrderLog orderLog = new OrderLog();
		orderLog.setOrderId(Integer.parseInt(order.getId().toString()));
		orderLog.setOpStatusAfter("INIT");
		orderLog.setOp("待接单");
		orderLogService.insert(orderLog);
		try {
			if((Order.TitleType.BIGTHING+"").equals(order.getTitle()+"")){
				asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId, MiniTemplatemessageUtil.page,order.getOrderNo(),"平台已受理","大件回收");
			}else if ((Order.TitleType.DIGITAL+"").equals(order.getTitle()+"")){
				asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId,MiniTemplatemessageUtil.page,order.getOrderNo(),"平台已受理","家电回收");
			}else {
				asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId,MiniTemplatemessageUtil.page,order.getOrderNo(),"平台已受理","生活垃圾");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		resultMap.put("msg","操作成功");
		resultMap.put("code",0);
		resultMap.put("id",order.getId());
		resultMap.put("status",order.getTitle());
		return resultMap;
	}
	/**
	 * 保存小家电的订单
	 * @return
	 */
	@Override
	public Object saveSmallAppOrder(OrderBean orderBean) {
		Map<String,Object> resultMap = new HashMap<>();
		boolean flag = false;
		//根据分类Id查询物流公司分类的信息
		CompanyCategory companyCategory = companyCategoryService.selectOne(new EntityWrapper<CompanyCategory>().eq("category_id", orderBean.getCategoryId()).eq("del_flag", 0).eq("company_id", orderBean.getCompanyId()));
		//根据分类Id查询分类信息
		Category categorys = categoryService.selectById(orderBean.getCategoryId());
//		Category parentCategory = categoryService.selectById(categorys.getParentId());
		Order order = new Order();
		try{
			order.setArrivalTime(new SimpleDateFormat("yyyy-MM-dd").parse(orderBean.getArrivalTime()));
			order.setArrivalPeriod(orderBean.getArrivalPeriod());
		}catch(Exception e){
			e.printStackTrace();
		}
		try {
			order.setIsMysl(orderBean.getIsMysl());
			order.setMemberId(orderBean.getMemberId());
			order.setOrderNo(orderBean.getOrderNo());
			order.setAreaId(orderBean.getAreaId());
			order.setStreetId(orderBean.getStreetId());
			order.setCommunityId(orderBean.getCommunityId());
			order.setCategoryId(Integer.parseInt(orderBean.getCategoryParentIds()));
			order.setCategoryParentIds(orderBean.getCategoryParentIds());
			order.setUnit(companyCategory.getUnit());
			order.setPrice(new BigDecimal(companyCategory.getPrice()));
			order.setAliUserId(orderBean.getAliUserId());
			order.setTitle(Order.TitleType.SMALLDIGITAL);
			order.setCompanyId(orderBean.getCompanyId());
			order.setExpressAmount(new BigDecimal(orderBean.getQty()));
			order.setAddress(orderBean.getAddress());
			order.setFullAddress(orderBean.getFullAddress());
			order.setTel(orderBean.getTel());
			order.setLinkMan(orderBean.getLinkMan());
			order.setQty(orderBean.getQty());
			order.setRemarks(orderBean.getRemarks());
			order.setFormId(orderBean.getFormId());
            if (StringUtils.isNotBlank(orderBean.getAliAccount())){
                order.setOrderFrom("2");
                order.setAliAccount(orderBean.getAliAccount());
            }
			this.insert(order);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("msg","FAIL");
			return resultMap;
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
				Category categoryPar = categoryService.selectById(category.getParentId());
				if(!(category.getTitle().getValue()==Order.TitleType.SMALLDIGITAL.getValue())){
                    throw new ApiException("品类错误，下单失败！！");
                }
				OrderItem orderItem = new OrderItem();
				orderItem.setOrderId(Integer.parseInt(order.getId() + ""));
				orderItem.setCategoryId(Integer.parseInt(category.getId() + ""));
				orderItem.setCategoryName(category.getName());
				orderItem.setParentId(category.getParentId());
				orderItem.setParentIds(category.getParentIds());
				orderItem.setParentName(categoryPar.getName());
				orderItem.setAmount(orderBean.getQty());
				orderItem.setUnit(category.getUnit());
				orderItem.setPrice(StringUtils.isBlank(orderItemBean.getPrice())?category.getPrice().floatValue():Float.parseFloat(orderItemBean.getPrice()));
				orderItemService.insert(orderItem);
			}
		}else {
			throw new ApiException("网络异常，请重新提交订单");
		}
        //保存分享码
        if (!StringUtils.isEmpty(orderBean.getShareCode())){
            lineQrCodeOrderService.insertQrCodeOrder(order.getOrderNo(), orderBean.getShareCode());
        }
		//储存订单的日志
		OrderLog orderLog = new OrderLog();
		orderLog.setOrderId(Integer.parseInt(order.getId().toString()));
		orderLog.setOpStatusAfter("INIT");
		orderLog.setOp("待接单");
		orderLogService.insert(orderLog);
		try {
			if((Order.TitleType.BIGTHING+"").equals(order.getTitle()+"")){
				asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId, MiniTemplatemessageUtil.page,order.getOrderNo(),"平台已受理","大件回收");
			}else if ((Order.TitleType.DIGITAL+"").equals(order.getTitle()+"")){
				asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId,MiniTemplatemessageUtil.page,order.getOrderNo(),"平台已受理","家电回收");
			}else {
				asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId,MiniTemplatemessageUtil.page,order.getOrderNo(),"平台已受理","生活垃圾");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		resultMap.put("msg","操作成功");
		resultMap.put("code",0);
		resultMap.put("id",order.getId());
		resultMap.put("status",order.getTitle());
		return resultMap;
	}

	/**
	 * 小程序大家具下单接口
	 * @param orderbean
	 * @return
	 * @throws com.taobao.api.ApiException
	 */
	@Override
	public Map<String,Object> saveBigThingOrder(OrderBean orderbean,MqttClient mqtt4PushOrder) throws com.taobao.api.ApiException{
		//查询价格
		BigDecimal price = categoryService.getPricesAll(orderbean.getAliUserId(),orderbean.getCategoryId(),"BIGTHING",orderbean.getOrderItemBean().getCategoryAttrOppIds());
		Map<String,Object> resultMap = new HashMap<>();
		//获取分类的所有父类编号
		Category category =  categoryService.selectById(orderbean.getCategoryId());
		EnterpriseCode enterpriseCode = null;
		orderbean.setCategoryParentIds(category.getParentIds());
		Order order = new Order();
		try {
			order.setArrivalTime(new SimpleDateFormat("yyyy-MM-dd").parse(orderbean.getArrivalTime()));
			order.setArrivalPeriod(orderbean.getArrivalPeriod());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			order.setMemberId(orderbean.getMemberId());
			order.setCompanyId(orderbean.getCompanyId());
			order.setRecyclerId(orderbean.getRecyclerId());
			order.setOrderNo(orderbean.getOrderNo());
			order.setAreaId(orderbean.getAreaId());
			order.setStreetId(orderbean.getStreetId());
			order.setCommunityId(orderbean.getCommunityId());
			order.setAddress(orderbean.getAddress());
			order.setFullAddress(orderbean.getFullAddress());
			order.setTel(orderbean.getTel());
			order.setLinkMan(orderbean.getLinkMan());
			order.setCategoryId(orderbean.getCategoryId());
			order.setCategoryParentIds(orderbean.getCategoryParentIds());
			order.setIsMysl(orderbean.getIsMysl());
			order.setAchPrice(new BigDecimal("0"));
			order.setPrice(price);
			order.setUnit("个");
			order.setQty(1);
			order.setLevel(orderbean.getLevel());
            if (StringUtils.isNotBlank(orderbean.getAliAccount())){
                order.setOrderFrom("2");
                order.setAliAccount(orderbean.getAliAccount());
            }
			order.setGreenCode(orderbean.getGreenCode());
			order.setAliUserId(orderbean.getAliUserId());
			order.setRemarks(orderbean.getRemarks());
			order.setTitle(Order.TitleType.BIGTHING);
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
			order.setFormId(orderbean.getFormId());
			order.setPriceT(orderbean.getPrice());
			this.insert(order);
			//更新券码信息
			if(null!=enterpriseCode){
				enterpriseCode.setIsUse("1");
				enterpriseCode.setOrderId(order.getId().intValue());
				enterpriseCodeService.updateById(enterpriseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("msg","FAIL");
			return resultMap;
		}
		long orderId = order.getId();

		//保存orderItem
		OrderItemBean orderItemBean = orderbean.getOrderItemBean();
		//取出所有的分类选项Id的和
		String categoryAttrOppIds = orderItemBean.getCategoryAttrOppIds();
		String[] oppIds = categoryAttrOppIds.split(",");
		for (int i = 0; i < oppIds.length; i++) {
			//根据分类选项主键查询相关的一条记录
			CategoryAttrOption categoryAttrOption  = categoryAttrOptionService.getOptionById(oppIds[i]);
			//根据查出来的分类属性Id查出分类属性的记录
			CategoryAttr categoryAttr = categoryAttrService.selectById(categoryAttrOption.getCategoryAttrId());
			//储存订单分类属性明细
			orderItemBean.setOrderId(Integer.parseInt(orderId + ""));
			orderItemBean.setCategoryId(Integer.parseInt(category.getId() + ""));
			orderItemBean.setCategoryName(category.getName());
			orderItemBean.setCategoryAttrId(Integer.parseInt(categoryAttr.getId() + ""));
			orderItemBean.setCategoryAttrName(categoryAttr.getName());
			orderItemBean.setCategoryAttrOppId(Integer.parseInt(categoryAttrOption.getId() + ""));
			orderItemBean.setCategoryAttrOpptionName(categoryAttrOption.getName());
			orderItemService.saveByOrderItem(orderItemBean);
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
				orderPicService.insert(orderPicc);
			}
		}
		//保存分享码
        if (!StringUtils.isEmpty(orderbean.getShareCode())){
            lineQrCodeOrderService.insertQrCodeOrder(order.getOrderNo(), orderbean.getShareCode());
        }

		//储存订单的日志
		OrderLog orderLog = new OrderLog();
		orderLog.setOrderId(Integer.parseInt(orderId + ""));
		orderLog.setOpStatusAfter("INIT");
		orderLog.setOp("待接单");
		orderLogService.insert(orderLog);
		try {
			if((Order.TitleType.BIGTHING+"").equals(order.getTitle()+"")){
				asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId, MiniTemplatemessageUtil.page,order.getOrderNo(),"平台已受理","大件回收");
			}else if ((Order.TitleType.DIGITAL+"").equals(order.getTitle()+"")){
				asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId,MiniTemplatemessageUtil.page,order.getOrderNo(),"平台已受理","家电回收");
			}else {
				asyncService.sendOpenAppMini(order.getAliUserId(),order.getFormId(), MiniTemplatemessageUtil.orderTemplateId,MiniTemplatemessageUtil.page,order.getOrderNo(),"平台已受理","生活垃圾");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		resultMap.put("msg","操作成功");
		resultMap.put("id",orderId);
		resultMap.put("code",0);
		resultMap.put("status",order.getTitle());
        //大件下单消息推送
        asyncService.pushOrder(order,mqtt4PushOrder);
        return resultMap;

	}
	/**
	 * 根据订单传来的状态获取订单列表
	 */
	@Override
	public  Map<String, Object> getBigOrderList(String status,Long recycleId , PageBean pagebean){
		Map<String, Object> result = new HashMap<>();
		List<Map<String, Object>> bigOrderList = null;
		Integer count = 0;
		if("6".equals(status)){
			//转派订单列表
			bigOrderList = orderMapper.getBigOrderTransferList(recycleId.intValue(), (pagebean.getPageNumber() - 1) * pagebean.getPageSize(), pagebean.getPageSize());
			count = orderMapper.getBigOrderTransferCount(recycleId.intValue());
		}else{
			bigOrderList = orderMapper.getBigOrderList(Integer.parseInt(status),recycleId.intValue(),(pagebean.getPageNumber()-1)*pagebean.getPageSize(),pagebean.getPageSize());
			count = orderMapper.getBigOrderCount(Integer.parseInt(status),recycleId.intValue());
		}
		result.put("bigOrderList",bigOrderList);
		result.put("count",count);
		return result;
	}
	@Override
	public  String setAchOrder(OrderBean orderBean){

		Order order = orderService.selectById(orderBean.getId());
		order.setAchRemarks(orderBean.getAchRemarks());
		order.setSignUrl(orderBean.getSignUrl());
		String[] origPicss = orderBean.getPicUrl().split(",");
		for(String picUrl:origPicss){
			OrderPicAch orderPicAch = new OrderPicAch();
			orderPicAch.setOrderId(orderBean.getId());
			orderPicAch.setOrigPic(picUrl);
			orderPicAch.setPicUrl(picUrl);
			orderPicAch.setSmallPic(picUrl);
			orderPicAchService.insert(orderPicAch);
		}
		orderService.updateById(order);
		return "操作成功";
	}
	@Override
	public String saveBigOrderPrice(OrderBean orderBean,MqttClient mqtt4PushOrder){
		Order order = orderService.selectById(orderBean.getId());
        CompanyCategory companyCategory = companyCategoryService.selectCompanyCategory(order.getCompanyId().toString(), order.getCategoryId().toString());
		Category categorys = categoryService.selectById(order.getCategoryId());
		Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
		if (StringUtils.isBlank(recyclers.getAliUserId())){
			throw new ApiException("您未授权，请先在app内进行支付宝授权才能完成订单");
		}
        order.setCommissionsPrice(new BigDecimal(orderBean.getAchPrice()).multiply(companyCategory==null?BigDecimal.ZERO:companyCategory.getAdminCommissions()).divide(new BigDecimal(100)));
        order.setAdminCommissions(companyCategory==null?BigDecimal.ZERO:companyCategory.getAdminCommissions());
        order.setCompanyCommissions(companyCategory==null?BigDecimal.ZERO:companyCategory.getCompanyCommissions());
        order.setAchPrice(new BigDecimal(orderBean.getAchPrice()));
        order.setGreenCount(categorys.getGreenCount().doubleValue());
        order.setBackCommissionsPrice(new BigDecimal(orderBean.getAchPrice()).multiply(companyCategory==null?BigDecimal.ZERO:companyCategory.getCompanyCommissions()).divide(new BigDecimal(100)));
		if(Double.parseDouble(orderBean.getAchPrice())==0){
			orderBean.setStatus("3");
			orderBean.setAmount(categorys.getGreenCount());
			//修改订单状态
			this.modifyOrderSta(orderBean,mqtt4PushOrder);
			order.setStatus(OrderType.COMPLETE);

		}

		orderService.updateById(order);

		return "操作成功";
	}

//    public static void main(String[] args) {
//            int amount = 0
//            amount += (long)Math.floor((double)itemMap.get("amount"));
//            ItemOrder itemOrder = new ItemOrder();
//            itemOrder.setItemName(itemMap.get("parentName").toString());
//            itemOrder.setQuantity((long)Math.floor((double)itemMap.get("amount")));
//            List<OrderExtInfo> extInfo = new ArrayList<>();
//            OrderExtInfo orderExtInfo = new OrderExtInfo();
//            orderExtInfo.setExtKey("ITEM_TYPE");
//            orderExtInfo.setExtValue(itemMap.get("aliItemType").toString());
//            extInfo.add(orderExtInfo);
//            itemOrder.setExtInfo(extInfo);
//            orderItemList.add(itemOrder);
//
//		model.setItemOrderList(orderItemList);
//		order.setMyslParam(JSON.toJSONString(model));
//    OrderBean orderBean = new OrderBean();
//		if (amount<=30000){
//        orderBean.setIsRisk("0");
//        order.setIsRisk("0");
//    }else {
//        order.setIsRisk("1");
//        orderBean.setIsRisk("1");
//    }
//		orderService.updateById(order);
//    //给用户增加能量
//		ansycMyslService.updateForest(order.getId().toString(),JSON.toJSONString(model), 0);
//    }
	@Override
	public void deleteBigOrderRemarks(Integer orderId){
		orderMapper.deleteBigOrderRemarks(orderId);
	}

	@Override
	public List<Map<String, Object>> sevenDayorderNum(String streetId) {
		return orderMapper.sevenDayorderNum(streetId);
	}
	@Override
	public List<Map<String, Object>> oneDayorderNum(String streetId) {
		return orderMapper.oneDayorderNum(streetId);
	}


	@Override
	public OrderBean myslOrderData(String orderId) {
		Order order = orderService.selectById(orderId);
		if(!StringUtils.isBlank(order.getMyslOrderId())){
			return null;
		}
		Map<String, Object> digitalMap = null;
		List<Map<String, Object>> houseList  = null;
		//判断订单是否是电器
		if((Order.TitleType.DIGITAL.getValue()+"").equals(order.getTitle().getValue()+"")){
			digitalMap = orderItemService.selectItemOne(Integer.parseInt(orderId));
			if (null == digitalMap || digitalMap.isEmpty() ){
				return null;
			}
		}else if((Order.TitleType.HOUSEHOLD.getValue()+"").equals(order.getTitle().getValue()+"")||(Order.TitleType.FIVEKG.getValue()+"").equals(order.getTitle().getValue()+"")||(Order.TitleType.IOTORDER.getValue()+"").equals(order.getTitle().getValue()+"") || (Order.TitleType.SMALLDIGITAL.getValue()+"").equals(order.getTitle().getValue()+"")){
			houseList = orderItemAchService.selectItemSumAmount(Integer.parseInt(orderId));
		}else {
			return null;
		}
		long amount = 0;
        AlipayEcoActivityRecycleSendModel model=new AlipayEcoActivityRecycleSendModel();
		model.setBuyerId(order.getAliUserId());
		model.setSellerId(AlipayConst.SellerId);
		model.setOutBizType("RECYCLING");
		model.setOutBizNo(order.getOrderNo());
        List<EnergyGoodRequest> itemList = new ArrayList<>();
		//如果是电器
		if((Category.CategoryType.DIGITAL.getValue()+"").equals(order.getTitle().getValue()+"")){
            EnergyGoodRequest itemOrder = new EnergyGoodRequest();
			itemOrder.setItemName(digitalMap.get("name").toString());
			itemOrder.setQuantity("1");
			List<EnergyExtRequest> extInfo = new ArrayList<>();
            EnergyExtRequest orderExtInfo = new EnergyExtRequest();
			orderExtInfo.setExtKey("ITEM_TYPE");
			orderExtInfo.setExtValue(digitalMap.get("aliItemType").toString());
			extInfo.add(orderExtInfo);
			itemOrder.setItems(extInfo);
            itemList.add(itemOrder);
		}else if((Order.TitleType.HOUSEHOLD.getValue()+"").equals(order.getTitle().getValue()+"")||(Order.TitleType.FIVEKG.getValue()+"").equals(order.getTitle().getValue()+"")||(Order.TitleType.IOTORDER.getValue()+"").equals(order.getTitle().getValue()+"") || (Order.TitleType.SMALLDIGITAL.getValue()+"").equals(order.getTitle().getValue()+"")){
			for (Map<String, Object> itemMap:houseList) {
				if (null==itemMap.get("aliItemType")){
					continue;
				}
				amount += (long)Math.floor((double)itemMap.get("amount"));
                EnergyGoodRequest itemOrder = new EnergyGoodRequest();
				itemOrder.setItemName(itemMap.get("parentName").toString());
				itemOrder.setQuantity( Math.floor((double)itemMap.get("amount"))+"");
				List<EnergyExtRequest> extInfo = new ArrayList<>();
                EnergyExtRequest orderExtInfo = new EnergyExtRequest();
				orderExtInfo.setExtKey("ITEM_TYPE");
				orderExtInfo.setExtValue(itemMap.get("aliItemType").toString());
				extInfo.add(orderExtInfo);
				itemOrder.setItems(extInfo);
                itemList.add(itemOrder);
			}
		}
		if (null == itemList || itemList.isEmpty() ){
			return null;
		}
		model.setItemList(itemList);
		order.setMyslParam(JSON.toJSONString(model));
		OrderBean orderBean = new OrderBean();
		if (amount<=30000){
			orderBean.setIsRisk("0");
			order.setIsRisk("0");
		}else {
			order.setIsRisk("1");
			orderBean.setIsRisk("1");
		}
		orderService.updateById(order);
		//给用户增加能量
		ansycMyslService.updateForest(order.getId().toString(),JSON.toJSONString(model), 0);
		orderBean.setMyslParam(JSON.toJSONString(model));
		return orderBean;
	}

	/**	根据手机号查询订单列表
	  * @author sgmark@aliyun.com
	  * @date 2019/6/3 0003
	  * @param
	  * @return
	  */
	@Override
	public Map<String, Object> getOrderListByPhone(OrderBean orderbean) {
		int count = 0;
		List<AppOrderResult> listOrder = null;
		if (null != orderbean.getTel()){
			orderbean.setTel("%"+orderbean.getTel()+"%");
		}
		count = orderMapper.getOrderCountByPhone(orderbean);//得到总数
		listOrder = orderMapper.getOrderListByPhone(orderbean, (orderbean.getPagebean().getPageNumber() - 1) * orderbean.getPagebean().getPageSize(), orderbean.getPagebean().getPageSize());
		Map<String, Object> map = new HashMap<String, Object>();
		int pageNum = count % orderbean.getPagebean().getPageSize() == 0 ? count / orderbean.getPagebean().getPageSize() : count / orderbean.getPagebean().getPageSize() + 1;
		int currentpage = orderbean.getPagebean().getPageNumber();
		map.put("pageNum", pageNum);
		map.put("listOrder", this.createName4App(listOrder));
		map.put("count", count);
		map.put("currentPage", currentpage > pageNum ? pageNum : currentpage);
		return map;
	}
	/** 根据手机号查询大件订单列表
	  * @author sgmark@aliyun.com
	  * @date 2019/6/4 0004
	  * @param
	  * @return
	  */
	@Override
	public Map<String, Object> getBigOrderListByPhone(OrderBean orderbean) {
		Map<String, Object> result = new HashMap<>();
		if (null != orderbean.getTel() && orderbean.getTel().length() < 11){
			orderbean.setTel("%"+orderbean.getTel()+"%");
		}
		List<Map<String, Object>> bigOrderList = null;
		Integer count = 0;
		bigOrderList = orderMapper.getBigOrderListByPhone(orderbean.getOrderNo(),orderbean.getRecyclerId(), orderbean.getTel(), (orderbean.getPagebean().getPageNumber() - 1) * orderbean.getPagebean().getPageSize(), orderbean.getPagebean().getPageSize());
		count = orderMapper.getBigOrderCountByPhone(orderbean.getOrderNo(),orderbean.getRecyclerId(), orderbean.getTel());
		result.put("bigOrderList", bigOrderList);
		result.put("count", count);
		return result;
	}

	@Transactional
	@Override
	public Object orderUpdateStatus(String companyId, String orderId) {
        OrderLog orderLog = new OrderLog();
        Order order = this.selectById(orderId);
        orderLog.setOpStatusBefore(order.getStatus().name());
        order.setStatus(OrderType.INIT);
        order.setRecyclerId(0);
        this.updateById(order);
        //新增订单日志表的记录
        orderLog.setOpStatusAfter(order.getStatus().name());
        orderLog.setOp("已初始");
        orderLog.setOrderId(order.getId().intValue());
        orderLogService.insert(orderLog);
        return "操作成功";
	}
	/**
	 * 根据订单id自动派单给具体的回收人员
	 * @param orderId
	 * @return
	 */
	@Override
	public String orderSendRecycleByOrderId(Integer orderId){

		List<Recyclers> sendOrderRecyclersList = recyclersService.getSendOrderRecyclersList(orderId);
		if(!sendOrderRecyclersList.isEmpty()){
			Order order = this.selectById(orderId);
			order.setRecyclerId(sendOrderRecyclersList.get(0).getId().intValue());
			order.setStatus(OrderType.TOSEND);
			order.setDistributeTime(new Date());
			this.updateById(order);
		}
		return "操作成功";
	}

	/**
	 * 派发五公斤订单
	 * @param orderId
	 * @return
	 */
	@Override
    public Object tosendfiveKgOrder(Integer orderId){
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		Order order = orderService.selectById(orderId);
		Company company = companyService.selectById(order.getCompanyId());
		if(null==company||null==company.getAliMns()){
			return "该企业无法回收五公斤";
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
			if(Order.TitleType.SMALLDIGITAL==order.getTitle()){
                param.put("type","appliances");
                param.put("orderType","生活小家电");
            }
			param.put("channelMemberId","RC20190427231730100044422");
			param.put("orderAmount",order.getQty());
			param.put("userName",order.getLinkMan());
			param.put("userTel", order.getTel());
			param.put("userAddress",order.getAddress()+order.getFullAddress());
			param.put("arrivalTime", sim.format(order.getArrivalTime())+" "+("am".equals(order.getArrivalPeriod())?"10:00:00":("pm".equals(order.getArrivalPeriod())?"16:00:00":order.getArrivalPeriod().substring(0,2)+":00:00")));
			param.put("isCancel","N");
			sendRocketmqMessageService.sendDeliveryOrder(JSON.toJSONString(param),company.getAliMns());
		}catch (Exception e){
			e.printStackTrace();
		}
		return "操作成功";
	}

	@Override
	public Boolean selectOrderByImprisonRule(String aliUserId,String title,Integer orderNum,Integer dateNum){
		Boolean isImprisonRule = false;
		Date endDate = new Date();
		Date startDate = ToolUtils.RmDateByNow(endDate,dateNum-1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer orders = this.selectCount(new EntityWrapper<Order>().eq("ali_user_id", aliUserId).eq("title", title).between("create_date", sdf.format(startDate)+" 00:00:01", sdf.format(endDate)+" 23:59:59"));
		if(orders >= orderNum){
			isImprisonRule = true;
		}
		return isImprisonRule;
	}


	public Object recallOrder(Integer orderId,Long recyclerId){
		String status = "5";
		String recrcleTel = "";
		Order order = this.selectById(orderId);
		if((OrderType.TOSEND+"").equals(order.getStatus()+"")||(Order.OrderType.ALREADY+"").equals(order.getStatus()+"")) {
			Recyclers recyclers = recyclersService.selectById(recyclerId);
			Recyclers recycler = recyclersService.selectById(order.getRecyclerId());
			recrcleTel = recycler.getTel();
			if (null == order) {
				return "未找到该订单 id：" + orderId;
			}
			order.setRecyclerId(recyclerId.intValue());
			order.setStatus(OrderType.TOSEND);
			boolean b = this.updateById(order);
			if (!b) {
				return "转派失败";
			}
            OrderOperate orderOperate = new OrderOperate();
            orderOperate.setOrderNo(order.getOrderNo());
            orderOperate.setOperateLog("撤回转派");
            orderOperate.setReason("/");
            orderOperate.setOperatorMan("业务经理-"+recyclers.getName());
            orderOperateService.insert(orderOperate);

			PushUtils.getAcsResponse(recrcleTel, status, order.getTitle().getValue() + "");
			PushUtils.getAcsResponse(recyclers.getTel(), order.getStatus().getValue() + "", order.getTitle().getValue() + "");
			return "操作成功";
		}else {
			return "订单已完成或已取消，不可操作";
		}
	}
	@Override
	public Object getOrderListByAdmin(OrderBean orderBean) {
		PageBean pageBean = orderBean.getPagebean();


		Integer startPage = (pageBean.getPageNumber()-1)*pageBean.getPageSize();
		Integer pageSize = pageBean.getPageSize();
		Object orderList = orderMapper.getOrderListByAdmin(orderBean.getComplaintType(),orderBean.getCompanyId()==null?null:orderBean.getCompanyId().toString(), orderBean.getTitle(), orderBean.getStatus(), orderBean.getTel(), orderBean.getOrderNo(), orderBean.getLinkName(), orderBean.getStartTime(), orderBean.getEndTime(), startPage, pageSize);
		Integer orderCount = orderMapper.getOrderCountByAdmin(orderBean.getComplaintType(),orderBean.getCompanyId()==null?null:orderBean.getCompanyId().toString(), orderBean.getTitle(), orderBean.getStatus(), orderBean.getTel(), orderBean.getOrderNo(), orderBean.getLinkName(), orderBean.getStartTime(), orderBean.getEndTime());
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> pagination = new HashMap<>();
		pagination.put("current",pageBean.getPageNumber());
		pagination.put("pageSize",pageBean.getPageSize());
		pagination.put("total",orderCount);
		resultMap.put("pagination",pagination);
		resultMap.put("orderList",orderList);
		return resultMap;
	}

	@Override
	public Object getOrderListByAdminReception(OrderBean orderBean) {
		PageBean pageBean = orderBean.getPagebean();
		if (null==pageBean){
			pageBean = new PageBean();
		}
		Integer startPage = (pageBean.getPageNumber()-1)*pageBean.getPageSize();
		Integer pageSize = pageBean.getPageSize();
		List<Map<String, Object>> orderList = orderMapper.getOrderListByAdminReception(orderBean.getComplaintType(), orderBean.getCompanyId() == null ? null : orderBean.getCompanyId().toString(), orderBean.getTitle(), orderBean.getStatus(), orderBean.getTel(), orderBean.getOrderNo(), orderBean.getLinkName(), orderBean.getStartTime(), orderBean.getEndTime(), startPage, pageSize);
		Integer orderCount = orderMapper.getOrderCountByAdminReception(orderBean.getComplaintType(),orderBean.getCompanyId()==null?null:orderBean.getCompanyId().toString(), orderBean.getTitle(), orderBean.getStatus(), orderBean.getTel(), orderBean.getOrderNo(), orderBean.getLinkName(), orderBean.getStartTime(), orderBean.getEndTime());
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> pagination = new HashMap<>();
		pagination.put("current",pageBean.getPageNumber());
		pagination.put("pageSize",pageBean.getPageSize());
		pagination.put("total",orderCount);
		resultMap.put("pagination",pagination);
		resultMap.put("orderList",orderList);
		return resultMap;
	}
    @Override
    public Object getXyOrderListByAdminReception(OrderBean orderBean) {
        PageBean pageBean = orderBean.getPagebean();
        if (null==pageBean){
            pageBean = new PageBean();
        }
        Integer startPage = (pageBean.getPageNumber()-1)*pageBean.getPageSize();
        Integer pageSize = pageBean.getPageSize();
        List<Map<String, Object>> orderList = orderMapper.getXyOrderListByAdminReception(orderBean.getIsNormal(),orderBean.getCompanyId() == null ? null : orderBean.getCompanyId().toString(), orderBean.getTitle(), orderBean.getStatus(), orderBean.getTel(), orderBean.getOrderNo(), orderBean.getLinkName(), orderBean.getStartTime(), orderBean.getEndTime(), startPage, pageSize);
        Integer orderCount = orderMapper.getXyOrderCountByAdminReception(orderBean.getIsNormal(),orderBean.getCompanyId()==null?null:orderBean.getCompanyId().toString(), orderBean.getTitle(), orderBean.getStatus(), orderBean.getTel(), orderBean.getOrderNo(), orderBean.getLinkName(), orderBean.getStartTime(), orderBean.getEndTime());
        Map<String,Object> resultMap = new HashMap<>();
        Map<String,Object> pagination = new HashMap<>();
        pagination.put("current",pageBean.getPageNumber());
        pagination.put("pageSize",pageBean.getPageSize());
        pagination.put("total",orderCount);
        resultMap.put("pagination",pagination);
        resultMap.put("orderList",orderList);
        return resultMap;
    }
	@Override
    public List<Map<String, Object>> getOutComplaintOrderList(OrderBean orderBean){
	    if(StringUtils.isBlank(orderBean.getStartTime())&&StringUtils.isBlank(orderBean.getEndTime())){
            if (StringUtils.isBlank(orderBean.getComplaintType())&&null==orderBean.getCompanyId()&&StringUtils.isBlank(orderBean.getTitle())&&StringUtils.isBlank(orderBean.getStatus())&&StringUtils.isBlank(orderBean.getTel())&&StringUtils.isBlank(orderBean.getOrderNo())&&StringUtils.isBlank(orderBean.getLinkName())){
                throw new ApiException("请输入查询条件");
            }
        }
		return orderMapper.getOutComplaintOrderList(orderBean.getComplaintType(),orderBean.getCompanyId()==null?null:orderBean.getCompanyId().toString(), orderBean.getTitle(), orderBean.getStatus(), orderBean.getTel(), orderBean.getOrderNo(), orderBean.getLinkName(), orderBean.getStartTime(), orderBean.getEndTime());
	}

	@Override
	public Object getOrderDetailByIdByAdmin(String orderId) {
		Order order = this.selectById(orderId);
		Company company = companyService.selectById(order.getCompanyId());
		List<OrderItem> orderItemList = orderItemService.selectList(new EntityWrapper<OrderItem>().eq("order_id", orderId));
		List<OrderItemAch> orderItemAchList = orderItemAchService.selectList(new EntityWrapper<OrderItemAch>().eq("order_id", orderId));
		if (orderItemAchList.isEmpty()){
			orderItemAchList = new ArrayList<>();
		}
		List<OrderPic> orderPicList = orderPicService.selectList(new EntityWrapper<OrderPic>().eq("order_id", orderId));
		List<OrderPicAch> orderPicAchList = orderPicAchService.selectList(new EntityWrapper<OrderPicAch>().eq("order_id", orderId));
		if (orderPicAchList.isEmpty()){
			orderPicAchList = new ArrayList<>();
		}
		String paymentNo = "";
		Payment payment = paymentService.selectOne(new EntityWrapper<Payment>().eq("order_sn", order.getOrderNo()));
		if (null != payment){
			AlipayFundTransOrderQueryResponse response = paymentService.getTransfer(payment.getOrderSn());
			if(response.isSuccess()){
				paymentNo = response.getOrderId();
			}
		}
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("order",order);
		resultMap.put("company",company);
		resultMap.put("orderItemList",orderItemList);
		resultMap.put("orderItemAchList",orderItemAchList);
		resultMap.put("orderPicList",orderPicList);
		resultMap.put("orderPicAchList",orderPicAchList);
		resultMap.put("paymentNo",paymentNo);

		return resultMap;
    }
	/**
	 * 根据区域id 提供第三方的订单数据 默认第一页 10条数据
	 * @param areaId
	 * @return
	 */
	@Override
    public List<ThirdOrderResult> orderStatistics4Third(String areaId, String startTime, String endTime, Integer pageNumber, Integer pageSize){
		Integer _pageNumber = pageNumber == null?0:pageNumber;
		Integer _pageSize  = pageSize==null?10:pageSize;

		Integer start = _pageNumber*_pageSize;

		return orderMapper.orderStatistics4Third(areaId,startTime,endTime,start,_pageSize);
	}


	@Override
	public Integer getOrderCountByLj(LjAdminBean ljAdminBean){
		return orderMapper.getOrderCountByLj(ljAdminBean.getCityId(),ljAdminBean.getAreaId(),ljAdminBean.getStreetId(),ljAdminBean.getCompanyId(),ljAdminBean.getStartDate(),ljAdminBean.getEndDate());
	}
	@Override
	public Integer getInitCountByLj(LjAdminBean ljAdminBean){
		return orderMapper.getInitCountByLj(ljAdminBean.getCityId(),ljAdminBean.getAreaId(),ljAdminBean.getStreetId(),ljAdminBean.getCompanyId());
	}
	@Override
	public Integer getTosendCountByLj(LjAdminBean ljAdminBean){
		return orderMapper.getTosendCountByLj(ljAdminBean.getCityId(),ljAdminBean.getAreaId(),ljAdminBean.getStreetId(),ljAdminBean.getCompanyId());
	}
	@Override
	public Integer getReadyCountByLj(LjAdminBean ljAdminBean){
		return orderMapper.getReadyCountByLj(ljAdminBean.getCityId(),ljAdminBean.getAreaId(),ljAdminBean.getStreetId(),ljAdminBean.getCompanyId());
	}
	@Override
	public Integer getOrderCountBytitle(LjAdminBean ljAdminBean,String title,String isGreen){
		return orderMapper.getOrderCountBytitle(ljAdminBean.getCityId(),ljAdminBean.getAreaId(),ljAdminBean.getStreetId(),ljAdminBean.getCompanyId(),ljAdminBean.getStartDate(),ljAdminBean.getEndDate(),title,isGreen);
	}
	@Override
	public Double getGreenBigPaymentOrderPrice(LjAdminBean ljAdminBean){
		return orderMapper.getGreenBigPaymentOrderPrice(ljAdminBean.getCityId(),ljAdminBean.getAreaId(),ljAdminBean.getStreetId(),ljAdminBean.getCompanyId(),ljAdminBean.getStartDate(),ljAdminBean.getEndDate());
	}
	@Override
	public List<Map<String,Object>> getOrderCategoryByLj(LjAdminBean ljAdminBean){
		return orderMapper.getOrderCategoryByLj(ljAdminBean.getCityId(),ljAdminBean.getAreaId(),ljAdminBean.getStreetId(),ljAdminBean.getCompanyId(),ljAdminBean.getStartDate(),ljAdminBean.getEndDate());
	}
	@Override
	public List<Map<String,Object>> getHouseOrderCategoryByLj(LjAdminBean ljAdminBean,String isCash){
		if ("0".equals(isCash)){
			return orderMapper.getHouseOrderCategoryByLjAsCash(ljAdminBean.getCityId(),ljAdminBean.getAreaId(),ljAdminBean.getStreetId(),ljAdminBean.getCompanyId(),ljAdminBean.getStartDate(),ljAdminBean.getEndDate());
		}
		return orderMapper.getHouseOrderCategoryByLjAsGreen(ljAdminBean.getCityId(),ljAdminBean.getAreaId(),ljAdminBean.getStreetId(),ljAdminBean.getCompanyId(),ljAdminBean.getStartDate(),ljAdminBean.getEndDate());
	}
	@Override
	public Double avgOrMaxDateByOrder(LjAdminBean ljAdminBean,String status,String avgOrMax,String title){
		if ("avg".equals(avgOrMax)){
			return orderMapper.avgOrMaxDateByOrderAvg(ljAdminBean.getCityId(),ljAdminBean.getAreaId(),ljAdminBean.getStreetId(),ljAdminBean.getCompanyId(),ljAdminBean.getStartDate(),ljAdminBean.getEndDate(),status,title);
		}
		return orderMapper.avgOrMaxDateByOrderMax(ljAdminBean.getCityId(),ljAdminBean.getAreaId(),ljAdminBean.getStreetId(),ljAdminBean.getCompanyId(),ljAdminBean.getStartDate(),ljAdminBean.getEndDate(),status,title);
	}
	@Override
	public Integer getSumOrderBylj(LjAdminBean ljAdminBean){
		return orderMapper.getSumOrderBylj(ljAdminBean.getCityId(),ljAdminBean.getAreaId(),ljAdminBean.getStreetId(),ljAdminBean.getCompanyId(),ljAdminBean.getStartDate(),ljAdminBean.getEndDate());
	}

	@Override
	public Integer getOrderLjByStatus(LjAdminBean ljAdminBean,String status){
		return orderMapper.getOrderLjByStatus(ljAdminBean.getCityId(),ljAdminBean.getAreaId(),ljAdminBean.getStreetId(),ljAdminBean.getCompanyId(),ljAdminBean.getStartDate(),ljAdminBean.getEndDate(),status);
	}

	@Override
	public Object getNewOrderDetail(Integer orderId){
		Map<String,Object> resultMap = new HashMap<>();
		Order order = this.selectById(orderId);
		String paymentNo = "";
		Payment payment = paymentService.selectByOrderSn(order.getOrderNo());
		if (null != payment){
			//查询此单交易是否成功
//			AlipayFundTransOrderQueryResponse aliPayment = paymentService.getTransfer(payment.getId().toString());
//			if("Success".equals(aliPayment.getMsg())){
//				paymentNo = aliPayment.getOrderId();
//			}
            paymentNo = payment.getTradeNo();
		}
		Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
		List<OrderPic> orderPicList = orderPicService.selectList(new EntityWrapper<OrderPic>().eq("order_id", order.getId()));
		List<OrderPicAch> orderPicAchList = orderPicAchService.selectList(new EntityWrapper<OrderPicAch>().eq("order_id", order.getId()));

		Company company = companyService.selectById(order.getCompanyId());
		List<Map<String,Object>> houseOrderItemiPrice = new ArrayList<>();
		List<Map<String,Object>> houseOrderItemiNoPrice = new ArrayList<>();
		List<OrderItem> orderItemList = new ArrayList<>();
		Category category = null;
		Category parentCategory = null;
		if ((Order.TitleType.HOUSEHOLD+"").equals(order.getTitle()+"")||(Order.TitleType.FIVEKG+"").equals(order.getTitle()+"")||(Order.TitleType.IOTORDER+"").equals(order.getTitle()+"")||(Order.TitleType.SMALLDIGITAL+"").equals(order.getTitle()+"")){
			if ((OrderType.COMPLETE+"").equals(order.getStatus()+"")){
				houseOrderItemiPrice  = orderItemAchService.getOrderItemDetail(order.getId(),"0");
				houseOrderItemiNoPrice = orderItemAchService.getOrderItemDetail(order.getId(),"1");
			}else {
				houseOrderItemiPrice = orderItemService.getOrderItemDetail(order.getId(),"0");
				houseOrderItemiNoPrice  = orderItemService.getOrderItemDetail(order.getId(),"1");
			}
		}else {
			category = categoryService.selectById(order.getCategoryId());
			if (null != category){
                parentCategory = categoryService.selectById(category.getParentId());
            }
			orderItemList = orderItemService.selectList(new EntityWrapper<OrderItem>().eq("order_id", order.getId()));
		}
		//马上回收跳转过来的可领取红包的订单（当前的订单编号-随机数）保存到redis中，随机数作为发红包的order_sn
		RedisUtil.SaveOrGetFromRedis saveOrGetFromRedis = new RedisUtil.SaveOrGetFromRedis();
		Object fromRedis = saveOrGetFromRedis.getFromRedis("receive:" + order.getOrderNo(), jedisPool);
		if (null != fromRedis){
			if (OrderType.COMPLETE.getValue().equals(order.getStatus().getValue())){
				//根据随机数去发放流水表中查找记录
				Payment deliveryPament = paymentService.selectOne(new EntityWrapper<Payment>().eq("del_flag", 0).eq("order_sn", fromRedis));
				if (null != deliveryPament){
					//已领取
					resultMap.put("deliveryStatus", "COMPLETE");
				}else {
					//可领取，未领取状态
					resultMap.put("deliveryStatus", "ALREADY");
					//红包派发随机数
					resultMap.put("delivery_no", fromRedis);
				}
			}else {
				//第二种：订单未完成
				resultMap.put("deliveryStatus", "INIT");
			}
		}else {
			//没有红包
			resultMap.put("deliveryStatus", "NOT");
		}
		//优惠券详情
		resultMap.putAll(this.voucherInfo(order));

		resultMap.put("order",order);
		resultMap.put("paymentNo",paymentNo);
		resultMap.put("recyclers",recyclers);
		resultMap.put("orderPicList",orderPicList);
		resultMap.put("orderPicAchList",orderPicAchList);
		resultMap.put("orderItemList",orderItemList);
		resultMap.put("company",company);
		resultMap.put("category",category);
		resultMap.put("parentCategory",parentCategory);
		resultMap.put("houseOrderItemiPrice",houseOrderItemiPrice);
		resultMap.put("houseOrderItemiNoPrice",houseOrderItemiNoPrice);
		return  resultMap;
	}



	@Override
	public Object getAdminOrderDetail(Integer orderId){
		Map<String,Object> resultMap = new HashMap<>();
		Order order = this.selectById(orderId);
		String paymentNo = "";
		Payment payment = paymentService.selectByOrderSn(order.getOrderNo());
		if (null != payment){
			//查询此单交易是否成功
			AlipayFundTransOrderQueryResponse aliPayment = paymentService.getTransfer(payment.getOrderSn());
			if("Success".equals(aliPayment.getMsg())){
				paymentNo = aliPayment.getOrderId();
			}
		}
		Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
		List<OrderPic> orderPicList = orderPicService.selectList(new EntityWrapper<OrderPic>().eq("order_id", order.getId()));
		List<OrderPicAch> orderPicAchList = orderPicAchService.selectList(new EntityWrapper<OrderPicAch>().eq("order_id", order.getId()));
		Company company = companyService.selectById(order.getCompanyId());
		Category category = categoryService.selectById(order.getCategoryId());
		List<OrderItem>	orderItemList = orderItemService.selectList(new EntityWrapper<OrderItem>().eq("order_id", order.getId()));
		List<OrderItemAch> orderItemAchList = orderItemAchService.selectList(new EntityWrapper<OrderItemAch>().eq("order_id", order.getId()));
		List<OrderComplaint> orderComplaintList = orderComplaintService.selectList(new EntityWrapper<OrderComplaint>().eq("order_no", order.getOrderNo()).orderBy("create_date",false));
		Map<String, Object> orderComplaintMap = orderMapper.getOrderComplaint(order.getOrderNo());
		List<OrderComplaint> resultList = new ArrayList<>();
		if (null != orderComplaintMap && 2880<Integer.parseInt(orderComplaintMap.get("overTime")+"")){
			OrderComplaint orderComplaint = new OrderComplaint();
			orderComplaint.setType("6");
			orderComplaint.setTypes("订单超时");
			orderComplaint.setReason("超时两天");
			try{
				orderComplaint.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(orderComplaintMap.get("dates")+""));
				orderComplaint.setUpdateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(orderComplaintMap.get("dates")+""));
			}catch (Exception e){
				orderComplaint.setCreateDate(new Date());
				orderComplaint.setUpdateDate(new Date());
			}
			orderComplaint.setIsComplaint("1");
			resultList.add(orderComplaint);
		}
		if (null != orderComplaintList){
			resultList.addAll(orderComplaintList);
		}
		resultList = resultList.stream().sorted(Comparator.comparing(OrderComplaint::getUpdateDate).reversed()).collect(Collectors.toList());
		int initCount = orderComplaintService.selectCount(new EntityWrapper<OrderComplaint>().eq("order_no", order.getOrderNo()).eq("type_", "0"));
		int TosendCount = orderComplaintService.selectCount(new EntityWrapper<OrderComplaint>().eq("order_no", order.getOrderNo()).eq("type_", "1"));
		int AlreadyCount = orderComplaintService.selectCount(new EntityWrapper<OrderComplaint>().eq("order_no", order.getOrderNo()).eq("type_", "2"));
		OrderEvaluation orderEvaluation = orderEvaluationService.selectOne(new EntityWrapper<OrderEvaluation>().eq("order_id", orderId));
		VoucherMember voucherMember = voucherMemberService.selectOne(new EntityWrapper<VoucherMember>().eq("order_no", order.getOrderNo()));
		resultMap.put("initComplaint", "催派"+initCount);
		resultMap.put("TosendCount", "催接"+TosendCount);
		resultMap.put("AlreadyCount", "催收"+AlreadyCount);
		resultMap.put("order",order);
		resultMap.put("OrderEvaluation",orderEvaluation);
		resultMap.put("paymentNo",paymentNo);
		resultMap.put("recyclers",recyclers);
		resultMap.put("orderPicList",orderPicList);
		resultMap.put("orderPicAchList",orderPicAchList);
		resultMap.put("orderItemList",orderItemList);
		resultMap.put("company",company);
		resultMap.put("category",category);
		resultMap.put("orderItemAchList",orderItemAchList);
		resultMap.put("orderComplaintList",resultList);
		resultMap.put("voucherMember",voucherMember);
		return  resultMap;
	}
	@Override
	public Map<String, Object> getOrderDetailByOrderId(Integer orderId) {
		Map<String, Object> resultMap = new HashMap<>();
		Order order = this.selectById(orderId);
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
					}
				}
			}
		}
		List<Map<String, Object>> houseOrderItem = new ArrayList<>();
		List<Map<String, Object>> houseOrderAchItem = new ArrayList<>();
		if (order.getTitle() == Order.TitleType.HOUSEHOLD||order.getTitle() == Order.TitleType.FIVEKG ||order.getTitle() == Order.TitleType.SMALLDIGITAL || order.getTitle() == Order.TitleType.IOTORDER) {
			OrderBean orderBean = new OrderBean();
			orderBean.setId(orderId);
			orderBean.setTitle(order.getTitle().toString());
			orderBean.setStatus(order.getStatus().getValue().toString());
			orderBean.setIsCash(order.getIsCash());
			if (OrderType.COMPLETE.getValue().equals(order.getStatus().getValue())) {
				houseOrderAchItem = (List<Map<String, Object>>) this.createPriceAmount4PC(orderBean).get("listMapObject");
			}
			houseOrderItem =  (List<Map<String, Object>>)this.getInitDetail(orderId).get("listMapObject");
		}
		//回收人员填的图片
		List<OrderPicAch> orderPicAchList = orderPicAchService.selectbyOrderId(orderId);
		//查询订单表的用户填的图片
		List<OrderPic> orderPicList = orderPicService.selectbyOrderId(orderId);
		//查询家电订单表未完成明细表
		List<OrderItem> orderApplianceItemList = orderItemService.selectByOrderId(orderId);
		//查询相关回收人员
		Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
		//订单评价
		OrderEvaluation orderEvaluation = orderEvaluationService.selectOne(new EntityWrapper<OrderEvaluation>().eq("order_id", order.getId()));
		//公司信息
		Company company = companyService.selectById(order.getCompanyId());
		//查询申请取消理由
		OrderCancleExamine orderCancleExamine = orderCancleExamineService.selectOne(new EntityWrapper<OrderCancleExamine>().eq("order_no", order.getOrderNo()));
		int initCount = orderComplaintService.selectCount(new EntityWrapper<OrderComplaint>().eq("order_no", order.getOrderNo()).eq("type_", "0"));
		int TosendCount = orderComplaintService.selectCount(new EntityWrapper<OrderComplaint>().eq("order_no", order.getOrderNo()).eq("type_", "1"));
		int AlreadyCount = orderComplaintService.selectCount(new EntityWrapper<OrderComplaint>().eq("order_no", order.getOrderNo()).eq("type_", "2"));
		VoucherMember voucherMember = voucherMemberService.selectOne(new EntityWrapper<VoucherMember>().eq("order_no", order.getOrderNo()));
		Payment payment = paymentService.selectOne(new EntityWrapper<Payment>().eq("order_sn", order.getOrderNo()).eq("status_", "2"));
		int arrivalTimeCount = arrivalTimeLogService.selectCount(new EntityWrapper<ArrivalTimeLog>().eq("order_id", order.getId()));
		resultMap.put("initComplaint", "催派"+initCount);
		resultMap.put("TosendCount", "催接"+TosendCount);
		resultMap.put("AlreadyCount", "催收"+AlreadyCount);
		resultMap.put("order",order);
		resultMap.put("houseOrderItem",houseOrderItem);
		resultMap.put("houseOrderAchItem",houseOrderAchItem);
		resultMap.put("orderPicAchList",orderPicAchList);
		resultMap.put("orderPicList",orderPicList);
		resultMap.put("orderApplianceItemList",orderApplianceItemList);
		resultMap.put("recyclers",recyclers);
		resultMap.put("orderEvaluation",orderEvaluation);
		resultMap.put("company",company);
		resultMap.put("orderCancleExamine",orderCancleExamine);
		resultMap.put("voucherMember",voucherMember);
		resultMap.put("payment",payment);
		resultMap.put("arrivalTimeCount",arrivalTimeCount);
		return resultMap;
	}
	@Autowired
	private ArrivalTimeLogService arrivalTimeLogService;
	@Override
	public Map<String, Object> getOrderComplaint(String orderNo) {
		return orderMapper.getOrderComplaint(orderNo);
	}
	@Override
	public List<Map<String, Object>> selectHouseAmount() {
		return orderMapper.selectHouseAmount();
	}

	@Override
    public boolean isTenGreen(Order order){
		if ("1".equals(order.getIsCash())){
			return true;
		}
		List<OrderItem> orderItems = orderItemService.selectList(new EntityWrapper<OrderItem>().eq("order_id", order.getId()));
		List<OrderItem> collect = orderItems.stream().filter(orderItem -> orderItem.getPrice() > 0).collect(Collectors.toList());
		if(null==collect||collect.isEmpty()){
			return true;
		}
		return false;
	}

	/**
	 * 导出企业的完成订单的Excel
	 * @param companyId
	 * @param startTime
	 * @param endTime
	 * @return
	 * added by michael_wang
	 */
	@Override
	public List<Map<String,Object>> orderDetail4HorseHold(Integer companyId,String startTime,String endTime,String recyclerName,String title){
		return orderMapper.orderDetail4HorseHold(companyId,startTime,endTime,recyclerName,title);
	}

    @Override
    public List<Order> getOrderListss(Integer companyId, String orderNo, String linkMan, String recyclerName, String startTime, String endTime, String title) {
        return orderMapper.getOrderListss(companyId,orderNo,linkMan,recyclerName,startTime,endTime,title);
    }

    @Override
    public Map<String, Object> select1Or4Map(Long id) {
        return orderMapper.select1Or4Map(id);
    }

    @Override
    public List<Map<String, Object>> select2Or3Map(Long id) {
        return orderMapper.select2Or3Map(id);
    }

    @Override
    public Object getReyclersServiceAbility(OrderBean orderBean, Integer companyId){
		PageBean pagebean = orderBean.getPagebean();
		Integer pageNumber = null!=pagebean ?pagebean.getPageNumber():1;
		Integer pageSize = null!=pagebean ?pagebean.getPageSize():9999;
		Integer pageStart = (pageNumber-1)*pageSize;
		List<Map<String, Object>> reyclersServiceAbilityList = orderMapper.getReyclersServiceAbility(companyId,orderBean.getRecyclerName(),orderBean.getMobile(),orderBean.getStartTime(),orderBean.getEndTime(),orderBean.getIsBig(),orderBean.getIsOverTime(),pageStart,pageSize);
		Integer	count = orderMapper.getReyclersServiceAbilityCount(companyId, orderBean.getRecyclerName(), orderBean.getMobile(), orderBean.getStartTime(), orderBean.getEndTime(), orderBean.getIsBig(), orderBean.getIsOverTime());
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("recyclerList",reyclersServiceAbilityList);
		resultMap.put("count",count);
		resultMap.put("pageNumber",pageNumber);
		return resultMap;
	}
	@Override
    public  Object overTimeOrderListByReyclersId(OrderBean orderBean, Integer companyId){
		PageBean pagebean = orderBean.getPagebean();
		Integer pageNumber = null!=pagebean ?pagebean.getPageNumber():1;
		Integer pageSize = null!=pagebean ?pagebean.getPageSize():9999;
		Integer pageStart = (pageNumber-1)*pageSize;
		List<Map<String, Object>> overTimeOrderList = orderMapper.overTimeOrderListByReyclersId(companyId,orderBean.getRecyclerId(), orderBean.getStartTime(), orderBean.getEndTime(), orderBean.getIsBig(), orderBean.getIsOverTime(), pageStart, pageSize);
		Integer count = orderMapper.overTimeOrderListCount(companyId,orderBean.getRecyclerId(), orderBean.getStartTime(), orderBean.getEndTime(), orderBean.getIsBig(), orderBean.getIsOverTime());
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("overTimeOrderList",overTimeOrderList);
		resultMap.put("count",count);
		resultMap.put("pageNumber",pageNumber);
		return resultMap;
	}
	@Override
    public  List<Map<String,Object>>  getRecyclerOrderList(OrderBean orderBean){
		return  orderMapper.getRecyclerOrderList(orderBean.getCompanyId(), orderBean.getRecyclerName(), orderBean.getMobile(),orderBean.getStartTime(),orderBean.getEndTime(), orderBean.getIsBig(), orderBean.getIsOverTime());
	}
	@Transactional
	@Override
	public Object cancleOrderExamine(OrderBean orderBean){
		Order order = this.selectOne(new EntityWrapper<Order>().eq("order_no", orderBean.getOrderNo()));
		if (null == order){
			throw new ApiException("输入的订单号查找不到订单");
		}
		/*if(!(OrderType.INIT+"").equals(order.getStatus()+"")){
			throw new ApiException("该订单的不是初始状态，目前不可被申请取消");
		}*/
		OrderCancleExamine orderCancleExamine = orderCancleExamineService.selectOne(new EntityWrapper<OrderCancleExamine>().eq("order_no",orderBean.getOrderNo()));
		if (null == orderCancleExamine){
			orderCancleExamine = new OrderCancleExamine();
		}
        //新增操作流水日志
        OrderOperate orderOperate = new OrderOperate();
        orderOperate.setOrderNo(orderBean.getOrderNo());
        orderOperate.setOperateLog("申请取消订单");
        orderOperate.setReason(orderBean.getCancelReason());
        Company company = companyService.selectById(BusinessUtils.getCompanyAccount().getCompanyId());
        orderOperate.setOperatorMan(company.getName());

		orderCancleExamine.setOrderNo(orderBean.getOrderNo());
		orderCancleExamine.setCancleReason(orderBean.getCancelReason());
		orderCancleExamine.setStatus("0");
		orderCancleExamineService.insertOrUpdate(orderCancleExamine);
        orderOperateService.insert(orderOperate);
		return "操作成功";
	}
	@Override
	public Map<String,Object> getOrderCancleExamineList(OrderBean orderBean){
		PageBean pagebean = orderBean.getPagebean();
		Integer pageNumber = null!=pagebean ?pagebean.getPageNumber():1;
		Integer pageSize = null!=pagebean ?pagebean.getPageSize():9999;
		Integer pageStart = (pageNumber-1)*pageSize;
		List<Map<String,Object>> getOrderCancleExamineList = orderMapper.getOrderCancleExamineList(orderBean.getCompanyId(),orderBean.getOrderNo(),orderBean.getStartTime(),orderBean.getEndTime(),pageStart,pageSize);
		Integer examineCount = orderMapper.getOrderCancleExamineCount(orderBean.getCompanyId(), orderBean.getOrderNo(), orderBean.getStartTime(), orderBean.getEndTime());
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("overTimeOrderList",getOrderCancleExamineList);
		resultMap.put("count",examineCount);
		resultMap.put("pageNumber",pageNumber);
		return resultMap;
	}
	@Override
    @Transactional
	public String agreeExamineOdrerStatus(OrderBean orderbean,MqttClient mqtt4PushOrder){
		Order order = this.selectById(orderbean.getId());
		if((OrderType.COMPLETE+"").equals(order.getStatus()+"")||(OrderType.CANCEL+"").equals(order.getStatus()+"")){
			throw new ApiException("该订单的状态，目前不可被申请取消");
		}
		OrderCancleExamine orderCancleExamine = orderCancleExamineService.selectOne(new EntityWrapper<OrderCancleExamine>().eq("order_no", order.getOrderNo()));

        //新增操作流水日志
        OrderOperate orderOperate = new OrderOperate();
        orderOperate.setOrderNo(order.getOrderNo());

		if ("1".equals(orderbean.getStatus())){
			this.updateOrderByBusiness(orderbean.getId(),"REJECTED",orderbean.getCancelReason(),null,mqtt4PushOrder);
            orderOperate.setOperateLog("同意申请取消订单");
            //如果是咸鱼的单子，就通知咸鱼订单取消
            if (StringUtils.isNotBlank(order.getTaobaoNo())){
                QiMemOrder qiMemOrder = new QiMemOrder();
                qiMemOrder.setBizOrderId(order.getOrderNo());
                qiMemOrder.setOrderStatus(QiMemOrder.QI_MEM_ORDER_CANCEL);
                qiMemOrder.setReason(orderbean.getCancelReason());
                boolean bool = qiMemService.updateQiMemOrder(qiMemOrder);
            }
		}else{
            orderOperate.setOperateLog("拒绝申请取消订单");
        }
        orderOperate.setReason("/");
        orderOperate.setOperatorMan("平台");
        orderOperateService.insert(orderOperate);

		orderCancleExamine.setUpdateDate(new Date());
		orderCancleExamine.setStatus(orderbean.getStatus());
		orderCancleExamineService.updateById(orderCancleExamine);
		return "操作成功";
	}
	@Override
    @Transactional
	public void  updateOrderCompany(String streetId,String companyId,String title){
		orderMapper.updateOrderCompany(streetId,companyId,title);
	}

	@Override
	public Map<String, Object> receivingMoney(OrderBean orderbean) {
		Map<String, Object> returnMap = new HashMap<>();
		if (StringUtils.isEmpty(orderbean.getOrderNo()) || StringUtils.isEmpty(orderbean.getIsDelivery())){
			returnMap.put("msg", "参数错误");
		}
		RedisUtil.SaveOrGetFromRedis saveOrGetFromRedis = new RedisUtil.SaveOrGetFromRedis();
		Object fromRedis = saveOrGetFromRedis.getFromRedis("receive:" + orderbean.getOrderNo(), jedisPool);
		Order order = orderService.selectOne(new EntityWrapper<Order>().eq("del_flag", 0).eq("order_no", orderbean.getOrderNo()).eq("status_", OrderStatusType.ALREADY.getValue()));
		if (null == fromRedis || null == order){
			//redis中未查到当前订单，领奖失败
			returnMap.put("msg", "未达到领奖条件");
		}else if (!orderbean.getIsDelivery().equals(fromRedis)){
			//随机数不匹配，领奖失败
			returnMap.put("msg", "随机数异常");
		}else {
			String price = randomPrice();
			Payment payment = paymentService.selectOne(new EntityWrapper<Payment>().eq("del_flag", 0).eq("order_sn", fromRedis));
			if (null == payment){
				payment = new Payment();
			}else {
				//这里不判断也没关系，再次用相同的单号交易会出现异常（之后的定时任务会处理当前交易完成但是状态异常的订单）
				returnMap.put("msg", "红包已领取");
				return returnMap;
			}
			payment.setAliUserId(orderbean.getAliUserId());
			payment.setPrice(new BigDecimal(price));
			payment.setOrderSn(fromRedis.toString());
			payment.setSellerId(orderbean.getAliUserId());
			payment.setPayType(Payment.PayType.CASH_BAG);
			//产生随机金额，发送红包
            AlipayFundTransUniTransferResponse alipayFundTransToaccountTransferResponse = paymentService.receivingMoneyTransfer(orderbean.getAliUserId(), price, fromRedis.toString());
			if ("Success".equals(alipayFundTransToaccountTransferResponse.getMsg())) {
				//交易完成(状态设置为已转账)
				payment.setStatus(STATUS_TRANSFER);
				payment.setIsSuccess("1");
				payment.setTradeNo(alipayFundTransToaccountTransferResponse.getOrderId());
			} else {
				//交易失败(状态设置为未转账)
				payment.setStatus(STATUS_PAYED);
				payment.setIsSuccess("0");
				payment.setRemarks(alipayFundTransToaccountTransferResponse.getSubMsg());
				//和收呗转账区分开（转账失败不设置tradeNo）下个版本增加转账失败后的定时任务
//                            payment.setTradeNo(alipayFundTransToaccountTransferResponse.getOrderId());
			}
			paymentService.insertOrUpdate(payment);
			returnMap.put("msg", "领取成功");
			returnMap.put("price", price);
		}
		return returnMap;
	}

	/**【业务数据总览】(分页)
     * @author sgmark@aliyun.com
     * @date 2019/9/9 0009
     * @param
     * @return
     */
    @Override
    public Map<String, Object> getAllOrderMapOverview(BOrderBean orderBean) {
        Map<String, Object> returnMap = new HashMap<>();
        //分页展示
        Integer pageNumber = null!=orderBean.getPagebean() ?orderBean.getPagebean().getPageNumber():1;
        Integer pageSize = null!=orderBean.getPagebean() ?orderBean.getPagebean().getPageSize():10;
        Integer pageStart = (pageNumber-1)*pageSize;
        List<Map<String, Object>> returnMapList = orderMapper.getAllOrderMapOverview(orderBean.getComplaintType(),orderBean.getStatus(), orderBean.getCompanyId(), orderBean.getCategoryType(), pageStart, pageSize, orderBean.getStartTime(), orderBean.getEndTime());
		Integer count = orderMapper.getAllOrderMapOverviewCount(orderBean.getComplaintType(),orderBean.getStatus(), orderBean.getCompanyId(), orderBean.getCategoryType(), orderBean.getStartTime(), orderBean.getEndTime());
        returnMap.put("count", count);
        returnMap.put("list", returnMapList);
        return returnMap;
    }

    /**	已完成订单
     * @author sgmark@aliyun.com
     * @date 2019/9/10 0010
     * @param
     * @return
     */
    @Override
    public List<Map<String, Object>> outAchOrderListOverview(BOrderBean bOrderBean) {
        if (StringUtils.isEmpty(bOrderBean.getStatus())){
//            bOrderBean.setStatus("3");
        }else if (StringUtils.isNotEmpty(bOrderBean.getStatus()) && !"3".equals(bOrderBean.getStatus())){
            return new ArrayList<>();
        }
        return orderMapper.outAchOrderListOverview(bOrderBean.getComplaintType(),bOrderBean.getCompanyId()+"", bOrderBean.getStatus(), bOrderBean.getCategoryType(), bOrderBean.getStartTime(), bOrderBean.getEndTime());
    }

    /**	非完成状态订单
     * @author sgmark@aliyun.com
     * @date 2019/9/10 0010
     * @param
     * @return
     */
    @Override
    public List<Map<String, Object>> outOtherOrderListOverview(BOrderBean bOrderBean) {
        if (StringUtils.isNotEmpty(bOrderBean.getStatus()) && "3".equals(bOrderBean.getStatus())){
            return new ArrayList<>();
        }else if (StringUtils.isEmpty(bOrderBean.getStatus())){
            //所有非完成订单
//            bOrderBean.setStatus2(Arrays.asList(0,1,2,4,5));
        }
        return orderMapper.outOtherOrderListOverview(bOrderBean.getComplaintType(),bOrderBean.getCompanyId() +"", bOrderBean.getStatus(), bOrderBean.getCategoryType(), bOrderBean.getStartTime(), bOrderBean.getEndTime());
    }

	public String randomPrice(){
		Integer randomInt = new Random().nextInt(10000);
		String price = "";
		switch (randomInt){
			case 0:
				price = "88.88";
				break;
			case 100:
				price = "66.88";
				break;
			case 200:
				price = "28.88";
				break;
			case 300:
				price = "18.88";
				break;
			case 400:
				price = "8.88";
				break;
			case 500:
				price = "5.88";
				break;
			case 600:
				price = "3.88";
				break;
			case 700:
				price = "2.88";
				break;
			case 800:
				price = "1.88";
				break;
			default:
				price = "0.88";
		}
		return price;
	}

	@Override
    @Transactional
	public Object saveOrderReceptionByOrderNo(OrderBean orderbean){
		Order order = this.selectOne(new EntityWrapper<Order>().eq("order_no", orderbean.getOrderNo()));
		int count = orderComplaintService.selectCount(new EntityWrapper<OrderComplaint>().eq("order_no", order.getOrderNo()));
		OrderComplaint orderComplaint = new OrderComplaint();
        OrderOperate orderOperate = new OrderOperate();

		orderComplaint.setOrderNo(orderbean.getOrderNo());
		orderComplaint.setType(orderbean.getType());

        orderOperate.setOrderNo(orderbean.getOrderNo());

		if ("0".equals(orderbean.getType())){
			if(!(OrderType.INIT+"").equals(order.getStatus()+"")){
				throw new ApiException("该订单不是待派单状态");
			}
			orderComplaint.setTypes("催派");
            orderOperate.setOperateLog("催派");
            orderOperate.setReason("/");
		}else if ("1".equals(orderbean.getType())){
			if(!(OrderType.TOSEND+"").equals(order.getStatus()+"")){
				throw new ApiException("该订单不是待接单状态");
			}
			orderComplaint.setTypes("催接");
            orderOperate.setOperateLog("催接");
            orderOperate.setReason("/");
		}else if ("2".equals(orderbean.getType())){
			if(!(OrderType.ALREADY+"").equals(order.getStatus()+"")){
				throw new ApiException("该订单不是待完成状态");
			}
			orderComplaint.setTypes("催收");
            orderOperate.setOperateLog("催收");
            orderOperate.setReason("/");
		}else if ("4".equals(orderbean.getType())){
			orderComplaint.setTypes("主动客诉");
            orderOperate.setOperateLog("投诉");
            orderOperate.setReason(orderbean.getReason());
		}
		orderComplaint.setReason(orderbean.getReason());
		//判断订单是否超时
		Map<String, Object> orderComplaintMap = orderMapper.getOrderComplaint(order.getOrderNo());
		if (count>0||"4".equals(orderbean.getType())||(null != orderComplaintMap && 2880<Integer.parseInt(orderComplaintMap.get("overTime")+""))){
			order.setComplaintType("3");
			orderComplaint.setIsComplaint("1");
		}else {
			order.setComplaintType(orderbean.getType());
			orderComplaint.setIsComplaint("0");
		}
        orderOperate.setOperatorMan("平台");

        orderOperateService.insert(orderOperate);
		orderComplaintService.insert(orderComplaint);
		this.updateById(order);
		return "操作成功";
	}

	@Override
    public Object getOrderComplaintDetail(String orderNo){
		Order order = this.selectOne(new EntityWrapper<Order>().eq("order_no", orderNo));
		if (null==order){
			throw new ApiException("传入的订单Id有误");
		}
		List<OrderComplaint> orderComplaintList = orderComplaintService.selectList(new EntityWrapper<OrderComplaint>().eq("order_no", order.getOrderNo()).orderBy("create_date",false));
		Map<String, Object> orderComplaintMap = orderMapper.getOrderComplaint(order.getOrderNo());
		List<OrderComplaint> resultList = new ArrayList<>();
		if (null != orderComplaintMap && 2880<Integer.parseInt(orderComplaintMap.get("overTime")+"")){
			OrderComplaint orderComplaint = new OrderComplaint();
			orderComplaint.setType("6");
			orderComplaint.setTypes("订单超时");
			orderComplaint.setReason("超时两天");
			try{
				orderComplaint.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(orderComplaintMap.get("dates")+""));
				orderComplaint.setUpdateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(orderComplaintMap.get("dates")+""));
			}catch (Exception e){
				orderComplaint.setCreateDate(new Date());
				orderComplaint.setUpdateDate(new Date());
			}
			orderComplaint.setIsComplaint("1");
			resultList.add(orderComplaint);
		}
		if (null != orderComplaintList){
			resultList.addAll(orderComplaintList);
		}
		resultList = resultList.stream().sorted(Comparator.comparing(OrderComplaint::getUpdateDate).reversed()).collect(Collectors.toList());
		return  resultList;
	}


    @Override
	@Transactional
	public Object addOrderComplaintBack(String orderNo,String type,String complaintBack){
    	if (StringUtils.isBlank(orderNo)){
    		throw new ApiException("该客诉无法进行反馈");
		}
        if (StringUtils.isBlank(complaintBack)){
            throw new ApiException("请输入反馈内容");
        }
		OrderComplaint orderComplaint = orderComplaintService.selectOne(new EntityWrapper<OrderComplaint>().eq("order_no",orderNo).eq("type_",type).orderBy("create_date",false));
		if (null == orderComplaint){
			throw new ApiException("未查到该客诉信息，无法进行反馈");
		}
        OrderOperate orderOperate = orderOperateService.selectOne(new EntityWrapper<OrderOperate>().eq("operate_log",orderComplaint.getTypes()).orderBy("create_date",false));
		if(orderOperate != null){
            orderOperate.setReason(complaintBack);
            orderOperateService.updateById(orderOperate);
        }
		orderComplaint.setComplaintBack(complaintBack);
		orderComplaintService.updateById(orderComplaint);
		return "操作成功";
    }
	/**
	 * 订单中使用优惠券详情
	 * @author: sgmark@aliyun.com
	 * @Date: 2019/10/31 0031
	 * @Param:
	 * @return:
	 */
    public Map<String, Object>  voucherInfo(Order order){
		Map<String, Object> resultMap = new HashMap<>();
		//查询订单是否使用优惠券（根据订单号）
		VoucherMember voucherMembers = voucherMemberService.selectOne(new EntityWrapper<VoucherMember>().eq("order_no", order.getOrderNo()).last(" LIMIT 1"));
		if (null != voucherMembers){
			resultMap.put("useVoucher", "Y");
			resultMap.put("voucherName", voucherMembers.getVoucherName());
			if (Order.TitleType.BIGTHING.getValue().equals(order.getTitle().getValue())){
				//完成订单
				if (OrderType.COMPLETE.getValue().equals(order.getStatus().getValue())){
					resultMap.put("discountPrice", order.getDiscountPrice());//用户收到或支付的金额
					resultMap.put("achPrice", order.getAchPrice());//回收员收到或支付的金额
					try {
						resultMap.put("money", order.getAchPrice().subtract(order.getDiscountPrice()).abs());//优惠金额
					}catch (Exception e){
						resultMap.put("money", voucherMembers.getMoney());
					}

				}else {
					//进行中订单展示的优惠券抵扣价格（优惠金额）也称作平台支付金额
					resultMap.put("money", voucherMembers.getMoney());
				}
			}else {
				//大件以外的订单
				if (OrderType.COMPLETE.getValue().equals(order.getStatus().getValue())){
					resultMap.put("discountPrice", order.getDiscountPrice());//用户收到或支付的金额
					resultMap.put("achPrice", order.getAchPrice());//回收员收到或支付的金额
					try {
						resultMap.put("money", order.getDiscountPrice().subtract(order.getAchPrice()).abs());//优惠金额
					}catch (Exception e){
						resultMap.put("money", voucherMembers.getMoney());
					}
				}else {
					//进行中订单展示的优惠券抵扣价格（优惠金额）也称作平台支付金额
					resultMap.put("money", voucherMembers.getMoney());
				}
			}
            order.setPriceAch(order.getDiscountPrice().add(order.getCommissionsPrice()));
			resultMap.put("voucherMembers", voucherMembers);//整个券码
		}else {
            order.setPriceAch(order.getAchPrice().add(order.getCommissionsPrice()));
			resultMap.put("useVoucher", "未使用优惠券");
		}
		return resultMap;
	}
	@Override
	public Map<String, Object> getOrderAchItemDatail(OrderBean orderBean){
		//未完成订单的数据

		Order order = this.selectById(orderBean.getId());
		List<OrderItemAch> orderItemAches = orderItemAchService.selectByOrderId(orderBean.getId());
		List<ComCatePrice> comCatePrices = orderItemService.selectCateAchName(orderBean.getId());
		Map<String, String> map = null;
		float price = 0l;
		List<Map<String, String>> listMap = null;
		Map<String, Object> mapListMap = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> listMapObject = new ArrayList<>();
		//首先取得分类name, 去重
		for (ComCatePrice name : comCatePrices) {
			mapListMap = new HashMap<>();
			price = 0l;
			listMap = new ArrayList<>();
			for (OrderItemAch list : orderItemAches) {
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
		resultMap.put("order", order);
		return resultMap;
	}

	@Override
	public List<Long> getOrderListByTitleStatus(String startTime,String endTime,String title,String status){
        return orderMapper.getOrderListByTitleStatus(startTime,endTime,title,status);
    }
    @Override
    public List<Long> getOrderListByCity(String startTime,String endTime,String title){
        return orderMapper.getOrderListByCity(startTime,endTime,title);
    }
    @Override
    public List<Map<String,Object>> getOrderCancelByCompany(String startTime,String endTime){
        return orderMapper.getOrderCancelByCompany(startTime,endTime);
    }
    @Override
    public List<Long> outOrderListByRecycler(String startTime,String endTime,String isOverTime){
        return orderMapper.outOrderListByRecycler(startTime,endTime,isOverTime);
    }
    @Override
    public List<Long> outOrderListGroupCompany(String startTime,String endTime){
        return orderMapper.outOrderListGroupCompany(startTime,endTime);
    }
    @Override
    public Integer getManyOrderListByDate(String startTime,String endTime,String title,String status){
        return orderMapper.getManyOrderListByDate(startTime,endTime,title,status);
    }
    @Override
    public Integer getOrderListByDate(String startTime,String endTime,String title,String status){
        EntityWrapper<Order> wrapper = new EntityWrapper<>();
        wrapper.eq("title",title);
        wrapper.eq("status_",status);
        wrapper.ge("create_date", startTime+" 00:00:01");
        wrapper.le("create_date", endTime+" 23:59:59");
        return this.selectCount(wrapper);
    }

    /**
     * iot 回收人员清运订单列表
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/11 0011
     * @Param:
     * @return:
     */
    @Override
    public Map<String, Object> selectIotRecList(Long recId, String status, PageBean pageBean) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("count", orderMapper.selectCount(new EntityWrapper<Order>().eq("del_flag",0).eq("recycler_id", recId).eq("status_", status).eq("title", Order.TitleType.IOTCLEANORDER)));
        returnMap.put("returnList", orderMapper.selectIotRecList(recId+"", status, (pageBean.getPageNumber()-1)*pageBean.getPageSize(), pageBean.getPageSize()));
        return returnMap;
    }

    @Override
    public Object saveXyOrder(String message) {

        Map<String, Object> objectMap = (Map<String, Object>) JSONObject.fromObject(message);
        String nickName = objectMap.get("seller_nick")+"";
        String sellerRealName = objectMap.get("seller_real_name")+"";
        String address = objectMap.get("seller_address")+"";
        String aliUserId = objectMap.get("seller_alipay_user_id")+"";
        String tel = objectMap.get("seller_phone")+"";
        String city = objectMap.get("city")+"";
        String quoteId = objectMap.get("apprize_id")+"";
        String shipTime = objectMap.get("ship_time")+"";
        String closeReason = objectMap.get("close_reason")+"";
        String taobaoNo = objectMap.get("biz_order_id")+"";
        Order order = this.selectOne(new EntityWrapper<Order>().eq("order_no", taobaoNo));
        if (null != order){
            if(StringUtils.isNotBlank(closeReason)&&!"null".equals(closeReason)){
                order.setCancelTime(new Date());
                order.setCancelReason(closeReason);
                order.setStatus(OrderType.CANCEL);
                orderService.updateById(order);
                return "操作成功";
            }else {
                throw new ApiException("该订单已存在");
            }
        }
        Member member = memberService.selectMemberByAliUserId(aliUserId);
        if (null == member){
            member = new Member();
            member.setName(sellerRealName);
            member.setMobile(tel);
            member.setCity(city);
            member.setLinkName(nickName);
            member.setAddress(address);
            member.setAliUserId(aliUserId);
            memberService.insert(member);
        }
        order = new Order();
        Integer companyId = null;
        Integer areaId = 0;
        Integer streetId = 0;
        try {
            String location = mapService.getLocation(address);
            AmapResult amap = mapService.getAmap(location);
            String towncode = amap.getTowncode();
            Area area = areaService.selectOne(new EntityWrapper<Area>().eq("code_", towncode));
            companyId = companyStreetHouseService.selectStreetHouseCompanyId(area.getId().intValue());
            streetId = area.getId().intValue();
            areaId = area.getParentId();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            order.setArrivalTime(new SimpleDateFormat("yyyy-MM-dd").parse(shipTime.split(" ")[0]));
            int i = Integer.parseInt(new SimpleDateFormat("HH").format(new Date()));
            if (i < 12) {
                order.setArrivalPeriod("am");
            } else {
                order.setArrivalPeriod("pm");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        order.setCompanyId(companyId);
        order.setMemberId(member.getId().intValue());
        order.setStatus(OrderType.INIT);
        order.setOrderNo(taobaoNo);
        order.setTaobaoNo(quoteId);
        order.setAreaId(areaId);
        order.setStreetId(streetId);
        order.setAddress(address);
        order.setPrice(BigDecimal.ZERO);
        order.setTel(tel);
        order.setLinkMan(sellerRealName);
        order.setCategoryId(26);
        order.setCategoryParentIds("25");
        order.setIsEvaluated("0");
        order.setLevel("0");
        order.setAliUserId(aliUserId);
        order.setIsCash("1");
        order.setIsMysl("1");
        order.setUnit("kg");
        order.setTitle(Order.TitleType.HOUSEHOLD);
        List<XyCategoryOrder> xyCategoryOrderList = xyCategoryOrderService.selectList(new EntityWrapper<XyCategoryOrder>().eq("quote_id", quoteId).eq("parent_id","1"));
        XyCategoryOrder amountCategoryOrder = xyCategoryOrderService.selectOne(new EntityWrapper<XyCategoryOrder>().eq("quote_id", quoteId).eq("parent_id","2"));
        order.setQty(amountCategoryOrder.getCategoryId());
        orderService.insert(order);
        Integer orderId = order.getId().intValue();
        xyCategoryOrderList.stream().forEach(xyCategoryOrder -> {
                Category category = categoryService.selectById(xyCategoryOrder.getCategoryId());
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(orderId);
                orderItem.setCategoryId(category.getId().intValue());
                orderItem.setCategoryName(category.getName());
                orderItem.setParentId(category.getId().intValue());
                orderItem.setParentIds(category.getId()+"_");
                orderItem.setParentName(category.getName());
                orderItem.setAmount(amountCategoryOrder.getCategoryId());
                orderItem.setUnit(category.getUnit());
                orderItem.setPrice(0);
                orderItemService.insert(orderItem);
        });
        return "操作成功";
    }
    @Override
    public Object sendXyOrderByCompanyId(OrderBean orderBean){
        if (null==orderBean.getOrderId()||null==orderBean.getCompanyId()){
            throw new ApiException("订单id或公司id不能为空");
        }
        Order order = this.selectById(orderBean.getOrderId());
        if (null==order){
            throw new ApiException("订单不存在");
        }
        if (null!=order.getCompanyId()){
            throw new ApiException("订单已存在公司");
        }
        order.setCompanyId(orderBean.getCompanyId());
        this.updateById(order);
        return "操作成功";
    }

    @Override
    public Map<String, Object> dsddOrderDetail(Integer orderId) {
        Map<String, Object> map = new HashMap<>();
        Order order = this.selectById(orderId);
        if (null == order){
            throw new ApiException("查询不到该订单信息");
        }
        Double greenCount = order.getGreenCount();
        Double companyPoint = order.getCompanyPoint();
        Double sumPoint = greenCount + companyPoint;
        Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
        DsddRecyclePosition dsddRecyclePosition = dsddRecyclePositionService.selectById(order.getNetId());
        DsddPayment dsddPayment = dsddPaymentService.selectOne(new EntityWrapper<DsddPayment>().eq("out_trade_no", order.getOrderNo()));
        String paymentNo = dsddPayment!=null?dsddPayment.getTradeNo():"/";
        List<BusinessOrderItemBean> categoryInfoList = orderMapper.getCategoryInfoByOrderId(String.valueOf(orderId), null);

        map.put("order", order);
        map.put("recyclers", recyclers);
        map.put("categoryInfo", categoryInfoList);
        map.put("dsddRecyclePosition", dsddRecyclePosition);
        map.put("payment",dsddPayment);
        map.put("paymentNo",paymentNo);
        map.put("sumPoint", sumPoint);
        return map;
    }

    @Override
    public Object getOrderDetailPrice(Integer id) {

        Order order = this.selectOne(new EntityWrapper<Order>().eq("id", id));
        if(order==null) {
               throw new RuntimeException("该订单不存在");
        }
        if(OrderType.COMPLETE != (order.getStatus())) {
              throw new RuntimeException("该订单不是完成状态");
        }
            Map<String, Object> resultMap = new HashMap<>();
            if (order.getTitle() == Order.TitleType.HOUSEHOLD||order.getTitle() == Order.TitleType.FIVEKG || order.getTitle() == Order.TitleType.SMALLDIGITAL || order.getTitle() == Order.TitleType.IOTORDER){
                List<Map<String, Object>> list = orderMapper.getCategoryPriceList(id);
                if (list.size() > 0) {
                    for (Map map : list) {
                        BigDecimal price = new BigDecimal(String.valueOf(map.get("amount"))) ;
                        BigDecimal platformPrice = new BigDecimal(String.valueOf(map.get("platformPrice")));
                        BigDecimal platform=price.multiply(platformPrice);
                        BigDecimal commission = platform.setScale(2, RoundingMode.DOWN);
                        map.put("price",String.valueOf(commission));
                    }
                }
                resultMap.put("listReturn", list);
                resultMap.put("sumPrice", order.getCommissionsPrice());
            }else{
                if(order.getCategoryId()!=null){
                    Category category = categoryService.selectById(order.getCategoryId());
                    Category category1 = categoryService.selectById(category.getParentId());
                    List<Map<String, Object>> list = new ArrayList<>();
                    Map<String, Object> map = new HashMap<>();
                    map.put("parentName",category1.getName() );
                    map.put("categoryName",category.getName());
                    map.put("amount","1.00" );
                    map.put("unit",category.getUnit());
                    map.put("price",order.getCommissionsPrice() );
                    list.add(map);
                    resultMap.put("listReturn",list);
                    resultMap.put("sumPrice", order.getCommissionsPrice());
                }
            }
            return resultMap;
    }

    @Override
    @Transactional
    public Object uploadCategoryByAoTu(String aliUserId, String equipmentCode, String categoryName, Double rubbishWeight) {
        Map<String,Object> resultMap = new HashMap<>();
        Member member = memberService.selectMemberByAliUserId(aliUserId);
        Order order = new Order();
        order.setStatus(OrderType.COMPLETE);
        //随机生成订单号
        String orderNo = "IOT"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);
        order.setOrderNo(orderNo);
        order.setTel(member.getMobile() == null ? "" : member.getMobile());
        order.setPrice(BigDecimal.ZERO);
        order.setUnit("kg");
        order.setQty(9999);
        order.setCompanyId(1);
        order.setCompleteDate(new Date());
        order.setIsEvaluated("0");
        order.setLevel("0");
        order.setLinkMan(member.getLinkName());
        order.setAliUserId(member.getAliUserId());
        order.setIsCash("0");
        order.setIsDistributed("0");
        order.setAreaId(0);
        order.setIsMysl("1");
        order.setTitle(Order.TitleType.IOTORDER);
        order.setAchPrice(BigDecimal.ZERO);
        order.setIsScan("0");
        order.setCompleteDate(new Date());
        order.setIotEquipmentCode(equipmentCode);
        order.setOrderFrom("3");
        orderService.insert(order);
        //得到用户的垃圾总量
        Category category = categoryService.selectOne(new EntityWrapper<Category>().eq("name_",categoryName));
        Double amount = 0.0;
        if (null==category){
            resultMap.put("respCode","0");
            resultMap.put("respInfo",0);
        }else {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId().intValue());
            orderItem.setCategoryName(categoryName);
            orderItem.setParentName(categoryName);
            orderItem.setAmount(rubbishWeight);
            orderItem.setCategoryId(category.getId().intValue());
            orderItem.setParentId(category.getParentId());
            orderItem.setParentIds(category.getParentIds());
            orderItem.setUnit(category.getUnit());
            orderItem.setPrice(0);
            orderItemService.insert(orderItem);
            OrderItemAch orderItemAch = new OrderItemAch();
            orderItemAch.setOrderId(order.getId().intValue());
            orderItemAch.setCategoryName(categoryName);
            orderItemAch.setParentName(categoryName);
            orderItemAch.setAmount(rubbishWeight);
            orderItemAch.setCategoryId(category.getId().intValue());
            orderItemAch.setParentId(category.getParentId());
            orderItemAch.setParentIds(category.getParentIds());
            orderItemAch.setUnit(category.getUnit());
            orderItemAch.setPrice(0);
            orderItemAchService.insert(orderItemAch);
            amount = rubbishWeight*category.getGreenCount();
            order.setGreenCount(amount);
            this.updateById(order);
            //给用户增加积分
            this.updateMemberPoint(order.getAliUserId(), order.getOrderNo(), amount,"定点回收物");
            //给用户增加蚂蚁能量
            orderService.myslOrderData(order.getId().toString());
            resultMap.put("respCode","0");
            resultMap.put("respInfo",amount);
        }
        return resultMap;
    }


    @Override
    public Order getByOrderNo(String orderNo) {
        return this.selectOne(new EntityWrapper<Order>().eq("order_no", orderNo).eq("del_flag", "0"));
    }

    @Override
    public List<Order> getOrders() {
        return this.selectList(new EntityWrapper<Order>().eq("status_", "3")
                .eq("del_flag", "0").isNull("mysl_order_id").isNotNull("mysl_param").gt("complete_date","2021-06-01 00:00:00"));
    }
}