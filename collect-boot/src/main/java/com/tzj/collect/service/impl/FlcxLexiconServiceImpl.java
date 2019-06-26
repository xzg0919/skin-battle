package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.taobao.api.ApiException;
import com.tzj.collect.api.lexicon.param.FlcxBean;
import com.tzj.collect.api.lexicon.result.FlcxResult;
import com.tzj.collect.entity.FlcxEggshell;
import com.tzj.collect.entity.FlcxLexicon;
import com.tzj.collect.entity.FlcxRecords;
import com.tzj.collect.mapper.FlcxLexiconMapper;
import com.tzj.collect.mapper.FlcxRecordsMapper;
import com.tzj.collect.service.FlcxEggshellService;
import com.tzj.collect.service.FlcxLexiconService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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
    private FlcxRecordsMapper flcxRecordsMapper;

    @Resource
    private FlcxEggshellService flcxEggshellService;

    @Transactional(readOnly = false)
    public Map lexCheck(FlcxBean flcxBean) throws ApiException {
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

        HashMap<String, Object> map = new HashMap<>();
        FlcxResult flcxResult = flcxLexiconMapper.lexCheck(flcxBean.getName(), flcxBean.getTypeId());
        //记录查询
        flcxRecords.setAliUserId(flcxBean.getAliUserId());
        if (null != flcxResult){
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
        flcxRecordsMapper.insert(flcxRecords);
        return map;
    }


    /**
     * 根据关键字查询返回结果满足条件的结果集
     * @param flcxBean
     * @return
     * @throws ApiException
     */
    @Transactional(readOnly = false)
    public Map keySearch(FlcxBean flcxBean) throws ApiException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("result",flcxLexiconMapper.selectList(new EntityWrapper<FlcxLexicon>().eq("del_flag", 0).like("name_", flcxBean.getName()+"%")));
        return map;
    }
}
