package com.skin.api.user;

import com.skin.common.security.MD5Util;
import com.skin.common.util.AssertUtil;
import com.skin.core.service.*;
import com.skin.entity.User;
import com.skin.entity.VerifyMessage;
import com.skin.params.TakeOrderBean;
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
import java.math.BigDecimal;
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

    @Autowired
    TakeOrderService takeOrderService;
    @Autowired
    VipRewardService    vipRewardService;

    @Autowired
    InvitationService   invitationService;
    @Autowired
    PointService pointService;

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
        HashMap<String,Object> result  =new HashMap<>();
        result.put("userInfo",userService.getUserInfo(getMember().getId()));
        result.put("point",pointService.getUserValidPoint(getMember().getId()));
        return result;
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

    @Api(name = "user.getPackagePage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object getpackagePage(TakeOrderBean takeOrderBean) {
        return  takeOrderService.getUserPackage(UserApi.getMember().getId(),
                takeOrderBean.getPageBean().getPageNum(), takeOrderBean.getPageBean().getPageSize());
    }

    @Api(name = "user.take", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object take(TakeOrderBean takeOrderBean) {
          takeOrderService.userTake(UserApi.getMember().getId(),takeOrderBean.getId());
          return "提货申请已提交！";
    }


    @Api(name = "user.recycle", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object recycle(TakeOrderBean takeOrderBean) {
          takeOrderService.recycle(UserApi.getMember().getId(), takeOrderBean.getId());
        return "回收成功";
    }

    @Api(name = "user.recycleBatch", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object recycleBatch(TakeOrderBean takeOrderBean) {
        takeOrderService.recycleBatch(UserApi.getMember().getId(), takeOrderBean.getPackageIds());
        return "回收成功";
    }

    @Api(name = "user.vipRewardList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object vipRewardList(TakeOrderBean takeOrderBean) {
        HashMap<String,Object> rewardMap =new HashMap<>();
        BigDecimal growthValue = userService.getById(UserApi.getMember().getId()).getGrowthValue();
        rewardMap.put("reward",vipRewardService.getVipCheckList(UserApi.getMember().getId(),growthValue));
        rewardMap.put("growthValue", growthValue);
        return rewardMap;
    }

    @Api(name = "user.receiveReward", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object receiveReward(UserBean userBean) {
        vipRewardService.receiveVipReward(UserApi.getMember().getId(),userBean.getLevel());
        return "领取成功";
    }


    @Api(name = "user.userInvitationInfo", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object userInvitationInfo() {
        HashMap<String,Object> rewardMap =new HashMap<>();
        rewardMap.put("inviteCount",invitationService.getInvitationCount(UserApi.getMember().getId()));
        rewardMap.put("reward", pointListService.invitationReward(UserApi.getMember().getId()));
        rewardMap.put("invitationPercentage", invitationService.invitationPercentage(UserApi.getMember().getId()));
        return rewardMap;
    }

    @Api(name = "user.userInvitationRule", version = "1.0")
    @SignIgnore
  @AuthIgnore
    public Object userInvitationRule() {
        return invitationService.userInvitationRule();
    }

    @Api(name = "user.userInvitationPage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object userInvitationPage(UserBean userBean) {
        return invitationService.getInvitationPage(userBean.getPageBean().getPageNum(), userBean.getPageBean().getPageSize(), UserApi.getMember().getId());
    }

    @Api(name = "user.userInvitationDetailPage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object userInvitationDetailPage(UserBean userBean) {
        return invitationService.getInvitationLogPage(userBean.getPageBean().getPageNum(), userBean.getPageBean().getPageSize(), UserApi.getMember().getId());
    }

}
