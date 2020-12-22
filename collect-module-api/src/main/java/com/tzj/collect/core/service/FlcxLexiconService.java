package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import com.tzj.collect.core.param.flcx.FlcxBean;
import com.tzj.collect.core.param.flcx.FlcxLexiconBean;
import com.tzj.collect.core.param.flcx.FlcxThirdBean;
import com.tzj.collect.entity.FlcxLexicon;
import com.tzj.module.easyopen.exception.ApiException;

import java.util.List;
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
    List<FlcxLexicon> keySearch(String flcxString) throws ApiException;
    @DS("slave")
    Map keySearchInRedis(FlcxBean flcxBean);
//    @DS("slave")
//    Map lexCheckByType(FlcxBean flcxBean);

    /**
     * 根据关键字搜索 支持模糊查询
     * @param flcxBean
     * @return
     * @throws ApiException
     */
    @DS("slave")
    Page searchByName(FlcxBean flcxBean);


    /**
     * 新增词库关键词
     * @param flcxLexiconBean
     * @return
     * @throws ApiException
     */
    public Map addFlcxLexiconBean (FlcxLexiconBean flcxLexiconBean);


    /**
     * 判断是否已经存在该关键字
     * @param flcxLexiconBean
     * @return
     * @throws ApiException
     */
    @DS("slave")
    public boolean isExistFlcxLexicon (FlcxLexiconBean flcxLexiconBean);


    /**
     * 删除关键字
     * @param flcxLexiconBean
     * @return
     * @throws ApiException
     */
    public Map deleteFlcxLexicon (FlcxLexiconBean flcxLexiconBean);


    /**
     * 修改关键字 如果传入了typeIds 说明需要修改类型 否则默认不修改
     * @param flcxLexiconBean
     * @return
     * @throws ApiException
     */
    public Map updateFlcxLexicon (FlcxLexiconBean flcxLexiconBean);

    public Map lexThirdCheck(FlcxThirdBean flcxBean);

}
