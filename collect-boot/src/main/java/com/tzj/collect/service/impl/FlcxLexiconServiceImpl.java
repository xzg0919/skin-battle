package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.taobao.api.ApiException;
import com.tzj.collect.api.lexicon.param.FlcxBean;
import com.tzj.collect.api.lexicon.result.FlcxResult;
import com.tzj.collect.entity.FlcxLexicon;
import com.tzj.collect.entity.FlcxRecords;
import com.tzj.collect.mapper.FlcxLexiconMapper;
import com.tzj.collect.mapper.FlcxRecordsMapper;
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
            map.put("msg", "empty");
        }
        flcxRecordsMapper.insert(flcxRecords);
        return map;
    }
}
