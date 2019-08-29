package com.tzj.collect.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.DailyReceiving;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
  * @author sgmark@aliyun.com
  * @date 2019/8/15 0015
  * @param
  * @return
  */
public interface DailyReceivingMapper extends BaseMapper<DailyReceiving> {

    Integer signNum(@Param("aliUserId")String aliUserId , @Param("tableName")String tableName);

    List<Map<String, Object>> receiveNum(@Param("aliUserId")String aliUserId, @Param("week")String week);

    Integer updateRecords(@Param("aliUserId") String aliUserId, @Param("week") String week, @Param("setNum") Integer setNum, @Param("price") String price, @Param("uuId")String uuId);
}
