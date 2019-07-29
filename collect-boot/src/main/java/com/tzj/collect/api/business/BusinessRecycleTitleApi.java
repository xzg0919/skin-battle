package com.tzj.collect.api.business;


import com.tzj.collect.core.param.business.RecyclersServiceRangeBean;
import com.tzj.collect.core.service.RecyclersTitleService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.BUSINESS_API_COMMON_AUTHORITY;

@ApiService
public class BusinessRecycleTitleApi {

    @Autowired
    private RecyclersTitleService recyclersTitleService;

    /**
     * 获取回收人员的回收类型范围
     * @param
     * @return
     */
    @Api(name = "business.title.getRecyclerTitleList", version = "1.0")
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public List<Map<String, Object>> getRecyclerTitleList(RecyclersServiceRangeBean recyclersServiceRangeBean){

        return recyclersTitleService.getRecyclerTitleList(recyclersServiceRangeBean.getRecycleId());
    }




}
