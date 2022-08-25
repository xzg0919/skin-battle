package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.common.lottery.LotteryBean;
import com.skin.common.lottery.LotteryUtils;
import com.skin.common.util.AssertUtil;
import com.skin.core.mapper.BlindBoxMapper;
import com.skin.core.mapper.BlindBoxSKinMapper;
import com.skin.core.service.*;
import com.skin.entity.*;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    UserService userService;
    @Autowired
    PointService pointService;
    @Autowired
    SysParamsService sysParamsService;
    @Autowired
    TakeOrderService takeOrderService;

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<TakeOrder> openBox(Long userId, Long boxId, Integer num) {
        AssertUtil.isTrue(num <= 0 || num > 5, "不能超过五个");
        //判断用户是否填写steam链接
        User user = userService.getById(userId);
        AssertUtil.isBlank(user.getSteamUrl(), "您还没有绑定Steam交易链接，请先绑定链接以继续游戏");
        BlindBox blindBox = this.getById(boxId);
        AssertUtil.isNull(blindBox, "该盒子不存在");
        AssertUtil.isTrue(blindBox.getEnable() == 0, "该盒子已经关闭");
        PointInfo pointInfo = pointService.getByUid(userId);
        BigDecimal totalPrice = blindBox.getDiscountPrice().multiply(new BigDecimal(num));
        AssertUtil.isTrue(totalPrice.compareTo(pointInfo.getPoint()) > 0, "余额不足，请先充值");
        List<BlindBoxSkin> blindBoxSkins = blindBoxSKinMapper.selectList(new QueryWrapper<BlindBoxSkin>().eq("box_id", boxId));
        if (blindBoxSkins == null || blindBoxSkins.size() == 0) {
            throw new ApiException("该盒子没有皮肤");
        }
        List<LotteryBean> lotteryBeans = new ArrayList<>();
        blindBoxSkins.forEach(blindBoxSkin -> {
            LotteryBean lotteryBean = new LotteryBean();
            lotteryBean.setGoodsId(blindBoxSkin.getId());
            lotteryBean.setBili(blindBoxSkin.getProbability());
            //判断是否设置了单独的概率
            if (user.getHighProbability() != null && blindBoxSkin.getLevel() == 2) {
                lotteryBean.setBili(user.getHighProbability());
            }
            if (user.getMiddleProbability() != null  && blindBoxSkin.getLevel() == 1) {
                lotteryBean.setBili(user.getMiddleProbability());
            }
            if (user.getLowProbability() != null  && blindBoxSkin.getLevel() == 0) {
                lotteryBean.setBili(user.getLowProbability());
            }
            lotteryBeans.add(lotteryBean);
        });
        List<HashMap<String, Object>> lotteryResult = LotteryUtils.lottery(lotteryBeans, num);

        List<TakeOrder> takeOrders = new ArrayList<>();
        //存入背包
        lotteryResult.forEach(map -> {
            BlindBoxSkin blindBoxSkin = blindBoxSKinMapper.selectById((Long) map.get("goodsId"));
            AssertUtil.isNull(blindBoxSkin, "网络异常，请稍后再试");
            TakeOrder takeOrder = new TakeOrder();
            takeOrder.setBoxId(boxId);
            takeOrder.setUserId(userId);
            String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);
            takeOrder.setOrderNo(orderNo);
            takeOrder.setSource(Integer.parseInt(sysParamsService.getSysParams("blind_box").getVal()));
            takeOrder.setSkinName(blindBoxSkin.getSkinName());
            takeOrder.setNickName(user.getNickName());
            takeOrder.setSteamUrl(user.getSteamUrl());
            takeOrder.setTel(user.getTel());
            takeOrder.setEmail(user.getEmail());
            takeOrder.setStatus(0);
            takeOrder.setPrice(blindBoxSkin.getPrice());
            takeOrder.setAttritionRate(blindBoxSkin.getAttritionRate());
            takeOrder.setLevel(blindBoxSkin.getLevel());
            takeOrder.setPicUrl(blindBoxSkin.getPicUrl());
            takeOrder.setAvatar(user.getAvatar());
            takeOrders.add(takeOrder);
            int from = Integer.parseInt(sysParamsService.getSysParams("business_type", "开箱", "1"));
            pointService.editPoint(userId, blindBox.getDiscountPrice(), from, "开箱", orderNo, 0);

        });

        takeOrderService.saveBatch(takeOrders);
        return takeOrders;
    }

    @Override
    public BlindBox getBoxById(Long id) {
       return baseMapper.selectOne(
               new QueryWrapper<BlindBox>().eq("id", id)
                       .select("id,box_name,price,discount_price,box_pic,discount,skin_pic,high_Probability,middle_Probability,low_Probability"));
    }

    @Override
    public List<BlindBoxSkin> getSkinByBoxId(Long boxId) {
        return blindBoxSKinMapper.selectList(new QueryWrapper<BlindBoxSkin>().eq("box_id", boxId)
                .select("skin_name,attrition_rate,level_,price,pic_url")
                .orderByDesc("level_")
        );
    }


}
