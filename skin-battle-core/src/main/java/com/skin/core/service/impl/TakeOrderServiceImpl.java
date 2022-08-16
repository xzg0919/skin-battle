package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.core.mapper.TakeOrderMapper;
import com.skin.core.service.TakeOrderService;
import com.skin.entity.TakeOrder;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/11 10:57
 * @Description:
 */
@Service
@Transactional(readOnly = true)
public class TakeOrderServiceImpl extends ServiceImpl<TakeOrderMapper, TakeOrder> implements TakeOrderService {


    @Override
    public Page<Map<String, Object>> userTakeOrderPage(Integer pageNum, Integer pageSize, Long userId,String orderNo) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        if(StringUtils.isNotBlank(orderNo)){
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
        if(StringUtils.isNotBlank(nickName)){
            queryWrapper.eq(TakeOrder::getNickName,nickName);
        }
        if(StringUtils.isNotBlank(email)){
            queryWrapper.and(o->o.eq(TakeOrder::getEmail,email).or().eq(TakeOrder::getTel,email));
        }
        queryWrapper.orderByDesc(TakeOrder::getCreateDate);
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Transactional
    @Override
    public void changeStatus(Long id, Integer status) {
        TakeOrder takeOrder = baseMapper.selectById(id);
        takeOrder.setStatus(status);
        baseMapper.updateById(takeOrder);
    }

    @Override
    public BigDecimal totalPrice() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status_", 1);
        queryWrapper.select("sum(price) as price");
        TakeOrder takeOrder = baseMapper.selectOne(queryWrapper);
        return takeOrder==null?BigDecimal.ZERO:takeOrder.getPrice();
    }

    @Override
    public BigDecimal totalPrice(String dateBegin, String dateEnd) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status_", 1);
        queryWrapper.between("create_date",dateBegin,dateEnd);
        queryWrapper.select("sum(price) as price");
        TakeOrder takeOrder = baseMapper.selectOne(queryWrapper);
        return takeOrder==null?BigDecimal.ZERO:takeOrder.getPrice();
    }

    @Override
    public Integer takeOrderCount(String dateBegin, String dateEnd) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status_", 1);
        queryWrapper.between("create_date",dateBegin,dateEnd);
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
        queryWrapper.eq("source",source);
        queryWrapper.select("sum(price) as price");
        TakeOrder takeOrder = baseMapper.selectOne(queryWrapper);
        return takeOrder==null?BigDecimal.ZERO:takeOrder.getPrice();
    }

    @Override
    public BigDecimal totalPrice(String dateBegin, String dateEnd, Integer status, Integer source) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status_", status);
        queryWrapper.eq("source",source);
        queryWrapper.between("create_date",dateBegin,dateEnd);
        queryWrapper.select("sum(price) as price");
        TakeOrder takeOrder = baseMapper.selectOne(queryWrapper);
        return takeOrder==null?BigDecimal.ZERO:takeOrder.getPrice();
    }


}
