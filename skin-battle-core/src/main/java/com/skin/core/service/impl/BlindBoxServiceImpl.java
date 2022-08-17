package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.common.util.AssertUtil;
import com.skin.core.mapper.BlindBoxMapper;
import com.skin.core.mapper.BlindBoxSKinMapper;
import com.skin.core.service.BlindBoxService;
import com.skin.core.service.SkinService;
import com.skin.entity.BlindBox;
import com.skin.entity.BlindBoxSkin;
import com.skin.entity.CdkInfo;
import com.skin.entity.PullBoxSkin;
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
public class BlindBoxServiceImpl extends ServiceImpl<BlindBoxMapper, BlindBox> implements BlindBoxService {

    @Autowired
    BlindBoxSKinMapper blindBoxSKinMapper;
    @Autowired
    SkinService skinService;

    @Override
    public Page<BlindBox> getPage(Integer pageNo, Integer pageSize, String boxName, Long boxType) {

        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(boxName)) {
            queryWrapper.like("box_name", boxName);
        }
        if (boxType != null && boxType != 0L) {
            queryWrapper.eq("box_type", boxType);
        }
        queryWrapper.orderByDesc("create_date");
        return baseMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
    }

    @Transactional
    @Override
    public void changeStatus(Long id) {
        BlindBox blindBox = this.getById(id);
        if (blindBox == null) {
            throw new ApiException("更改的数据不存在");
        }
        if (blindBox.getEnable() == 1) {
            blindBox.setEnable(0);
        } else {
            blindBox.setEnable(1);
        }
        this.updateById(blindBox);
    }

    @Override
    public Page<BlindBoxSkin> getSkinPage(Integer pageNo, Integer pageSize, String skinName) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(skinName)) {
            queryWrapper.like("skin_name", skinName);
        }
        queryWrapper.orderByDesc("create_date");
        return blindBoxSKinMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
    }

    @Transactional
    @Override
    public void delBoxSkin(Long id) {
        blindBoxSKinMapper.deleteById(id);
    }

    @Override
    public BlindBoxSkin getSkinById(Long id) {
        return blindBoxSKinMapper.selectById(id);
    }

    @Override
    public void updateSkin(BlindBoxSkin blindBoxSkin) {
        blindBoxSKinMapper.updateById(blindBoxSkin);
    }

    @Transactional
    @Override
    public void insertSkin(Long boxId, Long skinId, double probability) {
        Optional.ofNullable(skinService.getById(skinId)).ifPresent(skin -> {
            BlindBoxSkin sameSkin = blindBoxSKinMapper.selectOne(new QueryWrapper<BlindBoxSkin>().eq("skin_name", skin.getName()).eq("box_id", boxId));
            AssertUtil.isNotNull(sameSkin, "该皮肤已经存在");
            BlindBoxSkin blindBoxSkin = new BlindBoxSkin();
            blindBoxSkin.setPicUrl(skin.getPicUrl());
            blindBoxSkin.setBoxId(boxId);
            blindBoxSkin.setSkinName(skin.getName());
            blindBoxSkin.setAttritionRate(skin.getAttritionRate());
            blindBoxSkin.setLevel(skin.getLevel());
            blindBoxSkin.setPrice(skin.getPrice());
            blindBoxSkin.setProbability(probability);
            blindBoxSKinMapper.insert(blindBoxSkin);
        });
    }

    @Override
    public Page<BlindBox> getBoxByType(Integer pageNo, Integer pageSize, Long boxType) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (boxType != null && boxType != 0L) {
            queryWrapper.eq("box_type", boxType);
        }
        queryWrapper.eq("enable_", 1);
        queryWrapper.select("id,box_name,price,discount_price,box_pic,discount,skin_pic");
        queryWrapper.orderByDesc("create_date");
        return baseMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
    }
}
