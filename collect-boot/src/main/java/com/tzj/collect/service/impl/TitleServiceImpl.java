package com.tzj.collect.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.Title;
import com.tzj.collect.mapper.TitleMapper;
import com.tzj.collect.service.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class TitleServiceImpl extends ServiceImpl<TitleMapper,Title> implements TitleService{

    @Autowired
    private TitleMapper titleMapper;
}
