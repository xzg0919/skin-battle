package com.tzj.collect.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.commom.mqtt.MQTTConfig;
import com.tzj.collect.api.commom.mqtt.util.ConnectionOptionWrapper;
import com.tzj.collect.core.service.CompanyEquipmentService;
import com.tzj.collect.core.service.CompanyRecyclerService;
import com.tzj.collect.core.service.EquipmentMessageService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.CompanyEquipment;
import com.tzj.collect.entity.CompanyRecycler;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.OrdersType;
import org.aspectj.weaver.ast.Or;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    private MQTTConfig mqttConfig;
    @Resource
    private CompanyRecyclerService companyRecyclerService;
    @Resource
    private OrderService orderService;
    @Resource
    private CompanyEquipmentService companyEquipmentService;

    @Override
    public void dealWithMessage(String topic,String message) {
        Map<String, Object> messageMap = new HashMap<>();
        try {
            messageMap = JSONObject.parseObject(message);
            //关闭箱门
            if(CompanyEquipment.EquipmentAction.EquipmentActionCode.EQUIPMENT_CLOSE.getKey().equals(messageMap.get("code"))){
                //拿到物品识别图片地址,返回识别结果(挡板翻转)
                messageMap.get("imgUrl");
                //调用阿里的物品识别接口 todo
                Integer nextInt =  new Random().nextInt(1);
                //返回預定圖片
                messageMap = new HashMap<>();
                messageMap.put("imgUrl", "http://images.sqmall.top/collect/20180427_category_pic/11.png");
                messageMap.put("action", nextInt);
                messageMap.put("code", CompanyEquipment.EquipmentAction.EquipmentActionCode.DISCERN_FINISH.getKey());
                //發送消息
                this.sendMessageToMQ4IoTUseSignatureMode(JSONObject.toJSONString(messageMap), topic);
            }else if (CompanyEquipment.EquipmentAction.EquipmentActionCode.RECYCLE_CLOSE.getKey().equals(messageMap.get("code"))){
                //清运关门（根據回收員付款成功的交易號，退回金額）

            }else if (CompanyEquipment.EquipmentAction.EquipmentActionCode.UPLOAD_STATUS.getKey().equals(messageMap.get("code"))){
                //上传满溢值
                CompanyEquipment companyEquipment = companyEquipmentService.selectOne(new EntityWrapper<CompanyEquipment>().eq("del_flag", 0).eq("hardware_code", topic));
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
                        order.setIotEquipmentCode(map.get("equipmentCode")+"");
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
            }
        }catch (Exception e){
            //消息体错误
            try {
                messageMap.put("code", CompanyEquipment.EquipmentAction.EquipmentActionCode.ERROR.getKey());
                messageMap.put("message", "消息错误---"+e.getMessage());
                this.sendMessageToMQ4IoTUseSignatureMode(JSONObject.toJSONString(messageMap), topic);
            } catch (MqttException ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    public void sendMessageToMQ4IoTUseSignatureMode(String message, String topic) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(qosLevel);
        final MemoryPersistence memoryPersistence = new MemoryPersistence();
        /**
         * 客户端使用的协议和端口必须匹配，具体参考文档 https://help.aliyun.com/document_detail/44866.html?spm=a2c4g.11186623.6.552.25302386RcuYFB
         * 如果是 SSL 加密则设置ssl://endpoint:8883
         */
        final MqttClient mqttClient = new MqttClient("tcp://" +  mqttConfig.getInstanceId() + ":1883", mqttConfig.getClientId(), memoryPersistence);
        mqttClient.connect(connectionOptionWrapper.getMqttConnectOptions());
        /**
         * 客户端设置好发送超时时间，防止无限阻塞
         */
        mqttClient.setTimeToWait(5000);
        mqttClient.publish(mqttConfig.getParentTopic() + "/" + topic, mqttMessage);
        mqttClient.close();
    }

    @Override
    public void sendMessageToMQ4IoTUseTokenMode(String message, String sendTo) throws MqttException {

    }
}
