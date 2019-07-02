package com.tzj.collect.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;

import com.tzj.collect.api.lexicon.param.FlcxBean;
import com.tzj.collect.entity.FlcxLexicon;
import com.tzj.module.easyopen.exception.ApiException;

import java.util.Map;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param 
  * @return 
  */
public interface FlcxLexiconService extends IService<FlcxLexicon> {
    @DS("slave")
    Map lexCheck(FlcxBean flcxBean) throws ApiException;

    @DS("slave")
    Map keySearch(FlcxBean flcxBean) throws ApiException;
}
