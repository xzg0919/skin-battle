package com.tzj.collect.core.handler;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.commom.redis.RedisUtil;
import com.tzj.collect.common.utils.ToolUtils;
import com.tzj.collect.core.service.OrderService;
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
    @Autowired
    OrderService orderService;

    @Override
    public void beforeComplete(Long recyclerId, Order.TitleType titleType, String aliUserId) {
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

            if (limitCompleteRecyclers != null && limitCompleteRecyclers.size() != 0
                    && limitCompleteRecyclers.contains(recyclers.getTel()) &&
                    limitCompleteTime != null && limitCompleteTime.size() != 0) {
                limitCompleteTime.forEach(timeMap -> {
                    if (ToolUtils.checkTime(new Date(), timeMap.get("beforeTime"), timeMap.get("afterTime"))) {
                        throw new ApiException("当前时间段限制完成订单！");
                    }
                });

            }

            //判断家电和五费是否超订单量
            int userLimitCompleteCount = Integer.parseInt(redisUtil.get("userLimitCompleteCount").toString());
            //限制次数为-1 表示不限制
            if (userLimitCompleteCount !=-1 &&
                    (titleType.compareTo(Order.TitleType.DIGITAL) == 0 || titleType.compareTo(Order.TitleType.HOUSEHOLD) == 0)) {
                Integer orderCount = orderService.selectCount(new EntityWrapper<Order>().eq("ali_user_id", aliUserId)
                        .eq("title", titleType.getValue()).eq("status_", Order.OrderType.COMPLETE.getValue().toString())
                        .last(" and  to_days(complete_date) = to_days(now())"));
                String titleName = titleType.compareTo(Order.TitleType.DIGITAL) == 0 ? "家电" : "五公斤";
                if (orderCount >= userLimitCompleteCount) {
                    throw new ApiException("该用户每日完成" + titleName + "订单超限额,每日限制次数三次！");
                }
            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
