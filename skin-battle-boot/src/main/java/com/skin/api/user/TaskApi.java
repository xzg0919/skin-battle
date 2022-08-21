package com.skin.api.user;

import com.skin.core.service.DailyTaskService;
import com.skin.core.service.RedPacketTaskService;
import com.skin.params.UserBean;
import com.tzj.module.api.annotation.*;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.easyopen.ApiContext;
import org.springframework.beans.factory.annotation.Autowired;

import static com.skin.common.constant.TokenConst.USER_API_COMMON_AUTHORITY;

/**
 * @Auther: xiang
 * @Date: 2022/8/20 17:09
 * @Description:
 */
@ApiService
public class TaskApi {

    @Autowired
    RedPacketTaskService redPacketTaskService;

    @Autowired
    DailyTaskService dailyTaskService;


    @Api(name = "task.redPacketPage", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object redPacketPage(UserBean userBean) {
        Subject subject = ApiContext.getSubject();
        Long userId =null;
        if(subject!=null){
            userId =UserApi.getMember().getId();
        }
        return redPacketTaskService.getPage(userId,userBean.getPageBean().getPageNum(), userBean.getPageBean().getPageSize());
    }

    @Api(name = "task.redPacketPageReceive", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values=USER_API_COMMON_AUTHORITY)
    public Object redPacketPageReceive(UserBean userBean) {
        redPacketTaskService.receive(UserApi.getMember().getId(), userBean.getId());
        return "success";
    }


    @Api(name = "task.dailyTaskPage", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object dailyTaskPage(UserBean userBean) {
        Subject subject = ApiContext.getSubject();
        Long userId =null;
        if(subject!=null){
            userId =UserApi.getMember().getId();
        }
        return dailyTaskService.getPage(userId,userBean.getPageBean().getPageNum(), userBean.getPageBean().getPageSize());
    }


    @Api(name = "task.dailyTaskReceive", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values=USER_API_COMMON_AUTHORITY)
    public Object dailyTaskReceive(UserBean userBean) {
        dailyTaskService.receive(UserApi.getMember().getId(), userBean.getId());
        return "success";
    }

}
