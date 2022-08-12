package com.skin.api.admin;

import com.skin.core.service.CdkService;
import com.skin.params.AdminBean;
import com.skin.params.CdkBean;
import com.tzj.module.api.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import static com.skin.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/10 09:57
 * @Description:
 */
@ApiService
public class CdkApi {

    @Autowired
    CdkService cdkService;


    @Api(name = "cdk.getPage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getCdkPage(CdkBean cdkBean){
        return cdkService.getCdkPage(cdkBean.getPageBean().getPageNum(),cdkBean.getPageBean().getPageSize());
    }

    @Api(name = "cdk.changeStatus", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object changeStatus(CdkBean cdkBean){
        cdkService.changeStatus(cdkBean.getId());
        return "success";
    }

    @Api(name = "cdk.delete", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object deleteCdk(CdkBean cdkBean){
        cdkService.deleteCdk(cdkBean.getId());
        return "success";
    }


    @Api(name = "cdk.update", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object updateCdk(CdkBean cdkBean){
        cdkService.updateCdk(cdkBean.getId(),cdkBean.getCdkVal());
        return "success";
    }

    @Api(name = "cdk.insert", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object insertCdk(CdkBean cdkBean){
        cdkService.insertCdk(cdkBean.getCdkVal());
        return "success";
    }





}
