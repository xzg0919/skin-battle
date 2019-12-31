package com.tzj.collect.api.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tzj.collect.core.param.admin.AdminShareCodeBean;
import com.tzj.collect.core.service.LineQrCodeService;
import com.tzj.collect.entity.LineQrCode;
import com.tzj.module.api.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

@ApiService
public class AdminShareCodeApi {

    @Autowired
    private LineQrCodeService lineQrCodeService;

    /**
     * 根据层用户用和密码登录获取token
     * @param
     * @return
     */
    @Api(name = "admin.create.share.code", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Map<String, Object> createShareCode(AdminShareCodeBean adminShareCodeBean){
       return lineQrCodeService.createShareCode(adminShareCodeBean);
    }
    /**
     * 分享码分页
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/30 0030
     * @Param: 
     * @return: 
     */
    @Api(name = "admin.page.share.code", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Page<LineQrCode> lineQrCodePage(AdminShareCodeBean adminShareCodeBean){
        return lineQrCodeService.lineQrCodePage(adminShareCodeBean);
    }
    /**
     * 删除分享码
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/30 0030
     * @Param: 
     * @return: 
     */
    @Api(name = "admin.del.share.code", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Map<String, Object> lineQrCodeDel(AdminShareCodeBean adminShareCodeBean){
        return lineQrCodeService.lineQrCodeDel(adminShareCodeBean);
    }

    /**
     * 分享码详情
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/30 0030
     * @Param: 
     * @return: 
     */
    @Api(name = "admin.share.code.detail", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Map<String, Object> lineQrCodeDetail(AdminShareCodeBean adminShareCodeBean){
        return lineQrCodeService.lineQrCodeDetail(adminShareCodeBean);
    }
    @Api(name = "admin.share.code.report", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Map<String, Object> lineQrCodeReport(AdminShareCodeBean adminShareCodeBean){
        return lineQrCodeService.lineQrCodeReport(adminShareCodeBean);
    }
    @Api(name = "admin.share.code.street", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public List<Map<String, Object>> lineQrCodeStreet(AdminShareCodeBean adminShareCodeBean){
        return lineQrCodeService.lineQrCodeStreet(adminShareCodeBean);
    }

}
