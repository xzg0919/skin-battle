package com.skin.api.user;

import com.skin.core.service.BoxBattleService;
import com.skin.entity.User;
import com.skin.params.BoxBattleBean;
import com.skin.params.UserBean;
import com.skin.task.BoxBattleItem;
import com.skin.task.BoxBattleTask;
import com.tzj.module.api.annotation.*;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.easyopen.ApiContext;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static com.skin.common.constant.TokenConst.USER_API_COMMON_AUTHORITY;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/25 12:41
 * @Description:
 */
@ApiService
public class UserBoxBattleApi {

    @Autowired
    BoxBattleService boxBattleService;


    @Api(name = "battle.create", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object create(BoxBattleBean boxBattleBean) {
        boxBattleService.createBattleBox(UserApi.getMember().getId(), boxBattleBean.getBoxIds(), boxBattleBean.getUserCount());
        return "success";
    }


    @Api(name = "battle.join", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object join(BoxBattleBean boxBattleBean) {
        boolean finish = boxBattleService.join(UserApi.getMember().getId(), boxBattleBean.getBoxBattleId());
        if (finish) {
            BoxBattleItem boxBattleItem = new BoxBattleItem(new Date().getTime(), boxBattleBean.getBoxBattleId());
            BoxBattleTask.queue.put(boxBattleItem);
        }
        return "success";
    }

    @Api(name = "battle.boxPage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object boxPage(BoxBattleBean boxBattleBean) {
        return boxBattleService.getBoxPage(boxBattleBean.getPageBean().getPageNum(), boxBattleBean.getPageBean().getPageSize());
    }


    @Api(name = "battle.page", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object page(BoxBattleBean boxBattleBean) {
        Subject subject = ApiContext.getSubject();
        User user = new User();
        if (subject != null) {
            user = (User) subject.getUser();
        }
        return boxBattleService.getBattleInfoPage(boxBattleBean.getPageBean().getPageNum(), boxBattleBean.getPageBean().getPageSize(), user.getId());
    }


    @Api(name = "battle.getBattleInfo", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object getBattleInfo(BoxBattleBean boxBattleBean) {
        return boxBattleService.getBattleInfo(boxBattleBean.getBoxBattleId());
    }

    @Api(name = "battle.getSkinList", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object getSkinList(BoxBattleBean boxBattleBean) {
        return boxBattleService.getSkinList(boxBattleBean.getId());
    }


    @Api(name = "battle.historyPage", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object historyPage(BoxBattleBean boxBattleBean) {
        return boxBattleService.getHistoryPage(null, boxBattleBean.getPageBean().getPageNum(), boxBattleBean.getPageBean().getPageSize());
    }


    @Api(name = "battle.userHistoryPage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object userHistoryPage(BoxBattleBean boxBattleBean) {
        return boxBattleService.getHistoryPage(UserApi.getMember().getId(),
                boxBattleBean.getPageBean().getPageNum(), boxBattleBean.getPageBean().getPageSize());
    }
}
