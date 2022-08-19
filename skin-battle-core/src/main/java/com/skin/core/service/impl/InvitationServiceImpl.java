package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.core.mapper.InvitationMapper;
import com.skin.core.service.InvitationService;
import com.skin.dto.InvitationPage;
import com.skin.entity.Invitation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/17 16:16
 * @Description:
 */
@Service
@Transactional(readOnly = true,rollbackFor = Exception.class)
public class InvitationServiceImpl extends ServiceImpl<InvitationMapper, Invitation> implements InvitationService {


    @Override
    public Integer getInvitationCount(Long userId) {
         return  baseMapper.selectCount(new QueryWrapper<Invitation>().eq("user_id", userId)).intValue();
    }

    @Override
    public BigDecimal getInvitationAmount(Long userId) {
       return baseMapper.selectOne(new QueryWrapper<Invitation>().eq("user_id", userId)
               .select("sum(recharge_amount) as rechargeAmount")).getRechargeAmount();
    }

    @Override
    public Page<InvitationPage> getInvitationPage(Integer pageNum, Integer pageSize, Long userId) {
        Page page = new Page<>(pageNum, pageSize);
        return baseMapper.getInvitationPage(page, userId);
    }

    @Override
    public Page<InvitationPage> getInvitationLogPage(Integer pageNum, Integer pageSize, Long userId) {
        Page page = new Page<>(pageNum, pageSize);
        return baseMapper.getInvitationLogPage(page, userId);
    }
}
