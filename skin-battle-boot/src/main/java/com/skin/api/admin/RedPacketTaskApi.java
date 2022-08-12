package com.skin.api.admin;

import com.skin.core.service.RedPacketTaskService;
import com.skin.entity.Notice;
import com.skin.entity.RedPacketTask;
import com.skin.params.NoticeBean;
import com.skin.params.RedPacketBean;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static com.skin.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/11 16:52
 * @Description:
 */
@ApiService
public class RedPacketTaskApi {

    @Autowired
    RedPacketTaskService redPacketTaskService;




    @Api(name = "redpacket.pageList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object pageList(RedPacketBean redPacketBean) {
        return redPacketTaskService.getPage(redPacketBean.getPageBean().getPageNum(), redPacketBean.getPageBean().getPageSize());
    }


    @Api(name = "redpacket.insertOrUpdate", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object insertOrUpdate(RedPacketBean redPacketBean) {
        if (redPacketBean.getId() == null) {
            RedPacketTask redPacketTask = new RedPacketTask();
            BeanUtils.copyProperties(redPacketBean, redPacketTask);
            redPacketTaskService.save(redPacketTask);
        } else {
            RedPacketTask redPacketTask = redPacketTaskService.getById(redPacketBean.getId());
            BeanUtils.copyProperties(redPacketBean, redPacketTask);
            redPacketTaskService.updateById(redPacketTask);
        }
        return "success";
    }


    @Api(name = "redpacket.removeById", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object removeById(RedPacketBean redPacketBean) {
        redPacketTaskService.removeById(redPacketBean.getId());
        return "success";
    }

    @Api(name = "redpacket.info", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object info(RedPacketBean redPacketBean) {
        return redPacketTaskService.getById(redPacketBean.getId());
    }
}
