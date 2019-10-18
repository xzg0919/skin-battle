package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.EquipmentErrorCodeMapper;
import com.tzj.collect.core.param.iot.IotErrorParamBean;
import com.tzj.collect.core.service.EquipmentErrorCodeService;
import com.tzj.collect.entity.EquipmentErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sgmark
 * @create 2019-04-02 15:25
 **/
@Service
@Transactional(readOnly = true)
public class EquipmentErrorCodeServiceImpl extends ServiceImpl<EquipmentErrorCodeMapper, EquipmentErrorCode> implements EquipmentErrorCodeService {

    @Resource
    private  EquipmentErrorCodeMapper equipmentErrorCodeMapper;
    @Override
    public List<Map<String, Object>> iotErrorCode(IotErrorParamBean iotErrorParamBean) {
        List<Map<String, Object>> listMap= new ArrayList<>();
        equipmentErrorCodeMapper.selectList(new EntityWrapper<EquipmentErrorCode>().eq("del_flag", 0).orderBy("error_code",true)).stream().forEach(equipmentErrorCodes -> {
            Map<String, Object> map = new HashMap<>();
            map.put("errorCode", equipmentErrorCodes.getErrorCode());
            map.put("errorMessage", equipmentErrorCodes.getErrorMessage());
            listMap.add(map);
        });
        return listMap;
    }
}
