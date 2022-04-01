package com.tzj.collect.core.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.MyslRequestLogMapper;
import com.tzj.collect.core.service.MyslRequestLogService;
import com.tzj.collect.entity.MyslRequestLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MyslRequestLogImpl extends ServiceImpl<MyslRequestLogMapper, MyslRequestLog> implements MyslRequestLogService {

    @Autowired
    private MyslRequestLogMapper myslRequestLogMapper;



    public List<MyslRequestLog> getNoMysqlList(){

        return selectList(new EntityWrapper<MyslRequestLog>().isNull("full_energy"));
    }

    @Override
    public Integer getFullEnergyByOrderNo(String orderNo) {

        EntityWrapper<MyslRequestLog> entityWrapper=new EntityWrapper();
        entityWrapper.setSqlSelect("sum(full_energy) as fullEnergy");
        entityWrapper.eq("order_no",orderNo);

        MyslRequestLog myslRequestLog = selectOne(entityWrapper);

        return   myslRequestLog==null?0:myslRequestLog.getFullEnergy();

    }
}
