package com.tzj.collect.flcx.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.tzj.collect.core.param.flcx.FlcxBean;
import com.tzj.collect.core.param.flcx.FlcxEggshellBean;
import com.tzj.collect.core.param.flcx.FlcxLexiconBean;
import com.tzj.collect.core.param.flcx.FlcxTypeBean;
import com.tzj.collect.core.service.FlcxEggshellService;
import com.tzj.collect.core.service.FlcxLexiconService;
import com.tzj.collect.core.service.FlcxTypeService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.easyopen.exception.ApiException;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2019/7/5
 * Title: TODO
 * Description: TODO
 * Copyright: Copyright (c) 2019
 * Company: 铸乾信息科技有限公司
 * Department:研发部
 *
 * @author:Michael_Wang
 * @Version 1.0
 **/
@ApiService
public class LexiconBackendApi {
    @Resource
    private FlcxLexiconService flcxLexiconService;

    @Resource
    private FlcxTypeService flcxTypeService;

    @Resource
    private FlcxEggshellService flcxEggshellService;


    /**
     * 根据层级获取该层级的所有分类
     * @param typeBean
     * @return
     * @throws ApiException
     */
    @Api(name = "backend.listAllByType", version = "1.0")
    @AuthIgnore
    public Map listAllByType(FlcxTypeBean typeBean)throws ApiException {
        return flcxTypeService.listAllByType(typeBean);
    }


    /**
     * 根据关键字搜索 支持模糊查询
     * @param flcxBean
     * @return
     * @throws ApiException
     */
    @Api(name = "backend.searchByKey", version = "1.0")
    @AuthIgnore
    @SignIgnore
    public Page searchByKey(FlcxBean flcxBean)throws ApiException {
        return flcxLexiconService.searchByName(flcxBean);
    }


    /**
     * 根据关键字搜索彩蛋 支持模糊查询
     * @param flcxEggshellBean
     * @return
     * @throws ApiException
     */
    @Api(name = "backend.searchEggshellByKey", version = "1.0")
    @AuthIgnore
    @SignIgnore
    public Page searchEggshellByKey(FlcxEggshellBean flcxEggshellBean)throws ApiException {
        return flcxEggshellService.searchEggshellByKey(flcxEggshellBean);
    }

    /**
     * 根据分类上级id查询 所有子分类
     * @param flcxTypeBean
     * @return
     * @throws ApiException
     */
    @Api(name = "backend.findTypeByParent", version = "1.0")
    @AuthIgnore
    @SignIgnore
    public Map findTypeByParent(FlcxTypeBean flcxTypeBean) throws ApiException {
        return flcxTypeService.findTypeByParent(flcxTypeBean);
    }


    /**
     * 新增彩蛋
     * @param flcxEggshellBean
     * @return
     * @throws ApiException
     */
    @Api(name = "backend.addEggShell", version = "1.0")
    @AuthIgnore
    @SignIgnore
    public Map addEggShell (FlcxEggshellBean flcxEggshellBean) throws ApiException {
        return flcxEggshellService.addEggShell(flcxEggshellBean);
    }


    /**
     * 判断彩蛋是否已存在
     * @param flcxEggshellBean
     * @return
     * @throws ApiException
     */
    @Api(name = "backend.isExistEggShell", version = "1.0")
    @AuthIgnore
    @SignIgnore
    public Map isExistEggShell (FlcxEggshellBean flcxEggshellBean) throws ApiException {
        HashMap<String, Object> map = new HashMap<>();
        if(flcxEggshellService.isExistEggShell(flcxEggshellBean)){
           throw new ApiException("彩蛋已存在!");
        }else {
            map.put("msg","success");
        }
        return  map;
    }


