package com.tzj.collect.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.FlcxLexiconType;
import com.tzj.collect.mapper.FlcxLexiconTypeMapper;
import com.tzj.collect.core.service.FlcxLexiconTypeService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param 
  * @return 
  */
@Component
@Service(version = "${flcx.service.version}")
@Transactional(readOnly = true)
public class FlcxLexiconTypeServiceImpl extends ServiceImpl<FlcxLexiconTypeMapper, FlcxLexiconType> implements FlcxLexiconTypeService {

}
