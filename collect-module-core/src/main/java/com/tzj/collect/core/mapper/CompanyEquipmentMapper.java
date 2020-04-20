package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.CompanyEquipment;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CompanyEquipmentMapper extends BaseMapper<CompanyEquipment> {
    /**
     * 查询iot设备订单数人数统计
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/13 0013
     * @Param: 
     * @return: 
     */
    List<Map<String, Object>> adminIotOrderPage(@Param("equipmentCode") String equipmentCode, @Param("companyId")String companyId, @Param("startTime")String startTime, @Param("endTime")String endTime, @Param("startSize")Integer startSize, @Param("endSize")Integer endSize);
    /**
     * 近15天转化率
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/13 0013
     * @Param: 
     * @return: 
     */
    String iotConRate(@Param("day") String day);

    List<Map<String, Object>> adminIotOrderList(@Param("equipmentCode")String equipmentCode, @Param("companyId")String companyId, @Param("startTime")String startTime, @Param("endTime")String endTime);

    void insertIotImg(@Param("equipmentCode")String topic,@Param("imgUrl")Object imgUrl);
}
