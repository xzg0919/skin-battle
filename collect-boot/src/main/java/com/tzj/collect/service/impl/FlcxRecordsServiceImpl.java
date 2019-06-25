package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.FlcxRecords;
import com.tzj.collect.entity.FlcxType;
import com.tzj.collect.mapper.FlcxRecordsMapper;
import com.tzj.collect.mapper.FlcxTypeMapper;
import com.tzj.collect.service.FlcxRecordsService;
import com.tzj.collect.service.FlcxTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param 
  * @return 
  */
@Service
@Transactional(readOnly = true)
public class FlcxRecordsServiceImpl extends ServiceImpl<FlcxRecordsMapper, FlcxRecords> implements FlcxRecordsService {

    @Resource
    private FlcxRecordsMapper flcxRecordsMapper;

    @Override
    public Map topFive() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("typeList", flcxRecordsMapper.selectList(new EntityWrapper<FlcxRecords>().eq("del_flag", 0).isNotNull("lexicon_after").groupBy("lexicon_after").setSqlSelect("count(1) as count_, lexicon_after").orderBy("count_", false).last("limit 0, 5")));
        return map;
    }
}
