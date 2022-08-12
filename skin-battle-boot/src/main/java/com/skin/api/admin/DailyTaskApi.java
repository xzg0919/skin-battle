package com.skin.api.admin;

import com.skin.core.service.DailyTaskService;
import com.skin.entity.DailyTask;
import com.skin.entity.Notice;
import com.skin.params.DailyTaskBean;
import com.skin.params.NoticeBean;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static com.skin.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/11 16:29
 * @Description:
 */
@ApiService
public class DailyTaskApi {

    @Autowired
    DailyTaskService dailyTaskService;


    @Api(name = "daily.pageList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object pageList(DailyTaskBean dailyTaskBean) {
        return dailyTaskService.getPage(dailyTaskBean.getPageBean().getPageNum(), dailyTaskBean.getPageBean().getPageSize());
    }


    @Api(name = "daily.insertOrUpdate", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object insertOrUpdate(DailyTaskBean dailyTaskBean) {
        if (dailyTaskBean.getId() == null) {
            DailyTask dailyTask = new DailyTask();
            BeanUtils.copyProperties(dailyTaskBean, dailyTask);

            dailyTaskService.save(dailyTask);
        } else {
            DailyTask dailyTask = dailyTaskService.getById(dailyTaskBean.getId());
            BeanUtils.copyProperties(dailyTaskBean, dailyTask);
            dailyTaskService.updateById(dailyTask);
        }
        return "success";
    }


    @Api(name = "daily.removeById", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object removeById(DailyTaskBean dailyTaskBean) {
        dailyTaskService.removeById(dailyTaskBean.getId());
        return "success";
    }

    @Api(name = "daily.info", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object info(DailyTaskBean dailyTaskBean) {
        return dailyTaskService.getById(dailyTaskBean.getId());
    }
}
