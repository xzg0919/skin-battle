package com.skin.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.vo.InvitationPage;
import com.skin.entity.Invitation;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface InvitationService extends IService<Invitation> {

    Integer getInvitationCount(Long userId);

    BigDecimal getInvitationAmount(Long userId);


    Integer invitationPercentage(Long userId);

    Page<InvitationPage> getInvitationPage(Integer pageNum,Integer pageSize ,Long userId);

    Page<InvitationPage> getInvitationLogPage(Integer pageNum,Integer pageSize ,Long userId);

    List<HashMap<String, Object>> userInvitationRule();


    Invitation getInvitationByUserId(Long inviteUserId);
}
