package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.common.handler.VipHandler;
import com.skin.common.util.AssertUtil;
import com.skin.core.mapper.VipRewardMapper;
import com.skin.core.service.PointService;
import com.skin.core.service.SysParamsService;
import com.skin.core.service.UserService;
import com.skin.core.service.VipRewardService;
import com.skin.dto.VipCheckList;
import com.skin.entity.User;
import com.skin.entity.VipReward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/18 14:26
 * @Description:
 */
@Service
@Transactional(readOnly = true,rollbackFor = Exception.class)
public class VipRewardServiceImpl extends ServiceImpl<VipRewardMapper, VipReward> implements VipRewardService {

    @Autowired
    UserService userService;

    @Autowired
    PointService pointService;
    @Autowired
    SysParamsService sysParamsService;



    @Transactional(rollbackFor = Exception.class)
    @Override
    public void receiveVipReward(Long userId, Integer level) {
        //判断成长值够不够
        User user = userService.getById(userId);
        BigDecimal limit =  VipHandler.vipLevelMap.get(level);
        if(user.getGrowthValue().compareTo(limit) < 0){
            throw new RuntimeException("成长值不够");
        }
        //判断是否已经领取过
        AssertUtil.isNotNull(baseMapper.selectOne(new QueryWrapper<VipReward>().eq("user_id",userId).eq("level_",level)),"已经领取过");
        VipReward vipReward = new VipReward();
        vipReward.setUserId(userId);
        vipReward.setLevel(level);
        vipReward.setReward(VipHandler.vipAwardMap.get(level));
        baseMapper.insert(vipReward);
        int from = Integer.parseInt(sysParamsService.getSysParams("business_type", "VIP等级赠送", "8"));
        pointService.editPoint(userId, VipHandler.vipAwardMap.get(level), from, "VIP等级赠送", vipReward.getId().toString(), 1);
    }

    @Override
    public List<VipCheckList> getVipCheckList(Long userId,BigDecimal growthValue) {
        List<VipCheckList> vipCheckLists =  new ArrayList<>();
        List<VipReward> vipRewards = baseMapper.selectList(new QueryWrapper<VipReward>().eq("user_id",userId));
        for (Map.Entry<Integer, BigDecimal> entry : VipHandler.vipLevelMap.entrySet()) {
            VipCheckList vipCheckList = new VipCheckList();
            vipCheckList.setVipLevel("VIP"+entry.getKey());
            vipCheckList.setCondition(entry.getValue());
            vipCheckList.setCanReceive(growthValue.compareTo(entry.getValue()) >= 0?1:0);
            vipCheckList.setReward(VipHandler.vipAwardMap.get(entry.getKey()));
            vipCheckList.isReceive(vipRewards.stream().filter(v -> v.getLevel().equals(entry.getKey())).findAny().isPresent());
            vipCheckLists.add(vipCheckList);
        }
        return vipCheckLists;
    }
}
