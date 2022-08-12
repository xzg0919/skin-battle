package com.skin.core.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.core.mapper.UserMapper;
import com.skin.core.service.UserService;
import com.skin.dto.UserPage;
import com.skin.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/10 14:27
 * @Description:
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Page<UserPage> getUserPage(Integer pageNum, Integer pageSize, String nickName, String tel) {
        Page page=new Page<>(pageNum, pageSize);
        return userMapper.getUserPage(page, nickName, tel);
    }

    @Transactional
    @Override
    public void updateVIP(Long id, Integer vip) {
        User user = baseMapper.selectById(id);
        user.setVip(vip);
        baseMapper.updateById(user);
    }

    @Transactional
    @Override
    public void editUser(User user) {
        baseMapper.updateById(user);
    }
}
