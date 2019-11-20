package com.tzj.collect.core.service;


import org.springframework.scheduling.annotation.Async;

public interface DailySendMessageService {

    @Async
    void sendMsgToAllMember(String aliUserId, String formId);
}
