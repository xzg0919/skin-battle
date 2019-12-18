package com.tzj.collect.core.service;

import com.tzj.collect.entity.VoucherMember;

import java.util.List;

public interface DailyVoucherMemberService {
    /**
     *
     * <p>Created on 2019年12月3日</p>
     * <p>Description:[使用复活卡]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return void
     */
    void useRevive(String aliId);
    /**
     *
     * <p>Created on 2019年12月3日</p>
     * <p>Description:[没用过的复活卡的id]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return List<String>
     */
    List<VoucherMember> getReviveIdList(String aliId);
    /**
     *
     * <p>Created on 2019年12月3日</p>
     * <p>Description:[没用过的复活卡的数量]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return List<String>
     */
    Integer getReviveCount(String aliId);
}
