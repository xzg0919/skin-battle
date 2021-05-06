package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.WxTransfer;

import java.util.List;

public interface WxTransferService extends IService<WxTransfer> {

    List<WxTransfer> getFailList();

    WxTransfer findByPartnerTradeNo(String partnerTradeNo);
}
