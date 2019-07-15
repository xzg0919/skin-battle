package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.FlcxLexiconType;
import com.tzj.collect.mapper.FlcxLexiconTypeMapper;
import com.tzj.collect.service.FlcxLexiconTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param 
  * @return 
  */
@Service
@Transactional(readOnly = true)
public class FlcxLexiconTypeServiceImpl extends ServiceImpl<FlcxLexiconTypeMapper, FlcxLexiconType> implements FlcxLexiconTypeService {

}
