package com.tzj.collect.core.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.XcxThemeMapper;
import com.tzj.collect.core.service.XcxThemeService;
import com.tzj.collect.entity.XcxTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class XcxThemeServiceImpl extends ServiceImpl<XcxThemeMapper, XcxTheme> implements XcxThemeService {
    @Autowired
    private XcxThemeMapper xcxThemeMapper;

}
