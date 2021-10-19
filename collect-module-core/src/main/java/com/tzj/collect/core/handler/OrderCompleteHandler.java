package com.tzj.collect.core.handler;

import com.tzj.collect.commom.redis.RedisUtil;
import com.tzj.collect.common.utils.ToolUtils;
import com.tzj.collect.core.service.RecyclersService;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.Recyclers;
import com.tzj.module.common.utils.DateUtils;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: xiangzhongguo
 * @Date: 2021/10/15 14:09
 * @Description:
 */
@Component
public class OrderCompleteHandler extends OrderHandler {

    @Autowired
    RecyclersService recyclersService;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public void beforeComplete(Long recyclerId) {
        try {
            //判断回收人员是否超过限额
            Recyclers recyclers = recyclersService.selectById(recyclerId);
            String key = "recyclerDayTimes:" + DateUtils.formatDate(new Date(), "yyyyMMdd")
                    + ":" + recyclers.getId();
            if ((recyclers.getAllowTimes() != 0 && redisUtil.hasKey(key)
                    && ((Integer) redisUtil.get(key)) >= recyclers.getAllowTimes())
                    || recyclers.getAllowTimes() < 0) {
                throw new ApiException("超限额，次日恢复");
            }

            //回收时间段判断
            List<String> limitCompleteRecyclers = (List<String>) redisUtil.get("limitCompleteRecyclers");
            List<Map<String, String>> limitCompleteTime = (List<Map<String, String>>) redisUtil.get("limitCompleteTime");

            if (limitCompleteRecyclers != null && limitCompleteRecyclers.size()!=0
                    && limitCompleteRecyclers.contains(recyclers.getTel()) &&
                    limitCompleteTime !=null && limitCompleteTime.size()!=0) {
                limitCompleteTime.forEach(timeMap ->{
                    if(ToolUtils.checkTime(new Date(),timeMap.get("beforeTime"),timeMap.get("afterTime"))){
                        throw new ApiException("当前时间段限制完成订单！");
                    }
                });

            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}
