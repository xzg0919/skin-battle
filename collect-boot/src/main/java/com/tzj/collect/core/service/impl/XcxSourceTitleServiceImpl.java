package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.XcxSourceTitleMapper;
import com.tzj.collect.core.service.XcxSourceTitleService;
import com.tzj.collect.entity.XcxSourceTitle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly=true)
public class XcxSourceTitleServiceImpl extends ServiceImpl<XcxSourceTitleMapper, XcxSourceTitle> implements XcxSourceTitleService {



}
