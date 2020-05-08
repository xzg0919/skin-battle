package com.tzj.green.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.green.entity.Recyclers;
import com.tzj.green.param.RecyclersBean;

import java.util.List;
import java.util.Map;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [回收人员service]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
public interface RecyclersService extends IService<Recyclers>
{

    @DS("slave")
    Recyclers selectByMobile(String mobile);

    Map<String, Object> bindingCardByRec(RecyclersBean recyclersBean);

    Map<String, Object> selectRecRange(Long id);

    List<Map<String, Object>> categoryPointInfo(Long id);

    Map<String, Object> appChangePoint(Map<String, Object> paramMap,Long RecyclerId);

    String getAuthCode(String authCode, Long recyclersId) throws Exception;

    Map<String, Object> pointsList(RecyclersBean recyclersBean);

    Object updatePassword(Long recyclersId,RecyclersBean recyclersBean);

    Object getAreaDetail(String parentId);

    Object getCommunityByStreetId(String streetId,String communityName);

    Object getCompanyAddressByLocal(Long recyclerId,String lng,String lat);

    Object checkCardNo(String realNo,Long recyclerId);
}