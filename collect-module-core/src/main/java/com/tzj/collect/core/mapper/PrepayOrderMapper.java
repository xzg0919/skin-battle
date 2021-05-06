package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.PrepayOrder;

import java.util.List;
import java.util.Map;

public interface PrepayOrderMapper extends BaseMapper<PrepayOrder> {

    List<Map<String, Object>> findUnPayOrder();

    List<Map<String, Object>> findUnPayOrderDelete();
}