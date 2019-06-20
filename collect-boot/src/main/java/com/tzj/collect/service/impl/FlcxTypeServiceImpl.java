package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.FlcxLexicon;
import com.tzj.collect.entity.FlcxType;
import com.tzj.collect.mapper.FlcxLexiconMapper;
import com.tzj.collect.mapper.FlcxTypeMapper;
import com.tzj.collect.service.FlcxLexiconService;
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
public class FlcxTypeServiceImpl extends ServiceImpl<FlcxTypeMapper, FlcxType> implements FlcxTypeService {

    @Resource
    private FlcxTypeMapper flcxTypeMapper;

    @Override
    public Map typeList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("typeList", flcxTypeMapper.selectList(new EntityWrapper<FlcxType>().eq("del_flag", 0).eq("level_", 0).eq("parent_id", 0)));
        return map;
    }
}
