package com.skin.api.admin;

import com.skin.core.service.NoticeService;
import com.skin.entity.MallSkin;
import com.skin.entity.Notice;
import com.skin.params.NoticeBean;
import com.tzj.module.api.annotation.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static com.skin.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/11 15:23
 * @Description:
 */
@ApiService
public class NoticeApi {

    @Autowired
    NoticeService noticeService;


    @Api(name = "notice.pageList", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object pageList(NoticeBean noticeBean) {
        return noticeService.getPage(noticeBean.getPageBean().getPageNum(), noticeBean.getPageBean().getPageSize());
    }


    @Api(name = "notice.insertOrUpdate", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object insertOrUpdate(NoticeBean noticeBean) {
        if (noticeBean.getId() == null) {
            Notice notice = new Notice();
            BeanUtils.copyProperties(noticeBean, notice);
            notice.setSort(0);
            noticeService.save(notice);
        } else {
            Notice notice = noticeService.getById(noticeBean.getId());
            BeanUtils.copyProperties(noticeBean, notice);
            noticeService.updateById(notice);
        }
        return "success";
    }


    @Api(name = "notice.removeById", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object removeById(NoticeBean noticeBean) {
        noticeService.removeById(noticeBean.getId());
        return "success";
    }

    @Api(name = "notice.info", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object info(NoticeBean noticeBean) {
        return noticeService.getById(noticeBean.getId());
    }

}
