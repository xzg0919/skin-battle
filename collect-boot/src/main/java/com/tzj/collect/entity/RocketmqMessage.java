package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 消息队列内容表
 */
@TableName("sb_rocketmq_message")
public class RocketmqMessage extends  DataEntity<Long>{

    private Long id ;

    private String messageId;

    private String message;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
