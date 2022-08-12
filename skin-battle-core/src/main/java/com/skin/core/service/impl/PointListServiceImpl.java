package com.skin.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.core.mapper.PointListMapper;
import com.skin.core.service.PointListService;
import com.skin.entity.PointList;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/10 16:55
 * @Description:
 */
@Service
@Transactional(readOnly = true)
public class PointListServiceImpl extends ServiceImpl<PointListMapper, PointList> implements PointListService {


    @Override
    public Page<Map<String, Objects>> getPointListPage(Integer pageNum, Integer pageSize, Long userId, Integer orderFrom,String orderNo) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("order_from", orderFrom);
        if(StringUtils.isNotBlank(orderNo)){
            queryWrapper.eq("order_no", orderNo);
        }
        queryWrapper.select("create_date as createDate,order_no as orderNo,amount");
        queryWrapper.orderByDesc("create_date");
        return baseMapper.selectPage(page, queryWrapper);
    }
}
