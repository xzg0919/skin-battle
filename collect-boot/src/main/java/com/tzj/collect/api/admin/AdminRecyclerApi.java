package com.tzj.collect.api.admin;


import com.tzj.collect.core.param.admin.AdminCommunityBean;
import com.tzj.collect.core.param.admin.RecyclersBean;
import com.tzj.collect.core.service.CompanyRecyclerService;
import com.tzj.collect.core.service.RecyclersService;
import com.tzj.collect.core.service.RecyclersTitleService;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.CompanyRecycler;
import com.tzj.collect.entity.Recyclers;
import com.tzj.collect.entity.RecyclersTitle;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.tzj.collect.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

@ApiService
public class AdminRecyclerApi {

    @Autowired
    private CompanyRecyclerService companyRecyclerService;
    @Autowired
    private RecyclersService recyclersService;
    @Autowired
    private RecyclersTitleService recyclersTitleService;


    //获取所有的回收人员列表
    @Api(name = "admin.recycler.getRecyclerList", version = "1.0")
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getRecyclerList(RecyclersBean recyclersBean) {
        return companyRecyclerService.getRecyclerList(recyclersBean.getRecyclerName(),recyclersBean.getRecyclerTel(),recyclersBean.getCityId(),recyclersBean.getPage());
    }
    //获取回收人员Id获取回收类型
    @Api(name = "admin.recycler.getRecyclerTitleById", version = "1.0")
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getRecyclerTitleById(RecyclersBean recyclersBean) {
        return recyclersTitleService.getRecyclerTitleById(recyclersBean.getRecyclerId());
    }
    //获取回收人员Id，公司Id和类型获取相关服务范围
    @Api(name = "admin.recycler.getRecyclerAreaByTitleId", version = "1.0")
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getRecyclerAreaByTitleId(RecyclersBean recyclersBean) {
        return recyclersService.getRecyclerAreaByTitleId(recyclersBean.getRecyclerId(),recyclersBean.getTitle(),recyclersBean.getCompanyId());
    }
    //根据回收人员Id获取回收人员相关信息
    @Api(name = "admin.recycler.getRecyclerDetailById", version = "1.0")
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getRecyclerDetailById(RecyclersBean recyclersBean) {
        return recyclersService.selectById(recyclersBean.getRecyclerId());
    }
    //关闭回收人员回收订单和区域
    @Api(name = "admin.recycler.closeRecyclerArea", version = "1.0")
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object closeRecyclerArea(RecyclersBean recyclersBean) {
        return companyRecyclerService.closeRecyclerArea(recyclersBean.getRecyclerId(),recyclersBean.getCompanyId(),recyclersBean.getType());
    }
    //关闭回收人员信息(禁止登陆)
    @Api(name = "admin.recycler.closeRecyclerCard", version = "1.0")
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object closeRecyclerCard(RecyclersBean recyclersBean) {
        return recyclersService.closeRecyclerCard(recyclersBean.getRecyclerId());
    }

}
