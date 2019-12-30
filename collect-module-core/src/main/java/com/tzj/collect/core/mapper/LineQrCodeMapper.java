package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.LineQrCode;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface LineQrCodeMapper extends BaseMapper<LineQrCode> {
    List<Map<String, Object>> lineQrCodeReport(@Param("qrType")Serializable value, @Param("qrName")String qrName, @Param("startTime")String startTime, @Param("endTime")String endTime, @Param("startPage")Integer startPage, @Param("pageSize")Integer pageSize);
}
