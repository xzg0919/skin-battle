package com.tzj.collect.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.api.ali.param.MemberAddressBean;
import com.tzj.collect.entity.MemberAddress;

import java.util.List;

/**
 * @Author 王灿
 **/
public interface MemberAddressMapper extends BaseMapper<MemberAddress>{


    List<MemberAddressBean> selectMemberAddressByCommunityId();

    List<MemberAddressBean> selectMemberAddressBystreetId();
}
