package com.skin.api.admin;

import com.skin.core.service.SysParamsService;
import com.skin.entity.SysParams;
import com.skin.params.SysParamBean;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

import static com.skin.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/11 14:55
 * @Description:
 */
@ApiService
public class SysParamsApi {

    @Autowired
    SysParamsService sysParamsService;


    @Api(name = "kefu.get", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object kefuGet() {
        HashMap<String, String> map = new HashMap<>();
        String qq = sysParamsService.getSysParams("kefu_qq").getVal();
        String QrCode = sysParamsService.getSysParams("qq_qrcode").getVal();
        map.put("qq", qq);
        map.put("QrCode", QrCode);
        return map;
    }

    @Api(name = "kefu.edit", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object kefuEdit(SysParamBean sysParams) {
        SysParams QQ = sysParamsService.getSysParams("kefu_qq");
        SysParams qrcode = sysParamsService.getSysParams("qq_qrcode");
        QQ.setVal(sysParams.getQq());
        qrcode.setVal(sysParams.getQrCode());
        sysParamsService.updateById(QQ);
        sysParamsService.updateById(qrcode);
        return "success";
    }

}
