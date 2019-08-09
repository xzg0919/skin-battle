package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.lexicon.param.FlcxBean;
import com.tzj.collect.api.lexicon.param.FlcxTypeBean;
import com.tzj.collect.entity.FlcxLexicon;
import com.tzj.collect.entity.FlcxLexiconType;
import com.tzj.collect.entity.FlcxType;
import com.tzj.collect.mapper.FlcxCityMapper;
import com.tzj.collect.mapper.FlcxLexiconMapper;
import com.tzj.collect.mapper.FlcxLexiconTypeMapper;
import com.tzj.collect.mapper.FlcxTypeMapper;
import com.tzj.collect.service.FlcxTypeService;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

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

    @Resource
    private FlcxLexiconMapper flcxLexiconMapper;

    @Resource
    private FlcxLexiconTypeMapper flcxLexiconTypeMapper;

    @Resource
    private FlcxCityMapper flcxCityMapper;

    @Override
    @Cacheable(value = "typeListMap" , key = "#flcxBean.cityName + '_'+ #flcxBean.cityId",   sync = true)
    public Map typeList(FlcxBean flcxBean) {
        HashMap<String, Object> map = new HashMap<>();
        //分类展示列表
        map.put("typeList", flcxTypeMapper.typeList(flcxBean.getCityName(), flcxBean.getCityId().toString()));
        //提供单位及联合发布
        Map<String, Object> synProMap = flcxCityMapper.synPro(flcxBean.getCityName(), flcxBean.getCityId().toString());
        if (CollectionUtils.isEmpty(synProMap)){
            synProMap = new HashMap<>();
            synProMap.put("provider", "归属地专业管理部门");
            synProMap.put("syndication", "浙江省长三角循环经济技术研究院");
            synProMap.put("remarks", "生活垃圾分类同时包括：大件垃圾、建筑装修垃圾。");
        }
        map.put("synPro", synProMap);
        return map;
    }

    @Override
    @Transactional(readOnly = false)
    public void inputLinAndType(List<Map<String, String>> mapList) {
        List<FlcxType> flcxTypes = flcxTypeMapper.selectList(new EntityWrapper<FlcxType>().eq("del_flag", 0).eq("level_", 1));
        mapList.parallelStream().forEach(map -> {
            FlcxLexicon flcxLexicon = new FlcxLexicon();
            flcxLexicon.setName(map.get("name"));
            if ("是".equals(map.get("recover"))){
                flcxLexicon.setRecover("1");
            }else {
                flcxLexicon.setRecover("0");
            }
            flcxTypes.stream().forEach(flcxType ->{
                if (map.get("type").contains(flcxType.getName())){
                    flcxLexicon.setParentId(flcxType.getParentId());
                }
            });
            try {
                flcxLexicon.setCreateDate(new Date());
                flcxLexicon.setUpdateDate(new Date());
                flcxLexiconMapper.insert(flcxLexicon);
            }catch (Exception e){
                e.printStackTrace();
            }
//            String[] stringSet = map.get("type").split("/");
//            List<String> stringList = Arrays.asList(stringSet);
//            //保存类型
//            stringList.stream().forEach(string -> {
//                flcxTypes.stream().forEach(flcxType -> {
//                    if (flcxType.getName().equals(string)){
//                        FlcxLexiconType flcxLexiconType = new FlcxLexiconType();
//                        flcxLexiconType.setLexiconId(flcxLexicon.getId());
//                        flcxLexiconType.setTypeId(flcxType.getId());
//                        flcxLexiconType.setParentId(flcxType.getParentId());
//                        flcxLexiconType.setCreateDate(new Date());
//                        flcxLexiconType.setUpdateDate(new Date());
//                        flcxLexiconTypeMapper.insert(flcxLexiconType);
//                    }
//                });
//            });
        });
    }



    /**
     * 根据层级获取该层级的所有分类
     * @param typeBean
     * @return
     * @throws ApiException
     */
    @Override
    public Map listAllByType(FlcxTypeBean typeBean){
        //默认返回第一层级别
        HashMap<String, Object> map = new HashMap<>();
        if(StringUtils.isEmpty(typeBean.getLevel())){
            map.put("typeList", flcxTypeMapper.selectList(new EntityWrapper<FlcxType>().eq("del_flag", 0).eq("level_", 0).eq("parent_id", 0)));
            return map;
        }
        //具体返回某一个层级的所有类型
        map.put("typeList", flcxTypeMapper.selectList(new EntityWrapper<FlcxType>().eq("del_flag", typeBean.getLevel()).eq("level_", typeBean.getLevel())));
        return map;
    }


    /**
     * 根据分类上级id查询 所有子分类
     * @param typeBean
     * @return
     * @throws ApiException
     */
    @Override
    public Map findTypeByParent(FlcxTypeBean typeBean){
        //具体返回某一个层级的所有类型
        HashMap<String, Object> map = new HashMap<>();

        //默认返回第一层级别
        if(StringUtils.isEmpty(typeBean.getParentId())){
            map.put("msg","error");
            return map;
        }
        map.put("typeList", flcxTypeMapper.selectList
                (new EntityWrapper<FlcxType>().eq("del_flag", 0).
                        eq("parent_id", typeBean.getParentId())));
        return map;
    }

}
