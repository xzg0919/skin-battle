package com.tzj.collect.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayOpenAppMiniTemplatemessageSendModel;
import com.alipay.api.internal.util.codec.Base64;
import com.alipay.api.request.AlipayOpenAppMiniTemplatemessageSendRequest;
import com.alipay.api.response.AlipayOpenAppMiniTemplatemessageSendResponse;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import com.tzj.collect.api.commom.constant.MQTTConst;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.common.utils.ToolUtils;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.param.ali.OrderPushBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.common.notify.DingTalkNotify;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.tzj.collect.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @Author 胡方明（12795880@qq.com）
 *
 * 异步通知
 *
 *
 */
@Service
public class AsyncServiceImpl implements AsyncService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RecyclersService recyclersService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderPicAchService orderPicAchService;
    @Autowired
    private OrderEvaluationService orderEvaluationService;
    @Autowired
    private OrderItemAchService orderItemAchService;
    @Resource(name= "mqtt4PushOrder")
    private MqttClient mqtt4PushOrder;
    protected final static Logger log = LoggerFactory.getLogger(AsyncServiceImpl.class);
    @Override
    @Async
    public void notifyDingDingOrderCreate(OrderBean orderBean) {
        StringBuffer message = new StringBuffer();
        message.append("收呗有新订单啦！\r\n");
        message.append("订单号：").append(orderBean.getOrderNo()).append("\r\n");
        message.append("企业：").append(orderBean.getCompanyName());
        ArrayList<String> atMobiles = new ArrayList<>();
        atMobiles.add("15996602085");
        atMobiles.add("13501675585");
        atMobiles.add("15901929893");
        atMobiles.add("17702196566");
        atMobiles.add("17717939653");
        atMobiles.add("13301857305");
        atMobiles.add("13855829796");
        atMobiles.add("15121063188");
        atMobiles.add("13262790702");
        String ddUrl = this.getDingDingUrl(orderBean.getDingDingUrl(), orderBean.getDingDingSing());
        DingTalkNotify.sendTextMessageWithAt(message.toString(), atMobiles, ddUrl);
    }

    @Override
    @Async
    public void notifyDingDingPaymentError(String orderNo, String reason, String dingDingUrl, String dingDingSing, String receiveTel) {
        StringBuffer message = new StringBuffer();
        message.append("转账有异常了！\r\n");
        message.append("订单号：").append(orderNo).append("\r\n");
        message.append("内容：").append(reason);
        ArrayList<String> atMobiles = new ArrayList<>();
        atMobiles.add(receiveTel);
        String ddUrl = this.getDingDingUrl(dingDingUrl, dingDingSing);
        DingTalkNotify.sendTextMessageWithAt(message.toString(), atMobiles, ddUrl);
    }

    public String getDingDingUrl(String ddUrl, String ddSing) {
        if (StringUtils.isBlank(ddSing)) {
            return ddUrl;
        }
        try {
            String secret = ddSing;
            Long timestamp = System.currentTimeMillis();
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            String sing = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
            ddUrl = ddUrl + "&timestamp=" + timestamp + "&sign=" + sing;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ddUrl;
    }

    @Async
    @Override
    public void notifyDingDingOrderCreate(String message, boolean atAll, String dingDingUrl) {
        DingTalkNotify.sendTextMessageWithAtAndAtAll(message, null, atAll, dingDingUrl);
    }

    /**
     *
     * <p>
     * Created on 2017年8月14日</p>
     * <p>
     * Description:[发送接单短信]</p>
     *
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @param freeSignName : 服务商名称
     * @param moblie : 向具体某个手机号发送短信
     * @param temlateCode : SMS_142151759
     * @param recyclerName : 回收人员名称
     * @param phone : 回收人员手机号
     * @param companyName : 企业名称
     */
    @Override
    @Async
    public void sendOrder(final String freeSignName, final String moblie, final String temlateCode,
            final String recyclerName, final String phone, final String companyName) {
        TaobaoClient client = new DefaultTaobaoClient(ToolUtils.url, ToolUtils.appkey, ToolUtils.secret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName(freeSignName);
        req.setSmsParamString("{\"name\":\"" + recyclerName + "\",\"phone\":\"" + phone + "\",\"company\":\"" + companyName + "\"}");
        req.setRecNum(moblie);
        req.setSmsTemplateCode(temlateCode);
        AlibabaAliqinFcSmsNumSendResponse rsp;
        try {
            rsp = client.execute(req);
            System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * <p>
     * Created on 2017年8月14日</p>
     * <p>
     * Description:[发送发货短信]</p>
     *
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @param freeSignName : 服务商名称
     * @param moblie : 向具体某个手机号发送短信
     * @param temlateCode : SMS_142151759
     */
    @Override
    @Async
    public void sendOrderProduct(final String freeSignName, final String moblie, final String temlateCode,
            final String goodsName, final String orderCompany, final String orderNum) {
        TaobaoClient client = new DefaultTaobaoClient(ToolUtils.url, ToolUtils.appkey, ToolUtils.secret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName(freeSignName);
        req.setSmsParamString("{\"goods\":\"" + goodsName + "\",\"order_company\":\"" + orderCompany + "\",\"order_num\":\"" + orderNum + "\"}");
        req.setRecNum(moblie);
        req.setSmsTemplateCode(temlateCode);
        AlibabaAliqinFcSmsNumSendResponse rsp;
        try {
            rsp = client.execute(req);
            System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * <p>
     * Created on 2017年8月14日</p>
     * <p>
     * Description:[发送发货短信]</p>
     *
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @param freeSignName : 服务商名称
     * @param moblie : 向具体某个手机号发送短信
     * @param temlateCode : SMS_142151759
     */
    @Override
    @Async
    public void sendEnterprise(final String freeSignName, final String moblie, final String temlateCode,
            final String code, final String productName) {
        TaobaoClient client = new DefaultTaobaoClient(ToolUtils.url, ToolUtils.appkey, ToolUtils.secret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName(freeSignName);
        req.setSmsParamString("{\"code\":\"" + code + "\",\"productName\":\"" + productName + "\"}");
        req.setRecNum(moblie);
        req.setSmsTemplateCode(temlateCode);
        AlibabaAliqinFcSmsNumSendResponse rsp;
        try {
            rsp = client.execute(req);
            System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Async
    public void sendOpenAppMini(String aliUserId, String formId, String templateId, String page, String value1, String value2, String value3) {
        if (StringUtils.isNotBlank(formId)){
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConst.XappId, AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
            AlipayOpenAppMiniTemplatemessageSendRequest request = new AlipayOpenAppMiniTemplatemessageSendRequest();
            AlipayOpenAppMiniTemplatemessageSendModel model = new AlipayOpenAppMiniTemplatemessageSendModel();
            model.setToUserId(aliUserId);
            model.setFormId(formId);
            model.setUserTemplateId(templateId);
            model.setPage(page);
            model.setData("{\"keyword1\" :{\"value\":\"" + value1 + "\"},\"keyword2\" :{\"value\":\"" + value2 + "\"},\"keyword3\" :{\"value\":\"" + value3 + "\"}}");
            System.out.println(JSON.toJSONString(model));
            request.setBizModel(model);
            AlipayOpenAppMiniTemplatemessageSendResponse response = null;
            try {
                response = alipayClient.execute(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (response.isSuccess()) {
                System.out.println("调用成功");
            } else {
                System.out.println("调用失败");
            }
            System.out.println(response.getBody());
        }
    }
    /**
     * 订单推送【仅针对线上业务订单数据（定时定点订单，IOT订单除外）】
     * @param order
     */
    @Async
    public void pushOrder(Order order) {
        OrderPushBean orderB = new OrderPushBean();
        //用户信息：姓名，电话，地址，支付宝UID
        //服务商（统一回传“上海铸乾信息技术有限公司”）
        //订单类型：废弃家电（一二级类目）/生活垃圾（一二级类目）/大件垃圾（一二级类目）
        orderB.setAliUserId(order.getAliUserId());
        orderB.setCompanyName("上海铸乾信息技术有限公司");
        /**
         * 订单状态
         *  INIT(0), // 待接单
         * 	TOSEND(1), // 已派送
         * 	ALREADY(2), // 已接单
         * 	COMPLETE(3), // 已完成
         * 	CANCEL(4), // 已取消
         *
         *订单类型
         * 	DEFUALT(0),   	 //初始值
         * 	DIGITAL(1),		//家电数码
         * 	HOUSEHOLD(2),	//生活垃圾
         * 	FIVEKG(3),		//5公斤废纺衣物回收
         * 	BIGTHING(4),	//大件垃圾
         */
        orderB.setLinkName(order.getLinkMan());
        orderB.setMobile(order.getTel());
        orderB.setOrderNo(order.getOrderNo());
        orderB.setAddress(order.getAddress());
        orderB.setTitle(order.getTitle().name());
        List<Map<String, String>> upList = new ArrayList<>();
        if(!"HOUSEHOLD".equals(orderB.getTitle())){
            Category category2 = categoryService.selectById(order.getCategoryId());
            Category category1 = categoryService.selectById(category2.getParentId());
            Map<String, String> map = new HashMap<>();
            map.put("category1", category1.getName());
            map.put("category2", category2.getName());
            upList.add(map);
        }else{
            List<OrderItem> orderItemList = orderItemService.selectByOrderId(order.getId().intValue());
            for (OrderItem orderItemBean : orderItemList) {
                Category category2 = categoryService.selectById(orderItemBean.getCategoryId());
                Category category1 = categoryService.selectById(orderItemBean.getParentId());
                Map<String, String> map = new HashMap<>();
                map.put("category1", category1 == null ? "": category1.getName());
                map.put("category2", category2 == null ? "":category2.getName());
                upList.add(map);
            }
        }
        orderB.setCategoryList(upList);
        orderB.setStatus(order.getStatus().name());
        switch (order.getStatus().name()){
            case "INIT":
                //1.订单创建时间
                //2.用户预约上门时间
                orderB.setArrivalTime(order.getArrivalTimePage());
                orderB.setStartTime(order.getCreateDatePage());
                break;
            case "ALREADY":
                //1.回收人员确认接单时间
                //2.回收人员信息：姓名，手机号
                orderB.setReceiveTime(String.valueOf(order.getReceiveTime()));
                Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
                orderB.setRecyclerName(recyclers.getName());
                orderB.setRecyclerTel(recyclers.getTel());
                break;
            case "COMPLETE":
                //1.订单完成时间
                //2.成交金额
                //3订单评价信息：星级+评价内容（若用户已评价）
                orderB.setCompleteTime(String.valueOf(order.getCompleteDate()));
                orderB.setAchPrice(String.valueOf(order.getAchPrice()));
                orderB.setIsCash(order.getIsCash());
                orderB.setIsMysl("1".equals(order.getIsMysl())? "1":"0");
                if("1".equals(order.getIsEvaluated())) {
                    OrderEvaluation orderEvaluation = orderEvaluationService.selectById(order.getId());
                    orderB.setScore(orderEvaluation.getScore());
                    orderB.setContent(StringUtils.isNotBlank(orderEvaluation.getContent())? orderEvaluation.getContent() : "");
                }
                if("HOUSEHOLD".equals(orderB.getTitle())){
                    //生活垃圾：实际回收品类（一二级），重量，单价
                    List<OrderItemAch> orderItemAches = orderItemAchService.selectByOrderId(order.getId().intValue());
                    orderB.setAchCategoryList(new ArrayList<>());
                    for (OrderItemAch orderItemAch : orderItemAches) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("category1", orderItemAch.getParentName());
                        map.put("category2", orderItemAch.getCategoryName());
                        map.put("amount", orderItemAch.getAmount());
                        map.put("price", orderItemAch.getPrice());
                        map.put("unit", orderItemAch.getUnit());
                        orderB.getAchCategoryList().add(map);
                    }
                }else{
                    //实际回收物照片，描述(废弃家电、大件垃圾)
                    orderB.setAchRemarks(order.getAchRemarks());
                    List<OrderPicAch> orderPicAches = orderPicAchService.selectbyOrderId(order.getId().intValue());
                    orderB.setAchPicList(new ArrayList<>());
                    orderB.getAchPicList().addAll(orderPicAches.stream().map(orderPicAch -> orderPicAch.getPicUrl()).collect(Collectors.toList()));
                }

                break;
            case "CANCEL":
                //1.取消时间
                //2.取消原因
                orderB.setCancelReason(order.getCancelReason());
                orderB.setCancelTime(String.valueOf(order.getCancelTime()));
                break;
        }
        MqttMessage mqttMessage = new MqttMessage(JSON.toJSONBytes(orderB));
        mqttMessage.setQos(1);
        try {
            mqtt4PushOrder.publish(MQTTConst.ORDER_TOPIC+"/adc", mqttMessage);
        } catch (MqttException e) {
            log.error("传不上去了..{}", e.getMessage());
            e.printStackTrace();
        }
    }
}
