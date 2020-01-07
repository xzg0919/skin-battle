package com.tzj.green.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.green.entity.CompanyCommunity;
import com.tzj.green.param.CompanyCommunityBean;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [社区表service]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
public interface CompanyCommunityService extends IService<CompanyCommunity>
{
    Object saveCompanyCommunity(CompanyCommunityBean companyCommunityBean,Long companyId);

    Object getCompanyCommunityById(String id);

    Object getCompanyCommunityList(CompanyCommunityBean companyCommunityBean,Long companyId);

    Object getCompanyCommunityListByStreetId(CompanyCommunityBean companyCommunityBean,Long companyId);

    Object getRecyclerListByHouseId(CompanyCommunityBean companyCommunityBean,Long companyId);
}