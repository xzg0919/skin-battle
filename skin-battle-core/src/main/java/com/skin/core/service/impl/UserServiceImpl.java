package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.common.security.MD5Util;
import com.skin.core.mapper.PointMapper;
import com.skin.core.mapper.UserMapper;
import com.skin.core.service.UserService;
import com.skin.dto.UserPage;
import com.skin.entity.PointInfo;
import com.skin.entity.User;
import com.skin.params.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;


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

    @Autowired
    PointMapper pointMapper;

    @Override
    public Page<UserPage> getUserPage(Integer pageNum, Integer pageSize, String nickName, String tel) {
        Page page = new Page<>(pageNum, pageSize);
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

    @Override
    public User getUserByTel(String tel) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.or().eq("tel", tel);
        queryWrapper.or().eq("email", tel);
        return baseMapper.selectOne(queryWrapper);

    }

    @Override
    public User getUserByPromoCode(String promoCode) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("promo_code", promoCode);
        return baseMapper.selectOne(queryWrapper);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(UserBean userBean) {
        User user = new User();
        if (userBean.getTel().contains("@")) {
            user.setEmail(userBean.getTel());
        } else {
            user.setTel(userBean.getTel());
        }
        user.setNickName(userBean.getNickName());
        user.setUserPwd(userBean.getUserPwd());
        user.setUserId(UUID.randomUUID().toString().replace("-", "").replace("o", "0").replace("i", "1"));
        user.setPromoCode(UUID.randomUUID().toString().replace("-", "").replace("o", "0").replace("i", "1").substring(0, 7).toUpperCase());
        baseMapper.insert(user);
        //初始化pointInfo
        PointInfo pointInfo = new PointInfo();
        pointInfo.setUserId(user.getUserId());
        pointInfo.setConsumePoint(BigDecimal.ZERO);
        pointInfo.setTotalPoint(BigDecimal.ZERO);
        pointInfo.setPoint(BigDecimal.ZERO);
        pointInfo.setMd5Code(MD5Util.md5(pointInfo.getPoint().toString() + pointInfo.getTotalPoint().toString() + MD5Util.SIGN_KEY));
        pointMapper.insert(pointInfo);

        //TODO 如果填写了邀请码 记录
    }

    @Override
    public User login(String tel, String password) {

        QueryWrapper queryWrapper = new QueryWrapper();
        if (tel.contains("@")) {
            queryWrapper.eq("email", tel);
        } else {
            queryWrapper.eq("tel", tel);
        }
        queryWrapper.eq("user_pwd",password);
         return  baseMapper.selectOne(queryWrapper);
    }




}
