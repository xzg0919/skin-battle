package com.tzj.collect.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayIserviceCognitiveClassificationWasteQueryRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayIserviceCognitiveClassificationWasteQueryResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.commom.constant.MQTTConst;
import com.tzj.collect.commom.redis.RedisUtil;
import com.tzj.collect.common.amap.AmapConst;
import com.tzj.collect.common.amap.AmapRegeoJson;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.core.param.iot.EquipmentParamBean;
import com.tzj.collect.core.param.iot.IotParamBean;
import com.tzj.collect.core.result.flcx.AlipayResponseResult;
import com.tzj.collect.core.result.iot.BizContent;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.utils.security.CipherTools;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_TOKEN_CYPTO_KEY;
import static com.tzj.collect.common.constant.TokenConst.ALI_API_TOKEN_SECRET_KEY;

/**
 * @author sgmark
 * @create 2019-11-15 9:50
 **/
@Service
@Transactional(readOnly = true)
public class EquipmentMessageServiceImpl implements EquipmentMessageService {

    final int qosLevel = 1;
    @Resource
    private CompanyRecyclerService companyRecyclerService;
    @Resource
    private OrderService orderService;
    @Resource
    private PaymentService paymentService;
    @Resource
    private CompanyEquipmentService companyEquipmentService;
    @Resource
    private EquipmentLocationListService equipmentLocationListService;
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private  JedisPool jedisPool;
    @Resource
    private MessageService messageService;
    @Resource
    private RecyclersService recyclersService;
    @Resource
    private MemberService memberService;
    @Override
    @Transactional(readOnly = false)
    public void dealWithMessage(String topic,String message, MqttClient mqttClient) {
        Map<String, Object>  messageMap = JSONObject.parseObject(message);
        CompanyEquipment companyEquipment = companyEquipmentService.selectOne(new EntityWrapper<CompanyEquipment>().eq("del_flag", 0).eq("hardware_code", topic));
        try {
            if (CollectionUtils.isEmpty(messageMap)){
                messageMap = new HashMap<>();
                messageMap.put("code", CompanyEquipment.EquipmentAction.EquipmentActionCode.ERROR.getKey());
                messageMap.put("message", "---消息格式错误---");
                this.sendMessageToMQ4IoTUseSignatureMode(JSONObject.toJSONString(messageMap), topic, mqttClient);
                return;
            }
            //关闭（识别图片）
            if(CompanyEquipment.EquipmentAction.EquipmentActionCode.IDENTIFYING_PICTURES.getKey().equals(messageMap.get("code"))){
                //拿到物品识别图片地址,返回识别结果(挡板翻转)
                System.out.println(messageMap.get("imgUrl"));
                try {
                    companyEquipmentService.insertIotImg(topic, messageMap.get("imgUrl"));
                }catch (Exception e){

                }
                //调用阿里的物品识别接口 todo
//                returnTypeByPic(messageMap.get("imgUrl")+"");

                Integer nextInt =  new Random().nextInt(3);
                //返回預定圖片
                messageMap = new HashMap<>();
                messageMap.put("imgUrl", messageMap.get("imgUrl"));
                messageMap.put("action", nextInt > 1 ? "1":"1");
                messageMap.put("code", CompanyEquipment.EquipmentAction.EquipmentActionCode.DISCERN_FINISH.getKey());
                messageMap.put("message",  CompanyEquipment.EquipmentAction.EquipmentActionCode.DISCERN_FINISH.getValue());
                //發送消息
                this.sendMessageToMQ4IoTUseSignatureMode(JSONObject.toJSONString(messageMap), topic, mqttClient);
            }else if (CompanyEquipment.EquipmentAction.EquipmentActionCode.RECYCLE_CLOSE.getKey().equals(messageMap.get("code"))){
                //清运关门（根據回收員付款成功的交易號，退回金額）
                //先查出所生成的交易订单，据此回退金额（哪儿来回哪儿去）
                Payment payment = paymentService.selectOne(new EntityWrapper<Payment>().eq("del_flag", 0).eq("seller_id", messageMap.get("sellerId")).eq("status_", Payment.STATUS_PAYED).eq("is_success", 0).eq("pay_type", Payment.PayType.RECYCLE_IOT).last("1"));
                if(null != payment){
                    AlipayFundTransToaccountTransferResponse alipayFundTransToaccountTransferResponse = paymentService.iotTransfer(payment.getOrderSn(), payment.getPrice()+"", payment.getTradeNo());
                    if ("Success".equals(alipayFundTransToaccountTransferResponse.getMsg())){
                        //更新交易状态
                        payment.setStatus(Payment.STATUS_TRANSFER);
                        payment.setIsSuccess("1");
                        paymentService.updateById(payment);
                        messageMap.put("code", CompanyEquipment.EquipmentAction.EquipmentActionCode.TRADE_SUCCESS.getKey());
                        messageMap.put("message", CompanyEquipment.EquipmentAction.EquipmentActionCode.TRADE_SUCCESS.getValue());
                        this.sendMessageToMQ4IoTUseSignatureMode(JSONObject.toJSONString(messageMap), topic, mqttClient);
                        //更改订单状态
                        Order order = orderService.selectOne(new EntityWrapper<Order>().eq("del_flag", 0).eq("order_sn", payment.getOrderSn()));
                        order.setStatus(Order.OrderType.COMPLETE);
                        orderService.updateById(order);
                    }else {
                        payment.setIsSuccess("1");
                        payment.setRemarks(alipayFundTransToaccountTransferResponse.getSubMsg());
                        paymentService.updateById(payment);
                        messageMap.put("code", CompanyEquipment.EquipmentAction.EquipmentActionCode.ERROR.getKey());
                        messageMap.put("message", "交易失败");
                        this.sendMessageToMQ4IoTUseSignatureMode(JSONObject.toJSONString(messageMap), topic, mqttClient);
                    }
                }

            }else if (CompanyEquipment.EquipmentAction.EquipmentActionCode.UPLOAD_STATUS.getKey().equals(messageMap.get("code"))){
                //上传满溢值

                //该公司下随机取一个回收人员（硬件编号找信息）
                Map<String, Object> map = companyRecyclerService.selectRecByHardwareCode(topic);
                if (Double.parseDouble(messageMap.get("currentValue")+"")>= Double.parseDouble(map.get("set_value")+"")){
                    //如果满溢状态达到设定值，生成清运订单
                    Order order = new Order();
                    order.setTitle(Order.TitleType.IOTCLEANORDER);
                    order.setStatus(Order.OrderType.TOSEND);
                    order.setPrice(BigDecimal.ONE);
                    order.setAchPrice(BigDecimal.ONE);
                    if (!CollectionUtils.isEmpty(map)){
                        //当前设备存在未清运订单，不再重新生成订单
                        List<Order> orderList = orderService.selectList(new EntityWrapper<Order>().eq("iot_equipment_code", map.get("equipmentCode")).notLike("status_", Order.OrderType.COMPLETE.getValue()+""));
                        if (orderList.size() >= 1){
                            //订单清运完成，初始值设为0
                            companyEquipment.setCurrentValue(Double.parseDouble(messageMap.get("currentValue")+""));
                            companyEquipmentService.updateById(companyEquipment);
                            return;
                        }
                        order.setRecyclerId(Integer.parseInt(map.get("recId")+""));
                        order.setIotEquipmentCode(companyEquipment.getEquipmentCode());
                        order.setAddress(map.get("address")+"");
                        order.setMemberId(Integer.parseInt(map.get("comId")+""));//
                        order.setAreaId(Integer.parseInt(map.get("areaId")+""));
                    }
                    orderService.insert(order);
                }else {
                    //修改设备满溢当前值
                    companyEquipment.setCurrentValue(Double.parseDouble(messageMap.get("currentValue")+""));
                    companyEquipmentService.updateById(companyEquipment);
                }
            }else if (CompanyEquipment.EquipmentAction.EquipmentActionCode.UPLOAD_ORDER.getKey().equals(messageMap.get("code"))){
                //上传订单数据(用户投递)
                this.creatIotOrderByMqtt(topic, message, mqttClient);
            }else if (CompanyEquipment.EquipmentAction.EquipmentActionCode.UPLOAD_LOCATION.getKey().equals(messageMap.get("code"))){
                try {
                    //上传定位（保存最新10条记录）
                    EquipmentLocationList equipmentLocationList = new EquipmentLocationList();
                    equipmentLocationList.setEquipmentCode(topic);
                    equipmentLocationList.setLatitude(messageMap.get("latitude")+"");
                    equipmentLocationList.setLongitude(messageMap.get("longitude")+"");
                    if (!StringUtils.isEmpty(messageMap.get("location")+"")){
                        equipmentLocationList.setLocation(messageMap.get("location")+"");
                    }else {
                        equipmentLocationList.setLocation(locationByLBS(equipmentLocationList.getLongitude(), equipmentLocationList.getLatitude()));
                    }
                    equipmentLocationListService.insert(equipmentLocationList);
                    //只保留最新10条
                    Integer equipmentLocationCount = equipmentLocationListService.selectCount(new EntityWrapper<EquipmentLocationList>().eq("del_flag", 0).eq("equipment_code", topic));
                    if(equipmentLocationCount - 10 > 0){
                        List<EquipmentLocationList> equipmentLocationLists = equipmentLocationListService.selectList(new EntityWrapper<EquipmentLocationList>().eq("del_flag", 0).eq("equipment_code", topic).orderBy("id", true).last(" limit " + (equipmentLocationCount - 10)));
                        equipmentLocationLists.stream().forEach(locationList -> {
                            equipmentLocationListService.deleteById(locationList);
                        });
                    }
                }catch (Exception e){
                    messageMap = new HashMap<>();
                    messageMap.put("code", CompanyEquipment.EquipmentAction.EquipmentActionCode.ERROR.getKey());
                    messageMap.put("message", "---消息格式错误---");
                    this.sendMessageToMQ4IoTUseSignatureMode(JSONObject.toJSONString(messageMap), topic, mqttClient);
                    return;
                }
            }
        }catch (Exception e){
            //消息体错误
            try {
                messageMap = new HashMap<>();
                messageMap.put("code", CompanyEquipment.EquipmentAction.EquipmentActionCode.ERROR.getKey());
                messageMap.put("message", "消息错误---"+e.getMessage());
                this.sendMessageToMQ4IoTUseSignatureMode(JSONObject.toJSONString(messageMap), topic, mqttClient);
            } catch (MqttException ex) {
                ex.printStackTrace();
            }
        }

    }

