package com.tzj.green.serviceImpl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.green.entity.Logs;
import com.tzj.green.mapper.LogsMapper;
import com.tzj.green.service.LogsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LogsServiceImpl extends ServiceImpl<LogsMapper, Logs> implements LogsService {


}
