package com.tzj.fish.controller;

import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaIdleRecycleOrderQueryRequest;
import com.taobao.api.request.AlibabaIdleRecycleSpuTemplateModifyRequest;
import com.taobao.api.response.AlibabaIdleRecycleOrderQueryResponse;
import com.taobao.api.response.AlibabaIdleRecycleSpuTemplateModifyResponse;
import com.tzj.fish.common.rocket.RocketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tzj.collect.common.utils.ToolUtils.appkey;

@RestController
@RequestMapping("test")
public class Test {

    @Autowired
    private Producer producer;

    @RequestMapping("/send")
    public String send(){
        com.aliyun.openservices.ons.api.Message aliMessage = new com.aliyun.openservices.ons.api.Message(RocketUtil.ALI_TOPIC,"TagA","Hello MQ".getBytes());
        aliMessage.setKey("测试消息发送11111111111111");
        // 发送消息，只要不抛异常就是成功
        SendResult sendResult = producer.send(aliMessage);
        return "发送成功";
    }

}
