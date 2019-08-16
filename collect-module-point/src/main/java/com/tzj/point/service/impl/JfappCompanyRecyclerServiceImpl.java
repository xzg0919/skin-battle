package com.tzj.point.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.point.entity.JfappCompanyRecycler;
import com.tzj.point.mapper.JfappCompanyRecyclerMapper;
import com.tzj.point.service.JfappCompanyRecyclerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class JfappCompanyRecyclerServiceImpl extends ServiceImpl<JfappCompanyRecyclerMapper, JfappCompanyRecycler> implements JfappCompanyRecyclerService {


}
