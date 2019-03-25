package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.XcxThemeContent;
import com.tzj.collect.mapper.XcxThemeContentMapper;
import com.tzj.collect.service.XcxThemeContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly=true)
public class XcxThemeContentServiceImpl extends ServiceImpl<XcxThemeContentMapper, XcxThemeContent> implements XcxThemeContentService {

    @Autowired
    private XcxThemeContentMapper xcxThemeContentMapper;



}
