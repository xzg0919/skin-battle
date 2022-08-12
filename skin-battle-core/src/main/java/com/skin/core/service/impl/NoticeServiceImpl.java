package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.core.mapper.NoticeMapper;
import com.skin.core.service.NoticeService;
import com.skin.entity.Notice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/11 15:19
 * @Description:
 */
@Service
@Transactional
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {


    @Override
    public Page<Notice> getPage(Integer pageNo, Integer pageSize) {
        return baseMapper.selectPage(new Page<>(pageNo, pageSize),  new QueryWrapper<Notice>().orderByDesc("create_date"));
    }
}
