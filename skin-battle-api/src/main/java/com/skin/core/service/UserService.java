package com.skin.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.dto.UserPage;
import com.skin.entity.User;


/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/10 14:25
 * @Description:
 */
public interface UserService extends IService<User> {
    Page<UserPage> getUserPage(Integer pageNum, Integer pageSize, String nickName, String tel);


    void updateVIP(Long id , Integer vip);


    void editUser(User user);
}
