package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.core.mapper.MallSkinMapper;
import com.skin.core.mapper.SkinMapper;
import com.skin.core.service.*;
import com.skin.entity.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/11 13:48
 * @Description:
 */
@Service
@Transactional(readOnly = true)
public class MallSkinServiceImpl extends ServiceImpl<MallSkinMapper, MallSkin> implements MallSkinService {


    @Autowired
    PointService pointService;
    @Autowired
    SysParamsService sysParamsService;

    @Autowired
    TakeOrderService takeOrderService;

    @Autowired
    UserService userService;

    @Override
    public Page<MallSkin> getSkinPage(Integer pageNum, Integer pageSize, String skinName) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(skinName)) {
            queryWrapper.eq("goods_name", skinName);
        }
        queryWrapper.orderByDesc("create_date");
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Page<MallSkin> getSkinPage(Integer pageNum, Integer pageSize, String skinName, Integer isDesc) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(skinName)) {
            queryWrapper.eq("goods_name", skinName);
        }
        queryWrapper.gt("stock", 0);
        if (isDesc == 1) {
            queryWrapper.orderByDesc("price");
        } else if (isDesc == 0) {
            queryWrapper.orderByAsc("price");
        } else {
            queryWrapper.orderByAsc("create_date");
        }
        queryWrapper.select("id", "goods_name", "price", "attrition_rate,goods_pic");
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void exchangeSkin(Long userId, Long skinId) {
        MallSkin mallSkin = baseMapper.selectById(skinId);
        if (mallSkin.getStock() <= 0) {
            throw new RuntimeException("库存不足");
        }
        PointInfo pointInfo = pointService.getByUid(userId);
        if (pointInfo.getPoint().compareTo(mallSkin.getPrice()) < 0) {
            throw new RuntimeException("积分不足");
        }

        User user = userService.getById(userId);
        String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);
        TakeOrder takeOrder = new TakeOrder();
        takeOrder.setUserId(userId);
        takeOrder.setOrderNo(orderNo);
        takeOrder.setSource(6);
        takeOrder.setSkinName(mallSkin.getGoodsName());
        BeanUtils.copyProperties(user, takeOrder);
        takeOrder.setStatus(0);
        takeOrder.setPrice(mallSkin.getPrice());
        takeOrder.setPicUrl(mallSkin.getGoodsPic());
        takeOrderService.save(takeOrder);

        int from = Integer.parseInt(sysParamsService.getSysParams("business_type", "商城兑换", "10"));
        pointService.editPoint(userId, mallSkin.getPrice().negate(), from, "商城兑换", orderNo, 0);
    }
}
