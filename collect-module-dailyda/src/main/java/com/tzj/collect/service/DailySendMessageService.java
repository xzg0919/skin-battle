package com.tzj.collect.service;


import org.springframework.scheduling.annotation.Async;

public interface DailySendMessageService {

    @Async
    void sendMsgToAllMember(String aliUserId, String formId);
}
