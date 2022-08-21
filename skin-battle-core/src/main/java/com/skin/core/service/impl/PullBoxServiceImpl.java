package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.common.lottery.LotteryBean;
import com.skin.common.lottery.LotteryUtils;
import com.skin.common.util.AssertUtil;
import com.skin.core.mapper.PullBoxMapper;
import com.skin.core.mapper.PullBoxSkinMapper;
import com.skin.core.service.*;
import com.skin.entity.*;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    UserService userService;
    @Autowired
    PointService pointService;
    @Autowired
    SysParamsService sysParamsService;
    @Autowired
    TakeOrderService takeOrderService;
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
            PullBoxSkin sameSkin = pullBoxSkinMapper.selectOne(new QueryWrapper<PullBoxSkin>().eq("skin_name", skin.getName()).eq("pull_box_id", pullBoxId));
            AssertUtil.isNotNull(sameSkin, "该皮肤已经存在");
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

    @Override
    public TakeOrder openBox(Long userId, Long pullBoxId,Double probability) {
        //判断用户是否填写steam链接
        User user = userService.getById(userId);
        AssertUtil.isBlank(user.getSteamUrl(), "您还没有绑定Steam交易链接，请先绑定链接以继续游戏");
        PullBox pullBox = this.getById(pullBoxId);
        AssertUtil.isNull(pullBox, "系统异常");
        AssertUtil.isTrue(pullBox.getEnable() == 0, "该盒子已经关闭");
        PointInfo pointInfo = pointService.getByUid(userId);
        BigDecimal totalPrice = pullBox.getPrice().multiply(new BigDecimal(String.valueOf(probability))).setScale(2, BigDecimal.ROUND_HALF_UP);
        AssertUtil.isTrue(totalPrice.compareTo(pointInfo.getPoint()) > 0, "余额不足，请先充值");
        List<PullBoxSkin> blindBoxSkins = pullBoxSkinMapper.selectList(new QueryWrapper<PullBoxSkin>().eq("pull_box_id", pullBoxId));
        if (blindBoxSkins == null || blindBoxSkins.size() == 0) {
            throw new ApiException("该盒子没有皮肤");
        }
        List<LotteryBean> lotteryBeans = new ArrayList<>();
        blindBoxSkins.forEach(blindBoxSkin -> {
            LotteryBean lotteryBean = new LotteryBean();
            lotteryBean.setGoodsId(blindBoxSkin.getId());
            lotteryBean.setBili(blindBoxSkin.getProbability());
            if (blindBoxSkin.getIsReward()==1){
                lotteryBean.setBili(blindBoxSkin.getProbability()+probability);
            }
            //判断是否设置了单独的概率
            if (user.getHighProbability() != null && user.getHighProbability() != 0 && blindBoxSkin.getLevel() == 2) {
                lotteryBean.setBili(user.getHighProbability());
            }
            if (user.getMiddleProbability() != null && user.getMiddleProbability() != 0 && blindBoxSkin.getLevel() == 1) {
                lotteryBean.setBili(user.getMiddleProbability());
            }
            if (user.getLowProbability() != null && user.getLowProbability() != 0 && blindBoxSkin.getLevel() == 0) {
                lotteryBean.setBili(user.getLowProbability());
            }
            lotteryBeans.add(lotteryBean);
        });
        List<HashMap<String, Object>> lotteryResult = LotteryUtils.lottery(lotteryBeans, 1);

        TakeOrder takeOrder = new TakeOrder();
        //存入背包
        lotteryResult.forEach(map -> {
            PullBoxSkin blindBoxSkin = pullBoxSkinMapper.selectById((Long) map.get("goodsId"));
            AssertUtil.isNull(blindBoxSkin, "网络异常，请稍后再试");
            takeOrder.setBoxId(pullBoxId);
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
            int from = Integer.parseInt(sysParamsService.getSysParams("business_type", "拉货", "1"));
            pointService.editPoint(userId, totalPrice, from, "拉货", orderNo, 0);
            takeOrderService.save(takeOrder);

        });

        return takeOrder;
    }

    @Transactional
    @Override
    public void setReward(Long userId, Long pullBoxSkinId,Integer status) {
        PullBoxSkin pullBoxSkin = pullBoxSkinMapper.selectById(pullBoxSkinId);
        if(status ==1){
            pullBoxSkinMapper.updateSkin(pullBoxSkin.getPullBoxId());
            pullBoxSkin.setIsReward(1);
            pullBoxSkinMapper.updateById(pullBoxSkin);
        }
    }
}
