package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.core.mapper.RedPacketTaskMapper;
import com.skin.core.service.RedPacketTaskService;
import com.skin.entity.RedPacketTask;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/11 16:51
 * @Description:
 */
@Service
@Transactional
public class RedPacketTaskServiceImpl extends ServiceImpl<RedPacketTaskMapper, RedPacketTask> implements RedPacketTaskService {

    @Override
    public Page<RedPacketTask> getPage(Integer pageNo, Integer pageSize) {
        return baseMapper.selectPage(new Page<>(pageNo, pageSize),  new QueryWrapper<RedPacketTask>().orderByDesc("create_date"));
    }
}
