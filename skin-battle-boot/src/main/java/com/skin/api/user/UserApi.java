package com.skin.api.user;

import com.skin.common.security.MD5Util;
import com.skin.common.util.AssertUtil;
import com.skin.core.service.MessageService;
import com.skin.core.service.PointListService;
import com.skin.core.service.UserService;
import com.skin.core.service.VerifyMessageService;
import com.skin.entity.User;
import com.skin.entity.VerifyMessage;
import com.skin.params.UserBean;
import com.tzj.module.api.annotation.*;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.service.SubjectService;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.utils.security.Md5Utils;
import com.tzj.module.easyopen.ApiContext;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static com.skin.common.constant.TokenConst.*;
import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

/**
 * @Auther: xiang
 * @Date: 2022/8/16 14:39
 * @Description:
 */
@ApiService
public class UserApi {

    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    @Autowired
    PointListService pointListService;

    @Autowired
    VerifyMessageService verifyMessageService;

    @Api(name = "user.register", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object register(UserBean userBean) {
        //检验手机号/邮箱是否已经注册
        AssertUtil.isNotNull(userService.getUserByTel(userBean.getTel()), "该手机号/邮箱已经注册，请直接登录！");
        // 校验验证码
        VerifyMessage verifyMessage = verifyMessageService.check(userBean.getTel(), userBean.getVerificationCode());
        //校验推广码
        AssertUtil.isNull(userService.getUserByPromoCode(userBean.getInvitationCode()), "邀请码不存在！");
        userService.register(userBean);
        verifyMessage.setIsUse(1);
        verifyMessageService.updateById(verifyMessage);
        return "注册成功！";
    }

    @Api(name = "user.login", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object login(UserBean userBean) {
        User user = userService.login(userBean.getTel(), MD5Util.md5(userBean.getUserPwd()));
        AssertUtil.isNull(user, "用户名或密码错误！");
        Map<String, Object> resultMap = new HashMap<>();
        String token = JwtUtils.generateToken(user.getId().toString(), USER_API_EXPRIRE, USER_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, USER_API_TOKEN_CYPTO_KEY);
        resultMap.put("token", securityToken);
        resultMap.put("user", user);
        return resultMap;
    }

    @Api(name = "user.updatePwd", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object updatePwd(UserBean userBean) {
        //检验手机号/邮箱是否已经注册
        User user = userService.getUserByTel(userBean.getTel());
        AssertUtil.isNull(user, "账号不存在！");
        // 校验验证码
        VerifyMessage verifyMessage = verifyMessageService.check(userBean.getTel(), userBean.getVerificationCode());
        user.setUserPwd(MD5Util.md5(userBean.getUserPwd()));
        userService.updateById(user);
        verifyMessage.setIsUse(1);
        verifyMessageService.updateById(verifyMessage);
        return "修改成功！";
    }


    @Api(name = "user.editSteamUrl", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object editSteamUrl(UserBean userBean) {
        User userCache = getMember();
        User user = userService.getById(userCache.getId());
        user.setSteamUrl(userBean.getSteamUrl());
        userService.updateById(user);
        return "修改成功！";
    }


    @Api(name = "user.getUserInfo", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object getUserInfo(UserBean userBean) {
        return userService.getUserInfo(getMember().getId());
    }

    @Api(name = "user.getMessagePage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object getMessagePage(UserBean userBean) {
        return messageService.getPage(userBean.getPageBean().getPageNum(), userBean.getPageBean().getPageSize(), getMember().getId());
    }


    @Api(name = "user.getMessage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object getMessage(UserBean userBean) {
        return messageService.getByUserIdAndId(getMember().getId(), userBean.getId());
    }

    @Api(name = "user.pointPage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object pointPage(UserBean userBean) {
        return pointListService.userPage(getMember().getId(), userBean.getStartDate(), userBean.getEndDate(), userBean.getType(),
                userBean.getPageBean().getPageNum(), userBean.getPageBean().getPageSize());
    }

    public static User getMember() {

        Subject subject = ApiContext.getSubject();
        if (subject == null) {
            throw new ApiException("request中subject为空，确认此接口有token参数传入！");
        }
        User user = (User) subject.getUser();
        return user;
    }

}
