package com.tzj.collect.core.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.DailyLexicon;

import java.util.List;
import java.util.Map;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
public interface DailyLexiconMapper extends BaseMapper<DailyLexicon> {
    /** 所有的题库(id, name, ,typeId, depth)
     * @author sgmark@aliyun.com
     * @date 2019/8/9 0009
     * @param
     * @return
     */
    List<Map<String, Object>> lexiconList();
}