    /**
     * 修改彩蛋信息
     * @param flcxEggshellBean
     * @return
     * @throws ApiException
     */
    @Api(name = "backend.updateEggShell", version = "1.0")
    @AuthIgnore
    @SignIgnore
    public Map updateEggShell (FlcxEggshellBean flcxEggshellBean) throws ApiException {
        HashMap<String, Object> map = new HashMap<>();
        if(null == flcxEggshellBean.getId()){
            throw new ApiException("id不能为空!");
        }
        if(flcxEggshellBean.getId()<=0){
            throw new ApiException("id参数错误");
        }
        if(flcxEggshellService.isExistEggShell(flcxEggshellBean)){
            throw new ApiException("彩蛋已存在!");
        }else {
            flcxEggshellService.updateEggShell(flcxEggshellBean);
            map.put("msg","success");
        }
        return  map;
    }

    /**
     * 删除彩蛋信息
     * @param flcxEggshellBean
     * @return
     * @throws ApiException
     */
    @Api(name = "backend.deleteEggShell", version = "1.0")
    @AuthIgnore
    @SignIgnore
    public Map deleteEggShell (FlcxEggshellBean flcxEggshellBean) throws ApiException {
        if(null == flcxEggshellBean.getId()){
            throw new ApiException("id不能为空!");
        }
        if(flcxEggshellBean.getId()<=0){
            throw new ApiException("id参数错误");
        }
        HashMap<String, Object> map = new HashMap<>();
        flcxEggshellService.deleteEggShell(flcxEggshellBean);
        map.put("msg","success");
        return  map;
    }

    /**
     * 新增词库关键词
     * @param flcxLexiconBean
     * @return
     * @throws ApiException
     */
    @Api(name = "backend.addFlcxBean", version = "1.0")
    @AuthIgnore
    @SignIgnore
    public Map addFlcxLexiconBean (FlcxLexiconBean flcxLexiconBean) throws ApiException {
        return flcxLexiconService.addFlcxLexiconBean(flcxLexiconBean);
    }


    /**
     * 判断关键词是否已存在
     * @param flcxLexiconBean
     * @return
     * @throws ApiException
     */
    @Api(name = "backend.isExistFlcxLexicon", version = "1.0")
    @AuthIgnore
    @SignIgnore
    public Map isExistFlcxLexicon (FlcxLexiconBean flcxLexiconBean) throws ApiException {
        HashMap<String, Object> map = new HashMap<>();
        if(flcxLexiconService.isExistFlcxLexicon(flcxLexiconBean)){
            throw new ApiException("关键词已存在!");
        }else {
            map.put("msg","success");
        }
        return  map;
    }

    /**
     * 删除关键词
     * @param flcxLexiconBean
     * @return
     * @throws ApiException
     */
    @Api(name = "backend.deleteFlcxLexicon", version = "1.0")
    @AuthIgnore
    @SignIgnore
    public Map deleteFlcxLexicon (FlcxLexiconBean flcxLexiconBean) throws ApiException {
        if(null == flcxLexiconBean.getId()){
            throw new ApiException("id不能为空!");
        }
        if(flcxLexiconBean.getId()<=0){
            throw new ApiException("id参数错误");
        }
        HashMap<String, Object> map = new HashMap<>();
        flcxLexiconService.deleteFlcxLexicon(flcxLexiconBean);
        map.put("msg","success");
        return  map;
    }


    /**
     * 修改关键字 如果传入了typeIds 说明需要修改类型 否则默认不修改
     * @param flcxLexiconBean
     * @return
     * @throws ApiException
     */
    @Api(name = "backend.updateFlcxLexicon", version = "1.0")
    @AuthIgnore
    @SignIgnore
    public Map updateFlcxLexicon (FlcxLexiconBean flcxLexiconBean) throws ApiException {
        if(null == flcxLexiconBean.getId()){
            throw new ApiException("id不能为空!");
        }
        if(flcxLexiconBean.getId()<=0){
            throw new ApiException("id参数错误");
        }
        HashMap<String, Object> map = new HashMap<>();
        flcxLexiconService.updateFlcxLexicon(flcxLexiconBean);
        map.put("msg","success");
        return  map;
    }

}
