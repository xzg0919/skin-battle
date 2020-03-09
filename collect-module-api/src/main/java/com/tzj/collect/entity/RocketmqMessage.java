package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 消息队列内容表
 */
@TableName("sb_rocketmq_message")
@Data
public class RocketmqMessage extends  DataEntity<Long>{

    private Long id ;

    private String messageId;

    private String message;

    private String topicId;

    private String groupId;

}
