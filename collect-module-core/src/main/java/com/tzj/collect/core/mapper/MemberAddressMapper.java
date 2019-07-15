package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.core.param.ali.MemberAddressBean;
import com.tzj.collect.entity.MemberAddress;

import java.util.List;

/**
 * @Author 王灿
 **/
public interface MemberAddressMapper extends BaseMapper<MemberAddress>{


    List<MemberAddressBean> selectMemberAddressByCommunityId();

    List<MemberAddressBean> selectMemberAddressBystreetId();
}
