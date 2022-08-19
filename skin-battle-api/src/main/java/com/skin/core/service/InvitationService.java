package com.skin.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.dto.InvitationPage;
import com.skin.entity.Invitation;
import org.apache.shiro.crypto.hash.Hash;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public interface InvitationService extends IService<Invitation> {

    Integer getInvitationCount(Long userId);

    BigDecimal getInvitationAmount(Long userId);


    Integer invitationPercentage(Long userId);

    Page<InvitationPage> getInvitationPage(Integer pageNum,Integer pageSize ,Long userId);

    Page<InvitationPage> getInvitationLogPage(Integer pageNum,Integer pageSize ,Long userId);

    List<HashMap<String, Object>> userInvitationRule();
}
