package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.OrderComplaint;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderComplaintMapper extends BaseMapper<OrderComplaint> {

    List<OrderComplaint>selectComplaintList(@Param("orderNo") String orderNo);



}
