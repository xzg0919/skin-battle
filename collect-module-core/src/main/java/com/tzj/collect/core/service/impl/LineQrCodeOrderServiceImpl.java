package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.LineQrCodeOrderMapper;
import com.tzj.collect.core.mapper.LineQrCodeRangeMapper;
import com.tzj.collect.core.service.LineQrCodeOrderService;
import com.tzj.collect.core.service.LineQrCodeRangeService;
import com.tzj.collect.entity.LineQrCodeOrder;
import com.tzj.collect.entity.LineQrCodeRange;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
@Service
@Transactional(readOnly = true)
public class LineQrCodeOrderServiceImpl extends ServiceImpl<LineQrCodeOrderMapper, LineQrCodeOrder> implements LineQrCodeOrderService {

    @Override
    @Transactional(readOnly = false)
    public Boolean insertQrCodeOrder(String orderNo, String qrCode) {
        LineQrCodeOrder qrCodeOrder = new LineQrCodeOrder();
        qrCodeOrder.setShareCode(qrCode);
        qrCodeOrder.setOrderNo(orderNo);
        return this.insert(qrCodeOrder);
    }
}
