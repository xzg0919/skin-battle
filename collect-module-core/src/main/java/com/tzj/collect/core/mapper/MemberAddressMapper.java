package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.core.param.ali.MemberAddressBean;
import com.tzj.collect.entity.MemberAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author 王灿
 **/
public interface MemberAddressMapper extends BaseMapper<MemberAddress>{

    List<MemberAddress> selectMemberAddress(@Param("memberAddress") MemberAddress memberAddress);

    Integer deleteMemberAddressByAliUserId(@Param("memberAddress") MemberAddress memberAddress);

    Integer updateMemberAddressByAliUserId(@Param("memberAddress") MemberAddress memberAddress);

    Integer insertMemberAddress(@Param("memberAddress") MemberAddress memberAddress);
}
