package com.skin.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.skin.dto.InvitationPage;
import com.skin.dto.UserPage;
import com.skin.entity.Invitation;
import org.apache.ibatis.annotations.Param;

public interface InvitationMapper extends BaseMapper<Invitation> {
    Page<InvitationPage> getInvitationPage(@Param("page") Page<UserPage> page, @Param("userId") Long userId );

    Page<InvitationPage> getInvitationLogPage(@Param("page") Page<UserPage> page, @Param("userId") Long userId );

}
