package com.tzj.collect.core.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.admin.AdminShareCodeBean;
import com.tzj.collect.entity.LineQrCode;

import java.util.Map;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
public interface LineQrCodeService extends IService<LineQrCode> {
    Map<String, Object> createShareCode(AdminShareCodeBean adminShareCodeBean);

    Page<LineQrCode> lineQrCodePage(AdminShareCodeBean adminShareCodeBean);

    Map<String, Object> lineQrCodeDel(AdminShareCodeBean adminShareCodeBean);

    Map<String, Object> lineQrCodeDetail(AdminShareCodeBean adminShareCodeBean);
}
