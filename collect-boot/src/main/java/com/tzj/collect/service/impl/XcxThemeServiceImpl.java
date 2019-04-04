package com.tzj.collect.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.XcxTheme;
import com.tzj.collect.mapper.XcxThemeMapper;
import com.tzj.collect.service.XcxThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class XcxThemeServiceImpl extends ServiceImpl<XcxThemeMapper, XcxTheme> implements XcxThemeService {
    @Autowired
    private XcxThemeMapper xcxThemeMapper;

}
