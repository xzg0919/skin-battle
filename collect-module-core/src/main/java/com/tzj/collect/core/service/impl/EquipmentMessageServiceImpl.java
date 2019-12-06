package com.tzj.collect.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.commom.constant.MQTTConst;
import com.tzj.collect.api.commom.mqtt.MQTTConfig;
import com.tzj.collect.api.commom.mqtt.util.ConnectionOptionWrapper;
import com.tzj.collect.api.commom.mqtt.util.Tools;
import com.tzj.collect.commom.redis.RedisUtil;
import com.tzj.collect.core.param.iot.IotParamBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.utils.security.CipherTools;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.ast.Or;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_TOKEN_CYPTO_KEY;
import static com.tzj.collect.common.constant.TokenConst.ALI_API_TOKEN_SECRET_KEY;
import static org.eclipse.paho.client.mqttv3.MqttConnectOptions.MQTT_VERSION_3_1_1;

/**
 * @author sgmark
 * @create 2019-11-15 9:50
 **/
@Service
@Transactional(readOnly = true)
public class EquipmentMessageServiceImpl implements EquipmentMessageService {

    final int qosLevel = 1;
    @Resource(name = "connectionOptionWrapperSignature")
    private ConnectionOptionWrapper connectionOptionWrapper;
    @Resource
    private CompanyRecyclerService companyRecyclerService;
    @Resource
    private OrderService orderService;
    @Resource
    private PaymentService paymentService;
    @Resource
    private CompanyEquipmentService companyEquipmentService;
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private  JedisPool jedisPool;

    @Override
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
                messageMap.get("imgUrl");
                //调用阿里的物品识别接口 todo
                Integer nextInt =  new Random().nextInt(1);
                //返回預定圖片
                messageMap = new HashMap<>();
                messageMap.put("imgUrl", "http://images.sqmall.top/collect/20180427_category_pic/11.png");
                messageMap.put("action", nextInt);
                messageMap.put("code", CompanyEquipment.EquipmentAction.EquipmentActionCode.DISCERN_FINISH.getKey());
                messageMap.put("msg",  CompanyEquipment.EquipmentAction.EquipmentActionCode.DISCERN_FINISH.getValue());
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
                        messageMap.put("msg", CompanyEquipment.EquipmentAction.EquipmentActionCode.TRADE_SUCCESS.getValue());
                        this.sendMessageToMQ4IoTUseSignatureMode(JSONObject.toJSONString(messageMap), topic, mqttClient);
                        //更改订单状态
                    }else {
                        payment.setIsSuccess("1");
                        payment.setRemarks(alipayFundTransToaccountTransferResponse.getSubMsg());
                        paymentService.updateById(payment);
                        messageMap.put("code", CompanyEquipment.EquipmentAction.EquipmentActionCode.ERROR.getKey());
                        messageMap.put("msg", "交易失败");
                        this.sendMessageToMQ4IoTUseSignatureMode(JSONObject.toJSONString(messageMap), topic, mqttClient);
                    }
                }

            }else if (CompanyEquipment.EquipmentAction.EquipmentActionCode.UPLOAD_STATUS.getKey().equals(messageMap.get("code"))){
                //上传满溢值

                //该公司下随机取一个回收人员（硬件编号找信息）
                Map<String, Object> map = companyRecyclerService.selectRecByHardwareCode(topic);
                if (Double.parseDouble(messageMap.get("currentValue")+"")>= Double.parseDouble(map.get("setValue")+"")){
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
                            return;
                        }
                        order.setRecyclerId(Integer.parseInt(map.get("recId")+""));
                        order.setIotEquipmentCode(companyEquipment.getEquipmentCode());
                        order.setAddress(map.get("address")+"");
                        order.setMemberId(Integer.parseInt(map.get("comId")+""));//
                        order.setAreaId(Integer.parseInt(map.get("areaId")+""));
                    }
                    orderService.insert(order);
                    //订单清运完成，初始值设为0
                    companyEquipment.setCurrentValue(Double.valueOf(0));
                    companyEquipmentService.updateById(companyEquipment);
                }else {
                    //修改设备满溢当前值
                    companyEquipment.setCurrentValue(Double.parseDouble(messageMap.get("code")+""));
                    companyEquipmentService.updateById(companyEquipment);
                }
            }else if (CompanyEquipment.EquipmentAction.EquipmentActionCode.UPLOAD_ORDER.getKey().equals(messageMap.get("code"))){
                //上传订单数据(用户投递)
                this.creatIotOrderByMqtt(topic, message, mqttClient);
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
                iotParamBean = new IotParamBean();
                iotParamBean.setSumPrice(new BigDecimal(object.get("sumPrice").toString()));
                iotParamBean.setEquipmentCode(object.get("equipmentCode").toString());
                iotParamBean.setMemberId(subjectStr);
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
}
