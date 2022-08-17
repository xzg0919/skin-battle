package com.skin.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.core.mapper.InvitationMapper;
import com.skin.core.service.InvitationService;
import com.skin.entity.Invitation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/17 16:16
 * @Description:
 */
@Service
@Transactional(readOnly = true,rollbackFor = Exception.class)
public class InvitationServiceImpl extends ServiceImpl<InvitationMapper, Invitation> implements InvitationService {


}
