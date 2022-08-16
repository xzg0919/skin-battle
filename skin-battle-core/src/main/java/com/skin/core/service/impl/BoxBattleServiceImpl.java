package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.core.mapper.BlindBoxMapper;
import com.skin.core.mapper.BlindBoxSKinMapper;
import com.skin.core.mapper.BoxBattleMapper;
import com.skin.core.mapper.BoxBattleSkinMapper;
import com.skin.core.service.BlindBoxService;
import com.skin.core.service.BoxBattleService;
import com.skin.core.service.SkinService;
import com.skin.entity.BlindBox;
import com.skin.entity.BlindBoxSkin;
import com.skin.entity.BoxBattle;
import com.skin.entity.BoxBattleSkin;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @Auther: xiang
 * @Date: 2022/8/14 16:04
 * @Description:
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class BoxBattleServiceImpl extends ServiceImpl<BoxBattleMapper, BoxBattle> implements BoxBattleService {

    @Autowired
    BoxBattleSkinMapper boxBattleSkinMapper;
    @Autowired
    SkinService skinService;

    @Override
    public Page<BoxBattle> getPage(Integer pageNo, Integer pageSize, String boxName) {

        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(boxName)) {
            queryWrapper.like("box_name", boxName);
        }
        queryWrapper.orderByDesc("create_date");
        return baseMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
    }

    @Transactional
    @Override
    public void changeStatus(Long id) {
        BoxBattle boxBattle = this.getById(id);
        if (boxBattle == null) {
            throw new ApiException("更改的数据不存在");
        }
        if (boxBattle.getStatus() == 1) {
            boxBattle.setStatus(0);
        } else {
            boxBattle.setStatus(1);
        }
        this.updateById(boxBattle);
    }

    @Override
    public Page<BoxBattleSkin> getSkinPage(Integer pageNo, Integer pageSize, String skinName) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(skinName)) {
            queryWrapper.like("skin_name", skinName);
        }
        queryWrapper.orderByDesc("create_date");
        return boxBattleSkinMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
    }

    @Transactional
    @Override
    public void delBoxSkin(Long id) {
        boxBattleSkinMapper.deleteById(id);
    }

    @Override
    public BoxBattleSkin getSkinById(Long id) {
        return boxBattleSkinMapper.selectById(id);
    }

    @Transactional
    @Override
    public void updateSkin(BoxBattleSkin boxBattleSkin) {
        boxBattleSkinMapper.updateById(boxBattleSkin);
    }

    @Transactional
    @Override
    public void insertSkin(Long boxId, Long skinId, double probability) {
        Optional.ofNullable(skinService.getById(skinId)).ifPresent(skin -> {
            BoxBattleSkin boxBattleSkin = new BoxBattleSkin();
            boxBattleSkin.setPicUrl(skin.getPicUrl());
            boxBattleSkin.setBoxBattleId(boxId);
            boxBattleSkin.setSkinName(skin.getName());
            boxBattleSkin.setAttritionRate(skin.getAttritionRate());
            boxBattleSkin.setLevel(skin.getLevel());
            boxBattleSkin.setPrice(skin.getPrice());
            boxBattleSkin.setProbability(probability);
            boxBattleSkinMapper.insert(boxBattleSkin);
        });
    }
}
