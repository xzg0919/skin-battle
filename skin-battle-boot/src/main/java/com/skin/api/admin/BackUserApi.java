package com.skin.api.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.skin.core.service.TakeOrderService;
import com.skin.core.service.UserService;
import com.skin.entity.User;
import com.skin.params.UserBean;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static com.skin.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/10 14:23
 * @Description:
 */
@ApiService
public class BackUserApi {

    @Autowired
    UserService userService;
    @Autowired
    TakeOrderService takeOrderService;

    @Api(name = "user.getPage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getUserPage(UserBean userBean) {
        return userService.getUserPage(userBean.getPageBean().getPageNum(), userBean.getPageBean().getPageSize(), userBean.getNickName(), userBean.getTel());

    }


    @Api(name = "user.getOne", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getOne(UserBean userBean) {
        return userService.getById(userBean.getId());

    }


    @Api(name = "user.edit", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object editUser(UserBean userBean) {
        User byId = userService.getById(userBean.getId());
        byId.setNickName(userBean.getNickName());
        byId.setHighProbability(userBean.getHighProbability());
        byId.setMiddleProbability(userBean.getMiddleProbability());
        byId.setLowProbability(userBean.getLowProbability());
        byId.setAvatar(userBean.getAvatar());
        userService.editUser(byId);
        return "success";
    }


    @Api(name = "user.takeOrderPage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object takeOrderPage(UserBean userBean) {
        HashMap<String,Object> resultMap  =new HashMap<>();
        Page<Map<String, Object>> mapPage = takeOrderService.userTakeOrderPage(userBean.getPageBean().getPageNum(), userBean.getPageBean().getPageSize(), userBean.getId(),userBean.getOrderNo());
        resultMap.put("page",mapPage);
        resultMap.put("totalPrice",takeOrderService.totalPrice(userBean.getId()));
        return resultMap;
    }
}
