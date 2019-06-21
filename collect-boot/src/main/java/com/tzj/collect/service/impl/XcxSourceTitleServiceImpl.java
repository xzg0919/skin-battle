package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.XcxSourceTitle;
import com.tzj.collect.mapper.XcxSourceTitleMapper;
import com.tzj.collect.service.XcxSourceTitleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly=true)
public class XcxSourceTitleServiceImpl extends ServiceImpl<XcxSourceTitleMapper, XcxSourceTitle> implements XcxSourceTitleService {



}
