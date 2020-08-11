package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.OrderComplaint;

import java.util.List;
import java.util.Map;

public interface OrderComplaintService extends IService<OrderComplaint> {

    @DS("slave")
    Object getIsOrderComplaint(String orderNo);

}
