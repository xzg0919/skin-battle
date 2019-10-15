package com.tzj.collect.module.task.mq;

import com.alibaba.fastjson.JSON;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rabbitmq.client.Channel;
import com.tzj.collect.entity.Payment;
import com.tzj.collect.service.DailyPaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.tzj.collect.entity.Payment.STATUS_PAYED;
import static com.tzj.collect.entity.Payment.STATUS_TRANSFER;

/**
 * 查询消息监听器
 */
@Component
public class OperateKeywordsListenter implements ChannelAwareMessageListener {

    protected final static Logger logger = LoggerFactory.getLogger(OperateKeywordsListenter.class);

    @Resource
    private DailyPaymentService dailyPaymentService;


    @Override
    @Transactional(readOnly =  false)
    public void onMessage(Message message, Channel channel) throws Exception {

        try {
            String outBizNo = new String(message.getBody(), "UTF-8").replace("\"", "");

            //根据派发红包的随机数去找pament
            Payment payment = dailyPaymentService.selectOne(new EntityWrapper<Payment>().eq("order_sn", outBizNo).eq("is_success","0").eq("del_flag", 0).isNotNull("ali_user_id").eq("status_",1).eq("pay_type", 1));
            if (null != payment){
                //用户转账
                AlipayFundTransToaccountTransferResponse alipayFundTransToaccountTransferResponse = dailyPaymentService.dailyDaTransfer(payment.getAliUserId(), payment.getPrice()+"", outBizNo);
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
                }
                dailyPaymentService.insertOrUpdate(payment);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //不管成功不成功，此条数据都丢弃
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);






        logger.info("收到的消息： {}",new String(message.getBody()));

        //消息确认
        //channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);




        //重试，重新进入队列
        //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);

        //丢弃这条记录
        //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
    }
}
