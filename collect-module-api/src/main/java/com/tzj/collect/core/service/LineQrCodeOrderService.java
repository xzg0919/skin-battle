package com.tzj.collect.core.service;


import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.LineQrCodeOrder;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
public interface LineQrCodeOrderService extends IService<LineQrCodeOrder> {
    Boolean insertQrCodeOrder(String orderNo, String qrCode);
}
