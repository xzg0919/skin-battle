package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.commom.redis.RedisUtil;
import com.tzj.collect.api.lexicon.param.FlcxBean;
import com.tzj.collect.api.lexicon.result.FlcxResult;
import com.tzj.collect.entity.FlcxEggshell;
import com.tzj.collect.entity.FlcxLexicon;
import com.tzj.collect.entity.FlcxRecords;
import com.tzj.collect.mapper.FlcxLexiconMapper;
import com.tzj.collect.service.FlcxEggshellService;
import com.tzj.collect.service.FlcxLexiconService;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param 
  * @return 
  */
@Service
@Transactional(readOnly = true)
public class FlcxLexiconServiceImpl extends ServiceImpl<FlcxLexiconMapper, FlcxLexicon> implements FlcxLexiconService {
    @Resource
    private FlcxLexiconMapper flcxLexiconMapper;
    @Resource
    private FlcxEggshellService flcxEggshellService;
    @Autowired
    private RedisUtil redisUtil;






    @Transactional(readOnly = false)
    @Cacheable(value = "lexCheckMap" , key = "#flcxBean.name", sync = true)
    public Map lexCheck(FlcxBean flcxBean) throws ApiException {
        HashMap<String, Object> map = new HashMap<>();
        //根据名称从redis取出结果集
        Boolean object = null;
        if (!StringUtils.isBlank(flcxBean.getName())){
            object = redisUtil.hasKey(flcxBean.getName());
        }
        Map<String, Object> resultMap = null;
        if (true == object|| flcxBean.getTypeId()> 0){
            FlcxRecords flcxRecords = new FlcxRecords();
            if (StringUtils.isEmpty(flcxBean.getName()) && flcxBean.getTypeId() == 0 || StringUtils.isEmpty(flcxBean.getAliUserId())){
                throw new ApiException("参数错误");
            }
            if (null == flcxBean.getName() || StringUtils.isEmpty(flcxBean.getName()) || flcxBean.getTypeId() != 0){
                flcxBean.setName("");
                flcxRecords.setLexicons(flcxBean.getTypeId().toString());
            }else {
                flcxBean.setTypeId(0L);
                flcxRecords.setLexicons(flcxBean.getName());
            }

            FlcxResult flcxResult = flcxLexiconMapper.lexCheck(flcxBean.getName(), flcxBean.getTypeId());
            //记录查询
            flcxRecords.setAliUserId(flcxBean.getAliUserId());
            if (null != flcxResult){
                flcxResult.setRemarksList(Arrays.asList(flcxResult.getRemarks().split(";")));
                map.put("results", flcxResult);
                map.put("msg", "success");
                flcxRecords.setLexiconAfter(flcxResult.getLexicon());
                flcxRecords.setLexiconsId(flcxResult.getLexiconId());
            }else {
                //搜索是否存在彩蛋
                FlcxEggshell flcxEggshell = flcxEggshellService.selectOne(new EntityWrapper<FlcxEggshell>().eq("del_flag", 0).eq("lexicon_", flcxBean.getName()));
                if (null != flcxEggshell){
                    map.put("msg", "eggshell");
                    map.put("describe", flcxEggshell.getDescribe());
                }else {
                    map.put("msg", "empty");
                }
            }
            map.put("flcxRecords", flcxRecords);
            //把最后的结果放入redis
            return map;
        }else {
            map.put("msg", "empty");
            return map;
        }
    }

    @Cacheable(value = "lexCheckList" , key = "#flcxString", sync = true)
    public List<FlcxLexicon> keySearch(String flcxString) throws ApiException {
        return flcxLexiconMapper.selectList(new EntityWrapper<FlcxLexicon>().eq("del_flag", 0));
    }


    public Map keySearchInRedis(FlcxBean flcxBean) throws ApiException {
        HashMap<String, Object> map = new HashMap<>();
        List<FlcxLexicon> flcxLexiconList = this.selectList(new EntityWrapper<FlcxLexicon>().eq("del_flag", 0));
        List<FlcxEggshell> flcxEggshellList = flcxEggshellService.selectList(new EntityWrapper<FlcxEggshell>().eq("del_flag", 0));
        flcxLexiconList.stream().forEach(flcxLexicon -> {
            redisUtil.set(flcxLexicon.getName(), 1);
        });
        flcxEggshellList.stream().forEach(flcxEggshell -> {
            redisUtil.set(flcxEggshell.getLexicon(), 1);
        });
        System.out.println();
        return map;
    }

    @Override
    @Cacheable(value = "lexCheckMap" , key = "#flcxBean.typeId", sync = true)
    public Map lexCheckByType(FlcxBean flcxBean) {
        HashMap<String, Object> map = new HashMap<>();
        //根据名称从redis取出结果集
        Boolean object = null;
        if (!StringUtils.isBlank(flcxBean.getName())){
            object = redisUtil.hasKey(flcxBean.getName());
        }
        Map<String, Object> resultMap = null;
        if (true == object|| flcxBean.getTypeId()> 0){
            FlcxRecords flcxRecords = new FlcxRecords();
            if (StringUtils.isEmpty(flcxBean.getName()) && flcxBean.getTypeId() == 0 || StringUtils.isEmpty(flcxBean.getAliUserId())){
                throw new ApiException("参数错误");
            }
            if (null == flcxBean.getName() || StringUtils.isEmpty(flcxBean.getName()) || flcxBean.getTypeId() != 0){
                flcxBean.setName("");
                flcxRecords.setLexicons(flcxBean.getTypeId().toString());
            }else {
                flcxBean.setTypeId(0L);
                flcxRecords.setLexicons(flcxBean.getName());
            }

            FlcxResult flcxResult = flcxLexiconMapper.lexCheck(flcxBean.getName(), flcxBean.getTypeId());
            //记录查询
            flcxRecords.setAliUserId(flcxBean.getAliUserId());
            if (null != flcxResult){
                flcxResult.setRemarksList(Arrays.asList(flcxResult.getRemarks().split(";")));
                map.put("results", flcxResult);
                map.put("msg", "success");
                flcxRecords.setLexiconAfter(flcxResult.getLexicon());
                flcxRecords.setLexiconsId(flcxResult.getLexiconId());
            }else {
                //搜索是否存在彩蛋
                FlcxEggshell flcxEggshell = flcxEggshellService.selectOne(new EntityWrapper<FlcxEggshell>().eq("del_flag", 0).eq("lexicon_", flcxBean.getName()));
                if (null != flcxEggshell){
                    map.put("msg", "eggshell");
                    map.put("describe", flcxEggshell.getDescribe());
                }else {
                    map.put("msg", "empty");
                }
            }
            map.put("flcxRecords", flcxRecords);
            //把最后的结果放入redis
            return map;
        }else {
            map.put("msg", "empty");
            return map;
        }
    }
}
