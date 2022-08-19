package com.skin.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.dto.VipCheckList;
import com.skin.entity.VipReward;

import java.util.List;

public interface VipRewardService extends IService<VipReward> {

    void receiveVipReward(Long userId, Integer level);

    List<VipCheckList> getVipCheckList(Long userId);
}
