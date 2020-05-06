package com.tzj.green.serviceImpl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.green.mapper.OrderCountMapper;
import com.tzj.green.service.OrderCountService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@Transactional(readOnly = true)
public class OrderCountServiceImpl extends ServiceImpl<OrderCountMapper, T> implements OrderCountService {

    @Resource
    private OrderCountMapper orderCountMapper;

    @Override
    public List<Map<String, Object>> getOrderCount() {
        return orderCountMapper.getOrderCountList();
    }

    @Override
    public Object getOrderCount1() {

        List<Map<String, Object>>list=orderCountMapper.getOrderCountList1();
        List<Map<String, Object>>listCount = new ArrayList<>();
        Map <String, Object> map= null;
        for(int i=0,j = list.size(); i<j;i++){
            map = list.get(i);
            Map <String, Object> mapCount = new HashMap<>();
            mapCount.put("rfid",map.get("userNo"));
            mapCount.put("garbageType",map.get("garbageTypeNumber"));
            mapCount.put("recycleType",map.get("recycleTypeNumber"));
            mapCount.put("weight",map.get("amount"));
            mapCount.put("classifyState","1");
            mapCount.put("weighTime",map.get("createTime"));
            mapCount.put("dealNum","JK"+map.get("id")+map.get("recyclerId"));
            mapCount.put("company","解衣科技");
            mapCount.put("equipNo","");
            listCount.add(mapCount);
        }
        Integer count=list.size();
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("total",count);
        resultMap.put("pagination", pagination);
        resultMap.put("orderList", listCount);
        return resultMap;
    }
}
