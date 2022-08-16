package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.core.mapper.MessageMapper;
import com.skin.core.service.MessageService;
import com.skin.entity.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther: xiang
 * @Date: 2022/8/16 16:28
 * @Description:
 */
@Service
@Transactional(readOnly = true,rollbackFor = Exception.class)
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {


    @Override
    public Page<Message> getPage(Integer pageNo, Integer pageSize, Long userId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_date");
        return baseMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
    }
}
