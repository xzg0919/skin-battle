package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.common.util.AssertUtil;
import com.skin.core.mapper.PullBoxMapper;
import com.skin.core.mapper.PullBoxSkinMapper;
import com.skin.core.service.PullBoxService;
import com.skin.core.service.SkinService;
import com.skin.entity.PullBox;
import com.skin.entity.PullBoxSkin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @Auther: xiang
 * @Date: 2022/8/14 13:09
 * @Description:
 */
@Service
@Transactional(readOnly = true)
public class PullBoxServiceImpl extends ServiceImpl<PullBoxMapper, PullBox> implements PullBoxService {

    @Autowired
    PullBoxSkinMapper pullBoxSkinMapper;
    @Autowired
    SkinService skinService;

    @Override
    public Page<PullBox> getPage(Integer pageNo, Integer pageSize, String skinName) {
        QueryWrapper<PullBox> queryWrapper = new QueryWrapper<PullBox>().orderByDesc("create_date");
        if (StringUtils.isNotBlank(skinName)) {
            queryWrapper.like("skin_name", skinName);
        }
        return baseMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
    }

    @Transactional
    @Override
    public void changeStatus(Long id) {
        PullBox pullBox = this.getById(id);
        AssertUtil.isNull(pullBox, "更改的数据不存在");
        if (pullBox.getEnable() == 1) {
            pullBox.setEnable(0);
        } else {
            pullBox.setEnable(1);
        }
        this.updateById(pullBox);
    }

    @Override
    public Page<PullBoxSkin> getSkinPage(Integer pageNo, Integer pageSize, String skinName) {
        QueryWrapper<PullBoxSkin> queryWrapper = new QueryWrapper<PullBoxSkin>().orderByDesc("create_date");
        if (StringUtils.isNotBlank(skinName)) {
            queryWrapper.like("skin_name", skinName);
        }
        return pullBoxSkinMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
    }


    @Transactional
    @Override
    public void delSkin(Long skinId) {
        PullBoxSkin pullBoxSkin = pullBoxSkinMapper.selectById(skinId);
        AssertUtil.isNull(pullBoxSkin, "删除的数据不存在");
        pullBoxSkinMapper.deleteById(skinId);
    }

    @Override
    public PullBoxSkin getSkinById(Long skinId) {
        return pullBoxSkinMapper.selectById(skinId);
    }

    @Transactional
    @Override
    public void updateSkin(PullBoxSkin pullBoxSkin) {
        pullBoxSkinMapper.updateById(pullBoxSkin);
    }

    @Transactional
    @Override
    public void insertSkin(Long skinId,Long pullBoxId, double probability) {
        Optional.ofNullable(skinService.getById(skinId)).ifPresent(skin -> {
            PullBoxSkin pullBoxSkin = new PullBoxSkin();
            pullBoxSkin.setPicUrl(skin.getPicUrl());
            pullBoxSkin.setPullBoxId(pullBoxId);
            pullBoxSkin.setSkinName(skin.getName());
            pullBoxSkin.setAttritionRate(skin.getAttritionRate());
            pullBoxSkin.setLevel(skin.getLevel());
            pullBoxSkin.setPrice(skin.getPrice());
            pullBoxSkin.setProbability(probability);
            pullBoxSkinMapper.insert(pullBoxSkin);
        });

    }
}
