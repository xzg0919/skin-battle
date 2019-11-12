package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.RecyclersTitle;

import java.util.List;
import java.util.Map;

public interface RecyclersTitleMapper extends BaseMapper<RecyclersTitle> {

    List<Map<String, Object>> getRecyclerTitleList(String recycleId);


}
