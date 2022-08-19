package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.common.handler.InvitationHandler;
import com.skin.core.mapper.InvitationMapper;
import com.skin.core.service.InvitationService;
import com.skin.core.service.PointListService;
import com.skin.dto.InvitationPage;
import com.skin.entity.Invitation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/17 16:16
 * @Description:
 */
@Service
@Transactional(readOnly = true,rollbackFor = Exception.class)
public class InvitationServiceImpl extends ServiceImpl<InvitationMapper, Invitation> implements InvitationService {

@Autowired
    PointListService pointListService;




    @Override
    public Integer getInvitationCount(Long userId) {
         return  baseMapper.selectCount(new QueryWrapper<Invitation>().eq("user_id", userId)).intValue();
    }

    @Override
    public BigDecimal getInvitationAmount(Long userId) {
       return baseMapper.selectOne(new QueryWrapper<Invitation>().eq("user_id", userId)
               .select("ifnull(sum(recharge_amount),0) as rechargeAmount")).getRechargeAmount();
    }

    @Override
    public Integer invitationPercentage(Long userId) {
        return InvitationHandler.getReward(getInvitationAmount(userId));
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

    @Override
    public List<HashMap<String, Object>> userInvitationRule() {
        List<HashMap<String, Object>> result =new ArrayList<>();
        for (Map.Entry<Integer, BigDecimal> entry : InvitationHandler.invitationLevelMap.entrySet()) {
            result.add(new HashMap<String, Object>(){{
                put("level",entry.getKey());
                put("amount",entry.getValue());
                put("percentage",InvitationHandler.invitationRewardMap.get(entry.getKey()));
            }});
        }
        return result;
    }
}
