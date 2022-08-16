package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.common.util.AssertUtil;
import com.skin.core.mapper.RollRoomMapper;
import com.skin.core.mapper.RollRoomSkinInfoMapper;
import com.skin.core.mapper.RollRoomUserMapper;
import com.skin.core.service.RollRoomService;
import com.skin.core.service.SkinService;
import com.skin.entity.RollRoom;
import com.skin.entity.RollRoomSkinInfo;
import com.skin.entity.RollRoomUser;
import com.skin.entity.Skin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @Auther: xiang
 * @Date: 2022/8/13 11:54
 * @Description:
 */

@Service
@Transactional(readOnly = true)
public class RollRoomServiceImpl extends ServiceImpl<RollRoomMapper, RollRoom> implements RollRoomService {


    @Autowired
    RollRoomSkinInfoMapper rollRoomSkinInfoMapper;
    @Autowired
    SkinService skinService;

    @Autowired
    RollRoomUserMapper rollRoomUserMapper;

    @Override
    public Page<RollRoom> getRoomPage(Integer pageNo, Integer pageSize, Integer roomType, String roomName) {
        QueryWrapper<RollRoom> queryWrapper = new QueryWrapper<RollRoom>().orderByDesc("create_date");
        if (roomType != null && roomType != 0) {
            queryWrapper.eq("room_type", roomType);
        }
        if (StringUtils.isNotBlank(roomName)) {
            queryWrapper.eq("name_", roomName);
        }
        return baseMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
    }

    @Override
    public Page<RollRoomSkinInfo> getRoomSkinPage(Integer pageNo, Integer pageSize, String skinName,Long roomId) {
        QueryWrapper<RollRoomSkinInfo> queryWrapper = new QueryWrapper<RollRoomSkinInfo>().orderByDesc("create_date");
        queryWrapper.eq("room_id", roomId);
        if (StringUtils.isNotBlank(skinName)) {
            queryWrapper.like("skin_name", skinName);
        }
        return rollRoomSkinInfoMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
    }


    @Override
    public RollRoomSkinInfo getRoomSkinInfo(Long id) {
        return rollRoomSkinInfoMapper.selectById(id);
    }

    @Transactional
    @Override
    public void delRoomSkinInfo(Long id) {
        rollRoomSkinInfoMapper.deleteById(id);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addToRoom(Long SkinId, Long roomId) {
            Optional.ofNullable(skinService.getById(SkinId)).ifPresent(skin -> {
            RollRoomSkinInfo sameInfo = rollRoomSkinInfoMapper.selectOne(new QueryWrapper<RollRoomSkinInfo>().eq("skin_name", skin.getName()).eq("room_id", roomId));
            AssertUtil.isNotNull(sameInfo, "该皮肤已经存在于该房间中");
            RollRoomSkinInfo rollRoomSkinInfo = new RollRoomSkinInfo();
            rollRoomSkinInfo.setSkinName(skin.getName());
            rollRoomSkinInfo.setPicUrl(skin.getPicUrl());
            rollRoomSkinInfo.setPrice(skin.getPrice());
            rollRoomSkinInfo.setAttritionRate(skin.getAttritionRate());
            rollRoomSkinInfo.setLevel(skin.getLevel());
            rollRoomSkinInfo.setRoomId(roomId);
            rollRoomSkinInfoMapper.insert(rollRoomSkinInfo);
        });
    }

    @Override
    public BigDecimal sumRoomPrice(Long roomId) {
        return rollRoomSkinInfoMapper.selectOne(new QueryWrapper<RollRoomSkinInfo>().eq("room_id",roomId).select("sum(price) as price")).getPrice();
    }

    @Override
    public Page<RollRoomUser> getRoomUserPage(Integer pageNo, Integer pageSize, Long roomId) {
        QueryWrapper<RollRoomUser> queryWrapper = new QueryWrapper<RollRoomUser>().orderByDesc("create_date");
        queryWrapper.eq("room_id", roomId);
        return rollRoomUserMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRoomSkinInfo(RollRoomSkinInfo rollRoomSkinInfo) {
        rollRoomSkinInfoMapper.updateById(rollRoomSkinInfo);
    }


}
