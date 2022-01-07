package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.Admin;
import com.tzj.collect.entity.MyslRequestLog;

import java.util.List;


public interface MyslRequestLogService extends IService<MyslRequestLog>{

    public List<MyslRequestLog> getNoMysqlList();
}
