package com.tzj.green.serviceImpl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.green.common.redis.RedisUtil;
import com.tzj.green.entity.Logs;
import com.tzj.green.mapper.OrderCountMapper;
import com.tzj.green.param.CompanyBean;
import com.tzj.green.service.LogsService;
import com.tzj.green.service.OrderCountService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@Transactional(readOnly = true)
public class OrderCountServiceImpl extends ServiceImpl<OrderCountMapper, T> implements OrderCountService {

    @Resource
    private OrderCountMapper orderCountMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LogsService logsService;


    @Override
    public List<Map<String, Object>> getOrderCount() {
        return orderCountMapper.getOrderCountList();
    }

    @Override
    public List<Map<String, Object>> getPointCount(CompanyBean companyBean) {
        return orderCountMapper.getPointCountList(companyBean.getCompanyId(),companyBean.getStartTime(),companyBean.getEndTime());
    }

    @Override
    @Transactional(readOnly = false)
    public Object getOrderCount1() {
        //插入日志
        Logs logs = new Logs();
        logs.setCompanyId(6);
        logs.setParam("萧山城管局获取信息");

        List<Object> listOrder = redisUtil.lGet("order", 0, -1);
        if (listOrder == null||listOrder.isEmpty()) {
            List<Map<String, Object>> list = orderCountMapper.getOrderCountList1();
            Map<String, Object> map = null;
            for (int i = 0, j = list.size(); i < j; i++) {
                map = list.get(i);
                Map<String, Object> mapCount = new HashMap<>();
                mapCount.put("rfid", map.get("userNo"));
                mapCount.put("garbageType", map.get("garbageTypeNumber"));
                mapCount.put("recycleType", map.get("recycleTypeNumber"));
                mapCount.put("weight", map.get("amount"));
                mapCount.put("classifyState", "1");
                mapCount.put("weighTime", map.get("createTime"));
                mapCount.put("dealNum", "JK" + map.get("id") + map.get("recyclerId"));
                mapCount.put("company", "解衣科技");
                mapCount.put("equipNo", "");
                listOrder.add(mapCount);
            }

            if (list!=null&&!list.isEmpty()) {
                redisUtil.lSet("order",listOrder,60);
                logs.setBody(listOrder.toString());
            }
            else{
                logs.setBody("今天无数据");
            }
        }else {
            logs.setBody(listOrder.toString());
        }
        try {
            logsService.insert(logs);
        } catch (Exception e) {
            e.printStackTrace();
        }
         return listOrder;
    }
}