    private void creatIotOrderByMqtt(String topic, String message, MqttClient mqttClient) {
        IotParamBean iotParamBean = new IotParamBean();
        JSONObject object = JSON.parseObject(message);
        String subjectStr = "";
        //验证token是否有效，若无效，直接丢弃消息
        try {
            Map<String, Object> messageMap = new HashMap<>();
            String token = object.get("token")+"";
            if (StringUtils.isEmpty(token)){
                //说明接收到的消息token不存在，直接丢弃掉
                return;
            }else {
                //解析token(获取用户阿里userId)
                String tokenCyptoKey = ALI_API_TOKEN_CYPTO_KEY;
                String key = CipherTools.initKey(tokenCyptoKey);
                String decodeToken = CipherTools.decrypt(token, key);
                Claims claims = JwtUtils.getClaimByToken(decodeToken, ALI_API_TOKEN_SECRET_KEY);
                subjectStr = claims.getSubject();
            }
            if (!StringUtils.isEmpty(subjectStr)){
                System.out.println("---------------ali_uid:" +subjectStr+"--------------------------");
                iotParamBean = new IotParamBean();
                iotParamBean.setSumPrice(new BigDecimal(object.get("sumPrice").toString()));
                iotParamBean.setEquipmentCode(object.get("equipmentCode").toString());
                Member member = memberService.selectMemberByAliUserId(subjectStr);
                iotParamBean.setMemberId(member.getCardNo());
                iotParamBean.setParentLists(object.getJSONArray("parentLists").toJavaList(IotParamBean.ParentList.class));
                //消息保存成功之后再处理
                orderService.iotCreatOrder(iotParamBean);
                messageMap.put("code", CompanyEquipment.EquipmentAction.EquipmentActionCode.UPLOAD_SUCCESS.getKey());
                messageMap.put("message", CompanyEquipment.EquipmentAction.EquipmentActionCode.UPLOAD_SUCCESS.getValue());
            }else {
                messageMap.put("code", CompanyEquipment.EquipmentAction.EquipmentActionCode.ERROR.getKey());
                messageMap.put("message", "解析用户信息错误:" + CompanyEquipment.EquipmentAction.EquipmentActionCode.ERROR.getValue());
            }
            this.sendMessageToMQ4IoTUseSignatureMode(JSONObject.toJSONString(messageMap), topic, mqttClient);

        }catch (Exception e){
            //说明接收到的消息有问题，直接丢弃掉
            Map<String, Object> messageMap = new HashMap<>();
            try {
                messageMap.put("code", CompanyEquipment.EquipmentAction.EquipmentActionCode.ERROR.getKey());
                messageMap.put("message", "消息错误---"+e.getMessage());
                this.sendMessageToMQ4IoTUseSignatureMode(JSONObject.toJSONString(messageMap), topic, mqttClient);
            } catch (MqttException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void sendMessageToMQ4IoTUseSignatureMode(String message, String topic, MqttClient mqttClient) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(qosLevel);
        System.out.println("message:"+message);
        mqttClient.publish(MQTTConst.PARENT_TOPIC + "/" + topic, mqttMessage);
    }

    @Override
    public void sendMessageToMQ4IoTUseTokenMode(String message, String sendTo, MqttClient mqttClient) throws MqttException {

    }
    /**
     * redis 保存set
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/2 0002
     * @Param: 
     * @return: 
     */
    @Override
    public Boolean redisSetAdd(String key, String value){
        return redisUtil.redisSetAdd(key, value, jedisPool);
    }
    /**
     * 查找redis set 中是否存在该值
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/2 0002
     * @Param: 
     * @return: 
     */
    @Override
    public Boolean redisSetCheck(String key, String value){
        return redisUtil.redisSetCheck(key, value, jedisPool);
    }

    @Override
    public Map<String, Object> equipmentCodeOpen(EquipmentParamBean equipmentParamBean, MqttClient mqttClient) {
        Map<String, Object> returnMap = new HashMap<>();
        Order order = orderService.selectOne(new EntityWrapper<Order>().eq("del_flag", 0).eq("iot_equipment_code", equipmentParamBean.getHardwareCode()).eq("status_", Order.OrderType.TOSEND));
        if (null != order){
            Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
            if (messageService.validMessage(recyclers.getTel(), equipmentParamBean.getCaptcha())){
                //验证码有效，发送开箱请求
                Map<String, Object> messageMap = new HashMap<>();
                messageMap.put("code", CompanyEquipment.EquipmentAction.EquipmentActionCode.RECYCLE_OPEN.getKey());
                messageMap.put("message", CompanyEquipment.EquipmentAction.EquipmentActionCode.RECYCLE_OPEN.getKey());
                try {
                    this.sendMessageToMQ4IoTUseSignatureMode(JSONObject.toJSONString(messageMap), equipmentParamBean.getHardwareCode(), mqttClient);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                //使验证码失效
                EntityWrapper<Message> wrapper = new EntityWrapper<>();
                wrapper.eq("del_flag", 0);
                wrapper.eq("tel", recyclers.getTel());
                wrapper.eq("message_code", equipmentParamBean.getCaptcha());
                Message message= messageService.selectOne(wrapper);
                message.setDelFlag("1");
                messageService.updateById(message);
            }
        }
        returnMap.put("msg", "操作成功");
        returnMap.put("code", "0");
        return returnMap;
    }

    private String locationByLBS(String longitude, String latitude) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        String url = "https://restapi.amap.com/v3/geocode/regeo";
        Response response = null;
        response = FastHttpClient.get().url(url)
                .addParams("key", AmapConst.AMAP_KEY)
                .addParams("location", longitude + "," + latitude)
                .build().execute();
        String resultJson = response.body().string();
        if (StringUtils.isNotEmpty(resultJson)){
            resultJson = resultJson.replaceAll("\n", "");
            try {
                AmapRegeoJson amapRegeoJson = JSON.parseObject(resultJson, AmapRegeoJson.class);
                return amapRegeoJson.getRegeocode().getFormatted_address();
            }catch (Exception e){
                System.out.println("=============异常抛出来咯："+resultJson+"============");
            }
        }else {
            resultMap.put("city", "");
        }
        return "";
    }

    /**
     * Ali识别
     * @author: sgmark@aliyun.com
     * @Date: 2020/1/17 0017
     * @Param: 
     * @return: 
     */
    public static AlipayResponseResult returnTypeByPic(String picUrl) {
        AlipayResponseResult responseResult = new AlipayResponseResult();
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConst.flcxaAppId, AlipayConst.flcx_private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.flcx_ali_public_key, AlipayConst.sign_type);
        AlipayIserviceCognitiveClassificationWasteQueryRequest request = new AlipayIserviceCognitiveClassificationWasteQueryRequest();
        AlipayIserviceCognitiveClassificationWasteQueryResponse execute = null;
        try {
            BizContent bizContent = new BizContent();
            if (!StringUtils.isBlank(picUrl)) {
                bizContent.setBiz_code("biz8");
                bizContent.setSource("isv");
                bizContent.setCognition_content(picUrl);
                bizContent.setCognition_type("ImageUrl");
            }
            request.setBizContent(JSON.toJSONString(bizContent));

            execute = alipayClient.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        responseResult.setKeyWords(execute.getKeyWords());
        responseResult.setTraceId(execute.getTraceId());
        return responseResult;
    }

    public static void main(String[] args) {
        returnTypeByPic("http://images.sqmall.top/new_bridge/20191227/iot_07162e6c-4879-40b5-81ba-04908343ad5c.jpg");
    }

}
