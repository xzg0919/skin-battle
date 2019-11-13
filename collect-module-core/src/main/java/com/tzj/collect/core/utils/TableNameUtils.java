package com.tzj.collect.core.utils;

import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.MemberAddress;
import com.tzj.collect.module.common.shard.ShardTableHelper;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;

public class TableNameUtils {

    public static String getMemberTableName(Member member){
        if(null==member|| StringUtils.isBlank(member.getAliUserId())){
            throw new ApiException("aliUserId不能为空");
        }
        return ShardTableHelper.getTableNameByModeling("sb_member",Long.parseLong(member.getAliUserId()),40);
    }
    public static String getMemberAddressTableName(MemberAddress memberAddress){
        if(null==memberAddress|| StringUtils.isBlank(memberAddress.getAliUserId())){
            throw new ApiException("aliUserId不能为空");
        }
        return ShardTableHelper.getTableNameByModeling("sb_member_address",Long.parseLong(memberAddress.getAliUserId()),40);
    }
}
