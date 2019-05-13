package com.tzj.collect.api.common.async;

import com.tzj.collect.api.iot.IotApi;
import com.tzj.collect.api.iot.localmap.LatchMap;
import com.tzj.collect.common.redis.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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

    /** 异步保存至redis（订单id及派单时间）
     * @author sgmark@aliyun.com
     * @date 2019/5/10 0010
     * @param
     * @return
     */
    @Async
    public void saveOrRemoveOrderIdAndTimeFromRedis(Long orderId, Long recId, Long time, String saveOrRemove){
        Hashtable<String, Object> orderRevertListMap = (Hashtable) redisUtil.get("orderRevertListMap");
        List<Hashtable<String, Object>> orderList = null;
        Hashtable<String, Object> hashTable =  new Hashtable<>();
        if (null == orderRevertListMap){
            orderRevertListMap = new Hashtable<>();
            orderList = new ArrayList<>();
        }else {
            orderList = (List<Hashtable<String, Object>>) orderRevertListMap.get("orderRevertList");
            if (null == orderList){
                orderList = new ArrayList<>();
            }
        }
        if ("save".equals(saveOrRemove)){
            hashTable.put("orderId", orderId);
            hashTable.put("recId", recId);
            hashTable.put("distributeTime", time);
            orderList.add(hashTable);
            orderRevertListMap.put("orderRevertList", orderList);
            redisUtil.set("orderRevertListMap", orderRevertListMap, 5400);//最长存值一个半小时(1.5*60*60)
            //每次进来调用一次
            this.pollingFromRedis();
        }else if ("remove".equals(saveOrRemove)){
            orderList.removeIf(orderMap -> orderMap.get("orderId") == orderId && orderMap.get("recId") == recId);
            orderRevertListMap.put("orderRevertList", orderList);
            redisUtil.set("orderRevertListMap", orderRevertListMap, 5400);//最长存值一个半小时(1.5*60*60)
        }

    }
    /** 派单时保存至redis，回收员接单时从redis移除，待接单状态下超过一个小时回退至平台或者管理员处
     * @author sgmark@aliyun.com
     * @date 2019/5/10 0010
     * @param
     * @return
     */
    @Async
    public void pollingFromRedis(){
        Object orderRevertIsRunning =  redisUtil.get("orderRevertIsRunning");
        if (null != orderRevertIsRunning){
            Boolean isRunning = (Boolean) orderRevertIsRunning;
            if (isRunning){
                //已启动时直接返回
                return;
            }else {//没运行时开启
                redisUtil.set("orderRevertIsRunning", true, 5400);
            }
        }else {//没找到表示未开启
            redisUtil.set("orderRevertIsRunning", true, 5400);
        }
        Hashtable<String, Object> orderRevertListMap = null;
        List<Hashtable<String, Object>> orderList = null;
        do {
            orderRevertListMap = (Hashtable)redisUtil.get("orderRevertListMap");
            if (null != orderRevertListMap && null != orderRevertListMap.get("orderRevertList")){
                orderList = (List<Hashtable<String, Object>>) orderRevertListMap.get("orderRevertList");
                if (orderList.size() == 0){
                    redisUtil.set("orderRevertIsRunning", false, 5400);
                    //停止当前线程
                    System.out.println("-------已派發訂單處理完成，線程關閉------");
                    Thread.currentThread().interrupted();
                    return;
                }else {
                    orderList = orderList.stream().filter(orderListFilter -> System.currentTimeMillis() - (long)orderListFilter.get("distributeTime") >= 120 * 1000).collect(Collectors.toList());
//                    orderList
                    System.out.println("------需要回退的訂單數量："+orderList.size()+ "--------");
                    //处理业务代码，订单回退到平台或者回退到回收经理处
                    //
                }
            }
            try {
                Thread.sleep(30 * 1000);//每分钟执行一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while (null != redisUtil.get("orderRevertIsRunning") && true == (Boolean) redisUtil.get("orderRevertIsRunning"));
    }


}
