package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.EquipmentAdvertMapper;
import com.tzj.collect.core.service.EquipmentAdvertService;
import com.tzj.collect.entity.AbstractEntity;
import com.tzj.collect.entity.EquipmentAdvert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;

/**
 * @author sgmark
 * @create 2019-04-02 15:25
 **/
@Service
@Transactional(readOnly = true)
public class EquipmentAdvertServiceImpl extends ServiceImpl<EquipmentAdvertMapper, EquipmentAdvert> implements EquipmentAdvertService {

    @Resource
    private EquipmentAdvertMapper equipmentAdvertMapper;
    
    /**
     * 当前广告位所要展示的图片
     * @author: sgmark@aliyun.com
     * @Date: 2019/11/13 0013
     * @Param: 
     * @return: 
     */
    @Override
    public List<Map<String, Object>> iotEquipmentAdvertList() {
        return equipmentAdvertMapper.selectMaps(new EntityWrapper<EquipmentAdvert>().setSqlSelect("pic_url as picUrl").eq("del_flag", 0).ge("end_time", LocalDate.now()).le("start_time", LocalDate.now()+" 23:59:59"));
    }
}
