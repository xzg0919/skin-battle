package com.skin.api.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.skin.core.service.RollRoomService;
import com.skin.entity.RollRoomUser;
import com.skin.entity.User;
import com.skin.params.RollRoomBean;
import com.tzj.module.api.annotation.*;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.easyopen.ApiContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

import static com.skin.common.constant.TokenConst.USER_API_COMMON_AUTHORITY;


/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/24 10:32
 * @Description:
 */
@ApiService
public class RollApi {


    @Autowired
    RollRoomService rollRoomService;

    @Api(name = "room.getRoomPage", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object getRoomPage(RollRoomBean rollRoomBean) {
        Subject subject = ApiContext.getSubject();
        User user = new User();
        if (subject != null) {
            user = (User) subject.getUser();
        }
        return rollRoomService.getRoomPage(rollRoomBean.getPageBean().getPageNum(), rollRoomBean.getPageBean().getPageSize(),
                rollRoomBean.getStatus(), user.getId());

    }

    @Api(name = "room.getRewardUser", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object getRewardUser(RollRoomBean rollRoomBean) {
        HashMap<String,Object> result =new HashMap<>();
        Page<RollRoomUser> rewardUser = rollRoomService.getRewardUser(rollRoomBean.getId(), rollRoomBean.getPageBean().getPageNum(), rollRoomBean.getPageBean().getPageSize());
        result.put("page",rewardUser);
        result.put("count",rewardUser.getTotal());
        result.put("rewardPrice",rollRoomService.rewardSumPrice(rollRoomBean.getId()));
        return result;
    }


    @Api(name = "room.join", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object join(RollRoomBean rollRoomBean) {
        rollRoomService.join(UserApi.getMember().getId(), rollRoomBean.getId(), rollRoomBean.getRoomPswd());
        return "success";
    }


    @Api(name = "room.getRoomSkinById", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object getRoomSkinById(RollRoomBean rollRoomBean) {
        return rollRoomService.getRoomSkinById(rollRoomBean.getPageBean().getPageNum(), rollRoomBean.getPageBean().getPageSize(),rollRoomBean.getId());

    }

    @Api(name = "room.getRoomUserById", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object getRoomUserById(RollRoomBean rollRoomBean) {
        return rollRoomService.getRoomUserById(rollRoomBean.getPageBean().getPageNum(), rollRoomBean.getPageBean().getPageSize(),rollRoomBean.getId());

    }

    @Api(name = "room.getInfo", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object getInfo(RollRoomBean rollRoomBean) {
        Subject subject = ApiContext.getSubject();
        User user = new User();
        if (subject != null) {
            user = (User) subject.getUser();
        }
        return rollRoomService.getInfo(rollRoomBean.getId(),user.getId());

    }


}
