package com.skin.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.skin.vo.UserPage;
import com.skin.entity.User;
import org.apache.ibatis.annotations.Param;


public interface UserMapper extends BaseMapper<User> {

    Page<UserPage> getUserPage(@Param("page") Page<UserPage> page, @Param("nickName") String nickName, @Param("tel") String tel);
}
