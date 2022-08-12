package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.core.mapper.DailyTaskMapper;
import com.skin.core.service.DailyTaskService;
import com.skin.entity.DailyTask;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/11 15:47
 * @Description:
 */
@Service
@Transactional(readOnly = true)
public class DailyTaskServiceImpl extends ServiceImpl<DailyTaskMapper, DailyTask> implements DailyTaskService {


    @Override
    public Page<DailyTask> getPage(Integer pageNo, Integer pageSize) {
        return baseMapper.selectPage(new Page<>(pageNo, pageSize), new QueryWrapper<DailyTask>().orderByDesc("create_date"));
    }
}
