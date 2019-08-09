package com.tzj.collect.core.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.RecyclersTitleMapper;
import com.tzj.collect.core.service.RecyclersTitleService;
import com.tzj.collect.entity.RecyclersTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly=true)
public class RecyclersTitleServiceServiceImpl extends ServiceImpl<RecyclersTitleMapper,RecyclersTitle> implements RecyclersTitleService {

    @Autowired
    private RecyclersTitleMapper recyclersTitleMapper;


    @Override
    public List<Map<String, Object>> getRecyclerTitleList(String recycleId) {

        return recyclersTitleMapper.getRecyclerTitleList(recycleId);
    }
}
