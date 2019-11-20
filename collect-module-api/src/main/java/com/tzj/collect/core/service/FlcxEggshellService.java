package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.flcx.FlcxEggshellBean;
import com.tzj.collect.entity.FlcxEggshell;
import com.tzj.module.easyopen.exception.ApiException;

import java.util.Map;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param
  * @return 
  */
public interface FlcxEggshellService extends IService<FlcxEggshell> {

    /**
     * 根据关键字搜索彩蛋 支持模糊查询
     * @param flcxEggshellBean
     * @return
     * @throws ApiException
     */
    @DS("slave")
    public Page<FlcxEggshell> searchEggshellByKey(FlcxEggshellBean flcxEggshellBean);


    /**
     * 新增彩蛋
     * @param flcxEggshellBean
     * @return
     * @throws ApiException
     */
    public Map addEggShell (FlcxEggshellBean flcxEggshellBean);

    /**
     * 新增彩蛋
     * @param flcxEggshellBean
     * @return
     * @throws ApiException
     */
    @DS("slave")
    public boolean isExistEggShell (FlcxEggshellBean flcxEggshellBean);


    /**
     * 修改彩蛋
     * @param flcxEggshellBean
     * @return
     * @throws ApiException
     */
    public Map updateEggShell (FlcxEggshellBean flcxEggshellBean);


    /**
     * 删除彩蛋
     * @param flcxEggshellBean
     * @return
     * @throws ApiException
     */
    public Map deleteEggShell (FlcxEggshellBean flcxEggshellBean);


}
