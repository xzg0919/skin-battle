package com.tzj.collect.service;


import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.api.lexicon.param.DailyDaParam;
import com.tzj.collect.entity.DailyReceiving;

import java.util.Map;

/**
 * @Author sgmark@aliyun.com
 **/
public interface DailyReceivingService extends IService<DailyReceiving> {

     Map<String, Object> memberReceivingRecords(String aliUserId);

     Map<String, Object> receivingMoney(DailyDaParam dailyDaParam);
}
