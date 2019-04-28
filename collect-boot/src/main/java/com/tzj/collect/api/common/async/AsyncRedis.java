package com.tzj.collect.api.common.async;

import com.tzj.collect.api.iot.IotApi;
import com.tzj.collect.api.iot.localmap.LatchMap;
import com.tzj.collect.common.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 异步调用
 *
 * @author sgmark
 * @create 2019-04-28 14:53
 **/

@Component
public class AsyncRedis {

    @Autowired
    private RedisUtil redisUtil;

    @Async
    public void getTokenByCache(Long startTime, String iotMemId){
        IotApi.flag = new  AtomicBoolean(true);//每次进来设值为真
        Hashtable<String, String> iotMapCache;
        LatchMap latchMapResult = null;
        do {
            try {
                iotMapCache  = (Hashtable<String, String>)redisUtil.get("iotMap");
                if (iotMapCache != null && iotMapCache.containsKey(iotMemId)){
                    latchMapResult = IotApi.latMapConcurrent.get(iotMemId);
                    if (null != latchMapResult){
                        latchMapResult.orderId = iotMapCache.get(iotMemId);
                        latchMapResult.latch.countDown();
                        iotMapCache.remove(iotMemId);
                        redisUtil.set("iotMap", iotMapCache);
                        break;
                    }else{
                        continue;
                    }
                }else{
                    //每秒执行一次
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){
                        System.exit(0);//退出程序
                    }
                }
            }catch (Exception e){
                System.exit(0);//退出程序
            }
        }while (System.currentTimeMillis() - startTime <= 8*1000 && IotApi.flag.get());
        //9s内还没订单返回，关闭长连接
        if (null != latchMapResult){
            latchMapResult.latch.countDown();
        }
    }
}
