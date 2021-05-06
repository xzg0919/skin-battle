package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.commom.constant.WXConst;
import com.tzj.collect.core.mapper.WxTransferMapper;
import com.tzj.collect.core.service.WxTransferService;
import com.tzj.collect.entity.WxTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class WxTransferServiceImpl extends ServiceImpl<WxTransferMapper, WxTransfer> implements WxTransferService {


    @Autowired
    WxTransferMapper transferMapper;


    @Override
    public List<WxTransfer> getFailList() {
        return selectList(new EntityWrapper<WxTransfer>().eq("result_code", WXConst.FAIL_CODE));
    }

    @Override
    public WxTransfer findByPartnerTradeNo(String partnerTradeNo) {
        return selectOne(new EntityWrapper<WxTransfer>().eq("partner_trade_no",partnerTradeNo)
                .eq("del_flag","0"));
    }
}
