package com.tzj.collect.core.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.TitleMapper;
import com.tzj.collect.core.service.TitleService;
import com.tzj.collect.entity.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class TitleServiceImpl extends ServiceImpl<TitleMapper,Title> implements TitleService {

    @Autowired
    private TitleMapper titleMapper;
}
