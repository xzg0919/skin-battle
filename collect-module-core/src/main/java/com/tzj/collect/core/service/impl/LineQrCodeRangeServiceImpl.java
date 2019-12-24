package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.LineQrCodeRangeMapper;
import com.tzj.collect.core.service.LineQrCodeRangeService;
import com.tzj.collect.entity.LineQrCodeRange;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
@Service
@Transactional(readOnly = true)
public class LineQrCodeRangeServiceImpl extends ServiceImpl<LineQrCodeRangeMapper, LineQrCodeRange> implements LineQrCodeRangeService {


}
