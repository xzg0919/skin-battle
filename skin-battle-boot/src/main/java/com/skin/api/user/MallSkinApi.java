package com.skin.api.user;

import com.skin.core.service.MallSkinService;
import com.skin.params.UserBean;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import static com.skin.common.constant.TokenConst.USER_API_COMMON_AUTHORITY;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/19 15:41
 * @Description:
 */
@ApiService
public class MallSkinApi {


    @Autowired
    MallSkinService mallSkinService;


    @Api(name = "mall.skinPage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object skinPage(UserBean userBean) {

        return "领取成功";
    }
}
