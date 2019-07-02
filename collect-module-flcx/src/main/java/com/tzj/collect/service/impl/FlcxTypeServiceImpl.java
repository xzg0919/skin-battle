package com.tzj.collect.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.FlcxLexicon;
import com.tzj.collect.entity.FlcxLexiconType;
import com.tzj.collect.entity.FlcxType;
import com.tzj.collect.mapper.FlcxLexiconMapper;
import com.tzj.collect.mapper.FlcxLexiconTypeMapper;
import com.tzj.collect.mapper.FlcxTypeMapper;
import com.tzj.collect.service.FlcxLexiconService;
import com.tzj.collect.service.FlcxTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public Map typeList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("typeList", flcxTypeMapper.selectList(new EntityWrapper<FlcxType>().eq("del_flag", 0).eq("level_", 0).eq("parent_id", 0)));
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
            try {
                flcxLexiconMapper.insert(flcxLexicon);
            }catch (Exception e){
                e.printStackTrace();
            }
            String[] stringSet = map.get("type").split("/");
            List<String> stringList = Arrays.asList(stringSet);
            //保存类型
            stringList.stream().forEach(string -> {
                flcxTypes.stream().forEach(flcxType -> {
                    if (flcxType.getName().equals(string)){
                        FlcxLexiconType flcxLexiconType = new FlcxLexiconType();
                        flcxLexiconType.setLexiconId(flcxLexicon.getId());
                        flcxLexiconType.setTypeId(flcxType.getId());
                        flcxLexiconTypeMapper.insert(flcxLexiconType);
                    }
                });
            });
        });
    }


}
