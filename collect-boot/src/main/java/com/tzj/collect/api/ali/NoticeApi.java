package com.tzj.collect.api.ali;

import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.service.NoticeService;
import com.tzj.collect.entity.Notice;
import com.tzj.module.api.annotation.*;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;

/**
 * Created on 2019/9/6
 * Title: TODO
 * Description: TODO
 * Copyright: Copyright (c) 2019
 * Company: 上海铸乾信息科技有限公司
 * Department:研发部
 *
 * @author:Michael_Wang
 * @Version 1.0
 **/
@ApiService
public class NoticeApi {
    @Autowired
    private NoticeService noticeService;

    /**getVaildNotices
     * 获取所有的有效的消息
     * @return
     */
    @Api(name = "notice.getVaildNotices", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    @AuthIgnore
    public List<Notice> getVaildNotices(){
        return noticeService.getVaildNotices();
    }


    /**getNoticeDetail
     * 获取消息的明细信息
     * @return
     */
    @Api(name = "notice.getNoticeDetail", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    @AuthIgnore
    public Notice getNoticeDetail(Long id){
        if(null == id){
            throw  new ApiException("id不能为空!");
        }
        return noticeService.selectById(id);
    }

}
