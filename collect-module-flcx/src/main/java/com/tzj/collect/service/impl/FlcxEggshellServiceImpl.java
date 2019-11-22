package com.tzj.collect.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.param.flcx.FlcxEggshellBean;
import com.tzj.collect.entity.FlcxEggshell;
import com.tzj.collect.mapper.FlcxEggshellMapper;
import com.tzj.collect.core.service.FlcxEggshellService;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param 
  * @return 
  */
@Component
@Service(version = "${flcx.service.version}")
@Transactional(readOnly = true)
public class FlcxEggshellServiceImpl extends ServiceImpl<FlcxEggshellMapper, FlcxEggshell> implements FlcxEggshellService {

    /**
     * 根据关键字搜索彩蛋 支持模糊查询
     * @param flcxEggshellBean
     * @return
     * @throws ApiException
     */
    @Override
    public Page<FlcxEggshell> searchEggshellByKey(FlcxEggshellBean flcxEggshellBean){
        Page<FlcxEggshell> page = null;
        if(null == flcxEggshellBean.getPageBean()){
            page = new Page<FlcxEggshell>(1,10);
        }else {
            page = new Page<FlcxEggshell>(flcxEggshellBean.getPageBean() == null ? 1 : flcxEggshellBean.getPageBean().getPageNumber(), flcxEggshellBean.getPageBean().getPageSize() == null ? 10 : flcxEggshellBean.getPageBean().getPageSize());
        }
        HashMap<String, Object> map = new HashMap<>();
        EntityWrapper<FlcxEggshell> wrapper = new EntityWrapper<FlcxEggshell>();
        wrapper.eq("del_flag",0);
        wrapper.like("lexicon_",flcxEggshellBean.getLexicon());
        return this.selectPage(page,wrapper);
    }


    /**
     * 判断彩蛋是否已经存在
     */
    @Override
    @DS("slave")
    public boolean isExistEggShell(FlcxEggshellBean flcxEggshellBean){
        EntityWrapper<FlcxEggshell> wrapper = new EntityWrapper<FlcxEggshell>();
        wrapper.eq("lexicon_",flcxEggshellBean.getLexicon());
        wrapper.eq("del_flag",0);
        if(flcxEggshellBean.getId()!=null && flcxEggshellBean.getId()>0){
            wrapper.ne("id",flcxEggshellBean.getId());
        }
        return this.selectCount(wrapper)>0 ? true:false;

    }


    /**
     * 新增彩蛋
     * @param flcxEggshellBean
     * @return
     * @throws ApiException
     */
    @Override
    @Transactional(readOnly = false)
    public Map addEggShell (FlcxEggshellBean flcxEggshellBean){
        HashMap<String, Object> map = new HashMap<>();
        if(StringUtils.isBlank(flcxEggshellBean.getLexicon())){
            throw  new ApiException("名称不能为空");
        }
        if(StringUtils.isBlank(flcxEggshellBean.getDescribe())){
            throw  new ApiException("说明不能为空");
        }

        if(isExistEggShell(flcxEggshellBean)){
            throw  new ApiException("该彩蛋已存在");
        }
        FlcxEggshell flcxEggshell = new FlcxEggshell();
        flcxEggshell.setLexicon(flcxEggshellBean.getLexicon());
        flcxEggshell.setDescribe(flcxEggshellBean.getDescribe());
        flcxEggshell.setCreateDate(Calendar.getInstance().getTime());
        flcxEggshell.setUpdateDate(Calendar.getInstance().getTime());
        this.insert(flcxEggshell);

        map.put("msg","success");
        return map;
    }

    /**
     * 修改彩蛋
     * @param flcxEggshellBean
     * @return
     * @throws ApiException
     */
    @Override
    @Transactional(readOnly = false)
    public Map updateEggShell (FlcxEggshellBean flcxEggshellBean){
        HashMap<String, Object> map = new HashMap<>();
        if(null == flcxEggshellBean.getId()){
            throw new ApiException("id不能为空!");
        }
        if(flcxEggshellBean.getId()<=0){
            throw new ApiException("id参数错误");
        }
        if(StringUtils.isBlank(flcxEggshellBean.getLexicon())){
            throw  new ApiException("名称不能为空");
        }
        if(StringUtils.isBlank(flcxEggshellBean.getLexicon())){
            throw  new ApiException("名称不能为空");
        }
        if(StringUtils.isBlank(flcxEggshellBean.getDescribe())){
            throw  new ApiException("说明不能为空");
        }
        if(isExistEggShell(flcxEggshellBean)){
            throw  new ApiException("该彩蛋已存在");
        }
        FlcxEggshell flcxEggshell = this.selectById(flcxEggshellBean.getId());
        if(null == flcxEggshell){
            throw new ApiException("彩蛋不存在!");
        }
        flcxEggshell.setId(flcxEggshellBean.getId());
        flcxEggshell.setDescribe(flcxEggshellBean.getDescribe());
        flcxEggshell.setLexicon(flcxEggshellBean.getLexicon());
        flcxEggshell.setUpdateDate(Calendar.getInstance().getTime());
        this.updateById(flcxEggshell);
        map.put("msg","success");
        return map;
    }

    /**
     * 修改彩蛋
     * @param flcxEggshellBean
     * @return
     * @throws ApiException
     */
    @Override
    @Transactional(readOnly = false)
    public Map deleteEggShell (FlcxEggshellBean flcxEggshellBean){
        HashMap<String, Object> map = new HashMap<>();
        if(null == flcxEggshellBean.getId()){
            throw new ApiException("id不能为空!");
        }
        if(flcxEggshellBean.getId()<=0){
            throw new ApiException("id参数错误");
        }
        FlcxEggshell flcxEggshell = this.selectById(flcxEggshellBean.getId());

        if(null == flcxEggshell){
            throw new ApiException("彩蛋不存在!");
        }

        flcxEggshell.setUpdateDate(Calendar.getInstance().getTime());
        flcxEggshell.setDelFlag("1");
        this.updateById(flcxEggshell);
        map.put("msg","success");
        return map;
    }

}
