package com.tzj.collect.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.RecyclersTitle;

import java.util.List;
import java.util.Map;

public interface RecyclersTitleService extends IService<RecyclersTitle> {

    @DS("slave")
    List<Map<String, Object>> getRecyclerTitleList(String recycleId);

}
