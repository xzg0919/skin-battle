package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.common.security.MD5Util;
import com.skin.common.util.AssertUtil;
import com.skin.core.mapper.TakeOrderMapper;
import com.skin.core.service.*;
import com.skin.entity.Message;
import com.skin.entity.PointInfo;
import com.skin.entity.PointList;
import com.skin.entity.TakeOrder;
import com.taobao.api.ApiException;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/11 10:57
 * @Description:
 */
@Service
@Transactional(readOnly = true)
public class TakeOrderServiceImpl extends ServiceImpl<TakeOrderMapper, TakeOrder> implements TakeOrderService {


    @Autowired
    MessageService messageService;
    @Autowired
    SysParamsService sysParamsService;

    @Autowired
    PointListService pointListService;

    @Autowired
    PointService pointService;

    @Autowired
    TakeOrderService    takeOrderService;

    @Override
    public Page<Map<String, Object>> userTakeOrderPage(Integer pageNum, Integer pageSize, Long userId, String orderNo) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        if (StringUtils.isNotBlank(orderNo)) {
            queryWrapper.eq("order_no", orderNo);
        }
        queryWrapper.select("create_date as createDate,order_no as orderNo,skin_name as skinName,price,steam_url as steamUrl,status_ as status");
        queryWrapper.orderByDesc("create_date");
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public BigDecimal totalPrice(Long userId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("status_", 1);
        queryWrapper.select("sum(price) as price");
        return baseMapper.selectOne(queryWrapper).getPrice();
    }

    @Override
    public Page<TakeOrder> getPage(Integer pageNum, Integer pageSize, String email, String nickName) {
        Page page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<TakeOrder> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(nickName)) {
            queryWrapper.eq(TakeOrder::getNickName, nickName);
        }
        if (StringUtils.isNotBlank(email)) {
            queryWrapper.and(o -> o.eq(TakeOrder::getEmail, email).or().eq(TakeOrder::getTel, email));
        }
        queryWrapper.ne(TakeOrder::getStatus, 0);
        queryWrapper.orderByDesc(TakeOrder::getCreateDate);
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Transactional
    @Override
    public void changeStatus(Long id, Integer status) {
        TakeOrder takeOrder = baseMapper.selectById(id);
        if (takeOrder.getStatus() == 1) {
            takeOrder.setStatus(status);

            Message message = new Message();
            switch (status) {
                case 2:
                    message.setTitle("物品取回通知");
                    message.setContent("订单号：" + takeOrder.getOrderNo() + "，物品已取回，请查收！");
                    message.setUserId(takeOrder.getUserId());
                    messageService.save(message);
                    break;
                case 3:
                    message.setTitle("物品取回通知");
                    message.setContent("订单号：" + takeOrder.getOrderNo() + "，物品取回被驳回！");
                    message.setUserId(takeOrder.getUserId());
                    takeOrder.setTakeTime(new Date());
                    messageService.save(message);
                    break;
            }
        } else {
            throw new RuntimeException("当前订单状态不是待发货状态");
        }
        baseMapper.updateById(takeOrder);
    }

    @Override
    public BigDecimal totalPrice() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status_", 1);
        queryWrapper.select("sum(price) as price");
        TakeOrder takeOrder = baseMapper.selectOne(queryWrapper);
        return takeOrder == null ? BigDecimal.ZERO : takeOrder.getPrice();
    }

    @Override
    public BigDecimal totalPrice(String dateBegin, String dateEnd) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status_", 1);
        queryWrapper.between("create_date", dateBegin, dateEnd);
        queryWrapper.select("sum(price) as price");
        TakeOrder takeOrder = baseMapper.selectOne(queryWrapper);
        return takeOrder == null ? BigDecimal.ZERO : takeOrder.getPrice();
    }

    @Override
    public Integer takeOrderCount(String dateBegin, String dateEnd) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status_", 1);
        queryWrapper.between("create_date", dateBegin, dateEnd);
        return baseMapper.selectCount(queryWrapper).intValue();
    }

    @Override
    public Integer takeOrderCount() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status_", 1);
        return baseMapper.selectCount(queryWrapper).intValue();
    }

    @Override
    public BigDecimal totalPrice(Integer status, Integer source) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status_", status);
        queryWrapper.eq("source", source);
        queryWrapper.select("sum(price) as price");
        TakeOrder takeOrder = baseMapper.selectOne(queryWrapper);
        return takeOrder == null ? BigDecimal.ZERO : takeOrder.getPrice();
    }

    @Override
    public BigDecimal totalPrice(String dateBegin, String dateEnd, Integer status, Integer source) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status_", status);
        queryWrapper.eq("source", source);
        queryWrapper.between("create_date", dateBegin, dateEnd);
        queryWrapper.select("sum(price) as price");
        TakeOrder takeOrder = baseMapper.selectOne(queryWrapper);
        return takeOrder == null ? BigDecimal.ZERO : takeOrder.getPrice();
    }


    @Override
    public List<TakeOrder> getLastPageList() {
        QueryWrapper<TakeOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("skin_name,box_id,pic_url,level_,nick_name,avatar,attrition_rate");
        queryWrapper.orderByDesc("create_date");
        queryWrapper.last("limit 9");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public Page<TakeOrder> getUserPackage(Long userId, Integer pageNo, Integer pageSize) {
        Page<TakeOrder> page = new Page<>(pageNo, pageSize);
        QueryWrapper<TakeOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id,skin_name,pic_url,price,status_,attrition_rate");
        queryWrapper.eq("user_id", userId);
        queryWrapper.ne("status_", 4);
        queryWrapper.orderByDesc("create_date");
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void userTake(Long userId, Long packageId) {
        // 判断用户是否有充值记录
        String val = sysParamsService.getSysParams("business_type", "充值", "2");
        if (pointListService.getCountByUserIdAndType(userId, Integer.parseInt(val)) == 0) {
            throw new RuntimeException("取回失败，需要进行任意数量的充值");
        }
        TakeOrder takeOrder = baseMapper.selectOne(new QueryWrapper<TakeOrder>().eq("user_id", userId)
                .eq("id", packageId).in("status_", 0, 3));
        AssertUtil.isNull(takeOrder, "取回异常");
        takeOrder.setStatus(1);
        takeOrder.setApplyTime(new Date());
        baseMapper.updateById(takeOrder);
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void recycle(Long userId, Long packageId) {
        TakeOrder takeOrder = baseMapper.selectOne(new QueryWrapper<TakeOrder>().eq("user_id", userId)
                .eq("id", packageId).in("status_", 0, 3));
        AssertUtil.isNull(takeOrder, "回收异常");
        takeOrder.setStatus(4);
        baseMapper.updateById(takeOrder);
        int from = Integer.parseInt(sysParamsService.getSysParams("business_type", "回收", "3"));
        pointService.editPoint(userId, takeOrder.getPrice(), from, "回收", takeOrder.getOrderNo(), 1);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void recycleBatch(Long userId, String packageIds) {
        for (String packageId : packageIds.split(",")) {
            takeOrderService.recycle(userId, Long.parseLong(packageId));
        }
    }

    @Override
    public Page<TakeOrder> recentTakeOrder(Long boxId, Integer pageNo, Integer pageSize) {
        Page<TakeOrder> page = new Page<>(pageNo, pageSize);
        QueryWrapper<TakeOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("skin_name,pic_url,level_,attrition_rate");
        queryWrapper.eq("box_id", boxId);
        queryWrapper.orderByDesc("create_date");
        return baseMapper.selectPage(page, queryWrapper);
    }
}
