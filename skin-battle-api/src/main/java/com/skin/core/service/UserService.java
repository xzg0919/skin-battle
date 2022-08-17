package com.skin.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.dto.UserPage;
import com.skin.entity.User;
import com.skin.params.UserBean;


/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/10 14:25
 * @Description:
 */
public interface UserService extends IService<User> {
    Page<UserPage> getUserPage(Integer pageNum, Integer pageSize, String nickName, String tel);


    void updateVIP(Long id , Integer vip);


    void editUser(User user);

    User getUserByTel(String tel);

    User getUserByPromoCode(String promoCode);

    void register(UserBean userBean);

    User login(String tel,String password);

    User getUserInfo (Long id );


    User findByInvitationCode(String invitationCode);
}
